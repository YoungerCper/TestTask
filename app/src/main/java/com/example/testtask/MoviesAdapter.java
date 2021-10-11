package com.example.testtask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import androidx.core.content.ContextCompat;

public class MoviesAdapter extends ArrayAdapter {

    private IPressBox context;
    private ArrayList<Movie> listOfMovie;
    private LayoutInflater inflater;

    private ImageLoader imageLoader = ImageLoader.getInstance();
    private Bitmap defaultBm;

    private DBHelperFavorite dbHelperFavorite;


    public MoviesAdapter(IPressBox context, ArrayList<Movie> movies, DBHelperFavorite dbHelperFavorite)
    {
        super(context.getContext(), R.layout.component_movie_for_list);
        this.context = context;
        this.listOfMovie = movies;
        this.inflater = (LayoutInflater) this.context.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.dbHelperFavorite = dbHelperFavorite;

        this.defaultBm = imageLoader.loadImageSync("https://cs4.pikabu.ru/images/previews_comm/2015-06_2/14339500161051.png");
    }

    @Override
    public int getCount() {
        return this.listOfMovie.size();
    }

    @Override
    public Object getItem(int position) {
        return this.listOfMovie.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if(view == null)
        {
            view = this.inflater.inflate(R.layout.component_movie_for_list, parent, false);
        }
        Movie m = (Movie)this.getItem(position);

        TextView t = view.findViewById(R.id.titleFilm);
        TextView t2 = view.findViewById(R.id.description);
        ImageView poster = view.findViewById(R.id.posterInList);
        LinearLayout l = view.findViewById(R.id.box);

        l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.onClick(m.id);
            }
        });

        ImageButton ButtonStar = (ImageButton) view.findViewById(R.id.star);
        ButtonStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dbHelperFavorite.checkIsDataAlreadyInDBorNotId(m.id)){
                    dbHelperFavorite.deleteId(m.id);
                    ButtonStar.setImageDrawable(ContextCompat.getDrawable(context.getContext().getApplicationContext(),R.drawable.ic_baseline_star_24_off));
                }else{
                    dbHelperFavorite.insertId(m.id);
                    ButtonStar.setImageDrawable(ContextCompat.getDrawable(context.getContext().getApplicationContext(),R.drawable.ic_baseline_star_24));
                }
            }
        });

        if(this.dbHelperFavorite.checkIsDataAlreadyInDBorNotId(m.id)){
            ButtonStar.setImageDrawable(ContextCompat.getDrawable(context.getContext().getApplicationContext(),R.drawable.ic_baseline_star_24));
        }
        else{
            ButtonStar.setImageDrawable(ContextCompat.getDrawable(context.getContext().getApplicationContext(),R.drawable.ic_baseline_star_24_off));
        }

        System.out.println(m.poster_path);

        poster.setImageBitmap(this.defaultBm);
        imageLoader.displayImage(m.getImageUrl(), poster);

        t2.setText(m.overview);
        t.setText(m.title);
        return view;
    }
}
