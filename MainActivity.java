package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ImageView modelImageView;
    private LinearLayout clothesContainer;
    private List<Integer> clothesImageResources;
    private List<String> clothesPrices;
    private List<Integer> clothesProductId;

    private int[] dollImageResources = {
            R.drawable.doll1,
            R.drawable.doll2,
            R.drawable.doll3,
            R.drawable.doll4,
            R.drawable.doll5
    };

    private int currentModelIndex = 0;
    private int lastDraggedProductIndex = -1; // Track the index of the last dragged product
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main2);

        modelImageView = findViewById(R.id.modelImageView);
        clothesContainer = findViewById(R.id.clothesContainer);

        // Set initial doll image
        modelImageView.setImageResource(dollImageResources[currentModelIndex]);
        modelImageView.setVisibility(View.VISIBLE);

        // Initialize clothes images, prices, and product IDs
        clothesImageResources = new ArrayList<>();
        clothesPrices = new ArrayList<>();
        clothesProductId = new ArrayList<>();

        // Add sample clothes data
        clothesImageResources.add(R.drawable.dresses);
        clothesPrices.add("Rs.800");
        clothesProductId.add(101);

        clothesImageResources.add(R.drawable.dress1);
        clothesPrices.add("Rs.1500");
        clothesProductId.add(182);

        clothesImageResources.add(R.drawable.dress2);
        clothesPrices.add("Rs.500");
        clothesProductId.add(251);

        clothesImageResources.add(R.drawable.dress3);
        clothesPrices.add("Rs.500");
        clothesProductId.add(623);

        clothesImageResources.add(R.drawable.dress4);
        clothesPrices.add("Rs.900");
        clothesProductId.add(794);

        // Load clothes images into the container
        loadClothesImages();

        // Set drag listener to handle the drop action for modelContainer
        findViewById(R.id.modelContainer).setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                int action = event.getAction();
                switch (action) {
                    case DragEvent.ACTION_DROP:
                        // Handle the drop - change doll image
                        ImageView droppedView = (ImageView) event.getLocalState();
                        int droppedIndex = (int) droppedView.getTag();
                        if (droppedIndex < dollImageResources.length) {
                            modelImageView.setImageResource(dollImageResources[droppedIndex]);
                            lastDraggedProductIndex = droppedIndex; // Update last dragged product index
                        }
                        return true;
                    default:
                        return true;
                }
            }
        });

        // Setup actionButton click listener
        Button actionButton = findViewById(R.id.actionButton);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastDraggedProductIndex != -1) {
                    int lastDraggedProductId = clothesProductId.get(lastDraggedProductIndex);
                    updateProductIDInFirestore(lastDraggedProductId);
                } else {
                    Log.e(TAG, "No product dragged yet.");
                }
            }
        });

        // Start animation for actionButton
        Animation blinkAnimation = AnimationUtils.loadAnimation(this, R.anim.main4);
        actionButton.startAnimation(blinkAnimation);
    }

    // Method to load clothes images into the container
    private void loadClothesImages() {
        for (int i = 0; i < clothesImageResources.size(); i++) {
            int resourceId = clothesImageResources.get(i);
            String price = clothesPrices.get(i);

            final ImageView clothesImageView = new ImageView(this);
            clothesImageView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            clothesImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            clothesImageView.setAdjustViewBounds(true);
            clothesImageView.setTag(i); // Tag to identify the index

            // Set image resource directly
            clothesImageView.setImageResource(resourceId);

            // Set touch listener to start the drag operation
            clothesImageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                        v.startDragAndDrop(null, shadowBuilder, v, 0);
                        return true;
                    } else {
                        return false;
                    }
                }
            });

            // Create TextView for the price
            TextView priceTextView = new TextView(this);
            priceTextView.setText(price);
            priceTextView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

            // Create a container for the image and price
            LinearLayout itemContainer = new LinearLayout(this);
            itemContainer.setOrientation(LinearLayout.VERTICAL);
            itemContainer.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            itemContainer.addView(clothesImageView);
            itemContainer.addView(priceTextView);

            // Add item container to clothes container
            clothesContainer.addView(itemContainer);
        }
    }

    // Method to update last dragged product ID in Firestore
    private void updateProductIDInFirestore(int productId) {
        // Create a map with the product ID
        Map<String, Object> productData = new HashMap<>();
        productData.put("ProductId", productId);

        // Update document in Firestore
        db.collection("mounika")  // Reference the "mounika" collection
                .document("F3GoOzgn1nLNX6OpzYNS")  // Replace with your document ID
                .set(productData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Product ID updated successfully in Firestore.");
                        } else {
                            Log.e(TAG, "Error updating product ID in Firestore.", task.getException());
                        }
                    }
                });
    }
}
