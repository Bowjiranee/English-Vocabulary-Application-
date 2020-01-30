package com.example.englishapplicationforkidbyimageprocessing.Model;

public class WordsGame {
    int id;
    String image;
    String sound;
    String score;

    public WordsGame(int id,String image, String sound, String score) {
        this.id = id;
        this.image = image;
        this.sound = sound;
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getSound() {
        return sound;
    }

    public String getScore() {
        return score;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
