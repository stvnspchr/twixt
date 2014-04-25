/**
 * TwixtManual.java
 *
 * CSE Fall 2007
 * Game Project
 * @author Steven Speicher
 * @version 1.0
 */
 
//-------------------
// Import Statements
//-------------------  
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TwixtManual extends JFrame implements ActionListener
{
//----------------------
// Private data members
//----------------------
	private JButton okButton;
	private JTextArea gameInfoText;
	private JLabel headingLabel,invalidMoveLabel,invalidMoveLabel2,invalidMoveText;
	private ImageIcon invalidMoveImageIcon,invalidMoveImageIcon2;
//-------------
// Constructor
//------------- 
	public TwixtManual()
	{
		setTitle("Twixt - Game Manual");
      setSize(600,600);
      setVisible(true);
      setLocation(100,100);
		setBackground(Color.white);
      
      Container contentPane;
      contentPane = getContentPane();
      contentPane.setLayout(new BorderLayout());
		
		headingLabel = new JLabel("Invalid Moves");
		headingLabel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		invalidMoveLabel = new JLabel();
		invalidMoveLabel.setLayout(new BorderLayout());
		invalidMoveImageIcon = new ImageIcon("invalidMove.gif");
		JLabel imageLabel = new JLabel(invalidMoveImageIcon);
		invalidMoveLabel.add(imageLabel,BorderLayout.CENTER);
		
		invalidMoveLabel2 = new JLabel();
		invalidMoveLabel2.setLayout(new FlowLayout());
		invalidMoveImageIcon2 = new ImageIcon("invalidMove2.gif");
		JLabel imageLabel2 = new JLabel(invalidMoveImageIcon2);
		invalidMoveLabel2.add(imageLabel2);
		
		okButton = new JButton("OK");
		okButton.addActionListener(this);
		
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.add(okButton);
		
		JPanel gameInfo = new JPanel(new GridLayout(2,2));
		gameInfo.add(invalidMoveLabel);
		gameInfo.add(invalidMoveLabel2);

		contentPane.add(headingLabel,BorderLayout.NORTH);
		contentPane.add(gameInfo,BorderLayout.CENTER);
		contentPane.add(buttonPanel,BorderLayout.SOUTH);
	
	}
//----------------
//Action Performed
//----------------	
	public void actionPerformed(ActionEvent event)
   {
      if(event.getSource() instanceof JButton){
         JButton clickedButton = (JButton) event.getSource();
         String buttonText = clickedButton.getText();
			 
			if(buttonText.equals("OK")){
			 	this.dispose();
			}
		}	 
	}
} 