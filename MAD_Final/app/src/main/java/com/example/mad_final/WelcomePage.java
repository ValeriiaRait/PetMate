package com.example.mad_final;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.mad_final.data.database.Pet;
import com.google.android.material.navigation.NavigationView;
import com.example.mad_final.data.database.PetDbHelper;
import java.util.List;
import java.util.Objects;
import android.database.Cursor;


public class WelcomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int ADD_NEW_FRIEND_REQUEST_CODE = 1;
    private DrawerLayout drawer;
    private PetDbHelper dbHelper;
    private List<Pet> petsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcomepage);

        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up the DrawerLayout
        drawer = findViewById(R.id.drawer_layout);

        // Set up ActionBarDrawerToggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Fetch the list of pets from the database
        dbHelper = new PetDbHelper(this);
        petsList = dbHelper.getAllPets();

        // Set up NavigationView
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Clear existing menu items
        navigationView.getMenu().clear();

        // Populate dynamic menu items for pets
        for (Pet pet : petsList) {
            navigationView.getMenu().add(R.id.group_pets, Menu.NONE, Menu.NONE, pet.getName())
                    .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            // Retrieve the Pet object based on the name
                            Pet selectedPet = getPetByName(Objects.requireNonNull(item.getTitle()).toString());

                            // Start PetDetailsActivity if the pet is found
                            if (selectedPet != null) {
                                Intent intent = new Intent(WelcomePage.this, PetDetailsActivity.class);
                                // Pass the entire Pet object as an extra
                                intent.putExtra("selectedPet", selectedPet);
                                startActivity(intent);
                                return true;
                            }
                            return false; // Indicate that the click event has not been consumed
                        }
                    });
        }

        // Handle the case where there are no pets
        if (petsList.isEmpty()) {
            // Optionally, you can add a default message or handle this case differently
            Log.e("error", "Pet list is empty");
        }

        // Set up buttons
        Button checkOnMyFeedButton = findViewById(R.id.checkOnMyFeed);
        checkOnMyFeedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomePage.this, VideoFeed.class));
            }
        });

        Button addNewFriendButton = findViewById(R.id.addNewFriendButton);
        addNewFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(WelcomePage.this, AddNewFriend.class), ADD_NEW_FRIEND_REQUEST_CODE);
            }
        });

        Button checkOnMyFriendsButton = findViewById(R.id.checkOnMyFriends);
        checkOnMyFriendsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomePage.this, CheckOnFriend.class));
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Retrieve the item's ID
        int itemId = item.getItemId();

        // Check if the selected item belongs to the group of pets
        if (item.getGroupId() == R.id.group_pets) {
            // Retrieve the Pet object based on the name
            Pet selectedPet = getPetByName(Objects.requireNonNull(item.getTitle()).toString());

            // Start PetDetailsActivity if the pet is found
            if (selectedPet != null) {
                Intent intent = new Intent(WelcomePage.this, PetDetailsActivity.class);
                // Pass the entire Pet object as an extra
                intent.putExtra("selectedPet", selectedPet);
                startActivity(intent);
            }
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // Method to get Pet by name from the database
    private Pet getPetByName(String petName) {
        for (Pet pet : petsList) {
            if (pet.getName().equals(petName)) {
                return pet; // Return the pet if found
            }
        }
        return null; // Return null if pet with the specified name is not found
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                drawer.openDrawer(GravityCompat.START);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_NEW_FRIEND_REQUEST_CODE && resultCode == RESULT_OK) {
            // Update the navigation menu after adding a new pet
            updateNavigationMenu();
        }
    }

    private void updateNavigationMenu() {
        // Fetch the updated list of pets from the database
        petsList = dbHelper.getAllPets();

        // Get the navigation view
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Clear existing menu items
        Menu menu = navigationView.getMenu();
        menu.clear();

        // Populate dynamic menu items for pets
        for (Pet pet : petsList) {
            menu.add(R.id.group_pets, Menu.NONE, Menu.NONE, pet.getName())
                    .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            // Retrieve the Pet object based on the name
                            Pet selectedPet = getPetByName(item.getTitle().toString());

                            // Start PetDetailsActivity if the pet is found
                            if (selectedPet != null) {
                                Intent intent = new Intent(WelcomePage.this, PetDetailsActivity.class);
                                // Pass the entire Pet object as an extra
                                intent.putExtra("selectedPet", selectedPet);
                                startActivity(intent);
                                return true;
                            }
                            return false; // Indicate that the click event has not been consumed
                        }
                    });
        }
    }
}

