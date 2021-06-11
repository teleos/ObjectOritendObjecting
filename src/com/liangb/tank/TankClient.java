package com.liangb.tank;

import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;

public class TankClient extends Frame{

    public static final int YARD_WIDTH = 1000;
    public static final int YARD_HEIGHT = 800;
    public static final int YARD_POSITION_X = 150;
    public static final int YARD_POSITION_Y = 50;
    private Tank tank ;

    private LinkedList<Tank> enemyTanks  = new LinkedList<>();

    private LinkedList<Explode> explodes = new LinkedList<>();
    //一张虚拟图片
    Image offScreenImage = null;

    public TankClient(){
        init();
    }

    private void init(){
        initTank();
        this.setTitle("坦克大战");
        this.setSize(YARD_WIDTH,YARD_HEIGHT);
        this.setLocation(YARD_POSITION_X,YARD_POSITION_Y);
        this.setBackground(Color.WHITE);
        this.setResizable(false);
        this.setVisible(true);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        new Thread(()->{
            while (true){
                try {
                    Thread.sleep(50);
                    repaint();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void update(Graphics g) {
        if (offScreenImage == null)
            offScreenImage = this.createImage(YARD_WIDTH,YARD_HEIGHT);

        Graphics graphics = offScreenImage.getGraphics();
        Color c = graphics.getColor();
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0,0,YARD_WIDTH,YARD_HEIGHT);
        graphics.setColor(c);
        print(graphics);
        g.drawImage(offScreenImage,0,0,null);
    }

    private void initTank() {
        tank = new Tank(100,200,Direction.R,true,this);
        //初始化敌人坦克
        for (int i = 0; i < 10; i++) {
            Tank enemy = new Tank(80 + Tank.TANK_WIDTH * 3 * i, 600, Direction.U, false,this);
            enemyTanks.add(enemy) ;
        }
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                tank.keyPressed(e);
            }
            @Override
            public void keyReleased(KeyEvent e) {
                tank.keyReleased(e);
            }
        });
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        tank.draw(g);
        enemyTanks.forEach(enemy ->
                {
                    if (enemy.isAlive()) {
                        enemy.draw(g);
                    }
                }
        );
        enemyTanks.removeIf(e -> !e.isAlive());//删除已死亡的坦克
        explodes.forEach(e->e.draw(g));
        explodes.removeIf(e->!e.isLive());

    }

    public LinkedList<Tank> getEnemyTanks() {
        return enemyTanks;
    }

    public LinkedList<Explode> getExplodes() {
        return explodes;
    }

    public static void main(String[] args) {
        new TankClient();
    }
}
