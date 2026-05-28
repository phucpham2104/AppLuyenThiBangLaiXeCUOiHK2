// ChapterAdapter.java Firebase-enabled
package phuc.edu.banglaixe;

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

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(String chapterId);
    }

    private List<String> chapters = new ArrayList<>();
    private OnItemClickListener listener;

    public ChapterAdapter(OnItemClickListener listener) {
        this.listener = listener;
        loadChaptersFromFirebase();
    }

    private void loadChaptersFromFirebase() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("questions");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chapters.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    String chapter = child.child("chapter").getValue(String.class);
                    if (chapter != null && !chapters.contains(chapter)) {
                        chapters.add(chapter);
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
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
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

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgChapter = itemView.findViewById(R.id.imgChapter);
            txtTitle = itemView.findViewById(R.id.txtChapterTitle);
            txtProgress = itemView.findViewById(R.id.txtProgress);
        }
    }
}