package com.jordylangen.woodstorage.view;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
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
        holder.tagTextView.setText(log.getTag());
        holder.messageTextView.setText(log.getMessage());
        holder.priorityTextView.setText(getPriorityTextResource(log));

        boolean hasException = !TextUtils.isEmpty(log.getException());
        holder.exceptionTextView.setVisibility(hasException ? View.VISIBLE : View.GONE);

        if (hasException) {
            holder.exceptionTextView.setText(log.getException());
        }
    }

    private int getPriorityTextResource(LogStatement log) {
        switch (log.getPriority()) {
            case Log.DEBUG:
                return R.string.log_level_debug;
            case Log.INFO:
                return R.string.log_level_info;
            case Log.WARN:
                return R.string.log_level_warn;
            case Log.ERROR:
                return R.string.log_level_error;
            default:
                return R.string.log_level_verbose;
        }
    }

    @Override
    public int getItemCount() {
        return logs.size();
    }

    public void add(LogStatement logStatement) {
        logs.add(logStatement);
        notifyItemInserted(logs.size());
    }

    public class LogStatementViewHolder extends RecyclerView.ViewHolder {

        private TextView tagTextView;
        private TextView priorityTextView;
        private TextView messageTextView;
        private TextView exceptionTextView;

        public LogStatementViewHolder(View itemView) {
            super(itemView);

            tagTextView = (TextView) itemView.findViewById(R.id.log_tag);
            priorityTextView = (TextView) itemView.findViewById(R.id.log_priority);
            messageTextView = (TextView) itemView.findViewById(R.id.log_message);
            exceptionTextView = (TextView) itemView.findViewById(R.id.log_exception);
        }
    }
}
