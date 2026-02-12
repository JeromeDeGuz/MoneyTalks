package com.bugbytes.moneytalks.Presentation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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



        holder.deleteButton.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();

            Expense expenseToDelete = expenses.get(currentPosition);
            Context context = v.getContext();

            // Call the service and check the result
            if (expenseService.deleteExpense(expenseToDelete)) {
                expenses.remove(currentPosition);   //remove from view list
                notifyItemRemoved(currentPosition); //update UI
                Toast.makeText(context, "Deleted: " + expenseToDelete.getName(), Toast.LENGTH_SHORT).show();
            } else {
                // TODO: make a better message?
                Toast.makeText(context, "Failed to delete: " + expenseToDelete.getName(), Toast.LENGTH_SHORT).show();
            }
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
