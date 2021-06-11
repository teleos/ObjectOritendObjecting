package com.liangb.tank;

import java.awt.*;

public class Bullet {

    private int x_speed=10,y_speed=10;
    private int x,y;
    private Direction dir ;

    public static final int BULLET_WIDTH = 5;
    public static final int BULLET_HEIGHT = 5;

    private boolean alive = true;
    private TankClient client ;

    public Bullet(int tank_x,int tank_y,Direction dir,TankClient client){
        this.dir = dir;
        this.client = client;
        x = tank_x + Tank.TANK_WIDTH/2;
        y = tank_y + Tank.TANK_HEIGHT/2;

        new Thread(() ->{
            while(alive) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                forward();
                if (x > TankClient.YARD_WIDTH || y > TankClient.YARD_HEIGHT || x < 0 || y < 0) {
                    alive = false;
                }
                if (alive){
                    client.getEnemyTanks().forEach(e ->{

                        if (hitTank(e)){ // 击中坦克
                            System.out.println("击中坦克");
                            //新建爆炸类
                            Explode explode = new Explode(x, y, client);
                            client.getExplodes().add(explode);
                        }

                    }  );
                }
            }
        }
        ).start();
    }

    /**
     * 子弹前进
     */
    private void forward(){
        switch (dir){
            case R: x += x_speed;break;
            case L: x -= x_speed; break;
            case U: y -= y_speed;break;
            case D: y += y_speed;break;
            case LD: x -= x_speed; y += y_speed;break;
            case RD: x += x_speed; y += y_speed;break;
            case LU: x -= x_speed; y -= y_speed;break;
            case RU: x += x_speed; y -= y_speed ;break;

        }
    }

    /**
     * 画出子弹
     * @param g 画笔对象
     */
    public void draw(Graphics g){
        Color color = g.getColor();
        g.setColor(Color.RED);
        g.fillOval(x,y,BULLET_WIDTH,BULLET_HEIGHT);
        g.setColor(color);
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    /**
     *
     * @param t 坦克对象
     * @return 是否击中某个坦克
     */
    public boolean hitTank(Tank t){
        boolean intersects = getRectangle().intersects(t.getRectangle());
        if (intersects) {
            this.alive = false;
            t.setAlive(false);
        }
        return intersects;
    }

    public Rectangle getRectangle(){

        return  new Rectangle(x,y,BULLET_WIDTH,BULLET_HEIGHT);
    }

    public boolean isAlive() {
        return alive;
    }
}
