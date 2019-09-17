/*
 * ICS4U1_MapCreator.java
 * Map creator
 * Jackie Chen, Grace Ting
 * ICS4U1_02
 * December 6, 2017
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import javax.swing.filechooser.*;
import java.util.*;

import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.*;

//The MapCreator class allows the user to create a map
public class ICS4U1_MapCreator {
	public static void main(String[] args){
		MyFrame frame = new MyFrame("Map Creator"); 
	}
}

class MyFrame extends JFrame implements ActionListener, MouseMotionListener{
	private Container c;
	private JPanel p1, p2, p3, p4, northPanel;
	private Color clickedColor = Color.gray;
	boolean selected = false;
	private JButton buttonArray[];
	private String buttons[] = {"Log1", "Log2", "Dirt", "Grass", "MRE", "First Aid Kit", "Egg", "Player", "Exit"}; 
	private JButton gridArray[];
	private JButton btn_save = new JButton("Save");   
	
	private JButton btn_exit = new JButton("Exit");
	private int width, height;
	private MyMap mapIn, mapToSave;
	private JFileChooser fc;
	private JScrollPane logScrollPane;
	private JTextArea txt_saveInfo;
	private ImageIcon[] imgI;
	private int selectedType;
	private boolean playerPut, hasPlayer;
	public static int x,y; // public, make private and method later
	
	public MyFrame(String s) {
		fc = new JFileChooser();
		setTitle(s);
		setResizable(false);
		MyImg.loadImage();
		imgI = new ImageIcon[buttons.length+1];
		loadImgArray();
		selectedType = 0;
		c = getContentPane();
		p1 = new JPanel();
		p2 = new JPanel();
		p3 = new JPanel(new FlowLayout(FlowLayout.CENTER, 20,150));
		p4 = new JPanel();
		northPanel= new JPanel();
		p1.setLayout(new GridLayout(8,1,10,10));
		buttonArray = new JButton[buttons.length];
		playerPut = false;
		hasPlayer = false;
		x=0;
		y=0;
		
		for (int i = 0; i < buttons.length; i++) {
			buttonArray[i] = new JButton(buttons[i]);
			buttonArray[i].setBackground(new Color(210, 205, 244));
			buttonArray[i].setBorderPainted(false);
			p1.add(buttonArray[i]);
			buttonArray[i].addActionListener(this);
		}
		
		p2.setLayout(new GridLayout(20,20,3,3));
		gridArray= new JButton[400];
		for (int i = 0; i < 400; i++) {
			gridArray[i] = new JButton("");
			gridArray[i].setIcon(new ImageIcon(""));
			gridArray[i].setBackground(new Color(205, 225, 244));
			gridArray[i].setBorderPainted(false);
			final int x = i;
			gridArray[i].addActionListener(this);
			gridArray[i].addMouseMotionListener(this);
			p2.add(gridArray[i]);
		}
		
		width = gridArray[0].getWidth();
		height = gridArray[0].getHeight();
		
		c.add(p3, BorderLayout.EAST);
		p3.add(p1);
		c.add(p2, BorderLayout.CENTER);
		
		p4.setLayout(new GridLayout(1,2));
		btn_save.addActionListener(this);
		p4.add(btn_save);
		
		btn_exit.addActionListener(this);
		p4.add(btn_exit);
		c.add(p4, BorderLayout.SOUTH);
		
		
		setSize(900,700);
		setVisible(true);
		setDefaultCloseOperation ( WindowConstants.EXIT_ON_CLOSE);
		
		txt_saveInfo = new JTextArea(5,20);
		txt_saveInfo.setMargin(new Insets(5,5,5,5));
		txt_saveInfo.setEditable(false);
		logScrollPane = new JScrollPane(txt_saveInfo);
		northPanel.add(txt_saveInfo);
	}
	
	public void actionPerformed(ActionEvent e) {
		JButton b = (JButton) e.getSource();

		if (b == btn_exit){
			System.exit(0);
		} else if (b == btn_save) {
			mapToSave=createMap();
			try{
				int returnVal = fc.showSaveDialog(this);   // need to import javax.swing.filechooser.*;
      
				if (returnVal == JFileChooser.APPROVE_OPTION) {  // if user did not click cancel and picked file name
					File file = fc.getSelectedFile();    // picked name
					txt_saveInfo.append("Saving: " + file.getName() + ".\n" );
					saveMethod(file);
				} else {
					txt_saveInfo.append("Save command cancelled by user.\n"); 
				}
		    }catch(Exception ex){
                System.out.print("Error" + ex);
            }		
		} 
		
		else if (b==buttonArray[0]) {
			selected = true;
			clickedColor = Color.red;
			selectedType = 0;
		} else if (b == buttonArray[1]) {
			selected = true;
			clickedColor = Color.green;
			selectedType = 1;
		} else if (b == buttonArray[2]) {
			selected = true;
			clickedColor = Color.blue;
			selectedType = 2;
		} else if (b == buttonArray[3]) {
			selected = true;
			clickedColor = Color.pink;
			selectedType = 3;
		} else if (b == buttonArray[4]) {
			selected = true;
			clickedColor = Color.black;
			selectedType = 4;
		} else if (b == buttonArray[5]) {
			selected = true;
			clickedColor = Color.cyan;
			selectedType = 5;
		} else if (b == buttonArray[6]) {
			selected = true;
			clickedColor = Color.gray;
			selectedType = 6;
		} else if (b == buttonArray[7]) {
			selected = true;
			clickedColor = Color.yellow;
			selectedType = 7;
		} else if (b == buttonArray[8]) {
			selected = true;
			clickedColor = Color.magenta;
			selectedType = 8;
		} else {
			if (selected) {
				if (clickedColor == Color.yellow && playerPut == false) {
					playerPut = true;
					buttonArray[7].setEnabled(false);
					selected = false;
				} else if (clickedColor != Color.yellow || playerPut == false) {
					for (int i = 0; i < gridArray.length; i++) {
						hasPlayer = false;
						if (gridArray[i].getBackground() == Color.yellow) {
							hasPlayer = true;
							break;
						}
					}
					if (hasPlayer == false) {
						buttonArray[7].setEnabled(true);
						playerPut = false;
					} 
				}
				b.setBackground(clickedColor);
				b.setIcon(imgI[selectedType]);
			}
		}
	}
	public void loadImgArray() {
		for (int i = 0; i < MyImg.getNumWall() + MyImg.getNumObj() + MyImg.getNumPlayer() + MyImg.getNumExit() + 1; i++) {
			if (i < MyImg.getNumWall()) {
				imgI[i] = new ImageIcon(MyImg.getWallImg(i));
			} else if (i < MyImg.getNumWall() + MyImg.getNumObj()) {
				imgI[i] = new ImageIcon(MyImg.getObjImg(i-MyImg.getNumWall()));
			} else if (i < MyImg.getNumWall() + MyImg.getNumObj() + MyImg.getNumPlayer()){
				imgI[i] = new ImageIcon(MyImg.getPlayerObj());
			} else {
				imgI[i] = new ImageIcon(MyImg.getExitObj());
			}
		}
	}
	public void mouseMoved(MouseEvent me) {}
	public void mouseDragged(MouseEvent me) {
		int mAtX=me.getXOnScreen(); 
		int mAtY=me.getYOnScreen();
		 
		int w=gridArray[0].getWidth();   
		int h=gridArray[0].getHeight();
		if (playerPut){
			buttonArray[7].setEnabled(false);
		}
		
		for (int i = 0; i < gridArray.length; i++ ) {
			int btnX= gridArray[i].getLocationOnScreen().x;
		    int btnY=gridArray[i].getLocationOnScreen().y; 
			
			if(mAtX>=btnX && mAtX<=btnX+w && mAtY>=btnY && mAtY<=btnY+h ) {
				if (clickedColor == Color.yellow && playerPut == false) {
					playerPut = true;
					buttonArray[7].setEnabled(false);
					selected = false;
					gridArray[i].setBackground(clickedColor);
					gridArray[i].setIcon(imgI[selectedType]);
				} else if (clickedColor != Color.yellow || playerPut == false) {
					gridArray[i].setBackground(clickedColor);
					gridArray[i].setIcon(imgI[selectedType]);
				}
				
				for (int k = 0; k < gridArray.length; k++) {
					hasPlayer = false;
					if (gridArray[k].getBackground() == Color.yellow) {
						hasPlayer = true;
						break;
					}
				}
				if (!hasPlayer) {
					buttonArray[7].setEnabled(true);
					playerPut = false;
				}
			}
		} 		
	}
	

	
	public MyMap createMap() { 
		MyMap m = new MyMap();
		for (int i = 0; i < 400; i++) {
			if (gridArray[i].getBackground() == Color.red) {
				m.addTile(new Tile(gridArray[i].getLocation().x, gridArray[i].getLocation().y, 0)); 
			} else if (gridArray[i].getBackground() == Color.green) {
				m.addTile(new Tile(gridArray[i].getLocation().x, gridArray[i].getLocation().y, 1)); 
			} else if (gridArray[i].getBackground() == Color.blue) {
				m.addTile(new Tile(gridArray[i].getLocation().x, gridArray[i].getLocation().y, 2)); 
			} else if (gridArray[i].getBackground() == Color.pink) {
				m.addTile(new Tile(gridArray[i].getLocation().x, gridArray[i].getLocation().y, 3));
			} else if (gridArray[i].getBackground() == Color.black) {
				m.addGameObj(new GameObject(gridArray[i].getLocation().x, gridArray[i].getLocation().y, 0));
			} else if (gridArray[i].getBackground() == Color.cyan) {
				m.addGameObj(new GameObject(gridArray[i].getLocation().x, gridArray[i].getLocation().y, 1)); 
			} else if (gridArray[i].getBackground() == Color.gray) {
				m.addGameObj(new GameObject(gridArray[i].getLocation().x, gridArray[i].getLocation().y, 2)); 
			} else if (gridArray[i].getBackground() == Color.yellow) {
				x = gridArray[i].getLocation().x;
				y = gridArray[i].getLocation().y;
				m.addPlayer(new Player(x, y)); 
			}else if (gridArray[i].getBackground() == Color.magenta) {
				m.addExit(new ExitObject(gridArray[i].getLocation().x, gridArray[i].getLocation().y, 0));
			}
		}
		return m;
	}
	
	public void saveMethod(File filePath){
		FileOutputStream fout = null;
		ObjectOutputStream oos = null;

		try {
			fout = new FileOutputStream(filePath);
			oos = new ObjectOutputStream(fout);
			oos.writeObject(mapToSave);
			System.out.println("Tiles read: "+mapToSave.getAllTiles().size());
			System.out.println("GameObjects read:" + mapToSave.getAllObjects().size());
			fout.close();
			oos.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	

}



class MyMap implements Serializable {
	private ArrayList<Tile> allTiles;
	private ArrayList <GameObject> myObj;
	private Player player;
	private ExitObject exit;
	
	public MyMap(){
	    allTiles = new ArrayList<Tile>();
		myObj = new ArrayList<GameObject>();
		player = new Player(900,700);
		exit = new ExitObject(1000,800, 0);
	}
	public ArrayList<Tile> getAllTiles(){
		return allTiles;
	}
	public void addTile(Tile t){
		allTiles.add(t);
	}
	public void addGameObj(GameObject t){
		myObj.add(t);
	}
	public void addPlayer(Player t){
		player = t;
	}
	public void addExit(ExitObject e) {
		exit = e;
	}
	public void setAllTiles(ArrayList<Tile> a){
		allTiles=a;
	}
	public  ArrayList<GameObject> getAllObjects(){
		return myObj;
	}
	public Player getPlayer() {
		return player;
	}
	public ExitObject getExit() {
		return exit;
	}
	public void setAllObjects(ArrayList <GameObject> go){
		myObj=go; 
	}
}

class Tile extends Rectangle implements Serializable{
    protected int type;
    private ImageIcon imgI;
    public Tile(int x, int y, int type) {
        super (x,y,30,30);
        this.type = type;
    }
    public void draw(Graphics g){
        Graphics g2d= (Graphics2D)g;
        BufferedImage wallImg=MyImg.getWallImg(type);
        g2d.drawImage(wallImg,x,y,width,height,null,null);
    }
}

 




