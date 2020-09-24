package com.example.jlne.helper;

import android.os.AsyncTask;

import java.util.HashMap;

public class AsyncTaskHelper extends AsyncTask<Void, Void, String> {
    private String link, requestMethod;
    HashMap<String, String> parameters;
    public AsyncResponse delegate = null;

    public AsyncTaskHelper(String link, String requestMethod, AsyncResponse delegate) {
        this.link = link;
        this.requestMethod = requestMethod;
        this.delegate = delegate;
    }

    public AsyncTaskHelper(String link, String requestMethod, HashMap<String, String> parameters, AsyncResponse delegate) {
        this.link = link;
        this.requestMethod = requestMethod;
        this.parameters = parameters;
        this.delegate = delegate;
    }

    public interface AsyncResponse {
        void processFinish(String result);
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {
        RequestHelper requestHelper = new RequestHelper();
        if (requestMethod.equals("POST")) {
            return requestHelper.sendPostRequest(link, parameters);
        }
        else return requestHelper.sendGetRequest(link);
    }

    @Override
    protected void onPostExecute(String s) {
        delegate.processFinish(s);
        cancel(true);
    }
}
