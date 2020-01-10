package osero;

import java.awt.Graphics;

class Block{
    int x;
    int y;
    final int SIZE = 50;

    Block(int x, int y){
        this.x = x;
        this.y = y;
    }

    void drawBlock(Graphics g){
        g.drawRect(x * SIZE+100, y * SIZE+100, 50, 50);
    }//drawBlock


    void drawFill(Graphics g){
        g.fillRect(x * SIZE+100, y * SIZE+100, 50, 50);
    }//drawFill

}//Block