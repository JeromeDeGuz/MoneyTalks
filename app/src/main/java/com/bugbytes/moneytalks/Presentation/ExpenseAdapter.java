package com.bugbytes.moneytalks.Presentation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bugbytes.moneytalks.Business.Services.ExpenseService;
import com.bugbytes.moneytalks.Models.Expense;
import com.bugbytes.moneytalks.R;

import java.util.List;
import java.util.Locale;

//Adapter for displaying expenses in RecyclerView
public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder>
{
    private final List<Expense> expenses;
    private final ExpenseService expenseService;

    //Constructor to initialize adapter (@param: expenses, expenseService)
    public ExpenseAdapter(List<Expense> expenses, ExpenseService expenseService)
    {
        this.expenses = expenses;
        this.expenseService = expenseService;
    }

    //Inflates row layout (@param: parent, viewType)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View row = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.expense_row, parent, false);
        return new ViewHolder(row);
    }

    //Binds expense data to row (@param: holder, position)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        final Expense e = expenses.get(position);

        holder.tvTitle.setText(e.getName());
        holder.tvAmount.setText(String.format(Locale.US, "$%.2f", e.getAmount()));
        holder.tvDate.setText(e.getDate());

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

        holder.deleteButton.setOnClickListener(v -> {
            final int currentPosition = holder.getAdapterPosition();

            if (currentPosition != RecyclerView.NO_POSITION)
            {
                final Expense expenseToDelete = expenses.get(currentPosition);
                final Context context = v.getContext();

                //Deletes expense and updates UI
                if (expenseService.deleteExpense(expenseToDelete))
                {
                    notifyItemRemoved(currentPosition);
                    Toast.makeText(context, "Deleted: " + expenseToDelete.getName(), Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(context, "Failed to delete: " + expenseToDelete.getName(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Returns number of expenses (@return: expenses size)
    @Override
    public int getItemCount()
    {
        return expenses.size();
    }

    //ViewHolder for expense row views
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        final TextView tvTitle, tvAmount, tvDate, tvNote;
        final ImageButton deleteButton;

        //Binds views from layout (@param: itemView)
        ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvNote = itemView.findViewById(R.id.tvNote);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}