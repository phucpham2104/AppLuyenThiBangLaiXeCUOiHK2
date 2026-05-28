package phuc.edu.banglaixe;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ViewHolder> {

    private final List<String> chapters;
    private final OnChapterClickListener listener;

    // Callback interface khi click chapter
    public interface OnChapterClickListener {
        void onClick(String chapterId);
    }

    public ChapterAdapter(List<String> chapters, OnChapterClickListener listener) {
        this.chapters = chapters;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ChapterAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterAdapter.ViewHolder holder, int position) {
        String chapterId = chapters.get(position);

        // Set tên chapter
        holder.txtChapterTitle.setText(chapterId);

        // Nếu muốn hiển thị tiến độ, có thể cập nhật sau:
        holder.txtProgress.setText("0 / 25 câu hỏi");

        holder.itemView.setOnClickListener(v -> listener.onClick(chapterId));
    }

    @Override
    public int getItemCount() {
        return chapters.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtChapterTitle;
        TextView txtProgress;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtChapterTitle = itemView.findViewById(R.id.txtChapterTitle);
            txtProgress = itemView.findViewById(R.id.txtProgress);
        }
    }
}