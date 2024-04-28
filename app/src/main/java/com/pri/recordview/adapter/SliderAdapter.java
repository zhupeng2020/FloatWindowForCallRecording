package com.pri.recordview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.pri.recordview.R;
import com.pri.recordview.bean.SliderImageData;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.MyViewHolder> {
    private final List<SliderImageData> mLists;
    private final LayoutInflater mLayoutInflater;


    public SliderAdapter(Context context, List<SliderImageData> list) {
        this.mLists = list;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.slider_item, parent,
                false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final SliderImageData bean = mLists.get(position);
        if (bean == null) {
            return;
        }
        holder.imageView.setImageDrawable(bean.getAppIcon());
    }

    @Override
    public int getItemCount() {
        return mLists.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
        }
    }
}
