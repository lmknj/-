package oseroNetwork;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JPanel;

import osero.MouseCheck;
import osero.Player;
import osero.TestPanel;

public class OseroClient extends JFrame{
    public TestPanel testPanel;
    JPanel panel;
    Player myPlayer;
    boolean isSenko;
    boolean isFinished = false;

    OseroClient(String title, TestPanel testPanel){
        super( title );

        panel = (JPanel)getContentPane();
        //  this.testPanel = testPanel;
        MouseCheck mc = new MouseCheck();
        mc.setPanel(testPanel);
        panel.addMouseListener( mc );
        this.testPanel = testPanel;
        testPanel.setIsPutTurn(false);
        //testPanel.setField(xy);

        panel.add(testPanel);

      //  myPlayer = osero.testPanel.getP2();//自分のプレイヤーのターンだけ操作を許可したい
        //isSenko = myPlayer.getSenko();
    }


	public static void main(String[] args) {

        try {
            InetSocketAddress socketAddress = new InetSocketAddress("localhost", 5002);

            Socket socket = new Socket();
            socket.connect(socketAddress, 10000);

            InetAddress inetadrs;
            if ((inetadrs = socket.getInetAddress()) != null) {
            	System.out.println("----------------------------------------------------");
                System.out.println("サーバーへ繋がりました。 "+ "address :" + inetadrs);
                System.out.println("----------------------------------------------------");
            } else {
                System.out.println("Connection fail");
                return;
            }
            //描画

            //最初の描画
            TestPanel testPanel = null;
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            int[][] xy = null;

            try {
                testPanel = (TestPanel) (ois.readObject());//バイト列から復元
            }catch(ClassNotFoundException e){
                e.printStackTrace();

            }

            //System.out.println( "Message sent from Client:"+ testPanel);

            OseroClient osero = new OseroClient( "OseroClient" , testPanel);
            osero.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            osero.setSize(617, 640 );
            osero.setVisible( true );
         //   osero.testPanel.setIsPutTurn(false);

            //受信終わり
            //ここで1度送信

            oos.writeObject(osero.testPanel.getField());//送信
            oos.flush();//必要かどうか不明
            //送信終わり
            System.out.println("------------------------------");
            System.out.println("        SEND COMPLETE");
            System.out.println("------------------------------");

            osero.myPlayer = osero.testPanel.getP1();
            osero.isSenko = osero.myPlayer.getSenko();
            osero.myPlayer.setPanel(osero.testPanel);
            System.out.println("");
            System.out.println( "Message from Client :"+ xy);
            System.out.println("");

            //Scanner a = new Scanner(System.in);
            //String b = a.next();

            if(osero.isSenko){//先行なら受信しない
                System.out.println("あなたは先攻です。");
                System.out.println("osero.isSenko :"+ osero.isSenko);
                System.out.println("YOUR COLOR: BLACK");
                //ここで受信までの間画面の判定をなくしたいaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
                try {
                    xy = (int[][]) (ois.readObject());//バイト列から復元
                }catch(ClassNotFoundException e){
                    e.printStackTrace();
                }
                osero.testPanel.setField(xy);
                osero.testPanel.repaint();

                osero.testPanel.turnChange();

                }else{
                	//後攻なら受信する
                	System.out.println("------------------------------");
                    System.out.println("あなたは後攻です。");
                    System.out.println("osero.isSenko :" + osero.isSenko);
                    System.out.println("YOUR COLOR WHITE");
                    System.out.println("------------------------------");
                    try {
                        xy = (int[][]) (ois.readObject());//バイト列から復元
                    }catch(ClassNotFoundException e){
                        e.printStackTrace();
                    }
                    osero.testPanel.setField(xy);
                    osero.testPanel.repaint();

                System.out.println("");
                System.out.println( "Message from Client:"+ xy);
                System.out.println("YOUR TURN");
                System.out.println("");
            }
            // System.out.println("first:" + osero.testPanel.getIsFirst());

            // Scanner s = new Scanner(System.in);
            // String c = s.next();
                while(!osero.isFinished){
                System.out.println("------------------------------");
                System.out.println( "client while !osero.isFinished : START!!");
            	System.out.println("------------------------------");
            	//送信
            	osero.testPanel.setIsPutTurn(true);
                osero.testPanel.turnChange();

                while(osero.testPanel.getWaitKey()){
                    System.out.println("-------------osero.testPanel.getWaitKey()-----------------");
                    System.out.println(osero.testPanel.getIsPutTurn());
                    System.out.println("senko :" + osero.testPanel.getSenko());
                    System.out.println("確認  : "+ osero.testPanel.getWaitKey());
                    System.out.println("----------------------------------------------------------");

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
                System.out.println( "false end");
                osero.testPanel.setWaitKey(true);
                //送信終了
                //ここで受信までの間画面の判定をなくしたい
                osero.isFinished = osero.testPanel.getFinished();
                if(osero.isFinished)break;
                //受信
                try {
                    xy = (int[][]) (ois.readObject());//バイト列から復元
                }catch(ClassNotFoundException e){
                    e.printStackTrace();
                }
                System.out.println( "Message from Client:"+ xy);
                osero.testPanel.setField(xy);
                osero.testPanel.repaint();
                System.out.println( "jusinn/////////////////////////////////////////////////////////////////////");
                //受信終了

                osero.isFinished = osero.testPanel.getFinished();
                if(osero.isFinished)break;
            }
            //※1
            ois.close();
            oos.close();
            socket.close();
            //※2
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
