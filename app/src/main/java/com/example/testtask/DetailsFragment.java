package com.example.testtask;


import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


@SuppressLint("ValidFragment")
public class DetailsFragment extends Fragment {

    private int filmId = -1;
    private final int minActors = 10;

    private DBHelperFavorite dbHelperFavorite;

    private DetailsMovie detailsMovie = null;
    private CreditsMovie creditsMovie = null;
    private ImageMovie imageMovie = null;
    private ReviewsMovie reviewsMovie = null;

    private ImageView poster;
    private TextView title;
    private TextView rating;
    private TextView description;
    private TextView budget;
    private TextView genres;
    private TextView releaseDate;
    private TextView credits;
    private Button moreOrMelee;
    private TextView username;
    private TextView createDate;
    private TextView contentReview;
    private TextView countVotes;
    private ImageButton favorite;

    private RecyclerView recyclerView;
    private ImageAdapter adapter;
    private List<Bitmap> images;
    static public ImageLoader imageLoader = ImageLoader.getInstance();
    private boolean melee = false;


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
        }, CreditsMovie.class, "credit");

        RequestMovie<ImageMovie> ri = new RequestMovie<ImageMovie>(this.filmId, new IConnectToServerDetails() {
            @Override
            public void finishSuccessful(MovieDetailsParent m) {
                imageMovie = (ImageMovie)m;
            }

            @Override
            public void finishError() {

            }
        }, ImageMovie.class, "image");

        RequestMovie<ReviewsMovie> rr = new RequestMovie<ReviewsMovie>(this.filmId, new IConnectToServerDetails() {
            @Override
            public void finishSuccessful(MovieDetailsParent m) {
                reviewsMovie = (ReviewsMovie)m;
            }

            @Override
            public void finishError() {

            }
        }, ReviewsMovie.class, "review");

        rr.start();
        rd.start();
        rc.start();
        ri.start();

        while (rr.isAlive() || rd.isAlive() || rc.isAlive() || ri.isAlive()){System.out.println(1);}

        return (this.creditsMovie!= null && this.detailsMovie != null && this.imageMovie != null && this.reviewsMovie != null);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.details_fragment,
                container, false);

        this.dbHelperFavorite = new DBHelperFavorite(this.getActivity());

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
        this.recyclerView = view.findViewById(R.id.imageFieldDetails);
        this.credits = view.findViewById(R.id.creditsDtails);
        this.moreOrMelee = view.findViewById(R.id.moreOrMelee);
        this.contentReview = view.findViewById(R.id.contentReview);
        this.createDate = view.findViewById(R.id.created);
        this.username = view.findViewById(R.id.username);
        this.countVotes = view.findViewById(R.id.voteCount);
        this.favorite = view.findViewById(R.id.statDetails);

        if(this.dbHelperFavorite.checkIsDataAlreadyInDBorNotId(detailsMovie.id)){
            favorite.setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(),R.drawable.ic_baseline_star_24));
        }
        else{
            favorite.setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(),R.drawable.ic_baseline_star_24_off));
        }

        this.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dbHelperFavorite.checkIsDataAlreadyInDBorNotId(detailsMovie.id)){
                    dbHelperFavorite.deleteId(detailsMovie.id);
                    favorite.setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(),R.drawable.ic_baseline_star_24_off));
                }else{
                    dbHelperFavorite.insertId(detailsMovie.id);
                    favorite.setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(),R.drawable.ic_baseline_star_24));
                }
            }
        });

        this.moreOrMelee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                melee = !melee;
                setComponentsCredits();
            }
        });

        this.setComponentsCredits();
        this.setComponentsDetails();
        this.setComponentsImages();
        this.setComponentsReviews();
    }

    @SuppressLint("SetTextI18n")
    private void setComponentsDetails(){

        this.description.setText(this.detailsMovie.overview);
        this.title.setText(this.detailsMovie.title);
        this.rating.setText(Double.toString(this.detailsMovie.vote_average));
        this.genres.setText(this.parseToString(this.detailsMovie.genres));
        this.budget.setText(Integer.toString(this.detailsMovie.budget));
        this.releaseDate.setText(this.detailsMovie.release_date);
        this.countVotes.setText(Integer.toString(this.detailsMovie.vote_count));

        imageLoader.displayImage(ServerConsts.IMAGE_MAIN_PART_ADDRESS + this.detailsMovie.poster_path, this.poster);
    }

    private void setComponentsReviews(){
        if(reviewsMovie.results.length > 0) {
            this.username.setText(reviewsMovie.results[0].author_details.username);
            this.contentReview.setText(reviewsMovie.results[0].content);
            this.createDate.setText(reviewsMovie.results[0].created_at);
            return;
        }

        this.username.setText("Отзывов нет");

    }

    private void setComponentsImages(){
        List<Bitmap> l = this.imageMovieToArrayList(imageMovie);
        l.add(imageLoader.loadImageSync(ServerConsts.IMAGE_MAIN_PART_ADDRESS + this.detailsMovie.poster_path));
        Log.d("Meow", String.valueOf(l.size()));
        this.adapter = new ImageAdapter(l, this.getActivity());
        this.recyclerView.setHasFixedSize(true);
        //this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        this.recyclerView.setAdapter(this.adapter);

    }

    private void setComponentsCredits(){
        StringBuilder cr = new StringBuilder("");
        for(int i = 0; i < this.creditsMovie.cast.length && (this.melee || i < this.minActors); i++){
            cr.append(this.creditsMovie.cast[i].character + "   -   " + this.creditsMovie.cast[i].name + "\n");
        }

        this.credits.setText(cr.toString());

    }

    private String parseToString(GenresDetail[] g){
        String ans = "";
        for(GenresDetail i : g){
            ans += i.name;
            ans += " ";
        }
        return ans;
    }

    private ArrayList<Bitmap> imageMovieToArrayList(ImageMovie im){
        ArrayList<Bitmap> ans = new ArrayList<Bitmap>();

        for(int i = 0; i < im.posters.length; i++){
            ans.add(imageLoader.loadImageSync(ServerConsts.IMAGE_MAIN_PART_ADDRESS + im.posters[i].file_path));
        }

        for(int i = 0; i < im.backdrops.length; i++){
            ans.add(imageLoader.loadImageSync(ServerConsts.IMAGE_MAIN_PART_ADDRESS + im.posters[i].file_path));
        }

        return ans;
    }
}
