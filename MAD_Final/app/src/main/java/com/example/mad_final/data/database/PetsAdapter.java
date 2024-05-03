package com.example.mad_final.data.database;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.mad_final.R;

import java.util.List;

public class PetsAdapter extends ArrayAdapter<Pet> {
    public PetsAdapter(Context context, List<Pet> pets) {
        super(context, 0, pets);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.pet_list_item, parent, false);
        }

        Pet pet = getItem(position);

        TextView petNameTextView = convertView.findViewById(R.id.pet_name);
        TextView petAgeTextView = convertView.findViewById(R.id.pet_age);
        TextView petSpeciesTextView = convertView.findViewById(R.id.pet_species);
        TextView petHobbiesTextView = convertView.findViewById(R.id.pet_hobbies);
        TextView petToysTextView = convertView.findViewById(R.id.pet_toys);
        TextView petFoodTextView = convertView.findViewById(R.id.pet_food);
        TextView petAllergiesTextView = convertView.findViewById(R.id.pet_allergies);

        petNameTextView.setText(pet.getName());
        petAgeTextView.setText(getContext().getString(R.string.pet_age, pet.getAge()));
        petSpeciesTextView.setText(getContext().getString(R.string.pet_species, pet.getSpecies()));
        petHobbiesTextView.setText(getContext().getString(R.string.pet_hobbies, pet.getHobbies()));
        petToysTextView.setText(getContext().getString(R.string.pet_favorite_toys, pet.getFavoriteToys()));
        petFoodTextView.setText(getContext().getString(R.string.pet_favorite_food, pet.getFavoriteFood()));
        petAllergiesTextView.setText(getContext().getString(R.string.pet_allergies, pet.getAllergies()));

        return convertView;
    }
}
