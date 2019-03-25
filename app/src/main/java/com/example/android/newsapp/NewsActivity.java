package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    /**
     * Constant value for the news loader ID.
     */
    private static final int NEWS_LOADER_ID = 1;

    /**
     * URL String for news data from the Guardian website created using Uri.builder
     */
    Uri.Builder builder = new Uri.Builder().scheme("https")
            .authority("content.guardianapis.com")
            .appendPath("search")
            .appendQueryParameter("section", "sport")
            .appendQueryParameter("order-by", "newest")
            .appendQueryParameter("use-date", "published")
            .appendQueryParameter("show-fields", "byline")
            .appendQueryParameter("api-key", "da563273-49db-4c73-a5a1-524673f8cc2b");
    String GUARDIAN_JSON_REQUEST = builder.build().toString();

    /**
     * Adapter for the list of news articles
     */
    private NewsAdapter adapter;
    /**
     * TextView that is displayed when the list is empty
     */
    private TextView emptyStateView;

    @Override
    public android.content.Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
//        Create new loader for the given URL
        return new NewsLoader(this, GUARDIAN_JSON_REQUEST);
    }

    @Override
    public void onLoadFinished(android.content.Loader<List<News>> loader, List<News> newsArticles) {
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);
        // Clear the adapter of previous data
        adapter.clear();

        // Set empty state text to display "No News items found."
        emptyStateView.setText(R.string.no_news);

        // If there is a valid list of {@link NewsArticle}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (newsArticles != null && !newsArticles.isEmpty()) {
            adapter.addAll(newsArticles);
        }
    }

    @Override
    public void onLoaderReset(android.content.Loader<List<News>> loader) {
        // Loader reset, so we can clear out our existing data.
        adapter.clear();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_activity);

        // Find a reference to the {@link ListView} in the layout
        ListView newsListView = (ListView) findViewById(R.id.list);

//        Empty view passed to the empty_view TextView
        emptyStateView = (TextView) findViewById(R.id.empty_view);
        newsListView.setEmptyView(emptyStateView);

        // Create a new adapter that takes an empty list of articles as input
        adapter = new NewsAdapter(this, new ArrayList<News>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        newsListView.setAdapter(adapter);
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Find the current article that was clicked on
                News currentArticle = adapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri newsUri = Uri.parse(currentArticle.getUrl());

                // Create a new intent to view the news article URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        // If there is a network connection, fetch data
        if (activeNetwork != null && activeNetwork.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            // Update empty state with no connection error message
            emptyStateView.setText(R.string.no_internet);
        }
    }
}
