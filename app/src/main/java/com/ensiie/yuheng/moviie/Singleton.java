package com.ensiie.yuheng.moviie;

import com.ensiie.yuheng.moviie.model.Movie;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by solael on 2017/1/21.
 */

public class Singleton {
    private static Singleton INSTANCE = new Singleton();
    private static final String TAG = Singleton.class.getSimpleName();

    private ArrayList<Movie> popular;
    private ArrayList<Movie> topRated;
    private ArrayList<Movie> upComing;

    private Singleton() {

    }

    public static Singleton getINSTANCE() {
        return INSTANCE;
    }

    public ArrayList<Movie> getPopular() {
        return popular;
    }

    public void setPopular(ArrayList<Movie> popular) {
        this.popular = popular;
    }

    public ArrayList<Movie> getTopRated() {
        return topRated;
    }

    public void setTopRated(ArrayList<Movie> topRated) {
        this.topRated = topRated;
    }

    public ArrayList<Movie> getUpComing() {
        return upComing;
    }

    public void setUpComing(ArrayList<Movie> upComing) {
        this.upComing = upComing;
    }
}
