/**
 * Twixt.java
 *
 * CSE Fall 2007
 * Game Project
 * @author Steven Speicher
 * @version 1.0
 */
 
//-------------------
// Import Statements
//-------------------  
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.awt.Graphics;
import java.lang.Integer;
import java.io.*;
import java.util.StringTokenizer;
 
public class Twixt extends JFrame implements ActionListener, MouseListener
{
//-----------
// Constants
//-----------
   private static final int FRAME_WIDTH    = 950;
   private static final int FRAME_HEIGHT   = 700;
   private static final int FRAME_X_ORIGIN = 50;
   private static final int FRAME_Y_ORIGIN = 50;
//----------------------
// Private data members
//----------------------
   //GUI
   private JMenu gameMenu;
   private JMenu editMenu;
   private JMenu helpMenu;
   private JPanel fullBoardPanel;
   private JPanel boardPanel;
   private JPanel gamePanel;
   private JPanel scorePanel;
   private JPanel textPanel;
   private JLabel redNameTotalPegsLabel,redTotalPegsLabel;
   private JLabel blueNameTotalPegsLabel,blueTotalPegsLabel;
   private JLabel redNameTotalConnectorsLabel,redTotalConnectorsLabel;
   private JLabel blueNameTotalConnectorsLabel,blueTotalConnectorsLabel;
   private JTextArea gameInfoText;
   private JScrollPane scrollBar;
   //Data members
   private int currentRow;          //Current row clicked on
   private int currentCol;          //Current column clicked on
   private int redTotalPegs;        //Red's total pegs placed
   private int blueTotalPegs;       //Blue's total pegs placed
   private int redTotalConnectors;  //Red's total connectors placed
   private int blueTotalConnectors; //Blue's total connectors placed
   private String redPlayerName;    //Red players name
   private String bluePlayerName;   //Blue players name
   private String[][] spaces;       //Array of board spaces
   private boolean redPlayerTurn;   //Alternates for whose turn it is
	private String fileName;
   private String[][] connectors = new String[577][577];
	private String[][] visitedConnectors;
   //Objects
   private TwixtCell[][] cell;      //Array of the boards cells
   private TwixtManual manual;
//------
// Main
//------   
   public static void main(String [] args)
   {
      Twixt frame = new Twixt();
      frame.setVisible(true);
   }//end main
//-------------
// Constructor
//------------- 
   public Twixt(){
      Container contentPane;
      redTotalPegs = 0;
      blueTotalPegs = 0;
      redTotalConnectors = 0;
      blueTotalConnectors = 0;
      
      setTitle("Twixt");
      setSize(FRAME_WIDTH, FRAME_HEIGHT);
      setResizable(false);
      setLocation(FRAME_X_ORIGIN,FRAME_Y_ORIGIN);
      
      contentPane = getContentPane();
      contentPane.setLayout(new BorderLayout());
      contentPane.setBackground(Color.white);
      
      redPlayerName = JOptionPane.showInputDialog("Enter red player name.");
      bluePlayerName = JOptionPane.showInputDialog("Enter blue player name.");
      
      do{
         if(redPlayerName.equals("")){    
            redPlayerName = JOptionPane.showInputDialog("Enter red player name.");
         }if(bluePlayerName.equals("")){
            bluePlayerName = JOptionPane.showInputDialog("Enter blue player name.");
         }
      }while(redPlayerName.equals("") || bluePlayerName.equals(""));
      
     redPlayerTurn = true;
     
      createBoardPanel();
      createBorderPanels();
      createScorePanel();
      
      gameInfoText = new JTextArea();
      gameInfoText.setTabSize(1);
      gameInfoText.setLineWrap(true);
      gameInfoText.setEditable(false);
      gameInfoText.setText(redPlayerName + " goes first.");
      scrollBar = new JScrollPane(gameInfoText);
      
      textPanel = new JPanel(new GridLayout(1,1));
      textPanel.add(scrollBar);
      
      gamePanel = new JPanel(new BorderLayout());
      gamePanel.add(scorePanel, BorderLayout.NORTH);
      gamePanel.add(textPanel, BorderLayout.CENTER);
      
      contentPane.add(fullBoardPanel, BorderLayout.CENTER);
      contentPane.add(gamePanel, BorderLayout.EAST);

      createGameMenu();
      createHelpMenu();

      JMenuBar menuBar= new JMenuBar();
      setJMenuBar(menuBar);
      menuBar.add(gameMenu);
      menuBar.add(helpMenu);
      
      setDefaultCloseOperation(EXIT_ON_CLOSE);
   }//end constructor
//-------------
//Create Border
//-------------
   private void createBorderPanels()
   {
      JPanel redLeftPanel = new JPanel();
      redLeftPanel.setBackground(Color.red);
      JPanel redRightPanel = new JPanel();
      redRightPanel.setBackground(Color.red);
      
      JPanel blueTopPanel = new JPanel();
      blueTopPanel.setBackground(Color.blue);
     JPanel blueBottomPanel = new JPanel();
      blueBottomPanel.setBackground(Color.blue);
      
      fullBoardPanel = new JPanel(new BorderLayout());
     fullBoardPanel.add(boardPanel, BorderLayout.CENTER);
      fullBoardPanel.add(redLeftPanel, BorderLayout.EAST);
      fullBoardPanel.add(redRightPanel, BorderLayout.WEST);
      fullBoardPanel.add(blueTopPanel, BorderLayout.NORTH);
      fullBoardPanel.add(blueBottomPanel, BorderLayout.SOUTH);
   }//end createBorderPanels
//-------------
//Create Board
//-------------
   private void createBoardPanel()
   {
      boardPanel = new JPanel(new GridLayout(24,24));
      cell = new TwixtCell[24][24];

      for (int row = 0; row < 24; row++){
         for (int col = 0; col < 24; col++){
         cell[row][col] = new TwixtCell(row,col);
         cell[row][col].addMouseListener(this);
         boardPanel.add(cell[row][col]);
         }
      }
      
      spaces = new String[25][25];
      for (int i = 0; i < 25; i++){
         for (int j = 0; j < 25; j++){
            spaces[i][j] = "";
         }
      }
      
      for (int i = 0; i < 577; i++){
         for (int j = 0; j < 577; j++){
            connectors[i][j] = "";
         }
      }
   }//end createBoardPanel
//------------------
//Create Score Board
//------------------
   private void createScorePanel()
   {
      scorePanel = new JPanel(new GridLayout(4,2,40,2));
      
      redNameTotalPegsLabel = new JLabel(redPlayerName + "'s total pegs: ");
      scorePanel.add(redNameTotalPegsLabel);
      redTotalPegsLabel = new JLabel();
      redTotalPegsLabel.setText(Integer.toString(redTotalPegs));
      scorePanel.add(redTotalPegsLabel);
      
      blueNameTotalPegsLabel = new JLabel(bluePlayerName + "'s total pegs: ");
      scorePanel.add(blueNameTotalPegsLabel);
      blueTotalPegsLabel = new JLabel();
      blueTotalPegsLabel.setText(Integer.toString(blueTotalPegs));
      scorePanel.add(blueTotalPegsLabel);
      
      redNameTotalConnectorsLabel = new JLabel(redPlayerName + "'s total connectors: ");
      scorePanel.add(redNameTotalConnectorsLabel);
      redTotalConnectorsLabel = new JLabel();
      redTotalConnectorsLabel.setText(Integer.toString(redTotalConnectors));
      scorePanel.add(redTotalConnectorsLabel);
      
      blueNameTotalConnectorsLabel = new JLabel(bluePlayerName + "'s total connectors: ");
      scorePanel.add(blueNameTotalConnectorsLabel);
      blueTotalConnectorsLabel = new JLabel();
      blueTotalConnectorsLabel.setText(Integer.toString(blueTotalConnectors));
      scorePanel.add(blueTotalConnectorsLabel);
   }//end createScorePanel
//----------------
//Create Game Menu
//----------------
   private void createGameMenu()
   {
      JMenuItem item;
      gameMenu = new JMenu("Game");

      item = new JMenuItem("New");
      item.addActionListener(this);
      gameMenu.add(item);
      
      gameMenu.addSeparator(); 

      item = new JMenuItem("Open");
      item.addActionListener(this);
      gameMenu.add(item);

      item = new JMenuItem("Save");
      item.addActionListener(this);
      gameMenu.add(item);
      
      gameMenu.addSeparator();
      
      item = new JMenuItem("High Scores");
      item.addActionListener(this);
      gameMenu.add(item);
      
      gameMenu.addSeparator(); 

      item = new JMenuItem("Quit");
      item.addActionListener(this);
      gameMenu.add(item);
   }//end createGameMenu
//----------------
//Create Help Menu
//----------------
   private void createHelpMenu()
   {
     JMenuItem item;
      helpMenu = new JMenu("Help");
      
      item = new JMenuItem("View Help");
      item.addActionListener(this);
      helpMenu.add(item);
      
      item = new JMenuItem("About Twixt");
      item.addActionListener(this);
      helpMenu.add(item);
      
      item = new JMenuItem("Game Manual");
      item.addActionListener(this);
      helpMenu.add(item);
   }//end createHelpMenu   
//------------
//Mouse Click
//------------
   public void mouseClicked(MouseEvent event)
   {
      TwixtCell cell = (TwixtCell) event.getSource();
      
      if(!cell.checkBorder(redPlayerTurn)){
         if (cell.checkFreeSpace()){
            if (redPlayerTurn){
               cell.setContent(TwixtCell.RED);
               spaces[cell.getCol()][cell.getRow()] = "red";
               gameInfoText.setText(gameInfoText.getText() + "\n\t" + redPlayerName + " placed a peg at " + cell.getCol() + ", " + cell.getRow() + ".");
               redTotalPegs++;
               redTotalPegsLabel.setText(Integer.toString(redTotalPegs));
            }else{
               cell.setContent(TwixtCell.BLUE);
               spaces[cell.getCol()][cell.getRow()] = "blue";
               gameInfoText.setText(gameInfoText.getText() + "\n\t" + bluePlayerName + " placed a peg at " + cell.getCol() + ", " + cell.getRow() + ".");
               blueTotalPegs++;
               blueTotalPegsLabel.setText(Integer.toString(blueTotalPegs));
            }
            currentCol = cell.getCol();
            currentRow = cell.getRow();
            placeConnectors();
				
				if (redPlayerTurn && redTotalPegs>12){
					checkRedWinner();
            }else if(!redPlayerTurn && blueTotalPegs>12){
					checkBlueWinner();
            }
            
            if (redPlayerTurn){
               gameInfoText.setText(gameInfoText.getText() + "\n" + "It is " + bluePlayerName + "'s turn.");
            }else{
               gameInfoText.setText(gameInfoText.getText() + "\n" + "It is " + redPlayerName + "'s turn.");
            }
            
            redPlayerTurn = !redPlayerTurn;
         }else{
            JOptionPane.showMessageDialog(null, "Please pick a place not already taken.");
         }
      }
   }//end mouseClick
   public void mouseEntered(MouseEvent event){}
   public void mouseExited(MouseEvent event){}
   public void mousePressed(MouseEvent event){}
   public void mouseReleased(MouseEvent event){}
//----------------
//Action Performed
//----------------
   public void actionPerformed(ActionEvent event)
   {
      String menuName;
      String fileName;

      menuName = event.getActionCommand();

      if(menuName.equals("Quit")){
         int quitGame = JOptionPane.showConfirmDialog(null,"Are you sure you want to quit this game?");
         
         if(quitGame == 0){
            System.exit(0);
        }
      }else if (menuName.equals("New")){
      
         int newGame = JOptionPane.showConfirmDialog(null,"Are you sure you want to start a new game?");
         
         if(newGame == 0){
            redTotalPegs = 0;
            blueTotalPegs = 0;
            redTotalConnectors = 0;
            blueTotalConnectors = 0;
            
            gameInfoText.setText("A new game has been started.");
            
            redPlayerName = JOptionPane.showInputDialog("Enter red player name.");
            bluePlayerName = JOptionPane.showInputDialog("Enter blue player name.");
            
            do{
              if(redPlayerName.equals("")){    
                  redPlayerName = JOptionPane.showInputDialog("Enter red player name.");
               }if(bluePlayerName.equals("")){
                  bluePlayerName = JOptionPane.showInputDialog("Enter blue player name.");
               }
            }while(redPlayerName.equals("") || bluePlayerName.equals(""));
            
            redPlayerTurn = true;
            gameInfoText.setText(redPlayerName + " goes first.");
            
            redNameTotalPegsLabel.setText(redPlayerName + "'s total pegs: ");
            blueNameTotalPegsLabel.setText(bluePlayerName + "'s total pegs: ");
            redNameTotalConnectorsLabel.setText(redPlayerName + "'s total connectors: ");
            blueNameTotalConnectorsLabel.setText(bluePlayerName + "'s total connectors: ");
            
            for (int i = 0; i < 24; i++){
               for (int j = 0; j < 24; j++){
                  spaces[i][j] = "";
               }
            }
            
            for (int i = 0; i < 24; i++){
               for (int j = 0; j < 24; j++){
                  cell[i][j].setContent(TwixtCell.BLANK);
               }
            }
            
            createBoardPanel();
            createBorderPanels();
            createScorePanel();
         }
      }else if (menuName.equals("Open")){
         JFileChooser chooser = new JFileChooser();
         int status = chooser.showOpenDialog(null);
         if (status == JFileChooser.APPROVE_OPTION){
            fileName = chooser.getSelectedFile().getAbsolutePath();
         }
      }else if (menuName.equals("Save")){
         JFileChooser chooser = new JFileChooser();
         int status = chooser.showSaveDialog(null);
         if (status == JFileChooser.APPROVE_OPTION){
            fileName = chooser.getSelectedFile().getAbsolutePath();
         }
      }else if (menuName.equals("High Scores")){
			String currentLine,readFile = "",highScores = "High Scores\n";
			
			JOptionPane.showMessageDialog(null, "Select file to read scores from.");
		   JFileChooser chooser = new JFileChooser();
	      int status = chooser.showSaveDialog(null);
	      if (status == JFileChooser.APPROVE_OPTION){
	         readFile = chooser.getSelectedFile().getAbsolutePath();
	      }
		
			try{
	         FileReader inReader = new FileReader(readFile);
	         BufferedReader in = new BufferedReader(inReader);
	         currentLine = in.readLine();
	         
	         do{
	            StringTokenizer line = new StringTokenizer(currentLine, " ");
	            String whoWins = line.nextToken();
	            String redName = line.nextToken();
					String redPegs = line.nextToken();
					String redConnectors = line.nextToken();
	            String blueName = line.nextToken();
					String bluePegs = line.nextToken();
					String blueConnectors = line.nextToken();
	           	
					highScores = highScores + "\n" + redName + " had " + redPegs + " pegs and " + redConnectors + " connectors and " + blueName + " had " + bluePegs + " pegs and " + blueConnectors + " connectors. " + whoWins + " won that game.";

	            currentLine = in.readLine();
	         }while(currentLine != null);
	         in.close();
	      }catch(IOException e){
	          JOptionPane.showMessageDialog(null, "File not found.");
	      }catch(NullPointerException e){}
			JOptionPane.showMessageDialog(null, highScores);
      }else if (menuName.equals("View Help")){
         JOptionPane.showMessageDialog(null, "Twixt!\n\nTwixt is board game where players take turns placing pegs on a 24X24 grid. The object of the game is \n to connect pegs from opposite sides. Red travels horizontally and blue travels vertically. Only \nthe matching color can play in the first or last row or column. If a player places a peg in any \ncombination of 2 in one direction and 1 in a perpendicular direction (like the rook in chess) then \na connector is placed on the board connecting the two pegs. You cannot place pegs on the same spot, \nand connectors are not allowed to overlap. You are allowed to move anywhere on the board at any time \nas long as it does not go into opponents row or column.");
      }else if (menuName.equals("About Twixt")){
         JOptionPane.showMessageDialog(null, "Twixt!\n\nBy Steven Speicher\n\n2007");
      }else if (menuName.equals("Game Manual")){
         manual = new TwixtManual();
      }
   }//end actionPerformed
//-----------------
//Place Connectors
//-----------------
   public void placeConnectors()
   {
      //----
      //Red
      //----
      if(redPlayerTurn){
         if(spaces[getPositionOneX()][getPositionOneY()].equals("red")){
            if(checkOpponentConnectorsPositionOne()){
               //System.out.println("up 1 right 2");
               gameInfoText.setText(gameInfoText.getText() + "\n\t" + redPlayerName + " added a connector from " + currentCol + ", " + currentRow + " to " + (currentCol+2) + ", " + (currentRow-1) + ".");
               //draw red
               connectors[(24*(currentRow-1)+currentCol)][(24*((currentRow-1)-1)+(currentCol+2))] = "r";
               connectors[(24*((currentRow-1)-1)+(currentCol+2))][(24*(currentRow-1)+currentCol)] = "r";
					System.out.println((24*(currentRow-1)+currentCol) + " " + (24*((currentRow-1)-1)+(currentCol+2)));
               //Increment Connectors
               redTotalConnectors++;
               redTotalConnectorsLabel.setText(Integer.toString(redTotalConnectors));
               //Check Winner
               //checkRedWinner();
            }
         }if(spaces[getPositionTwoX()][getPositionTwoY()].equals("red")){
            if(checkOpponentConnectorsPositionTwo()){
               //System.out.println("up 2 right 1");
               gameInfoText.setText(gameInfoText.getText() + "\n\t" + redPlayerName + " added a connector from " + currentCol + ", " + currentRow + " to " + (currentCol+1) + ", " + (currentRow-2) + ".");
               //draw red
               connectors[(24*(currentRow-1)+currentCol)][(24*((currentRow-2)-1)+(currentCol+1))] = "r";
               connectors[(24*((currentRow-2)-1)+(currentCol+1))][(24*(currentRow-1)+currentCol)] = "r";
					System.out.println((24*(currentRow-1)+currentCol) + " " + (24*((currentRow-2)-1)+(currentCol+1)));
               //Increment Connectors
               redTotalConnectors++;
               redTotalConnectorsLabel.setText(Integer.toString(redTotalConnectors));
               //Check Winner
               //checkRedWinner();
            }
         }if(spaces[getPositionThreeX()][getPositionThreeY()].equals("red")){
            if(checkOpponentConnectorsPositionThree()){
               //System.out.println("up 2 left 1");
               gameInfoText.setText(gameInfoText.getText() + "\n\t" + redPlayerName + " added a connector from " + currentCol + ", " + currentRow + " to " + (currentCol-1) + ", " + (currentRow-2) + ".");
               //draw red
               connectors[(24*(currentRow-1)+currentCol)][(24*((currentRow-2)-1)+(currentCol-1))] = "r";
               connectors[(24*((currentRow-2)-1)+(currentCol-1))][(24*(currentRow-1)+currentCol)] = "r";
					System.out.println((24*(currentRow-1)+currentCol) + " " + (24*((currentRow-2)-1)+(currentCol-1)));
               //Increment Connectors
               redTotalConnectors++;
               redTotalConnectorsLabel.setText(Integer.toString(redTotalConnectors));
               //Check Winner
               //checkRedWinner();
            }
         }if(spaces[getPositionFourX()][getPositionFourY()].equals("red")){
           if(checkOpponentConnectorsPositionFour()){
               //System.out.println("up 1 left 2");
               gameInfoText.setText(gameInfoText.getText() + "\n\t" + redPlayerName + " added a connector from " + currentCol + ", " + currentRow + " to " + (currentCol-2) + ", " + (currentRow-1) + ".");
               //draw red
               connectors[(24*(currentRow-1)+currentCol)][(24*((currentRow-1)-1)+(currentCol-2))] = "r";
               connectors[(24*((currentRow-1)-1)+(currentCol-2))][(24*(currentRow-1)+currentCol)] = "r";
					System.out.println((24*(currentRow-1)+currentCol) + " " + (24*((currentRow-1)-1)+(currentCol-2)));
               //Increment Connectors
               redTotalConnectors++;
               redTotalConnectorsLabel.setText(Integer.toString(redTotalConnectors));
               //Check Winner
               //checkRedWinner();
            }
         }if(spaces[getPositionFiveX()][getPositionFiveY()].equals("red")){
            if(checkOpponentConnectorsPositionFive()){
               //System.out.println("down 1 left 2");
               gameInfoText.setText(gameInfoText.getText() + "\n\t" + redPlayerName + " added a connector from " + currentCol + ", " + currentRow + " to " + (currentCol-2) + ", " + (currentRow+1) + ".");
               //draw red
               connectors[(24*(currentRow-1)+currentCol)][(24*((currentRow+1)-1)+(currentCol-2))] = "r";
               connectors[(24*((currentRow+1)-1)+(currentCol-2))][(24*(currentRow-1)+currentCol)] = "r";
					System.out.println((24*(currentRow-1)+currentCol) + " " + (24*((currentRow+1)-1)+(currentCol-2)));
               //Increment Connectors
               redTotalConnectors++;
               redTotalConnectorsLabel.setText(Integer.toString(redTotalConnectors));
               //Check Winner
               //checkRedWinner();
            }
         }if(spaces[getPositionSixX()][getPositionSixY()].equals("red")){
            if(checkOpponentConnectorsPositionSix()){
               //System.out.println("down 2 left 1");
               gameInfoText.setText(gameInfoText.getText() + "\n\t" + redPlayerName + " added a connector from " + currentCol + ", " + currentRow + " to " + (currentCol-1) + ", " + (currentRow+2) + ".");
               //draw red
               connectors[(24*(currentRow-1)+currentCol)][(24*((currentRow+2)-1)+(currentCol-1))] = "r";
               connectors[(24*((currentRow+2)-1)+(currentCol-1))][(24*(currentRow-1)+currentCol)] = "r";
					System.out.println((24*(currentRow-1)+currentCol) + " " + (24*((currentRow+2)-1)+(currentCol-1)));
               //Increment Connectors
               redTotalConnectors++;
               redTotalConnectorsLabel.setText(Integer.toString(redTotalConnectors));
               //Check Winner
               //checkRedWinner();
            }
         }if(spaces[getPositionSevenX()][getPositionSevenY()].equals("red")){
            if(checkOpponentConnectorsPositionSeven()){
               //System.out.println("down 2 right 1");
               gameInfoText.setText(gameInfoText.getText() + "\n\t" + redPlayerName + " added a connector from " + currentCol + ", " + currentRow + " to " + (currentCol+1) + ", " + (currentRow+2) + ".");
              //draw red
               connectors[(24*(currentRow-1)+currentCol)][(24*((currentRow+2)-1)+(currentCol+1))] = "r";
               connectors[(24*((currentRow+2)-1)+(currentCol+1))][(24*(currentRow-1)+currentCol)] = "r";
					System.out.println((24*(currentRow-1)+currentCol) + " " + (24*((currentRow+2)-1)+(currentCol+1)));
               //Increment Connectors
               redTotalConnectors++;
               redTotalConnectorsLabel.setText(Integer.toString(redTotalConnectors));
               //Check Winner
               //checkRedWinner();
            }
        }if(spaces[getPositionEightX()][getPositionEightY()].equals("red")){
           if(checkOpponentConnectorsPositionEight()){
               //System.out.println("down 1 right 2");
               gameInfoText.setText(gameInfoText.getText() + "\n\t" + redPlayerName + " added a connector from " + currentCol + ", " + currentRow + " to " + (currentCol+2) + ", " + (currentRow+1) + ".");
               //draw red
               connectors[(24*(currentRow-1)+currentCol)][(24*((currentRow+1)-1)+(currentCol+2))] = "r";
               connectors[(24*((currentRow+1)-1)+(currentCol+2))][(24*(currentRow-1)+currentCol)] = "r";
					System.out.println((24*(currentRow-1)+currentCol) + " " + (24*((currentRow+1)-1)+(currentCol+2)));
               //Increment Connectors
               redTotalConnectors++;
               redTotalConnectorsLabel.setText(Integer.toString(redTotalConnectors));
              //Check Winner
               //checkRedWinner();
            }
         }
      //-----
      //Blue
      //-----
      }else{
         if(spaces[getPositionOneX()][getPositionOneY()].equals("blue")){
            if(checkOpponentConnectorsPositionOne()){
               //System.out.println("up 1 right 2");
               gameInfoText.setText(gameInfoText.getText() + "\n\t" + bluePlayerName + " added a connector from " + currentCol + ", " + currentRow + " to " + (currentCol+2) + ", " + (currentRow-1) + ".");
               //draw blue
               connectors[(24*(currentRow-1)+currentCol)][(24*((currentRow-1)-1)+(currentCol+2))] = "b";
               connectors[(24*((currentRow-1)-1)+(currentCol+2))][(24*(currentRow-1)+currentCol)] = "b";
					System.out.println(24*(currentRow-1)+currentCol + " " + (24*((currentRow-1)-1)+(currentCol+2)));
               //Increment Connectors
               blueTotalConnectors++;
               blueTotalConnectorsLabel.setText(Integer.toString(blueTotalConnectors));
               //Check Winner
               //checkBlueWinner();
            }
         }if(spaces[getPositionTwoX()][getPositionTwoY()].equals("blue")){
            if(checkOpponentConnectorsPositionTwo()){
               //System.out.println("up 2 right 1");
               gameInfoText.setText(gameInfoText.getText() + "\n\t" + bluePlayerName + " added a connector from " + currentCol + ", " + currentRow + " to " + (currentCol+1) + ", " + (currentRow-2) + ".");
               //draw blue
               connectors[(24*(currentRow-1)+currentCol)][(24*((currentRow-2)-1)+(currentCol+1))] = "b";
               connectors[(24*((currentRow-2)-1)+(currentCol+1))][(24*(currentRow-1)+currentCol)] = "b";
					System.out.println(24*(currentRow-1)+currentCol + " " + (24*((currentRow-2)-1)+(currentCol+1)));
               //Increment Connectors
               blueTotalConnectors++;
               blueTotalConnectorsLabel.setText(Integer.toString(blueTotalConnectors));
               //Check Winner
               //checkBlueWinner();
            }
         }if(spaces[getPositionThreeX()][getPositionThreeY()].equals("blue")){
            if(checkOpponentConnectorsPositionThree()){
               //System.out.println("up 2 left 1");
               gameInfoText.setText(gameInfoText.getText() + "\n\t" + bluePlayerName + " added a connector from " + currentCol + ", " + currentRow + " to " + (currentCol-1) + ", " + (currentRow-2) + ".");
               //draw blue
               connectors[(24*(currentRow-1)+currentCol)][(24*((currentRow-2)-1)+(currentCol-1))] = "b";
               connectors[(24*((currentRow-2)-1)+(currentCol-1))][(24*(currentRow-1)+currentCol)] = "b";
					System.out.println(24*(currentRow-1)+currentCol + " " + (24*((currentRow-2)-1)+(currentCol-1)));
					//System.out.println(connectors[62][13] + " " + connectors[13][62]);
               //Increment Connectors
               blueTotalConnectors++;
               blueTotalConnectorsLabel.setText(Integer.toString(blueTotalConnectors));
               //Check Winner
               //checkBlueWinner();
            }
         }if(spaces[getPositionFourX()][getPositionFourY()].equals("blue")){
            if(checkOpponentConnectorsPositionFour()){
               //System.out.println("up 1 left 2");
               gameInfoText.setText(gameInfoText.getText() + "\n\t" + bluePlayerName + " added a connector from " + currentCol + ", " + currentRow + " to " + (currentCol-2) + ", " + (currentRow-1) + ".");
               //draw blue
               connectors[(24*(currentRow-1)+currentCol)][(24*((currentRow-1)-1)+(currentCol-2))] = "b";
               connectors[(24*((currentRow-1)-1)+(currentCol-2))][(24*(currentRow-1)+currentCol)] = "b";
					System.out.println(24*(currentRow-1)+currentCol + " " + (24*((currentRow-1)-1)+(currentCol-2)));
               //Increment Connectors
               blueTotalConnectors++;
               blueTotalConnectorsLabel.setText(Integer.toString(blueTotalConnectors));
               //Check Winner
               //checkBlueWinner();
            }
         }if(spaces[getPositionFiveX()][getPositionFiveY()].equals("blue")){
           if(checkOpponentConnectorsPositionFive()){
               //System.out.println("down 1 left 2");
               gameInfoText.setText(gameInfoText.getText() + "\n\t" + bluePlayerName + " added a connector from " + currentCol + ", " + currentRow + " to " + (currentCol-2) + ", " + (currentRow+1) + ".");
               //draw blue
               connectors[(24*(currentRow-1)+currentCol)][(24*((currentRow+1)-1)+(currentCol-2))] = "b";
               connectors[(24*((currentRow+1)-1)+(currentCol-2))][(24*(currentRow-1)+currentCol)] = "b";
					System.out.println(24*(currentRow-1)+currentCol + " " + (24*((currentRow+1)-1)+(currentCol-2)));
               //Increment Connectors
               blueTotalConnectors++;
               blueTotalConnectorsLabel.setText(Integer.toString(blueTotalConnectors));
               //Check Winner
               //checkBlueWinner();
            }
         }if(spaces[getPositionSixX()][getPositionSixY()].equals("blue")){
            if(checkOpponentConnectorsPositionSix()){
               //System.out.println("down 2 left 1");
               gameInfoText.setText(gameInfoText.getText() + "\n\t" + bluePlayerName + " added a connector from " + currentCol + ", " + currentRow + " to " + (currentCol-1) + ", " + (currentRow+2) + ".");
               //draw blue
               connectors[(24*(currentRow-1)+currentCol)][(24*((currentRow+2)-1)+(currentCol-1))] = "b";
               connectors[(24*((currentRow+2)-1)+(currentCol-1))][(24*(currentRow-1)+currentCol)] = "b";
					System.out.println(24*(currentRow-1)+currentCol + " " + (24*((currentRow+2)-1)+(currentCol-1)));
               //Increment Connectors
               blueTotalConnectors++;
               blueTotalConnectorsLabel.setText(Integer.toString(blueTotalConnectors));
               //Check Winner
               //checkBlueWinner();
            }
         }if(spaces[getPositionSevenX()][getPositionSevenY()].equals("blue")){
            if(checkOpponentConnectorsPositionSeven()){
               //System.out.println("down 2 right 1");
               gameInfoText.setText(gameInfoText.getText() + "\n\t" + bluePlayerName + " added a connector from " + currentCol + ", " + currentRow + " to " + (currentCol+1) + ", " + (currentRow+2) + ".");
               //draw blue
               connectors[(24*(currentRow-1)+currentCol)][(24*((currentRow+2)-1)+(currentCol+1))] = "b";
               connectors[(24*((currentRow+2)-1)+(currentCol+1))][(24*(currentRow-1)+currentCol)] = "b";
					System.out.println(24*(currentRow-1)+currentCol + " " + (24*((currentRow+2)-1)+(currentCol+1)));
               //Increment Connectors
               blueTotalConnectors++;
               blueTotalConnectorsLabel.setText(Integer.toString(blueTotalConnectors));
               //Check Winner
               //checkBlueWinner();
            }
         }if(spaces[getPositionEightX()][getPositionEightY()].equals("blue")){
            if(checkOpponentConnectorsPositionEight()){
               //System.out.println("down 1 right 2");
               gameInfoText.setText(gameInfoText.getText() + "\n\t" + bluePlayerName + " added a connector from " + currentCol + ", " + currentRow + " to " + (currentCol+2) + ", " + (currentRow+1) + ".");
               //draw blue
               connectors[(24*(currentRow-1)+currentCol)][(24*((currentRow+1)-1)+(currentCol+2))] = "b";
               connectors[(24*((currentRow+1)-1)+(currentCol+2))][(24*(currentRow-1)+currentCol)] = "b";
					System.out.println(24*(currentRow-1)+currentCol + " " + (24*((currentRow+1)-1)+(currentCol+2)));
               //Increment Connectors
               blueTotalConnectors++;
               blueTotalConnectorsLabel.setText(Integer.toString(blueTotalConnectors));
               //Check Winner
               //checkBlueWinner();
            }
         }
      }  
   }//end placeConnectors
                     //--------------------------------------------------------
                     //                   Check Peg Positions
                     //First method for X coordinate. Second for Y coordinate.
                     //--------------------------------------------------------
//-----------------
//Peg Position One
//-----------------
   public int getPositionOneX()
   {
      if(currentCol == 24 || currentCol == 23 || currentRow == 1){
         return 0;
      }else{
         int checkCol = currentCol + 2; 
         return checkCol;
     }
   }
   public int getPositionOneY()
   {
      if(currentCol == 24 || currentCol == 23 || currentRow == 1){
        return 0;
      }else{
         int checkRow = currentRow - 1;
         return checkRow;
      }
   }
//-----------------
//Peg Position Two
//-----------------
   public int getPositionTwoX()
   {
      if(currentRow == 1 || currentRow == 2 || currentCol == 24){
         return 0;
      }else{
         int checkCol = currentCol + 1; 
         return checkCol;
      }
   }
   public int getPositionTwoY()
   {
      if(currentRow == 1 || currentRow == 2 || currentCol == 24){
         return 0;
      }else{
         int checkRow = currentRow - 2;
         return checkRow;
      }
   }
//------------------
//Peg Position Three
//------------------
   public int getPositionThreeX()
   {
      if(currentRow == 1 || currentRow == 2 || currentCol == 1){
         return 0;
      }else{
         int checkCol = currentCol - 1; 
         return checkCol;  
      }
   }
   public int getPositionThreeY()
   {
      if(currentRow == 1 || currentRow == 2 || currentCol == 1){
      return 0;      
      }else{
         int checkRow = currentRow - 2;
         return checkRow;
      }
   }
//------------------
//Peg Position Four
//------------------
   public int getPositionFourX()
   {
      if(currentCol == 1 || currentCol == 2 || currentRow == 1){
         return 0;
      }else{
         int checkCol = currentCol - 2; 
         return checkCol;
      }
   }
   public int getPositionFourY()
  {
      if(currentCol == 1 || currentCol == 2 || currentRow == 1){
         return 0;
      }else{
        int checkRow = currentRow - 1;
         return checkRow;
      }
   }
//------------------
//Peg Position Five
//------------------
   public int getPositionFiveX()
   {
      if(currentCol == 1 || currentCol == 2 || currentRow == 24){
         return 0;
      }else{
         int checkCol = currentCol - 2;
         return checkCol;
      }
   }
   public int getPositionFiveY()
   {
      if(currentCol == 1 || currentCol == 2 || currentRow == 24){
         return 0;
      }else{
         int checkRow = currentRow + 1;
         return checkRow;
      }
   }
//-----------------
//Peg Position Six
//-----------------
   public int getPositionSixX()
   {
      if(currentRow == 23 || currentRow == 24 || currentCol == 1){
         return 0;
      }else{   
         int checkCol = currentCol - 1;
         return checkCol;
    }
   }
   public int getPositionSixY()
   {
      if(currentRow == 23 || currentRow == 24 || currentCol == 1){
        return 0;
      }else{
         int checkRow = currentRow + 2;
         return checkRow;
      }
   }
//-------------------
//Peg Position Seven
//-------------------
   public int getPositionSevenX()
   {
      if(currentRow == 23 || currentRow == 24 || currentCol == 24){
         return 0;
     }else{
        int checkCol = currentCol + 1; 
         return checkCol;
      }
   }
   public int getPositionSevenY()
   {
      if(currentRow == 23 || currentRow == 24 || currentCol == 24){
         return 0;
      }else{
         int checkRow = currentRow + 2;
         return checkRow;
      }
   }
//------------------
//Peg Position Eight
//------------------
   public int getPositionEightX()
   {
      if(currentCol == 24 || currentCol == 23 || currentRow == 24){
        return 0;
      }else{
         int checkCol = currentCol + 2; 
         return checkCol;  
      }
   }
   public int getPositionEightY()
   {
      if(currentCol == 24 || currentCol == 23 || currentRow == 24){
         return 0;
      }else{
        int checkRow = currentRow + 1;
         return checkRow;
      }
   }//end checkPegPositions
                     //----------------------------------------
                     //       Check Connector Positions
                     //Checks to see if another connector has 
                     //been drawn blocking current connector.
                     //----------------------------------------
//-----------------------
//Connectors Position One
//-----------------------
   public boolean checkOpponentConnectorsPositionOne()
   {
      boolean possible = true;
      
      try{
         if(spaces[currentCol][currentRow-1].equals("red")){   
            if(spaces[currentCol+1][currentRow+1].equals("red")){
               possible = false;
            }else if(spaces[currentCol+2][currentRow].equals("red")){
               possible = false;
            }
         }else if(spaces[currentCol][currentRow-1].equals("blue")){
            if(spaces[currentCol+1][currentRow+1].equals("blue")){
               possible = false;
            }else if(spaces[currentCol+2][currentRow].equals("blue")){
               possible = false;
            }
         }else if(spaces[currentCol+1][currentRow-1].equals("red")){
            if(spaces[currentCol][currentRow+1].equals("red")){
               possible = false;
            }else if(spaces[currentCol+2][currentRow+1].equals("red")){
               possible = false;
            }else if(spaces[currentCol+3][currentRow].equals("red")){
               possible = false;
            }
        }else if(spaces[currentCol+1][currentRow-1].equals("blue")){
            if(spaces[currentCol][currentRow+1].equals("blue")){
               possible = false;
            }else if(spaces[currentCol+2][currentRow+1].equals("blue")){
               possible = false;
            }else if(spaces[currentCol+3][currentRow].equals("blue")){
               possible = false;
            }
         }else if(spaces[currentCol+1][currentRow].equals("red")){
           if(spaces[currentCol-1][currentRow-1].equals("red")){
               possible = false;
            }else if(spaces[currentCol][currentRow-2].equals("red")){
               possible = false;
            }else if(spaces[currentCol+2][currentRow-2].equals("red")){
               possible = false;
            }
         }else if(spaces[currentCol+1][currentRow].equals("blue")){
            if(spaces[currentCol-1][currentRow-1].equals("blue")){
               possible = false;
            }else if(spaces[currentCol][currentRow-2].equals("blue")){
               possible = false;
            }else if(spaces[currentCol+2][currentRow-2].equals("blue")){
               possible = false;
            }
         }else if(spaces[currentCol+2][currentRow].equals("red")){
           if(spaces[currentCol+1][currentRow-2].equals("red")){
               possible = false;
            }
         }else if(spaces[currentCol+2][currentRow].equals("blue")){
            if(spaces[currentCol+1][currentRow-2].equals("blue")){
               possible = false;
            }
         }
      }catch(ArrayIndexOutOfBoundsException e){}
      
      return possible;
   }
//-----------------------
//Connectors Position Two
//-----------------------
   public boolean checkOpponentConnectorsPositionTwo()
   {
      boolean possible = true;
      
      try{
         if(spaces[currentCol][currentRow-1].equals("red")){   
            if(spaces[currentCol+1][currentRow+1].equals("red")){
               possible = false;
            }else if(spaces[currentCol+2][currentRow].equals("red")){
               possible = false;
            }else if(spaces[currentCol+2][currentRow-2].equals("red")){
               possible = false;
            }
         }else if(spaces[currentCol][currentRow-1].equals("blue")){
            if(spaces[currentCol+1][currentRow+1].equals("blue")){
               possible = false;
            }else if(spaces[currentCol+2][currentRow].equals("blue")){
               possible = false;
            }else if(spaces[currentCol+2][currentRow-2].equals("blue")){
               possible = false;
            }
         }else if(spaces[currentCol][currentRow-2].equals("red")){
            if(spaces[currentCol+2][currentRow-1].equals("red")){
               possible = false;
            }else if(spaces[currentCol+1][currentRow].equals("red")){
               possible = false;
            }
         }else if(spaces[currentCol][currentRow-2].equals("blue")){
            if(spaces[currentCol+2][currentRow-1].equals("blue")){
               possible = false;
            }else if(spaces[currentCol+1][currentRow].equals("blue")){
               possible = false;
            }
         }else if(spaces[currentCol+1][currentRow].equals("red")){
            if(spaces[currentCol-1][currentRow-1].equals("red")){
               possible = false;
            }
         }else if(spaces[currentCol+1][currentRow].equals("blue")){
            if(spaces[currentCol-1][currentRow-1].equals("blue")){
               possible = false;
            }
         }else if(spaces[currentCol+1][currentRow-1].equals("red")){
            if(spaces[currentCol][currentRow-3].equals("red")){
              possible = false;
            }else if(spaces[currentCol-1][currentRow-2].equals("red")){
               possible = false;
           }else if(spaces[currentCol-1][currentRow].equals("red")){
               possible = false;
            }
         }else if(spaces[currentCol+1][currentRow-1].equals("blue")){
            if(spaces[currentCol][currentRow-3].equals("blue")){
               possible = false;
            }else if(spaces[currentCol-1][currentRow-2].equals("blue")){
               possible = false;
            }else if(spaces[currentCol-1][currentRow].equals("blue")){
               possible = false;
            }
         }
      }catch(ArrayIndexOutOfBoundsException e){}
      
      return possible;
   }
//-------------------------
//Connectors Position Three
//-------------------------
   public boolean checkOpponentConnectorsPositionThree()
   {
      boolean possible = true;
      
      try{
        if(spaces[currentCol][currentRow-1].equals("red")){   
            if(spaces[currentCol-1][currentRow+1].equals("red")){
               possible = false;
            }else if(spaces[currentCol-2][currentRow].equals("red")){
               possible = false;
            }else if(spaces[currentCol-2][currentRow-2].equals("red")){
              possible = false;
            }
         }else if(spaces[currentCol][currentRow-1].equals("blue")){
            if(spaces[currentCol-1][currentRow+1].equals("blue")){
               possible = false;
            }else if(spaces[currentCol-2][currentRow].equals("blue")){
               possible = false;
            }else if(spaces[currentCol-2][currentRow-2].equals("blue")){
               possible = false;
            }
         }else if(spaces[currentCol][currentRow-2].equals("red")){
            if(spaces[currentCol-2][currentRow-1].equals("red")){
               possible = false;
            }else if(spaces[currentCol-1][currentRow].equals("red")){
               possible = false;
           }
         }else if(spaces[currentCol][currentRow-2].equals("blue")){
            if(spaces[currentCol-2][currentRow-1].equals("blue")){
               possible = false;
            }else if(spaces[currentCol-1][currentRow].equals("blue")){
               possible = false;
            }
         }else if(spaces[currentCol-1][currentRow].equals("red")){
            if(spaces[currentCol+1][currentRow-1].equals("red")){
               possible = false;
            }
         }else if(spaces[currentCol-1][currentRow].equals("blue")){
            if(spaces[currentCol+1][currentRow-1].equals("blue")){
               possible = false;
            }
         }else if(spaces[currentCol-1][currentRow-1].equals("red")){
            if(spaces[currentCol][currentRow-3].equals("red")){
               possible = false;
           }else if(spaces[currentCol+1][currentRow-2].equals("red")){
               possible = false;
            }else if(spaces[currentCol+1][currentRow].equals("red")){
               possible = false;
            }
         }else if(spaces[currentCol-1][currentRow-1].equals("blue")){
            if(spaces[currentCol][currentRow-3].equals("blue")){
               possible = false;
           }else if(spaces[currentCol+1][currentRow-2].equals("blue")){
               possible = false;
            }else if(spaces[currentCol+1][currentRow].equals("blue")){
               possible = false;
            }
         }
      }catch(ArrayIndexOutOfBoundsException e){}
      
      return possible;
   }
//------------------------
//Connectors Position Four
//------------------------
   public boolean checkOpponentConnectorsPositionFour()
   {
      boolean possible = true;
      
      try{
         if(spaces[currentCol][currentRow-1].equals("red")){   
            if(spaces[currentCol-1][currentRow+1].equals("red")){
               possible = false;
            }else if(spaces[currentCol-2][currentRow].equals("red")){
               possible = false;
            }
         }else if(spaces[currentCol][currentRow-1].equals("blue")){
           if(spaces[currentCol-1][currentRow+1].equals("blue")){
               possible = false;
            }else if(spaces[currentCol-2][currentRow].equals("blue")){
               possible = false;
            }
         }else if(spaces[currentCol-1][currentRow-1].equals("red")){
            if(spaces[currentCol][currentRow+1].equals("red")){
             possible = false;
            }else if(spaces[currentCol-2][currentRow+1].equals("red")){
               possible = false;
            }else if(spaces[currentCol-3][currentRow].equals("red")){
               possible = false;
            }
         }else if(spaces[currentCol-1][currentRow-1].equals("blue")){
            if(spaces[currentCol][currentRow+1].equals("blue")){
               possible = false;
            }else if(spaces[currentCol-2][currentRow+1].equals("blue")){
               possible = false;
            }else if(spaces[currentCol-3][currentRow].equals("blue")){
               possible = false;
            }
         }else if(spaces[currentCol-1][currentRow].equals("red")){
            if(spaces[currentCol+1][currentRow-1].equals("red")){
               possible = false;
            }else if(spaces[currentCol][currentRow-2].equals("red")){
               possible = false;
            }else if(spaces[currentCol-2][currentRow-2].equals("red")){
               possible = false;
            }
         }else if(spaces[currentCol-1][currentRow].equals("blue")){
            if(spaces[currentCol+1][currentRow-1].equals("blue")){
               possible = false;
            }else if(spaces[currentCol][currentRow-2].equals("blue")){
               possible = false;
            }else if(spaces[currentCol-2][currentRow-2].equals("blue")){
               possible = false;
           }
         }else if(spaces[currentCol-2][currentRow].equals("red")){
            if(spaces[currentCol-1][currentRow-2].equals("red")){
               possible = false;
            }
         }else if(spaces[currentCol-2][currentRow].equals("blue")){
            if(spaces[currentCol-1][currentRow-2].equals("blue")){
               possible = false;
            }
         }
      }catch(ArrayIndexOutOfBoundsException e){}
      
      return possible;
   }
//-------------------------
//Connectors Position Five 
//-------------------------  
   public boolean checkOpponentConnectorsPositionFive()
   {
      boolean possible = true;
      
     try{
         if(spaces[currentCol][currentRow+1].equals("red")){   
            if(spaces[currentCol-1][currentRow-1].equals("red")){
               possible = false;
            }else if(spaces[currentCol-2][currentRow].equals("red")){
               possible = false;
            }
         }else if(spaces[currentCol][currentRow+1].equals("blue")){
            if(spaces[currentCol-1][currentRow-1].equals("blue")){
               possible = false;
            }else if(spaces[currentCol-2][currentRow].equals("blue")){
               possible = false;
            }
         }else if(spaces[currentCol-1][currentRow+1].equals("red")){
            if(spaces[currentCol][currentRow-1].equals("red")){
               possible = false;
            }else if(spaces[currentCol-2][currentRow-1].equals("red")){
               possible = false;
            }else if(spaces[currentCol-3][currentRow].equals("red")){
               possible = false;
            }
         }else if(spaces[currentCol-1][currentRow+1].equals("blue")){
            if(spaces[currentCol][currentRow-1].equals("blue")){
               possible = false;
            }else if(spaces[currentCol-2][currentRow-1].equals("blue")){
               possible = false;
            }else if(spaces[currentCol-3][currentRow].equals("blue")){
               possible = false;
            }
         }else if(spaces[currentCol-1][currentRow].equals("red")){
            if(spaces[currentCol+1][currentRow+1].equals("red")){
               possible = false;
            }else if(spaces[currentCol][currentRow+2].equals("red")){
               possible = false;
            }else if(spaces[currentCol-2][currentRow+2].equals("red")){
               possible = false;
           }
         }else if(spaces[currentCol-1][currentRow].equals("blue")){
            if(spaces[currentCol+1][currentRow+1].equals("blue")){
               possible = false;
            }else if(spaces[currentCol][currentRow+2].equals("blue")){
               possible = false;
            }else if(spaces[currentCol-2][currentRow+2].equals("blue")){
               possible = false;
            }
         }else if(spaces[currentCol-2][currentRow].equals("red")){
            if(spaces[currentCol-1][currentRow+2].equals("red")){
               possible = false;
            }
         }else if(spaces[currentCol-2][currentRow].equals("blue")){
            if(spaces[currentCol-1][currentRow+2].equals("blue")){
               possible = false;
            }
         }
      }catch(ArrayIndexOutOfBoundsException e){}
   
      return possible;
   }
//------------------------
//Connectors Position Six 
//------------------------  
   public boolean checkOpponentConnectorsPositionSix()
   {
      boolean possible = true;
      
      try{
         if(spaces[currentCol-1][currentRow].equals("red")){   
            if(spaces[currentCol+1][currentRow+1].equals("red")){
               possible = false;
            }else if(spaces[currentCol][currentRow+2].equals("red")){
               possible = false;
            }
         }else if(spaces[currentCol-1][currentRow].equals("blue")){
            if(spaces[currentCol+1][currentRow+1].equals("blue")){
               possible = false;
            }else if(spaces[currentCol][currentRow+2].equals("blue")){
               possible = false;
            }
         }else if(spaces[currentCol-1][currentRow+1].equals("red")){
            if(spaces[currentCol+1][currentRow].equals("red")){
               possible = false;
            }else if(spaces[currentCol+1][currentRow+2].equals("red")){
               possible = false;
            }else if(spaces[currentCol][currentRow+3].equals("red")){
               possible = false;
            }
         }else if(spaces[currentCol-1][currentRow+1].equals("blue")){
            if(spaces[currentCol+1][currentRow].equals("blue")){
               possible = false;
           }else if(spaces[currentCol+1][currentRow+2].equals("blue")){
               possible = false;
            }else if(spaces[currentCol][currentRow+3].equals("blue")){
               possible = false;
            }
         }else if(spaces[currentCol][currentRow+1].equals("red")){
           if(spaces[currentCol-2][currentRow+2].equals("red")){
               possible = false;
            }else if(spaces[currentCol-2][currentRow].equals("red")){
               possible = false;
            }else if(spaces[currentCol-1][currentRow-1].equals("red")){
               possible = false;
            }
         }else if(spaces[currentCol][currentRow+1].equals("blue")){
            if(spaces[currentCol-2][currentRow+2].equals("blue")){
              possible = false;
            }else if(spaces[currentCol-2][currentRow].equals("blue")){
               possible = false;
            }else if(spaces[currentCol-1][currentRow-1].equals("blue")){
               possible = false;
            }
         }else if(spaces[currentCol][currentRow+2].equals("red")){
            if(spaces[currentCol-2][currentRow+1].equals("red")){
               possible = false;
            }
         }else if(spaces[currentCol][currentRow+2].equals("blue")){
            if(spaces[currentCol-2][currentRow+1].equals("blue")){
               possible = false;
            }
         }
      }catch(ArrayIndexOutOfBoundsException e){}
      
      return possible;
   }
//-------------------------
//Connectors Position Seven 
//-------------------------  
   public boolean checkOpponentConnectorsPositionSeven()
   {
      boolean possible = true;
      
      try{
         if(spaces[currentCol+1][currentRow].equals("red")){   
            if(spaces[currentCol-1][currentRow+1].equals("red")){
               possible = false;
            }else if(spaces[currentCol][currentRow+2].equals("red")){
               possible = false;
            }
         }else if(spaces[currentCol+1][currentRow].equals("blue")){
            if(spaces[currentCol-1][currentRow+1].equals("blue")){
               possible = false;
            }else if(spaces[currentCol][currentRow+2].equals("blue")){
              possible = false;
            }
         }else if(spaces[currentCol+1][currentRow+1].equals("red")){
            if(spaces[currentCol-1][currentRow].equals("red")){
               possible = false;
            }else if(spaces[currentCol-1][currentRow+2].equals("red")){
               possible = false;
           }else if(spaces[currentCol][currentRow+3].equals("red")){
               possible = false;
            }
        }else if(spaces[currentCol+1][currentRow+1].equals("blue")){
            if(spaces[currentCol-1][currentRow].equals("blue")){
               possible = false;
            }else if(spaces[currentCol-1][currentRow+2].equals("blue")){
               possible = false;
            }else if(spaces[currentCol][currentRow+3].equals("blue")){
               possible = false;
            }
         }else if(spaces[currentCol][currentRow+1].equals("red")){
            if(spaces[currentCol+2][currentRow+2].equals("red")){
               possible = false;
            }else if(spaces[currentCol+2][currentRow].equals("red")){
               possible = false;
            }else if(spaces[currentCol+1][currentRow-1].equals("red")){
               possible = false;
            }
        }else if(spaces[currentCol][currentRow+1].equals("blue")){
            if(spaces[currentCol+2][currentRow+2].equals("blue")){
               possible = false;
            }else if(spaces[currentCol+2][currentRow].equals("blue")){
               possible = false;
            }else if(spaces[currentCol+1][currentRow-1].equals("blue")){
               possible = false;
            }
         }else if(spaces[currentCol][currentRow+2].equals("red")){
            if(spaces[currentCol+2][currentRow+1].equals("red")){
               possible = false;
            }
         }else if(spaces[currentCol][currentRow+2].equals("blue")){
            if(spaces[currentCol+2][currentRow+1].equals("blue")){
               possible = false;
            }
         }
      }catch(ArrayIndexOutOfBoundsException e){}
      
      return possible;
   }
//-------------------------
//Connectors Position Eight
//-------------------------
   public boolean checkOpponentConnectorsPositionEight()
   {
      boolean possible = true;
      
      try{
         if(spaces[currentCol][currentRow+1].equals("red")){   
            if(spaces[currentCol+1][currentRow-1].equals("red")){
               possible = false;
            }else if(spaces[currentCol+2][currentRow].equals("red")){
               possible = false;
            }
         }else if(spaces[currentCol][currentRow+1].equals("blue")){
            if(spaces[currentCol+1][currentRow-1].equals("blue")){
               possible = false;
            }else if(spaces[currentCol+2][currentRow].equals("blue")){
               possible = false;
            }
         }else if(spaces[currentCol+1][currentRow+1].equals("red")){
            if(spaces[currentCol][currentRow-1].equals("red")){
               possible = false;
            }else if(spaces[currentCol+2][currentRow-1].equals("red")){
               possible = false;
           }else if(spaces[currentCol+3][currentRow].equals("red")){
              possible = false;
            }
         }else if(spaces[currentCol+1][currentRow+1].equals("blue")){
            if(spaces[currentCol][currentRow-1].equals("blue")){
               possible = false;
            }else if(spaces[currentCol+2][currentRow-1].equals("blue")){
               possible = false;
            }else if(spaces[currentCol+3][currentRow].equals("blue")){
               possible = false;
            }
         }else if(spaces[currentCol+1][currentRow].equals("red")){
            if(spaces[currentCol-1][currentRow+1].equals("red")){
               possible = false;
            }else if(spaces[currentCol][currentRow+2].equals("red")){
               possible = false;
            }else if(spaces[currentCol+2][currentRow+2].equals("red")){
              possible = false;
            }
         }else if(spaces[currentCol+1][currentRow].equals("blue")){
            if(spaces[currentCol-1][currentRow+1].equals("blue")){
               possible = false;
            }else if(spaces[currentCol][currentRow+2].equals("blue")){
               possible = false;
            }else if(spaces[currentCol+2][currentRow+2].equals("blue")){
               possible = false;
            }
         }else if(spaces[currentCol+2][currentRow].equals("red")){
            if(spaces[currentCol+1][currentRow+2].equals("red")){
               possible = false;
            }
         }else if(spaces[currentCol+2][currentRow].equals("blue")){
            if(spaces[currentCol+1][currentRow+2].equals("blue")){
               possible = false;
            }
         }
      }catch(ArrayIndexOutOfBoundsException e){}
      
      return possible;
   }//end checkConnectorPositions
//-------------------
//Make Visited Array
//-------------------
	public void makeVisitedArray()
	{
		visitedConnectors = new String[577][577];
		
		for (int i = 0; i < 577; i++){
         for (int j = 0; j < 577; j++){
            visitedConnectors[i][j] = connectors[i][j];
         }
      }
	}//end remakeArray
//----------------
//Check Red Winner
//----------------
   public void checkRedWinner()
   {
		for(int i=1;i<24;i++){
         if(spaces[1][i].equals("red")){
            for(int j=1;j<24;j++){
               if(spaces[24][j].equals("red")){
                  System.out.println("Possible red win at " + i + " to " + j + ".");
						//searchPath
						makeVisitedArray();
               	searchRedPath(24*(i-1)+1);
					
               }
            }
         }
      }
   }//end checkRedWinner
//----------------
//Search Red Path
//----------------
   public int searchRedPath(int checkRow)
   {
      if(checkRow==0){
			return 0;
		}else if((checkRow%24)==0){
			redWins();
			return 0;
		}else{
			for(int x=1;x<576;x++){
				if(visitedConnectors[checkRow][x].equals("r")){
					visitedConnectors[checkRow][x] = "rv";
					visitedConnectors[x][checkRow] = "rv";
					return searchRedPath(x);
				}
			}
		}
		return 0;
   }//end searchRedPath
//-----------
//Red Wins!!
//-----------
	public void redWins()
	{
		JOptionPane.showMessageDialog(null,redPlayerName + " wins!!");
		
		saveScores(0);
		
		int newGame = JOptionPane.showConfirmDialog(null,"Do you want to start a new game?");
		
		if(newGame == 0){
         redTotalPegs = 0;
         blueTotalPegs = 0;
         redTotalConnectors = 0;
         blueTotalConnectors = 0;
         
         gameInfoText.setText("A new game has been started.");
         
         redPlayerName = JOptionPane.showInputDialog("Enter red player name.");
         bluePlayerName = JOptionPane.showInputDialog("Enter blue player name.");
         
         do{
           if(redPlayerName.equals("")){    
               redPlayerName = JOptionPane.showInputDialog("Enter red player name.");
            }if(bluePlayerName.equals("")){
               bluePlayerName = JOptionPane.showInputDialog("Enter blue player name.");
            }
         }while(redPlayerName.equals("") || bluePlayerName.equals(""));
         
         redPlayerTurn = true;
         gameInfoText.setText(redPlayerName + " goes first.");
         
         redNameTotalPegsLabel.setText(redPlayerName + "'s total pegs: ");
         blueNameTotalPegsLabel.setText(bluePlayerName + "'s total pegs: ");
         redNameTotalConnectorsLabel.setText(redPlayerName + "'s total connectors: ");
         blueNameTotalConnectorsLabel.setText(bluePlayerName + "'s total connectors: ");
         
         for (int i = 0; i < 24; i++){
            for (int j = 0; j < 24; j++){
               spaces[i][j] = "";
            }
         }
         
         for (int i = 0; i < 24; i++){
            for (int j = 0; j < 24; j++){
               cell[i][j].setContent(TwixtCell.BLANK);
            }
         }
         
         createBoardPanel();
         createBorderPanels();
         createScorePanel();
      }else{
			System.exit(0);
		}
	}//end redWins
//------------------
//Check Blue Winner
//------------------
   public void checkBlueWinner()
   {
      for(int i=1;i<24;i++){
         if(spaces[i][1].equals("blue")){
            for(int j=1;j<24;j++){
               if(spaces[j][24].equals("blue")){
                  System.out.println("Possible blue win at " + i + " to " + j + ".");
						//searchPath
						makeVisitedArray();
               	searchBluePath(i);
               }
            }
         }
      }
   }//end checkBlueWinner
//-----------------
//Search Blue Path
//-----------------
   public int searchBluePath(int checkRow)
   {
		if(checkRow==0){
			return 0;
		}else if((576-checkRow)>=0&&(576-checkRow)<=23){
			blueWins();
			return 0;
		}else{
			for(int x=1;x<576;x++){
				if(visitedConnectors[checkRow][x].equals("b")){
					visitedConnectors[checkRow][x] = "bv";
					visitedConnectors[x][checkRow] = "bv";
					return searchBluePath(x);
				}
			}
		}
		return 0;
   }//end searchBluePath
//------------
//Blue Wins!!
//------------	
	public void blueWins()
	{
		JOptionPane.showMessageDialog(null, bluePlayerName + " wins!!");
		
		saveScores(1);
		
		int newGame = JOptionPane.showConfirmDialog(null,"Do you want to start a new game?");
		
	   if(newGame == 0){
         redTotalPegs = 0;
         blueTotalPegs = 0;
         redTotalConnectors = 0;
         blueTotalConnectors = 0;
         
         gameInfoText.setText("A new game has been started.");
         
         redPlayerName = JOptionPane.showInputDialog("Enter red player name.");
         bluePlayerName = JOptionPane.showInputDialog("Enter blue player name.");
         
         do{
           if(redPlayerName.equals("")){    
               redPlayerName = JOptionPane.showInputDialog("Enter red player name.");
            }if(bluePlayerName.equals("")){
               bluePlayerName = JOptionPane.showInputDialog("Enter blue player name.");
            }
         }while(redPlayerName.equals("") || bluePlayerName.equals(""));
         
         redPlayerTurn = true;
         gameInfoText.setText(redPlayerName + " goes first.");
         
         redNameTotalPegsLabel.setText(redPlayerName + "'s total pegs: ");
         blueNameTotalPegsLabel.setText(bluePlayerName + "'s total pegs: ");
         redNameTotalConnectorsLabel.setText(redPlayerName + "'s total connectors: ");
         blueNameTotalConnectorsLabel.setText(bluePlayerName + "'s total connectors: ");
         
         for (int i = 0; i < 24; i++){
            for (int j = 0; j < 24; j++){
               spaces[i][j] = "";
            }
         }
         
         for (int i = 0; i < 24; i++){
            for (int j = 0; j < 24; j++){
               cell[i][j].setContent(TwixtCell.BLANK);
            }
         }
         
         createBoardPanel();
         createBorderPanels();
         createScorePanel();
      }else{
			System.exit(0);
		}
	}//end blueWins
//------------
//Save Scores
//------------		
	public void saveScores(int whoWins)
	{
		JOptionPane.showMessageDialog(null, "Select file to save scores to.");
	   JFileChooser chooser = new JFileChooser();
      int status = chooser.showSaveDialog(null);
      if (status == JFileChooser.APPROVE_OPTION){
         fileName = chooser.getSelectedFile().getAbsolutePath();
      }
			
		try{
			File myFile = new File(fileName);
	      FileWriter outWriter = new FileWriter(myFile,true);
			if(whoWins == 0){
	      	outWriter.append("\n" + redPlayerName + " " + redPlayerName + " " + redTotalPegs + " " + redTotalConnectors + " " + bluePlayerName + " " + blueTotalPegs + " " + blueTotalConnectors);
	      }else{
				outWriter.append("\n" + bluePlayerName + " " + redPlayerName + " " + redTotalPegs + " " + redTotalConnectors + " " + bluePlayerName + " " + blueTotalPegs + " " + blueTotalConnectors);
			}
			outWriter.close();
		}catch(IOException e){}
	}//end saveScores
}//end Twixt