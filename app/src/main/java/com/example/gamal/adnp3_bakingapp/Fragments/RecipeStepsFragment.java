package com.example.gamal.adnp3_bakingapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.gamal.adnp3_bakingapp.BakingVideo_Description;
import com.example.gamal.adnp3_bakingapp.Models.Steps;
import com.example.gamal.adnp3_bakingapp.R;

import java.util.List;

public class RecipeStepsFragment extends Fragment {
    List<Steps> steps;
    boolean isTablet = false;
    onItemClickListener mCallBack;

    public interface onItemClickListener {
        void onStepClicked(int Id);
    }

    public void setTablet(boolean isTablet) {
        this.isTablet = isTablet;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallBack = (onItemClickListener) context;
    }

    public void setSteps(List<Steps> steps) {
        this.steps = steps;
    }

    public RecipeStepsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View roorView = inflater.inflate(R.layout.fragement_recipe_steps, container, false);
        ListView ls = roorView.findViewById(R.id.ls_steps);
        String[] values = new String[steps.size()];
        for (int i = 0; i < steps.size(); i++) {
            values[i] = steps.get(i).getShortDescription();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, android.R.id.text1, values);
        ls.setAdapter(adapter);
        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (isTablet) {
                    mCallBack.onStepClicked(steps.get(i).getId());
                } else {
                    Intent intent = new Intent(roorView.getContext(), BakingVideo_Description.class);
                    intent.putExtra(BakingVideo_Description.DESCRIPTION, steps.get(i).getDescription());
                    if (steps.get(i).getVidepURL().equals(""))
                        intent.putExtra(BakingVideo_Description.VIDEO_URL, steps.get(i).getThumbnailURL());
                    else
                        intent.putExtra(BakingVideo_Description.VIDEO_URL, steps.get(i).getVidepURL());
                    startActivity(intent);
                }
            }
        });
        return roorView;
    }
}
