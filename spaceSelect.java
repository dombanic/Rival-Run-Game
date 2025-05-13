//desertSelect

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.awt.Image;
import javax.imageio.ImageIO;
import java.io.File;
import javax.swing.*;
import javax.sound.sampled.*;

class spaceSelect extends Frame implements MouseListener, MouseWheelListener
{
   private Graphics g, gBuffer;   
   private Image virtualMem;
   private Rectangle map1, map2, map3;
   private boolean ready;
   private Image background, greenFlag, redFlag;
   private static char[][] course;
   private int wheel;
   private File[] mapFiles;
   private Rectangle[] rectangles;
   
   Color grey = new Color(120,120,120);
   Color purple = new Color(122,0,150);
   Color brown = new Color(100,60,0);
   
   public spaceSelect()
   {
      super("Space Select");
      ready = false;
      setSize(1900,1000);
      setVisible(true);
      virtualMem = createImage(1900,1000);
      gBuffer = virtualMem.getGraphics();
      addMouseListener(this);
      addMouseWheelListener(this);
      String directoryPath = "mapFiles";
      File directory = new File(directoryPath);
      mapFiles = directory.listFiles();
      rectangles = new Rectangle[mapFiles.length];
      for (int i = 0;i < rectangles.length;i++)
         rectangles[i] = new Rectangle(63+i*688,303,638,340);
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
	      gBuffer.fillRect(0,0,1900,1000);
         for (int k = 0; k < mapFiles.length; k++)
         {
            gBuffer.setColor(new Color(50,50,50));
            Grfx.fillRoundedRectangle(gBuffer, 53 + k*688 + wheel, 293, 711 + k*688 +wheel, 653,10);
            drawMaps(gBuffer,63 + k*688 +wheel,303,638,340,k);
         }
         g.drawImage (virtualMem,0,0,this);
         repaint();
      }
   }
   
   private void drawMaps(Graphics g, int x, int y, int width, int height, int k)
   {
      gBuffer.drawImage(background,x,y,width,height,this);
            
      char[][] course;
      
      try
		{
			BufferedReader inStream = new BufferedReader(new FileReader(mapFiles[k]));
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
         course = new char[0][0];
		}
      
      int pixelSize = height/course.length;
            
      for (int y1 = 0; y1 < course.length;y1++)
      {
         for (int x1 = 0; x1 < course[y1].length;x1++)
         {
            if (colorPicker(Integer.valueOf(course[y1][x1])-48) != null)
            {
               if (Integer.valueOf(course[y1][x1]) < 97)
               {
                  g.setColor(colorPicker(Integer.valueOf(course[y1][x1])-48));
                  g.fillRect(x1*pixelSize+x,y1*pixelSize+y,pixelSize,pixelSize);
               }
            }
            else
            {
               if (course[y1][x1] == 'a')
                  g.drawImage(greenFlag,x1*pixelSize + x,y1*pixelSize+y,pixelSize,pixelSize,this);
               else if (course[y1][x1] == 'b')
                  g.drawImage(redFlag,x1*pixelSize + x,y1*pixelSize+y,pixelSize,pixelSize,this);
            } 
         }
      }
   }
     
   private void initializeTextures()
	{
      try
      {
         background = ImageIO.read(new File("textures/space.png"));
         redFlag = ImageIO.read(new File("textures/redFlag.png"));
         greenFlag = ImageIO.read(new File("textures/greenFlag.png"));
      }
      catch (IOException e)
      {
         System.out.println("textures: " + e);
      }
   }
   
   public void mousePressed(MouseEvent e)
   {
		int x = e.getX() - 10-wheel;  
		int y = e.getY() - 45;
            
      for (int i = 0;i < rectangles.length;i++)
      {
         if (rectangles[i].contains(x,y))
         {
            this.setVisible(false);
            Game game = new Game(new File("mapFiles/" + mapFiles[i].getName()),2);
      		game.addWindowListener(new WindowAdapter()
      		{public void windowClosing(WindowEvent e)
      		{System.exit(0);}});
         }
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
   
   
   public void mouseClicked(MouseEvent e)  { }
   public void mouseEntered(MouseEvent e)    { }
   public void mouseExited(MouseEvent e)    { }
   public void mouseReleased(MouseEvent e) { }
   
   public void mouseWheelMoved(MouseWheelEvent e)
	{
		wheel += e.getWheelRotation() * 20;
      if (wheel > 0)
      {
         wheel -= e.getWheelRotation() * 20;
      }
      else if (wheel < mapFiles.length*-711 + 1910)
      {
         wheel -= e.getWheelRotation() * 20;
      }
		repaint();
	}
   
   public void update(Graphics g)
   {
      paint(g);
   }
}