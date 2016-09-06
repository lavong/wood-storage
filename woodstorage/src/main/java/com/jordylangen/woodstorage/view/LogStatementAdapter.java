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
import com.jordylangen.woodstorage.utils.ColorUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class LogStatementAdapter extends RecyclerView.Adapter<LogStatementAdapter.LogStatementViewHolder> {

    private static final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());

    private List<LogStatement> logs = new ArrayList<>();
    private Map<String, Integer> tagColors = new HashMap<>();

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
        holder.timestampTextView.setText(TIMESTAMP_FORMAT.format(log.getTimeStamp()));

        boolean hasException = !TextUtils.isEmpty(log.getException());
        holder.exceptionTextView.setVisibility(hasException ? View.VISIBLE : View.GONE);

        if (hasException) {
            holder.exceptionTextView.setText(log.getException());
        }

        int color;
        if (tagColors.containsKey(log.getTag())) {
            color = tagColors.get(log.getTag());
        } else {
            color = ColorUtils.randomColor();
            tagColors.put(log.getTag(), color);
        }

        holder.colorIndicatorView.setBackgroundColor(color);
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

        private View colorIndicatorView;
        private TextView tagTextView;
        private TextView priorityTextView;
        private TextView messageTextView;
        private TextView exceptionTextView;
        private TextView timestampTextView;

        public LogStatementViewHolder(View itemView) {
            super(itemView);

            colorIndicatorView = itemView.findViewById(R.id.log_color_indicator);
            tagTextView = (TextView) itemView.findViewById(R.id.log_tag);
            priorityTextView = (TextView) itemView.findViewById(R.id.log_priority);
            messageTextView = (TextView) itemView.findViewById(R.id.log_message);
            exceptionTextView = (TextView) itemView.findViewById(R.id.log_exception);
            timestampTextView = (TextView) itemView.findViewById(R.id.log_timestamp);
        }
    }
}
