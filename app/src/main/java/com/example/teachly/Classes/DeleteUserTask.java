package com.example.teachly.Classes;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.URI;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DeleteUserTask extends AsyncTask<Void,Void,String> {

    String uId;
    public DeleteUserTask(String uId){
        this.uId = uId;
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\"permanent\":true}");
            String url = "https://2548689ee4e78b36.api-us.cometchat.io/v3/users/"+uId;
            Request request = new Request.Builder()
                    .url(url)
                    .delete(body)
                    .addHeader("accept", "application/json")
                    .addHeader("content-type", "application/json")
                    .addHeader("apikey", "a90d769410e09cc76b8c9b5a4aaec7b84a2582c3")
                    .build();

            Response response = client.newCall(request).execute();
            return response.body().toString();
        } catch (IOException e) {
            return e.getMessage();
        }
    }
}
