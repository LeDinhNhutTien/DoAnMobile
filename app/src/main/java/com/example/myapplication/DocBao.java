package com.example.myapplication;

import java.io.Serializable;

public class DocBao implements Serializable {
    public String title;
    public String link;
    public String image;
    public String date;

    public DocBao(String title, String link, String image, String date) {
        this.title = title;
        this.link = link;
        this.image = image;
        this.date = date;
    }
    public DocBao() {

    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return Format.formartDate(date);
    }
}
