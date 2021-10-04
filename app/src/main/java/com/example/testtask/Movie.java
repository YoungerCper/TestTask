package com.example.testtask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Movie {
    
    public String poster_path;
    public boolean adult;
    public String overview;
    public String release_date;
    public int[] genre_ids;
    public int id;
    public String title;
    public String background_path;
    public double popularity;
    public int vote_count;
    public boolean video;
    public double vote_average;
    public String original_title;
    public String original_language;

    public Bitmap imageMap = null;

    public void setImageMap(Bitmap bm)
    {
        this.imageMap = bm;
    }

    public String getImageUrl()
    {
        return ServerConsts.IMAGE_MAIN_PART_ADDRESS + this.poster_path;
    }

}
