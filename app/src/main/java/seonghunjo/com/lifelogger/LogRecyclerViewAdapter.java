package seonghunjo.com.lifelogger;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
/**
 * Created by Seonghun on 2015-12-20.
 */

public class LogRecyclerViewAdapter extends RecyclerView.Adapter<LogRecyclerViewAdapter.ViewHolder> {
    Context context;
    List<Log> items;
    int item_layout;

    public LogRecyclerViewAdapter(Context context, List<Log> items, int item_layout) {
        this.context=context;
        this.items=items;
        this.item_layout=item_layout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(item_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Log item = items.get(position);

        holder.titleView.setText(item.title);
        holder.dateView.setText(item.date);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleView;
        TextView dateView;

        public ViewHolder(View itemView) {
            super(itemView);
            titleView = (TextView)itemView.findViewById(R.id.firstLine);
            dateView = (TextView)itemView.findViewById(R.id.secondLine);
        }
    }
}