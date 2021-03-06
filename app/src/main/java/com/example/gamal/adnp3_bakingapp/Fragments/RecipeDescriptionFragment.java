package com.example.gamal.adnp3_bakingapp.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gamal.adnp3_bakingapp.R;

public class RecipeDescriptionFragment extends Fragment {
    private String description;
    TextView textView;

    public void setDescription(String description) {
        this.description = description;
        if (textView != null)
            textView.setText(description);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_description, container, false);
        textView = rootView.findViewById(R.id.tv_recipe_description);
        textView.setText(description);
        return rootView;
    }
}
