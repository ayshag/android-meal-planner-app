package com.example.fitnessapp;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class Food implements Serializable {

    public String name;
    public String calories;
    public String carbohydrates;
    public String protein;
    public  String category;
    public  String fiber;
    public  String fat;
    public  String sugar;
    public  String satFat;
    public  String unSatFat;
    public  String otherCholestrol;
    public  String otherPottasium;
    public String username;
    public String userUid;
    public String username_food;

    public Food() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Food(String name, String calories, String carbohydrates, String protein, String category, String fiber, String fat, String sugar, String satFat, String unSatFat, String otherCholestrol, String otherPottasium, String username, String userUid, String username_food) {
        this.name = name;
        this.calories = calories;
        this.carbohydrates = carbohydrates;
        this.protein = protein;
        this.category = category;
        this.fiber = fiber;
        this.fat = fat;
        this.sugar = sugar;
        this.satFat = satFat;
        this.unSatFat = unSatFat;
        this.otherCholestrol = otherCholestrol;
        this.otherPottasium = otherPottasium;
        this.username = username;
        this.userUid = userUid;
        this.username_food = username_food;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(String carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public String getProtein() {
        return protein;
    }

    public void setProtein(String protein) {
        this.protein = protein;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFiber() {
        return fiber;
    }

    public void setFiber(String fiber) {
        this.fiber = fiber;
    }

    public String getFat() {
        return fat;
    }

    public void setFat(String fat) {
        this.fat = fat;
    }

    public String getSugar() {
        return sugar;
    }

    public void setSugar(String sugar) {
        this.sugar = sugar;
    }

    public String getSatFat() {
        return satFat;
    }

    public void setSatFat(String satFat) {
        this.satFat = satFat;
    }

    public String getUnSatFat() {
        return unSatFat;
    }

    public void setUnSatFat(String unSatFat) {
        this.unSatFat = unSatFat;
    }

    public String getOtherCholestrol() {
        return otherCholestrol;
    }

    public void setOtherCholestrol(String otherCholestrol) {
        this.otherCholestrol = otherCholestrol;
    }

    public String getOtherPottasium() {
        return otherPottasium;
    }

    public void setOtherPottasium(String otherPottasium) {
        this.otherPottasium = otherPottasium;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getUsername_food() {
        return username_food;
    }

    public void setUsername_food(String username_food) {
        this.username_food = username_food;
    }
}