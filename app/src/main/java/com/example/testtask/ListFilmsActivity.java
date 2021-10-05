package com.example.testtask;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.akexorcist.roundcornerprogressbar.IconRoundCornerProgressBar;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ListFilmsActivity extends AppCompatActivity {

    private EditText _searchInput;
    private ImageButton _searchButton;
    private ListView _scrollView;
    private IconRoundCornerProgressBar _progressBar;

    private ArrayList<Movie> movies;
    private MoviesAdapter adapter;
    private Handler mProgressHandler;
    private Handler mProgressBarHandler;

    private Context mainContext = this;

    private boolean withUpdateButton = true;

    class ListFilmsActivityClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            if(v.getId() == R.id.searchButton)
            {
                String query = _searchInput.getText().toString();
                UpdateMovieListCommand r = new UpdateMovieListCommand(new ListFilmsActivityRequestListener(), query);
                r.start();
            }
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
        }

        @Override
        public void finishSuccessful(){
            movies = this.newMovie;
            Message msg;
            msg = Message.obtain();
            msg.what = 1;
            System.out.println(msg.what);
            mProgressHandler.sendMessage(msg);
            withUpdateButton = false;
        }

        @Override
        public void finishError(){
            if(withUpdateButton){
                //_scrollView.addHeaderView();
            }
            else{

            }
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

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_films);

        this._searchButton = findViewById(R.id.searchButton);
        this._searchInput = findViewById(R.id.searchInput);
        this._scrollView = findViewById(R.id.moviesSpace);
        this._progressBar = findViewById(R.id.progressBar);

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);

        movies = new ArrayList<Movie>();
        adapter = new MoviesAdapter(this, movies);
        _scrollView.setAdapter(adapter);

        mProgressHandler = new Handler() {
            @Override
            public void handleMessage(@NotNull Message msg) {
                System.out.println(msg.what);
                switch (msg.what) {
                    case 1:
                        adapter = new MoviesAdapter(mainContext, movies);
                        _scrollView.setAdapter(adapter);
                        _progressBar.setProgress(0);
                        break;
                    case 2:
                        _progressBar.setProgress(_progressBar.getProgress() + (float)msg.obj);
                        break;
                    case 3:
                        _progressBar.setProgress(0);
                        break;
                }
                super.handleMessage(msg);
            }
        };


        UpdateMovieListCommand firstUpdate = new UpdateMovieListCommand(new ListFilmsActivityRequestListener());
        firstUpdate.start();

        this._searchButton.setOnClickListener(new ListFilmsActivityClickListener());
    }


}