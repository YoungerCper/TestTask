package com.example.testtask;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MoviesAdapter extends ArrayAdapter {

    private Context context;
    private ArrayList<Movie> listOfMovie;
    private LayoutInflater inflater;

    public MoviesAdapter(Context context, ArrayList<Movie> movies)
    {
        super(context, R.layout.component_movie_for_list);
        this.context = context;
        this.listOfMovie = movies;
        this.inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        t2.setText(m.overview);
        t.setText(m.title);
        return view;
    }
}
