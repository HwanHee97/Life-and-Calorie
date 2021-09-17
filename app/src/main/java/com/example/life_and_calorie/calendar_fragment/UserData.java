package com.example.life_and_calorie.calendar_fragment;

public class UserData {
    private String foodName;
    private String calorie;
    private String date;

    UserData(){
        this.foodName = "";
        this.calorie = "";
        this.date = "";

    }
    UserData(String foodName, String calorie, String date){
        this.foodName = foodName;
        this.calorie = calorie;
        this.date = date;

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

}
