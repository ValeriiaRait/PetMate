package com.example.mad_final.data.database;

import android.provider.BaseColumns;

public final class PetContract {
    private PetContract() {}

    public static class PetEntry implements BaseColumns {
        public static final String TABLE_NAME = "pets";
        public static final String COLUMN_PET_NAME = "name";
        public static final String COLUMN_PET_AGE = "age";
        public static final String COLUMN_PET_SPECIES = "species";
        public static final String COLUMN_PET_HOBBIES = "hobbies";
        public static final String COLUMN_PET_FAVORITE_TOYS = "favorite_toys";
        public static final String COLUMN_PET_FAVORITE_FOOD = "favorite_food";
        public static final String COLUMN_PET_ALLERGIES = "allergies";
    }
}
