package com.example.mohamedeldimardash.myguardiannewsapp;
/**
 * A {@link NewsItem} object that contains details related to a single
 * news item to be displayed in the list
 */

@SuppressWarnings("ALL")
class NewsItem {

    /** News Title */
    private final String newsTitle;

    /** News Section */
    private final String newsSection;

    /** News Published Date */
    private final String newsPublishedDate;

    /** News Author */
    private final String newsAuthor;

    /** News Web URL */
    private final String newsUrl;


    /**
     * Default Constructor to construct a {@link NewsItem} object
     * @param newsTitle
     * @param newsSection
     * @param newsPublishedDate
     * @param newsAuthor
     * @param newsUrl
     */
    public NewsItem(String newsTitle, String newsSection, String newsPublishedDate,
                    String newsAuthor, String newsUrl) {

        this.newsTitle = newsTitle;
        this.newsSection = newsSection;
        this.newsPublishedDate = newsPublishedDate;
        this.newsAuthor = newsAuthor;
        this.newsUrl = newsUrl;
    }

    /** Getter method - News Title */
    public String getNewsTitle() {
        return newsTitle;
    }

    /** Getter method - News Section */
    public String getNewsSection() {
        return newsSection;
    }

    /** Getter method - News Published Date */
    public String getNewsPublishedDate() {
        return newsPublishedDate;
    }

    /** Getter method - News Author */
    public String getNewsAuthor() {
        return newsAuthor;
    }

    /** Getter method - News Web URL */
    public String getNewsUrl() {
        return newsUrl;
    }

}


