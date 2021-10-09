package com.example.testtask;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RequestMovie<T extends MovieDetailsParent> extends Thread{

    protected final int idFilm;
    protected final String address;
    protected final IConnectToServerDetails connectToServer;
    protected final Class<T> aboutT;

    public RequestMovie(int id, IConnectToServerDetails connectToServer, Class<T> aboutT){
        this.idFilm = id;
        this.address = ServerConsts.MAIN_PART_ADDRESS
                + ServerConsts.WORD_MOVIE + "/"
                + Integer.toString(this.idFilm) + "?"
                + ServerConsts.WORD_API_KEY + "=" + ServerConsts.API_KEY + "&"
                + ServerConsts.LANG;
        this.connectToServer = connectToServer;
        this.aboutT = aboutT;
    }

    @Override
    public void run(){
        String JsonString = this.connectFromAddress();
        try {
            T movie = this.parseMovie(JsonString);
            this.connectToServer.finishSuccessful(movie);
        } catch (IOException e) {
            this.connectToServer.finishError();
        }
    }



    private T parseMovie(String JsonString) throws IOException {
        try {
            Moshi moshi = new Moshi.Builder().build();
            JsonAdapter<T> adapter = moshi.adapter(this.aboutT);

            T movie = adapter.fromJson(JsonString);
            return movie;
        }
        catch (Exception e){
            this.connectToServer.finishError();
            return null;
        }
    }

    protected String connectFromAddress()
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
}
