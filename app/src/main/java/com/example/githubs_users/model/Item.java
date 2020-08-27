package com.example.githubs_users.model;

public class Item {
   private String login;
   private String avatar_url;
   private String html_url;
   private String id;

    public Item() {
    }

    public Item(String login, String avatar_url, String html_url, String id) {
        this.login = login;
        this.avatar_url = avatar_url;
        this.html_url = html_url;
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getHtml_url() {
        return html_url;
    }

    public void setHtml_url(String html_url) {
        this.html_url = html_url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
