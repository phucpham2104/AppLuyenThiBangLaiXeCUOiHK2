package phuc.edu.banglaixe;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class WrongQuestionsListActivity extends AppCompatActivity {

    private RecyclerView rvWrongQuestions;
    private List<Question> wrongQuestions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrong_questions_list);

        rvWrongQuestions = findViewById(R.id.rvWrongQuestions);
        rvWrongQuestions.setLayoutManager(new LinearLayoutManager(this));

        loadWrongQuestions();
    }

    private void loadWrongQuestions() {
        new QuestionRepository().loadAllQuestions(list -> {
            for (Question q : list) {
                if (q.isFrequentlyWrong) wrongQuestions.add(q);
            }
            WrongQuestionAdapter adapter = new WrongQuestionAdapter(wrongQuestions, question -> {
                // mở ReviewQuestionActivity
                Intent i = new Intent(WrongQuestionsListActivity.this, ReviewQuestionActivity.class);
                i.putExtra("questionId", question.id);
                startActivity(i);
            });
            rvWrongQuestions.setAdapter(adapter);
        }, message -> {
            // lỗi tải dữ liệu
        });
    }
}