package com.example.caloriefit.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caloriefit.R;
import com.example.caloriefit.model.FoodEntity;
import com.example.caloriefit.viewmodel.FoodViewModel;

public class MainActivity extends AppCompatActivity {

    private static final int NEW_FOOD_REQUEST_CODE = 1;
    private FoodViewModel viewModel;

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

        addFoodBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NewFoodActivity.class);
            startActivityForResult(intent, NEW_FOOD_REQUEST_CODE);
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == NEW_FOOD_REQUEST_CODE && resultCode == RESULT_OK){
            FoodEntity food = new FoodEntity(data.getStringExtra("name"), data.getIntExtra("calories", 0));
            viewModel.insert(food);
        }
        else {
            Toast.makeText(this, "failed to add food", Toast.LENGTH_SHORT).show();
        }
    }
}