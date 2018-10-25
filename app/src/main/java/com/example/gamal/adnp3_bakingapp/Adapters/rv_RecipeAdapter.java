package com.example.gamal.adnp3_bakingapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gamal.adnp3_bakingapp.Models.Recipe;
import com.example.gamal.adnp3_bakingapp.R;

import java.util.List;

public class rv_RecipeAdapter extends RecyclerView.Adapter<rv_RecipeAdapter.ViewHolder> {
    private List<Recipe> recipes;
    private Context context;

    public interface OnItemClickListener {
        void onItemClick(int Id);
    }

    private final OnItemClickListener onItemClickListener;

    public rv_RecipeAdapter(Context context, List<Recipe> recipes, OnItemClickListener listener) {
        this.recipes = recipes;
        this.context = context;
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.rc_recipe_card_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        if (i % 2 == 0)
            holder.recipeImage.getLayoutParams().height = 820;
        else
            holder.recipeImage.getLayoutParams().height = 700;
        holder.recipeName.setText(recipes.get(i).getName());
        holder.recipeDuration.setText((int) (Math.random() * 45) + " Min");
        holder.recipeRating.setRating((float) Math.random() * 5);
        if (recipes.get(i).getImage() == 0) {
            switch (recipes.get(i).getName()) {
                case "Brownies":
                    holder.recipeImage.setImageResource(R.drawable.brownies);
                    break;

                case "Nutella Pie":
                    holder.recipeImage.setImageResource(R.drawable.nutella_pie);
                    break;

                case "Yellow Cake":
                    holder.recipeImage.setImageResource(R.drawable.yellow_cake);
                    break;

                case "Cheesecake":
                    holder.recipeImage.setImageResource(R.drawable.cheesecake);
                    break;


            }
        } else {
            holder.recipeImage.setImageResource(recipes.get(i).getImage());

        }
    }

    @Override
    public int getItemCount() {
        if (recipes == null)
            return 0;
        return recipes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView recipeName;
        TextView recipeDuration;
        ImageView recipeImage;
        RatingBar recipeRating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeName = itemView.findViewById(R.id.recipe_name);
            recipeDuration = itemView.findViewById(R.id.recipe_duration);
            recipeImage = itemView.findViewById(R.id.recipe_image);
            recipeRating = itemView.findViewById(R.id.recipe_rating);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int Id = recipes.get(getAdapterPosition()).getId();
            onItemClickListener.onItemClick(Id);
        }
    }
}
