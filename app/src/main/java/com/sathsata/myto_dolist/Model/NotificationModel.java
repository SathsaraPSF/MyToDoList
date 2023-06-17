package com.sathsata.myto_dolist.Model;

public class NotificationModel {
    private int id;
    private  String notification;

    public NotificationModel() {
    }

    public NotificationModel(String notification) {
        this.notification = notification;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }
}
