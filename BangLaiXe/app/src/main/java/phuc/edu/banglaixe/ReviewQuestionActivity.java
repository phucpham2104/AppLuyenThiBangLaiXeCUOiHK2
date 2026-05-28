package phuc.edu.banglaixe;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

// ReviewQuestionActivity Firebase-enabled
public class ReviewQuestionActivity extends AppCompatActivity {

    private TextView txtQuestionIndex, txtQuestion, txtExplanation;
    private RadioGroup rgOptions;
    private Button btnNext, btnPrev;

    private List<Question> questions = new ArrayList<>();
    private int currentIndex = 0;
    private String chapterId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_question);

        txtQuestionIndex = findViewById(R.id.txtQuestionIndex);
        txtQuestion = findViewById(R.id.txtQuestion);
        txtExplanation = findViewById(R.id.txtExplanation);
        rgOptions = findViewById(R.id.rgOptions);
        btnNext = findViewById(R.id.btnNext);
        btnPrev = findViewById(R.id.btnPrev);

        chapterId = getIntent().getStringExtra("chapterId");

        loadQuestionsFromFirebase();
    }

    private void loadQuestionsFromFirebase() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("questions");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    String qChapter = child.child("chapter").getValue(String.class);
                    if (qChapter != null && (qChapter.equals(chapterId) || chapterId.equals("all"))) {
                        int id = child.child("id").getValue(Integer.class);
                        String questionText = child.child("question").getValue(String.class);
                        List<String> optionsList = new ArrayList<>();
                        for (DataSnapshot opt : child.child("options").getChildren()) {
                            optionsList.add(opt.getValue(String.class));
                        }
                        String[] options = optionsList.toArray(new String[0]);
                        int answer = child.child("answer").getValue(Integer.class);
                        String explanation = child.child("explanation").getValue(String.class);

                        String image = child.child("image").getValue(String.class);
                        if (image == null) image = "";

                        Question q = new Question(id, qChapter, questionText, options, answer, explanation != null ? explanation : "", image);
                        questions.add(q);
                    }
                }

                if (!questions.isEmpty()) showQuestion();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                error.toException().printStackTrace();
            }
        });
    }

    private void showQuestion() {
        Question q = questions.get(currentIndex);
        txtQuestionIndex.setText("Câu " + (currentIndex + 1) + "/" + questions.size());
        txtQuestion.setText(q.question);
        txtExplanation.setVisibility(android.view.View.GONE);

        rgOptions.removeAllViews();
        for (int i = 0; i < q.options.length; i++) {
            RadioButton rb = new RadioButton(this);
            rb.setText(q.options[i]);
            rb.setId(i);
            rgOptions.addView(rb);
        }

        rgOptions.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == q.answer) {
                ((RadioButton) group.getChildAt(checkedId)).setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            } else {
                ((RadioButton) group.getChildAt(checkedId)).setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                ((RadioButton) group.getChildAt(q.answer)).setTextColor(getResources().getColor(android.R.color.holo_green_dark));
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
            if (currentIndex < questions.size() - 1) {
                currentIndex++;
                showQuestion();
            }
        });
    }
}