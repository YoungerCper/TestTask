package com.example.testtask;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
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
    private IconRoundCornerProgressBar _progressBar;

    private MovieListFragment movieListFragment;
    private LoadFragment loadFragment = new LoadFragment();
    private ErrorFragment errorFragment = new ErrorFragment(new TryAgain());
    private FragmentTransaction fTrans;

    private final Handler handler = new Handler(){
        @Override
        public void handleMessage(@NotNull Message msg) {
            fTrans = getFragmentManager().beginTransaction();
            if(msg.what == 1){
                fTrans.remove(errorFragment).remove(loadFragment).remove(movieListFragment).add(R.id.listMovieFrame, movieListFragment);

            }
            else{
                fTrans.remove(errorFragment).remove(loadFragment).remove(movieListFragment).add(R.id.listMovieFrame, errorFragment);

            }
            fTrans.commit();
        }
    };

    class TryAgain implements IClickForTry{

        @Override
        public void onClick() {
            fTrans.remove(errorFragment).remove(loadFragment).remove(movieListFragment).add(R.id.listMovieFrame, loadFragment);
            begin();
        }
    }

    class ListFilmsActivityClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            if(v.getId() == R.id.searchButton)
            {
                String query = _searchInput.getText().toString();
                Thread h = new Thread() {

                    @Override
                    public void run() {
                        movieListFragment.loadNewList(query);
                    }
                };
                h.start();
            }
        }
    }

    class InteractionProgressBar implements IProgressInteraction{

        @Override
        public float getProgress() {
            return _progressBar.getProgress();
        }

        @Override
        public void setProgress(float f) {
            _progressBar.setProgress(f);
        }
    }

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_films);

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);

        this.movieListFragment = new MovieListFragment(new InteractionProgressBar());

        fTrans = getFragmentManager().beginTransaction();
        fTrans.remove(this.errorFragment).remove(this.loadFragment).remove(this.movieListFragment).add(R.id.listMovieFrame, this.loadFragment);
        fTrans.commit();

        this._searchButton = findViewById(R.id.searchButton);
        this._searchInput = findViewById(R.id.searchInput);
        this._progressBar = findViewById(R.id.progressBar);



        this.begin();

        this._searchButton.setOnClickListener(new ListFilmsActivityClickListener());
    }

    private void begin() {
        fTrans = getFragmentManager().beginTransaction();


        Thread h = new Thread() {

            @Override
            public void run() {
                boolean b = movieListFragment.loader();
                Message msg = new Message();
                if (b) {
                    msg.what = 1;
                }
                else {
                    msg.what = 0;
                }
                handler.sendMessage(msg);
            }
        };
        h.start();
    }


}