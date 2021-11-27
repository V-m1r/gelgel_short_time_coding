package com.example.gelgel_short_time_coding.models;

public class PhoneAuth {
    String phoneNum, password;

    public PhoneAuth(String phoneNum, String password) {
        this.phoneNum = phoneNum;
        this.password = password;
    }

    public PhoneAuth() {
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
