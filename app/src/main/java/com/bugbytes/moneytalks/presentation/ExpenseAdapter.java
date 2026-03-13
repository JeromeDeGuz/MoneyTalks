package com.bugbytes.moneytalks.presentation;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bugbytes.moneytalks.models.Expense;
import com.bugbytes.moneytalks.R;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

//Adapter for displaying expenses in RecyclerView
public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder>
{
    private List<Expense> expenses;
    private final OnExpenseEventListener listener;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    // Interface to delegate events back to the Activity (Suggestion #13)
    public interface OnExpenseEventListener
    {
        void onDeleteClick(Expense expense, int position);
    }

    //Constructor to initialize adapter (@param: expenses, listener)
    public ExpenseAdapter(List<Expense> expenses, OnExpenseEventListener listener)
    {
        this.expenses = expenses;
        this.listener = listener;
    }

    // Allows updating the dataset without recreating the adapter (Suggestion #14)
    public void setExpenses(List<Expense> newExpenses)
    {
        this.expenses = newExpenses;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View row = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.expense_row, parent, false);
        return new ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        final Expense e = expenses.get(position);

        holder.tvTitle.setText(e.getName());
        holder.tvAmount.setText(String.format(Locale.US, "$%s", e.getAmount().toPlainString()));
        holder.tvDate.setText(e.getDate().format(DATE_FORMATTER));

        // Updated: Set the category text
        holder.tvCategory.setText(e.getCategory());

        final String note = e.getNote();
        if (note != null && !note.trim().isEmpty())
        {
            holder.tvNote.setText(note);
            holder.tvNote.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.tvNote.setVisibility(View.GONE);
        }

        holder.deleteButton.setOnClickListener(v ->
        {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION && listener != null)
            {
                listener.onDeleteClick(expenses.get(currentPosition), currentPosition);
            }
        });

        // Edit button logic (Keep as is)
        holder.editButton.setOnClickListener(v ->
        {
            openEditScreen(v, holder.getAdapterPosition());
        });

        // Improvement: Optional row click to edit
        holder.itemView.setOnClickListener(v ->
        {
            openEditScreen(v, holder.getAdapterPosition());
        });
    }

    // Helper method to keep code clean
    private void openEditScreen(View v, int position)
    {
        if (position != RecyclerView.NO_POSITION)
        {
            Expense expenseToEdit = expenses.get(position);
            Intent i = new Intent(v.getContext(), AddAndEditExpense.class);
            i.putExtra(AddAndEditExpense.EXTRA_EXPENSE, expenseToEdit);
            v.getContext().startActivity(i);
        }
    }

    @Override
    public int getItemCount()
    {
        return expenses.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        // Added tvCategory to the ViewHolder
        final TextView tvTitle, tvAmount, tvDate, tvNote, tvCategory;
        final ImageButton deleteButton;
        final ImageButton editButton;

        ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvNote = itemView.findViewById(R.id.tvNote);
            tvCategory = itemView.findViewById(R.id.tvCategory); // Link to the new layout ID
            deleteButton = itemView.findViewById(R.id.deleteButton);
            editButton = itemView.findViewById(R.id.editButton);
        }
    }
}