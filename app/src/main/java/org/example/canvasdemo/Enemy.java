package org.example.canvasdemo;


class Enemy {
    public int enemyX;
    public int enemyY;
    int move_duration = 0;
    int move_direction = 0;

    public Enemy() {
    }

    public Enemy(int x, int y) {
        this.enemyX = x;
        this.enemyY = y;
    }

    public void moveRight(int x)
    {
        this.enemyX = enemyX + x;
    }

    public void moveLeft(int x)
    {
        this.enemyX = enemyX - x;
    }

    public void moveUp(int y)
    {
        this.enemyY = enemyY - y;
    }

    public void moveDown(int y) {this.enemyY = enemyY + y;
    }
}