package com.liangb.tank;

import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

import static java.awt.event.KeyEvent.*;

public class Tank {
    private int x_speed=5,y_speed=5;
    private int x,y;
    public static final int TANK_WIDTH = 30;
    public static final int TANK_HEIGHT = 30;
    private Direction dir = Direction.STOP;
    private Direction bulletDir  = Direction.D ;
    private LinkedList<Bullet> bullets = new LinkedList<>();
    private boolean bL,bR,bU,bD;
    private boolean good;
    private boolean alive;

    private TankClient client ;



    public Tank(int x,int y,Direction dir,boolean good,TankClient client){
        this.x = x ;
        this.y = y ;
        this.dir = dir;
        this.good = good;
        alive =  true;
        this.client = client;
    }

    /**
     *
     * @param g
     */
    public void draw(@NotNull Graphics g) {

        Color c = g.getColor();
        if (good)
            g.setColor(Color.CYAN);
        else
            g.setColor(Color.BLUE);
        g.fill3DRect(x,y,TANK_WIDTH,TANK_HEIGHT,true);
        drawBarrel(g);


        g.setColor(c);
        bullets.forEach(bullet ->{
                    if (bullet.isAlive()) {
                        bullet.draw(g);
                    }
                });
        bullets.removeIf( b -> !b.isAlive() );
    }

    /**
     * 画炮筒
     * @param g
     */
    private void drawBarrel(Graphics g) {
        Color color = g.getColor();
        g.setColor(Color.MAGENTA);
        switch (bulletDir){
            case L: g.drawLine(x,y + TANK_HEIGHT/2 ,x + TANK_WIDTH / 2,y + TANK_HEIGHT/2);break;
            case LU:g.drawLine(x , y , x + TANK_WIDTH / 2,y + TANK_HEIGHT/2);break;
            case U: g.drawLine(x + TANK_WIDTH/2 , y + TANK_HEIGHT/2 , x + TANK_WIDTH / 2,y);break;
            case RU:g.drawLine(x + TANK_WIDTH/2 , y + TANK_HEIGHT/2 , x + TANK_WIDTH ,y );break;
            case R : g.drawLine(x + TANK_WIDTH/2 , y + TANK_HEIGHT/2 , x + TANK_WIDTH ,y  + TANK_HEIGHT / 2); break;
            case RD : g.drawLine(x + TANK_WIDTH/2 ,y + TANK_HEIGHT/2 , x + TANK_WIDTH ,y + TANK_HEIGHT );break;
            case D : g.drawLine(x + TANK_WIDTH/2 , y + TANK_HEIGHT/2 , x + TANK_WIDTH/2 ,y + TANK_HEIGHT ); break;
            case LD :g.drawLine(x  , y + TANK_HEIGHT, x + TANK_WIDTH /2 ,y+TANK_HEIGHT/2);break;
        }
        g.setColor(color);
    }

    /**
     *
     * @param e
     */
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode){
            case VK_W:bU = true;break;
            case VK_S:bD = true;break;
            case VK_A:bL = true;break;
            case VK_D:bR = true;break;

            case VK_J:  bullets.add(new Bullet(x,y,bulletDir,client)) ; break;
        }
        positioning();
        forward();
    }

    /**
     * 对坦克进行定位
     */
    public void positioning(){
        if (bL && !bR && !bU && !bD)
            dir = Direction.L;
        else if (!bL && bR && !bU && !bD)
            dir = Direction.R ;
        else if (!bL && !bR && bU && !bD)
            dir = Direction.U;
        else if (!bL && !bR && !bU && bD)
            dir = Direction.D;
        else if (bL && !bR && bU && !bD){
            dir = Direction.LU;
        }else if (bL && !bR && !bU && bD){
            dir = Direction.LD;
        }else if (!bL && bR && bU && !bD){
            dir = Direction.RU;
        }else if (!bL && bR && !bU && bD){
            dir = Direction.RD;
        }else if (!bL && !bR && !bU && !bD){
            dir = Direction.STOP;
        }

        if (dir!= Direction.STOP){
            bulletDir = dir;
        }
    }

    private void forward(){

        switch (dir){
            case L: x -= x_speed;break;
            case LU:x -= x_speed;y -= y_speed ;break;
            case U: y -= y_speed;break;
            case RU: x += x_speed;y -= y_speed;break;
            case R : x += x_speed; break;
            case RD : x += x_speed; y+=y_speed;break;
            case D : y += y_speed; break;
            case LD : x -= x_speed; y += y_speed;break;
            case STOP:break;
        }
    }

    /**
     * 键盘松开后， 确定坦克的方向
     * @param e 键盘事件对象
     */
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()){
            case VK_W : bU = false ;break;
            case VK_D: bR = false;break;
            case VK_A: bL =  false;break;
            case VK_S: bD = false; break;
        }
    }

    /**
     *
     * @return 坦克所处的矩形
     */
    public Rectangle getRectangle(){
        return new Rectangle(x,y,TANK_WIDTH,TANK_HEIGHT);
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }
}


enum Direction{
    L,R,U,D,STOP,
    LU,LD,RU,RD
}