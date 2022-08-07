//Ayush Patel
//June 11, 2021
//AP Computer Science
package chess;

//Importing GUI and Array classes 
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
//Board class that is used as the main class where the game is run from
//Implements actionlistener so it can read the button clicks
public class Board implements ActionListener {
    //Declaring and initializing variables
    private int[] checkSquare2 = {-1,-1};
    //Creates board as a 2d square array
    private Square[][] board = new Square[8][8];
    //The panel tiles is used to hold all of the squares on the board and put them on the screen
    private JPanel tiles = new JPanel();
    private JButton reset = new JButton("Reset");
    //Creates main frame where the game runs on
    private JFrame f = new JFrame("Chess");
    //Declares and intializes pieces that the rest of the game will make copies from
    private Bishop bishopW= new Bishop("white");
    private Bishop bishopB= new Bishop("black");
    private King kingW = new King("white");
    private King kingB = new King("black");
    private Rook rookW = new Rook("white");
    private Rook rookB = new Rook("black");
    private Queen queenW = new Queen("white");
    private Queen queenB = new Queen("black");
    private Knight knightW = new Knight("white");
    private Knight knightB = new Knight("black");
    private Pawn pawnW = new Pawn("white");
    private Pawn pawnB = new Pawn("black");
    //Records turn
    private int turn =0;
    private String winner = null;
    //Keeps track of previously clicked square to track when a piece has been clicked on
    private Square previousClick = null;
    //Creates a JButton array for the pawn promotion buttons
    private JButton[] promoButtons= new JButton[4];
    private int[] checkSquare = {-1,-1};
    private JPanel promo = new JPanel();
    private JPanel winScreen = new JPanel();
    //Initializes undo and resign buttons
    private JButton resign = new JButton("Resign");
    private JButton undo = new JButton("Undo");
    //Saves the move of the piece that can be undo'd
    private int[] undoMove = {-1,-1};
    //Records the current move
    private int[] curMove = new int[2];
    //Booleans keep track if last move was short or long castle for the undo button
    private boolean shortC = false;
    private boolean longC = false;
    //Records whos turn it is to display on screen
    private JLabel turnName;
    private JLabel line;

    //Constructor for board
    public Board(){
        //Stops program if frame closes
	    f.setDefaultCloseOperation(f.EXIT_ON_CLOSE);
        //Clears layout of frame
        f.setLayout(null);
        //Sets the size and location of the frame
        f.setSize(1000, 800);
        //Gets the dimensions of the screen
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        //Location of the frame is set to the middle of the screen
        f.setLocation(dim.width/2-f.getSize().width/2, dim.height/2-f.getSize().height/2);
        //Creates a panel for the undo, reset, and resign buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1,3));
        //Sets the location of the 3 option buttons
        buttonPanel.setBounds(741,600,230,25);
        //Adds action listener for the reset button 
        reset.addActionListener(this);
        //Makes it so the boarder of the reset button doesn't show when clicked
        reset.setFocusPainted(false);
        //Adds action listener for the resign button 
        resign.addActionListener(this);
        //Makes it so the boarder of the resign button doesn't show when clicked
        resign.setFocusPainted(false);
        //Adds action listener for the udno button 
        undo.addActionListener(this);
        //Makes it so the boarder of the undo button doesn't show when clicked
        undo.setFocusPainted(false);

        //Adds option buttons to the panel 
        buttonPanel.add(reset);
        buttonPanel.add(undo);
        buttonPanel.add(resign);
        //Adds buttonpanel to the screen
        f.add(buttonPanel);
        //Creates a 8x8 grid for the tiles panel and sets its location
        tiles.setLayout(new GridLayout(8,8));
        tiles.setBounds(25,25,700,700);
        //For each grid location on the 8x8 board
        for(int i = 0; i<8; i++){
            for(int j = 0; j<8;  j++){
                //Calculates cords of grid location
                int[] cords = {i,j};
                //Uses mod for the grid locatinos to greate a checkard board pattern
                //Those that have j and i are both even or both odd...
                if((j%2 == 0 && i%2==0) || (j%2 ==1 && i%2 ==1)){
                    //It adds a tan square to the board
                    board[i][j] = new Square(cords, "tan");
                }  
                //If they are not
                else{
                    //It adds a green square to the board
                    board[i][j] = new Square(cords, "green");
                }
                //Since each Square is a JButton it sets their boarders to clear
                board[i][j].setBorderPainted(false);
                //It also removes the boarder when you click on it and adds an action listener for each square
                board[i][j].setFocusPainted(false);
                board[i][j].addActionListener(this);
                //Adds board to tiles panel
                tiles.add(board[i][j]);
            }
        }
        //Adds panel to the frame
        f.add(tiles);
        //Changes the turn name to "white to move" since white goes first 
        turnName = new JLabel("White to Move");
        //Sets font and location of turn name and adds
        turnName.setFont(new Font("Verdana", 1, 25));
        turnName.setBounds(756,25,250,100);
        //Adds a dash to make it look nicer
        line = new JLabel("-------------------");
        //Sets font and location of dash
        line.setFont(new Font("Verdana", 1, 25));
        line.setBounds(737,55,250,100);
        //Adds both turn name and dash to frame
        f.add(turnName);
        f.add(line);
        //Resets the board to that all the pieces are in their proper locations
        resetBoard();
    }
    //This method resets the board to its starting position
    public void resetBoard(){
        //First it clears the entire board by looping through each square and..
        for(int row = 0; row<8; row++){
            for(int col = 0; col<8;  col++){
                //Setting the Piece on the square and its icon to null
                board[row][col].setIcon(null);
                board[row][col].setPiece(null);
            }
        }
        //Adds the black rooks to the board by 
        //Adding their icons to their corresponding tiles 
        //And adding a new instance of them to the tiles
        board[0][0].setIcon(rookB.getIcon());
        board[0][0].setPiece(rookB.newInstance());
        board[0][7].setIcon(rookB.getIcon());
        board[0][7].setPiece(rookB.newInstance());
        //Adds the black knights to the board by 
        //Adding their icons to their corresponding tiles 
        //And adding a new instance of them to the tiles
        board[0][1].setIcon(knightB.getIcon());
        board[0][1].setPiece(knightB.newInstance());
        board[0][6].setIcon(knightB.getIcon());
        board[0][6].setPiece(knightB.newInstance());
        //Adds the black bishops to the board by 
        //Adding their icons to their corresponding tiles 
        //And adding a new instance of them to the tiles
        board[0][2].setIcon(bishopB.getIcon());
        board[0][2].setPiece(bishopB.newInstance());
        board[0][5].setIcon(bishopB.getIcon());
        board[0][5].setPiece(bishopB.newInstance());
        //Adds the black queen to the board by 
        //Adding its icon to its corresponding tile 
        //And adding a new instance of it to the tile
        board[0][3].setIcon(queenB.getIcon());
        board[0][3].setPiece(queenB.newInstance());
        //Adds the black king to the board by 
        //Adding its icon to its corresponding tile 
        //And adding a new instance of it to the tile
        board[0][4].setIcon(kingB.getIcon());
        board[0][4].setPiece(kingB.newInstance());

        //Adds the white rooks to the board by 
        //Adding their icons to their corresponding tiles 
        //And adding a new instance of them to the tiles
        board[7][0].setIcon(rookW.getIcon());
        board[7][0].setPiece(rookW.newInstance());
        board[7][7].setIcon(rookW.getIcon());
        board[7][7].setPiece(rookW.newInstance());
        //Adds the white knights to the board by 
        //Adding their icons to their corresponding tiles 
        //And adding a new instance of them to the tiles
        board[7][1].setIcon(knightW.getIcon());
        board[7][1].setPiece(knightW.newInstance());
        board[7][6].setIcon(knightW.getIcon());
        board[7][6].setPiece(knightW.newInstance());
        //Adds the white bishops to the board by 
        //Adding their icons to their corresponding tiles 
        //And adding a new instance of them to the tiles
        board[7][2].setIcon(bishopW.getIcon());
        board[7][2].setPiece(bishopW.newInstance());
        board[7][5].setIcon(bishopW.getIcon());
        board[7][5].setPiece(bishopW.newInstance());
        //Adds the white queen to the board by 
        //Adding its icon to its corresponding tile 
        //And adding a new instance of it to the tile
        board[7][3].setIcon(queenW.getIcon());
        board[7][3].setPiece(queenW.newInstance());
        //Adds the white king to the board by 
        //Adding its icon to its corresponding tile 
        //And adding a new instance of it to the tile
        board[7][4].setIcon(kingW.getIcon());
        board[7][4].setPiece(kingW.newInstance());

        //Loops through collums of board
        for(int i = 0; i<8;i++){
            //Sets the icon of the first row of the board to a black pawn
            board[1][i].setIcon(pawnB.getIcon());
            //Adds a new instance of black pawn on the first row of the board
            board[1][i].setPiece(pawnB.newInstance());
            //Sets the icon of the sixth row of the board to a white pawn
            board[6][i].setIcon(pawnW.getIcon());
            //Adds a new instance of white pawn on the sixth row of the board
            board[6][i].setPiece(pawnW.newInstance());
        }
        //Sets turn back to 0 and winner back to null
        turn = 0;
        winner = null;
        f.remove(turnName);
        //Changes the turn name to "white to move" since white goes first 
        turnName = new JLabel("White to Move");
        //Sets font and location of turn name and adds
        turnName.setFont(new Font("Verdana", 1, 25));
        turnName.setBounds(756,25,250,100);
        //Adds both turn name and dash to frame
        f.add(turnName);
        //Makes frame visibile
        f.setVisible(true);
        //Enables the board if it was disabled
        boardEnable(true);
        f.repaint();
    }
    //Calcualtes all possible legal moves for the pieces of a specific color on a specific board 
    public ArrayList<int[]> legalMovesColor(String color, Square[][] board1){
        //Creates an array list of legal moves for a team
        ArrayList<int[]> legalMoves = new ArrayList<int[]>();
        //loops through all tiles on the board
        for(int row = 0; row<8; row++){
            for(int col = 0; col<8;  col++){
                //Checks if there is a peice on the board and if there in than it checks that its color is equal to the color of the team we are getting the moves for
                if(board1[row][col].getPiece()!= null && board1[row][col].getPiece().getColor().equals(color)){
                    //If it is, then we use the calcualte the legal moves for the piece on that board
                    board1[row][col].getPiece().calculateMoves(board1, board1[row][col], turn);
                    //For the moves in legal moves for that piece
                    for(int[] move :board1[row][col].getPiece().getLegalMoves()){
                        //We add them to the list of total legal moves for that color
                        legalMoves.add(move);
                    }
                    //Clears the legal move list for the piece so that it goes back to being empty
                    board1[row][col].getPiece().setLegalMoves(new ArrayList<int[]>());
                }
            }
        }
        //returns the total list of legal moves for every piece on the board
        return legalMoves;
    }
    //Determines if a team is in check on a specific board
    public boolean check(String color, Square[][] board1){
        //Creates an int array for the cordinates of the king 
        int[] kingCords = new int[2];
        ArrayList<int[]> legalMoves = new ArrayList<int[]>();
        //Loops through tiles on board
        for(int row = 0; row<8; row++){
            for(int col = 0; col<8;  col++){
                //If the tile is a king it is the same color as the team we are checking
                if(board1[row][col].getPiece() instanceof King && board1[row][col].getPiece().getColor().equals(color)){
                    //It recordsthe cordinates of the king
                    kingCords = board1[row][col].getCords();
                }
            }
        }
        //If the color of the team we are seeing is in check is black
        if(color.equals("black")){
            //We calculate all the legal moves for white
            legalMoves = legalMovesColor("white", board1);
        }
        //If the color of the team we are seeing is in check is white
        else{
            //We calculate all the legal moves for black
            legalMoves = legalMovesColor("black", board1);
        }
        //We now loop through all the moves in legalMoves
        for(int[] move: legalMoves){
            //And see if the legalMoves for the opposite team intersect our king
            if(Arrays.equals(move, kingCords)){
                //If they do, it records the kings checked location
                checkSquare = kingCords;
                //And returns true
                return true;
            }
        }
        //If they are not the same, then it returns false, the king is not in check
        return false;
    }
    //Calcualtes whether either team is checkmated or stalemated
    public void checkMate(){
        //Declares and initializes a copy for the current board
        Square[][] temp = new Square[8][8];
        //Loops through the tiles on the board
        for(int row = 0; row<8; row++){
            for(int col = 0; col<8;  col++){
                //Sets the temp copy equal to a newInstance of the tile on the board
                temp[row][col] = board[row][col].newInstance();
            }
        }
        //If its black's turn
        if(turn %2 == 1){
            //Loops through tiles on temp board
            for(int row = 0; row<8; row++){
                for(int col = 0; col<8;  col++){
                    //Checks if the tile has a piece on it and if it is a black piece
                    if(temp[row][col].getPiece()!= null && temp[row][col].getPiece().getColor().equals("black")){
                        //If so, it calculates the moves for that piece
                        temp[row][col].getPiece().calculateMoves(temp, temp[row][col], turn);
                        //Loops through all legal moves for the piece
                        for(int[] moves: temp[row][col].getPiece().getLegalMoves()){
                            //Moves the piece to its legal move
                            temp[moves[0]][moves[1]].setPiece(temp[row][col].getPiece());
                            //Clears the piece from its old position
                            temp[row][col].setPiece(null);
                            //Checks to see if the king will not be in check after the movement
                            if(!moveCheck(moves,temp[row][col].getCords(), board)){
                                //If so it ends the method because the player still has a legal move
                                return;
                            }
                            //makes temp board back to its original state before the move 
                            temp[row][col].setPiece(temp[moves[0]][moves[1]].getPiece());
                            temp[moves[0]][moves[1]].setPiece(null);
                        }
                        //Clears legal moves for the piece on temp board
                        temp[row][col].getPiece().setLegalMoves(new ArrayList<int[]>());
                    }
                }
            }
            //If the player is in check after there is no more legal moves for it, it sets the winner equal to black
            if(check("black", board)){
                winner = "white";
            }
            //If the winner is not in check its a stalemate because there are no more legal moves
            else{
                winner = "both";
            }
        }
        //If its white's turn
        if(turn %2 == 0){
            //Loops through tiles on temp board
            for(int row = 0; row<8; row++){
                for(int col = 0; col<8;  col++){
                    //Checks if the tile has a piece on it and if it is a white piece
                    if(temp[row][col].getPiece()!= null && temp[row][col].getPiece().getColor().equals("white")){
                        //If so, it calculates the moves for that piece
                        temp[row][col].getPiece().calculateMoves(temp, temp[row][col], turn);
                        //Loops through all legal moves for the piece
                        for(int[] moves: temp[row][col].getPiece().getLegalMoves()){
                            //Moves the piece to its legal move
                            temp[moves[0]][moves[1]].setPiece(temp[row][col].getPiece());
                            //Clears the piece from its old position
                            temp[row][col].setPiece(null);
                            //Checks to see if the king will not be in check after the movement
                            if(!moveCheck(moves,temp[row][col].getCords(), board)){
                                //If so it ends the method because the player still has a legal move
                                return;
                            }
                            //makes temp board back to its original state before the move 
                            temp[row][col].setPiece(temp[moves[0]][moves[1]].getPiece());
                            temp[moves[0]][moves[1]].setPiece(null);
                        }
                        //Clears legal moves for the piece on temp board
                        temp[row][col].getPiece().setLegalMoves(new ArrayList<int[]>());
                    }
                }
            }
            //If the player is in check after there is no more legal moves for it, it sets the winner equal to black
            if(check("white", board)){
                winner = "black";
            }
            //If the winner is not in check its a stalemate because there are no more legal moves
            else{
                winner = "both";
            }
        }

    }
    //Checks if the king will be in check after a piece is moved
    public boolean moveCheck(int[] currCords, int[] prevCords, Square[][] board1){
        //Declares and initializes a copy for the current board
        Square[][] temp = new Square[8][8];
        //Loops through the tiles on the board
        for(int row = 0; row<8; row++){
            for(int col = 0; col<8;  col++){
                //Sets the temp copy equal to a newInstance of the tile on the board
                temp[row][col] = board1[row][col].newInstance();
            }
        }
        //Moves the piece to the desired location on the temp booard
        temp[currCords[0]][currCords[1]].setPiece(temp[prevCords[0]][prevCords[1]].getPiece());
        //Clears piece from previous location on temp board
        temp[prevCords[0]][prevCords[1]].setPiece(null);
        //Gets the color of the piece that was moved
        String color = temp[currCords[0]][currCords[1]].getPiece().getColor();
        boolean moveCheck = false;
        //Sees if the king is in check on the new board which is after the move happened
        moveCheck = check(color, temp);
        //Returns true if it is, false if its not
        return moveCheck;

    }
    //Runs everytime a button is clicked
    public void actionPerformed(ActionEvent e){
        //Runs to make sure the player is not in checkmate 
        checkMate();
        //Booleans to make sure source is not a square first
        boolean boardActive = true;
        boolean button = true;
        boolean undoTrue = true;
        int promoIndex =0;
        //If the button was the reset button
        if(e.getSource() == reset){
            //It resets the board
            resetBoard();
            //Sets button boolean to false, telling code its not a square
            button = false;
        }
        //If button was resign button
        if(e.getSource() == resign){
            //Checks what teams turn it is
            if(turn%2 == 0){
                //Sets winner to that team
                winner = "black";
            }
            if(turn%2 == 1){
                //Sets winner to that team 
                winner = "white";
            }
            //Runs the end screen for that team
            endScreen();
            //Sets button boolean to false, telling code its not a square
            button = false;
        }
        //If button was undo button
        if(e.getSource() == undo){
            //Sets undoTrue to false to not run the win screen when pressing the undo button after there is a winner
            undoTrue = false;
            //there is no winner, it checks to see if there is a move in undo move (not first move of the game) and if the curMove has a piece on it
            if(winner==null && undoMove[0]!=-1 && board[curMove[0]][curMove[1]].getPiece()!=null){
                //Sets the board at the undoMove location equal to the tile at the current move location
                board[undoMove[0]][undoMove[1]].setPiece(board[curMove[0]][curMove[1]].getPiece());
                board[undoMove[0]][undoMove[1]].setIcon(board[curMove[0]][curMove[1]].getIcon());
                //Clears the current move tile
                board[curMove[0]][curMove[1]].setPiece(null);
                board[curMove[0]][curMove[1]].setIcon(null);
                //Decrements the turn
                turn--;
                //Removes the turn name and line from the screen
                f.remove(turnName);
                f.repaint();
                //Redraws the turn name with the decremented turn
                if(turn%2==0){
                    //Creates white turn name
                    turnName = new JLabel("White to Move");
                    turnName.setFont(new Font("Verdana", 1, 25));
                }
                if(turn%2 == 1){
                    //Creates black turn name
                    turnName = new JLabel("Black to Move");
                    turnName.setFont(new Font("Verdana", 1, 25));
                }
                //Readds the turn name to the screen 
                turnName.setBounds(756,25,250,100);
                f.add(turnName);
                //Checks if the piece jsut moved (hasMovedInt = 1)
                if(board[undoMove[0]][undoMove[1]].getPiece().getHasMovedInt()==1){
                    //If so it sets has moved to false and has moved in to 0
                    board[undoMove[0]][undoMove[1]].getPiece().setHasMoved(false);
                    board[undoMove[0]][undoMove[1]].getPiece().setHasMovedInt(0);
                }
                //Sets it for castling row
                int cast =-1;
                //Checks to see if short castle is true
                if(shortC){
                    //If it is, it gets the color of the piece in the undo move location
                    if(board[undoMove[0]][undoMove[1]].getPiece().getColor().equals("white")){
                        //sets row equal to the row of the rook of that color (white)
                        cast =7;
                    }
                    else{
                        //sets row equal to row of the rook of that color (black)
                        cast = 0;
                    }
                    //Moves rook from castled location back to normal location
                    board[cast][7].setPiece(board[cast][5].getPiece());
                    board[cast][7].setIcon(board[cast][5].getPiece().getIcon());
                    board[cast][7].getPiece().setHasMoved(false);
                    //Clears rook from castled location 
                    board[cast][5].setPiece(null);
                    board[cast][5].setIcon(null);
                }
                //Checks to see if short castle is true
                if(longC){
                    //If it is, it gets the color of the piece in the undo move location
                    if(board[undoMove[0]][undoMove[1]].getPiece().getColor().equals("white")){
                        //sets row equal to the row of the rook of that color (white)
                        cast =7;
                    }
                    else{
                        //sets row equal to row of the rook of that color (black)
                        cast = 0;
                    }
                    //Moves rook from castled location back to normal location
                    board[cast][0].setPiece(board[cast][3].getPiece());
                    board[cast][0].setIcon(board[cast][3].getPiece().getIcon());
                    board[cast][0].getPiece().setHasMoved(false);
                    //Clears rook from castled location
                    board[cast][3].setPiece(null);
                    board[cast][3].setIcon(null);
                }

            }
            //Sets button boolean to false, telling code its not a square
            button = false;
        }
        //If there is no winner
        if(winner ==null){
            //Loops through buttons on promotion pannel
            for(JButton promo: promoButtons){
                //If the button comes from the pomopanel
                if(promo == e.getSource()){
                    //It loops thorugh coloum of board
                    for(int col = 0; col<8;  col++){ 
                        //Finds the pawn if its white
                        if(board[7][col].getPiece() instanceof Pawn){
                            //Promotes the pawn
                            promote(board[7][col], promoIndex);
                            //Deactivates the board code from running
                            boardActive = false;
                        }
                        //Finds the pawn if its black
                        if(board[0][col].getPiece() instanceof Pawn){
                            //Promotes the pawn
                            promote(board[0][col], promoIndex);
                            //Deactivates the board code from running
                            boardActive = false;
                        }

                    }
                }
                //Incriments promotion index
                promoIndex++;
            }
            //If the previous click was null and the board has not been deactivated
            if(previousClick == null && boardActive && button){
                //Creates a square object from the previous click 
                previousClick = (Square) e.getSource();
                //Makes a new instance of the square object
                previousClick = previousClick.newInstance();
                //Checks to see if the square clicked on was empty, if so it resets previous click to null
                if(previousClick.getPiece()==null){
                    previousClick = null;
                }
                //If the square wasn't empty, that means that the previous click now has a square with a piece on it
                else{
                    //It calculates the legal moves for the previously clicked piece 
                    previousClick.getPiece().calculateMoves(board, previousClick, turn);
                    //It loops through the legal moves of the previously clicked piece
                    for(int[] move:previousClick.getPiece().getLegalMoves()){
                        //It then checks to see if its the proper teams turn and the move does not place the king in check
                        if(((turn%2 == 0 && previousClick.getPiece().getColor().equals("white")) ||(turn%2==1 && previousClick.getPiece().getColor().equals("black"))) && !moveCheck(board[move[0]][move[1]].getCords(), previousClick.getCords(),board)){
                            //If all thse conditions are met, then it checks to see if there is not a piece on the tile
                            if(board[move[0]][move[1]].getPiece() == null){
                                //If not it overlays a picture on the tile to show the legal moves
                                ImageIcon circle = new ImageIcon("img/circle.png");
                                circle = new ImageIcon(circle.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
                                board[move[0]][move[1]].setIcon(circle);
                            }
                            //If there is
                            else{
                                //It draws the version of the piece with the circle on it
                                String path = board[move[0]][move[1]].getPiece().getImgPath();
                                ImageIcon circlePiece = new ImageIcon(path.substring(0,path.length()-4)+"2"+".png");
                                //Shows the legal capture that the previous piece can make
                                circlePiece = new ImageIcon(circlePiece.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH));
                                board[move[0]][move[1]].setIcon(circlePiece);
                            }
                        }
                    }
                    //clears the legal moves for the piece 
                    previousClick.getPiece().setLegalMoves(new ArrayList<int[]>());
                    
                }
            }
            //If the previous click is not null and the board is active (this means a piece was clicked on)
            else if (previousClick!= null && boardActive && button){
                //It checks to see if the new click was a legal move for the piece
                boolean legalMove = false;
                //Loops through tiles on board
                for(int row = 0; row<8; row++){
                    for(int col = 0; col<8;  col++){
                        //Checks to see if tile was the source of the click
                        if(board[row][col]==e.getSource()){
                            //if it was, it calculates the legal moves for the piece clicked on 
                            previousClick.getPiece().calculateMoves(board, previousClick, turn);
                            //It then checks if its the proper teams turn
                            if((turn%2 == 0 && previousClick.getPiece().getColor().equals("white")) ||(turn%2==1 && previousClick.getPiece().getColor().equals("black"))){
                                //If all these conditions are met, it then loops through the legal moves for the piece
                                for(int move = 0; move<previousClick.getPiece().getLegalMoves().size(); move++){
                                    //It checks if the clicked on square is a legal move for the piece and that by moving the piece does not place the king in check
                                    if(Arrays.equals(board[row][col].getCords(), previousClick.getPiece().getLegalMoves().get(move)) && !moveCheck(board[row][col].getCords(), previousClick.getCords(),board)){
                                        //Lastly, if all these conditions are true, it sets has move to true for the piece 
                                        previousClick.getPiece().setHasMoved(true);
                                        //Incriments the hasmoved int
                                        previousClick.getPiece().incrHasMovedInt();
                                        //Sets the icon of the current tile to that of the previous tile
                                        board[row][col].setIcon(previousClick.getPiece().getIcon());
                                        //Clears the legal moves of the piece 
                                        previousClick.getPiece().setLegalMoves(new ArrayList<int[]>());
                                        previousClick.getPiece().setLastMove(previousClick.getCords());
                                        //Sets tpiece of the current tile to that of the previouss tile
                                        board[row][col].setPiece(previousClick.getPiece());
                                        board[row][col].getPiece().checkEnPassant(board[row][col], turn);
                                        //sets the current move equal to the current clicked on tile
                                        curMove[0]=row;
                                        curMove[1]=col;
                                        //Sets the undo move to the previously clicked on tile
                                        undoMove=previousClick.getCords();
                                        //Sets legal moves to true and short and long castle to false
                                        legalMove = true;
                                        shortC = false;
                                        longC = false;
                                        //Incriments turn
                                        turn++;
                                        //Removes turnname from screen
                                        f.remove(turnName);
                                        f.repaint();
                                        //Changes it to the more current turn name
                                        if(turn%2==0){
                                            //White if its white's turn
                                            turnName = new JLabel("White to Move");
                                            turnName.setFont(new Font("Verdana", 1, 25));
                                        }
                                        if(turn%2 == 1){
                                            //Black if its black's turn
                                            turnName = new JLabel("Black to Move");
                                            turnName.setFont(new Font("Verdana", 1, 25));
                                        }
                                        //Re adds turn name to the screen
                                        turnName.setBounds(756,25,250,100);
                                        f.add(turnName);
                                        //Checks to see if the moved piece was a pawn and if its on the promotion row
                                        if(board[row][col].getPiece() instanceof Pawn && (board[row][col].getCords()[0]==7 || board[row][col].getCords()[0]==0)){
                                            //If so, it brings up the promotion panel for the color of the pawn
                                            promotionPanel(board[row][col].getPiece().getColor());
                                        }
                                        //Checks to see if the piece enpassanted
                                        if(board[row][col].getPiece().getEnPasCords()!= null && Arrays.equals(board[row][col].getPiece().getEnPasCords(),board[row][col].getCords())){
                                            //If it did, it checks to see if the piece was black
                                            if(board[row][col].getPiece().getColor().equals("black")){
                                                //If so if the piece of the row below it was white
                                                if(board[row-1][col].getPiece().getColor().equals("white")){
                                                    //Then it deletes the piece below it 
                                                    board[row-1][col].setIcon(null);
                                                    board[row-1][col].setPiece(null);
                                                }

                                            }
                                            //If it did and the piece was white
                                            else{
                                                //It checks the piece of the row above it to see if its black
                                                if(board[row+1][col].getPiece().getColor().equals("black")){
                                                    //If so it deletes the piece above it 
                                                    board[row+1][col].setIcon(null);
                                                    board[row+1][col].setPiece(null);
                                                }
                                            }
                                        }
                                        //If the piece short castled
                                        if(Arrays.equals(board[row][col].getPiece().getShortCastleCords(),board[row][col].getCords())){
                                            //It sets short castle to true for the undo
                                            shortC = true;
                                            //If the piece was black
                                            if(board[row][col].getPiece().getColor().equals("black")){
                                                //It moves the rook to the short castle location
                                                board[0][5].setIcon(board[0][7].getPiece().getIcon());
                                                board[0][5].setPiece(board[0][7].getPiece());
                                                //And clears the old rook from the original loaction
                                                board[0][7].setIcon(null);
                                                board[0][7].setPiece(null);

                                            }
                                            //If the piece was white
                                            else{
                                                //It moves the rook to the short castle location
                                                board[7][5].setIcon(board[7][7].getPiece().getIcon());
                                                board[7][5].setPiece(board[7][7].getPiece());
                                                //And clears the old rook from the original loaction
                                                board[7][7].setIcon(null);
                                                board[7][7].setPiece(null);
                                            }
                                            //It resets the short castle cords for the piece
                                            int reset[] = {-1,-1};
                                            board[row][col].getPiece().setShortCastleCords(reset);
                                        }
                                        //If the piece long castled
                                        if(Arrays.equals(board[row][col].getPiece().getLongCastleCords(),board[row][col].getCords())){
                                            //It sets short castle to true for the undo
                                            longC = true;
                                            //If the piece was black
                                            if(board[row][col].getPiece().getColor().equals("black")){
                                                //It moves the rook to the long castle location
                                                board[0][3].setIcon(board[0][0].getPiece().getIcon());
                                                board[0][3].setPiece(board[0][0].getPiece());
                                                //And clears the old rook from the original loaction
                                                board[0][0].setIcon(null);
                                                board[0][0].setPiece(null);
                                            }
                                            //If the piece was white
                                            else{
                                                //It moves the rook to the long castle location
                                                board[7][3].setIcon(board[7][0].getPiece().getIcon());
                                                board[7][3].setPiece(board[7][0].getPiece());
                                                //And clears the old rook from the original loaction
                                                board[7][0].setIcon(null);
                                                board[7][0].setPiece(null);
                                            }
                                            //It resets the short castle cords for the piece
                                            int reset[] = {-1,-1};
                                            board[row][col].getPiece().setLongCastleCords(reset);
                                        }
                                    
                                    }
                                }
                            }
                        }
                        //if the clicked location was null it sets the icon of the location to null
                        if(board[row][col].getPiece()==null){
                            board[row][col].setIcon(null);
                        }
                        //If the tile piece was not null, then it sets the icon of the tile of the moved piece 
                        else{
                            board[row][col].setIcon(board[row][col].getPiece().getIcon());
                        }
                    }
                }
                //For the tiles on the baord
                for(int row = 0; row<8; row++){
                    for(int col = 0; col<8;  col++){
                        //If the same square was clicked on, and it was a legal move
                        if(board[row][col].getCords() == previousClick.getCords() && legalMove == true){
                            //Clears the square that was clicked on
                            board[row][col].setIcon(null);
                            board[row][col].setPiece(null);
                        }
                    }
                }
                //Resets previous click to null
                previousClick = null;

            }
            //If black is in check
            if(check("black", board)){
                //It checks to see if the color of the board equals green
                if(board[checkSquare[0]][checkSquare[1]].getColor().equals("green")){
                    //If so it colors the board a shade of red
                    board[checkSquare[0]][checkSquare[1]].setBackground(new Color(212,108,81));
                }
                //If the tile was tan
                else{
                    //It colors the tile a different shade of red
                    board[checkSquare[0]][checkSquare[1]].setBackground(new Color(236,126,106));
                }
                //Sets the checked square
                checkSquare2 = checkSquare;
            }
            //If white is in check
            else if(check("white", board)){
                //It checks to see if the color of the board equals green
                if(board[checkSquare[0]][checkSquare[1]].getColor().equals("green")){
                    //If so it colors the board a shade of red
                        board[checkSquare[0]][checkSquare[1]].setBackground(new Color(212,108,81));
                    }
                    //If the tile was tan
                    else{
                        //It colors the tile a different shade of red
                        board[checkSquare[0]][checkSquare[1]].setBackground(new Color(236,126,106));
                    }
                    //Sets the checked square
                    checkSquare2 = checkSquare;

            }
            //If there was a checked square
            else if(checkSquare2[0] != -1){
                //Resets the color of the square to green if it was green
                if(board[checkSquare2[0]][checkSquare2[1]].getColor().equals("green")){
                    board[checkSquare2[0]][checkSquare2[1]].setBackground(new Color(118,150,86));
                }
                //Resets color of the square to tan if it was tan
                else{
                    board[checkSquare2[0]][checkSquare2[1]].setBackground(new Color(238, 238, 210));
                } 
            }
            //Checks to see if king is in checkmate after move
            checkMate();
        }
        //If there was a winner and the undo button wasn't clicked
        if(winner !=  null && undoTrue){
            //It shows the endScreen
            endScreen();
        }
    
    }
    //Displays the endscreen
    public void endScreen(){
        //Disables the board
        boardEnable(false);
        //If the winner was black it displays the black winscreen
        if(winner.equals("black")){
            //Creates a popup win screen of the correct color
            PopUpFrame win = new PopUpFrame("Black");
        }
        //If the winner was white it displays the white winscreen
        else if(winner.equals("white")){
            //Creates a popup win screen of the correct color
            PopUpFrame win = new PopUpFrame("White");
        }
        //If the winner was both it displays the both winscreen
        else{
            //Creates a popup win screen for stalemate
            PopUpFrame win = new PopUpFrame("both");

        }
    }
    // Enables/Disables the board
    public void boardEnable(boolean enable){
        //Loops through tiles on board
        for(int row = 0; row<8; row++){
            for(int col = 0; col<8;  col++){
                //If they are not empty
                if(board[row][col].getPiece()!=null){
                    //It sets their disabled icon so that it doenst grey out
                    board[row][col].setDisabledIcon(board[row][col].getPiece().getIcon());
                }
                //It disables/enables them
                board[row][col].setEnabled(enable);
            }
        }
    }
    //Prmotes a pawn
    public void promote(Square tile, int idx){
        //Disables the board while the pawn is being promoted
        boardEnable(true);
        //Checks to see if peice is black
        if(board[tile.getCords()[0]][tile.getCords()[1]].getPiece().getColor().equals("black")){
            //If so it checks to see if the button that was clicked on was idx 0
            if(idx == 0){
                //If so it promotes to a black rook
                board[tile.getCords()[0]][tile.getCords()[1]].setPiece(rookB.newInstance());
                board[tile.getCords()[0]][tile.getCords()[1]].setIcon(rookB.getIcon());

            }
            //If so it checks to see if the button that was clicked on was idx 1
            else if(idx ==1){
                //If so it promotes to a black knight
                board[tile.getCords()[0]][tile.getCords()[1]].setPiece(knightB.newInstance());
                board[tile.getCords()[0]][tile.getCords()[1]].setIcon(knightB.getIcon());

            }
            //If so it checks to see if the button that was clicked on was idx 2
            else if(idx ==2){
                //If so it promotes to a black bishop
                board[tile.getCords()[0]][tile.getCords()[1]].setPiece(bishopB.newInstance());
                board[tile.getCords()[0]][tile.getCords()[1]].setIcon(bishopB.getIcon());

            }
            //If so it checks to see if the button that was clicked on was idx 3
            else if(idx ==3){
                //If so it promotes to a black queen 
                board[tile.getCords()[0]][tile.getCords()[1]].setPiece(queenB.newInstance());
                board[tile.getCords()[0]][tile.getCords()[1]].setIcon(queenB.getIcon());

            }
        }
        //If the pawn was white
        else{
            //If so it checks to see if the button that was clicked on was idx 0
            if(idx == 0){
                //If so it promotes to a white rook
                board[tile.getCords()[0]][tile.getCords()[1]].setPiece(rookW.newInstance());
                board[tile.getCords()[0]][tile.getCords()[1]].setIcon(rookW.getIcon());

                
            }
            //If so it checks to see if the button that was clicked on was idx 1
            else if(idx ==1){
                //If so it promotes to a white knight
                board[tile.getCords()[0]][tile.getCords()[1]].setPiece(knightW.newInstance());
                board[tile.getCords()[0]][tile.getCords()[1]].setIcon(knightW.getIcon());

            }
            //If so it checks to see if the button that was clicked on was idx 2
            else if(idx ==2){
                //If so it promotes to a white bishop
                board[tile.getCords()[0]][tile.getCords()[1]].setPiece(bishopW.newInstance());
                board[tile.getCords()[0]][tile.getCords()[1]].setIcon(bishopW.getIcon());
            }
            //If so it checks to see if the button that was clicked on was idx 3
            else if(idx ==3){
                //If so it promotes to a white queen
                board[tile.getCords()[0]][tile.getCords()[1]].setPiece(queenW.newInstance());
                board[tile.getCords()[0]][tile.getCords()[1]].setIcon(queenW.getIcon());

            }
        }
        //Removes the promotion panel from the screen
        f.remove(promo);
        f.repaint();
    }
    //Creates the promotion panel
    public void promotionPanel(String color){
        //Disables the board
        boardEnable(false);
        //Creates a 4 by 1 promotion panel
        promo.setLayout(new GridLayout(4,1));
        promo.removeAll();
        //Sets location of panel
        promo.setBounds(725,275,75,200);
        //Loops through promotion buttons array
        for(int i = 0; i< promoButtons.length; i++){
            //Adds a promobutton to each location in the array 
            promoButtons[i] = new JButton();
            //Sets the color of the promotion panel as well as removes clicked boarder
            promoButtons[i].setFocusPainted(false);
            promoButtons[i].setBackground(new Color(255,255,255));
            //Adds action listener for each button
            promoButtons[i].addActionListener(this);

        }
        //If the color is black
        if(color.equals("black")){
            //Sets icons to black piece icons for buttons on panel
            promoButtons[0].setIcon(rookB.getScaledIcon(50));
            promoButtons[1].setIcon(knightB.getScaledIcon(50));
            promoButtons[2].setIcon(bishopB.getScaledIcon(50));
            promoButtons[3].setIcon(queenB.getScaledIcon(50));
        }
        //If the color is white
        else{
            //Sets icons to white piece icons for buttons on panel
            promoButtons[0].setIcon(rookW.getScaledIcon(50));
            promoButtons[1].setIcon(knightW.getScaledIcon(50));
            promoButtons[2].setIcon(bishopW.getScaledIcon(50));
            promoButtons[3].setIcon(queenW.getScaledIcon(50));  
        }
        //For buttons in promobuttons array
        for(int i = 0; i< promoButtons.length; i++){
            //Adds them to promo panel
            promo.add(promoButtons[i]);
        }
        //Adds promo panel to frame
        f.add(promo);
    }
}
