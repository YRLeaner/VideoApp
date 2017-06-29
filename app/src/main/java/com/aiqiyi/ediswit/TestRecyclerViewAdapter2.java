package com.aiqiyi.ediswit;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.aiqiyi.ediswit.entity.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;


public class TestRecyclerViewAdapter2 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Movie> contents;
    Context mContext;
    static final int TYPE_HEADER = 0;
    static final int TYPE_CELL = 1;

    private OnItemClickListener mOnItemClickListener;



    public TestRecyclerViewAdapter2(List<Movie> contents,Context mContext) {
        this.contents = contents;
        this.mContext = mContext;
    }

    public void setmOnItemClickListener(OnItemClickListener onItemClickListener){
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return TYPE_HEADER;
            default:
                return TYPE_CELL;
        }
    }

    @Override
    public int getItemCount() {
        return contents.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_card_small, parent, false);
        return new ViewHolder(view);

    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEADER:
            case TYPE_CELL:
                if (holder instanceof ViewHolder){
                    Picasso.with(mContext).load(contents.get(position).getImg()).into(((ViewHolder) holder).img);
                    ((ViewHolder) holder).short_title.setText(contents.get(position).getShort_title());
                    ((ViewHolder)holder).title.setText(contents.get(position).getTitle());
                }
                break;
        }

        if (mOnItemClickListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.itemView,pos);
                }
            });
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView short_title;
        TextView title;
        public ViewHolder(View view){
            super(view);
            img = (ImageView)view.findViewById(R.id.img);
            short_title = (TextView)view.findViewById(R.id.short_title);
            title = (TextView)view.findViewById(R.id.title);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }
}