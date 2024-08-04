package com.europhia.base64encodedecode;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    Button takePhoto, imgBase64, base64_toimg;
    ActivityResultLauncher<Intent> activityResultLauncher;

    private Bitmap selectedImageBitmap;
    private String selectedImageBase64;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.image_view);
        takePhoto = findViewById(R.id.btn_photo);
        imgBase64 = findViewById(R.id.img_base64);
        base64_toimg = findViewById(R.id.base64_to_img);

        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(intent.resolveActivity(getPackageManager()) != null){
                    activityResultLauncher.launch(intent);
                } else {
                    Toast.makeText(MainActivity.this, "There is no app that support this action", Toast.LENGTH_SHORT).show();
                }
            }
        });

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bundle bundle = result.getData().getExtras();
                    selectedImageBitmap = (Bitmap) bundle.get("data");
                    //imageView.setImageBitmap(bitmap);
                }
            }
        });

        imgBase64.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if(selectedImageBitmap != null) {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    selectedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    selectedImageBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    Toast.makeText(MainActivity.this, selectedImageBase64, Toast.LENGTH_SHORT).show();
                    Log.d("encode result", selectedImageBase64);
                }
            }
        });

        base64_toimg.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                byte[] decodeByteArray = Base64.decode(selectedImageBase64, Base64.DEFAULT);
                Bitmap decodeBitmap = BitmapFactory.decodeByteArray(decodeByteArray,0,decodeByteArray.length);
                imageView.setImageBitmap(decodeBitmap);
            }
        });
    }
}