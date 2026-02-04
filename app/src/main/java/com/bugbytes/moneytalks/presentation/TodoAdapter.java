package com.bugbytes.moneytalks.presentation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bugbytes.moneytalks.R;
import com.bugbytes.moneytalks.models.Expense;

import java.util.ArrayList;
import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoViewHolder> {

    private final List<Expense> items = new ArrayList<>();

    public TodoAdapter(List<Expense> initialItems) {
        setItems(initialItems);
    }

    public void setItems(List<Expense> newItems) {
        items.clear();
        if (newItems != null) {
            items.addAll(newItems);
        }
        notifyDataSetChanged();
    }

    public Expense getItemAt(int position) {
        return items.get(position);
    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.todo_row, parent, false);
        return new TodoViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
        Expense item = items.get(position);

        holder.title.setText(item.getTitle());

        // Avoid showing "null" in the UI
        String desc = item.getDescription();
        holder.desc.setText(desc == null ? "" : desc);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class TodoViewHolder extends RecyclerView.ViewHolder {
        final TextView title;
        final TextView desc;

        TodoViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.itemTitle);
            desc = itemView.findViewById(R.id.itemDesc);
        }
    }
}
