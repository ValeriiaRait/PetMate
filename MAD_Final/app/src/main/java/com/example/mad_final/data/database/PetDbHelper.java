package com.example.mad_final.data.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class PetDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "PetApp.db";
    private static final int DATABASE_VERSION = 2;

    public PetDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_PETS_TABLE = "CREATE TABLE " + PetContract.PetEntry.TABLE_NAME + " ("
                + PetContract.PetEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PetContract.PetEntry.COLUMN_PET_NAME + " TEXT NOT NULL, "
                + PetContract.PetEntry.COLUMN_PET_AGE + " INTEGER, "
                + PetContract.PetEntry.COLUMN_PET_SPECIES + " TEXT, "
                + PetContract.PetEntry.COLUMN_PET_HOBBIES + " TEXT, "
                + PetContract.PetEntry.COLUMN_PET_FAVORITE_TOYS + " TEXT, "
                + PetContract.PetEntry.COLUMN_PET_FAVORITE_FOOD + " TEXT, "
                + PetContract.PetEntry.COLUMN_PET_ALLERGIES + " TEXT);";
        db.execSQL(SQL_CREATE_PETS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the old table if it exists
        db.execSQL("DROP TABLE IF EXISTS " + PetContract.PetEntry.TABLE_NAME);
        // Create the new table
        onCreate(db);
    }

    public long insertPet(Pet pet) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PetContract.PetEntry.COLUMN_PET_NAME, pet.getName());
        values.put(PetContract.PetEntry.COLUMN_PET_AGE, pet.getAge());
        values.put(PetContract.PetEntry.COLUMN_PET_SPECIES, pet.getSpecies());
        values.put(PetContract.PetEntry.COLUMN_PET_HOBBIES, pet.getHobbies());
        values.put(PetContract.PetEntry.COLUMN_PET_FAVORITE_TOYS, pet.getFavoriteToys());
        values.put(PetContract.PetEntry.COLUMN_PET_FAVORITE_FOOD, pet.getFavoriteFood());
        values.put(PetContract.PetEntry.COLUMN_PET_ALLERGIES, pet.getAllergies());
        long newRowId = db.insert(PetContract.PetEntry.TABLE_NAME, null, values);
        db.close();
        return newRowId;
    }

    @SuppressLint("Range")
    public List<Pet> getAllPets() {
        List<Pet> pets = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                PetContract.PetEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") Pet pet = new Pet(
                        cursor.getString(cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_NAME)),
                        cursor.getInt(cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_AGE)),
                        cursor.getString(cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_SPECIES)),
                        cursor.getString(cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_HOBBIES)),
                        cursor.getString(cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_FAVORITE_TOYS)),
                        cursor.getString(cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_FAVORITE_FOOD)),
                        cursor.getString(cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_ALLERGIES))
                );
                pet.setId(cursor.getLong(cursor.getColumnIndex(PetContract.PetEntry._ID)));
                pet.setHobbies(cursor.getString(cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_HOBBIES)));
                pet.setFavoriteToys(cursor.getString(cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_FAVORITE_TOYS)));
                pet.setFavoriteFood(cursor.getString(cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_FAVORITE_FOOD)));
                pet.setAllergies(cursor.getString(cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_ALLERGIES)));
                pets.add(pet);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return pets;
    }
}
