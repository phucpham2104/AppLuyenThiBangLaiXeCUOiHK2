package phuc.edu.banglaixe;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

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

// ReviewActivity Firebase-enabled
public class ReviewActivity extends AppCompatActivity {

    private RecyclerView rvChapters;
    private List<String> chapterList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        rvChapters = findViewById(R.id.rvChapters);
        rvChapters.setLayoutManager(new LinearLayoutManager(this));

        loadChaptersFromFirebase();
    }

    private void loadChaptersFromFirebase() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("questions");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<String> chapters = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    String chapter = child.child("chapter").getValue(String.class);
                    if (chapter != null && !chapters.contains(chapter)) {
                        chapters.add(chapter);
                    }
                }

                chapterList.addAll(chapters);

                ChapterAdapter adapter = new ChapterAdapter(chapterList, chapterId -> {
                    Intent i = new Intent(ReviewActivity.this, ReviewQuestionActivity.class);
                    i.putExtra("chapterId", chapterId);
                    startActivity(i);
                });
                rvChapters.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                error.toException().printStackTrace();
            }
        });
    }
}