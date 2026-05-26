package phuc.edu.banglaixe;

import java.util.ArrayList;
import java.util.List;

public class QuestionRepository {

    public interface LoadCallback {
        void onLoaded(List<Question> list);
    }

    public interface ErrorCallback {
        void onError(String message);
    }

    // Thí dụ đơn giản: load hardcode, sau này bạn có thể đổi sang load từ JSON
    public void loadAllQuestions(LoadCallback callback, ErrorCallback errorCallback) {
        try {
            List<Question> questions = new ArrayList<>();

            // Ví dụ 1 câu
            questions.add(new Question(1, "all", "Hành vi nào sau đây bị nghiêm cấm?",
                    new String[]{
                            "Điều khiển xe lạng lách",
                            "Chống đối người thi hành công vụ",
                            "Cả hai"
                    }, 2, "Theo luật GTĐB 2024"));

            callback.onLoaded(questions);
        } catch (Exception e) {
            errorCallback.onError(e.getMessage());
        }
    }
}