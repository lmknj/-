package osero;

import java.awt.event.MouseEvent;
import java.io.Serializable;

import javax.swing.event.MouseInputAdapter;




public class MouseCheck extends MouseInputAdapter  implements Serializable { // リスナの実装
    private int x = 0;
    private int y = 0;
    TestPanel testPanel = null;

    public MouseCheck(){

    }



    public void mouseClicked( MouseEvent e ) {
        if(!testPanel.getFinished() && testPanel.getIsPutTurn()){
            //System.out.println("clicked");
            x = getValue(e.getX());//マスを取得
            y = getValue(e.getY());//マスを取得
            //System.out.println("X:" + (x+1));
            //System.out.println("Y;" + (y+1));


            testPanel.setClickedX(x);
            testPanel.setClickedY(y);

            testPanel.repaint();
        }

    }//mouseClicked


    int getValue(double val){
        int value = 0;
        if(100 <= val && val < 150)value = 0;
        if(150 <= val && val < 200)value = 1;
        if(200 <= val && val < 250)value = 2;
        if(250 <= val && val < 300)value = 3;
        if(300 <= val && val < 350)value = 4;
        if(350 <= val && val < 400)value = 5;
        if(400 <= val && val < 450)value = 6;
        if(450 <= val && val < 500)value = 7;
        System.out.println("value:" + value);
        return value;
    }

    public void setPanel(TestPanel panel){
        this.testPanel = panel;
    }





}//MouseCheck