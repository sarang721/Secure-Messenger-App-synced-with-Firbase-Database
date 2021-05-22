package com.example.chat;

public class post {

    String name,username;

    public post() {
    }

    public post(String name, String username) {
        this.name = name;
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "post{" +
                "name='" + name + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}

