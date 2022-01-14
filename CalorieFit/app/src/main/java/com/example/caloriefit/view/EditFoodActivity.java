package com.example.caloriefit.view;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.caloriefit.R;
import com.example.caloriefit.model.FoodEntity;

public class EditFoodActivity extends AppCompatActivity {

    private EditText editName;
    private EditText editCalories;
    private Button saveBtn;
    private Button deleteBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_activity);

        editName = findViewById(R.id.food_edit_name);
        editCalories = findViewById(R.id.food_edit_calories);
        saveBtn = findViewById(R.id.save_food_btn);
        deleteBtn = findViewById(R.id.food_delete_btn);

        int id = getIntent().getIntExtra("id", 1);
        FoodEntity current = MainActivity.viewModel.getAllFood().getValue().get(id);
        editName.setText(current.getName());
        editCalories.setText(String.valueOf(current.getCalories()));

        saveBtn.setOnClickListener(v -> {
            if(!editName.getText().equals("") && !editCalories.getText().equals("")){
                int limit = current.getCalorieLimit();
                current.setName(editName.getText().toString());
                current.setCalories(Integer.parseInt(editCalories.getText().toString()));

                MainActivity.viewModel.updateLimit(limit);
                MainActivity.viewModel.update(current);
                finish();
            }
        });

        deleteBtn.setOnClickListener(v -> {
            int limit = current.getCalorieLimit();
            MainActivity.viewModel.deleteFood(current);
            MainActivity.viewModel.updateLimit(limit);
            finish();
        });
    }
}
