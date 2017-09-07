package com.admuing.danmaku.bean;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by duan .
 */

public class DanmakuInfo {
    private LinkedList<String> danmakus;
    private int type = 1;
    private int[] fontColors;

    public LinkedList<String> getDanmakus() {
        return danmakus;
    }

    public void setDanmakus(LinkedList<String> danmakus) {
        this.danmakus = danmakus;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int[] getFontColors() {
        return fontColors;
    }

    public void setFontColors(int[] fontColors) {
        this.fontColors = fontColors;
    }

    @Override
    public String toString() {
        return "{" +
                "'danmakus':" + danmakus +
                ", 'type':" + type +
                ", 'fontColors':" + Arrays.toString(fontColors) +
                '}';
    }
}
