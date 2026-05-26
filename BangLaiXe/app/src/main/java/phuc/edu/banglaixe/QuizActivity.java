package phuc.edu.banglaixe;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private TextView txtQuestionIndex, txtQuestion, txtTimer, txtTip;
    private RadioGroup rgOptions;
    private Button btnNext, btnPrev;

    private List<Question> questions = new ArrayList<>();
    private int currentIndex = 0;
    private String examId;

    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        txtQuestionIndex = findViewById(R.id.txtQuestionIndex);
        txtQuestion = findViewById(R.id.txtQuestion);
        txtTip = findViewById(R.id.txtTip);
        txtTimer = findViewById(R.id.txtTimer);
        rgOptions = findViewById(R.id.rgOptions);
        btnNext = findViewById(R.id.btnNext);
        btnPrev = findViewById(R.id.btnPrev);

        examId = getIntent().getStringExtra("examId");

        showBeginDialog();
    }

    private void showBeginDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Bài thi thử sát hạch lý thuyết lái xe hạng A1")
                .setMessage("- Tổng số câu: 25 câu\n- Thời lượng làm bài: 19 phút\n- Số câu đúng tối thiểu: 21/25\n- Chú ý: Làm sai câu điểm liệt sẽ bị tính là trượt")
                .setPositiveButton("BẮT ĐẦU LÀM BÀI", (dialog, which) -> {
                    loadQuestions();
                    startTimer(19 * 60 * 1000);
                })
                .setCancelable(false)
                .show();
    }

    private void loadQuestions() {
        new QuestionRepository().loadAllQuestions(list -> {
            for (Question q : list) {
                if (examId.equals(q.examId)) questions.add(q);
            }
            if (!questions.isEmpty()) showQuestion();
        }, message -> Toast.makeText(this, "Lỗi tải câu hỏi: " + message, Toast.LENGTH_LONG).show());
    }

    private void startTimer(long millis) {
        timer = new CountDownTimer(millis, 1000) {
            @Override
            public void onTick(long l) {
                int minutes = (int) (l / 1000 / 60);
                int seconds = (int) ((l / 1000) % 60);
                txtTimer.setText(String.format("%02d:%02d", minutes, seconds));
            }

            @Override
            public void onFinish() {
                Toast.makeText(QuizActivity.this, "Hết thời gian!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }.start();
    }

    private void showQuestion() {
        Question q = questions.get(currentIndex);
        txtQuestionIndex.setText((currentIndex + 1) + "/" + questions.size());
        txtQuestion.setText(q.question);
        txtTip.setText(q.tip != null ? q.tip : "");

        rgOptions.removeAllViews();
        for (int i = 0; i < q.options.length; i++) {
            RadioButton rb = new RadioButton(this);
            rb.setText(q.options[i]);
            rb.setId(i);
            rgOptions.addView(rb);
        }

        rgOptions.setOnCheckedChangeListener((group, checkedId) -> {
            saveAnswer(checkedId);
            if (currentIndex < questions.size() - 1) {
                currentIndex++;
                showQuestion();
            } else {
                Toast.makeText(this, "Hoàn tất bài thi", Toast.LENGTH_SHORT).show();
            }
        });

        btnPrev.setOnClickListener(v -> {
            if (currentIndex > 0) {
                currentIndex--;
                showQuestion();
            }
        });

        btnNext.setOnClickListener(v -> {
            if (currentIndex < questions.size() - 1) {
                currentIndex++;
                showQuestion();
            }
        });
    }

    private void saveAnswer(int selectedId) {
        Question q = questions.get(currentIndex);
        if (selectedId != q.answer) {
            // TODO: lưu câu sai
        }
    }
}