package com.jordylangen.woodstorage.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jordylangen.woodstorage.LogStatement;
import com.jordylangen.woodstorage.R;

import java.util.ArrayList;
import java.util.List;

public class LogStatementAdapter extends RecyclerView.Adapter<LogStatementAdapter.LogStatementViewHolder> {

    private List<LogStatement> logs;

    public LogStatementAdapter() {
        logs = new ArrayList<>();
    }

    @Override
    public LogStatementViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_log, parent, false);
        return new LogStatementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LogStatementViewHolder holder, int position) {
        LogStatement log = logs.get(position);
        holder.messageTextView.setText(log.getMessage());
    }

    @Override
    public int getItemCount() {
        return logs.size();
    }

    public void add(LogStatement logStatement) {
        logs.add(logStatement);
        notifyDataSetChanged();
    }

    public class LogStatementViewHolder extends RecyclerView.ViewHolder {

        private TextView messageTextView;

        public LogStatementViewHolder(View itemView) {
            super(itemView);
            messageTextView = (TextView) itemView.findViewById(R.id.log_message);
        }
    }
}
