package com.example.testtask;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RequestVideoFilm extends Thread{
    private final int idFilm;
    private final String address;
    private final IConnectToServerDetails connectToServer;

    public RequestVideoFilm(int id, IConnectToServerDetails connectToServer){
        this.idFilm = id;
        this.address = ServerConsts.MAIN_PART_ADDRESS
                + ServerConsts.WORD_MOVIE + "/"
                + Integer.toString(this.idFilm) + "/"
                + ServerConsts.WORD_VIDEOS + "?"
                + ServerConsts.WORD_API_KEY + "=" + ServerConsts.API_KEY + "&"
                + ServerConsts.LANG;
        this.connectToServer = connectToServer;
    }

    @Override
    public void run(){
        String JsonString = this.connectFromAddress();
        try {
            VideoMovie movie = this.parseMovie(JsonString);
            this.connectToServer.finishSuccessful(null);
        } catch (IOException e) {
            this.connectToServer.finishError();
        }
    }

    private String connectFromAddress()
    {
        URL url = null;
        try {
            url = new URL(this.address);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            //print result
            System.out.println(response.toString());

            return response.toString();
        }catch (IOException e) {
            this.connectToServer.finishError();
        }
        return null;
    }

    private VideoMovie parseMovie(String JsonString) throws IOException {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<VideoMovie> adapter = moshi.adapter(VideoMovie.class);

        VideoMovie movie = adapter.fromJson(JsonString);
        return movie;
    }
}
