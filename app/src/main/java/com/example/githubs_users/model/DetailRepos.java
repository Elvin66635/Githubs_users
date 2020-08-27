package com.example.githubs_users.model;

import java.util.Date;

public class DetailRepos {
    private int id;
    private int stargazers_count;
    private String name;
    private String language;
    private Date updated_at;

    public DetailRepos(String name, String language, int stargazers_count) {
        this.name = name;
        this.language = language;
        this.stargazers_count = stargazers_count;
    }

    public String getLanguage() {
        return language;
    }

    public int getStargazers_count() {
        return stargazers_count;
    }

    public void setStargazers_count(int stargazers_count) {
        this.stargazers_count = stargazers_count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    @Override
    public String toString() {
        return  name + " | " + language + " | " + "Stars: "+stargazers_count;
    }
}
