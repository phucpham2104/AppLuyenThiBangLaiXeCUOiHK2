package phuc.edu.banglaixe;

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

        loadDeadlyQuestionsFromFirebase();
    }

    private void loadDeadlyQuestionsFromFirebase() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("questions");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                try {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        Boolean isDeadly = child.child("isDeadly").getValue(Boolean.class);
                        if (isDeadly != null && isDeadly) {
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

                            Question q = new Question(id, chapter, questionText, options, answer, explanation);
                            deadlyQuestions.add(q);
                        }
                    }
                    if (!deadlyQuestions.isEmpty()) showQuestion();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                error.toException().printStackTrace();
            }
        });
    }

    private void showQuestion() {
        Question q = deadlyQuestions.get(currentIndex);
        txtQuestionIndex.setText("Câu " + (currentIndex + 1) + "/" + deadlyQuestions.size());
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
