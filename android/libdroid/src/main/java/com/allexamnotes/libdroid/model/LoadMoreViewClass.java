package com.allexamnotes.libdroid.model;

public class LoadMoreViewClass {

    private static LoadMoreViewClass INSTANCE;

    private LoadMoreViewClass(){}

    public static LoadMoreViewClass newInstance(){
        if (INSTANCE==null){
            INSTANCE = new LoadMoreViewClass();
        }
        return INSTANCE;
    }
}
