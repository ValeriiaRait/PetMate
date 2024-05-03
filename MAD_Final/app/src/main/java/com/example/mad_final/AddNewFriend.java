package com.example.mad_final;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mad_final.data.database.Pet;
import com.example.mad_final.data.database.PetContract;
import com.example.mad_final.data.database.PetDbHelper;

public class AddNewFriend extends AppCompatActivity {

    private EditText editTextPetName;
    private EditText editTextPetSpecies;
    private EditText editTextPetAge;
    private EditText editTextPetHobbies;
    private EditText editTextPetToys;
    private EditText editTextPetFood;
    private EditText editTextPetAllergies;
    private PetDbHelper dbHelper;
    private static final int ADD_NEW_FRIEND_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_friend);

        editTextPetName = findViewById(R.id.editTextPetName);
        editTextPetSpecies = findViewById(R.id.editTextPetSpecies);
        editTextPetAge = findViewById(R.id.editTextPetAge);
        editTextPetHobbies = findViewById(R.id.editTextPetHobbies);
        editTextPetToys = findViewById(R.id.editTextPetToys);
        editTextPetFood = findViewById(R.id.editTextPetFood);
        editTextPetAllergies = findViewById(R.id.editTextPetAllergies);

        // Initialize dbHelper
        dbHelper = new PetDbHelper(this);

        findViewById(R.id.buttonSavePet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePetInfo();
            }
        });

        findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // destroy the current activity and go back
                finish();
            }
        });
    }

    // Function to save pet data
    private void savePetInfo() {
        String petName = editTextPetName.getText().toString().trim();
        String petSpecies = editTextPetSpecies.getText().toString().trim();
        int petAge = Integer.parseInt(editTextPetAge.getText().toString().trim());
        String petHobbies = editTextPetHobbies.getText().toString().trim();
        String petToys = editTextPetToys.getText().toString().trim();
        String petFood = editTextPetFood.getText().toString().trim();
        String petAllergies = editTextPetAllergies.getText().toString().trim();

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PetContract.PetEntry.COLUMN_PET_NAME, petName);
        values.put(PetContract.PetEntry.COLUMN_PET_SPECIES, petSpecies);
        values.put(PetContract.PetEntry.COLUMN_PET_AGE, petAge);
        values.put(PetContract.PetEntry.COLUMN_PET_HOBBIES, petHobbies);
        values.put(PetContract.PetEntry.COLUMN_PET_FAVORITE_TOYS, petToys);
        values.put(PetContract.PetEntry.COLUMN_PET_FAVORITE_FOOD, petFood);
        values.put(PetContract.PetEntry.COLUMN_PET_ALLERGIES, petAllergies);

        long newRowId = db.insert(PetContract.PetEntry.TABLE_NAME, null, values);

        if (newRowId != -1) {
            // If the pet is saved successfully, set the result as RESULT_OK
            setResult(RESULT_OK);
            finish(); // Finish the activity
        } else {
            // Handle the case where there was an error with saving the pet
            Toast.makeText(this, "Error with saving pet", Toast.LENGTH_SHORT).show();
        }
    }
}
