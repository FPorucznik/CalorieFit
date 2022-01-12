package com.example.caloriefit.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
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

    @Query("DELETE FROM food_table WHERE id = :id")
    void deleteFood(int id);

    @Update
    void update(FoodEntity entity);

    @Query("SELECT calorie_limit FROM food_table LIMIT 1")
    LiveData<Integer> getCalorieLimit();

    @Query("SELECT * FROM food_table")
    LiveData<List<FoodEntity>> getAllFood();

    @Query("SELECT * FROM food_table WHERE id = :id")
    LiveData<FoodEntity> getFood(int id);
}
