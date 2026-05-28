// WrongQuestionsListActivity.java Firebase-enabled
package phuc.edu.banglaixe;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WrongQuestionsListActivity extends AppCompatActivity {

    private RecyclerView rvWrongQuestions;
    private List<Question> wrongQuestions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrong_questions_list);

        rvWrongQuestions = findViewById(R.id.rvWrongQuestions);
        rvWrongQuestions.setLayoutManager(new LinearLayoutManager(this));

        loadWrongQuestionsFromFirebase();
    }

    private void loadWrongQuestionsFromFirebase() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("questions");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                wrongQuestions.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Boolean isFrequentlyWrong = child.child("isFrequentlyWrong").getValue(Boolean.class);
                    if (isFrequentlyWrong != null && isFrequentlyWrong) {
                        int id = child.child("id").getValue(Integer.class);
                        String chapter = child.child("chapter").getValue(String.class);
                        String questionText = child.child("question").getValue(String.class);
                        List<String> optionsList = new ArrayList<>();
                        for (DataSnapshot opt : child.child("options").getChildren()) {
                            optionsList.add(opt.getValue(String.class));
                        }
                        String[] options = optionsList.toArray(new String[0]);
                        int answer = child.child("answer").getValue(Integer.class);
                        String tip = child.child("tip").getValue(String.class);

                        String explanation = ""; // hoặc lấy từ Firebase nếu có
                        String image = ""; // hoặc lấy từ Firebase nếu có

                        Question q = new Question(id, chapter, questionText, options, answer,
                                explanation, image);
                        q.setTip(tip != null ? tip : "");
                        wrongQuestions.add(q);
                    }
                }

                WrongQuestionAdapter adapter = new WrongQuestionAdapter(WrongQuestionsListActivity.this, wrongQuestions, question -> {
                    Intent i = new Intent(WrongQuestionsListActivity.this, ReviewQuestionActivity.class);
                    i.putExtra("questionId", question.getId());
                    startActivity(i);
                });
                rvWrongQuestions.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                error.toException().printStackTrace();
            }
        });
    }
}
