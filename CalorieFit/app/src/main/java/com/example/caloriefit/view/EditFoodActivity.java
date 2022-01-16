package com.example.caloriefit.view;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.caloriefit.R;
import com.example.caloriefit.databinding.ActivityEditFoodBinding;
import com.example.caloriefit.databinding.ActivityNewFoodBinding;
import com.example.caloriefit.model.FoodEntity;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class EditFoodActivity extends AppCompatActivity {

    private EditText editName;
    private EditText editCalories;
    private Button saveBtn;
    private Button deleteBtn;

    private ActivityEditFoodBinding binding;
    private static final int CAMERA_PERMISSION_CODE = 1;
    private static final int CAMERA_INTENT = 2;
    private Uri savePicturePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditFoodBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.addPhotoButton.setOnClickListener(v -> {
            activateCamera();
        });

        editName = findViewById(R.id.food_edit_name);
        editCalories = findViewById(R.id.food_edit_calories);
        saveBtn = findViewById(R.id.save_food_btn);
        deleteBtn = findViewById(R.id.food_delete_btn);

        int id = getIntent().getIntExtra("id", 1);
        FoodEntity current = MainActivity.viewModel.getAllFood().getValue().get(id);
        editName.setText(current.getName());
        editCalories.setText(String.valueOf(current.getCalories()));
        if(current.getPhoto() != null){
            binding.foodImageView.setImageURI(Uri.parse(current.getPhoto()));
        }

        saveBtn.setOnClickListener(v -> {
            if(!editName.getText().equals("") && !editCalories.getText().equals("")){
                if(editCalories.getText().toString().contains(".") || editCalories.getText().toString().contains(",") || editCalories.getText().toString().contains(" ") || editCalories.getText().toString().contains("-")){
                    Toast.makeText(this, "Only integer values", Toast.LENGTH_LONG).show();
                }
                else{
                    int limit = current.getCalorieLimit();
                    current.setName(editName.getText().toString());
                    current.setCalories(Integer.parseInt(editCalories.getText().toString()));
                    if(savePicturePath != null){
                        current.setPhoto(savePicturePath.toString());
                    }

                    MainActivity.viewModel.updateLimit(limit);
                    MainActivity.viewModel.update(current);
                    finish();
                }
            }
        });

        deleteBtn.setOnClickListener(v -> {
            int limit = current.getCalorieLimit();
            MainActivity.viewModel.deleteFood(current);
            MainActivity.viewModel.updateLimit(limit);
            finish();
        });
    }

    private void activateCamera() {
        Dexter.withContext(this).withPermission(
                Manifest.permission.CAMERA
        ).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_INTENT);
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                showRationaleDialog();
            }
        }).onSameThread().check();
    }

    private void showRationaleDialog(){
        new AlertDialog.Builder(this)
                .setMessage("This feature requires camera permission")
                .setPositiveButton("Ask me", (dialog, which) -> {
                    try {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                    catch (ActivityNotFoundException e){
                        e.printStackTrace();
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == CAMERA_INTENT){
                Bitmap image = (Bitmap)data.getExtras().get("data");
                binding.foodImageView.setImageBitmap(image);
                savePicturePath = savePicture(image);
            }
        }
    }

    private Uri savePicture(Bitmap bitmap){
        ContextWrapper wrapper = new ContextWrapper(getApplicationContext());
        File file = wrapper.getDir("myGallery", Context.MODE_PRIVATE);
        file = new File(file, UUID.randomUUID().toString() + ".jpg");

        try {
            OutputStream stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            stream.flush();
            stream.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return Uri.parse(file.getAbsolutePath());
    }
}
