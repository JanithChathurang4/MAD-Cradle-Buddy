package com.example.infantcareaidingmobileapplication;

import com.google.firebase.database.Exclude;

public class Uploader {


    private String mImageUrl;
    private String month;
    private String mKey;
    public Uploader() {
    //
    }

    public Uploader(String month, String mImageUrl) {
        this.mImageUrl = mImageUrl;
        this.month = month;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    @Exclude
    public String getKey(){

        return mKey;
    }
    @Exclude
    public void setKey(String Key){
        mKey = Key;
    }



    //    public Uploader(String mImageUrl ,String photoMonthNum){
//        if (photoMonthNum.trim().equals("") ){
//            photoMonthNum = "No month number";
//        }
//        photoMonthNum=photoMonthNum;
//        mImageUrl=mImageUrl;
//    }

}
