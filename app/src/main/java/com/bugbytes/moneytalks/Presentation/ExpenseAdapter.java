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

//connect expense data to UI rowz
public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder>
{

    private final List<Expense> expenses;

    //cons recieves expense liat
    public ExpenseAdapter(List<Expense> expenses)
    {
        this.expenses = expenses;
    }

    @NonNull
    @Override
    //create new row view when needed
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View row = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.expense_row, parent, false);
        return new ViewHolder(row);
    }

    //binds expense data to row at given pos
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        Expense e = expenses.get(position);

        //set title as name of expense
        holder.tvTitle.setText(e.getName());

        holder.tvAmount.setText(String.format(Locale.US, "$%.2f", e.getAmount()));

        //Date + Note (note is optional)
        String note = e.getNote();
        if (note != null && !note.trim().isEmpty())
        {
            holder.tvDate.setText(e.getDate() + " • " + note);
        }
        else
        {
            holder.tvDate.setText(e.getDate());
        }
    }

    //total # of items for later use
    @Override
    public int getItemCount()
    {
        return expenses.size();
    }

    //holds reference to ui element for single expense row
    static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvTitle, tvAmount, tvDate;

        ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            //connectring TextViews from the row layout
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvDate = itemView.findViewById(R.id.tvDate);
        }
    }
}
