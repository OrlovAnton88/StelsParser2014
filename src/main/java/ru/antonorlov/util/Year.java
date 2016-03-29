package ru.antonorlov.util;

/**
 * Created by antonorlov on 29/01/16.
 */
public enum Year {

    YEAR_2015(2015, 15),
    YEAR_2016(2016,16);

    private int fullNum;
    private int shortNum;

    private Year(int shortNum, int fullNum) {
        this.shortNum = shortNum;
    }

    public int getShortNum() {
        return shortNum;
    }

    public int getFullNum() {
        return fullNum;
    }
}
