package phuc.edu.banglaixe;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class DeadlyQuestionAdapter extends RecyclerView.Adapter<DeadlyQuestionAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Question question);
    }

    private List<Question> questions;
    private OnItemClickListener listener;

    public DeadlyQuestionAdapter(List<Question> questions, OnItemClickListener listener) {
        this.questions = questions;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_deadly_question, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Question q = questions.get(position);
        holder.txtQuestionNumber.setText("Câu hỏi số " + q.id);
        holder.txtQuestionTitle.setText(q.question);
        holder.itemView.setOnClickListener(v -> listener.onItemClick(q));
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtQuestionNumber, txtQuestionTitle, imgIcon;

        ViewHolder(View itemView) {
            super(itemView);
            txtQuestionNumber = itemView.findViewById(R.id.txtQuestionNumber);
            txtQuestionTitle = itemView.findViewById(R.id.txtQuestionTitle);
            imgIcon = itemView.findViewById(R.id.imgIcon);
        }
    }
}