package phuc.edu.banglaixe;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

public class SelectExamActivity extends AppCompatActivity {

    private LinearLayout btnRandom, btnExam1, btnExam2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_exam);

        btnRandom = findViewById(R.id.btnRandom);
        btnExam1 = findViewById(R.id.btnExam1);
        btnExam2 = findViewById(R.id.btnExam2);

        btnRandom.setOnClickListener(v -> startQuiz("random"));
        btnExam1.setOnClickListener(v -> startQuiz("exam1"));
        btnExam2.setOnClickListener(v -> startQuiz("exam2"));

    }

    private void startQuiz(String examId) {
        Intent i = new Intent(SelectExamActivity.this, QuizActivity.class);
        i.putExtra("examId", examId);
        startActivity(i);
    }
}