package osero;

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
        this.setSize(617, 640 );
        this.setVisible( true );
    }

    public static void main(String[] args) {
        Osero osero = new Osero( "osero" );

    }


}//Osero