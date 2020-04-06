package com.workspace.carnote.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.workspace.carnote.R;

import java.text.DateFormat;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private Context context;
    private List<Record> recordsList;

    public HistoryAdapter(Context context, List<Record> recordsList) {
        this.context = context;
        this.recordsList = recordsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       @SuppressLint("InflateParams") View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_history_item,null);
       return new ViewHolder(view);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Record record = recordsList.get(position);
        Drawable drawable;
        if(record.getRecordType() == RecordType.TANK_UP) {
            drawable = context.getResources().getDrawable(R.drawable.ic_local_gas_station);
            holder.activityImageView.setImageDrawable(drawable);
            DateFormat dateFormat = DateFormat.getDateInstance();
            holder.historyTextViewTopLeft.setText(dateFormat.format(record.getDate()));
            holder.historyTextViewTopRight.setText("Mileage: " + record.getMileage().toString() + " km");
            holder.historyTextViewBottomLeft.setText("Tanked: " + record.getTankedUpGasLiters().toString() + " L");
            holder.historyTextViewBottomRight.setText("for: " + record.getCostInPLN().toString() + " PLN");
        }else if(record.getRecordType() == RecordType.COST) {
            drawable = context.getResources().getDrawable(R.drawable.ic_history_cost);
            holder.activityImageView.setImageDrawable(drawable);
            DateFormat dateFormat = DateFormat.getDateInstance();
            holder.historyTextViewTopLeft.setText(dateFormat.format(record.getDate()));
            holder.historyTextViewTopRight.setText("ADDITIONAL COST");
            holder.historyTextViewBottomLeft.setText("Cost: " + record.getCostInPLN().toString() + " PLN");
            holder.historyTextViewBottomRight.setText(record.getDescription());
        }
            holder.deleteItemImageView.setOnClickListener(v -> confirmCarRemoveDialog(position));

    }

    private void confirmCarRemoveDialog(int position) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this.context);
        dialog  .setMessage("REMOVE THE SELECTED RECORD?")
                .setPositiveButton("NO", (dialog1, id) -> dialog1.cancel())
                .setNegativeButton("YES", (dialog12, id) -> {
                    recordsList.remove(position);
                    HistoryAdapter.this.notifyDataSetChanged();
                })
                .show();
    }

    @Override
    public int getItemCount() {
        if(recordsList == null) return 0;
        else return recordsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView activityImageView;
        ImageView deleteItemImageView;
        TextView historyTextViewTopLeft;
        TextView historyTextViewBottomLeft;
        TextView historyTextViewTopRight;
        TextView historyTextViewBottomRight;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.activityImageView = itemView.findViewById(R.id.history_item_image);
            this.deleteItemImageView = itemView.findViewById(R.id.delete_item_image_view);

            this.historyTextViewTopLeft = itemView.findViewById(R.id.history_text_view_top_left);
            this.historyTextViewBottomLeft = itemView.findViewById(R.id.history_text_view_bottom_left);
            this.historyTextViewTopRight = itemView.findViewById(R.id.history_text_view_top_right);
            this.historyTextViewBottomRight = itemView.findViewById(R.id.history_text_view_bottom_right);
        }
    }
}
