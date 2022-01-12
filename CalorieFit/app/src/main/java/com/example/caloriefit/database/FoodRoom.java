package com.example.caloriefit.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.caloriefit.model.FoodDAO;
import com.example.caloriefit.model.FoodEntity;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {FoodEntity.class}, version = 1, exportSchema = false)
public abstract class FoodRoom  extends RoomDatabase {

    public abstract FoodDAO foodDAO();

    private static volatile FoodRoom INSTANCE;
    private static final int NUM_OF_THREADS = 4;

    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUM_OF_THREADS);

    public static FoodRoom getDatabase(final Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), FoodRoom.class, "food_database")
                    .build();
        }
        return INSTANCE;
    }
}
