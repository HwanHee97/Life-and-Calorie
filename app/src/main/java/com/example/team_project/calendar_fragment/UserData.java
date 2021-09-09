package com.example.team_project.calendar_fragment;

public class UserData {
    private String foodName;
    private String calorie;
    private String date;
    private String weight;
    UserData(){
        this.foodName = "";
        this.calorie = "";
        this.date = "";
        this.weight = "";
    }
    UserData(String foodName, String calorie, String date, String weight){
        this.foodName = foodName;
        this.calorie = calorie;
        this.date = date;
        this.weight = weight;
    }
    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getCalorie() {
        return calorie;
    }

    public void setCalorie(String calorie) {
        this.calorie = calorie;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}
