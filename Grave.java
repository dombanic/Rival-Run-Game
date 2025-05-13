import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.io.*;
import java.awt.Image;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.image.ImageObserver;

public class Grave
{
   private Graphics g;
   private int xCoor, yCoor, direction, width, height;
   private int speed = 10;
   private boolean collided;
   private ImageObserver screen;
   
   private Image grave;
   
   public Grave(int x, int y, Graphics g, int width, int height, ImageObserver screen, int playerNum)
   {
      this.xCoor = x;
      this.yCoor = y;
      this.g = g;
      this.width = width;
      this.height = height;
      this.screen = screen;
      try
      {
   	   if (playerNum == 0)
            grave = ImageIO.read(new File("textures/grave.png"));
         else
            grave = ImageIO.read(new File("textures/grave2.png"));
      }
      catch (IOException e)
      {
         System.out.println("textures: " + e);
      }
   }
   
   public void fall()
   {
      if (canFall())
         yCoor+=10;
      else
         lineUpY();
      draw();
   }
   
   public boolean canFall()
   {
      int x, x2, y, y2;
      x = xCoor / Game.getPixelSize();  //left x
      x2 = (xCoor+width) / Game.getPixelSize();   //right x
      y = (yCoor+1) / Game.getPixelSize()-1;  //current y position - top y
      y2 = (yCoor+1) / Game.getPixelSize();  //bottom y
      
      //g.fillRect(x*Game.getPixelSize(),y*Game.getPixelSize(),Game.getPixelSize(),Game.getPixelSize());
      //g.fillRect(x*Game.getPixelSize(),y2*Game.getPixelSize(),Game.getPixelSize(),Game.getPixelSize());
      
      try
		{
         if ((!Player.empty(Game.getCourse1(y2, x)) || !Player.empty(Game.getCourse1(y2, x2))) && (Player.empty(Game.getCourse1(y, x)) && Player.empty(Game.getCourse1(y, x2))))
            return false;
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			return true;  //fell off map
		}
      return true;
   }
   
   private void draw()
   {
      g.drawImage(grave,xCoor,yCoor-height,width,height,screen);
   }
   
   private void lineUpY()
   {
      int y = yCoor / Game.getPixelSize();
      yCoor = y * (Game.getPixelSize());
   }
}