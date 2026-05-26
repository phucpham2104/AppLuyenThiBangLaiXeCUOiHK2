package phuc.edu.banglaixe;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.phuc.edu.banglaixe.model.Question;
import com.phuc.edu.banglaixe.repository.QuestionRepository;

import java.util.ArrayList;
import java.util.List;

public class DeadlyQuestionsListActivity extends AppCompatActivity {

    private RecyclerView rvDeadlyQuestions;
    private List<Question> deadlyQuestions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deadly_questions_list);

        rvDeadlyQuestions = findViewById(R.id.rvDeadlyQuestions);
        rvDeadlyQuestions.setLayoutManager(new LinearLayoutManager(this));

        loadDeadlyQuestions();
    }

    private void loadDeadlyQuestions() {
        new QuestionRepository().loadAllQuestions(list -> {
            for (Question q : list) {
                if (q.isDeadly) deadlyQuestions.add(q);
            }
            DeadlyQuestionAdapter adapter = new DeadlyQuestionAdapter(deadlyQuestions, question -> {
                // Mở ReviewQuestionActivity cho câu được click
                Intent i = new Intent(DeadlyQuestionsListActivity.this, ReviewQuestionActivity.class);
                i.putExtra("questionId", question.id);
                startActivity(i);
            });
            rvDeadlyQuestions.setAdapter(adapter);
        }, message -> {
            // lỗi tải dữ liệu
        });
    }
}