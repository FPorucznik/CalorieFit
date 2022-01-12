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
}
