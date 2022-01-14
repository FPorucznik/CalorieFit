package com.example.caloriefit.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caloriefit.R;
import com.example.caloriefit.model.FoodEntity;
import com.example.caloriefit.viewmodel.FoodViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int NEW_FOOD_REQUEST_CODE = 1;
    public static FoodViewModel viewModel;

    private EditText calorieLimitEdit;
    private Button addCalorieLimitBtn;
    private Button addFoodBtn;
    private ProgressBar progressBar;
    private TextView calorieLimitText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        final FoodRecyclerViewAdapter adapter = new FoodRecyclerViewAdapter(
                new FoodRecyclerViewAdapter.FoodDiff()
        );
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        viewModel = new ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(FoodViewModel.class);

        viewModel.getAllFood().observe(this, adapter::submitList);

        calorieLimitEdit = findViewById(R.id.calorie_limit_edit);
        addCalorieLimitBtn = findViewById(R.id.set_limit_btn);
        addFoodBtn = findViewById(R.id.add_food_btn);
        progressBar = findViewById(R.id.progress_bar);
        calorieLimitText = findViewById(R.id.calorie_limit_text);


        final Observer<List<FoodEntity>> foodObserver = foodEntities -> {
            if(!viewModel.getAllFood().getValue().isEmpty()){
                int sumOfCalories = 0;
                for(FoodEntity entity : viewModel.getAllFood().getValue()){
                    sumOfCalories += entity.getCalories();
                }
                calorieLimitText.setText(sumOfCalories + " / " + viewModel.getAllFood().getValue().get(0).getCalorieLimit());
                updateProgressBar(sumOfCalories, viewModel.getAllFood().getValue().get(0).getCalorieLimit());

            }
            else {
                calorieLimitText.setText("0 / set limit");
                updateProgressBar(0, 0);
            }
        };

        viewModel.getAllFood().observe(this, foodObserver);

        addFoodBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NewFoodActivity.class);

            if(calorieLimitEdit.getText().toString().trim().length() == 0){
                int limit = 0;
                intent.putExtra("limit", limit);
            }
            else {
                int limit = Integer.parseInt(calorieLimitEdit.getText().toString());
                intent.putExtra("limit", limit);
            }
            startActivityForResult(intent, NEW_FOOD_REQUEST_CODE);
        });

        addCalorieLimitBtn.setOnClickListener(v -> {
            if(calorieLimitEdit.getText().toString().trim().length() == 0){
                Toast.makeText(this, "Wrong input", Toast.LENGTH_LONG).show();
            }
            else{
                int limit = Integer.parseInt(calorieLimitEdit.getText().toString());
                if(!viewModel.getAllFood().getValue().isEmpty()){
                    viewModel.updateLimit(limit);
                    calorieLimitText.setText("0/" + limit);
                }
                else {
                    Toast.makeText(this, "Add some food first", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == NEW_FOOD_REQUEST_CODE && resultCode == RESULT_OK){
            FoodEntity food = new FoodEntity(data.getStringExtra("name"), data.getIntExtra("calories", 0));
            viewModel.insert(food);
            if(!viewModel.getAllFood().getValue().isEmpty()){
                viewModel.updateLimit(viewModel.getAllFood().getValue().get(0).getCalorieLimit());
            }

        }
        else {
            Toast.makeText(this, "failed to add food", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateProgressBar(int sum, int limit){
        int progress = 0;
        progress += ((float)sum / limit) * 100;

        if(sum > limit){
            progressBar.setProgressTintList(ColorStateList.valueOf(Color.RED));
        }
        else if(sum < limit){
            progressBar.setProgressTintList(ColorStateList.valueOf(Color.GREEN));
        }
        else{
            progressBar.setProgressTintList(ColorStateList.valueOf(Color.GREEN));
        }
        progressBar.setProgress(progress);
    }
}