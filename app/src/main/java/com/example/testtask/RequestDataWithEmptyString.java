package com.example.testtask;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class RequestDataWithEmptyString extends Thread
{

    private final IConnectToServer connectToServer;
    private final String address;

    private int countOgPages = -1;

    public RequestDataWithEmptyString(IConnectToServer connectToServer)
    {
        this.connectToServer = connectToServer;
        this.address = ServerConsts.MAIN_PART_ADDRESS
                + ServerConsts.WORD_DISCOVER + "/"
                + ServerConsts.WORD_MOVIE +"?"
                + ServerConsts.WORD_API_KEY + "=" + ServerConsts.API_KEY
                + "&" + ServerConsts.LANG;
    }

    public void run()
    {
        String JsonString = this.request();
        if(JsonString != null) {
            this.connectToServer.startConnecting();
            this.parsePageCount(JsonString);
            this.parseMovies();
        }
    }

    private String request()
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

    private void parsePageCount(String JsonString)
    {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<CountPages> adapter = moshi.adapter(CountPages.class);
        try {
            CountPages ans = adapter.fromJson(JsonString);
            this.countOgPages = ans.total_pages;
        } catch (Exception e) {
            e.printStackTrace();
            this.connectToServer.finishError();
        }
    }

    private void parseMovies()
    {
        int countOfThread = Math.min(40, this.countOgPages);
        RequestAndParsePage[] p = new RequestAndParsePage[countOfThread];
        int count = 0;

        for(int i = 0; count < this.countOgPages; i = ( i + 1) % countOfThread)
        {
            if(p[i] == null || !p[i].isAlive())
            {
                System.out.println(count);
                p[i] = new RequestAndParsePage(this.address + "&" + ServerConsts.WORD_PAGE + "=" + Integer.toString(count + 1), this.connectToServer, this.countOgPages);
                count++;
                p[i].start();
            }
        }

        boolean d = true;
        while(d) {
            d = false;

            for (int i = 0; i < countOfThread; i++) {
                d = d || p[i].isAlive();
            }
        }
        this.connectToServer.finishSuccessful();
    }
}
