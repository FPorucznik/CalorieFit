package com.example.caloriefit.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.caloriefit.R;
import com.example.caloriefit.databinding.ActivityNewFoodBinding;
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

public class NewFoodActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "REPLY";
    private ActivityNewFoodBinding binding;
    private static final int CAMERA_PERMISSION_CODE = 1;
    private static final int CAMERA_INTENT = 2;
    private Uri savePicturePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNewFoodBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.addPhotoNewButton.setOnClickListener(v -> {
            activateCamera();
        });

        EditText addNewFoodName = findViewById(R.id.food_edit_new_name);
        EditText addNewCalories = findViewById(R.id.food_edit_new_calories);

        final Button button = findViewById(R.id.add_food_new);
        button.setOnClickListener(v -> {
            if(addNewCalories.getText().toString().contains(".") || addNewCalories.getText().toString().contains(",") || addNewCalories.getText().toString().contains(" ") || addNewCalories.getText().toString().contains("-")){
                Toast.makeText(this, "Only integer values", Toast.LENGTH_LONG).show();
            }
            else{
                Intent replyIntent = new Intent();
                if(TextUtils.isEmpty(addNewFoodName.getText()) || TextUtils.isEmpty(addNewCalories.getText())){
                    setResult(RESULT_CANCELED, replyIntent);
                }
                else{
                    String foodName = addNewFoodName.getText().toString();
                    int calories = Integer.parseInt(addNewCalories.getText().toString());

                    replyIntent.putExtra("name", foodName);
                    replyIntent.putExtra("calories", calories);
                    replyIntent.putExtra("photo", savePicturePath.toString());

                    if(!MainActivity.viewModel.getAllFood().getValue().isEmpty()){
                        MainActivity.viewModel.updateLimit(getIntent().getIntExtra("limit", 0));
                    }

                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
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
                //binding.crimeImageView.setImageBitmap(image);
                binding.foodImageNewView.setImageBitmap(image);
                savePicturePath = savePicture(image);
                //MainActivity.dbHandler.updateCrimeImage(current.getId(), savePicturePath.toString());
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