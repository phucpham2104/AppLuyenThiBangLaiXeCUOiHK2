package phuc.edu.banglaixe;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.phuc.edu.banglaixe.R;

import java.util.List;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(String chapterId);
    }

    private List<String> chapters;
    private OnItemClickListener listener;

    public ChapterAdapter(List<String> chapters, OnItemClickListener listener) {
        this.chapters = chapters;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String chapter = chapters.get(position);
        holder.txtTitle.setText(chapter);
        holder.txtProgress.setText("0 / 25 câu hỏi");
        holder.itemView.setOnClickListener(v -> listener.onItemClick(chapter));
    }

    @Override
    public int getItemCount() {
        return chapters.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgChapter;
        TextView txtTitle, txtProgress;

        ViewHolder(View itemView) {
            super(itemView);
            imgChapter = itemView.findViewById(R.id.imgChapter);
            txtTitle = itemView.findViewById(R.id.txtChapterTitle);
            txtProgress = itemView.findViewById(R.id.txtProgress);
        }
    }
}