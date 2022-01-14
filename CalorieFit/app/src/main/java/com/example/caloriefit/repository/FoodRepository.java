package com.example.caloriefit.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.caloriefit.database.FoodRoom;
import com.example.caloriefit.model.FoodDAO;
import com.example.caloriefit.model.FoodEntity;

import java.util.List;

public class FoodRepository {

    private FoodDAO mFoodDAO;
    private LiveData<List<FoodEntity>> mAllFood;
    private FoodEntity mFood;

    public FoodRepository(Application application){
        FoodRoom db = FoodRoom.getDatabase(application);
        mFoodDAO = db.foodDAO();
        mAllFood = mFoodDAO.getAllFood();
    }

    public LiveData<List<FoodEntity>> getAllFood(){
        return mAllFood;
    }

    public void insert(FoodEntity entity){
        FoodRoom.databaseWriteExecutor.execute(() -> {
            mFoodDAO.insert(entity);
        });
    }

    public void deleteFood(FoodEntity entity) {
        FoodRoom.databaseWriteExecutor.execute(() -> {
            mFoodDAO.deleteFood(entity);
        });
    }

    public void update(FoodEntity entity){
        FoodRoom.databaseWriteExecutor.execute(() -> {
            mFoodDAO.update(entity);
        });
    }

    public void updateLimit(int limit){
        FoodRoom.databaseWriteExecutor.execute(() -> {
            mFoodDAO.updateLimit(limit);
        });
    }
}
