package com.sathsata.myto_dolist.Model;

import android.graphics.Bitmap;

public class ModelClass {

    private String userName,email,password,user,newPassword;
    private Bitmap userImage;

    public ModelClass(String userName, String email, String password, Bitmap userImage) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.userImage = userImage;
    }



    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public ModelClass(String user, String newPassword) {
        this.newPassword = newPassword;
        this.user = user;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Bitmap getUserImage() {
        return userImage;
    }

    public void setUserImage(Bitmap userImage) {
        this.userImage = userImage;
    }
}
