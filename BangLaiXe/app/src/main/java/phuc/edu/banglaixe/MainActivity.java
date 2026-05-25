package phuc.edu.banglaixe;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnThiBoDe).setOnClickListener(v -> startActivity(new Intent(this, SelectExamActivity.class)));
        findViewById(R.id.btnXemCauSai).setOnClickListener(v -> startActivity(new Intent(this, WrongQuestionsActivity.class)));
        findViewById(R.id.btnOnTapCauHoi).setOnClickListener(v -> startActivity(new Intent(this, ReviewQuestionsActivity.class)));
        findViewById(R.id.btnBienBao).setOnClickListener(v -> startActivity(new Intent(this, TrafficSignsActivity.class)));
        findViewById(R.id.btn50CauSai).setOnClickListener(v -> startActivity(new Intent(this, TopWrongQuestionsActivity.class)));
        findViewById(R.id.btn20CauDiemLiet).setOnClickListener(v -> startActivity(new Intent(this, CriticalQuestionsActivity.class)));
        findViewById(R.id.btnMeoThi).setOnClickListener(v -> startActivity(new Intent(this, TipsActivity.class)));
        findViewById(R.id.btnThaoLuan).setOnClickListener(v -> startActivity(new Intent(this, DiscussionActivity.class)));
    }
}