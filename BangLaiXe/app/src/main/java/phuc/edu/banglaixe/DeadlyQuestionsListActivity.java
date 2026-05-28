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

public class DeadlyQuestionsListActivity extends AppCompatActivity {

    private RecyclerView rvDeadlyQuestions;
    private List<Question> deadlyQuestions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deadly_questions_list);

        rvDeadlyQuestions = findViewById(R.id.rvDeadlyQuestions);
        rvDeadlyQuestions.setLayoutManager(new LinearLayoutManager(this));

        loadDeadlyQuestionsFromFirebase();
    }

    private void loadDeadlyQuestionsFromFirebase() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("questions");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                deadlyQuestions.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Boolean isDeadly = child.child("isDeadly").getValue(Boolean.class);
                    if (isDeadly != null && isDeadly) {
                        int id = child.child("id").getValue(Integer.class);
                        String chapterId = child.child("chapterId").getValue(String.class);
                        String questionText = child.child("question").getValue(String.class);

                        List<String> optionsList = new ArrayList<>();
                        for (DataSnapshot opt : child.child("options").getChildren()) {
                            optionsList.add(opt.getValue(String.class));
                        }
                        String[] options = optionsList.toArray(new String[0]);

                        int answer = child.child("answer").getValue(Integer.class);
                        String explanation = child.child("explanation").getValue(String.class);
                        boolean isFrequentlyWrong = child.child("isFrequentlyWrong").getValue(Boolean.class) != null ? child.child("isFrequentlyWrong").getValue(Boolean.class) : false;
                        String examId = child.child("examId").getValue(String.class);
                        String tip = child.child("tip").getValue(String.class);

                        String chapter = child.child("chapter").getValue(String.class); // match JSON
                        String image = child.child("image").getValue(String.class);     // nếu JSON có image
                        if (image == null) image = "";

                        Question q = new Question(id, chapter, questionText, options, answer, explanation, image);
                        q.isDeadly = true;
                        q.isFrequentlyWrong = isFrequentlyWrong;
                        q.examId = examId != null ? examId : "";
                        q.tip = tip;

                        deadlyQuestions.add(q);
                    }
                }

                DeadlyQuestionAdapter adapter = new DeadlyQuestionAdapter(DeadlyQuestionsListActivity.this, deadlyQuestions, question -> {
                    Intent i = new Intent(DeadlyQuestionsListActivity.this, ReviewQuestionActivity.class);
                    i.putExtra("questionId", question.getId());
                    startActivity(i);
                });
                rvDeadlyQuestions.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                error.toException().printStackTrace();
            }
        });
    }
}
