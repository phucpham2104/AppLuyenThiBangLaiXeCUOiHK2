package phuc.edu.banglaixe;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private TextView txtQuestionIndex, txtQuestion;
    private LinearLayout layoutOptions;
    private Button btnPrev, btnNext, btnSubmit;

    private List<Question> questions = new ArrayList<>();
    private int currentIndex = 0;
    private HashMap<Integer, Integer> userAnswers = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        txtQuestionIndex = findViewById(R.id.txtQuestionIndex);
        txtQuestion = findViewById(R.id.txtQuestion);
        layoutOptions = findViewById(R.id.layoutOptions);
        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);
        btnSubmit = findViewById(R.id.btnSubmit);

        loadQuestions();

        btnPrev.setOnClickListener(v -> {
            if (currentIndex > 0) currentIndex--;
            showQuestion();
        });

        btnNext.setOnClickListener(v -> {
            if (currentIndex < questions.size() - 1) currentIndex++;
            showQuestion();
        });

        btnSubmit.setOnClickListener(v -> showSubmitDialog());
    }

    private void loadQuestions() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("questions");
        ref.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                questions.clear();
                int count = 0;
                for (DataSnapshot child : snapshot.getChildren()) {
                    if (count >= 25) break;
                    int id = child.child("id").getValue(Integer.class);
                    String chapter = child.child("chapter").getValue(String.class);
                    String questionText = child.child("question").getValue(String.class);
                    List<String> opts = new ArrayList<>();
                    for (DataSnapshot opt : child.child("options").getChildren()) {
                        opts.add(opt.getValue(String.class));
                    }
                    String[] options = opts.toArray(new String[0]);
                    int answer = child.child("answer").getValue(Integer.class);
                    String explanation = child.child("explanation").getValue(String.class);
                    if (explanation == null) explanation = "";
                    questions.add(new Question(id, chapter, questionText, options, answer, explanation, ""));
                    count++;
                }
                if (!questions.isEmpty()) showQuestion();
            }
            @Override
            public void onCancelled(DatabaseError error) {}
        });
    }

    private void showQuestion() {
        Question q = questions.get(currentIndex);
        txtQuestionIndex.setText((currentIndex + 1) + "/" + questions.size());
        txtQuestion.setText(q.getQuestion());
        layoutOptions.removeAllViews();

        for (int i = 0; i < q.getOptions().length; i++) {
            String opt = q.getOptions()[i];
            LinearLayout optionCard = new LinearLayout(this);
            optionCard.setOrientation(LinearLayout.HORIZONTAL);
            optionCard.setPadding(16,16,16,16);
            optionCard.setBackgroundResource(R.drawable.option_card_bg); // viền xám, nền trắng
            optionCard.setGravity(Gravity.CENTER_VERTICAL);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0,8,0,8);
            optionCard.setLayoutParams(params);

            // Circle tick
            ImageView circle = new ImageView(this);
            circle.setImageResource(userAnswers.getOrDefault(currentIndex, -1) == i ?
                    R.drawable.ic_radio_selected : R.drawable.ic_radio_unselected);
            LinearLayout.LayoutParams circleParams = new LinearLayout.LayoutParams(50,50);
            circle.setLayoutParams(circleParams);

            // Text
            TextView tvOpt = new TextView(this);
            tvOpt.setText(opt);
            tvOpt.setTextColor(Color.BLACK);
            tvOpt.setTextSize(16);
            tvOpt.setPadding(16,0,0,0);
            tvOpt.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            optionCard.addView(circle);
            optionCard.addView(tvOpt);

            int index = i;
            optionCard.setOnClickListener(v -> {
                userAnswers.put(currentIndex, index);
                showQuestion(); // refresh highlight
            });

            layoutOptions.addView(optionCard);
        }
    }
    private void showSubmitDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Nộp bài")
                .setMessage("Bạn có chắc chắn muốn nộp bài và xem kết quả?")
                .setPositiveButton("OK", (dialog, which) -> showResult())
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void showResult() {
        int correctCount = 0;
        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            Integer ans = userAnswers.get(i);
            if (ans != null && ans == q.getAnswer()) correctCount++;
        }

        String resultMsg = "Bạn làm đúng " + correctCount + " / " + questions.size() + " câu.";
        new AlertDialog.Builder(this)
                .setTitle(correctCount >= 20 ? "ĐẬU" : "TRƯỢT")
                .setMessage(resultMsg)
                .setPositiveButton("OK", null)
                .show();
    }
}