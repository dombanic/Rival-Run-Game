import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.io.*;
import java.awt.Image;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.image.ImageObserver;

public class  Bullet
{
   protected Graphics g;
   protected int x, oldX, y, direction;
   protected int speed = 10;
   protected int width = Game.getPixelSize()/4;
   protected int height = Game.getPixelSize()/8;
   protected boolean collided;
   protected Player owner;
   
   public Bullet(int x, int y, int direction, Graphics g, Player owner)
   {
      this.x = x;
      this.y = y;
      this.direction = direction;
      this.g = g;
      this.owner = owner;
   }
   
   public void exist()
   {
      if (!collided)
      {
         drawBullet();
         move();
      }
      colliding();
   }
   
   public void drawBullet()
   {
      g.setColor(Color.gray);
      g.fillRect(oldX,y,width,height);
      g.setColor(new Color(153,101,21));
      g.fillRect(x,y,width,height);
   }
   
   private void move()
   {
      oldX = x;
      if (direction == 1)
         x += speed;
      else
         x -= speed;
   }
   
   private boolean colliding()
   {
      if (direction == 1)
      {
         if (!Player.empty(Game.getCourse1(y/Game.getPixelSize(), (x + speed)/Game.getPixelSize())) || (Game.getCourse1(y/Game.getPixelSize(), (x + speed)/Game.getPixelSize()) == '.'))
            collided = true;
      }
      else
      {
         if (!Player.empty(Game.getCourse1(y/Game.getPixelSize(), (x - speed)/Game.getPixelSize())) || (Game.getCourse1(y/Game.getPixelSize(), (x - speed)/Game.getPixelSize()) == '.'))
            collided = true;
      }
      return collided;
   }
   
   public boolean isCollided()
   {
      return collided;
   }
   
   public boolean hitPlayer(Player player)
   {
      int prx = player.getRX();
      int plx = player.getLX();
      int pty = player.getTY();
      int pby = player.getBY();
      int y2 = y+height;
      
      boolean mainX = ((x>=plx)&&(x<=prx));
      boolean olderX = ((oldX>=plx)&&(oldX<=prx));
      boolean topY = ((y<=pby)&&(y>=pty));
      boolean bottomY = ((y2<=pby)&&(y2>=pty));
      
      if ((mainX || olderX)&&(topY || bottomY))
         collided = true;
         
      return ((mainX || olderX)&&(topY || bottomY));
   }
   
   public Player getParent()
   {
      return owner;
   }
}