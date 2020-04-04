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
import androidx.recyclerview.widget.RecyclerView;

import com.workspace.carnote.R;

import java.text.DateFormat;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private Context context;
    private List<TankUpRecord> tankList;
    TankUpRecord tankUpRecord;
    private Drawable drawable;

    public HistoryAdapter(Context context, List<TankUpRecord> tankList) {
        this.context = context;
        this.tankList = tankList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_history_item,null);
       return new ViewHolder(view);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        tankUpRecord = tankList.get(position);
        drawable = context.getResources().getDrawable(R.drawable.ic_local_gas_station);
        holder.activityImageView.setImageDrawable(drawable);
        DateFormat dateFormat = DateFormat.getDateInstance();
        holder.historyTextViewTopLeft.setText(dateFormat.format(tankUpRecord.getTankUpDate()));
        holder.historyTextViewTopRight.setText("Mileage: "+tankUpRecord.getMileage().toString()+" km");
        holder.historyTextViewBottomLeft.setText("Tanked: "+tankUpRecord.getTankedUpGasLiters().toString()+" L");
        holder.historyTextViewBottomRight.setText("for: "+tankUpRecord.getCostInPLN().toString()+" PLN");
         holder.deleteItemImageView.setOnClickListener(v -> {
             tankList.remove(position);
             HistoryAdapter.this.notifyDataSetChanged();
         });
    }

    @Override
    public int getItemCount() {
        if(tankList == null) return 0;
        else return tankList.size();
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
