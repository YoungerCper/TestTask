package com.example.testtask;


import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.jetbrains.annotations.NotNull;




@SuppressLint("ValidFragment")
public class DetailsFragment extends Fragment {

    private int filmId = -1;

    private DetailsMovie detailsMovie = null;
    private CreditsMovie creditsMovie = null;
    private ImageMovie imageMovie = null;
    private VideoMovie videoMovie = null;
    private ReviewsMovie reviewsMovie = null;
    
    private ImageLoader imageLoader = ImageLoader.getInstance();

    private ImageView poster;
    private HorizontalScrollView imagesField;
    private TextView title;
    private TextView rating;
    private TextView description;
    private TextView budget;
    private TextView genres;
    private TextView releaseDate;


    public DetailsFragment(int filmId){
        this.filmId = filmId;
    }

    public boolean isLoaded(){
        RequestMovie<DetailsMovie> rd = new RequestMovie<DetailsMovie>(this.filmId, new IConnectToServerDetails() {
            @Override
            public void finishSuccessful(MovieDetailsParent m) {
                detailsMovie = (DetailsMovie)m;
            }

            @Override
            public void finishError() {

            }
        }, DetailsMovie.class);

        RequestMovie<CreditsMovie> rc = new RequestMovie<CreditsMovie>(this.filmId, new IConnectToServerDetails() {
            @Override
            public void finishSuccessful(MovieDetailsParent m) {
                creditsMovie = (CreditsMovie)m;
            }

            @Override
            public void finishError() {

            }
        }, CreditsMovie.class);

        RequestMovie<VideoMovie> rv = new RequestMovie<VideoMovie>(this.filmId, new IConnectToServerDetails() {
            @Override
            public void finishSuccessful(MovieDetailsParent m) {
                videoMovie = (VideoMovie)m;
            }

            @Override
            public void finishError() {

            }
        }, VideoMovie.class);

        RequestMovie<ImageMovie> ri = new RequestMovie<ImageMovie>(this.filmId, new IConnectToServerDetails() {
            @Override
            public void finishSuccessful(MovieDetailsParent m) {
                imageMovie = (ImageMovie)m;
            }

            @Override
            public void finishError() {

            }
        }, ImageMovie.class);

        RequestMovie<ReviewsMovie> rr = new RequestMovie<ReviewsMovie>(this.filmId, new IConnectToServerDetails() {
            @Override
            public void finishSuccessful(MovieDetailsParent m) {
                reviewsMovie = (ReviewsMovie)m;
            }

            @Override
            public void finishError() {

            }
        }, ReviewsMovie.class);

        rr.start();
        rd.start();
        rc.start();
        rv.start();
        ri.start();

        while (rr.isAlive() || rd.isAlive() || rc.isAlive() || rv.isAlive() || ri.isAlive()){System.out.println(1);}

        return (this.creditsMovie!= null && this.detailsMovie != null && this.imageMovie != null && this.reviewsMovie != null && this.videoMovie != null);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.details_fragment,
                container, false);
        this.init(view);

        return view;
    }

    private void init(View view){
        this.poster = view.findViewById(R.id.posterDetails);
        this.title = view.findViewById(R.id.titleDetails);
        this.rating = view.findViewById(R.id.rating);
        this.description = view.findViewById(R.id.descriptionD);
        this.budget = view.findViewById(R.id.budgetDetails);
        this.releaseDate = view.findViewById(R.id.releaseDetails);
        this.genres = view.findViewById(R.id.genresDetails);
        this.imagesField = view.findViewById(R.id.imageFieldDetails);

        this.setComponentsCredits();
        this.setComponentsDetails();
        this.setComponentsImages();
        this.setComponentsReviews();
        this.setComponentsVideos();
    }

    @SuppressLint("SetTextI18n")
    private void setComponentsDetails(){

        this.description.setText(this.detailsMovie.overview);
        this.title.setText(this.detailsMovie.title);
        this.rating.setText(Double.toString(this.detailsMovie.vote_average));
        this.genres.setText(this.parseToString(this.detailsMovie.genres));
        this.budget.setText(Integer.toString(this.detailsMovie.budget));
        this.releaseDate.setText(this.detailsMovie.release_date);

        imageLoader.displayImage(ServerConsts.IMAGE_MAIN_PART_ADDRESS + this.detailsMovie.poster_path, this.poster);
    }

    private void setComponentsVideos(){

    }

    private void setComponentsReviews(){

    }

    private void setComponentsImages(){

    }

    private void setComponentsCredits(){

    }

    private String parseToString(GenresDetail[] g){
        String ans = "";
        for(GenresDetail i : g){
            ans += i.name;
            ans += " ";
        }
        return ans;
    }
}
