/*
 * ICS4U1_Game.java
 * Map Game
 * Jackie Chen, Grace Ting
 * ICS4U1_02
 * December 6, 2017
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import javax.swing.filechooser.*;
import java.awt.image.BufferedImage;
import javax.imageio.*;

//The Game class runs the maze game
public class ICS4U1_Game {
	public static void main(String[] args){
		GameFrame frame = new GameFrame("Game"); 
	}
}

class GameFrame extends JFrame implements ActionListener{
	private Container c;
	private JPanel control;
	private InventoryPanel inventory;
	private GamePanel gamePanel;
	private JButton btn_open = new JButton("Open");  
	private JFileChooser fc;
	private MyMap mapIn, map1;
	private boolean up,down,left,right;
	private int x, y;
	
	public GameFrame(String s) {
		MyImg.loadImage();
		fc = new JFileChooser();
		setTitle(s);
		
		control = new JPanel();
		inventory = new InventoryPanel();
		c = getContentPane();
		gamePanel = new GamePanel(this);
		c.add(gamePanel,BorderLayout.CENTER);
		c.add(control, BorderLayout.SOUTH);
		c.add(inventory, BorderLayout.EAST);
		c.addKeyListener(gamePanel);
		control.add(btn_open);
		map1 = new MyMap();
		mapIn = new MyMap();
		
		btn_open.addActionListener(this);
		btn_open.addKeyListener(gamePanel);
		
		setSize(750,720);
		setVisible(true);
		setResizable(false);
		up = false;
		down = false;
		left = false;
		right = false;
		x = MyFrame.x;
		y = MyFrame.y;

		setDefaultCloseOperation ( WindowConstants.EXIT_ON_CLOSE);
	}

	
	public void actionPerformed(ActionEvent e) {
		JButton b = (JButton) e.getSource();	
		
		if (b == btn_open) {
			try{
				int returnVal = fc.showOpenDialog(this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					openMethod(file);
				} 
		    }catch(Exception ex){	
				System.out.print("Error" + ex);
            }		
		}
	}	
	public void openMethod(File filePath){
		FileInputStream fileIn = null;
		ObjectInputStream objectIn = null;
		try{
			fileIn = new FileInputStream(filePath);
			objectIn = new ObjectInputStream(fileIn);
			mapIn = (MyMap)(objectIn.readObject());
			gamePanel.setMap(mapIn);
			
			System.out.print("Tiles opened: "+mapIn.getAllTiles().size());
			System.out.println("GameObjects opened:" + mapIn.getAllObjects().size());
			objectIn.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	public void openInv() {
		inventory.openInv();
		map1.getPlayer().toggleInv();
	}
	public void selectInv() {
		inventory.nextSelect();
		map1.getPlayer().nextInv();
	}
}

class InventoryPanel extends JPanel{
	private InventoryObject[] inventoryArr;
	private int x;
	private int y;
	private boolean openInv;
	private int selectedItem;
	
	public InventoryPanel() {
		x = 15;
		y = 50;
		selectedItem = 0;
		openInv = false;
		inventoryArr = new InventoryObject[MyImg.getNumObj()];
		setPreferredSize(new Dimension(100, 620));
		setBackground(new Color(234, 218, 187));
		for (int i = 0; i < MyImg.getNumObj(); i++) {
			inventoryArr[i] = new InventoryObject (x, y, i);
			y += 100;
		}
	}
	
	public void openInv() {
		openInv = !openInv;
		if (openInv) {
			inventoryArr[selectedItem].selected = true;
			if (selectedItem >= inventoryArr.length) {
				selectedItem = 0;
			}
		}
		else {
			for (int i = 0; i < inventoryArr.length; i++) {
				inventoryArr[i].selected = false;
			}
		}
	}
	
	public void nextSelect() {
		if (openInv) {
			for (int i = 0; i < inventoryArr.length; i++) {
				inventoryArr[i].selected = false;
			}
			inventoryArr[selectedItem].selected = true;
			selectedItem++; 
			if (selectedItem == inventoryArr.length) {
				selectedItem = 0;
			}
		}
	}
	public void paintComponent (Graphics g) {
		super.paintComponent(g);
		Graphics2D g2D = (Graphics2D)g;
		for (int i = 0; i < inventoryArr.length; i++) {
			inventoryArr[i].draw(g2D);
		}
		repaint();
	}
	public void print(boolean x) {
		System.out.println(x);
	}
}

class GamePanel extends JPanel implements ActionListener, KeyListener{
	Timer myTimer;
	Player p;
    MyMap map1;
	private boolean invSelect;
	private int invX[] = {200, 200, 650};
	private int invY[] = {50, 130, 210};
	private int invNum;
	GameFrame parent;
	
    public GamePanel(GameFrame parent) {
        this.parent = parent;
    	map1 = new MyMap();
		myTimer = new Timer(60, this);
		addKeyListener(this);
		invSelect = false;
		invNum = 0;
    }
   
    public void setMap(MyMap mm) {
        map1 = mm;
		p = map1.getPlayer();
		myTimer.start();
        repaint();
    }
	public MyMap getMap(){
		return map1;
	}
    public boolean getInvSelect() {
    	return invSelect;
    }
	public void paintComponent (Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < map1.getAllTiles().size(); i++) {
            map1.getAllTiles().get(i).draw(g);
        }
		for (int j = 0; j < map1.getAllObjects().size(); j++) {
			map1.getAllObjects().get(j).draw(g);
		}
		map1.getPlayer().draw(g);
		map1.getExit().draw(g);
		repaint();
    }
	public void actionPerformed(ActionEvent e) {
		map1.getPlayer().move(map1);
		repaint();
	}
	public void keyPressed(KeyEvent ev) {
		System.out.println(ev.getKeyCode());
		if (map1.getPlayer().gameOver == true) {
			JOptionPane.showMessageDialog(parent , "You win!");
		}
		
		if (ev.getKeyCode() == 38) {
			System.out.println("Player moving up");
			map1.getPlayer().up = true;
			map1.getPlayer().down = false;
		} 
		if (ev.getKeyCode() == 40) {
			System.out.println("Player moving down");
			map1.getPlayer().down = true;
			map1.getPlayer().up = false;
		}
		if (ev.getKeyCode() == 37) {
			System.out.println("Player moving left");
			map1.getPlayer().left = true;
			map1.getPlayer().right = false;
		}
		if (ev.getKeyCode() == 39) {
			System.out.println("Player moving right");
			map1.getPlayer().right = true;
			map1.getPlayer().left = false;
		}
		if (ev.getKeyCode() == 73) {
			System.out.print("Inventory selected");
			parent.openInv();
			
		}
		if (ev.getKeyCode() == 81) {
			parent.selectInv();
		}
	}

	public void keyReleased(KeyEvent ev) {
		System.out.println(ev.getKeyCode());
		if (ev.getKeyCode() == 38) {
			System.out.println("Player stopped moving up");
			map1.getPlayer().up = false;
		} 
		if (ev.getKeyCode() == 40) {
			System.out.println("Player stopped moving down");
			map1.getPlayer().down = false;
		}
		if (ev.getKeyCode() == 37) {
			System.out.println("Player stopped moving left");
			map1.getPlayer().left = false;
		}
		if (ev.getKeyCode() == 39) {
			System.out.println("Player stopped moving right");
			map1.getPlayer().right = false;
		}
	}
	public void keyTyped(KeyEvent e) {}
}

class GameObject extends Tile implements Serializable{
    public GameObject(int x, int y, int type) {
        super(x,y,type);
    }
    public void draw(Graphics g) {
        BufferedImage objImg = MyImg.getObjImg(type);
        g.drawImage(objImg, x, y, width, height, null, null);
    }
}

class ExitObject extends Tile implements Serializable{
	public ExitObject(int x, int y, int type) {
		super(x,y,type);
	}
	public void draw(Graphics g) {
		BufferedImage exitImg = MyImg.getExitObj();
		g.drawImage(exitImg, x, y, width, height, null, null);
	}
}


class InventoryObject extends Tile {
	boolean selected;
	public InventoryObject (int x, int y, int type) {
		super(x,y,type);
		selected = false;
	}
	public void draw(Graphics g) {
		BufferedImage invImg = MyImg.getObjImg(type);
		g.drawImage(invImg, x, y, 70, 70, null, null);
		if (selected) {
			Graphics2D g2D = (Graphics2D)g;
			g2D.setColor(Color.BLACK);
			g2D.setStroke(new BasicStroke(4f));
			g2D.drawRect(x-2, y-2, 74, 74);
		}
	}
}

class Player extends Tile implements Serializable{
	public boolean up,down,left,right, canMove, gameOver;
	public int selected;
	public boolean toggleInv;
	//public MyMap map1;
	public Player (int x, int y){
		super(x,y,0);
		gameOver = false;
		toggleInv = false;
		selected = 0;
	}
	public void draw(Graphics g) {
        BufferedImage playerImg = MyImg.getPlayerObj();
        g.drawImage(playerImg, x, y, width, height, null, null);
        if (toggleInv) {
        	BufferedImage invImg = MyImg.getObjImg(selected);
        	g.drawImage(invImg, x - 10, y - 10, 20, 20, null, null);
        }
        
    }
	public void toggleInv() {
		toggleInv = !toggleInv;
	}
	public void nextInv() {
		selected++;
		if (selected == MyImg.getNumObj()) {
			selected = 0;
		}
	}
	public void move(MyMap map1) {
		if (gameOver)
			return;
		canMove = true;
		if (up && y > 7) {
			y-=5;
		} else if (down && y < 597){
			y+=5;
		}
		
		if (left && x > 6) {
			x-=5;
		} else if (right && x < 600) {
			x+=5;
		}
		if (map1.getPlayer().intersects(map1.getExit())) {
			System.out.println("Player wins!");
			gameOver = true;
			return;
		}
		for (int i = 0; i < map1.getAllTiles().size(); i++) {
			if (map1.getPlayer().intersects(map1.getAllTiles().get(i))){
				System.out.println("Player intersects");
				canMove = false;
				if (!canMove) {
					System.out.println("Player can't move");
				}
				break;
			}
		}
		
		if (!canMove){
			if (up) {
				y+=5;
			} else if (down){
				y-=5;
			}
			
			if (left) {
				x+=5;
			} else if (right) {
				x-=5;
			}
			canMove = true;
		}
	}
}

class MyImg{
    public static BufferedImage wallObj[] = new BufferedImage[4];
    public static BufferedImage gameObj[] = new BufferedImage[3];
	public static BufferedImage playerObj[] = new BufferedImage[1];
	public static BufferedImage exitObj[] = new BufferedImage[1];
	
    public static void loadImage() {
        try {
            for (int i = 0; i < wallObj.length; i++) {
                wallObj[i] = ImageIO.read(new File("images/wallObj" + i + ".png"));
            }
            for (int j = 0; j < gameObj.length; j++) {
                gameObj[j] = ImageIO.read(new File("images/gameObj" + j + ".png"));
            }
			playerObj[0] = ImageIO.read(new File ("images/playerObj.png"));
			exitObj[0] = ImageIO.read(new File ("images/exit.png"));
        }
        catch(IOException ioe){
            System.out.println("Something is wrong with images' loading " + ioe);
        }
    }
    public static BufferedImage getWallImg(int i) {
        return wallObj[i];
    }
    public static BufferedImage getObjImg(int i) {
        return gameObj[i];
    }
	public static BufferedImage getPlayerObj(){
		return playerObj[0];
	}
	public static BufferedImage getExitObj(){
		return exitObj[0];
	}
	public static int getNumPlayer() {
		return playerObj.length;
	}
    public static int getNumWall() {
    	return wallObj.length;
    }
    public static int getNumObj() {
    	return gameObj.length;
    }
	public static int getNumExit() {
		return exitObj.length;
	}
}
