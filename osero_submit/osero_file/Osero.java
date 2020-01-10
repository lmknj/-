package osero;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;



public class Osero extends JFrame{
    JPanel panel;
    TestPanel testPanel;
  //  Komasuu komasuu;

    public Osero(String title){
        super( title );
        panel = (JPanel)getContentPane();
        testPanel = TestPanel.getInstance();
      //  komasuu = new Komasuu();
        MouseCheck mc = new MouseCheck();
        mc.setPanel(testPanel);
        panel.addMouseListener(mc);
        panel.add(testPanel);
        testPanel.getP1().setPanel(testPanel);
        testPanel.getP2().setPanel(testPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE );
        this.setSize(617, 740 );
        this.setTitle("オセロゲーム");
        this.setVisible( true );


    }
    public void paintComponent(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;

    try {
      BufferedImage image = ImageIO.read(
        new File("C:\\Users\\sistema\\Pictures\\osero_file\\osero_back.jpg"));
      g2d.drawImage(image, null, 617, 740);
    } catch (IOException e) {
      e.printStackTrace();
    }
    }

    public static void main(String[] args) {
        Osero osero = new Osero( "osero" );

    }


}//Osero