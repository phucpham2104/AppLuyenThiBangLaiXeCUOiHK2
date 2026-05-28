package phuc.edu.banglaixe;

public class Question {
    public int id;
    public String chapterId;
    public String question;
    public String[] options;
    public int answer;
    public String explanation;
    public boolean isDeadly;
    public boolean isFrequentlyWrong;
    public String examId;
    public String tip;

    public Question(int id, String chapterId, String question, String[] options, int answer, String explanation) {
        this.id = id;
        this.chapterId = chapterId;
        this.question = question;
        this.options = options;
        this.answer = answer;
        this.explanation = explanation;
        this.isDeadly = false;
        this.isFrequentlyWrong = false;
        this.examId = "";
        this.tip = null;
    }
    public int getId() {
        return id;
    }
}