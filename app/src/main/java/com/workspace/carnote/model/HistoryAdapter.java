package com.workspace.carnote.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
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
    private Record record;
    private Drawable drawable;

    public HistoryAdapter(Context context, List<Record> recordsList) {
        this.context = context;
        this.recordsList = recordsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_history_item,null);
       return new ViewHolder(view);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        record = recordsList.get(position);
        if(record.getRecordType() == RecordType.TANK_UP) {
            drawable = context.getResources().getDrawable(R.drawable.ic_local_gas_station);
            holder.activityImageView.setImageDrawable(drawable);
            DateFormat dateFormat = DateFormat.getDateInstance();
            holder.historyTextViewTopLeft.setText(dateFormat.format(record.getDate()));
            holder.historyTextViewTopRight.setText("Mileage: " + record.getMileage().toString() + " km");
            holder.historyTextViewBottomLeft.setText("Tanked: " + record.getTankedUpGasLiters().toString() + " L");
            holder.historyTextViewBottomRight.setText("for: " + record.getCostInPLN().toString() + " PLN");
        }else if(record.getRecordType() == RecordType.COLLISION) {
            drawable = context.getResources().getDrawable(R.drawable.ic_history_collision);
            holder.activityImageView.setImageDrawable(drawable);
            DateFormat dateFormat = DateFormat.getDateInstance();
            holder.historyTextViewTopLeft.setText(dateFormat.format(record.getDate()));
            holder.historyTextViewTopRight.setText("COLLISION");
            holder.historyTextViewBottomLeft.setText("Cost: " + record.getCostInPLN().toString() + " PLN");
            holder.historyTextViewBottomRight.setText(record.getDescription());
        }else if(record.getRecordType() == RecordType.REPAIR) {
            drawable = context.getResources().getDrawable(R.drawable.ic_history_repair);
            holder.activityImageView.setImageDrawable(drawable);
            DateFormat dateFormat = DateFormat.getDateInstance();
            holder.historyTextViewTopLeft.setText(dateFormat.format(record.getDate()));
            holder.historyTextViewTopRight.setText("REPAIR");
            holder.historyTextViewBottomLeft.setText("Cost: " + record.getCostInPLN().toString() + " PLN");
            holder.historyTextViewBottomRight.setText(record.getDescription());
        }else if(record.getRecordType() == RecordType.COST) {
            drawable = context.getResources().getDrawable(R.drawable.ic_history_cost);
            holder.activityImageView.setImageDrawable(drawable);
            DateFormat dateFormat = DateFormat.getDateInstance();
            holder.historyTextViewTopLeft.setText(dateFormat.format(record.getDate()));
            holder.historyTextViewTopRight.setText("ADDITIONAL COST");
            holder.historyTextViewBottomLeft.setText("Cost: " + record.getCostInPLN().toString() + " PLN");
            holder.historyTextViewBottomRight.setText(record.getDescription());
        }
            holder.deleteItemImageView.setOnClickListener(v -> {
                confirmCarRemoveDialog(position);
            });

    }

    private void confirmCarRemoveDialog(int position) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this.context);
        dialog  .setMessage("REMOVE THE SELECTED RECORD?")
                .setPositiveButton("NO",  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                })
                .setNegativeButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int id) {
                        recordsList.remove(position);
                        HistoryAdapter.this.notifyDataSetChanged();
                    }
                })
                .show();
    }

    @Override
    public int getItemCount() {
        if(recordsList == null) return 0;
        else return recordsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        protected ImageView activityImageView;
        protected ImageView deleteItemImageView;
        protected TextView historyTextViewTopLeft;
        protected TextView historyTextViewBottomLeft;
        protected TextView historyTextViewTopRight;
        protected TextView historyTextViewBottomRight;

        public ViewHolder(@NonNull View itemView) {
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
