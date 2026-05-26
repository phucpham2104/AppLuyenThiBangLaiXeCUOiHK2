package phuc.edu.banglaixe;

import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.phuc.edu.banglaixe.model.Question;
import com.phuc.edu.banglaixe.repository.QuestionRepository;
import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private TextView txtQuestionIndex, txtQuestion;
    private Button btnReport, btnNext, btnPrev;
    private RadioGroup rgOptions;

    private List<Question> questions = new ArrayList<>();
    private int currentIndex = 0;
    private String examId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        txtQuestionIndex = findViewById(R.id.txtQuestionIndex);
        txtQuestion = findViewById(R.id.txtQuestion);
        btnReport = findViewById(R.id.btnReport);
        btnNext = findViewById(R.id.btnNext);
        btnPrev = findViewById(R.id.btnPrev);
        rgOptions = findViewById(R.id.rgOptions);

        examId = getIntent().getStringExtra("examId");

        loadQuestions();

        btnNext.setOnClickListener(v -> {
            saveAnswer();
            if (currentIndex < questions.size() - 1) {
                currentIndex++;
                showQuestion();
            }
        });

        btnPrev.setOnClickListener(v -> {
            if (currentIndex > 0) {
                currentIndex--;
                showQuestion();
            }
        });

        btnReport.setOnClickListener(v -> {
            Question q = questions.get(currentIndex);
            Toast.makeText(this, "Đã báo lỗi câu: " + q.id, Toast.LENGTH_SHORT).show();
            // TODO: lưu báo lỗi vào Firebase nếu muốn
        });
    }

    private void loadQuestions() {
        new QuestionRepository().loadAllQuestions(new QuestionRepository.LoadCallback() {
            @Override
            public void onSuccess(List<Question> list) {
                for (Question q : list) {
                    if (examId.equals(q.examId)) {
                        questions.add(q);
                    }
                }
                if (!questions.isEmpty()) showQuestion();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(QuizActivity.this, "Lỗi tải câu hỏi: " + message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showQuestion() {
        Question q = questions.get(currentIndex);
        txtQuestionIndex.setText("Đáp án câu " + (currentIndex + 1) + "/" + questions.size());
        txtQuestion.setText(q.question);

        rgOptions.removeAllViews();
        for (int i = 0; i < q.options.length; i++) {
            RadioButton rb = new RadioButton(this);
            rb.setText(q.options[i]);
            rb.setId(i);
            rgOptions.addView(rb);
        }
    }

    private void saveAnswer() {
        int selectedId = rgOptions.getCheckedRadioButtonId();
        if (selectedId != -1) {
            Question q = questions.get(currentIndex);
            if (selectedId != q.answer) {
                // TODO: lưu câu sai để ôn tập
            }
        }
    }
}