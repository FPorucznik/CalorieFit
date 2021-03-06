package com.example.caloriefit.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "food_table")
public class FoodEntity {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private int mId;

    @NonNull
    @ColumnInfo(name = "food_name")
    private String mName;

    @NonNull
    @ColumnInfo(name = "calorie_value")
    private int mCalories;

    @NonNull
    @ColumnInfo(name = "calorie_limit")
    private int mCalorieLimit;

    @NonNull
    @ColumnInfo(name = "photo_uri")
    private String mPhoto;

    public FoodEntity(@NonNull String name, @NonNull int calories, @NonNull String photo){
        this.mName = name;
        this.mCalories = calories;
        this.mPhoto = photo;
    }

    public String getName() {
        return this.mName;
    }

    public int getCalories() {
        return this.mCalories;
    }

    public String getPhoto() {
        return this.mPhoto;
    }

    public int getId() {
        return this.mId;
    }

    public int getCalorieLimit() { return this.mCalorieLimit; }

    public void setId(int mId) { this.mId = mId; }

    public void setCalorieLimit(int mCalorieLimit) { this.mCalorieLimit = mCalorieLimit; }

    public void setPhoto(String uri){ this.mPhoto = uri; }

    public void setName(String name) { this.mName = name; }

    public void setCalories(int calories){ this.mCalories = calories; }
}
