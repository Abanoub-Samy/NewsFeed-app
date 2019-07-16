package com.example.bebo.newsfeed_app;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;
 class MainLoader extends AsyncTaskLoader<List<MainList>> {
    private String mURL;

    MainLoader(Context context, String url) {
        super(context);
        mURL = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<MainList> loadInBackground() {
        return new NetworkUtils().fetchDataFromURL(mURL);
    }
}
