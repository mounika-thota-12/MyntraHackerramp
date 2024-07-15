package com.example.myapplication; // Replace with your package name

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ShoppingBagActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView textView;
    private TextView newTextView;
    private TextView belowTextView;
    private TextView below;
    private TextView imageDescription;
    private Button bottomButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main5);

        // Initialize views
        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);
        newTextView = findViewById(R.id.newTextView);
        belowTextView = findViewById(R.id.belowTextView);
        imageDescription = findViewById(R.id.image_description);
        bottomButton = findViewById(R.id.bottom_button);

        // Example: Set text for TextViews
        textView.setText("Shopping Bag");
        newTextView.setText("Thota Mounika, 500037");
        belowTextView.setText("Deliver to:");
        imageDescription.setText("Rs.900");

        // Example: Handle button click
        bottomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform action when button is clicked, e.g., add to cart logic
            }
        });
    }
}
