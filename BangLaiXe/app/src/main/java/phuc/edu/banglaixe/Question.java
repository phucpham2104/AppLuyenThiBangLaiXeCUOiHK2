package phuc.edu.banglaixe;

import java.util.List;

public class Question {
    public String category;
    public String question;
    public List<String> options;
    public String answer;
    public String image;

    public Question() {} // required for Firebase

    public Question(String category, String question, List<String> options, String answer, String image){
        this.category = category;
        this.question = question;
        this.options = options;
        this.answer = answer;
        this.image = image;
    }
}