package com.example.englishapplicationforkidbyimageprocessing.Model;

public class QuestionsGame {
    private int id;
    private String question;
    String opta;
    String optb;
    String optc;
    private String answer;

    public QuestionsGame(int id, String question, String opta, String optb, String optc, String answer) {
        this.id = id;
        this.question = question;
        this.opta = opta;
        this.optb = optb;
        this.optc = optc;
        this.answer = answer;
    }

    public QuestionsGame(){
        id = 0;
        question = "";
        opta = "";
        optb = "";
        optc = "";
        answer = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOpta() {
        return opta;
    }

    public void setOpta(String opta) {
        this.opta = opta;
    }

    public String getOptb() {
        return optb;
    }

    public void setOptb(String optb) {
        this.optb = optb;
    }

    public String getOptc() {
        return optc;
    }

    public void setOptc(String optc) {
        this.optc = optc;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
