package com.example.caloriefit.view;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caloriefit.R;
import com.example.caloriefit.model.FoodEntity;

import java.util.List;

public class FoodRecyclerViewAdapter extends ListAdapter <FoodEntity, FoodRecyclerViewAdapter.FoodViewHolder> {

    private static Context context;

    protected FoodRecyclerViewAdapter(@NonNull DiffUtil.ItemCallback<FoodEntity> diffCallback) {
        super(diffCallback);
    }

    static class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView foodNameView;
        private final TextView foodCaloriesView;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);

            foodNameView = itemView.findViewById(R.id.item_name);
            foodCaloriesView = itemView.findViewById(R.id.item_calories);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int id = getLayoutPosition();
            Intent intent = new Intent(context, EditFoodActivity.class);
            intent.putExtra("id", id);
            context.startActivity(intent);
        }
    }

    @NonNull
    @Override
    public FoodRecyclerViewAdapter.FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FoodViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.food_view,
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull FoodRecyclerViewAdapter.FoodViewHolder holder, int position) {
        context = holder.itemView.getContext();
        FoodEntity current = getItem(position);
        holder.foodNameView.setText(current.getName());
        holder.foodCaloriesView.setText(current.getCalories() + " kcal");
    }

    static class FoodDiff extends DiffUtil.ItemCallback<FoodEntity>{

        @Override
        public boolean areItemsTheSame(@NonNull FoodEntity oldItem, @NonNull FoodEntity newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull FoodEntity oldItem, @NonNull FoodEntity newItem) {
            return (oldItem.getName().equals(newItem.getName())) && (oldItem.getCalories() == newItem.getCalories());
        }
    }
}
