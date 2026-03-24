package com.bugbytes.moneytalks.presentation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bugbytes.moneytalks.R;
import com.bugbytes.moneytalks.models.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    public interface OnCategoryEventListener
    {
        void onAddClick();

        void onEditClick(Category category);

        void onDeleteClick(Category category);
    }

    private static final int VIEW_TYPE_CATEGORY = 0;
    private static final int VIEW_TYPE_ADD = 1;

    private List<Category> categories;
    private final OnCategoryEventListener listener;

    //CategoryAdapter: Constructor to initialize the adapter with a list and listener. Takes in @param categories and listener.
    public CategoryAdapter(List<Category> categories, OnCategoryEventListener listener)
    {
        this.categories = categories;
        this.listener = listener;
    }

    //setCategories: It updates the internal list and refreshes the RecyclerView UI. Takes in @param categories.
    public void setCategories(List<Category> categories)
    {
        this.categories = categories;
        notifyDataSetChanged();
    }

    //getItemViewType: It determines if an item is a regular category or the 'Add' button. Takes in @param position and @return view type int.
    @Override
    public int getItemViewType(int position)
    {
        if (position == categories.size())
        {
            return VIEW_TYPE_ADD;
        }
        return VIEW_TYPE_CATEGORY;
    }

    //getItemCount: It returns the total number of items including the extra 'Add' item. Takes in nothing and @return total count.
    @Override
    public int getItemCount()
    {
        return categories == null ? 1 : categories.size() + 1;
    }

    //onCreateViewHolder: It inflates the correct layout based on the view type. Takes in @param parent and viewType and @return ViewHolder.
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == VIEW_TYPE_ADD)
        {
            View view = inflater.inflate(R.layout.item_add_category, parent, false);
            return new AddCategoryViewHolder(view);
        }

        View view = inflater.inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    //onBindViewHolder: It binds data to the ViewHolder and sets click listeners for actions. Takes in @param holder and position.
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        if (getItemViewType(position) == VIEW_TYPE_ADD)
        {
            holder.itemView.setOnClickListener(v -> listener.onAddClick());
            return;
        }

        Category category = categories.get(position);
        CategoryViewHolder categoryHolder = (CategoryViewHolder) holder;

        categoryHolder.tvCategoryName.setText(category.getName());
        categoryHolder.btnEditCategory.setOnClickListener(v -> listener.onEditClick(category));
        categoryHolder.btnDeleteCategory.setOnClickListener(v -> listener.onDeleteClick(category));
    }

    //CategoryViewHolder: Inner class to hold references to category item UI components. Takes in @param itemView.
    static class CategoryViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvCategoryName;
        ImageButton btnEditCategory;
        ImageButton btnDeleteCategory;

        public CategoryViewHolder(@NonNull View itemView)
        {
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            btnEditCategory = itemView.findViewById(R.id.btnEditCategory);
            btnDeleteCategory = itemView.findViewById(R.id.btnDeleteCategory);
        }
    }

    //AddCategoryViewHolder: Inner class to hold the UI reference for the add category item. Takes in @param itemView.
    static class AddCategoryViewHolder extends RecyclerView.ViewHolder
    {
        public AddCategoryViewHolder(@NonNull View itemView)
        {
            super(itemView);
        }
    }
}