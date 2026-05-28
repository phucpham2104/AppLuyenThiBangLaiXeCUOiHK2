package phuc.edu.banglaixe;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class SelectExamActivity extends AppCompatActivity {

    private LinearLayout btnExam1, btnExam2, btnExam3, btnExam4, btnExam5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_exam);

        // Map các nút trong layout
        btnExam1 = findViewById(R.id.btnExam1);
        btnExam2 = findViewById(R.id.btnExam2);
        btnExam3 = findViewById(R.id.btnExam3);
        btnExam4 = findViewById(R.id.btnExam4);
        btnExam5 = findViewById(R.id.btnExam5);

        // Gán sự kiện click
        btnExam1.setOnClickListener(v -> startQuiz("exam_random"));
        btnExam2.setOnClickListener(v -> startQuiz("exam_1"));
        btnExam3.setOnClickListener(v -> startQuiz("exam_2"));
        btnExam4.setOnClickListener(v -> startQuiz("exam_3"));
        btnExam5.setOnClickListener(v -> startQuiz("exam_4"));
    }

    // Phương thức mở QuizActivity với examId
    private void startQuiz(String examId) {
        Intent i = new Intent(SelectExamActivity.this, QuizActivity.class);
        i.putExtra("examId", examId);
        startActivity(i);
    }
}