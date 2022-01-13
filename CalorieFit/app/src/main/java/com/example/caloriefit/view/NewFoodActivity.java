package com.example.caloriefit.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.example.caloriefit.R;

public class NewFoodActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "REPLY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_food);

        EditText addNewFoodName = findViewById(R.id.food_edit_new_name);
        EditText addNewCalories = findViewById(R.id.food_edit_new_calories);

        final Button button = findViewById(R.id.add_food_new);
        button.setOnClickListener(v -> {
            Intent replyIntent = new Intent();
            if(TextUtils.isEmpty(addNewFoodName.getText()) || TextUtils.isEmpty(addNewCalories.getText())){
                setResult(RESULT_CANCELED, replyIntent);
            }
            else{
                String foodName = addNewFoodName.getText().toString();
                int calories = Integer.parseInt(addNewCalories.getText().toString());

                replyIntent.putExtra("name", foodName);
                replyIntent.putExtra("calories", calories);
                setResult(RESULT_OK, replyIntent);
            }
            finish();
        });
    }
}