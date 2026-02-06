package com.bugbytes.moneytalks.Presentation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bugbytes.moneytalks.Models.Expense;
import com.bugbytes.moneytalks.R;

import java.util.List;
import java.util.Locale;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {

    private final List<Expense> expenses;

    public ExpenseAdapter(List<Expense> expenses) {
        this.expenses = expenses;
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

        // Title -> use category (since your model doesn't have name getter)
        holder.tvTitle.setText(e.getCategory());

        // Amount
        holder.tvAmount.setText(String.format(Locale.US, "$%.2f", e.getAmount()));

        // Date + Note (note is optional)
        String note = e.getNote();
        if (note != null && !note.trim().isEmpty()) {
            holder.tvDate.setText(e.getDate() + " • " + note);
        } else {
            holder.tvDate.setText(e.getDate());
        }
    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvAmount, tvDate;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvDate = itemView.findViewById(R.id.tvDate);
        }
    }
}
