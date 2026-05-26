package phuc.edu.banglaixe;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.phuc.edu.banglaixe.R;

import java.util.List;

public class ExamAdapter extends RecyclerView.Adapter<ExamAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(String examId);
    }

    private List<String> exams;
    private OnItemClickListener listener;

    public ExamAdapter(List<String> exams, OnItemClickListener listener) {
        this.exams = exams;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_exam, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String exam = exams.get(position);
        holder.txtExamTitle.setText(exam);
        if (position == 0) {
            holder.imgExamIcon.setVisibility(View.VISIBLE); // icon cho ngẫu nhiên
        } else {
            holder.imgExamIcon.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(v -> listener.onItemClick(exam));
    }

    @Override
    public int getItemCount() {
        return exams.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgExamIcon;
        TextView txtExamTitle;

        ViewHolder(View itemView) {
            super(itemView);
            imgExamIcon = itemView.findViewById(R.id.imgExamIcon);
            txtExamTitle = itemView.findViewById(R.id.txtExamTitle);
        }
    }
}