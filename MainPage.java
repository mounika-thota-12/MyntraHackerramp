package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainPage extends AppCompatActivity {

    private Button createAvatarButton;
    private ImageView banner1ImageView;
    private ImageButton imageButton3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main1);

        // Initialize views
        createAvatarButton = findViewById(R.id.textButton);
        banner1ImageView = findViewById(R.id.banner1ImageView);
        imageButton3 = findViewById(R.id.imageButton3);

        // Set click listener for create avatar button
        createAvatarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainPage.this, MainActivity.class));
            }
        });
        imageButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 startActivity(new Intent(MainPage.this,ShoppingBagActivity.class));
            }
        });
    }

    // Method to handle click on banner1ImageView
    public void openFashion(View view) {
        Intent intent = new Intent(MainPage.this, Fashion.class);
        startActivity(intent);
    }
}
