package com.bugbytes.moneytalks.Presentation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bugbytes.moneytalks.Business.Services.ExpenseService;
import com.bugbytes.moneytalks.Models.Expense;
import com.bugbytes.moneytalks.R;

import java.util.List;
import java.util.Locale;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {

    private final List<Expense> expenses;
    private final ExpenseService expenseService;

    public ExpenseAdapter(List<Expense> expenses, ExpenseService expenseService) {
        this.expenses = expenses;
        this.expenseService = expenseService;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.expense_row, parent, false);
        return new ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Expense e = expenses.get(position);

        holder.tvTitle.setText(e.getName());
        holder.tvAmount.setText(String.format(Locale.US, "$%.2f", e.getAmount()));
        holder.tvDate.setText(e.getDate());

        String note = e.getNote();
        if (note != null && !note.trim().isEmpty()) {
            holder.tvNote.setText(note);
            holder.tvNote.setVisibility(View.VISIBLE);
        } else {
            holder.tvNote.setVisibility(View.GONE);
        }



        // The listener now directly calls the service to handle the deletion
        holder.deleteButton.setOnClickListener(v -> {
            // Get the position of the item only ONCE
            int currentPosition = holder.getAdapterPosition();

            Expense expenseToDelete = expenses.get(currentPosition);

            //call business/logic layer to handle deletion
            expenseService.deleteExpense(expenseToDelete);

            //notify android recyclerview that item deleted
            notifyItemRemoved(currentPosition);
        });
    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvAmount, tvDate, tvNote;
        Button deleteButton;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvNote = itemView.findViewById(R.id.tvNote);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
