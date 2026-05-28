package phuc.edu.banglaixe;

public class Question {

    public int id;
    public String chapter;
    public String question;
    public String[] options;
    public int answer;
    public String explanation;
    public String image;
    public boolean isDeadly;
    public boolean isFrequentlyWrong;
    public String examId;
    public String tip;

    // Constructor mặc định cần cho Firebase
    public Question() {}

    // Constructor đầy đủ 7 tham số
    public Question(int id, String chapter, String question, String[] options, int answer,
                    String explanation, String image) {
        this.id = id;
        this.chapter = chapter;
        this.question = question;
        this.options = options;
        this.answer = answer;
        this.explanation = explanation != null ? explanation : "";
        this.image = image != null ? image : "";
        this.isDeadly = false;
        this.isFrequentlyWrong = false;
        this.examId = "";
        this.tip = null;
    }

    // Constructor 6 tham số cũ để tương thích code trước
    public Question(int id, String chapter, String question, String[] options, int answer, String explanation) {
        this(id, chapter, question, options, answer, explanation, "");
    }

    // Getter / Setter
    public int getId() { return id; }
    public String getChapter() { return chapter; }
    public void setChapter(String chapter) { this.chapter = chapter; }
    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }
    public String[] getOptions() { return options; }
    public void setOptions(String[] options) { this.options = options; }
    public int getAnswer() { return answer; }
    public void setAnswer(int answer) { this.answer = answer; }
    public String getExplanation() { return explanation; }
    public void setExplanation(String explanation) { this.explanation = explanation; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    public boolean isDeadly() { return isDeadly; }
    public void setDeadly(boolean deadly) { this.isDeadly = deadly; }
    public boolean isFrequentlyWrong() { return isFrequentlyWrong; }
    public void setFrequentlyWrong(boolean frequentlyWrong) { this.isFrequentlyWrong = frequentlyWrong; }
    public String getExamId() { return examId; }
    public void setExamId(String examId) { this.examId = examId; }
    public String getTip() { return tip; }
    public void setTip(String tip) { this.tip = tip; }
}