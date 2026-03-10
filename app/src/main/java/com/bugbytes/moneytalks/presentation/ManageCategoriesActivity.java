package com.bugbytes.moneytalks.presentation;

import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bugbytes.moneytalks.R;
import com.bugbytes.moneytalks.application.MoneyTalksApp;
import com.bugbytes.moneytalks.business.services.CategoryService;
import com.bugbytes.moneytalks.models.Category;

import java.util.ArrayList;
import java.util.List;

public class ManageCategoriesActivity extends AppCompatActivity
        implements CategoryAdapter.OnCategoryEventListener
{
    private CategoryService categoryService;
    private CategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_categories);

        final MoneyTalksApp app = (MoneyTalksApp) getApplication();
        categoryService = app.getCategoryService();

        RecyclerView rvCategories = findViewById(R.id.rvCategories);
        rvCategories.setLayoutManager(new LinearLayoutManager(this));

        adapter = new CategoryAdapter(new ArrayList<>(), this);
        rvCategories.setAdapter(adapter);

        Button btnBackManageCategories = findViewById(R.id.btnBackManageCategories);
        btnBackManageCategories.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        loadCategories();
    }

    private void loadCategories()
    {
        List<Category> categories = categoryService.getAllCategories();
        adapter.setCategories(categories);
    }

    @Override
    public void onAddClick()
    {
        showAddCategoryDialog();
    }

    private void showAddCategoryDialog()
    {
        final EditText input = new EditText(this);
        input.setHint("Enter category name");
        input.setInputType(InputType.TYPE_CLASS_TEXT);

        new AlertDialog.Builder(this)
                .setTitle("Add Category")
                .setView(input)
                .setPositiveButton("Save", (dialog, which) ->
                {
                    String name = input.getText().toString().trim();

                    if (name.isEmpty())
                    {
                        Toast.makeText(this, "Category name cannot be empty", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    try
                    {
                        Category newCategory = new Category(name);
                        categoryService.addCategory(newCategory);
                        Toast.makeText(this, "Category added", Toast.LENGTH_SHORT).show();
                        loadCategories();
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(this, "Failed to add category", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onEditClick(Category category)
    {
        final EditText input = new EditText(this);
        input.setText(category.getName());
        input.setSelection(category.getName().length());
        input.setInputType(InputType.TYPE_CLASS_TEXT);

        new AlertDialog.Builder(this)
                .setTitle("Edit Category")
                .setView(input)
                .setPositiveButton("Save", (dialog, which) ->
                {
                    String newName = input.getText().toString().trim();

                    if (newName.isEmpty())
                    {
                        Toast.makeText(this, "Category name cannot be empty", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    try
                    {
                        category.setName(newName);
                        categoryService.updateCategory(category);
                        Toast.makeText(this, "Category updated", Toast.LENGTH_SHORT).show();
                        loadCategories();
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(this, "Failed to update category", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onDeleteClick(Category category)
    {
        new AlertDialog.Builder(this)
                .setTitle("Delete Category")
                .setMessage("Are you sure you want to delete \"" + category.getName() + "\"?")
                .setPositiveButton("Delete", (dialog, which) ->
                {
                    try
                    {
                        categoryService.deleteCategory(category);
                        Toast.makeText(this, "Category deleted", Toast.LENGTH_SHORT).show();
                        loadCategories();
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(this, "Failed to delete category", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}