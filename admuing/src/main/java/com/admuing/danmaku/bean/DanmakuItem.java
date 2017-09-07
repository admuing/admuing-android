package com.admuing.danmaku.bean;

/**
 * Created by duan .
 */

public class DanmakuItem {
    private float x = 0;
    private float y = 0;
    private String content;
    private int speed = 0;
    private int color;
    private float conlen;

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public float getConlen() {
        return conlen;
    }

    public void setConlen(float conlen) {
        this.conlen = conlen;
    }
}
