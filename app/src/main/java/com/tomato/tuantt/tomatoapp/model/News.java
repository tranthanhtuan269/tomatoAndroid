package com.tomato.tuantt.tomatoapp.model;

public class News {
    private int Id;
    private String Title;
    private String Year;

    public News() {

    }

    public News(int id, String title, String year) {
        Id = id;
        Title = title;
        Year = year;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }
}
