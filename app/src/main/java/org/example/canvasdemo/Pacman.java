package org.example.canvasdemo;


public class Pacman {

    public int pacx;
    public int pacy;

    public Pacman() {}

    public Pacman(int x, int y) {
        this.pacx = x;
        this.pacy = y;
    }

    public void moveRight(int x)
    {
        this.pacx = pacx + x;
    }

    public void moveLeft(int x)
    {
        this.pacx = pacx - x;
    }

    public void moveUp(int y)
    {
        this.pacy = pacy - y;
    }

    public void moveDown(int y)
    {
        this.pacy = pacy + y;
    }
}

