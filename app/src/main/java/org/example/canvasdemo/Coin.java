package org.example.canvasdemo;

import java.util.Random;


public class Coin {
    public final static int radius = 25;
    private Random rnd = new Random();
    public int x;
    public int y;

    public Coin(int w, int h) {
        this.x = rnd.nextInt(w - 2*radius) + radius;
        this.y = rnd.nextInt(h - 2*radius) + radius;
    }
}
