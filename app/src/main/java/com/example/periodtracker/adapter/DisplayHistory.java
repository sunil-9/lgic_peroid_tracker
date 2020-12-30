package com.example.periodtracker.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.periodtracker.R;
import com.example.periodtracker.model.HistoryModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.Calendar;

public class DisplayHistory  extends FirebaseRecyclerAdapter<
        HistoryModel, DisplayHistory.HistoryViewholder> {
    Context c;

    public DisplayHistory(
            @NonNull FirebaseRecyclerOptions<HistoryModel> options, Context c) {
        super(options);
        this.c = c;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull DisplayHistory.HistoryViewholder holder, int position, @NonNull HistoryModel model) {

        Long data = model.getStartTime();
       Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(data);

        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        holder.history_text.setText(""+mYear+"-"+(mMonth+1)+"-"+mDay);
    }
    @NonNull
    @Override
    public DisplayHistory.HistoryViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view
                = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.display_history_item, parent, false);

        return new DisplayHistory.HistoryViewholder(view);
    }

    public class HistoryViewholder extends RecyclerView.ViewHolder {
        public TextView history_text ;

        public HistoryViewholder(@NonNull View itemView) {
            super(itemView);

            history_text = itemView.findViewById(R.id.displayHistoryDetails);
        }
    }
}
