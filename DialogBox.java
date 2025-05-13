import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

class DialogBox extends JFrame
{
	private Container contentPane;
	private JToolBar toolBar;
   private JTextField text;
	private JButton button;
   Font buttonFont = new Font("Courier", Font.BOLD, 28);
   //Font buttonFont = new Font("Courier", Font.BOLD, 28);


	public DialogBox()
	{
		super("User Input");
      
      contentPane = getContentPane();
      contentPane.add(new JLabel("Enter New File Name:"));
      JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
      split.setDividerLocation(192);
      text = new JTextField("");
      text.setFont(buttonFont);
      button = new JButton("Create");
      button.setFont(buttonFont);
      button.addMouseListener(new MouseAdapter() { public void mousePressed(MouseEvent e)
		{
	   	String fileName = text.getText();
         try 
         {
            File myObj = new File("mapFiles/"+fileName+".txt");
            if (myObj.createNewFile())
               System.out.println("File created: " + myObj.getName());
            else
               System.out.println("File already exists.");
         } 
         catch (IOException error) 
         {
            System.out.println("An error occurred.");
            error.printStackTrace();
         }
      }});
      split.setTopComponent(text);
      split.setBottomComponent(button);
      contentPane.add(split);
   }
}
