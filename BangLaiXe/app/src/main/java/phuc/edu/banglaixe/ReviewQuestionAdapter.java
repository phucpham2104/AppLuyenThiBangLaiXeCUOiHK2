package phuc.edu.banglaixe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

// ReviewQuestionAdapter Firebase-enabled
public class ReviewQuestionAdapter extends RecyclerView.Adapter<ReviewQuestionAdapter.ViewHolder> {

    private List<Question> questions = new ArrayList<>();
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Question question);
    }

    public ReviewQuestionAdapter(Context context, String chapterId, OnItemClickListener listener) {
        this.context = context;
        this.listener = listener;
        loadQuestionsFromFirebase(chapterId);
    }

    private void loadQuestionsFromFirebase(String chapterId) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("questions");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                questions.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    String qChapter = child.child("chapter").getValue(String.class);
                    if (qChapter != null && qChapter.equals(chapterId)) {
                        int id = child.child("id").getValue(Integer.class);
                        String questionText = child.child("question").getValue(String.class);
                        List<String> optionsList = new ArrayList<>();
                        for (DataSnapshot opt : child.child("options").getChildren()) {
                            optionsList.add(opt.getValue(String.class));
                        }
                        String[] options = optionsList.toArray(new String[0]);
                        int answer = child.child("answer").getValue(Integer.class);
                        String image = child.child("image").getValue(String.class);

                        String explanation = child.child("explanation").getValue(String.class);
                        if (explanation == null) explanation = "";

                        String img = image != null ? image : "";

                        Question q = new Question(id, qChapter, questionText, options, answer, explanation, img);
                        questions.add(q);
                    }
                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                error.toException().printStackTrace();
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_question, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Question q = questions.get(position);
        holder.tvQuestion.setText(q.question);
        holder.itemView.setOnClickListener(v -> listener.onItemClick(q));
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuestion;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuestion = itemView.findViewById(R.id.tvQuestion);
        }
    }
}
