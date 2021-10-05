package com.example.testtask;

import java.util.ArrayList;

public interface IConnectToServer
{
    void startConnecting();

    void finishSuccessful();
    void finishError();

    default void subTotal(ArrayList<Movie> newMovies, double percent){return;}

}
