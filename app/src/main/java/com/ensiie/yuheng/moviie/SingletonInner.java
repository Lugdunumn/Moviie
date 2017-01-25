package com.ensiie.yuheng.moviie;

import com.ensiie.yuheng.moviie.model.Movie;

import java.util.ArrayList;

/**
 * Created by solael on 2017/1/21.
 */

// Inner singleton is more safe
public class SingletonInner {

    private static final String TAG = SingletonInner.class.getSimpleName();

    private ArrayList<Movie> popular;
    private ArrayList<Movie> topRated;
    private ArrayList<Movie> upComing;


    private SingletonInner() {

    }

    private static class SingletonHolder {
        private static SingletonInner INSTANCE = new SingletonInner();
    }

    public static SingletonInner getINSTANCE() {
        return SingletonHolder.INSTANCE;
    }

    ArrayList<Movie> getPopular() {
        return popular;
    }

    void setPopular(ArrayList<Movie> popular) {
        this.popular = popular;
    }

    ArrayList<Movie> getTopRated() {
        return topRated;
    }

    void setTopRated(ArrayList<Movie> topRated) {
        this.topRated = topRated;
    }

    ArrayList<Movie> getUpComing() {
        return upComing;
    }

    void setUpComing(ArrayList<Movie> upComing) {
        this.upComing = upComing;
    }
}
