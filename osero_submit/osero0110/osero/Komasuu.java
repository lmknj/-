package osero;

import java.awt.Graphics;
import java.io.Serializable;

import javax.swing.JPanel;

public class Komasuu extends JPanel  implements Serializable{
	 public void paintComponent(Graphics g) {
		 rect(200,50,g);
		 rect(200,550,g);
	 }

	 public void rect(int x ,int y,Graphics g){
		 g.drawRect(x, y, 200, 100);

	 }
}
