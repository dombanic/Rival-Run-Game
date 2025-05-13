import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.io.*;
import java.awt.Image;
import javax.imageio.ImageIO;
import java.io.File;
import javax.swing.*;

class Mapper
{
	public static void main(String args[])
	{
		MapMaker mapMaker = new MapMaker();
		mapMaker.addWindowListener(new WindowAdapter()
		{public void windowClosing(WindowEvent e)
		{System.exit(0);}});
	}
}


public class MapMaker extends Frame implements KeyListener, FocusListener, MouseListener, MouseMotionListener
{
	private int frameWidth;		   
	private int frameHeight;
	private Graphics g, gBuffer;	
   private Image virtualMem;    
   private boolean ready;
   private Image file, save, arrow, sideArrow, num0, num1, num2, num3, num4, num5, num6, num7, num8, num9, Xout, redFlag, greenFlag; 
   
   //File Chooser  
   private Rectangle[][] files; //row -> column
   private File[] mapFiles;
   private boolean inFileChooser;
   private Rectangle[] buttons = new Rectangle[4];
   private File currentFile;
   
   //Menu
   int menuBoxSize = 75;  //size of the menu boxes
   private int menuX = 25;
   private int menuY = 25;
   
   //Canvas
   int CWidth = 16;   //canvas defalt width
   int CHeight = 16;  //canvas default height
   private char[][] canvas = new char[CHeight][CWidth];
   private int canvasX = 200;
   private int canvasY = 25;
   int canvasWidth = 1675;
   int canvasHeight = 850;
   int canvasBoxSize;
   private boolean start, end;
   
   //brush settings
   private int currentColor = 9;
   private int brushSize = 0;
   
   //Colors
   Color purple = new Color(122,0,150);
   Color brown = new Color(100,60,0);
   Color lightGrey = new Color(200,200,200);
   Color grey = new Color(120,120,120);
   
   //Fonts
   Font buttonFont = new Font("Times New Roman", Font.PLAIN, 50);
   Font filesFont = new Font("Times New Roman", Font.PLAIN, 18);

	public MapMaker()
   {
      super("Map Maker");
      ready = false;
      setSize(1920,1160);
		setVisible(true);
  		frameWidth = 1900;
		frameHeight = 1000;
		virtualMem = createImage(frameWidth,frameHeight);
		gBuffer = virtualMem.getGraphics();
	   addKeyListener(this);
		addFocusListener(this);
      addMouseListener(this);
      addMouseMotionListener(this);
      
      initializeTextures();
      
      for (int y = 0;y<canvas.length;y++)
      {
         for (int x = 0;x<canvas[0].length;x++)
         {
            canvas[y][x] = '0';
         }
      }
      
      ready = true;
      repaint();
	}

	public void paint(Graphics g)
	{
		if (ready)
      {
         this.g = g;  
         Graphics2D g2D = (Graphics2D) g;
   		g2D.translate(10,50);
         
         if (!inFileChooser)
         {
            drawMenu();
            drawCanvas();
         }
         else
         {
            drawFileChooser();
         }
         
         g.drawImage (virtualMem,0,0,this);
         Grfx.delay(15);
   		repaint();  
         gBuffer.setColor(Color.white);   
         gBuffer.fillRect(0,0,1900,950); //clears canvas
      }
	}
   
   public void initializeTextures()
	{
      try
      {
   	   save = ImageIO.read(new File("textures/save.png"));
         file = ImageIO.read(new File("textures/file.png"));
         arrow = ImageIO.read(new File("textures/arrow.png"));
         sideArrow = ImageIO.read(new File("textures/sideArrow.png"));         
   	   num0 = ImageIO.read(new File("textures/num0.png"));
         num1 = ImageIO.read(new File("textures/num1.png"));
         num2 = ImageIO.read(new File("textures/num2.png"));
   	   num3 = ImageIO.read(new File("textures/num3.png"));
         num4 = ImageIO.read(new File("textures/num4.png"));
         num5 = ImageIO.read(new File("textures/num5.png"));
   	   num6 = ImageIO.read(new File("textures/num6.png"));
         num7 = ImageIO.read(new File("textures/num7.png"));
         num8 = ImageIO.read(new File("textures/num8.png"));
         num9 = ImageIO.read(new File("textures/num9.png"));
         Xout = ImageIO.read(new File("textures/Xout.png"));
         redFlag = ImageIO.read(new File("textures/redFlag.png"));
         greenFlag = ImageIO.read(new File("textures/greenFlag.png"));
      }
      catch (IOException e)
      {
         System.out.println("textures: " + e);
      }
	}
   
   private void drawMenu()
   {
      gBuffer.setColor(Color.red);
      gBuffer.fillRect(menuX,menuY,menuBoxSize,menuBoxSize);
      gBuffer.setColor(Color.orange);
      gBuffer.fillRect(menuX,menuY+menuBoxSize,menuBoxSize,menuBoxSize);
      gBuffer.setColor(Color.yellow);
      gBuffer.fillRect(menuX,menuY+menuBoxSize*2,menuBoxSize,menuBoxSize);
      gBuffer.setColor(Color.green);
      gBuffer.fillRect(menuX,menuY+menuBoxSize*3,menuBoxSize,menuBoxSize);
      gBuffer.setColor(Color.blue);
      gBuffer.fillRect(menuX,menuY+menuBoxSize*4,menuBoxSize,menuBoxSize);
      gBuffer.setColor(purple);
      gBuffer.fillRect(menuX,menuY+menuBoxSize*5,menuBoxSize,menuBoxSize);
      gBuffer.setColor(Color.pink);
      gBuffer.fillRect(menuX,menuY+menuBoxSize*6,menuBoxSize,menuBoxSize);
      gBuffer.setColor(brown);
      gBuffer.fillRect(menuX,menuY+menuBoxSize*7,menuBoxSize,menuBoxSize);
      gBuffer.setColor(grey);
      gBuffer.fillRect(menuX,menuY+menuBoxSize*8,menuBoxSize,menuBoxSize);
      gBuffer.setColor(Color.white);
      gBuffer.fillRect(menuX,menuY+menuBoxSize*9,menuBoxSize,menuBoxSize);
      
      //icons
      gBuffer.fillRect(menuX+menuBoxSize,menuY,menuBoxSize,menuBoxSize);      
      gBuffer.drawImage(sideArrow,menuX+menuBoxSize,menuY+menuBoxSize*0,menuBoxSize,menuBoxSize,this);
      gBuffer.drawImage(numChooser(),menuX+menuBoxSize,menuY+menuBoxSize*1,menuBoxSize,menuBoxSize,this);
      gBuffer.drawImage(arrow,menuX+menuBoxSize,menuY+menuBoxSize*2,menuBoxSize,menuBoxSize,this);
      gBuffer.drawImage(arrow,menuX+menuBoxSize,menuY+menuBoxSize*4,menuBoxSize,-menuBoxSize,this);
      gBuffer.drawImage(greenFlag,menuX+menuBoxSize,menuY+menuBoxSize*5,menuBoxSize,menuBoxSize,this);
      gBuffer.drawImage(redFlag,menuX+menuBoxSize,menuY+menuBoxSize*6,menuBoxSize,menuBoxSize,this);
      gBuffer.drawImage(file,menuX+menuBoxSize,menuY+menuBoxSize*7,menuBoxSize,menuBoxSize,this);
      gBuffer.drawImage(save,menuX+menuBoxSize,menuY+menuBoxSize*8,menuBoxSize,menuBoxSize,this);
      gBuffer.drawImage(Xout,menuX+menuBoxSize,menuY+menuBoxSize*9,menuBoxSize,menuBoxSize,this);

      gBuffer.setColor(Color.black);
      for (int y = 0;y<10;y++)
      {
         for (int x = 0;x<2;x++)
         {
            gBuffer.drawRect(x*menuBoxSize+menuX,y*menuBoxSize+menuY,menuBoxSize,menuBoxSize);
         }
      }
   }
   
   private void drawCanvas()
   {      
      if (canvasWidth / canvas[0].length >= canvasHeight / canvas.length)
         canvasBoxSize = canvasHeight / canvas.length;
      else
         canvasBoxSize = canvasWidth / canvas[0].length;
      
      for (int y = 0;y<canvas.length;y++)
      {
         for (int x = 0;x<canvas[0].length;x++)
         {
            if (Integer.valueOf(canvas[y][x]) < 97)  //97 is integer value of a
            {
               if (Integer.valueOf(canvas[y][x]) >= 48)
                  gBuffer.setColor(colorPicker(Integer.valueOf(canvas[y][x])-48));
               else
                  gBuffer.setColor(colorPicker(Integer.valueOf(canvas[y][x])));
               gBuffer.fillRect(x*canvasBoxSize+canvasX,y*canvasBoxSize+canvasY,canvasBoxSize,canvasBoxSize);
            }
            else
            {
               if (canvas[y][x] == 'a')
                  gBuffer.drawImage(greenFlag,x*canvasBoxSize+canvasX,y*canvasBoxSize+canvasY,canvasBoxSize,canvasBoxSize,this);
               else if (canvas[y][x] == 'b')
                  gBuffer.drawImage(redFlag,x*canvasBoxSize+canvasX,y*canvasBoxSize+canvasY,canvasBoxSize,canvasBoxSize,this);
            }
            gBuffer.setColor(Color.black);
            gBuffer.drawRect(x*canvasBoxSize+canvasX,y*canvasBoxSize+canvasY,canvasBoxSize,canvasBoxSize); //box borders
         }
      }
   }
   
   private void drawFileChooser()
   {
      files = new Rectangle[mapFiles.length][1];
      
      gBuffer.setColor(grey);
      gBuffer.fillRect(75,75,1750,800);
      gBuffer.setColor(lightGrey);
      gBuffer.fillRect(100,100,1700,750);
      
      gBuffer.setColor(grey);
      gBuffer.fillRect(125,125,1400,700);
      gBuffer.setColor(Color.white);
      gBuffer.fillRect(150,150,1350,650);
      gBuffer.setColor(grey);
      
      gBuffer.setFont(filesFont);
      for (int y = 0;y<mapFiles.length;y++)
      {
         for (int x = 0;x<1;x++)
         {
            gBuffer.drawRect(175,25*y+175,1300,25);
            gBuffer.drawString(mapFiles[y].getName(),180,25*y+193);
            files[y][x] = new Rectangle(175,25*y+175,1300,25);
         }
      }
      for (int x = 0;x<buttons.length*2;x+=2)
      {
         gBuffer.drawRect(1550,75*x+175,225,75);
         //gBuffer.drawString(mapFiles[y].getName(),180,25*y+193);
         buttons[x/2] = new Rectangle(1550,75*x+175,225,75);
      }
      
      gBuffer.setFont(buttonFont);
      gBuffer.drawString("Load",1600,225);
      gBuffer.drawString("Save",1600,225+150);
      gBuffer.drawString("New File",1575,225+300);
      gBuffer.drawString("Back",1600,225+450);
      
      inFileChooser = true;
   }
   
   private Image numChooser()
   {
      switch(brushSize)
		{
			case 0  : return num0;
			case 1  : return num1;
			case 2  : return num2;
			case 3  : return num3;
         case 4  : return num4;
			case 5  : return num5;
			case 6  : return num6;
         case 7  : return num7;
			case 8  : return num8;
			case 9  : return num9;
			default : return num1;
		}
   }
   
   private Color colorPicker(int color)
   {
      switch(color)
		{
			case 0  : return Color.white;
			case 1  : return Color.red;
			case 2  : return Color.orange;
			case 3  : return Color.yellow;
         case 4  : return Color.green;
			case 5  : return Color.blue;
			case 6  : return purple;
         case 7  : return Color.pink;
			case 8  : return brown;
			case 9  : return grey;
			default : return new Color(157,132,174);
		}
   }
   
   public void updateFile(File file)
   {
      try 
      {
         FileWriter myWriter = new FileWriter(file);
         myWriter.write(CWidth+"\n");
         myWriter.write(CHeight+"\n");
         for (int row = 0; row < CHeight;row++)
         {
            for (int column = 0; column < CWidth;column++)
            {
               if (Integer.valueOf(canvas[row][column]) < 97)  //97 is integer value of a
               {
                  myWriter.write(canvas[row][column]);
               }
               else
               {
                  if (canvas[row][column] == 'a')
                     myWriter.write(canvas[row][column]);
                  else if (canvas[row][column] == 'b')
                     myWriter.write(canvas[row][column]);
               }
            }
            myWriter.write("\n");
         }
         
         myWriter.close();
      } 
      catch (IOException e) 
      {
         System.out.println("There were problems with updating the map file as stated below\n");
			System.out.println(e.getMessage());
      }
   }
   
   private void createFile()
   {
      try 
      {
      File myObj = new File("filename.txt");
      if (myObj.createNewFile()) {
        System.out.println("File created: " + myObj.getName());
        currentFile = myObj;
        updateFile(currentFile);
      }
      else 
      {
        System.out.println("File already exists.");
      }
      }
      catch (IOException e) 
      {
         System.out.println("An error occurred.");
         e.printStackTrace();
      }
   }
   
   public void getFiles()
   {
      // Path of the specific directory 
      String directoryPath = "mapFiles";
      
      // Using File class create an object for specific directory
      File directory = new File(directoryPath);
      
      // Using listFiles method we get all the files of a directory 
      // return type of listFiles is array
      mapFiles = directory.listFiles();
      
      // Print name of the all files present in that path
      if (mapFiles != null) 
      {
        for (File file : mapFiles) 
        {
          System.out.println(file.getName());
        }
      }
   }
   
   public void readFile(String file)
   {
      try
		{
			BufferedReader inStream = new BufferedReader(new FileReader(file));
			String line;
         CWidth = Integer.parseInt(inStream.readLine());
         CHeight = Integer.parseInt(inStream.readLine());
         canvas = new char[CHeight][CWidth];

			for (int row = 0; row < CHeight;row++)
         {
            line = inStream.readLine();
            for (int column = 0; column < CWidth;column++)
            {
               try
               {
                  canvas[row][column] = (char) line.charAt(column);
               }
               catch(Exception e)
               {
                  
               }
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
   
   private void clearCanvas()
   {
      canvas = new char[CHeight][CWidth];
      for (int y = 0;y<canvas.length;y++)
      {
         for (int x = 0;x<canvas[0].length;x++)
         {
            canvas[y][x] = '0';
         }
      }
   }

	public void update(Graphics g)
	{
		paint(g);
	}
   
	public void mousePressed(MouseEvent e)	 
   {
      int mouseX = e.getX() - 10;
      int mouseY = e.getY() - 50;
      
      if (!inFileChooser)
         draw(mouseX,mouseY);
      
      if ((mouseX > menuX && mouseX < (menuX + menuBoxSize * 2)) && (mouseY > menuY && mouseY < (menuY + menuBoxSize * 10)))  //checks if click is on menu
      {
         int XIndex = (mouseX - menuX) / menuBoxSize;
         int YIndex = (mouseY - menuY) / menuBoxSize;
         
         if (XIndex == 0)
         {
            currentColor = (YIndex + 1) % 10;
         }
         
         else
         {
            switch(YIndex)
      		{
      			case 0  : this.setVisible(false);
                         RivalRun game = new RivalRun();
                         game.addWindowListener(new WindowAdapter()
                         {public void windowClosing(WindowEvent e)
                         {System.exit(0);}});
                         break;
      			case 1  : break;
      			case 2  : if (brushSize < 9)
                           brushSize++;
                           break;
      			case 3  : if (brushSize < 9)
                           brushSize--;
                           break;
               case 4  : break;
      			case 5  : currentColor = 10;break;
      			case 6  : currentColor = 11;break;
               case 7  : getFiles();
                         drawFileChooser();
                         break;
      			case 8  : if (currentFile != null)
                           updateFile(currentFile);
                           break;
      			case 9  : clearCanvas();break;
      			default : break;
      		}
         }
      }
      else if(inFileChooser)
      {
         for (int index = 0;index<files.length;index++)
         {
            if (files[index][0].contains(mouseX,mouseY)) //sees which file is clicked
            {
               currentFile = mapFiles[index];
               System.out.println(currentFile);
            }
         }
         
         if (buttons[0].contains(mouseX,mouseY))  //checks which control buttons is clicked
         {
            readFile(""+currentFile);
            inFileChooser = false;
         }
         else if (buttons[1].contains(mouseX,mouseY))  //checks which control buttons is clicked
         {
            if (currentFile != null)
               updateFile(currentFile);
         }
         else if (buttons[2].contains(mouseX,mouseY))  //checks which control buttons is clicked
         {
            DialogBox dialogBox = new DialogBox();
		      dialogBox.dispose();
		      dialogBox.setSize(500,300);
		      dialogBox.setVisible(true);
         }
         else if (buttons[3].contains(mouseX,mouseY))  //checks which control buttons is clicked
         {
            inFileChooser = false;
         }
      }
   }
   
   public void mouseDragged(MouseEvent e)
   {
      int mouseX = e.getX() - 10;
      int mouseY = e.getY() - 50;
      
      draw(mouseX,mouseY);
   }
   
   private void draw(int mouseX, int mouseY)
   {
      if ((mouseX > canvasX && mouseX < (canvasX + canvasBoxSize * canvas[0].length)) && (mouseY > canvasY && mouseY < (canvasY + canvasBoxSize * canvas.length)))  //checks if click is on canvas
      {
         int XIndex = (mouseX - canvasX) / canvasBoxSize;
         int YIndex = (mouseY - canvasY) / canvasBoxSize;
         
         for (int y = YIndex - brushSize; y <= YIndex + brushSize;y++)
         {
            for (int x = XIndex - brushSize; x <= XIndex + brushSize;x++)
            {
               if ((x>=0 && x<CWidth) && ( y>=0 && y<CHeight))
               {
                  if (currentColor < 10)
                  {
                     if (currentColor == 0)
                        canvas[y][x] = '0';
                     else
                        canvas[y][x] = (char) ('0'+currentColor);
                  }
                  else
                  {
                     if ((currentColor == 10) && !start)
                     {
                        canvas[y][x] = 'a';
                        start = true;
                     }
                     else if ((currentColor == 11) && !end)
                     {
                        canvas[y][x] = 'b';
                        end = true;
                     }
                  }
               }
            }
         }
      }
   }

	public void keyPressed(KeyEvent e)      
   {
      int key = e.getKeyCode();

		if (key == KeyEvent.VK_LEFT && CWidth > 1)
			CWidth--;

		if (key == KeyEvent.VK_RIGHT)
         CWidth++;
         
      if (key == KeyEvent.VK_UP && CHeight > 1)
			CHeight++;
         
		if (key == KeyEvent.VK_DOWN)
   		CHeight--;   
         
		if (key == 'Q')
   		start = false;   
         
		if (key == 'W')
   		end = false;       
         
		//if (key == KeyEvent.VK_ESCAPE)
			//gameOver = true;
      
      char[][] tempCanvas = new char[CHeight][CWidth];
      
      for (int y = 0;y<tempCanvas.length;y++)
      {
         for (int x = 0;x<tempCanvas[0].length;x++)
         {
            tempCanvas[y][x] = '0';
         }
      }
      
      boolean newIsLarger = false;
      
      if (canvas[0].length < tempCanvas[0].length)  //checks if new updated size can accomodate previous drawing
         newIsLarger = true;
      if (canvas[0].length > tempCanvas[0].length)
         newIsLarger = false;
      if (canvas.length < tempCanvas.length)
         newIsLarger = true;
      if (canvas.length > tempCanvas.length)
         newIsLarger = false;
      
      if (!newIsLarger)
      {
         for (int y = 0; y<CHeight;y++)
         {
            for (int x = 0;x<CWidth;x++)
            {
               tempCanvas[y][x] = canvas[y][x];
            }
         }
      }
      else
      {
         for (int y = 0; y<canvas.length;y++)
         {
            for (int x = 0;x<canvas[0].length;x++)
            {
               tempCanvas[y][x] = canvas[y][x];
            }
         }
      }
      canvas = tempCanvas;
   }   
   
   public void setCurrentFile(File file)
   {
      currentFile = file;
   }

	public void mouseMoved(MouseEvent e)    { }
   public void keyReleased(KeyEvent e)     { }
	public void keyTyped(KeyEvent e)        { }
	public void mouseClicked(MouseEvent e)  { }
	public void mouseEntered(MouseEvent e)	 { }
	public void mouseExited(MouseEvent e)	 { }
	public void mouseReleased(MouseEvent e) { }  
	public void focusGained(FocusEvent e)   { }
	public void focusLost(FocusEvent e) 	 { }
}