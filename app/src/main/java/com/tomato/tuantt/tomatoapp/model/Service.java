package com.tomato.tuantt.tomatoapp.model;

public class Service {
    private int Id;
    private String Name;
    private String Icon;

    public Service() {
    }

    public Service(int id, String name, String icon) {
        Id = id;
        Name = name;
        Icon = icon;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getIcon() {
        return Icon;
    }

    public void setIcon(String icon) {
        Icon = icon;
    }
}
