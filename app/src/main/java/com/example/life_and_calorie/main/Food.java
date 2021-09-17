package com.example.life_and_calorie.main;

public class Food {
    String foodname;//음식명
    String foodkcal;//음식칼로리
    boolean checked;//즐겨찾기 여부

    public Food(String foodname, String foodkcal, boolean checked) {
        this.foodname = foodname;
        this.foodkcal = foodkcal;
        this.checked = checked;
    }

    public String getName() {
        return foodname;
    }

    public void setName(String foodname) {
        this.foodname = foodname;
    }

    public String getKcal() {
        return foodkcal;
    }

    public void setKcal(String foodkcal) {
        this.foodkcal = foodkcal;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
