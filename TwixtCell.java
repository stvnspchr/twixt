/**
 * TwixtCell.java
 *
 * CSE Fall 2007
 * Game Project
 * @author Steven Speicher
 * @version 1.0
 */
 
//-------------------
// Import Statements
//------------------- 
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TwixtCell extends JLabel
{
//-----------
// Constants
//-----------
   public static final int BLANK = 0;
   public static final int RED = 1;
   public static final int BLUE = 2;
//----------------------
// Private data members
//----------------------
	private JLabel content;
   
   private ImageIcon redImageIcon;      //Red peg image
   private ImageIcon blueImageIcon;     //Blue peg image
   private ImageIcon blankImageIcon;    //Blank peg image
	private int row;                     //Row of cell
   private int col;                     //Column of cell
//-------------
// Constructor
//-------------   
   public TwixtCell(int row, int col)
   {
      this(null);
      this.row = row + 1;
      this.col = col + 1;
   }
   
   public TwixtCell(Point point)
   {
      redImageIcon = new ImageIcon("redPlayerPeg.gif");
      blueImageIcon = new ImageIcon("bluePlayerPeg.gif");
      blankImageIcon = new ImageIcon("blankPeg.gif");
      
      setLayout(new BorderLayout());
      setBackground(Color.white);
      setBorder(BorderFactory.createLineBorder(Color.gray));
    
	 	setBlank();
      //content = new JLabel(blankImageIcon);
      //add(content);
      
//      location = point;
   }
//---------
//Set Blank
//---------
	public void setBlank()
	{
		content = new JLabel(blankImageIcon);
      add(content);
	}
//--------
//Get Row
//--------
   public int getRow()
   {
      return this.row;
   }
//-----------
//Get Column
//-----------   
   public int getCol()
   {
      return this.col;
   }
//-----------
//Set Content
//-----------    
   public void setContent(int image)
   {
      switch (image){
      
      case RED:   content.setIcon(redImageIcon);
                  break;
      case BLUE:  content.setIcon(blueImageIcon);
                  break;
		case BLANK: content.setIcon(blankImageIcon);
						break;
      default:   //nothing
                 break;
      }
   }
//-----------------
//Check Free Space
//-----------------   
   public boolean checkFreeSpace()
   {
     boolean takenSpace = false;
      boolean freeSpace = true;
      if (content.getIcon().equals(redImageIcon)){
         return takenSpace;
      }else if(content.getIcon().equals(blueImageIcon)){
         return takenSpace;
      }else{
         return freeSpace;
      }
   }
//------------
//Check Border
//------------  
   public boolean checkBorder(boolean redPlayerTurn)
   {
      boolean borderTrespass = false;
     
      if(redPlayerTurn){
         if(getRow() == 1 || getRow() == 24){
           JOptionPane.showMessageDialog(null,"Red cannont move here.");
            borderTrespass = true;
         }
      }else{
         if(getCol() == 1 || getCol() == 24){
            JOptionPane.showMessageDialog(null,"Blue cannont move here.");
            borderTrespass = true;
         }
      }
      
      return borderTrespass;
   }
}

