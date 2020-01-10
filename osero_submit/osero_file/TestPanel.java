package osero;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JPanel;


public class TestPanel extends JPanel  implements Serializable{
    private final int FIELDX = 8; //盤面のXの数
    private final int FIELDY = 8; //盤面のYの数
    private Player p1;
    private Player p2;
    private boolean senko = true;
    private boolean isFirst;
    private int clickedX;
    private int clickedY;
    boolean isFinished = false;//終了判定
    private int[][] field = new int[8][8];
    private boolean canSend = false;//正常に駒を置いて相手にpanelを送れるかどうか
    private int i = 0;
    private boolean isPutTurn = true;
    private boolean waitKey = true;
    private ArrayList<int[]> available = new ArrayList<int[]>();//置ける座標の配列


    private static TestPanel testPanel;
    private ArrayList<ArrayList<ArrayList<int[]> >> availableAndTurnable
            = new ArrayList<ArrayList<ArrayList<int[]> >>();




    private TestPanel(){
        isFirst = true;
        int rand = new Random().nextInt(2);
        if(rand == 1)senko = false;

        p1 = new Player(senko);
        // p1.setSenko(senko);

        p2 = new Player(!senko);
        //p2.setSenko(!senko);
    }


    public void paintComponent(Graphics g) {

    	super.paintComponent(g);

        System.out.println("dddddddddddddddddddddddddddddddddd");

        g.setColor(Color.GREEN);
        g.fillRect(100, 100, 400, 400);
        g.setColor(Color.BLACK);


        for(int i = 0; i < FIELDY; i++){
            for(int j = 0; j < FIELDX; j++){
                new Block(i,j).drawBlock(g);
            }
        }



        if(isPutTurn){
            if(isFirst){
                for(int i = 0; i < 8; i++){
                    for(int j = 0; j < 8; j++){
                        field[i][j] = -1;
                        //System.out.print(field[i][j]);
                    }
                    //System.out.println();
                }



                for(int i = 0; i < FIELDY; i++){
                    for(int j = 0; j < FIELDX; j++){
                        new Block(i,j).drawBlock(g);
                    }
                }

                p1.putFirstKoma(3,3);
                p1.putFirstKoma(4,4);
                p2.putFirstKoma(3,4);
                p2.putFirstKoma(4,3);

                komasuu(g,senko);


                //0can 1changed(使いまわし)
                ArrayList<ArrayList<ArrayList<int[]> >> canChangedList
                        = canput(senko);
                for(int i = 0; i< available.size(); i++){
                	g.setColor(Color.RED);
                	g.drawRect(canChangedList.get(i).get(0).get(0)[0] * 50+100, canChangedList.get(i).get(0).get(0)[1] * 50+100, 50, 50);
                }

                isFirst = false;

            }else{
                System.out.println("ふdddddddddddddddddddddddddddddあ");
                boolean over = true;
                System.out.println("firstSenko:" + senko);

                if(senko){
                    System.out.println("p1111111111111111111111111111111");
                    over = putProcessing(g,p1);
                }else{
                    System.out.println("p22222222222222222222222222222222");
                    over = putProcessing(g,p2);
                }

                if(over){
                    System.out.println("TURN CHANGED");
                    senko = !senko;
                    System.out.println("endSenko:" + senko);
                    canSend = true;
                    komasuu(g,senko);
                    //0can 1changed(使いまわし)
                    ArrayList<ArrayList<ArrayList<int[]> >> canChangedList
                            = canput(senko);
                    for(int i = 0; i< available.size(); i++){
                    	g.setColor(Color.RED);
                    	g.drawRect(canChangedList.get(i).get(0).get(0)[0] * 50+100, canChangedList.get(i).get(0).get(0)[1] * 50+100, 50, 50);
                    }
                }

            }//isFirstElse

            //0can 1changed(使いまわし)
            ArrayList<ArrayList<ArrayList<int[]> >> canChangedList
                    = canput(senko);
            for(int i = 0; i< available.size(); i++){
            	g.setColor(Color.RED);
            	g.drawRect(canChangedList.get(i).get(0).get(0)[0] * 50+100, canChangedList.get(i).get(0).get(0)[1] * 50+100, 50, 50);
            }


        }//isPutTurn
        else{
            System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaあ");
        }




        drawAll(g, field);

        if( !(p1.getPut() || p2.getPut()) || getFin()  )isFinished = true;


        if(isFinished){
            int blackScore = 0;
            int whiteScore = 0;
            for(int i = 0; i < 8; i++){
                for(int j = 0; j < 8; j++){
                    if(field[j][i] == 1)blackScore++;
                    if(field[j][i] == 0)whiteScore++;
                }
            }
            if(blackScore > whiteScore){
                System.out.println("Winner,BLACK");
            }else if(blackScore < whiteScore){
                System.out.println("Winner,WHITE");
            }else{
                System.out.println("draw");
            }
            System.out.println("BLACK score:" + blackScore);
            System.out.println("WHITE score:" + whiteScore);

            System.out.println("finish");
        }

        System.out.println("repaint COMPLETE");
        //System.out.println("TURN CHANGED:" +p1.getColor() );
    }//paintComponent


    public void komasuu(Graphics g,boolean senko){


    	 Graphics2D g2d = (Graphics2D) g;

 	    try {
 	      BufferedImage image = ImageIO.read(
 	        new File("C:\\Users\\sistema\\Pictures\\osero_file\\osero_back.jpg"));
 	      BufferedImage image1 = ImageIO.read(
 	    	        new File("C:\\Users\\sistema\\Pictures\\osero_file\\osero_bord.jpg"));
 	      g2d.drawImage(image, null, 0, 0);
 	      g2d.drawImage(image1, null, 100, 100);
 	    } catch (IOException e) {
 	      e.printStackTrace();
 	    }

    	g2d.setColor(Color.RED);
    	g2d.fillRect(350, 575, 100, 50);
    	g2d.fillRect(150, 575, 100, 50);
    	g2d.setColor(Color.BLACK);
    	g2d.drawRect(350, 575, 100, 50);
    	g2d.drawRect(150, 575, 100, 50);
    	int blackScore = 0;
        int whiteScore = 0;
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(field[j][i] == 1)blackScore++;
                if(field[j][i] == 0)whiteScore++;
            }
        }

        String blackScorestr = Integer.toString(blackScore);
        String whiteScorestr = Integer.toString(whiteScore);

        Font font1 = new Font("HG行書体",Font.BOLD,38);
        g2d.setFont(font1);
        g2d.drawString("黒", 150, 570);
        g2d.drawString(blackScorestr, 190, 612);

        if(senko == true){
        	g.setColor(Color.BLACK);
        	 g2d.drawString("黒の手番", 300, 535);
        }else {
        	g.setColor(Color.WHITE);
        	 g2d.drawString("白の手番", 300, 535);
        }

        g2d.setColor(Color.WHITE);
        g2d.drawString(whiteScorestr, 390, 612);
       // g2d.setColor(Color.BLACK);
        g2d.drawString("白", 350, 570);


    }




    public void drawAll(Graphics g, int[][] field){
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                int[] xy = {j,i};
                if(field[j][i] == 1)drawKoma(g,true , xy);
                if(field[j][i] == 0)drawKoma(g,false, xy);


            }

        }
    }

    public void drawKoma(Graphics g,boolean isBlack,int[] xy){
        if(isBlack == true) g.setColor(Color.BLACK);
        else  g.setColor(Color.WHITE);
        g.fillOval(xy[0] * 50+100, xy[1] * 50+100, 50, 50);
    }




    public boolean putProcessing(Graphics g,Player p){
        System.out.println(p.getColor());
        availableAndTurnable = canput(p.getIsBlack());
        if(availableAndTurnable.size() != 0){
            System.out.println("availableAndTurnable != null");

            System.out.println("ClickedX:" + (clickedX + 1));
            System.out.println("ClickedY:" + (clickedY + 1));
            int[] xy = p.clickedKoma( availableAndTurnable,clickedX,clickedY);//クリックしたコマの座標

            if(xy[0] == -1)return false;

            //反転処理
            for(int i = 0; i < availableAndTurnable.size(); i++){
                //System.out.println("a");
                if(xy[0] == availableAndTurnable.get(i).get(0).get(0)[0]
                        && xy[1] == availableAndTurnable.get(i).get(0).get(0)[1] ){//canと比較
                    //System.out.println("b");

                    for(int j = 0; j < availableAndTurnable.get(i).get(1).size();j++){//canに対応するchanged

                        p.putSelectedKoma(availableAndTurnable.get(i).get(1).get(j)[0],
                                availableAndTurnable.get(i).get(1).get(j)[1]);

                        System.out.println("turnedX:" + (availableAndTurnable.get(i).get(1).get(j)[0] + 1));
                        System.out.println("turnedY:" + (availableAndTurnable.get(i).get(1).get(j)[1] + 1));
                    }//for
                    // break;
                }

            }

            for(int i = 0; i < 8; i++){
                for(int j = 0; j < 8; j++){
                    if(field[j][i] == -1)System.out.print( "*,");
                    else System.out.print(field[j][i] +  ",");

                }
                System.out.println();
            }

            p.setPut(true);

        }else{
            System.out.println("availableAndTurnable = null");
            p.setPut(false);
        }

        this.waitKey = false;
        return true;
    }//putProcess

    public ArrayList<ArrayList<ArrayList<int[]> >> canput(boolean isBlack){
        System.out.print("canput:");

        available.clear();
        ArrayList<int[]> can = new ArrayList<int[]>();//置ける座標の一時的な配列(使いまわし)
        ArrayList<int[]> changed = new ArrayList<int[]>();//置いたときに値が変わる座標の集合(使いまわし)
        ArrayList<ArrayList<int[]> > canChanged = new ArrayList<ArrayList<int[]> >();//canとchangeの対応付け
        //0can 1changed(使いまわし)
        ArrayList<ArrayList<ArrayList<int[]> >> canChangedList
                = new ArrayList<ArrayList<ArrayList<int[]> >>();//canChangedの集合
        int zero = 0;
        int one = 1;
        if(!isBlack){
            zero = 1;
            one = 0;
        }
        for(int i = 0; i < 8; i++){//縦
            for(int j = 0; j < 8; j++){//横


                try{
                    if(field[j+1][i] == zero && field[j][i] == -1){
                        for(int k= j + 1; k < 8; k++){
                            if(field[k][i] == zero){
                                int[] cha = {k,i};//代わる予定の座標を格納
                                changed.add(cha);//はさむための黒も格納する
                            }else if(field[k][i] == one){//はさむための黒を探す
                                int[] c = {j,i};//置ける座標
                                available.add(c.clone());
                                can.add(c.clone());
                                canChanged.add((ArrayList<int[]>)can.clone());
                                canChanged.add((ArrayList<int[]>)changed.clone());
                                canChangedList.add((ArrayList<ArrayList<int[]> > )canChanged.clone());
                                can.clear();
                                changed.clear();
                                canChanged.clear();
                                System.out.println("[j+1][i]");
                                break;
                            }else if(field[k][i] == -1){
                                break;
                            }
                        }//for
                        changed.clear();
                    }//if
                }catch(ArrayIndexOutOfBoundsException e){ changed.clear();}

                try{
                    if(field[j-1][i] == zero && field[j][i] == -1){
                        for(int k= j - 1; k >= 0; k--){
                            if(field[k][i] == zero){
                                int[] cha = {k,i};//代わる予定の座標を格納
                                changed.add(cha);
                            }else if(field[k][i] == one){
                                int[] c = {j,i};
                                available.add(c.clone());
                                can.add(c.clone());
                                canChanged.add((ArrayList<int[]>)can.clone());
                                canChanged.add((ArrayList<int[]>)changed.clone());
                                canChangedList.add((ArrayList<ArrayList<int[]> > )canChanged.clone());
                                can.clear();
                                changed.clear();
                                canChanged.clear();
                                System.out.println("[j-1][i]");
                                break;
                            }else if(field[k][i] == -1){
                                break;
                            }
                        }
                        changed.clear();
                    }//if
                }catch(ArrayIndexOutOfBoundsException e){changed.clear();}


                try{
                    if(field[j][i+1] == zero && field[j][i] == -1){
                        for(int k= i + 1; k < 8; k++){
                            if(field[j][k] == zero){
                                int[] cha = {j,k};//代わる予定の座標を格納
                                changed.add(cha);

                            }else if(field[j][k] == one){
                                int[] c = {j,i};
                                available.add(c.clone());
                                can.add(c.clone());
                                canChanged.add((ArrayList<int[]>)can.clone());
                                canChanged.add((ArrayList<int[]>)changed.clone());
                                canChangedList.add((ArrayList<ArrayList<int[]> > )canChanged.clone());
                                can.clear();
                                changed.clear();
                                canChanged.clear();
                                System.out.println("[j][i+1]");
                                break;
                            }else if(field[j][k] == -1){
                                break;
                            }
                        }
                        changed.clear();
                    }//if
                }catch(ArrayIndexOutOfBoundsException e){ changed.clear();}

                try{
                    if(field[j][i-1] == zero && field[j][i] == -1){
                        for(int k= i - 1; k >= 0; k--){
                            if(field[j][k] == zero){
                                int[] cha = {j,k};//代わる予定の座標を格納
                                changed.add(cha);
                            }else if(field[j][k] == one){
                                int[] c = {j,i};
                                available.add(c.clone());
                                can.add(c.clone());
                                canChanged.add((ArrayList<int[]>)can.clone());
                                canChanged.add((ArrayList<int[]>)changed.clone());
                                canChangedList.add((ArrayList<ArrayList<int[]> > )canChanged.clone());
                                can.clear();
                                changed.clear();
                                canChanged.clear();
                                System.out.println("[j][i-1]");
                                break;
                            }else if(field[j][k] == -1){
                                break;
                            }
                        }
                        changed.clear();
                    }//if
                }catch(ArrayIndexOutOfBoundsException e){changed.clear();}

                try{
                    if(field[j+1][i+1] == zero && field[j][i] == -1){
                        for(int k= i + 1, l = j + 1; k < 8; k++,l++){
                            if(field[l][k] == zero){
                                int[] cha = {l,k};//代わる予定の座標を格納
                                changed.add(cha);
                            }else if(field[l][k] == one){
                                int[] c = {j,i};
                                available.add(c.clone());
                                can.add(c.clone());
                                canChanged.add((ArrayList<int[]>)can.clone());
                                canChanged.add((ArrayList<int[]>)changed.clone());
                                canChangedList.add((ArrayList<ArrayList<int[]> > )canChanged.clone());
                                can.clear();
                                changed.clear();
                                canChanged.clear();
                                System.out.println("[j+1][i+1]");
                                break;
                            }else if(field[l][k] == -1){
                                break;
                            }
                        }
                        changed.clear();
                    }//if
                }catch(ArrayIndexOutOfBoundsException e){changed.clear();}

                try{
                    if(field[j-1][i-1] == zero && field[j][i] == -1){
                        for(int k= i - 1, l = j - 1; k >= 0; k--,l--){
                            if(field[l][k] == zero){
                                int[] cha = {l,k};//代わる予定の座標を格納
                                changed.add(cha);
                            }else if(field[l][k] == one){
                                int[] c = {j,i};
                                available.add(c.clone());
                                can.add(c.clone());
                                canChanged.add((ArrayList<int[]>)can.clone());
                                canChanged.add((ArrayList<int[]>)changed.clone());
                                canChangedList.add((ArrayList<ArrayList<int[]> > )canChanged.clone());
                                can.clear();
                                changed.clear();
                                canChanged.clear();
                                System.out.println("[j-1][i-1]");
                                break;
                            }else if(field[l][k] == -1){
                                break;
                            }
                        }
                        changed.clear();
                    }//if
                }catch(ArrayIndexOutOfBoundsException e){ changed.clear();}

                try{
                    if(field[j+1][i-1] == zero && field[j][i] == -1){
                        for(int k= i - 1, l = j + 1; k >= 0; k--,l++){
                            if(field[l][k] == zero){
                                int[] cha = {l,k};//代わる予定の座標を格納
                                changed.add(cha);
                            }else if(field[l][k] == one){
                                int[] c = {j,i};
                                available.add(c.clone());
                                can.add(c.clone());
                                canChanged.add((ArrayList<int[]>)can.clone());
                                canChanged.add((ArrayList<int[]>)changed.clone());
                                canChangedList.add((ArrayList<ArrayList<int[]> > )canChanged.clone());
                                can.clear();
                                changed.clear();
                                canChanged.clear();
                                System.out.println("[j+1][i-1]");
                                break;
                            }else if(field[l][k] == -1){
                                break;
                            }
                        }
                        changed.clear();
                    }//if
                }catch(ArrayIndexOutOfBoundsException e){ changed.clear();}

                try{
                    if(field[j-1][i+1] == zero && field[j][i] == -1){
                        for(int k= i + 1, l = j - 1; k < 8; k++,l--){
                            if(field[l][k] == zero){
                                int[] cha = {l,k};//代わる予定の座標を格納
                                changed.add(cha);
                            }else if(field[l][k] == one){
                                int[] c = {j,i};
                                available.add(c.clone());
                                can.add(c.clone());
                                canChanged.add((ArrayList<int[]>)can.clone());
                                canChanged.add((ArrayList<int[]>)changed.clone());
                                canChangedList.add((ArrayList<ArrayList<int[]> > )canChanged.clone());
                                can.clear();
                                changed.clear();
                                canChanged.clear();
                                System.out.println("[j-1][i+1]");
                                break;
                            }else if(field[l][k] == -1){
                                break;
                            }
                        }
                        changed.clear();
                    }//if
                }catch(ArrayIndexOutOfBoundsException e){ changed.clear();}

            }//forJ
        }//forI

        if(available.size() == 0){
            // System.out.println("available: null");
            return canChangedList;

        }


        System.out.println("canChangedList:" + canChangedList.size());
        for(int i = 0; i < canChangedList.size(); i++){//canChangedの数だけ繰り返す
            System.out.print("can:");
            System.out.print(canChangedList.get(i).get(0).get(0)[0] + 1);//canのi番目の座標の出力
            //canChangedList(i)の中のcanChangedの0番目(can)
            //の中の配列の0番目
            System.out.print((canChangedList.get(i).get(0).get(0)[1] + 1) + "\t");

            System.out.print("changed:");
            for(int j = 0; j < canChangedList.get(i).get(1).size();j++){//canに対応するchangedの出力
                System.out.print(canChangedList.get(i).get(1).get(j)[0] + 1);
                //canChangedList(i)の中のcanChanged
                //のj番目(changed)の中の配列の0番目
                System.out.print(canChangedList.get(i).get(1).get(j)[1] + 1);

                if(!(j == canChangedList.get(i).get(1).size() - 1))System.out.print(",");
            }//for canChangedList.get(i).size()
            System.out.println();

        }//for canChangedList.size()

        return canChangedList;
    }//canput

    public ArrayList<ArrayList<ArrayList<int[]> >> getAvailableAndTurnable(){ return availableAndTurnable; }

    public Player getP1(){return p1;}

    public Player getP2(){return p2;}

    public boolean getSenko(){return senko;}

    public void setClickedX(int x){this.clickedX = x;}
    public void setClickedY(int y){this.clickedY = y;}

    //  public void setWaiting(boolean waiting){this.waiting = waiting;}

    private boolean getFin(){
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if( field[j][i] == -1)return false;
            }
        }
        return true;
    }

    public boolean getFinished(){return isFinished;}

    public boolean getCanSend(){return canSend;}

    public void setCanSend(boolean canSend){this.canSend = canSend;}

    public static synchronized TestPanel getInstance(){
        if(testPanel == null){
            testPanel = new TestPanel();
        }
        return testPanel;
    }

    public void setIsFirst(boolean isFirst){this.isFirst = isFirst;}
    public boolean getIsFirst(){return isFirst;}

    public int[][] getField(){return field;}
    public void setField(int[][] xy){field = xy;}

    public int getFieldXY(int x,int y){return field[x][y];}
    public void setFieldXY(int x,int y,int val){field[x][y] = val;}

    public boolean getIsPutTurn(){return isPutTurn;}
    public void setIsPutTurn(boolean isPutTurn){this.isPutTurn = isPutTurn;}

    public boolean getWaitKey(){return waitKey;}
    public void setWaitKey(boolean waitKey){this.waitKey = waitKey;}

    public void turnChange(){
        senko = !senko;
        System.out.println("TURN CHANGED");
    }


}//TestPanel

