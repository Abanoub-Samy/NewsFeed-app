package com.example.bebo.newsfeed_app;

import java.io.Serializable;
 class MainList implements Serializable {
        private String name, location, authorName ,Url , webPublicationDate;

    MainList(String loc, String name, String author, String Url , String webPublicationDate) {
        this.name = name;
        this.location = loc;
        authorName = author;
        this.Url= Url;
        this.webPublicationDate=webPublicationDate;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getUrl() {
        return Url;
    }

     public String getWebPublicationDate() {
         return webPublicationDate;
     }
 }
