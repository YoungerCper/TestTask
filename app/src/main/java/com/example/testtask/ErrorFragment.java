package com.example.testtask;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

@SuppressLint("ValidFragment")
public class ErrorFragment extends Fragment {

    private final IClickForTry iClickForTry;
    private Button tryButton;

    public ErrorFragment(IClickForTry iClickForTry){
        this.iClickForTry = iClickForTry;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.error_fragment,
                container, false);
        this.tryButton = view.findViewById(R.id.tryButton);

        this.tryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.tryButton){
                    iClickForTry.onClick();
                }
            }
        });
        return view;
    }

}
