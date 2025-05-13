import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.io.*;
import java.awt.Image;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import javax.sound.sampled.*;

public class Player
{
   private ImageObserver screen;
   Graphics g;
   Image texture, grave;
   private static ArrayList<Grave> graves;
   
   private int height = Game.getPixelSize();
   private int width = (int) (height * 5 / 8);
   private int xCoor, yCoor, xog, yog, direction, playerNum;
   private boolean jumping, phasing, hit;
   
   private int jumpPowerReset = height*2/5;
   private double jumpPower = jumpPowerReset;
   private double gravity = jumpPowerReset/20.0;
   
   
   //Coords
   int x;  //left x
   int x2; //right x
   int y; //current y position - top y
   int y2;  //bottom y
   
   Player(Graphics g, Image texture, int x, int y, ImageObserver screen, int playerNum)
   {
      xCoor = x;
      yCoor = y;
      xog = x;
      yog = y;
      this.g = g;
      this.texture = texture;
      this.screen = screen;
      this.playerNum = playerNum;

   }
   
   public void exist()
   {
      draw();
      jumping();
      if (yCoor > 1500)
         reset();
   }
   
   public void reset()
   {
      graves.add(new Grave(xCoor, yCoor, g, width, height, screen, playerNum));
      xCoor = xog;
      yCoor = yog;
      jumpPower = 10;
      try
      {
         File f = new File("sounds/Dead.wav");
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
   
   private void draw()
   {
      if (direction == 0)
         g.drawImage(texture,xCoor,yCoor-height,width,height,screen);
      else
         g.drawImage(texture,xCoor+width,yCoor-height,-width,height,screen);
   }
   
   private void jumping()
   {
      if (jumping)
      {
         yCoor -= jumpPower;
         jumpPower -= gravity;
         isPhasing();
      }
      if (!canFall())
      {
         jumping = false;
         lineUpY();
         jumpPower = jumpPowerReset;
      }
      if (!jumping && canFall())
      {
         jumping = true;
         jumpPower = 0;
      }
   }
   
   public boolean canFall()
   {
      x = xCoor / Game.getPixelSize();  //left x
      x2 = (xCoor+width) / Game.getPixelSize();   //right x
      y = (yCoor+1) / Game.getPixelSize()-1;  //current y position - top y
      y2 = (yCoor+1) / Game.getPixelSize();  //bottom y
      
      //g.fillRect(x*Game.getPixelSize(),y*Game.getPixelSize(),Game.getPixelSize(),Game.getPixelSize());
      //g.fillRect(x*Game.getPixelSize(),y2*Game.getPixelSize(),Game.getPixelSize(),Game.getPixelSize());
      
      try
		{
         if ((!empty(Game.getCourse1(y2, x)) || !empty(Game.getCourse1(y2, x2))) && (empty(Game.getCourse1(y, x)) && empty(Game.getCourse1(y, x2))) && !phasing)
            return false;
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			return true;  //fell off map
		}
      return true;
   }
   
   public static  boolean empty(char pixel)
   {
      switch(pixel)
		{
			case '0'  : return true;
			case 'a'  : return true;
			case 'b'  : return true;
         case '.'  : return true;
			default   : return false;
		}
   }
   
   private boolean isPhasing()
   {
      if ((jumpPower>0) && !empty(Game.getCourse1(y2, x)) || !empty(Game.getCourse1(y2, x2)))  //if moving up and either bottom corner is not air
      {
         phasing = true;
         return true;
      }
      else
      {
         if ((jumpPower<0)&&(empty(Game.getCourse1(y2, x)) && empty(Game.getCourse1(y2, x2))))  //if moving down and either bottom corner is air
         {
            phasing = false;
            return false;
         }
      }
         return true;
   }
   
   private void lineUpX()
   {
      int x = xCoor / Game.getPixelSize();
      xCoor = x * Game.getPixelSize();
   }
   
   private void lineUpY()
   {
      int y = yCoor / Game.getPixelSize();
      yCoor = y * (Game.getPixelSize());
   }
   
   public void moveX(int dist)
   {
      xCoor += dist;
      if (dist < 0)
         direction = 0;
      else
         direction = 1;
         
      int x = xCoor / Game.getPixelSize();
      int x2 = (xCoor+width) / Game.getPixelSize();
      int y = (yCoor-1) / Game.getPixelSize();
      int y2 = (yCoor-height) / Game.getPixelSize();
      
      try
		{
         if (!empty(Game.getCourse1(y, x)) || !empty(Game.getCourse1(y, x2)))// || Game.getCourse1(y2, x) != 0 || Game.getCourse1(y2, x2) != 0)
         xCoor -= dist;
		}
		catch (ArrayIndexOutOfBoundsException e) {  }
   }
   
   public void shoot(Game game)
   {
      game.addBullet(new Bullet(xCoor + width/2, yCoor - height/2, direction, g, this));
   }
   
   public void jump()
   {
      jumping = true;
   }
   
   public static void passGraves(ArrayList grv)
   {
      graves = grv;
   }
   
   public int getLX()
   {
      return xCoor;
   }
   
   public int getRX()
   {
      return xCoor+width;
   }
   
   public int getTY()
   {
      return yCoor-height;
   }
   
   public int getBY()
   {
      return yCoor;
   }
}