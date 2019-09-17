/*
 * ICS4U1_Culminating.java
 * Music Game
 * Jackie Chen, Grace Ting
 * ICS4U1_02
 * December 24, 2017
 */
 
 
 import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
 
 
public class ICS4U1_Culminating {
 
    public static void main(String[] args) {
        MyFrame sf  = new MyFrame() ;
		sf.setSize(900,700);     
		sf.setVisible(true); 
		sf.setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );	
		
		try {
			FileInputStream fileInputStream = new FileInputStream("Songs/song1.mp3");
			Player player = new Player(fileInputStream);
			player.play();
        }
		catch(Exception ex) {
			ex.printStackTrace();
		}
    }
}

class MyFrame extends JFrame {
	MyPanel game;
	public MyFrame(){
		super("Music Game");
		Container c = getContentPane();
		c.setBackground(new Color(154,183,228));
		game = new MyPanel();
		c.setLayout(new BorderLayout());
		c.add(game,BorderLayout.CENTER);
	}
}

class MyPanel extends JPanel implements ActionListener{
	public MyPanel() {
	
	}
	
	public void actionPerformed(ActionEvent e) {
		
	}

}