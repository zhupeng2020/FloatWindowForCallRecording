package com.pri.recordview.bean;

public class FollowTouchBean {

    //down button
    private int defaultDownLpx;
    private int defaultDownLpy;
    private int currentDownLpx;
    private int currentDownLpy;

    // top button
    private int defaultTopLpx;
    private int defaultTopLpy;
    private int currentTopLpx;
    private int currentTopLpy;
    private float alphaPercentage;

    private int statusBarHeight = -1;

    public FollowTouchBean() {
    }

    public static FollowTouchBean getInstance() {
        return FollowTouchBean.Builder.inStance;
    }

    static class Builder {
        private static FollowTouchBean inStance = new FollowTouchBean();
    }

    public int getDefaultDownLpx() {
        return defaultDownLpx;
    }

    public void setDefaultDownLpx(int defaultDownLpx) {
        this.defaultDownLpx = defaultDownLpx;
    }

    public void setDefaultDownLp(int defaultDownLpx, int defaultDownLpy) {
        this.defaultDownLpx = defaultDownLpx;
        this.defaultDownLpy = defaultDownLpy;
    }

    public int getDefaultDownLpy() {
        return defaultDownLpy;
    }

    public void setDefaultDownLpy(int defaultDownLpy) {
        this.defaultDownLpy = defaultDownLpy;
    }

    public int getCurrentDownLpx() {
        return currentDownLpx;
    }

    public void setCurrentDownLpx(int currentDownLpx) {
        this.currentDownLpx = currentDownLpx;
    }

    public void setCurrentDownLp(int currentDownLpx, int currentDownLpy) {
        this.currentDownLpx = currentDownLpx;
        this.currentDownLpy = currentDownLpy;
    }

    public int getCurrentDownLpy() {
        return currentDownLpy;
    }

    public void setCurrentDownLpy(int currentDownLpy) {
        this.currentDownLpy = currentDownLpy;
    }

    public int getDefaultTopLpx() {
        return defaultTopLpx;
    }

    public void setDefaultTopLpx(int defaultTopLpx) {
        this.defaultTopLpx = defaultTopLpx;
    }

    public void setDefaultTopLp(int defaultTopLpx, int defaultTopLpy) {
        this.defaultTopLpx = defaultTopLpx;
        this.defaultTopLpy = defaultTopLpy;
    }

    public int getDefaultTopLpy() {
        return defaultTopLpy;
    }

    public void setDefaultTopLpy(int defaultTopLpy) {
        this.defaultTopLpy = defaultTopLpy;
    }

    public int getCurrentTopLpx() {
        return currentTopLpx;
    }

    public void setCurrentTopLpx(int currentTopLpx) {
        this.currentTopLpx = currentTopLpx;
    }

    public void setCurrentTopLp(int currentTopLpx, int currentTopLpy) {
        this.currentTopLpx = currentTopLpx;
        this.currentTopLpy = currentTopLpy;
    }

    public int getCurrentTopLpy() {
        return currentTopLpy;
    }

    public void setCurrentTopLpy(int currentTopLpy) {
        this.currentTopLpy = currentTopLpy;
    }

    public float getAlphaPercentage() {
        return alphaPercentage;
    }

    public void setAlphaPercentage(float alphaPercentage) {
        this.alphaPercentage = alphaPercentage;
    }

    public int getStatusBarHeight() {
        return statusBarHeight;
    }

    public void setStatusBarHeight(int statusBarHeight) {
        this.statusBarHeight = statusBarHeight;
    }

    @Override
    public String toString() {
        return "FollowTouchBean{" +
                "defaultDownLpx=" + defaultDownLpx +
                ", defaultDownLpy=" + defaultDownLpy +
                ", currentDownLpx=" + currentDownLpx +
                ", currentDownLpy=" + currentDownLpy +
                ", defaultTopLpx=" + defaultTopLpx +
                ", defaultTopLpy=" + defaultTopLpy +
                ", currentTopLpx=" + currentTopLpx +
                ", currentTopLpy=" + currentTopLpy +
                ", alphaPercentage=" + alphaPercentage +
                ", statusBarHeight=" + statusBarHeight +
                '}';
    }
}
