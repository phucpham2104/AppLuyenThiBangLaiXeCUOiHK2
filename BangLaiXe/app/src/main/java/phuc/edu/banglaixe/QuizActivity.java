package phuc.edu.banglaixe;

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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Bài thi thử sát hạch lý thuyết lái xe hạng A1")
                .setMessage("- Tổng số câu: 25 câu\n- Thời lượng làm bài: 19 phút\n- Số câu đúng tối thiểu: 21/25\n- Chú ý: Làm sai câu điểm liệt sẽ bị tính là trượt")
                .setPositiveButton("BẮT ĐẦU LÀM BÀI", (dialog, which) -> {
                    loadQuestionsFromFirebase();
                    startTimer(19 * 60 * 1000);
                })
                .setCancelable(false)
                .show();
    }

    private void loadQuestionsFromFirebase() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("questions");
        ref.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                questions.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    String qExamId = child.child("examId").getValue(String.class);
                    if (qExamId != null && qExamId.equals(examId)) {
                        int id = child.child("id").getValue(Integer.class);
                        String chapter = child.child("chapter").getValue(String.class);
                        String questionText = child.child("question").getValue(String.class);
                        List<String> optionsList = new ArrayList<>();
                        for (DataSnapshot opt : child.child("options").getChildren()) {
                            optionsList.add(opt.getValue(String.class));
                        }
                        String[] options = optionsList.toArray(new String[0]);
                        int answer = child.child("answer").getValue(Integer.class);
                        String explanation = child.child("explanation").getValue(String.class);
                        String tip = child.child("tip").getValue(String.class);
                        String image = child.child("image").getValue(String.class);

                        Question q = new Question(id, chapter, questionText, options, answer,
                                explanation != null ? explanation : "",
                                image != null ? image : "");
                        q.setTip(tip != null ? tip : "");

                        questions.add(q);
                    }
                }

                if (!questions.isEmpty()) showQuestion();
                else Toast.makeText(QuizActivity.this, "Không có câu hỏi cho đề này", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(QuizActivity.this, "Lỗi tải câu hỏi: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
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
        txtTip.setText(q.getTip());

        rgOptions.removeAllViews();
        for (int i = 0; i < q.options.length; i++) {
            RadioButton rb = new RadioButton(this);
            rb.setText(q.options[i]);
            rb.setId(i);
            rgOptions.addView(rb);
        }

        rgOptions.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId != q.answer) {
                // TODO: lưu câu sai nếu muốn
            }

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
}