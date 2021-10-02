package com.example.testtask;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RequestAndParsePage extends Thread{

    private final String address;
    private final IConnectToServer connectToServer;
    private final int countPage;

    public RequestAndParsePage(String address, IConnectToServer connectToServer, int countPage)
    {
        this.address = address;
        this.connectToServer = connectToServer;

        this.countPage = countPage;
    }

    public void run()
    {
        String JsonString = this.connectFromAddress();
        try {
            Movie[] movies = this.parseMovies(JsonString);
            ArrayList<Movie> res = new ArrayList<Movie>(Arrays.asList(movies));
            this.connectToServer.subTotal(res, 100.0 / this.countPage);
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

    private Movie[] parseMovies(String JsonString) throws IOException {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<ArrayMovie> adapter = moshi.adapter(ArrayMovie.class);

        ArrayMovie movies = adapter.fromJson(JsonString);
        System.out.println(movies.results[0].title + " " + movies.results[0].overview);
        return movies.results;
    }
}
