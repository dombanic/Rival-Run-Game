//levelSelect

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.awt.Image;
import javax.imageio.ImageIO;
import java.io.File;
import javax.swing.*;
import javax.sound.sampled.*;

class levelSelect extends Frame implements MouseListener
{
   private Graphics g, gBuffer;   
   private Image virtualMem;
   private Rectangle desert, space;
   private boolean ready;
   private Image background1, background2;
   
   public levelSelect()
   {
      super("levelSelect");
      ready = false;
      setSize(1922,1020);
      setVisible(true);
      virtualMem = createImage(1922,1020);
      gBuffer = virtualMem.getGraphics();
      addMouseListener(this);
      desert = new Rectangle(80,230,815,540);
      space = new Rectangle(1015,230,815,540);
      initializeTextures();
      ready = true;
      repaint();
   }
   
   public void paint(Graphics g)
   {
      if(ready)
      {
         Graphics2D g2D = (Graphics2D) g;
         g2D.translate(10,45);
         gBuffer.setColor(new Color(100,10,100));
	      gBuffer.fillRect(0,0,1922,1010);
         gBuffer.setColor(new Color(50,50,50));
         Grfx.fillRoundedRectangle(gBuffer,80,230,895,770,10);
         Grfx.fillRoundedRectangle(gBuffer,1015,230,1830,770,10);
         gBuffer.drawImage(background1,100,250,775,500,this);
         gBuffer.drawImage(background2,1035,250,775,500,this);
         g.drawImage (virtualMem,0,0,this);
         repaint();
      }
   }
   
   private void initializeTextures()
	{
      try
      {
         background1 = ImageIO.read(new File("textures/desert.png"));
         background2 = ImageIO.read(new File("textures/space.png"));
      }
      catch (IOException e)
      {
         System.out.println("textures: " + e);
      }
   }
   
   public void mousePressed(MouseEvent e)
   {
		int x = e.getX() - 10;  
		int y = e.getY() - 45;
		if(desert.contains(x,y))
      {
         this.setVisible(false);
         desertSelect game = new desertSelect();
   		game.addWindowListener(new WindowAdapter()
   		{public void windowClosing(WindowEvent e)
   		{System.exit(0);}});
      }
      if(space.contains(x,y))
      {
         this.setVisible(false);
         spaceSelect game = new spaceSelect();
   		game.addWindowListener(new WindowAdapter()
   		{public void windowClosing(WindowEvent e)
   		{System.exit(0);}});
      }
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
		repaint();
   }
   
   public void mouseClicked(MouseEvent e)  { }
   public void mouseEntered(MouseEvent e)    { }
   public void mouseExited(MouseEvent e)    { }
   public void mouseReleased(MouseEvent e) { }
   
   public void update(Graphics g)
   {
      paint(g);
   }
}