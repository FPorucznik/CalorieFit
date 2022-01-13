package com.example.caloriefit.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caloriefit.R;
import com.example.caloriefit.model.FoodEntity;

public class FoodRecyclerViewAdapter extends ListAdapter <FoodEntity, FoodRecyclerViewAdapter.FoodViewHolder> {

    protected FoodRecyclerViewAdapter(@NonNull DiffUtil.ItemCallback<FoodEntity> diffCallback) {
        super(diffCallback);
    }

    static class FoodViewHolder extends RecyclerView.ViewHolder{

        private final TextView foodNameView;
        private final TextView foodCaloriesView;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);

            foodNameView = itemView.findViewById(R.id.item_name);
            foodCaloriesView = itemView.findViewById(R.id.item_calories);
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
