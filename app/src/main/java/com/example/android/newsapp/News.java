package com.example.android.newsapp;

public class News {
    private String section;
    private String title;
    private String author;
    private String date;
    private String url;

    /**
     * Create a new NewsArticle object or public constructor
     */
    public News(String section_name, String titles, String author_name, String pub_date, String news_url) {
        section = section_name;
        title = titles;
        author = author_name;
        date = pub_date;
        url = news_url;
    }

    /**
     * Declare Getter methods to return news data
     */

    public String getSection() {
        return section;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }

    public String getUrl() {
        return url;
    }

}

