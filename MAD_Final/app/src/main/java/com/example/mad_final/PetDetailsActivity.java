package com.example.mad_final;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mad_final.data.database.Pet;

public class PetDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_details);

        // Retrieve the Pet object passed from the previous activity
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("selectedPet")) {
            Pet selectedPet = intent.getParcelableExtra("selectedPet");
            if (selectedPet != null) {
                // Retrieve the TextView for the header
                TextView textViewHeader = findViewById(R.id.textViewHeader);
                // Concatenate the pet's name to the existing text in the header
                textViewHeader.setText(textViewHeader.getText() + " " + selectedPet.getName());

                // Display the details of the selected pet
                displayPetDetails(selectedPet);
            }
        }

        // Retrieve the back button
        Button buttonBack = findViewById(R.id.buttonBack);

        // Set OnClickListener to the back button
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the activity to go back
                finish();
            }
        });
    }

    // Method to display the details of the selected pet
    private void displayPetDetails(Pet pet) {
        TextView textViewPetName = findViewById(R.id.textViewPetName);
        TextView textViewPetSpecies = findViewById(R.id.textViewPetSpecies);
        TextView textViewPetAge = findViewById(R.id.textViewPetAge);
        TextView textViewPetHobbies = findViewById(R.id.textViewPetHobbies);
        TextView textViewPetToys = findViewById(R.id.textViewPetToys);
        TextView textViewPetFood = findViewById(R.id.textViewPetFood);
        TextView textViewPetAllergies = findViewById(R.id.textViewPetAllergies);

        // Set the pet name, species, age, hobbies, favorite toys, favorite food, and allergies
        textViewPetName.setText("Name: " + pet.getName());
        textViewPetSpecies.setText("Species: " + pet.getSpecies());
        textViewPetAge.setText("Age: " + pet.getAge());
        textViewPetHobbies.setText("Hobbies: " + pet.getHobbies());
        textViewPetToys.setText("Favorite Toys: " + pet.getFavoriteToys());
        textViewPetFood.setText("Favorite Food: " + pet.getFavoriteFood());
        textViewPetAllergies.setText("Allergies: " + pet.getAllergies());
    }
}
