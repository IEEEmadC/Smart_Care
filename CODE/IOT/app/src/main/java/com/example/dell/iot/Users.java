package com.example.dell.iot;

public class Users {

    String name;
    String name_s;
    String email_s;
    String email;
    String phone;
    String phone_s;
    String lat;
    String lng;
    String uid;
    String status;



    public Users(String name, String name_s, String email_s, String email, String phone, String phone_s, String lat, String lng, String uid, String status){

        this.setEmail(email);
        this.setEmail_s(email_s);
        this.setLat(lat);
        this.setLng(lng);
        this.setName(name);
        this.setName_s(name_s);
        this.setPhone(phone);
        this.setPhone_s(phone_s);
        this.setUid(uid);
        this.setStatus(status);
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName_s() {
        return name_s;
    }

    public void setName_s(String name_s) {
        this.name_s = name_s;
    }

    public String getEmail_s() {
        return email_s;
    }

    public void setEmail_s(String email_s) {
        this.email_s = email_s;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone_s() {
        return phone_s;
    }

    public void setPhone_s(String phone_s) {
        this.phone_s = phone_s;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}
