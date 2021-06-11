package com.liangb.tank;

import java.awt.*;

public class Explode {
    int x ,y ;
    private boolean live  =true;
    private TankClient tc;
    int[] diameter = {4,7,12,18,26,32,49,30,14,6};
    int step = 0 ;

    public Explode(int x,int y,TankClient client){
        tc = client;
        this.x = x;
        this.y = y;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public void draw(Graphics g) {
        Color color = g.getColor();

        if (!live){
            tc.getExplodes().remove(this);
            return;
        }

        if (step == diameter.length){
            live = false;
            step = 0;
            return;
        }
        g.setColor(Color.ORANGE);
        g.fillOval(x,y,diameter[step],diameter[step++]);

        g.setColor(color);
    }

    public boolean isLive() {
        return live;
    }
}
