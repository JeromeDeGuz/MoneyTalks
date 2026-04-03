package com.bugbytes.moneytalks.presentation;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bugbytes.moneytalks.R;
import com.bugbytes.moneytalks.models.BudgetSummary;

import java.util.List;

public class BudgetAdapter extends RecyclerView.Adapter<BudgetAdapter.BudgetViewHolder>
{
    public interface OnBudgetEditListener
    {
        void onEditBudgetClick(BudgetSummary budgetSummary);
    }

    private List<BudgetSummary> budgetSummaries;
    private final OnBudgetEditListener listener;

    //BudgetAdapter: Constructor to initialize the adapter with budget summaries and listener. Takes in @param budgetSummaries and listener.
    public BudgetAdapter(List<BudgetSummary> budgetSummaries, OnBudgetEditListener listener)
    {
        this.budgetSummaries = budgetSummaries;
        this.listener = listener;
    }

    //setBudgetSummaries: It updates the internal list and refreshes the RecyclerView UI. Takes in @param budgetSummaries.
    public void setBudgetSummaries(List<BudgetSummary> budgetSummaries)
    {
        this.budgetSummaries = budgetSummaries;
        notifyDataSetChanged();
    }

    //onCreateViewHolder: It inflates the layout for a single budget row. Takes in @param parent and viewType and @return BudgetViewHolder.
    @NonNull
    @Override
    public BudgetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_budget, parent, false);
        return new BudgetViewHolder(view);
    }

    //onBindViewHolder: It binds one budget summary row to the UI and sets the edit click listener. Takes in @param holder and position.
    @Override
    public void onBindViewHolder(@NonNull BudgetViewHolder holder, int position)
    {
        BudgetSummary budgetSummary = budgetSummaries.get(position);

        // Resolve theme text color once
        int[] attrs = {android.R.attr.textColorPrimary};
        android.content.res.TypedArray ta = holder.itemView.getContext().obtainStyledAttributes(attrs);
        int themeTextColor = ta.getColor(0, Color.BLACK);
        ta.recycle();

        holder.tvCategoryName.setText(budgetSummary.getCategoryName());
        holder.tvCategoryName.setTextColor(themeTextColor);

        holder.tvBudgetAmount.setText(budgetSummary.getBudget().toPlainString());
        holder.tvBudgetAmount.setTextColor(themeTextColor);

        holder.tvSpentThisMonth.setText(budgetSummary.getSpentThisMonth().toPlainString());

        if (budgetSummary.isOverBudget())
        {
            holder.tvSpentThisMonth.setTextColor(Color.RED);
            holder.tvSpentThisMonth.setTypeface(holder.tvSpentThisMonth.getTypeface(), Typeface.BOLD);
        }
        else
        {
            holder.tvSpentThisMonth.setTextColor(themeTextColor);
            holder.tvSpentThisMonth.setTypeface(holder.tvSpentThisMonth.getTypeface(), Typeface.NORMAL);
        }

        holder.btnEditBudget.setOnClickListener(v -> listener.onEditBudgetClick(budgetSummary));
    }

    //getItemCount: It returns the total number of budget summary rows. Takes in nothing and @return int count.
    @Override
    public int getItemCount()
    {
        return budgetSummaries == null ? 0 : budgetSummaries.size();
    }

    //BudgetViewHolder: Inner class to hold the views for one budget row. Takes in @param itemView.
    static class BudgetViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvCategoryName;
        TextView tvBudgetAmount;
        TextView tvSpentThisMonth;
        ImageButton btnEditBudget;

        public BudgetViewHolder(@NonNull View itemView)
        {
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            tvBudgetAmount = itemView.findViewById(R.id.tvBudgetAmount);
            tvSpentThisMonth = itemView.findViewById(R.id.tvSpentThisMonth);
            btnEditBudget = itemView.findViewById(R.id.btnEditBudget);
        }
    }
}