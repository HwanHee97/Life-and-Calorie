package com.example.test_db2;

public class Food {
    String name;
    int cal;
    String source;//출처

    public Food(String name, int cal,String source) {
        this.name = name;
        this.cal = cal;
        this.source=source;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCal() {
        return cal;
    }

    public void setCal(int cal) {
        this.cal = cal;
    }
}