package phuc.edu.banglaixe;

import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class DeadlyQuestionsActivity extends AppCompatActivity {

    private TextView txtQuestionIndex, txtQuestion, txtExplanation;
    private RadioGroup rgOptions;
    private Button btnNext, btnPrev;

    private List<Question> deadlyQuestions = new ArrayList<>();
    private int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deadline_questions);

        txtQuestionIndex = findViewById(R.id.txtQuestionIndex);
        txtQuestion = findViewById(R.id.txtQuestion);
        txtExplanation = findViewById(R.id.txtExplanation);
        rgOptions = findViewById(R.id.rgOptions);
        btnNext = findViewById(R.id.btnNext);
        btnPrev = findViewById(R.id.btnPrev);

        loadDeadlyQuestions();
    }

    private void loadDeadlyQuestions() {
        new QuestionRepository().loadAllQuestions(list -> {
            // Lọc ra 20 câu điểm liệt
            for (Question q : list) {
                if (q.isDeadly) deadlyQuestions.add(q);
            }
            if (!deadlyQuestions.isEmpty()) showQuestion();
        }, message -> {
            // lỗi tải
        });
    }

    private void showQuestion() {
        Question q = deadlyQuestions.get(currentIndex);
        txtQuestionIndex.setText("Câu " + (currentIndex + 1) + "/" + deadlyQuestions.size());
        txtQuestion.setText(q.question);
        txtExplanation.setVisibility(View.GONE);

        rgOptions.removeAllViews();
        for (int i = 0; i < q.options.length; i++) {
            RadioButton rb = new RadioButton(this);
            rb.setText(q.options[i]);
            rb.setId(i);
            rgOptions.addView(rb);
        }

        rgOptions.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == q.answer) {
                ((RadioButton) group.getChildAt(checkedId))
                        .setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            } else {
                ((RadioButton) group.getChildAt(checkedId))
                        .setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                ((RadioButton) group.getChildAt(q.answer))
                        .setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            }
            txtExplanation.setText(q.explanation);
            txtExplanation.setVisibility(android.view.View.VISIBLE);
        });

        btnPrev.setOnClickListener(v -> {
            if (currentIndex > 0) {
                currentIndex--;
                showQuestion();
            }
        });

        btnNext.setOnClickListener(v -> {
            if (currentIndex < deadlyQuestions.size() - 1) {
                currentIndex++;
                showQuestion();
            }
        });
    }
}