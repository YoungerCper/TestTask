package com.example.testtask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.jetbrains.annotations.NotNull;

public class DetailsActivity extends AppCompatActivity {

    private DetailsFragment detailsFragment;
    private ErrorFragment errorFragment = new ErrorFragment(new TryAgain());
    private FragmentTransaction fTrans;
    private LoadFragment loadFragment = new LoadFragment();

    public class TryAgain implements IClickForTry {

        @Override
        public void onClick() {
            loadDetails();
        }
    }

    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(@NotNull Message msg) {
            fTrans = getFragmentManager().beginTransaction();
            if(msg.what == 1){

                fTrans.remove(detailsFragment).remove(errorFragment).remove(loadFragment).add(R.id.frcnt, detailsFragment);

            }
            else{
                fTrans.remove(detailsFragment).remove(errorFragment).remove(loadFragment).add(R.id.frcnt, errorFragment);
            }
            fTrans.commit();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detals);

        Bundle b = getIntent().getExtras();
        if(b != null){
            this.detailsFragment = new DetailsFragment(b.getInt("filmId"));
        }
        else {
            this.detailsFragment = new DetailsFragment(-1);
        }

        this.loadDetails();
    }

    private void loadDetails(){
        fTrans = getFragmentManager().beginTransaction();
        fTrans.remove(detailsFragment).remove(errorFragment).remove(loadFragment).add(R.id.frcnt, loadFragment);
        fTrans.commit();


        this.errorFragment = new ErrorFragment(new TryAgain());
        Thread load = new Thread(){
            @Override
            public void run(){
                Message msg = new Message();
                boolean b = detailsFragment.isLoaded();
                if(b){
                    msg.what = 1;
                    handler.sendMessage(msg);
                    return;
                }

                msg.what = 0;
                handler.sendMessage(msg);
            }


        };
        load.start();
    }



}