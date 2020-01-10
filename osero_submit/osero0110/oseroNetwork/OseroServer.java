package oseroNetwork;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JPanel;

import osero.MouseCheck;
import osero.Player;
import osero.TestPanel;


public class OseroServer extends JFrame{
    JPanel panel;
    public TestPanel testPanel;

    Player myPlayer;
    boolean isSenko ;
    boolean isFinished = false;

    public OseroServer(String title){
        super( title );
        panel = (JPanel)getContentPane();
        testPanel = TestPanel.getInstance();
        MouseCheck mc = new MouseCheck();
        mc.setPanel(testPanel);
        panel.addMouseListener( mc );
        panel.add(testPanel);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE );
        this.setSize(617, 640 );
        this.setVisible( true );
    }
    public static void main(String[] args){
        try {
            int port=5002;
            ServerSocket srvSock = new ServerSocket(port);
            Socket socket = srvSock.accept();
         	System.out.println("----------------------------------------------------");
            System.out.println("Address:" + socket.getInetAddress());
         	System.out.println("----------------------------------------------------");

            //最初の描画

            OseroServer osero = new OseroServer( "OseroServer" );

            //ここで1度クライアントにtestPanelを送信
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            //データコンテナオブジェクトをクライアントに送信
           // osero.testPanel.setIsPutTurn(false);
            oos.writeObject(osero.testPanel);
            oos.flush();//必要かどうか不明


            //送信終わり


            int[][] xy = null;
            //ここで一度受信
            try {
                xy = (int[][]) (ois.readObject());//バイト列から復元
            }catch(ClassNotFoundException e){
                e.printStackTrace();
            }
            System.out.println("----------------------------------------------------");
            System.out.println("TestPanel:"+ osero.testPanel);
            osero.testPanel.setField(xy);
            osero.testPanel.setIsPutTurn(false);
            System.out.println("osero.testPanel.getIsPutTurn():" + osero.testPanel.getIsPutTurn());
            osero.testPanel.repaint();
           // osero.testPanel.setIsFirst(false);

            System.out.println( "TestPanel 受信完了");
            System.out.println("----------------------------------------------------");
            //受信終わり

            osero.myPlayer =  osero.testPanel.getP2();//自分のプレイヤーのターンだけ操作を許可したい//
            osero.myPlayer.setPanel(osero.testPanel);
            osero.isSenko = osero.myPlayer.getSenko();

           // System.out.println("first:" + osero.testPanel.getIsFirst());
            //osero.testPanel.repaint();//

            if(osero.isSenko){//先行ならコマを置いて送信
            	System.out.println("----------------------------------------------------");
                System.out.println("あなたは先攻です。");
                System.out.println("osero.isSenko:"+ osero.isSenko);
                System.out.println("YOUR COLOR: BLACK");
                osero.testPanel.setIsPutTurn(true);
                System.out.println("----------------------------------------------------");






              while(osero.testPanel.getWaitKey()){
            	  System.out.println("****************************************************************************");
            	  System.out.println("srv while osero.testPanel.getWaitKey()まで行きました");
                  System.out.println(osero.testPanel.getIsPutTurn());
                  System.out.println(osero.testPanel);
                  System.out.println("----------------------------------------------------------------------------");

                  try {
                      Thread.sleep(1000);
                  } catch (InterruptedException e) {
                      e.printStackTrace();
                  }
                 // ここでクリック待ちしたい///////////////////////////////////////////////////////????????????????????????????????????????????
                }

                oos.writeObject(osero.testPanel.getField());//送信
                oos.flush();//必要かどうか不明
                osero.testPanel.setIsPutTurn(false);
                System.out.println("false end");
                System.out.println("***********************************fin****************************************");

                //  osero.isSenko = osero.myPlayer.getSenko();//ターンチェンジ

            }else{//後攻ならそのまま送信
                System.out.println("NOT YOUR TURN");
                System.out.println("YOUR COLOR WHITE");

                oos.writeObject(osero.testPanel.getField());//送信
                oos.flush();//必要かどうか不明
               //ならそのまま
            }
            //ここで受信までの間画面の判定をなくしたいaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
            //描画終わり


            while(!osero.isFinished){
            	System.out.println("");
                System.out.println("Server while !osero.isFinished : START!!");
                System.out.println("");
                //受信
                try {
                    xy = (int[][]) (ois.readObject());//バイト列から復元
                }catch(ClassNotFoundException e){
                    e.printStackTrace();
                }
                System.out.println( "Message from Client:"+ xy);
                osero.testPanel.setField(xy);
                osero.testPanel.repaint();
                System.out.println("受信");
                //受信終了

                osero.isFinished = osero.testPanel.getFinished();
                if(osero.isFinished)break;

                osero.testPanel.setIsPutTurn(true);

                osero.testPanel.turnChange();
                osero.testPanel.setWaitKey(true);
                while(osero.testPanel.getWaitKey()){
                    System.out.println("Server while osero.testPanel.getWaitKey()");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }                  // ここでクリック待ちしたい
                }

                oos.writeObject(osero.testPanel.getField());
                oos.flush();//必要かどうか不明
               // osero.isSenko = osero.myPlayer.getSenko();//ターンチェンジ
                System.out.println("送信");
                osero.testPanel.setIsPutTurn(false);
                osero.testPanel.setWaitKey(true);
                //送信終了
                //ここで受信までの間画面の判定をなくしたい
                osero.isFinished = osero.testPanel.getFinished();
                if(osero.isFinished)break;

            }//while

            //結果判定後処理
            System.out.println("ALL COMPLETE");
            //reader.close();
            ois.close();
            oos.close();
            socket.close();
            srvSock.close();
            //※2
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
