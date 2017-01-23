package com.ensiie.yuheng.moviie;

import com.ensiie.yuheng.moviie.model.Movie;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by solael on 2017/1/21.
 */

class Singleton {
    private static Singleton INSTANCE = new Singleton();
    private static final String TAG = Singleton.class.getSimpleName();

    private ArrayList<Movie> popular;
    private ArrayList<Movie> topRated;
    private ArrayList<Movie> upComing;

    private Singleton() {

    }

    static Singleton getINSTANCE() {
        return INSTANCE;
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
