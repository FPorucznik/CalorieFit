package com.example.caloriefit.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

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
            INSTANCE = Room
                    .databaseBuilder(context.getApplicationContext(), FoodRoom.class, "food_database")
                    .addCallback(sRoomDatabaseCallback)
                    .build();
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db){
                    super.onCreate(db);

                    databaseWriteExecutor.execute(() -> {
                        FoodDAO dao = INSTANCE.foodDAO();
                        dao.deleteAll();

                        FoodEntity food = new FoodEntity("Chocolate", 450);
                        dao.insert(food);
                        food = new FoodEntity("Beef", 230);
                        dao.insert(food);
                    });
                }
            };
}
