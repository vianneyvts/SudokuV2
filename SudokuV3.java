import javax.swing.*; 
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;


public class SudokuV3 extends JFrame implements ActionListener, KeyListener{

	private JTextField [][] boxesGrid;
	private String fieldMemory;
	private Color colorMemory;
	private JButton solveButton;
	private JButton saveButton;
	private JButton clearButton;
	private JButton loadButton;
	private JButton deleteButton;
	private int [] unactiveKeys = {8,10,16,37,38,39,40};
	private LinkedList<int[][]> savedGrids = new LinkedList<int[][]>();
	private LinkedList<String> savedNames = new LinkedList<String>();

	/*---------------Constructor---------------*/
	public SudokuV3(){
		/*--------Window--------*/
		super("Sudoku Solver by VV");
		setBounds(636, 100, 1000+6, 1200+6); // le +6 vient des marges
		setLayout(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Font calibri1 = new Font("Calibri", Font.BOLD, 50);
		Font calibri2 = new Font ("Calibri", Font.BOLD, 30);
		Font calibri3 = new Font ("Calibri", Font.BOLD, 20);
		Color darkBlue = new Color(35, 50, 90);
		Color blue = new Color(50, 100, 170);
		Color lightBlue = new Color(150, 180, 210);

		/*--------Main container--------*/
		JPanel globalPanel = new JPanel();
		globalPanel.setBounds(0, 0, 1000, 1200);
		globalPanel.setLayout(null);
		globalPanel.setBackground(lightBlue);

		/*--------Interface--------*/
		// titre
		JLabel title = new JLabel("Sudoku Solver");
		title.setBounds(345, 50, 500, 100);
		title.setFont(calibri1);
		globalPanel.add(title);
		// signature
		JLabel signature = new JLabel("by VV");
		signature.setBounds(680, 57, 300, 100);
		signature.setFont(calibri2);
		globalPanel.add(signature);
		// solve button
		solveButton = new JButton("Solve");
		solveButton.setBounds(110, 1030, 150, 70);
		solveButton.setBackground(darkBlue);
		solveButton.setForeground(Color.white);
		solveButton.setFont(calibri2);
		solveButton.addActionListener(this);
		globalPanel.add(solveButton);
		// delete button
		clearButton = new JButton("Clear");
		clearButton.setBounds(320, 1030, 150, 70);
		clearButton.setBackground(darkBlue);
		clearButton.setForeground(Color.white);
		clearButton.setFont(calibri2);
		clearButton.addActionListener(this);
		globalPanel.add(clearButton);
		// tag
		JLabel tag = new JLabel("Saves management :");
		tag.setBounds(600, 975, 500, 100);
		tag.setFont(calibri2);
		globalPanel.add(tag);
		// save button
		saveButton = new JButton("Save");
		saveButton.setBounds(550, 1050, 100, 50);
		saveButton.setBackground(blue);
		saveButton.setForeground(Color.white);
		saveButton.setFont(calibri3);
		saveButton.addActionListener(this);
		globalPanel.add(saveButton);
		// load button
		loadButton = new JButton("Load");
		loadButton.setBounds(670, 1050, 100, 50);
		loadButton.setBackground(blue);
		loadButton.setForeground(Color.white);
		loadButton.setFont(calibri3);
		loadButton.addActionListener(this);
		globalPanel.add(loadButton);
		// delete button
		deleteButton = new JButton("Delete");
		deleteButton.setBounds(790, 1050, 100, 50);
		deleteButton.setBackground(blue);
		deleteButton.setForeground(Color.white);
		deleteButton.setFont(calibri3);
		deleteButton.addActionListener(this);
		globalPanel.add(deleteButton);

		/*
		ancienne position des boutons
		solveButton.setBounds(110, 1000, 150, 70);
		clearButton.setBounds(320, 1000, 150, 70);
		saveButton.setBounds(530, 1000, 150, 70);
		loadButton.setBounds(740, 1000, 150, 70);
		*/


		/*--------Boxes--------*/
		boxesGrid = new JTextField [9][9];

		for(int i=0; i<9; i++){
			for(int j=0; j<9; j++){
				boxesGrid [i][j] = new JTextField();
				boxesGrid [i][j].setName(i+":"+j);
				boxesGrid [i][j].setBounds(110+80*j+30*(j/3), 180+80*i+30*(i/3), 70, 70);
				boxesGrid [i][j].setFont(calibri1);
				boxesGrid [i][j].setHorizontalAlignment(JTextField.CENTER);
				boxesGrid [i][j].addKeyListener(this);
				globalPanel.add(boxesGrid [i][j]);
			}
		}
		String s = boxesGrid [5][5].getName();
		
		// Display main container
		this.add(globalPanel);
		// Display window
		this.setVisible(true);

		createDataFiles();
		loadData();
	}

	/*---------------Listeners---------------*/
	public void actionPerformed (ActionEvent e){

		if(e.getSource() == solveButton){
			solve();
        }

        if(e.getSource() == clearButton){
        	clearGrid();
		}

		if(e.getSource() == saveButton){
			saveGrid();
		}

		if(e.getSource() == loadButton){
			loadGrid();
		}

		if(e.getSource() == deleteButton){
			deleteSave();
		}
	}

	public void keyPressed(KeyEvent ke){
		if(ke.getKeyCode() == 10){
			solve();
		}
		if(ke.getSource() instanceof JTextField){
			// JTextField filling
			JTextField box = (JTextField) ke.getSource();
			fieldMemory = box.getText();
			colorMemory = box.getForeground();
			if(ke.getKeyChar() >= '1' && ke.getKeyChar() <= '9'){
				box.setText("");
			}else if(!isUnactive(ke.getKeyCode())){
				box.setForeground(Color.white);
			}
			// JTextField navigation
			else{
				String name = box.getName();
				char[] ch = name.toCharArray();
				int i = Character.getNumericValue(ch[0]);
				int j = Character.getNumericValue(ch[2]);
				switch (ke.getKeyCode()) {
            		case 37:
            			j = (j-1+9)%9;
		                break;
		            case 38:
		            	i = (i-1+9)%9;
		                break;
		            case 39:
		            	j = (j+1)%9;
		                break;
		            case 40:
		            	i = (i+1)%9;
		                break;
		            default:
		                break;
		        }
		        boxesGrid[i][j].requestFocus();
		    }
		}
    }  

    public void keyReleased(KeyEvent ke) {  
		if(ke.getSource() instanceof JTextField){
			// JTextField filling
			JTextField box = (JTextField) ke.getSource();
			if(ke.getKeyChar() >= '0' && ke.getKeyChar() <= '9'){
				box.setText("");
				box.setText(Character.toString(ke.getKeyChar()));
				box.setForeground(Color.black);
			}else if(!isUnactive(ke.getKeyCode())){
				removeLetters();
				box.setText(fieldMemory);
				box.setForeground(colorMemory);
			}
        }
	}

    public void keyTyped(KeyEvent ke) { 
	} 
	
	/*---------------Main---------------*/
	public static void main(String[] arg){
		SudokuV3 SudokuWindow = new SudokuV3();
	}

	/*---------------IHM methods---------------*/
	public int[][] readGrid(){
		int [][] newGrid = new int [9][9];
		for(int i=0; i<9; i++){
			for(int j=0; j<9; j++){
				String textRead = boxesGrid[i][j].getText().trim();
				System.out.println(textRead);
				if(textRead.equals("")){
					newGrid[i][j] = 0;
				}else{
					try{
						newGrid[i][j] = Integer.parseInt(textRead);
					}catch(Exception e){
						JOptionPane.showMessageDialog(null,"The values must be numbers");
					}
				}
			}
		}
		return newGrid;
	}

	public String gridToString(int[][] grid){
		String res = new String();
		for(int i=0; i<grid.length; i++){
			for(int j=0; j<grid[1].length; j++){
				res += grid[i][j];
			}
		}
		return res;
	}

	public void displayGrid(Grid grid){
		for(int i=0; i<9; i++){
			for(int j=0; j<9; j++){
				// ne pas afficher les 0
				if(grid.nbGrid[i][j] != 0){
					boxesGrid[i][j].setText(Integer.toString(grid.nbGrid[i][j]));
					if(grid.nbGrid[i][j] == grid.unsolvedNbGrid[i][j]){
						boxesGrid[i][j].setForeground(Color.black);
					}
					else{
						boxesGrid[i][j].setForeground(Color.gray);
					}
				}else{
					boxesGrid[i][j].setText("");
				}
			}
		}
	}

	public boolean isUnactive(int n){
		boolean res = false;
		int i = 0;
		while(res == false & i < unactiveKeys.length){
			if(unactiveKeys[i] == n){
				res = true;
			}
			i++;
		}
		return res;
	}

	public void removeLetters(){
		char[] ch = fieldMemory.toCharArray();
		fieldMemory = "";
		for(int i = 0; i < ch.length; i++){
			if(ch[i] >= '0' && ch[i] <= '9'){
				fieldMemory = String.valueOf(ch[i]); 
			}
		}
	}

	/*---------------Buttons---------------*/
	public void solve(){
			Grid gridToSolve = new Grid(readGrid());
			/*int [][] gridTest = {{6,8,0,2,0,0,5,1,0},{0,4,1,0,6,0,0,0,0},{0,5,3,9,0,0,0,0,0},
        	{0,0,0,0,0,0,4,9,0},{4,0,0,5,0,9,0,0,7},{0,2,9,0,0,0,0,0,0},
        	{0,0,0,0,0,3,8,4,0},{0,0,0,0,5,0,3,7,0},{0,6,8,0,0,7,0,5,2}};
        	int [][] reponse = {{6,8,7,2,3,4,5,1,9},{9,4,1,7,6,5,2,8,3},{2,5,3,9,1,8,7,6,4},
        	{7,1,5,3,2,6,4,9,8},{4,3,6,5,8,9,1,2,7},{8,2,9,4,7,1,6,3,5},
        	{5,7,2,6,9,3,8,4,1},{1,9,4,8,5,2,3,7,6},{3,6,8,1,4,7,9,5,2}};*/
        	/*
			6 8 0|2 0 0|5 1 0
			0 4 1|0 6 0|0 0 0
			0 5 3|9 0 0|0 0 0
			- - - - - - - - -
			0 0 0|0 0 0|4 9 0
			4 0 0|5 0 9|0 0 7
			0 2 9|0 0 0|0 0 0
			- - - - - - - - -
			0 0 0|0 0 3|8 4 0
			0 0 0|0 5 0|3 7 0
			0 6 8|0 0 7|0 5 2
			*/
			/*
			6 8 7|2 3 4|5 1 9
			9 4 1|7 6 5|2 8 3
			2 5 3|9 1 8|7 6 4
			- - - - - - - - -
			7 1 5|3 2 6|4 9 8
			4 3 6|5 8 9|1 2 7
			8 2 9|4 7 1|6 3 5
			- - - - - - - - -
			5 7 2|6 9 3|8 4 1
			1 9 4|8 5 2|3 7 6
			3 6 8|1 4 7|9 5 2
			*/
        	if(gridToSolve.solveGrid()){
        		displayGrid(gridToSolve);
        	}else{
				JOptionPane.showMessageDialog(null,"The grid is unvalid");
        	}
	}

	public void clearGrid(){
		for(int i=0; i<9; i++){
			for(int j=0; j<9; j++){
				boxesGrid[i][j].setText("");
				boxesGrid[i][j].setForeground(Color.black);
			}
		}
	}

	/*---------------Files management methods---------------*/
	public void createDataFiles(){
		File file = new File("data.txt");
		try{
			if(!file.exists()){
			file.createNewFile();
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public void saveData(){
		File dataFile = new File("data.txt");
		FileWriter writer = null;
        BufferedWriter buffWriter = null;

	    try {
	     	writer = new FileWriter(dataFile,false);
	     	buffWriter = new BufferedWriter(writer);
	     	int index = 0;
        	for(String name : savedNames){
				String data = gridToString(savedGrids.get(index));
		        buffWriter.write(name+"\n"+data+"\n");
        		index++;
        	}
		} catch (IOException e) {
	     	e.printStackTrace();
	  	} finally{
            try {
                buffWriter.flush();
	     		buffWriter.close();
	     		writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveGrid(){
    	String gridName = "";
    	while(gridName.equals("") || savedNames.contains(gridName)){
    		gridName = JOptionPane.showInputDialog(null,"Enter the grid's name","Save the grid",JOptionPane.PLAIN_MESSAGE);
    		if(gridName == null){
    			break; 
    			// if the user clicks on cancel or closes the window, the returned value would be null
    			// but we can't use the equals() method on a null object
    			// we could have used a try catch statement
    		}
    	} 
    	if(gridName != null && !gridName.equals("")){
    		char[] ch = gridName.toCharArray();
    		// detection of the case where the name is just spaces
    		boolean spacesCase = true;
    		int index = 0;
    		while(spacesCase && index < ch.length){
    			if(ch[index] != ' '){
    				spacesCase = false;
    			}
    			index++;
    		}
    		// saves the grid
    		if(!spacesCase){
    			System.out.println(gridName);
		    	int[][] gridToSave = readGrid();
				savedGrids.add(gridToSave);
				savedNames.add(gridName);
				saveData();
    		}else{
    			saveGrid();
    		}
		}
    }

    public void loadData(){
		File dataFile = new File("data.txt");
		FileReader reader = null;
		BufferedReader buffReader = null;
	    try {
	    	reader = new FileReader(dataFile);
			buffReader = new BufferedReader(reader);
			String line;
			while ((line = buffReader.readLine()) != null) {
				savedNames.add(line);
				line = buffReader.readLine();
				int[][] loadedGrid = new int[9][9];
				char[] chars = line.toCharArray();
				int position = 0;
				for(char c : chars){
					loadedGrid[position%9][position/9] = Character.getNumericValue(c);
					position ++;
				}
				savedGrids.add(loadedGrid);
			}
		} catch (IOException e) {
	    	e.printStackTrace();
	  	} finally{
            try {
	     		buffReader.close();
	     		reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
	}

	public void loadGrid(){
		if(savedNames.isEmpty()){
			JOptionPane.showMessageDialog(null,"There is no grid saved");
		}else{
			// select the grid to load
	    	Object [] gridNames = savedNames.toArray();
	    	String input = (String) JOptionPane.showInputDialog(null, "Choose a grid to load", "Load a grid", JOptionPane.PLAIN_MESSAGE, null, gridNames, gridNames[0]);
	        // display the grid
	    	if (input != null){
		    	int index = savedNames.indexOf(input);
		    	int [][] gridToLoad = savedGrids.get(index);
		    	displayGrid(new Grid(gridToLoad));
		    }
    	}
	}

	public void deleteSave(){
		if(savedNames.isEmpty()){
			JOptionPane.showMessageDialog(null,"There is no grid saved");
		}else{
			// select the grid to load
	    	Object [] gridNames = savedNames.toArray();
	    	String input = (String) JOptionPane.showInputDialog(null, "Choose a grid to load", "Load a grid", JOptionPane.PLAIN_MESSAGE, null, gridNames, gridNames[0]);
	        // display the grid
	    	if (input != null){
		    	int index = savedNames.indexOf(input);
		    	savedNames.remove(index);
		    	savedGrids.remove(index);
		    	saveData();
		    }
    	}
	}
}