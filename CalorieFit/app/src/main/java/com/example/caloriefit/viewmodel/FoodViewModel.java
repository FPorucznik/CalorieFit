package com.example.caloriefit.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.caloriefit.model.FoodEntity;
import com.example.caloriefit.repository.FoodRepository;

import java.util.List;

public class FoodViewModel extends AndroidViewModel {

    private final FoodRepository mRepository;

    private final LiveData<List<FoodEntity>> mAllFood;
    private final LiveData<FoodEntity> mFood = null;

    public FoodViewModel(@NonNull Application application) {
        super(application);

        mRepository = new FoodRepository(application);
        mAllFood = mRepository.getAllFood();
    }

    public LiveData<List<FoodEntity>> getAllFood(){ return mAllFood; }

    public void insert(FoodEntity entity){ mRepository.insert(entity); }

    public void deleteFood(FoodEntity entity) { mRepository.deleteFood(entity); }

    public void update(FoodEntity entity) { mRepository.update(entity); }

    public void updateLimit(int limit) { mRepository.updateLimit(limit); }
}
