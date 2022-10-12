public class Grid {

    public String name;
    public int [][] unsolvedNbGrid;
    public int [][] nbGrid;
    public BoxesGrp[] boxesGrpList;
    public BoxesGrp[] boxesGrpSortedList;
    public int [][] fillingLog;
    public int completion;

    // fillingLog stores in each line the x and y coordinate of the filled box
    // fillingLog is sorted by time : the first boxes to be filled come in first

    /*---------------Constructors---------------*/
    public Grid(){
        // grid creation
        int [][] newGrid = new int [9][9];
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                newGrid[i][j] = 0;
            }
        }
        this.unsolvedNbGrid = newGrid;
        this.nbGrid = newGrid;
        this.boxesGrpList = new BoxesGrp[27];
        this.boxesGrpSortedList = new BoxesGrp[27];
        this.fillingLog = new int [81][2];
        this.completion = 0;
        // instanciation of boxes groups
        for(int i = 0; i < 9; i++){
            boxesGrpList[i] = new BoxesGrp("row", i, 0);
            boxesGrpSortedList[i] = boxesGrpList[i];
            boxesGrpList[9+i] = new BoxesGrp("column", i, 0);
            boxesGrpSortedList[9+i] = boxesGrpList[9+i];
            boxesGrpList[18+i] = new BoxesGrp("square", i, 0);
            boxesGrpSortedList[18+i] = boxesGrpList[18+i];
        }
    }
    
    public Grid(int [][] newGrid){
        this.nbGrid = newGrid;
        this.unsolvedNbGrid = copyGrid(nbGrid);
        this.boxesGrpList = new BoxesGrp[27];
        this.boxesGrpSortedList = new BoxesGrp[27];
        this.completion = 0;
        // instanciation of boxes groups
        for(int i = 0; i < 9; i++){
            boxesGrpList[i] = new BoxesGrp("row", i, 0);
            boxesGrpSortedList[i] = boxesGrpList[i];
            boxesGrpList[9+i] = new BoxesGrp("column", i, 0);
            boxesGrpSortedList[9+i] = boxesGrpList[9+i];
            boxesGrpList[18+i] = new BoxesGrp("square", i, 0);
            boxesGrpSortedList[18+i] = boxesGrpList[18+i];
        }
        // initialization of boxes groups
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                if(nbGrid[i][j] != 0){
                    boxesGrpList[i].completion++;
                    boxesGrpList[9+j].completion++;
                    boxesGrpList[18+3*(i/3)+(j/3)].completion++;
                    completion++;
                }
            }
        }
        // fillingLog initialization
        this.fillingLog = new int [81][2];
        // sort of the list of boxes groups
        sortBoxesGrpSortedList();
    }

    /*---------------Filling tools management---------------*/
    public void updateBoxesGrpList(){
        completion = 0;
        // set to 0 the completion of each boxesGrp in the list
        for(int i = 0; i < 27; i++){
            boxesGrpList[i].completion = 0;
        }
        // update the number of filled boxes for each boxesGrp in the list
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                if(nbGrid[i][j] != 0){
                    boxesGrpList[i].completion ++;
                    boxesGrpList[9+j].completion ++;
                    boxesGrpList[18+3*(i/3)+(j/3)].completion ++;
                    completion++;
                }
            }
        }
    }

    public void sortBoxesGrpSortedList(){
        // sort of the list of boxes groups with the bubble sort method
        // the most completed groups of boxes come in first
        int n = 0;
        boolean sorted;
        BoxesGrp memory = new BoxesGrp();
        int lastUnsorted = boxesGrpSortedList.length-1;
        do{
            sorted = true;
            for(int i = 0; i < lastUnsorted; i++){
                if(boxesGrpSortedList[i].completion < boxesGrpSortedList[i+1].completion){
                    memory = boxesGrpSortedList[i+1];
                    boxesGrpSortedList[i+1] = boxesGrpSortedList[i];
                    boxesGrpSortedList[i] = memory;
                    sorted = false;
                }
            }
            lastUnsorted--;
        }while(!sorted);
    }

    /*---------------Filling methods---------------*/
    public boolean fillLine(int indexGrp){
        // fills the full line unless it finds an error
        boolean error = false; // switch to true if an error is found
        int indexLine = boxesGrpSortedList[indexGrp].index;
        int j = 0;

        do{
            // fills the box if it is empty
            if(nbGrid[indexLine][j] == 0){
                fillBox(indexLine, j);
                // if it doesn't find any fiting number then an error is found
                if(nbGrid[indexLine][j] == 0){
                    error = true;
                }
            }
            j++;
        }while(!error && j < 9);

        return error;
    }

    public boolean fillColumn(int indexGrp){
        // fills the full column unless it finds an error
        boolean error = false; // switch to true if an error is found
        int indexColumn = boxesGrpSortedList[indexGrp].index;
        int j = 0;

        do{
            // fills the box if it is empty
            if(nbGrid[j][indexColumn] == 0){
                fillBox(j, indexColumn);
                // if it doesn't find any fiting number then an error is found
                if(nbGrid[j][indexColumn] == 0){
                    error = true;
                }
            }
            j++;
        }while(!error && j < 9);

        return error;
    }

    public boolean fillBigSquare(int indexGrp){
        // fills the full square unless it finds an error
        boolean error = false; // switch to true if an error is found
        int indexSquare = boxesGrpSortedList[indexGrp].index;
        int i = 0;
        // indexes of the box in the top left corner of the square
        int line0 = 3*(indexSquare/3);
        int column0 = 3*(indexSquare%3);

        do{
            // fills the box if it is empty
            if(nbGrid[line0+i/3][column0+i%3] == 0){
                fillBox(line0+i/3, column0+i%3);
                // if it doesn't find any fiting number then an error is found
                if(nbGrid[line0+i/3][column0+i%3] == 0){
                    error = true;
                }
            }
            i++;
        }while(!error && i < 9);

        return error;
    }

    public void fillBox(int indexLine, int indexColumn){
        // fills the box
        // sets 0 if it doesn't find any fiting number
        // increases the completion if it finds a fiting number
        boolean error = true; 
        // error == true if an error is found
        // error == false if no error have been found
        // error is initialized to true to get in the while loop
        // and to handle the case where the number is already 9
        int currentNb = nbGrid[indexLine][indexColumn];

        // loops through the numbers to test them
        while(currentNb < 9 && error){
            currentNb++;    // try with an other number
            error = false;  // the current number hasn't been tested yet

            // loops through the boxes of the line and the column
            int i = 0;
            do{
                // check if the number is already in the line or the column
                if(nbGrid[indexLine][i] == currentNb || nbGrid[i][indexColumn] == currentNb){
                    error = true;
                }             
                i++;
            }while(i < 9 && !error);

            // if the number fits the box regarding the boxes of line and the column
            if(!error){
                // sets the current coordinates to the top left corner of the square
                int currentLine = 3*(indexLine/3);      // line iterator
                int currentColumn = 3*(indexColumn/3);  // column iterator
                int indexLineMax = currentLine+3;       // limite of the line iterator
                int indexColumnMax = currentColumn+3;   // limite of the column iterator

                // loops through the lines of the square
                while(currentLine < indexLineMax && !error){
                    if(currentLine != indexLine){
                        // in case the line is the box' one, it has already been checked
                        currentColumn = 3*(indexColumn/3); // reinitializes the iterator
                        // loops through the columns of the square
                        while(currentColumn < indexColumnMax && !error){
                            // checks if the current box has the same number as the one we are testing
                            if(nbGrid[currentLine][currentColumn] == currentNb){
                                error = true;
                            }
                            currentColumn++;
                        }
                    }
                    currentLine++;
                }
            }
        }

        // in case the number was already 9,
        // error == true because it has been initialized to true

        // in case the number wasn't 9 but the loops return 9
        // if error == true, the loops gave us 9 and it doesn't fit the box
        // if error == false, the loops gave us 9 and it fits the box

        // the other numbers have already been tested to when we had set the 9
        // if the number is 9 when an error occurs, there isn't any other number that can fit the box

        if(currentNb == 9 && error){
            // set the number back to 0 to back up the grid because it cannot set any number
            currentNb = 0;
        }else{
            // the number fits the box
            // memorizing the coordinates of the box
            fillingLog[completion][0] = indexLine;
            fillingLog[completion][1] = indexColumn;
            // incrementing the completion after memorizing the coordinates
            // because 0 is the index of the first memory space of the tab
            completion++;
        }

        nbGrid[indexLine][indexColumn] = currentNb;
    }

    /*---------------Solving methods---------------*/
    public boolean solveGrid(){
        boolean isValid = true;
        if(validGrid()){
            // goes back and forth between filling and backing up the grid to solve it 
            fillGrid();
            while(completion != 81){
                backUpGrid();
                fillGrid();
            }
            if(!validGrid()){
                isValid = false;
            }
        }else{
            isValid = false;
        }
        if(!isValid){
            nbGrid = copyGrid(unsolvedNbGrid);
        }
        return isValid;
    }

    public void fillGrid(){
        // fills the grid based on assumptions
        // stops filling it if it realized one of its assumptions is wrong
        boolean error = false;
        int i = 0;
        BoxesGrp currentGrp;

        // loops through the groups of boxes
        do{
            currentGrp = boxesGrpSortedList[i];

            // checks if the groupe is already completed
            if(currentGrp.completion != 9){

                // fills the groupe of boxes depending on its type
                if(currentGrp.type == "row"){
                    error = fillLine(i);
                }else if(currentGrp.type == "column"){
                    error = fillColumn(i);
                }else{ // if the groupe of boxes is "square"
                    error = fillBigSquare(i);
                }
            }

            i++;
            updateBoxesGrpList();
            sortBoxesGrpSortedList();
        }while(!error && i < 27); // 27 corresponds to boxesGrpSortedList.length
    }

    public void backUpGrid(){
        // gets back to a state of the grid that is possible once that an error have occured
        boolean error;
        int currentLine;
        int currentColumn;

        // loops through the boxes that have been filled up from the last to the first
        // until it manages to fill a box
        do{
            error = false; // error == false until it manages to fill a box
            completion--; // 
            // gets the coordinates of the box to test
            currentLine = fillingLog[completion][0];
            currentColumn = fillingLog[completion][1];
            // tries filling the box
            // filling a box increases the completion if it finds a fiting number
            fillBox(currentLine, currentColumn); 
            
            // if no higher number can fit the box, fillBox sets 0 into it
            // it means that we have to back up again
            if(nbGrid[currentLine][currentColumn] == 0){
                error = true; // error detected
                // clear fillingLog's memory
                fillingLog[completion][0] = 0;
                fillingLog[completion][1] = 0;

            }
        }while(error);
    }

    /*---------------Check validity---------------*/
    public boolean validGrid(){
        boolean isValid = true;
        int i = 0;
        int j = 0;
        while(isValid && i < 9){
            j = 0;
            while(isValid && j < 9){
                isValid = validBox(i,j);
                j++;
            }
            i++;
        }
        return isValid;
    }

    public boolean validBox(int i, int j){
        boolean isValid = true;
        if(nbGrid[i][j] != 0){
            if(checkRow(i, j)){
                if(checkColumn(i, j)){
                    isValid = checkBigSquare(i,j);
                }else{
                    isValid = false;
                }
            }else{
                isValid = false;
            }
        }
        return isValid;
    }

    public boolean checkRow(int i, int j){
        boolean isValid = true;
        int k = 0;
        while(isValid && k < 9){
            if(k != j){
                if(nbGrid[i][k] == nbGrid[i][j]){
                    isValid = false;
                }
            }
            k++;
        }
        return isValid;
    }
    
    public boolean checkColumn(int i, int j){
        boolean isValid = true;
        int k = 0;
        while(isValid && k < 9){
            if(k != i){
                if(nbGrid[k][j] == nbGrid[i][j]){
                    isValid = false;
                }
            }
            k++;
        }
        return isValid;
    }

    public boolean checkBigSquare(int i, int j){
        boolean isValid = true;

        // sets the current coordinates to the top left corner of the square
        int currentLine = 3*(i/3);      // line iterator
        int currentColumn = 3*(j/3);  // column iterator
        int indexLineMax = currentLine+3;       // limite of the line iterator
        int indexColumnMax = currentColumn+3;   // limite of the column iterator

        // loops through the lines of the square
        while(isValid && currentLine < indexLineMax){
            if(currentLine != i){
                // in case the line is the box' one, it has already been checked
                currentColumn = 3*(j/3); // reinitializes the iterator
                // loops through the columns of the square
                while(currentColumn < indexColumnMax && isValid){
                    // checks if the current box has the same number as the one we are testing
                    if(nbGrid[currentLine][currentColumn] == nbGrid[i][j]){
                        isValid = false;
                    }
                    currentColumn++;
                }
            }
            currentLine++;
        }

        return isValid;
    }

    /*---------------Useful methods---------------*/
    public int [][] copyGrid(int [][] gridToCopy){
        int [][] gridCopy = new int [9][9];

        for(int i=0; i<9; i++){
            for(int j=0; j<9; j++){
                gridCopy[i][j] = gridToCopy[i][j];
            }
        }
        return gridCopy;
    }

    public String toString() {
        String res = "";
        res += "The grid :\n";

        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9;  j++){
                if(j == 2 || j == 5){
                    res += nbGrid[i][j]+"|";
                }else{
                    res += nbGrid[i][j]+" ";
                }
            }
            if(i == 2 || i == 5){
                res += "\n- - - - - - - - -";
            }
            res += "\n";
        }

        res += "boxesGrpSortedList :\n";
        for(int i = 0; i < 27 ; i++){
            res += boxesGrpSortedList[i].toString();
            res += "\n";
        }
    return res;
    }
    
}
