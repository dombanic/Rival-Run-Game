//Main

import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.awt.Image;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

public class Main
{
   public static void main(String args[])
   {
      RivalRun game = new RivalRun();
      game.addWindowListener(new WindowAdapter()
      {public void windowClosing(WindowEvent e)
      {System.exit(0);}});
   }
}


class RivalRun extends Frame implements KeyListener, FocusListener, MouseListener
{
   private Graphics g, gBuffer;   
   private Image virtualMem;     
   private boolean focus;
   private boolean noClickYet;
   private boolean ready;
   private int windowWidth = 1900;    
   private int windowHeight = 1000;
   private Random rand = new Random(); 
   private Image char1, char2, BG;   
   private int gameClock;
   private Rectangle start, instructions, mapEditor, back;
   private int numPage;

   public RivalRun()
   {
      super("Rival Run");
      ready = false;
      setSize(windowWidth+22,windowHeight+60);
      setVisible(true);
      virtualMem = createImage(windowWidth,windowHeight);
      gBuffer = virtualMem.getGraphics();
      addKeyListener(this);
      addFocusListener(this);
      addMouseListener(this);
      start = new Rectangle(500,500,900,100);
		instructions = new Rectangle(500,650,900,100);
		mapEditor = new Rectangle(500,800,900,100);
      back = new Rectangle(75,60,75,60);
      numPage = 0;
      focus = true;
      ready = true;
      initializeTextures();
      noClickYet = true;
      repaint();
   }

   public void paint(Graphics g)
   {
      if (ready)
      {
         Graphics2D g2D = (Graphics2D) g;
         g2D.translate(11,49);
         if (numPage == 0)
         {
            titleScreen();       
            g.drawImage (virtualMem,0,0,this);
            Grfx.delay(20);
            gameClock++;
         }
         
         switch (numPage)
		   {
			case 1:
            displayStart();
            g.drawImage (virtualMem,0,0,this);
				break;
			case 2:
            displayInstructions();
            g.drawImage (virtualMem,0,0,this);
				break;
			}
         repaint();
      }   
   }
   
   public void initializeTextures()
	{
      try
      {
   	   char1 = ImageIO.read(new File("textures/char1.png"));
         char2 = ImageIO.read(new File("textures/char2.png"));
         int randomInt = rand.nextInt(2)+1;
         if(randomInt == 1)
            BG = ImageIO.read(new File("textures/desert.png"));
         else
            BG = ImageIO.read(new File("textures/space.png"));
      }
      catch (IOException e)
      {
         System.out.println("textures: " + e);
      }
	}
   
   public void titleScreen()
   {
      gBuffer.drawImage(BG,0,0,1900,1000,this);

      Font titleFont = new Font("Algerian",Font.ITALIC,350);
      String title = "Rival Run";
      Font keys = new Font("Times New Roman",Font.PLAIN,72);
      
      gBuffer.setFont(titleFont);
      FontMetrics metrics = gBuffer.getFontMetrics(titleFont);
      
      int w = metrics.stringWidth(title);         //Buttons
      gBuffer.setColor(new Color(120,120,120));
      gBuffer.drawString(title,(windowWidth-w)/2-13,400);
      gBuffer.setColor(Grfx.white);
      Grfx.fillRoundedRectangle(gBuffer,500,500,1400,600,25);
      Grfx.fillRoundedRectangle(gBuffer,500,650,1400,750,25);
      Grfx.fillRoundedRectangle(gBuffer,500,800,1400,900,25);
      
      drawCenteredString("Play", keys, Color.black, 580);
      drawCenteredString("Instructions", keys, Color.black, 730);
      drawCenteredString("Map Editor", keys, Color.black, 880);
      int yValue = (int) (Math.sin((double) gameClock / 5 % 100)*50);
      
      //Draw left Char
      gBuffer.drawImage(char1,450,500+yValue,-(10*35),16*25,this);
         
      //draw right Char
      gBuffer.drawImage(char2,1450,500-yValue,10*35,16*25,this);
   }
      
   public void drawCenteredString(String text, Font font, Color color, int  y)
   {
      gBuffer.setFont(font);
      FontMetrics metrics = gBuffer.getFontMetrics(font);
      int w = metrics.stringWidth(text);
      gBuffer.setColor(color);
      gBuffer.drawString(text,(windowWidth-w)/2-13,y);
   }
   
   public void displayStart()
	{
      Grfx.setBackground(gBuffer,Grfx.darkRed);
      gBuffer.setColor(Grfx.darkBlue);
      Grfx.drawThickRectangle(gBuffer,0,0,windowWidth,windowHeight,48);
      Grfx.drawThickRoundedRectangle(gBuffer,0,0,windowWidth,windowHeight,48,48);
      
      Font titleFont = new Font("Algerian",Font.ITALIC,250);
      String title = "Play";
      Font keys = new Font("Times New Roman",Font.PLAIN,72);
      
      gBuffer.setColor(Grfx.white);
      gBuffer.setFont(titleFont);
      FontMetrics metrics = gBuffer.getFontMetrics(titleFont);
      
      int w = metrics.stringWidth(title);
      gBuffer.drawString(title,(windowWidth-w)/2-13,300);
      
      Grfx.fillRoundedRectangle(gBuffer,500,500,1400,600,25);
      Grfx.fillRoundedRectangle(gBuffer,500,650,1400,750,25);
      Grfx.fillRoundedRectangle(gBuffer,500,800,1400,900,25);
      
      gBuffer.setColor(Grfx.darkBlue);
      Grfx.fillPolygon(gBuffer,75,85,100,60,100,75,150,75,150,95,100,95,100,110);
      
      drawCenteredString("Select Game Mode", keys, Color.black, 580);
      drawCenteredString("Level Select", keys, Color.black, 730);
      drawCenteredString("Sandbox", keys, Color.black, 880);
	}
   
   public void displayInstructions()
	{      
      gBuffer.drawImage(BG,0,0,1900,1000,this);
      
      Font titleFont = new Font("Algerian",Font.ITALIC,250);
      String title = "Instructions";
      Font keys = new Font("Times New Roman",Font.PLAIN,72);
      
      
      gBuffer.setColor(new Color(120,120,120));
      gBuffer.setFont(titleFont);
      FontMetrics metrics = gBuffer.getFontMetrics(titleFont);
      
      int w = metrics.stringWidth(title);
      gBuffer.drawString(title,(windowWidth-w)/2-13,300);
      
      gBuffer.setColor(Color.white);
      Grfx.fillRoundedRectangle(gBuffer,400,500,1500,900,25);

      
      gBuffer.setColor(Grfx.darkBlue);
      Grfx.fillPolygon(gBuffer,75,85,100,60,100,75,150,75,150,95,100,95,100,110);
      
      drawCenteredString("Player one uses: WASD to move", keys, Color.black, 580);
      drawCenteredString("Player two uses: IJKL to move", keys, Color.black, 680);
      drawCenteredString("Objective: Beat your opponent", keys, Color.black, 780);
      drawCenteredString("by pew pewing them", keys, Color.black, 880);
	}
   
   public void mousePressed(MouseEvent e)
   {
      int x = e.getX() - 11;  
		int y = e.getY() - 49;
		if(start.contains(x,y) && numPage==0)
      {
         this.setVisible(false);
         levelSelect levelselect = new levelSelect();
   		levelselect.addWindowListener(new WindowAdapter()
   		{public void windowClosing(WindowEvent e)
   		{System.exit(0);}});
      }   
		else if(instructions.contains(x,y) && numPage==0)
			numPage = 2;
      else if(start.contains(x,y) && numPage==1)
			numPage = 1;
		else if(instructions.contains(x,y) && numPage==1)
      {
         this.setVisible(false);
         levelSelect levelselect = new levelSelect();
   		levelselect.addWindowListener(new WindowAdapter()
   		{public void windowClosing(WindowEvent e)
   		{System.exit(0);}});
      }    
		else if(mapEditor.contains(x,y) && numPage==1)
         numPage = 1;
		else if(mapEditor.contains(x,y) && numPage==0)
      {
         this.setVisible(false);
         MapMaker mapMaker = new MapMaker();
         mapMaker.addWindowListener(new WindowAdapter()
         {public void windowClosing(WindowEvent e)
         {System.exit(0);}});
      }         
      else if(back.contains(x,y))
			numPage = 0;
      try
      {
         File f = new File("sounds/Click.wav");
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
   public void focusGained(FocusEvent evt) { focus = true;       }
   public void focusLost(FocusEvent evt)   { focus = false;      }

   public void keyReleased(KeyEvent e)     { }
   public void keyTyped(KeyEvent e)        { }
   public void mouseClicked(MouseEvent e)  { }
   public void mouseEntered(MouseEvent e)    { }
   public void mouseExited(MouseEvent e)    { }
   public void mouseReleased(MouseEvent e) { }      

   public void keyPressed(KeyEvent e)  { }
      
   public void update(Graphics g)
   {
      paint(g);
   } 
}
  