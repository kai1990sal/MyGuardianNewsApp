package com.example.mohamedeldimardash.myguardiannewsapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static android.content.Context.CONNECTIVITY_SERVICE;


/**
 * Fragment to display list of news items as fetched from Guardian News API
 */

@SuppressWarnings("ALL")
public class NewsFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<NewsItem>>,
        SwipeRefreshLayout.OnRefreshListener {

    public static final String LOG_TAG = NewsFragment.class.getName();

    /** Guardian API Base URL */
    private static final String API_BASE_URL = "http://content.guardianapis.com/search?q=";
    private static final String API_SEARCH_URL = "https://content.guardianapis.com/search?section=";

    private View view;
    private NewsAdapter newsAdapter;
    private TextView textView;
    private static List<NewsItem> newsItems;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int loaderId;


    /**
     * Method to return a new instance of the news fragment
     * @param newsSection
     * @return newsFragment
     */
    public static NewsFragment newInstance(String newsSection, int menuPosition){
        NewsFragment newsFragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putString("section", newsSection);
        args.putInt("position", menuPosition);
        newsFragment.setArguments(args);
        return newsFragment;
    }

    /**
     * Method to get News Section Name
     * @return section
     */
    private String getSection() {
        return Objects.requireNonNull(getArguments()).getString("section", "");
    }

    /**
     * Method to get Selected Menu Position
     * @return menu position
     */
    private int getMenuPosition() {
        return Objects.requireNonNull(getArguments()).getInt("position", 0);
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_news, container, false);

        // Activate SwipeRefreshLayout feature so news list is updated when screen in swiped
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.colorAccent));

        // Find a reference to the {@link ListView} in the layout
        ListView mNewsListView = view.findViewById(R.id.list_news);

        // Set empty view
        textView = view.findViewById(R.id.text_empty_list);
        mNewsListView.setEmptyView(textView);

        // Create a new adapter that takes the list as input
        newsItems = new ArrayList<>();
        newsAdapter = new NewsAdapter(getContext(), newsItems);
        mNewsListView.setAdapter(newsAdapter);

        // Get a reference to the ConnectivityManager to check state of network connectivity
        //  ConnectivityManager connMgr = (ConnectivityManager)
        //  getActivity().getSystemService(CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        // NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = Objects.requireNonNull(getActivity()).getSupportLoaderManager();

            // Get loaderId from menu position, so that a different loaderId is assigned for each section
            loaderId = getMenuPosition();
            loaderManager.initLoader(loaderId, null, this);

        } else {
            // Hide loading indicator and show empty state view
            View progressIndicator = view.findViewById(R.id.progress_indicator);
            textView.setText(R.string.error_no_connection);
            progressIndicator.setVisibility(View.GONE);
        }

        mNewsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                NewsItem currentNews = newsAdapter.getItem(position);
                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri newsUri = Uri.parse(Objects.requireNonNull(currentNews).getNewsUrl());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        return view;
    }


    @Override
    public Loader<List<NewsItem>> onCreateLoader(int i, Bundle bundle) {

        String section = getSection();
        String url;
        String orderBy;
        long longFromDate;
        String fromDate;

        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        if (section.equals(getString(R.string.menu_home).toLowerCase())) {
            url = API_BASE_URL;
        } else {
            url = API_SEARCH_URL + section;
        }

        Uri baseUri = Uri.parse(url);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        // Get SharedPreferences
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        longFromDate = sharedPrefs.getLong(
                getString(R.string.settings_from_date_key), 0
        );

        Date dateObject = new Date(longFromDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateObject);
        fromDate = dateFormat.format(calendar.getTime());

        // Append parameters obtained fro, SharedPreferences
        uriBuilder.appendQueryParameter("order-by", orderBy);
        uriBuilder.appendQueryParameter("from-date", fromDate);

        return new NewsLoader(getContext(), uriBuilder.toString());
    }

    @Override
    public void onLoadFinished( Loader<List<NewsItem>> loader, List<NewsItem> newsItems) {

        swipeRefreshLayout.setRefreshing(false);

        // Hide progress indicator because the data has been loaded
        View progressIndicator = view.findViewById(R.id.progress_indicator);
        progressIndicator.setVisibility(View.GONE);

        // Check if connection is still available, otherwise show appropriate message
        if (isConnected()) {

            // Set empty state text when no news found
            if (newsItems == null || newsItems.size() == 0) {
                textView.setVisibility(View.VISIBLE);
                textView.setText(getString(R.string.info_no_news));
            } else {
                textView.setVisibility(View.GONE);
            }

            NewsFragment.newsItems.clear();

            // If there is a valid list of {@link Book}s, then add them to the adapter's
            // data set. This will trigger the ListView to update.
            if (newsItems != null && !newsItems.isEmpty()) {
                NewsFragment.newsItems.addAll(newsItems);
                newsAdapter.notifyDataSetChanged();
            }
        } else {
            textView.setText(R.string.error_no_connection);
        }
    }

    @Override
    public void onLoaderReset( Loader<List<NewsItem>> loader) {
        newsAdapter.clear();
    }

    @Override
    public void onRefresh() {
        Objects.requireNonNull(getActivity()).getSupportLoaderManager().restartLoader(loaderId, null, this);
    }

    /**
     * Method to check network connectivity
     * @return true/false
     */
    private boolean isConnected() {
        boolean hasNetwork;

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                Objects.requireNonNull(getActivity()).getSystemService(CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = Objects.requireNonNull(connMgr).getActiveNetworkInfo();

        // If there is a network connection, fetch data
        hasNetwork = networkInfo != null && networkInfo.isConnected();

        return hasNetwork;
    }

}

