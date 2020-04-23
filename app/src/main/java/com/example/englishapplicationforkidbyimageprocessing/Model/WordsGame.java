package com.example.englishapplicationforkidbyimageprocessing.Model;

import java.sql.Blob;

public class WordsGame {
    int id;
    String image;
    String sound;
    String meaning;

    public WordsGame(int id,String image, String sound, String meaning) {
        this.id = id;
        this.image = image;
        this.sound = sound;
        this.meaning = meaning;
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

    public String getMeaning() {
        return meaning;
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

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }
}
