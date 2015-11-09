package com.project.amit.loginactivity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Created by Amit on 02-03-2015.
 */
public class adapter_nav extends RecyclerView.Adapter<adapter_nav.myViewHolder> {

    private LayoutInflater inflater;
    List<Information_nav> data= Collections.emptyList();
    private Context context;
    private ClickListener clickListener;
    public adapter_nav(Context context, List<Information_nav> data)
    {
        this.context=context;
        inflater=LayoutInflater.from(context);
        this.data=data;
    }
    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View view=inflater.inflate(R.layout.rows_nav,parent,false);
        myViewHolder holder=new myViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(myViewHolder holder, int position) {
         Information_nav current=data.get(position);
         holder.title.setText(current.title);
        holder.icon.setImageResource(current.iconid);
    }

    public void setClickListener(ClickListener clickListener)
    {
        this.clickListener=clickListener;
    }
    @Override
    public int getItemCount() {
        return data.size();
    }
    class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        ImageView icon;

        public myViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            title=(TextView)itemView.findViewById(R.id.list_text);
            icon=(ImageView)itemView.findViewById(R.id.list_icon);
        }

        @Override
        public void onClick(View v) {
            if(clickListener!=null)
            {
                clickListener.itemClicked(v,getPosition());
            }
        }
    }
    public interface ClickListener
    {
        public void itemClicked(View view, int position);
    }
}
