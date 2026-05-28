package phuc.edu.banglaixe;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class QuestionRepository {

    public interface LoadCallback {
        void onLoaded(List<Question> list);
    }

    public interface ErrorCallback {
        void onError(String message);
    }

    private static final int QUESTIONS_PER_EXAM = 25; // mỗi đề 25 câu

    public void loadAllQuestions(LoadCallback callback, ErrorCallback errorCallback) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("questions");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                try {
                    List<Question> allQuestions = new ArrayList<>();
                    for (DataSnapshot child : snapshot.getChildren()) {
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
                        String image = child.child("image").getValue(String.class);

                        Question q = new Question(id, chapter, questionText, options, answer,
                                explanation != null ? explanation : "", image != null ? image : "");

                        allQuestions.add(q);
                    }

                    // Gán examId cho 5 đề
                    for (int i = 0; i < allQuestions.size(); i++) {
                        if (i < QUESTIONS_PER_EXAM) allQuestions.get(i).setExamId("exam1");
                        else if (i < 2 * QUESTIONS_PER_EXAM) allQuestions.get(i).setExamId("exam2");
                        else if (i < 3 * QUESTIONS_PER_EXAM) allQuestions.get(i).setExamId("exam3");
                        else if (i < 4 * QUESTIONS_PER_EXAM) allQuestions.get(i).setExamId("exam4");
                        else allQuestions.get(i).setExamId("exam5");
                    }

                    callback.onLoaded(allQuestions);

                } catch (Exception e) {
                    errorCallback.onError(e.getMessage());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                errorCallback.onError(error.getMessage());
            }
        });
    }
}