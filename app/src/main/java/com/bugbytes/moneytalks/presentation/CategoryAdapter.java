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

    public CategoryAdapter(List<Category> categories, OnCategoryEventListener listener)
    {
        this.categories = categories;
        this.listener = listener;
    }

    public void setCategories(List<Category> categories)
    {
        this.categories = categories;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position)
    {
        if (position == categories.size())
        {
            return VIEW_TYPE_ADD;
        }
        return VIEW_TYPE_CATEGORY;
    }

    @Override
    public int getItemCount()
    {
        return categories == null ? 1 : categories.size() + 1;
    }

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

    static class AddCategoryViewHolder extends RecyclerView.ViewHolder
    {
        public AddCategoryViewHolder(@NonNull View itemView)
        {
            super(itemView);
        }
    }
}