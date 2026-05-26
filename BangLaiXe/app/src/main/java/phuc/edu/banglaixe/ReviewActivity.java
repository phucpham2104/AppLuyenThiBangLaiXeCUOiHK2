package phuc.edu.banglaixe;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ReviewActivity extends AppCompatActivity {

    private RecyclerView rvChapters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        rvChapters = findViewById(R.id.rvChapters);
        rvChapters.setLayoutManager(new LinearLayoutManager(this));

        List<String> chapterList = new ArrayList<>();
        chapterList.add("Toàn bộ câu hỏi");
        chapterList.add("Quy định chung và quy tắc giao thông đường bộ");
        chapterList.add("Văn hóa giao thông, đạo đức người lái xe, kỹ năng phòng cháy");
        chapterList.add("Kỹ thuật lái xe");
        chapterList.add("Báo hiệu đường bộ");

        ChapterAdapter adapter = new ChapterAdapter(chapterList, chapterId -> {
            Intent i = new Intent(ReviewActivity.this, QuizActivity.class);
            i.putExtra("chapterId", chapterId);
            i.putExtra("isReviewMode", true); // để QuizActivity biết là ôn tập, không tính thời gian
            startActivity(i);
        });

        rvChapters.setAdapter(adapter);
    }
}