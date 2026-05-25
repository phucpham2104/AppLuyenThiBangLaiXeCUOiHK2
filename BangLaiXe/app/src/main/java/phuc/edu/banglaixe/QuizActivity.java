package phuc.edu.banglaixe;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class QuizActivity extends AppCompatActivity {

    private List<Question> questions;
    private int currentIndex = 0;
    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        String chapterId = getIntent().getStringExtra("chapterId");
        loadQuestionsFromJSON(chapterId);

        Button btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(v -> submitAnswer());
    }

    private void loadQuestionsFromJSON(String chapterId){
        try{
            InputStream is = getAssets().open("50_cau_5chuong_docx_final.json");
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, Map<String, Question>>>(){}.getType();
            Map<String, Map<String, Question>> allData = gson.fromJson(new InputStreamReader(is), type);
            Map<String, Question> chapterQuestions = allData.get(chapterId);
            questions = List.copyOf(chapterQuestions.values());
            showQuestion();
        }catch(Exception e){
            e.printStackTrace();
            Toast.makeText(this,"Không load được câu hỏi",Toast.LENGTH_SHORT).show();
        }
    }

    private void showQuestion(){
        if(currentIndex >= questions.size()) return;

        Question q = questions.get(currentIndex);
        TextView tvQ = findViewById(R.id.tvQuestion);
        tvQ.setText(q.question);

        RadioGroup rg = findViewById(R.id.optionsGroup);
        rg.removeAllViews();
        for(String opt : q.options){
            RadioButton rb = new RadioButton(this);
            rb.setText(opt);
            rg.addView(rb);
        }

        ImageView iv = findViewById(R.id.ivQuestion);
        int resId = getResources().getIdentifier(q.image,"drawable",getPackageName());
        if(resId != 0) iv.setImageResource(resId);
        else iv.setImageResource(0);
    }

    private void submitAnswer(){
        RadioGroup rg = findViewById(R.id.optionsGroup);
        int checkedId = rg.getCheckedRadioButtonId();
        if(checkedId != -1){
            RadioButton rb = findViewById(checkedId);
            Question q = questions.get(currentIndex);
            if(rb.getText().toString().equals(q.answer)) score++;
            currentIndex++;
            if(currentIndex < questions.size()) showQuestion();
            else showResult();
        }else{
            Toast.makeText(this,"Chọn 1 đáp án",Toast.LENGTH_SHORT).show();
        }
    }

    private void showResult(){
        new AlertDialog.Builder(this)
                .setTitle("Kết quả")
                .setMessage("Bạn đúng "+score+"/"+questions.size()+" câu")
                .setPositiveButton("OK",(d,w)->finish())
                .show();
    }
}