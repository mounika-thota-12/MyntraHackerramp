package com.example.myapplication;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Fashion extends AppCompatActivity {

    private ExoPlayer player;
    private PlayerView playerView;
    private static final String TAG = "FashionActivity";

    private ImageButton likeButton;
    private TextView likeCountTextView;
    private int likeCount = 0;

    // Firestore reference
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference likeCountRef;

    private ImageButton cartButton; // Added for cart button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main3); // Replace with your layout file name

        // Initialize PlayerView
        playerView = findViewById(R.id.player_view);

        // Check if playerView is correctly initialized
        if (playerView == null) {
            Log.e(TAG, "PlayerView not found. Check the layout file.");
            return;
        }

        // Create an ExoPlayer instance
        player = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(player);

        // Build the media item
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.fashion);
        Log.d(TAG, "Video URI: " + videoUri.toString());

        MediaItem mediaItem = MediaItem.fromUri(videoUri);
        player.setMediaItem(mediaItem);

        // Prepare the player and start playback
        player.prepare();
        player.setPlayWhenReady(true);

        Log.d(TAG, "Player prepared and playback started.");

        // Initialize like button and count TextView
        likeButton = findViewById(R.id.like);
        likeCountTextView = findViewById(R.id.like_count);

        // Firestore reference to the document where you store the like count
        likeCountRef = db.collection("mounika").document("F3GoOzgn1nLNX6OpzYNS");

        // Set initial like count text
        likeCountTextView.setText(String.valueOf(likeCount) + " Likes");

        // Set click listener for like button
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementLikeCount();
            }
        });

        // Initialize cart button and set click listener
        cartButton = findViewById(R.id.cart);
        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCartMessage();
            }
        });
    }

    private void incrementLikeCount() {
        // Increment like count
        likeCount++;

        // Update TextView to display the new like count
        likeCountTextView.setText(String.valueOf(likeCount) + " Likes");

        // Update Firestore document with the new like count
        likeCountRef.update("likecount", likeCount)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Like count updated successfully to Firestore.");
                    }
                })
                .addOnFailureListener(new OnFailureListener(){
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error updating like count to Firestore: " + e.getMessage());
                    }
                });
    }

    private void showCartMessage() {
        // Show a toast message when cart button is clicked
        Toast.makeText(this, "Item Added to Cart!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release the player when the activity is destroyed to free up resources
        if (player != null) {
            player.release();
            player = null;
        }
        Log.d(TAG, "Player released.");
    }
}
