package com.example.mad_final.data.database;

import android.os.Parcel;
import android.os.Parcelable;

public class Pet implements Parcelable {
    private long id;
    private String name;
    private int age;
    private String species;
    private String hobbies;
    private String favoriteToys;
    private String favoriteFood;
    private String allergies;

    public Pet(String name, int age, String species, String hobbies, String favoriteToys, String favoriteFood, String allergies) {
        this.name = name;
        this.age = age;
        this.species = species;
        this.hobbies = hobbies;
        this.favoriteToys = favoriteToys;
        this.favoriteFood = favoriteFood;
        this.allergies = allergies;
    }

    protected Pet(Parcel in) {
        id = in.readLong();
        name = in.readString();
        age = in.readInt();
        species = in.readString();
        hobbies = in.readString();
        favoriteToys = in.readString();
        favoriteFood = in.readString();
        allergies = in.readString();
    }

    public static final Creator<Pet> CREATOR = new Creator<Pet>() {
        @Override
        public Pet createFromParcel(Parcel in) {
            return new Pet(in);
        }

        @Override
        public Pet[] newArray(int size) {
            return new Pet[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getSpecies() {
        return species;
    }

    public String getHobbies() {
        return hobbies;
    }

    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }

    public String getFavoriteToys() {
        return favoriteToys;
    }

    public void setFavoriteToys(String favoriteToys) {
        this.favoriteToys = favoriteToys;
    }

    public String getFavoriteFood() {
        return favoriteFood;
    }

    public void setFavoriteFood(String favoriteFood) {
        this.favoriteFood = favoriteFood;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeInt(age);
        dest.writeString(species);
        dest.writeString(hobbies);
        dest.writeString(favoriteToys);
        dest.writeString(favoriteFood);
        dest.writeString(allergies);
    }
}
