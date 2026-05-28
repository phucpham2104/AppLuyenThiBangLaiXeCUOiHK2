package phuc.edu.banglaixe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WrongQuestionAdapter extends RecyclerView.Adapter<WrongQuestionAdapter.ViewHolder> {

    private final List<Question> wrongQuestions;
    private final Context context;
    private final OnItemClickListener listener;

    // Interface callback để truyền Question ra ngoài
    public interface OnItemClickListener {
        void onItemClick(Question question);
    }

    // Constructor nhận danh sách Question và listener
    public WrongQuestionAdapter(Context context, List<Question> wrongQuestions, OnItemClickListener listener) {
        this.context = context;
        this.wrongQuestions = wrongQuestions;
        this.listener = listener;
    }

    @NonNull
    @Override
    public WrongQuestionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_question, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WrongQuestionAdapter.ViewHolder holder, int position) {
        Question q = wrongQuestions.get(position);
        holder.tvQuestion.setText(q.question);

        // Gọi callback khi click item
        holder.itemView.setOnClickListener(v -> listener.onItemClick(q));
    }

    @Override
    public int getItemCount() {
        return wrongQuestions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuestion;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuestion = itemView.findViewById(R.id.tvQuestion);
        }
    }
}
