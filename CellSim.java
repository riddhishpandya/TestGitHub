import java.awt.Color;
public class CellSim{

	public static void main(String[] args){

		//Create a n x n 2-D character array representing the tissue sample
		// You can pick n
		//Write your code to test your methods here
		System.out.print("Enter n (the size of the tissue array- n x n): ");
		int n = IO.readInt();
		while (n <= 0) {
			System.out.print("Enter n (the size of the tissue array - n x n)(n must be postive): ");
			n = IO.readInt();
		}
		System.out.print("Enter the threshold (percent of like-agents for agent to be satisfied: ");
		int threshold = IO.readInt();
		while (threshold < 0 || threshold>100) {
			System.out.print("Enter the threshold (percent of like-agents for agent to be satisfied)(0-100): ");
			threshold = IO.readInt();
		}
		System.out.print("Enter percentage of blank cells: ");
		int blankCells = IO.readInt();
		while (blankCells < 0 || blankCells > 100) {
			System.out.print("Enter percentage of blank cells: (0 - 100) ");
			blankCells = IO.readInt();
		}
		System.out.print("Enter percentage of remaining cells, which are X agents: ");
		int xAgents = IO.readInt();
		while (xAgents < 0 || xAgents > 100) {
			System.out.print("Enter percentage of remaining cells, which are X agents (0-100):  ");
			xAgents = IO.readInt();
		}
		char[][] tissue = new char[n][n];
		assignCellTypes(tissue, blankCells, xAgents);
		System.out.println("Initial Board: ");
		printTissue(tissue);
		System.out.println();
		System.out.print("Enter maximum number of rounds before giving up: ");
		int maxRounds = IO.readInt();
		while (maxRounds <=0) {
			IO.reportBadInput();
			maxRounds = IO.readInt();
		}
		System.out.print("Enter frequency of board output: ");
		int frequency = IO.readInt();
		while (frequency <=0) {
			IO.reportBadInput();
			frequency = IO.readInt();
		}
		int currentRound = 0;
		int totalMoves = 0;
		int rounds;
		CellSimGUI board = new CellSimGUI(n,10);
		for (rounds = 1;rounds<maxRounds; rounds++){
			paintCells(tissue,board);
			if (boardSatisfied(tissue,threshold) == true) {
				break;
			} else {
			totalMoves = totalMoves + moveAllUnsatisfied(tissue,threshold);
			}
			currentRound++;
			if (currentRound == frequency) {
				System.out.println("Round #: " + rounds);
				printTissue(tissue);
				System.out.println();
				currentRound = 0;
			}
		}	
		if (boardSatisfied(tissue,threshold) == true) {
			System.out.println("Final Board:");
			printTissue(tissue);
			System.out.println("Satisfied: YES");
			System.out.println("Total Rounds: " + rounds);
			System.out.println("Total Movements: " + totalMoves);
			} 
		if (boardSatisfied(tissue,threshold) == false) {
			System.out.println("Final Board:");
			printTissue(tissue);
			System.out.println("Satisfied: NO");
			System.out.println("Percent of satisfied agents: " + percentageSatisfied(tissue, threshold));
			System.out.println("Total Movements: " + totalMoves);
			} 
	}
	
	/**
	* Given a tissue sample, prints the cell make up in grid form
	*
	* @param tissue a 2-D character array representing a tissue sample
	* 
	***/
	public static void printTissue(char[][] tissue){
		//Your code goes here
		for(int a = 0; a < tissue.length; a++) {
			for(int b = 0; b < tissue.length; b++) {
				System.out.print("[ " + tissue[a][b] + " ]" );
			}
			System.out.println();
		}
		
	}
	/**
	* Given a blank tissue sample, populate it with the correct cell makeup given the parameters. 
	* Cell type 'X' will be represented by the character 'X'
	* Cell type 'O' will be represented by the character 'O'
	* Vacant spaces will be represented by the character ' '
	*
	* Phase I: alternate X and O cells throughout, vacant cells at the "end" (50% credit)
	*		e.g.:	'X' 'O' 'X' 'O' 'X'
	*				'O' 'X' 'O' 'X' 'O'
	*				'X' 'O' 'X' 'O' 'X'
	*				' ' ' ' ' ' ' ' ' '
	*				' ' ' ' ' ' ' ' ' '
	*
	* Phase II: Random assignment of all cells (100% credit)
	*
	* @param tissue a 2-D character array that has been initialized
	* @param percentBlank the percentage of blank cells that should appear in the tissue
	* @param percentX Of the remaining cells, not blank, the percentage of X cells that should appear in the tissue. Round up if not a whole number
	*
	**/
	public static void assignCellTypes(char[][] tissue, int percentBlank, int percentX){
	
		//Your code goes here
		int totalCells = tissue.length*tissue.length;
		
		double decimalBlankCells = ((double)percentBlank/100.0) * (totalCells);
		int blankCells = (int)Math.ceil(decimalBlankCells);
		
		double decimalXCells = (totalCells - blankCells) * ((double)percentX/100.0); 
		int xCells = (int)Math.ceil(decimalXCells);
		
		int oCells = totalCells - blankCells - xCells; 	
		
		for(int a = 0; a < tissue.length; a++){ 
			int b = 0;
			while (b < tissue.length) {
				int cellValue = (int)(Math.random()*3);
				if (cellValue == 0 && blankCells > 0) {
					blankCells -= 1;
					tissue[a][b] = ' ';
					b++;
				} 
				if (cellValue == 1 && xCells > 0) {
					xCells -= 1;
					tissue[a][b] = 'X';
					b++;
				}
				if (cellValue == 2 && oCells > 0) {
					oCells -= 1;
					tissue[a][b] = 'O';
					b++;
				}
			}
		}
			
		
	}

	/**
	    * Given a tissue sample, and a (row,col) index into the array, determines if the agent at that location is satisfied.
	    * Note: Blank cells are always satisfied (as there is no agent)
	    *
	    * @param tissue a 2-D character array that has been initialized
	    * @param row the row index of the agent
	    * @param col the col index of the agent
	    * @param threshold the percentage of like agents that must surround the agent to be satisfied
	    * @return boolean indicating if given agent is satisfied
	    *
	    **/
	    public static boolean isSatisfied(char[][] tissue, int row, int col, int threshold){
			if (tissue.length == 1) {
				return false;
				}
	    	if (tissue[row][col] == ' ') {
	    		return true;
				}
	    	char q = tissue[row][col];
	    	int totalSurroundingCells = 0;
	    	int totalSameCells = 0;
	    	double t = (double)threshold/100.0;
	    	//for corner cells (3 comparisons)
	    	if (row == 0 && col == 0){
    			if(tissue[row][col+1] == 'X' || tissue[row][col+1] == 'O') {
    				totalSurroundingCells++;
    			}
    			if(tissue[row][col+1] == q) {
    				totalSameCells++;
    			}
    			if(tissue[row+1][col] == 'X' || tissue[row+1][col] == 'O') {
    				totalSurroundingCells++;
	    		}
	    		if(tissue[row+1][col] == q) {
	    			totalSameCells++;
	    		}
	    		if(tissue[row+1][col+1] == 'X' || tissue[row+1][col+1] == 'O') {
	    			totalSurroundingCells++;
	   			}
	   			if(tissue[row+1][col+1] == q) {
	   				totalSameCells++;
	   			}
    	}
	    	if (row == 0 && col == tissue.length - 1){
    			if(tissue[row][col-1] == 'X' || tissue[row][col-1] == 'O') {
    				totalSurroundingCells++;
    			}
    			if(tissue[row][col-1] == q) {
    				totalSameCells++;
    			}
    			if(tissue[row+1][col] == 'X' || tissue[row+1][col] == 'O') {
    				totalSurroundingCells++;
	    		}
	    		if(tissue[row+1][col] == q) {
	    			totalSameCells++;
	    		}
	    		if(tissue[row+1][col-1] == 'X' || tissue[row+1][col-1] == 'O') {
	    			totalSurroundingCells++;
	   			}
	   			if(tissue[row+1][col-1] == q) {
	   				totalSameCells++;
	   			}
    	}
	    	if (row == tissue.length - 1 && col == 0){
    			if(tissue[row-1][col] == 'X' || tissue[row-1][col] == 'O') {
    				totalSurroundingCells++;
    			}
    			if(tissue[row-1][col] == q) {
    				totalSameCells++;
    			}
    			if(tissue[row][col+1] == 'X' || tissue[row][col+1] == 'O') {
    				totalSurroundingCells++;
	    		}
	    		if(tissue[row][col+1] == q) {
	    			totalSameCells++;
	    		}
	    		if(tissue[row-1][col+1] == 'X' || tissue[row-1][col+1] == 'O') {
	    			totalSurroundingCells++;
	   			}
	   			if(tissue[row-1][col+1] == q) {
	   				totalSameCells++;
	   			}
    	}
	    	if (row == tissue.length - 1 && col == tissue.length - 1){
    			if(tissue[row-1][col] == 'X' || tissue[row-1][col] == 'O') {
    				totalSurroundingCells++;
    			}
    			if(tissue[row-1][col] == q) {
    				totalSameCells++;
    			}
    			if(tissue[row][col-1] == 'X' || tissue[row][col-1] == 'O') {
    				totalSurroundingCells++;
	    		}
	    		if(tissue[row][col-1] == q) {
	    			totalSameCells++;
	    		}
	    		if(tissue[row-1][col-1] == 'X' || tissue[row-1][col-1] == 'O') {
	    			totalSurroundingCells++;
	   			}
	   			if(tissue[row-1][col-1] == q) {
	   				totalSameCells++;
	   			}
    	}
	    	//for all edge cells that are not corner cells (5 comparisons)
	    	if (row == 0 && col > 0 && col < tissue.length - 1) {
	    		if(tissue[row][col-1] == 'X' || tissue[row][col-1] == 'O') {
	    			totalSurroundingCells++;
	    			}
	    		if(tissue[row][col-1] == q) {
		    		totalSameCells++;
		    		}
	    		if(tissue[row+1][col-1] == 'X' || tissue[row+1][col-1] == 'O') {
	    			totalSurroundingCells++;
	    			}
	    		if(tissue[row+1][col-1] == q) {
		    		totalSameCells++;
		    		}	
	    		if(tissue[row+1][col] == 'X' || tissue[row+1][col] == 'O') {
	    			totalSurroundingCells++;
	    			}
	    		if(tissue[row+1][col] == q) {
		    		totalSameCells++;
		    		}
	    		if(tissue[row][col+1] == 'X' || tissue[row][col+1] == 'O') {
	    			totalSurroundingCells++;
	    			}
	    		if(tissue[row][col+1] == q) {
		    		totalSameCells++;
		    		}
	    		if(tissue[row+1][col+1] == 'X' || tissue[row+1][col+1] == 'O') {
	    			totalSurroundingCells++;
	    			}
	    		if(tissue[row+1][col+1] == q) {
		    		totalSameCells++;
		    		}
	   	}	
	    	if (col == 0 && row > 0 && row < tissue.length - 1) {
	    		if(tissue[row-1][col] == 'X' || tissue[row-1][col] == 'O') {
	    			totalSurroundingCells++;
	    			}
	    		if(tissue[row-1][col] == q) {
		    		totalSameCells++;
		    		}
	    		if(tissue[row-1][col+1] == 'X' || tissue[row-1][col+1] == 'O') {
	    			totalSurroundingCells++;
	    			}
	    		if(tissue[row-1][col+1] == q) {
		    		totalSameCells++;
		    		}	
	    		if(tissue[row][col+1] == 'X' || tissue[row][col+1] == 'O') {
	    			totalSurroundingCells++;
	    			}
	    		if(tissue[row][col+1] == q) {
		    		totalSameCells++;
		    		}
	    		if(tissue[row+1][col] == 'X' || tissue[row+1][col] == 'O') {
	    			totalSurroundingCells++;
	    			}
	    		if(tissue[row+1][col] == q) {
		    		totalSameCells++;
		    		}
	    		if(tissue[row+1][col+1] == 'X' || tissue[row+1][col+1] == 'O') {
	    			totalSurroundingCells++;
	    			}
	    		if(tissue[row+1][col+1] == q) {
		    		totalSameCells++;
		    		}
	   	}	
	    	if (row == tissue.length - 1  && col > 0 && col < tissue.length - 1) {
	    		if(tissue[row][col-1] == 'X' || tissue[row][col-1] == 'O') {
	    			totalSurroundingCells++;
	    			}
	    		if(tissue[row][col-1] == q) {
		    		totalSameCells++;
		    		}
	    		if(tissue[row-1][col-1] == 'X' || tissue[row-1][col-1] == 'O') {
	    			totalSurroundingCells++;
	    			}
	    		if(tissue[row-1][col-1] == q) {
		    		totalSameCells++;
		    		}	
	    		if(tissue[row-1][col] == 'X' || tissue[row-1][col] == 'O') {
	    			totalSurroundingCells++;
	    			}
	    		if(tissue[row-1][col] == q) {
		    		totalSameCells++;
		    		}
	    		if(tissue[row][col+1] == 'X' || tissue[row][col+1] == 'O') {
	    			totalSurroundingCells++;
	    			}
	    		if(tissue[row][col+1] == q) {
		    		totalSameCells++;
		    		}
	    		if(tissue[row-1][col+1] == 'X' || tissue[row-1][col+1] == 'O') {
	    			totalSurroundingCells++;
	    			}
	    		if(tissue[row-1][col+1] == q) {
		    		totalSameCells++;
		    		}
	   	}	
	    	if (col == tissue.length - 1  && row > 0 && row < tissue.length - 1) {
	    		if(tissue[row-1][col] == 'X' || tissue[row-1][col] == 'O') {
	    			totalSurroundingCells++;
	    			}
	    		if(tissue[row-1][col] == q) {
		    		totalSameCells++;
		    		}
	    		if(tissue[row-1][col-1] == 'X' || tissue[row-1][col-1] == 'O') {
	    			totalSurroundingCells++;
	    			}
	    		if(tissue[row-1][col-1] == q) {
		    		totalSameCells++;
		    		}	
	    		if(tissue[row][col-1] == 'X' || tissue[row][col-1] == 'O') {
	    			totalSurroundingCells++;
	    			}
	    		if(tissue[row][col-1] == q) {
		    		totalSameCells++;
		    		}
	    		if(tissue[row+1][col] == 'X' || tissue[row+1][col] == 'O') {
	    			totalSurroundingCells++;
	    			}
	    		if(tissue[row+1][col] == q) {
		    		totalSameCells++;
		    		}
	    		if(tissue[row+1][col-1] == 'X' || tissue[row+1][col-1] == 'O') {
	    			totalSurroundingCells++;
	    			}
	    		if(tissue[row+1][col-1] == q) {
		    		totalSameCells++;
		    		}
	   	}	
	    	//for middle cells (all have the same 8 comparisons)
	    	if (row > 0 && col > 0 && row < tissue.length - 1 && col < tissue.length - 1) {
	    		if(tissue[row-1][col-1] == 'X' || tissue[row-1][col-1] == 'O') {
	    			totalSurroundingCells++;
	    			}
	    		if(tissue[row-1][col-1] == q) {
		    		totalSameCells++;
		    		}
	    		if(tissue[row-1][col] == 'X' || tissue[row-1][col] == 'O') {
	    			totalSurroundingCells++;
	    			}
	    		if(tissue[row-1][col] == q) {
		    		totalSameCells++;
		    		}	
	    		if(tissue[row-1][col+1] == 'X' || tissue[row-1][col+1] == 'O') {
	    			totalSurroundingCells++;
	    			}
	    		if(tissue[row-1][col+1] == q) {
		    		totalSameCells++;
		    		}
	    		if(tissue[row][col-1] == 'X' || tissue[row][col-1] == 'O') {
	    			totalSurroundingCells++;
	    			}
	    		if(tissue[row][col-1] == q) {
		    		totalSameCells++;
		    		}
	    		if(tissue[row][col+1] == 'X' || tissue[row][col+1] == 'O') {
	    			totalSurroundingCells++;
	    			}
	    		if(tissue[row][col+1] == q) {
		    		totalSameCells++;
		    		}
	    		if(tissue[row+1][col-1] == 'X' || tissue[row+1][col-1] == 'O') {
	    			totalSurroundingCells++;
	    			}
	    		if(tissue[row+1][col-1] == q) {
		    		totalSameCells++;
		    		}
	    		if(tissue[row+1][col] == 'X' || tissue[row+1][col] == 'O') {
	    			totalSurroundingCells++;
	    			}
	    		if(tissue[row+1][col] == q) {
		    		totalSameCells++;
		    		}
	    		if(tissue[row+1][col+1] == 'X' || tissue[row+1][col+1] == 'O') {
	    			totalSurroundingCells++;
	    			}
	    		if(tissue[row+1][col+1] == q) {
		    		totalSameCells++;
		    		}
	    	}
	    	double percentQ = (double)totalSameCells/totalSurroundingCells;
	    	if (percentQ >= t) {
	    		return true;
	    	} else {
	    			return false;
	    		}
	    		  		
	    }
	   
	    /**
	    * Given a tissue sample, determines if all agents are satisfied.
	    * Note: Blank cells are always satisfied (as there is no agent)
	    *
	    * @param tissue a 2-D character array that has been initialized
	    * @return boolean indicating whether entire board has been satisfied (all agents)
	    **/
	    public static boolean boardSatisfied(char[][] tissue, int threshold){
	    for (int rows = 0; rows < tissue.length - 1; rows++) {
	    	for (int cols = 0; cols < tissue.length - 1; cols++) {
	    		if (isSatisfied(tissue, rows, cols, threshold) == false) {
	    			return false;
	    			}
	    		}
	    	}
	    return true;
	    }    
	    /**
	    * Given a tissue sample, move all unsatisfied agents to a vacant cell
	    *
	    * @param tissue a 2-D character array that has been initialized
	    * @param threshold the percentage of like agents that must surround the agent to be satisfied
	    *
	    **/
	    public static int moveAllUnsatisfied(char[][] tissue, int threshold) {
			int numberofBlanks = 0;
			int totalMoves = 0;
			for (int row = 0; row < tissue.length; row++) {
				for (int col = 0; col < tissue.length; col++) {
					if (tissue[row][col] == ' ') {
						numberofBlanks++;
					}
				}
			}
			int [] blanksX = new int [numberofBlanks];
			int [] blanksY = new int [numberofBlanks];
			int x = 0;
			for (int row = 0; row < tissue.length; row++) {
				for (int col = 0; col < tissue.length; col++) {
					if (tissue[row][col] == ' ') {
					blanksX[x] = row;
					blanksY[x] = col;
					x++;
					}
				}
			}
			for (int rows = 0; rows < tissue.length; rows++) {
				for (int cols = 0; cols < tissue[0].length; cols ++ ) {
					int randomBlank = (int)(Math.random()*blanksX.length);
						if (isSatisfied(tissue, rows, cols, threshold) == false) {
							char temp = tissue[rows][cols];
							if(temp != ' '){
	    						tissue[rows][cols] = ' ';
		    					tissue[blanksX[randomBlank]][blanksY[randomBlank]] = temp;
		    					blanksX[randomBlank] = rows;
		    					blanksY[randomBlank] = cols;
		    					totalMoves++;
								}
							}
					}
				} 
			return totalMoves;
		}
	    public static double percentageSatisfied(char[][] tissue, int threshold) {
		    	int numberOfSatisfied = 0;
		    	int totalAgents = 0;
		    	double percentageSatisfied;
		    	for (int rows = 0; rows < tissue.length; rows++) {
		    		for (int cols = 0; cols < tissue[0].length; cols++) {
		    			if ((isSatisfied(tissue, rows, cols, threshold) == true) && (tissue[rows][cols] != ' ')) {
		    				numberOfSatisfied++;
		    			}
		    			if (tissue[rows][cols] != ' ') {
		    				totalAgents++;
		    			}
		    		}
		    	}
		    	percentageSatisfied = (((double)numberOfSatisfied/(double)totalAgents)*100.0);
		    	return percentageSatisfied;
		    }
	     
	    public static void paintCells(char[][] tissue ,CellSimGUI board) {
	    	for (int rows = 0; rows < tissue.length; rows++) {
	    		for (int cols = 0; cols < tissue.length; cols++) {
	    			if (tissue[rows][cols] == 'X') {
	    				board.setCell(rows, cols, Color.black);
	    			}
	    			if (tissue[rows][cols] == 'O') {
	    				board.setCell(rows, cols, Color.green);
	    			}
	    			if (tissue[rows][cols] == ' ') {
	    				board.setCell(rows, cols, Color.white);
	    			}
	    		}
	    	}
	    }
	}