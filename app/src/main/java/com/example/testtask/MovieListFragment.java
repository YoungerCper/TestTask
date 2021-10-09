package com.example.testtask;


import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

@SuppressLint({"ValidFragment", "HandlerLeak"})
public class MovieListFragment extends Fragment {

    private final IProgressInteraction iProgressInteraction;
    private ListView _scrollView;
    private ArrayList<Movie> movies = null;
    private MoviesAdapter adapter;
    private final Handler mProgressHandler = new Handler() {

        @Override
        public void handleMessage(@NotNull Message msg) {
            System.out.println(msg.what);
            switch (msg.what) {
                case 1:
                    if(!firstTime) {
                        adapter = new MoviesAdapter(new PressBox(), movies);
                        _scrollView.setAdapter(adapter);
                    }
                    iProgressInteraction.setProgress(0);
                    firstTime = false;
                    break;
                case 2:
                    iProgressInteraction.setProgress(iProgressInteraction.getProgress() + (float)msg.obj);
                    break;
                case 3:
                    iProgressInteraction.setProgress(0);
                    break;
                case 4:
                    Toast t = Toast.makeText(getActivity(), "Что-то пошло не так", Toast.LENGTH_LONG);
                    t.show();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private boolean upDated = true;
    private boolean firstTime = true;

    class PressBox implements IPressBox{

         public void onClick(int id){
             Intent intent = new Intent(getActivity(), DetailsActivity.class);
             intent.putExtra("filmId", id);
             getActivity().startActivity(intent);
         }

         public Context getContext(){
             return getActivity();
         }
    }

    class ListFilmsActivityRequestListener implements IConnectToServer
    {

        public ArrayList<Movie> newMovie;

        public ListFilmsActivityRequestListener()
        {
            this.newMovie = new ArrayList<Movie>();
        }

        public class F{
            public final float f;
            public F(float f){this.f = f;}
        }

        @Override
        public void startConnecting(){
            movies = this.newMovie;
            Message msg;
            msg = Message.obtain();
            msg.what = 3;
            System.out.println(msg.what);
            mProgressHandler.sendMessage(msg);
            upDated = false;
        }

        @Override
        public void finishSuccessful(){
            movies = this.newMovie;
            Message msg;
            msg = Message.obtain();
            msg.what = 1;
            System.out.println(msg.what);
            mProgressHandler.sendMessage(msg);
            upDated = true;
        }

        @Override
        public void finishError(){
            if(!firstTime){
               Message msg = new Message();
               msg.what = 4;
               mProgressHandler.sendMessage(msg);
            }
            upDated = false;
        }

        @Override
        public void subTotal(ArrayList<Movie> newMovies, double percent){
            this.newMovie.addAll(newMovies);
            movies = this.newMovie;
            Message msg;
            msg = Message.obtain();
            msg.what = 2;
            msg.obj = (float)percent;
            mProgressHandler.sendMessage(msg);
        }

    }

    public MovieListFragment(IProgressInteraction iProgressInteraction){
        this.iProgressInteraction = iProgressInteraction;
    }



    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movie_list_fragment,
                container, false);

        this._scrollView = view.findViewById(R.id.moviesSpace);

        adapter = new MoviesAdapter(new PressBox(), movies);
        _scrollView.setAdapter(adapter);
        return view;
    }

    public boolean loadNewList(String query){
        UpdateMovieListCommand r = new UpdateMovieListCommand(new ListFilmsActivityRequestListener(), query);
        r.start();

        while(r.isAlive()){}

        return this.upDated;
    }

    public boolean loader(){
        UpdateMovieListCommand firstUpdate = new UpdateMovieListCommand(new ListFilmsActivityRequestListener());
        firstUpdate.start();

        while (firstUpdate.isAlive()){}
        return this.upDated;
    }
}
