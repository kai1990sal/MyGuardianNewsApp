package com.example.mohamedeldimardash.myguardiannewsapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;


/**
 * A {@link NewsAdapter} will create a list item layout for each news item
 * in the data source (a list of {@link NewsItem} objects) to be displayed in a ListView
 */

@SuppressWarnings({"ALL", "unused"})
class NewsAdapter extends ArrayAdapter<NewsItem> {

    private static final String LOG_TAG = NewsAdapter.class.getName();

    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private static Typeface typeface;
    private static Typeface typeface1;


    /**
     * Default Constructor to create a new {@link NewsAdapter} object
     * @param context
     * @param newsItems
     */
    public NewsAdapter(Context context, List<NewsItem> newsItems) {
        super(context, 0, newsItems);
        NewsAdapter.context = context;
        @SuppressWarnings("unused") List<NewsItem> mNewsList = newsItems;

        // Set custom typeface
        typeface = Typeface.createFromAsset(NewsAdapter.context.getAssets(), "fonts/merriweather_regular.ttf");
        typeface1 = Typeface.createFromAsset(NewsAdapter.context.getAssets(), "fonts/merriweather_italic.ttf");
    }

    /**
     * This class describes the view items to create a list item
     */
    static class NewsViewHolder {

        final TextView textViewTitle;
        final TextView textViewSection;
        final TextView textViewDate;
        final TextView textViewAuthor;

        // Find various views within ListView and set custom typeface on them
        NewsViewHolder(View itemView) {
            textViewTitle = itemView.findViewById(R.id.text_news_title);
            textViewSection = itemView.findViewById(R.id.text_news_section);
            textViewDate = itemView.findViewById(R.id.text_news_date);
            textViewAuthor = itemView.findViewById(R.id.text_news_author);

            textViewTitle.setTypeface(typeface);
            textViewAuthor.setTypeface(typeface1);
            textViewDate.setTypeface(typeface);
            textViewSection.setTypeface(typeface);
        }
    }

    /**
     * Returns a list item view that displays information about the news at a given position
     * in the list of news.
     */
    @Override
    public View getView(int position, View convertView,  ViewGroup parent) {

        String title = "";
        String section = "";
        String newsDate = "";
        String author = "";
        NewsViewHolder holder;


        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_list_item, parent, false);
            holder = new NewsViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (NewsViewHolder) convertView.getTag();
        }

        // First check if position is not greater than size of news feed
        if (position < getCount()) {

            // Find news at the given position in the list
            NewsItem currentNews = getItem(position);

            // Set Title
            if ((Objects.requireNonNull(currentNews).getNewsTitle() != null) && (currentNews.getNewsTitle().length() > 0)) {
                title = currentNews.getNewsTitle();
            }

            // Set Section Name
            if ((currentNews.getNewsSection() != null) && (currentNews.getNewsSection().length() > 0)) {
                section = currentNews.getNewsSection();
            }

            // Set Author
            if ((currentNews.getNewsAuthor() != null) && (currentNews.getNewsAuthor().length() > 0)) {
                author = currentNews.getNewsAuthor();
            }

            // Set Date
            if ((currentNews.getNewsPublishedDate() != null) && (currentNews.getNewsPublishedDate().length() > 0)) {
                String date = currentNews.getNewsPublishedDate();
                newsDate = formatDate(date);
            }

        }

        holder.textViewTitle.setText(title);
        holder.textViewSection.setText(section);
        holder.textViewAuthor.setText(author);
        holder.textViewDate.setText(newsDate);

        return convertView;
    }

    /**
     * Method to check if Published Date exists, then format it if the extracted date is a valid date
     */
    private String formatDate(String date) {

        String dateFormatted = "";
        String dateNew = date.substring(0, 10); // gets date in yyyy-mm-dd format from timestamp

        // Format dateNew
        @SuppressLint("SimpleDateFormat") SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat newFormat = new SimpleDateFormat("MMM dd, yyyy");
        try {
            Date dt = inputFormat.parse(dateNew);
            dateFormatted = newFormat.format(dt);
        }
        catch(ParseException pe) {
            Log.e(LOG_TAG, context.getString(R.string.exception_date_format), pe);
        }

        return dateFormatted;
    }

}
