package com.example.testtask;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity {

    private int filmId = -1;

    private DetailsMovie detailsMovie;
    private CreditsMovie creditsMovie;
    private ImageMovie imageMovie;
    private VideoMovie videoMovie;
    private ReviewsMovie reviewsMovie;

    private ImageView poster;
    private TextView title;
    private TextView rating;
    private TextView description;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(@NotNull Message msg) {
            switch (msg.what){
                case 1:
                    setComponentsDetails();
                    break;
                case 2:
                    setComponentsDetails();
                    break;
                case 3:
                    setComponentsDetails();
                    break;
                case 4:
                    setComponentsDetails();
                    break;
                case 5:
                    setComponentsDetails();
                    break;
            }

            super.handleMessage(msg);
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detals);

        Bundle b = getIntent().getExtras();
        if(b != null)
            this.filmId = b.getInt("filmId");

        this.poster = findViewById(R.id.posterInList);
        this.title = findViewById(R.id.titleDetails);
        this.rating = findViewById(R.id.rating);
        this.description = findViewById(R.id.descriptionD);

        RequestDetailsAboutFilm rd = new RequestDetailsAboutFilm(this.filmId, new IConnectToServerDetails() {
            @Override
            public void finishSuccessful(MovieDetailsParent m) {
                detailsMovie = (DetailsMovie)m;
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
            }

            @Override
            public void finishError() {

            }
        });

        RequestCreditsFilm rc = new RequestCreditsFilm(this.filmId, new IConnectToServerDetails() {
            @Override
            public void finishSuccessful(MovieDetailsParent m) {
                creditsMovie = (CreditsMovie)m;
                Message msg = new Message();
                msg.what = 2;
                handler.sendMessage(msg);
            }

            @Override
            public void finishError() {

            }
        });

        RequestVideoFilm rv = new RequestVideoFilm(this.filmId, new IConnectToServerDetails() {
            @Override
            public void finishSuccessful(MovieDetailsParent m) {
                videoMovie = (VideoMovie)m;
                Message msg = new Message();
                msg.what = 3;
                handler.sendMessage(msg);
            }

            @Override
            public void finishError() {

            }
        });

        RequestImageFilm ri = new RequestImageFilm(this.filmId, new IConnectToServerDetails() {
            @Override
            public void finishSuccessful(MovieDetailsParent m) {
                imageMovie = (ImageMovie)m;
                Message msg = new Message();
                msg.what = 4;
                handler.sendMessage(msg);
            }

            @Override
            public void finishError() {

            }
        });

        RequestReviewsFilm rr = new RequestReviewsFilm(this.filmId, new IConnectToServerDetails() {
            @Override
            public void finishSuccessful(MovieDetailsParent m) {
                reviewsMovie = (ReviewsMovie)m;
                Message msg = new Message();
                msg.what = 5;
                handler.sendMessage(msg);
            }

            @Override
            public void finishError() {

            }
        });

        rr.start();
        rd.start();
        rc.start();
        rv.start();
        ri.start();
    }

    private void setComponentsDetails(){
        this.description.setText("hahha");
    }

    private void setComponentsVideos(){
        this.description.setText("hahha");
    }

    private void setComponentsReviews(){
        this.description.setText("hahha");
    }

    private void setComponentsImages(){
        this.description.setText("hahha");
    }

    private void setComponentsCredits(){
        this.description.setText("hahha");
    }

}