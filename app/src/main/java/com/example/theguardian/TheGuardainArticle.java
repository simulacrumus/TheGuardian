package com.example.theguardian;

/**
 * Class that is used to instantiate objects that represents The Guardian articles
 * @author Emrah Kinay
 * @version 1.0.0
 */
public class TheGuardainArticle {

    /**
     * The title of the article
     */
    private String title;

    /**
     * The url of the article
     */
    private String url;

    /**
     * The section of the article
     */
    private String sectionName;

    /**
     * The text of the article
     */
    private String bodyText;

    /**
     * The date of the article
     */
    private String date;

    /**
     * The url of the image of the article
     */
    private String imageUrl;

    /**
     * The id of the orticle in database
     */
    private long id;

    /**
     *
     * @param title
     * @param url
     * @param sectionName
     * @param bodyText
     * @param date
     * @param imageUrl
     */
    public TheGuardainArticle(String title, String url, String sectionName, String bodyText, String date, String imageUrl) {
        this(title, url, sectionName, bodyText, date, imageUrl,-1l);
    }

    /**
     *
     * @param title
     * @param url
     * @param sectionName
     * @param bodyText
     * @param date
     * @param imageUrl
     * @param id
     */
    public TheGuardainArticle(String title, String url, String sectionName, String bodyText, String date,String imageUrl, long id) {
        setTitle(title);
        setUrl(url);
        setSectionName(sectionName);
        setBodyText(bodyText);
        setDate(date);
        setId(id);
        setImageUrl(imageUrl);
    }

    /**
     * Returns the article text
     * @return
     */
    public String getBodyText() {
        return bodyText;
    }

    /**
     * Sets the article text
     * @param bodyText
     */
    public void setBodyText(String bodyText) {
        bodyText.replace("\n","\n\n\n");
        this.bodyText = bodyText;
    }

    /**
     * Returns the date of the article
     * @return
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets the date of the article
     * @param date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Returns the url of the image of the article
     * @return
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * Sets the url of the image of the article
     * @param imageUrl
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * Sets the id of the article in the database
     * @param id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Returns the id of the article in database
     * @return
     */
    public long getId() {
        return id;
    }

    /**
     * Returns the title of the article
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the article
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the url of the article
     * @return
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the url of the article
     * @param url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Returns the section name of the article
     * @return
     */
    public String getSectionName() {
        return sectionName;
    }

    /**
     * Sets the section name of the article
     * @param sectionName
     */
    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

}
