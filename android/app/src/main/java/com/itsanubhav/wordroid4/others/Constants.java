package com.itsanubhav.wordroid4.others;

public class Constants {

    public static final int AUTO_SLIDER = 1;

    public static final int LIST_ITEM = 2;

    public static final int GRID_ITEM = 3;

    public static final int HORIZONTAL_SCROLL_POST_SECTION = 4;

    public static final int HORIZONTAL_SCROLL_CATEGORY_SECTION = 5;

    public static final int HORIZONTAL_SCROLL_TAG_SECTION = 6;

    public static final int MANUAL_BIG_CAROUSEL_OUTER_TITLE = 2;

    public static final int MANUAL_BIG_CAROUSEL_INNER_TITLE = 3;

    public static final int MANUAL_SMALL_CAROUSEL = 4;

    public static double getRandomLayout(){
        double x = (int)(Math.random()*((4-1)+1))+1;
        return x;
    }


}
