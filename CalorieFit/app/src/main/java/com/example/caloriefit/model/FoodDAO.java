package com.example.caloriefit.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FoodDAO {
    @Insert
    void insert(FoodEntity food);

    @Query("DELETE FROM food_table")
    void deleteAll();

    @Delete
    void deleteFood(FoodEntity entity);

    @Update
    void update(FoodEntity entity);

    @Query("UPDATE food_table SET calorie_limit = :limit")
    void updateLimit(int limit);

    @Query("SELECT calorie_limit FROM food_table LIMIT 1")
    LiveData<Integer> getCalorieLimit();

    @Query("SELECT * FROM food_table")
    LiveData<List<FoodEntity>> getAllFood();
}
