package phuc.edu.banglaixe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private LinearLayout btnExam, btnReview, btn50Wrong, btnCritical;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnExam = findViewById(R.id.btnExam);
        btnReview = findViewById(R.id.btnReview);
        btn50Wrong = findViewById(R.id.btn50Wrong);
        btnCritical = findViewById(R.id.btnCritical);

        btnExam.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, SelectExamActivity.class);
            i.putExtra("mode", "exam");
            startActivity(i);
        });

        btnReview.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, SelectExamActivity.class);
            i.putExtra("mode", "review");
            startActivity(i);
        });

        btn50Wrong.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, WrongQuestionsActivity.class);
            startActivity(i);
        });

        btnCritical.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, CriticalQuestionsActivity.class);
            startActivity(i);
        });
    }
}