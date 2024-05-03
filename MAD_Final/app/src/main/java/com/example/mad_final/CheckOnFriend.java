package com.example.mad_final;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.mad_final.data.database.Pet;
import com.example.mad_final.data.database.PetDbHelper;

import java.util.List;

public class CheckOnFriend extends AppCompatActivity {

    public static final String ACTION_NEW_VIDEOS_AVAILABLE = "com.example.mad_final.NEW_VIDEOS_AVAILABLE";
    private NewVideosReceiver newVideosReceiver = new NewVideosReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_on_friend);

        // Set the back button listener
        findViewById(R.id.buttonBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Set the see videos button listener
        Button buttonSeeVideos = findViewById(R.id.buttonSeeVideos);
        buttonSeeVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the VideoFeed activity
                // Сan add code here to pass any necessary data to the VideoFeed activity
                startActivity(new Intent(CheckOnFriend.this, VideoFeed.class));
            }
        });

        // Display pet information
        displayPetInfo();
    }

    private void displayPetInfo() {
        PetDbHelper dbHelper = new PetDbHelper(this);
        List<Pet> petsList = dbHelper.getAllPets();

        LinearLayout linearLayoutPetNames = findViewById(R.id.linearLayoutPetNames);

        // Clear the existing views in the layout
        linearLayoutPetNames.removeAllViews();

        for (Pet pet : petsList) {
            // Inflate the layout for each pet entry dynamically
            View petEntryView = getLayoutInflater().inflate(R.layout.pet_entry_layout, null);

            // Find views within the inflated layout
            ImageView pawIcon = petEntryView.findViewById(R.id.pawIcon);
            TextView petNameTextView = petEntryView.findViewById(R.id.petNameTextView);

            // Set the pet's data (name, icon, etc.)
            petNameTextView.setText(pet.getName());
            pawIcon.setImageResource(R.drawable.paw_icon);

            // Add the inflated layout to the parent layout
            linearLayoutPetNames.addView(petEntryView);

            // Add separator line after each pet entry except the last one
            if (petsList.indexOf(pet) < petsList.size() - 1) {
                View separatorLine = new View(this);
                separatorLine.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
                separatorLine.setBackgroundColor(getResources().getColor(R.color.black));
                linearLayoutPetNames.addView(separatorLine);
            }

            // Set click listener for pet entry view
            petEntryView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Create a new Intent to start the PetDetailsActivity
                    Intent intent = new Intent(CheckOnFriend.this, PetDetailsActivity.class);

                    // Set the selected pet object as an extra in the Intent
                    intent.putExtra("selectedPet", pet);

                    // Start the PetDetailsActivity
                    startActivity(intent);
                }
            });
        }
    }

//    private void showPetDetailsFragment(Pet pet) {
//        // Start a new activity or fragment to display the detailed information about the selected pet
//        // You can pass the selected pet's information to the new activity or fragment using Intent extras or arguments
//        Intent intent = new Intent(this, PetDetailsActivity.class);
//        intent.putExtra("petName", pet.getName());
//        intent.putExtra("petSpecies", pet.getSpecies());
//        intent.putExtra("petAge", pet.getAge());
//        startActivity(intent);
//    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(ACTION_NEW_VIDEOS_AVAILABLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerReceiver(newVideosReceiver, filter, Context.RECEIVER_NOT_EXPORTED);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(newVideosReceiver);
    }
}
