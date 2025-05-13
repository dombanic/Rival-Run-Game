import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.io.*;
import java.awt.Image;
import javax.imageio.ImageIO;
import java.io.File;
import javax.swing.*;
import java.util.ArrayList;
import javax.sound.sampled.*;

class Game extends Frame implements KeyListener, FocusListener, MouseListener
{
	private int frameWidth;		   // width of the Frame window
	private int frameHeight;		// height of the Frame window
	private Graphics g, gBuffer;	// used for virtual memory/double buffering
   private Image virtualMem;     // to eliminate flicker in the animation
   private char keyFired;			// the last key pressed by the user
   private boolean ready;        // becomes true when the constructor is finished 
   private int gameClock;  
   
   private static char[][] course;
   private static int pixelSize;
   
   //Player
   private int numPlayers = 2;
   private Player[] players = new Player[numPlayers];
   
   //Misc
   private ArrayList<Bullet> bullets = new ArrayList<Bullet>();
   private ArrayList<Grave> graves = new ArrayList<Grave>();
   
   //Textures
   private Image background, char1, char2, greenFlag, redFlag, grave;
   
   //Colors
   Color grey = new Color(120,120,120);
   Color purple = new Color(122,0,150);
   Color brown = new Color(100,60,0);
   

	public Game(File file, int bgNum)
   {
      super("RivalRun");
      ready = false;
      setSize(1920,1050);
		setVisible(true);
  		frameWidth = 1920;
		frameHeight = 1050;
		virtualMem = createImage(frameWidth,frameHeight);
		gBuffer = virtualMem.getGraphics();
		keyFired = ' ';
		addKeyListener(this);
		addFocusListener(this);
      addMouseListener(this);
      initializeTextures(bgNum);
      readFile(file);
      
      if (frameWidth / course[0].length >= frameHeight / course.length)
         pixelSize = frameHeight / course.length;
      else
         pixelSize = frameWidth / course[0].length;
      
      int xC = 0;
      int yC = 0; 
      int xC2 = 0;
      int yC2 = 0;
      for (int y = 0;y<course.length;y++)
      {
         for (int x = 0;x<course[0].length;x++)
         {
            if (course[y][x] == 'a')
            {
               xC = x*pixelSize;
               yC = y*pixelSize;
            }
            else if (course[y][x] == 'b')
            {
               xC2 = x*pixelSize;
               yC2 = y*pixelSize;
            }

         }
      }
      
      Player.passGraves(graves);
      players[0] = new Player(gBuffer, char1, xC, yC, this, 0);
      players[1] = new Player(gBuffer, char2, xC2, yC2, this, 1);
         
      ready = true;
      repaint();
	}

	public void paint(Graphics g)
	{
      if (ready)
      {
         this.g = g;  
         Graphics2D g2D = (Graphics2D) g;
   		g2D.translate(10,40); 
         
         drawBackground();
         playerMove();
         exist();

         g.drawImage (virtualMem,0,0,this);
         gameClock++;
         Grfx.delay(15);
   		repaint(); 
      }
   }
   
   private void initializeTextures(int bgNum)
	{
      try
      {
         if(bgNum == 1)
            background = ImageIO.read(new File("textures/desert.png"));
         else
            background = ImageIO.read(new File("textures/space.png"));
   	   char1 = ImageIO.read(new File("textures/char1.png"));
         char2 = ImageIO.read(new File("textures/char2.png"));
         redFlag = ImageIO.read(new File("textures/redFlag.png"));
         greenFlag = ImageIO.read(new File("textures/greenFlag.png"));
         grave = ImageIO.read(new File("textures/grave.png"));
      }
      catch (IOException e)
      {
         System.out.println("textures: " + e);
      }
	}
   
   private void drawBackground()
   {
      gBuffer.drawImage(background,0,0,frameWidth,frameHeight,this);
      
      for (int y = 0; y < course.length;y++)
      {
         for (int x = 0; x < course[y].length;x++)
         {
            if (colorPicker(Integer.valueOf(course[y][x])-48) != null)
            {
               if (Integer.valueOf(course[y][x]) < 97)
               {
                  gBuffer.setColor(colorPicker(Integer.valueOf(course[y][x])-48));
                  gBuffer.fillRect(x*pixelSize,y*pixelSize,pixelSize,pixelSize);
               }
            }
            else
            {
               if (course[y][x] == 'a')
                  gBuffer.drawImage(greenFlag,x*pixelSize,y*pixelSize,pixelSize,pixelSize,this);
               else if (course[y][x] == 'b')
                  gBuffer.drawImage(redFlag,x*pixelSize,y*pixelSize,pixelSize,pixelSize,this);
            }
            
         }
      }
   }
   
   private void exist()
   {
      for (Bullet bullet : bullets)
         bullet.exist();
      for (int i = bullets.size()-1; i >= 0;i--)
         if (bullets.get(i).isCollided())
            bullets.remove(i);
      for (Bullet bullet : bullets)
      {
         if (players[0] == bullet.getParent())
         {
            if (bullet.hitPlayer(players[1])==true)
               players[1].reset();
         }
         else 
         {
            if (bullet.hitPlayer(players[0])==true)
               players[0].reset();
         }
      }
      for (Grave grave : graves)
         grave.fall();
      for (Player character : players)
         character.exist();
   }
   
   private Color colorPicker(int color)
   {
      switch(color)
		{
			case 0  : return null;
			case 1  : return Color.red;
			case 2  : return Color.orange;
			case 3  : return Color.yellow;
         case 4  : return Color.green;
			case 5  : return Color.blue;
			case 6  : return purple;
         case 7  : return Color.pink;
			case 8  : return brown;
			case 9  : return grey;
			default : return null;
		}
   }
   
   public void readFile(File file)
   {
      try
		{
			BufferedReader inStream = new BufferedReader(new FileReader(file));
			String line;
         int CWidth = Integer.parseInt(inStream.readLine());
         int CHeight = Integer.parseInt(inStream.readLine());
         course = new char[CHeight][CWidth];

			for (int row = 0; row < CHeight;row++)
         {
            line = inStream.readLine();
            for (int column = 0; column < CWidth;column++)
            {
               course[row][column] = line.charAt(column);
            }
         }
			inStream.close();
		}
		catch (IOException e)
		{
			System.out.println("There were problems with reading the map file as stated below\n");
			System.out.println(e.getMessage());
		}
   }
   
   private void playerMove()
   {
      int shiftAmount = 5;
      
      //Player 1
      if (Keys.getState('a'))
         players[0].moveX(-5);
      if (Keys.getState('d'))
         players[0].moveX(5);
      if (Keys.getState('w'))
         players[0].jump();
         
      //Player 2
      if (Keys.getState('j'))
         players[1].moveX(-5);
      if (Keys.getState('l'))
         players[1].moveX(5);
      if (Keys.getState('i'))
         players[1].jump();
   }
   
   public void mousePressed(MouseEvent e)	 {}

	public void keyReleased(KeyEvent e)     
   {
      keyFired = String.valueOf(e.getKeyChar()).charAt(0);
		if (keyFired == 'a')  
			Keys.setFalse('a');
      if (keyFired == 'd')  
			Keys.setFalse('d');
      if (keyFired == 'w')  
			Keys.setFalse('w');
      if (keyFired == 's')  
         Keys.setFalse('s');
      if (keyFired == 'e')  
			Keys.setFalse('e');
		if (keyFired == 'j')  
			Keys.setFalse('j');
      if (keyFired == 'k')  
			Keys.setFalse('k');
      if (keyFired == 'l')  
			Keys.setFalse('l');
      if (keyFired == 'i')  
         Keys.setFalse('i');
      if (keyFired == 'o')  
			Keys.setFalse('o');
         
      if (keyFired == KeyEvent.VK_ESCAPE)
	   {
         this.setVisible(false);
         RivalRun game = new RivalRun();
         game.addWindowListener(new WindowAdapter()
         {public void windowClosing(WindowEvent e)
         {System.exit(0);}});
      }
   }
	public void keyPressed(KeyEvent e) 
   {
      keyFired = String.valueOf(e.getKeyChar()).charAt(0);
		if (keyFired == 'a')  
			Keys.setTrue('a');
      if (keyFired == 'd')  
			Keys.setTrue('d');
      if (keyFired == 'w')  
      {
			Keys.setTrue('w');
         try
         {
            File f = new File("sounds/Jump.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL());
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
         }
         catch (Exception ex)
         {
            System.out.println(ex);
         }
      }
      if (keyFired == 's')  
      {
			players[0].shoot(this);
         try
         {
            File f = new File("sounds/Pew.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL());
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
         }
         catch (Exception ex)
         {
            System.out.println(ex);
         }
      }
      if (keyFired == 'e')  
			Keys.setTrue('e');
		if (keyFired == 'j')  
			Keys.setTrue('j');
      if (keyFired == 'k')  
      {
			players[1].shoot(this);
         try
         {
            File f = new File("sounds/Pew.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL());
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
         }
         catch (Exception ex)
         {
            System.out.println(ex);
         }
      }
      if (keyFired == 'l')  
         Keys.setTrue('l');
      if (keyFired == 'i')  
      {
			Keys.setTrue('i');
         try
         {
            File f = new File("sounds/Jump.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL());
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
         }
         catch (Exception ex)
         {
            System.out.println(ex);
         }
      }
      if (keyFired == 'o')  
			Keys.setTrue('o');
      
      keyFired = 'm';
      repaint();
	}  
   
   
   
	public void focusGained(FocusEvent e)   {}
	public void focusLost(FocusEvent e) 	 {}
	public void keyTyped(KeyEvent e)      { } 
	public void mouseClicked(MouseEvent e)  { }
	public void mouseEntered(MouseEvent e)	 { }
	public void mouseExited(MouseEvent e)	 { }
	public void mouseReleased(MouseEvent e) { }
   
   public static char[][] getCourse()
   {
      return course;
   } 
   
   public static char getCourse1(int y, int x)
   {
      try { return course[y][x]; }
      catch(ArrayIndexOutOfBoundsException e) {return '.';}
   } 
   
   public static int getPixelSize()
   {
      return pixelSize;
   }
   
   public void addBullet(Bullet bullet)
   {
      bullets.add(bullet);
   }
   
   public void addGrave(Grave grave)
   {
      graves.add(grave);
   }
   
   public void update(Graphics g)
	{
		paint(g);
	}
}
