package com.example.android.newsapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class NewsAdapter extends ArrayAdapter<News> {

    //    Declare a String separator for the date string to exclude the time
    private static final String separator = "T";
    //    date_string variable declared to hold date string after separation
    String date_string;

    public NewsAdapter(Activity context, ArrayList<News> newsArticles) {

        super(context, 0, newsArticles);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_list, parent, false);
        }

        // Get the {@link News} object located at this position in the list
        final News currentArticle = getItem(position);

        // Find the TextView in the news_list.xml layout with the ID section
        TextView section_textView = (TextView) listItemView.findViewById(R.id.section);
        String section = currentArticle.getSection();
        section_textView.setText(section);

        // Find the TextView in the news_list.xml layout with the ID title
        TextView title_textView = (TextView) listItemView.findViewById(R.id.title);
        String news_title = currentArticle.getTitle();
        title_textView.setText(news_title);

        // Find the TextView in the news_list.xml layout with the ID author
        TextView author_textView = (TextView) listItemView.findViewById(R.id.author);
        String author = currentArticle.getAuthor();
        author_textView.setText(author);

        // Get Date and split string
        String date = currentArticle.getDate();
//        If separator is found, separate strings at the separator
        if (date.contains(separator)) {
            String[] strings = date.split(separator);
            date_string = strings[0]; // Only require the date so anything from separator "T" is ignored
        }

        // Find the TextView in the news_list.xml layout with the ID date
        TextView date_textView = (TextView) listItemView.findViewById(R.id.date);
        date_textView.setText(date_string);

        // Return the whole list item layout so that it can be shown in the ListView.
        return listItemView;
    }

}
