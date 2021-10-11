package com.example.testtask;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<Bitmap> listBitmap;

    private ImageLoader imageLoader = ImageLoader.getInstance();
    private Bitmap defaultBm;
    private final LayoutInflater inflater;

    public ImageAdapter(List<Bitmap> im, Context context)
    {
        this.inflater = LayoutInflater.from(context);
        this.listBitmap = im;
        System.out.println(listBitmap.size());
        this.defaultBm = imageLoader.loadImageSync("https://cs4.pikabu.ru/images/previews_comm/2015-06_2/14339500161051.png");
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecyclerView.ViewHolder(inflater.inflate(R.layout.component_image_in_list, parent, false)) {
        };
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ImageView i = holder.itemView.findViewById(R.id.posterInListDetails);
        i.setImageBitmap(this.listBitmap.get(position));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return this.listBitmap.size();
    }

}
