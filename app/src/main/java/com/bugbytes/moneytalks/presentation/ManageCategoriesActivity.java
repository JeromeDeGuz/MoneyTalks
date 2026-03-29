package com.bugbytes.moneytalks.presentation;

import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.LinearLayout;

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

    //onCreate: It sets up the activity layout, initializes the service, and configures the RecyclerView. Takes in @param savedInstanceState.
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

    //onResume: It triggers a data refresh whenever the activity is brought to the foreground. Takes in nothing.
    @Override
    protected void onResume()
    {
        super.onResume();
        loadCategories();
    }

    //loadCategories: It fetches the latest categories from the service and updates the adapter. Takes in nothing.
    private void loadCategories()
    {
        List<Category> categories = categoryService.getAllCategories();
        adapter.setCategories(categories);
    }

    //onAddClick: It handles the callback from the adapter when the user wants to add a category. Takes in nothing.
    @Override
    public void onAddClick()
    {
        showAddCategoryDialog();
    }

    //showAddCategoryDialog: It displays an AlertDialog to capture a new category name and saves it via the service. Takes in nothing.
    private void showAddCategoryDialog()
    {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText nameInput = new EditText(this);
        nameInput.setHint("Enter category name");
        nameInput.setInputType(InputType.TYPE_CLASS_TEXT);

        layout.addView(nameInput);

        new AlertDialog.Builder(this)
                .setTitle("Add Category")
                .setView(layout)
                .setPositiveButton("Save", (dialog, which) ->
                {
                    String name = nameInput.getText().toString().trim();

                    if (name.isEmpty())
                    {
                        Toast.makeText(this, "Category name cannot be empty", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    try
                    {
                        categoryService.addCategory(new Category(name));
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

    //onEditClick: It handles the callback to modify an existing category. Takes in @param category.
    @Override
    public void onEditClick(Category category)
    {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText nameInput = new EditText(this);
        nameInput.setText(category.getName());
        nameInput.setSelection(category.getName().length());
        nameInput.setInputType(InputType.TYPE_CLASS_TEXT);

        layout.addView(nameInput);

        new AlertDialog.Builder(this)
                .setTitle("Edit Category")
                .setView(layout)
                .setPositiveButton("Save", (dialog, which) ->
                {
                    String newName = nameInput.getText().toString().trim();

                    if (newName.isEmpty())
                    {
                        Toast.makeText(this, "Category name cannot be empty", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    try
                    {
                        categoryService.updateCategory(category, new Category(newName));
                        Toast.makeText(this, "Category updated", Toast.LENGTH_SHORT).show();
                        loadCategories();
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(this, "Failed to update: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    //onDeleteClick: It prompts the user for confirmation before removing a category via the service. Takes in @param category.
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