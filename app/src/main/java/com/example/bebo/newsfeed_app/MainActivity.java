package com.example.bebo.newsfeed_app;


import android.app.LoaderManager;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<MainList>> {
    private ListView newsListView;
    private TextView emptyTextView;
    private List<MainList> arrayListReport;
    private SetAdapter adapter;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newsListView = findViewById(R.id.listview);
        emptyTextView = findViewById(R.id.textView);
        arrayListReport = new ArrayList<>();
        adapter = new SetAdapter(this, (ArrayList<MainList>) arrayListReport);
        newsListView.setAdapter(adapter);
        newsListView.setEmptyView(emptyTextView);
        if (hasNetwork()) {
            getLoaderManager().initLoader(1, null, this);
        } else {
            emptyTextView.setText(R.string.connection_network_text);
        }
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MainList currentReport = arrayListReport.get(i);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(currentReport.getUrl()));
                startActivity(intent);
            }
        });

    }

    @Override
    public android.content.Loader<List<MainList>> onCreateLoader(int id, Bundle args) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String numberReport = sharedPrefs.getString(getString(R.string.key_of_limit_of_report), "10");
        String sectionId = sharedPrefs.getString(getString(R.string.key_of_list_section_of_report), "sport");
        String query = sharedPrefs.getString(getString(R.string.key_of_word_search_of_report), "");
        String URL_NEWS_WEB = "https://content.guardianapis.com/search";
        Uri uri = Uri.parse(URL_NEWS_WEB);
        Uri.Builder uriBuilder = uri.buildUpon();
        if (!sectionId.equals("random")) {
            uriBuilder.appendQueryParameter("section", sectionId);
        }
        if (!(query.isEmpty() || query == null)) {
            uriBuilder.appendQueryParameter("q", query);
        }
        uriBuilder.appendQueryParameter("q","Android");
        uriBuilder.appendQueryParameter("show-tags","contributor");
        uriBuilder.appendQueryParameter("page-size", numberReport);
        uriBuilder.appendQueryParameter("api-key", "26ab4dd4-dba4-46cd-9582-a5db053df07c");
        String url = uriBuilder.toString();
        return new MainLoader(this, url);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_setting) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
        }
        switch (item.getItemId()) {

            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    TaskStackBuilder.create(this)
                            .addNextIntentWithParentStack(upIntent)
                            .startActivities();
                } else {
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLoadFinished(android.content.Loader<List<MainList>> loader, List<MainList> data) {
        if (!hasNetwork()) {
            emptyTextView.setText(R.string.connection_network_text);
        } else {
            updateUI(data);
        }
    }

    @Override
    public void onLoaderReset(android.content.Loader<List<MainList>> loader) {
        adapter.clear();
    }

    private void updateUI(List<MainList> reports) {
        adapter.clear();
        if (reports != null && !reports.isEmpty()) {
            adapter.addAll(reports);
            arrayListReport = reports;
        } else {
            emptyTextView.setText(R.string.empty_news);
        }
    }

    private boolean hasNetwork() {

        ConnectivityManager cM = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (cM != null) {
            networkInfo = cM.getActiveNetworkInfo();
        }
        return networkInfo != null && networkInfo.isConnected();
    }
}
