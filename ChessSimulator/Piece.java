//Ayush Patel
//June 11, 2021
//AP Computer Science
package chess;
//Importing GUI and Array classes
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
//Class for a piece that is used to create all the pieces in the game
public class Piece{
    //Declaring and Inintializing variables for each piece
    private String color;
    private String name;
    private ImageIcon icon;
    private ArrayList<int[]> legalMoves;
    private int[] lastMove;
    //Used to track the poision of enPassant to see if a piece needs to be deleted behind the pawn as it moves
    private int[] enPasCords = null;
    private int[] shortCastleCords = null;
    private int[] longCastleCords = null;
    //Used to track if a pawn can be enPassanted
    private boolean enPassant = false;
    //Used to see if piece has moved to see if pawn can double move or king can castle
    private boolean hasMoved = false;
    //Used to calcualte if pawn can be enPassanted
    private int enPasTurn = -1;
    //Used for the undo button to calculate how see if a piece can be undo'd to a spot where it hasn't moved
    private int hasMovedInt = 0;

    //Constructor for Piece Object
    public Piece(String name1, String color1,ImageIcon icon1){
        color = color1;
        name = name1;
        legalMoves = new ArrayList<int[]>();
        //Scales Image Icon to fit on each tile
        icon = new ImageIcon(icon1.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH));
    }
    //Getter method for path of image
    //Used to draw circle on possible caputured pieces later
    public String getImgPath(){
        return "img/"+color+name+".png";
    }
    //Getter method for image icon
    //Used to get draw image icon on tile on square as piece moves
    public ImageIcon getIcon(){
        return icon;
    }
    //Returns scaled version of a pieces image icon
    //Used to draw icon on promotion tiles since promotion icon tiles are smaller than board tiles
    public ImageIcon getScaledIcon(int scale){
        return new ImageIcon(icon.getImage().getScaledInstance(scale, scale, Image.SCALE_SMOOTH));
    }
    //Getter method for HasMoved 
    //Used to determine if king can castle
    public boolean getHasMoved(){
        return hasMoved;
    }
    //Getter method for en passant cords 
    //Used to determine if pawn en passant'ed or not
    public int[] getEnPasCords(){
        return enPasCords;
    }
    //Getter method for short castle cords
    //Used to determine if king short castled or not
    public int[] getShortCastleCords(){
        return shortCastleCords;
    }
    //Setter method for short castle cords
    //Used to reset short castle cords
    public void setShortCastleCords(int[] set){
        shortCastleCords =set;
    }
    //getter method for long castle cords
    //Used to determine if king long castled or not
    public int[] getLongCastleCords(){
        return longCastleCords;
    }
    //Setter method for long castle cords
    //Used to reset long castle cords
    public void setLongCastleCords(int[] set){
        longCastleCords =set;
    }
    //Setter method for en passant cords
    //Used to reset en passant cords
    public void setEnPasCords(int[] set){
        enPasCords =set;
    }
    //Getter method for the piece's color
    //Used to determine what color a piece is to determine if two pieces are on the same team or not
    public String getColor(){
        return color;
    }
    //Setter method for has Moved 
    //Used to mark when piece has moved or reset piece if it is reset to its starting position
    public void setHasMoved(Boolean set){
        hasMoved = set;
    }
    //Setter method for has moved int
    //Used to rseet has moved int
    public void setHasMovedInt(int set){
        hasMovedInt=set;
    }
    //Getter method for has moved int
    //Used to see if piece will be reset to starting position
    public int getHasMovedInt(){
        return hasMovedInt;
    }
    //Used to incriment has moved int every time a piece moves
    public void incrHasMovedInt(){
        hasMovedInt++;
    }
    //Setter to store the last move of the piece
    public void setLastMove(int[] set){
        lastMove = set;
    }
    //Getter method to get the last move of the piece
    public int[] getLastMove(){
        return lastMove;
    }
    //Getter method for en passant value to see if a piece can be en passant'ed or not
    public boolean getEnPassant(){
        return enPassant;
    }
    //Getter method for the turn the piece can be en passanted
    public int getEnPasTurn(){
        return enPasTurn;
    }
    //Checks to see if a piece can be en pasanted
    //Takes in the tile of the piece and the turn it is in the game
    public void checkEnPassant(Square tile, int turn){
        //Gets the cords of the tile
        int[] cords = tile.getCords();
        //Ses an int array to get the location of two rows below the tile
        int[] lMove = new int[2];
        //Calcualtes the cords two rows behind the pawn 
        if(color.equals("black")){
            //If the pawn is black it calulates two rows above the pawn
            lMove[0]= cords[0]-2;
            lMove[1]= cords[1];
        }
        else{
            //If its white it calcualtes two rows below the pawn
            lMove[0]= cords[0]+2;
            lMove[1]= cords[1];
        }
        //Checks to see if the last move of the pawn equaled the cords of the square two rows behind it
        if(Arrays.equals(getLastMove(),lMove)){
            //If this is true that means the pawn double moved and thus it can be enPassant'ed
            enPassant=true;
            //Sets the turn this happened to the enPasTurn to determine if it has only been one turn since then
            enPasTurn = turn;
        }
    }
    //Getter method for legal moves of pawn
    //Used to determine checks and if the piece can moved to clicked tile
    public ArrayList<int[]> getLegalMoves(){
        return legalMoves;
    }
    //Setter method for legal moves
    //Used to set legal moves after removing those that put the king in check
    public void setLegalMoves(ArrayList<int[]> moves){
        legalMoves = moves;
    }
    //Empty parent class method that is overideen by each of the child objects
    public void calculateMoves(Square[][] board, Square tile, int turn){

    }
    //Ez move method is a quick move method to calcuale the legal moves for all of the pieces besides pawns
    //I was soo proud of this method cause it can work for rooks bishops queens and even knights and kings all with one method
    //It takes in a board, the location of the piece on the board, x and y incriments, and a limit on the number of moves
    public void ezMove(Square[][] board, Square tile, int incrX, int incrY, int limit){
        //Gets cords for the tile
        int[] cords = tile.getCords();
        //ArrayList that will contain all possible moves for piece
        ArrayList<int[]> allMoves = new ArrayList<int[]>();
    
        int[][] moves = new int[14][2];

        //Sets all moves to -1 becasue they start at 0 and the position 0,0 is on the board
        for (int i = 0; i < moves.length; i++){
            for (int j = 0; j < moves[i].length; j++){
                moves[i][j] = -1;
            }
        }
        //Set a limmiter to 0 that will be compared to the limit
        int limitter = 0;
        //Saves x and y incriments as they will be incrimented
        int saveX = incrX;
        int saveY = incrY;
        //Gets array index for value in moves array that is not null
        int arrayIdx = 0;
        //Incriments through tiles on board where there is no piece by subtracting values from x and y incriments
        //Upwards Horizontal and vertical movement can be calcuated by setting y to 1 and x to 0 and vise versa
        while(cords[0]-incrY >=0 && cords[1]-incrX >= 0 && board[cords[0]-incrY][cords[1]-incrX].getPiece() == null){
            //Adds incrimented tile to valid move array
            moves[arrayIdx][0] = cords[0]-incrY;
            moves[arrayIdx][1] = cords[1]-incrX;
            //Incriments index and limiter
            arrayIdx++;
            limitter++;
            //Stops if limitter exceeds limit 
            //Used to stop piece if it can only move once in that direction (king and knight)
            if(limitter>= limit){
                limitter =0;
                break;
            }
            //Only incriments x incriment if it is not default 0
            if(incrX!=0){
                incrX++;
            }
            //Only incriments y incriment if it is not default 0
            if(incrY!=0){
                incrY++;
            }
        }
        //After the loop has endend, it means the tile either is out of bounds or has a piece on it
        //If the tile is in bounds, then it checks if the piece is a different color than the current piece
        if(cords[0]-incrY >=0 && cords[1]-incrX >= 0 && diffColor(board[cords[0]-incrY][cords[1]-incrX])){
            //If it is....
            //It adds taking it as a valid move
            moves[arrayIdx][0] = cords[0] -incrY;
            moves[arrayIdx][1] = cords[1]-incrX;
            arrayIdx++;
        }
        //Resets x and y increments
        incrX = saveX;
        incrY = saveY;
        //Incriments through tiles on board where there is no piece by adding values to x and y incriments
        //Downwards Horizontal and vertical movement can be calcuated by setting y to 1 and x to 0 and vise versa
        while(cords[0]+incrY <=7 && cords[1]+incrX <= 7 && board[cords[0]+incrY][cords[1]+incrX].getPiece() == null){
            //Adds incrimented tile to valid move array
            moves[arrayIdx][0] = cords[0]+incrY;
            moves[arrayIdx][1] = cords[1]+incrX;
            //Incriments index and limiter
            arrayIdx++;
            limitter++;
            //Stops if limitter exceeds limit 
            //Used to stop piece if it can only move once in that direction (king and knight)
            if(limitter>= limit){
                limitter =0;
                break;
            }
            //Only incriments x incriment if it is not default 0
            if(incrX!=0){
                incrX++;
            }
            //Only incriments y incriment if it is not default 0
            if(incrY!=0){
                incrY++;
            }
        }
        //After the loop has endend, it means the tile either is out of bounds or has a piece on it
        //If the tile is in bounds, then it checks if the piece is a different color than the current piece
        if(cords[0]+incrY <=7 && cords[1]+incrX <= 7 && diffColor(board[cords[0]+incrY][cords[1]+incrX])){
            //If it is....
            //It adds taking it as a valid move
            moves[arrayIdx][0] = cords[0] +incrY;
            moves[arrayIdx][1] = cords[1]+incrX;
            arrayIdx++;
        }
        //Checks if incriments are both not equal to 0
        //This code only calculates non horizontal and veritcal movement
        if(incrX !=0 && incrY !=0){
            //Resets x and y incriments
            incrX = saveX;
            incrY = saveY;
            //Incriments through tiles on board where there is no piece by subtracting values from the x incriment and adding values to the y incriment
            //Leftward diagonal movement can be calcuated by setting y to 1 and x to 1 
            while(cords[0]+incrY <=7 && cords[1]-incrX >= 0 && board[cords[0]+incrY][cords[1]-incrX].getPiece() == null){
                //Adds incrimented tile to valid move array
                moves[arrayIdx][0] = cords[0]+incrY;
                moves[arrayIdx][1] = cords[1]-incrX;
                //Incriments index and limiter
                arrayIdx++;
                limitter++;
                //Stops if limitter exceeds limit 
                //Used to stop piece if it can only move once in that direction (king and knight)
                if(limitter>= limit){
                    limitter =0;
                    break;
                }
                //Only incriments x incriment if it is not default 0
                if(incrX!=0){
                    incrX++;
                }
                //Only incriments y incriment if it is not default 0
                if(incrY!=0){
                    incrY++;
                }
            }
            //After the loop has endend, it means the tile either is out of bounds or has a piece on it
            //If the tile is in bounds, then it checks if the piece is a different color than the current piece
            if(cords[0]+incrY <=7 && cords[1]-incrX >= 0 && diffColor(board[cords[0]+incrY][cords[1]-incrX])){
                //If it is....
                 //It adds taking it as a valid move
                moves[arrayIdx][0] = cords[0]+incrY;
                moves[arrayIdx][1] = cords[1]-incrX;
                arrayIdx++;
            }
            //Resets x and y incriments
            incrX = saveX;
            incrY = saveY;
            //Incriments through tiles on board where there is no piece by subtracting values from the y incriment and adding values to the x incriment
            //Rightward diagonal movement can be calcuated by setting y to 1 and x to 1 
            while(cords[0]-incrY >=0 && cords[1]+incrX <= 7 && board[cords[0]-incrY][cords[1]+incrX].getPiece() == null){
                //Adds incrimented tile to valid move array
                moves[arrayIdx][0] = cords[0]-incrY;
                moves[arrayIdx][1] = cords[1]+incrX;
                //Incriments index and limiter
                arrayIdx++;
                limitter++;
                //Stops if limitter exceeds limit 
                //Used to stop piece if it can only move once in that direction (king and knight)
                if(limitter>= limit){
                    limitter =0;
                    break;
                }
                //Only incriments x incriment if it is not default 0
                if(incrX!=0){
                    incrX++;
                }
                //Only incriments y incriment if it is not default 0
                if(incrY!=0){
                    incrY++;
                }
            }
            //After the loop has endend, it means the tile either is out of bounds or has a piece on it
            //If the tile is in bounds, then it checks if the piece is a different color than the current piece
            if(cords[0]-incrY >=0 && cords[1]+incrX <= 7 && diffColor(board[cords[0]-incrY][cords[1]+incrX])){
                //If it is....
                //It adds taking it as a valid move
                moves[arrayIdx][0] = cords[0]-incrY;
                moves[arrayIdx][1] = cords[1]+incrX;
                arrayIdx++;
            }
        }
        //Loops through values in moves and adds them to allMoves arraylist if they are not equal to -1
        for(int i = 0; i<14; i++){
            if(moves[i][0]!= -1){
                allMoves.add(moves[i]);
            }
        }
        //Adds each move from allmoves to legalMoves
        for(int[] move: allMoves){
            legalMoves.add(move);
        }
    }
    //Method to caluclate if piece is a different color from a piece on another tile
    public boolean diffColor(Square tile){
        //Checks if the piece on the tile is not null
        if(tile.getPiece() != null){
            //If it isn't it gets the color of the piece and checks if it equals the color of this current piece
            if(color.equals(tile.getPiece().getColor())){
                //If it does, it returns false that the pieces are the same color
                return false;
            }
            //If it doesn't, it returns true that they are different colors
            return true;
        }
        //If there is no piece on the tile, it returns false that they are not different colors
        return false;
    }
}
//Creates a Bishop Object that is a child of the Piece Object
class Bishop extends Piece {
    //Constructor for bishop object
    public Bishop(String color){
        //Uses parent Piece class constructor and creates an image icon using image path
        super("bishop",color,new ImageIcon("img/"+color+"bishop"+".png"));
    }
    //Variable to return copy of current Piece
    //Used to clone Pieces to place on tiles
    public Bishop newInstance(){
        Bishop clone =  new Bishop(getColor());
        return clone;
    }
    //Method that overides parent calculatemoves method 
    //Used to calculate legal moves for the bishop
    public void calculateMoves(Square[][] board, Square tile, int turn){
        //Uses ez move method to calucate diagonal moves using 1 for both x and y incriments
        ezMove(board, tile, 1, 1,9999);
    }

}
//Creates a Knight Object that is a child of the Piece Object
class Knight extends Piece {
    //Constructor for knight object
    public Knight(String color){
        //Uses parent Piece class constructor and creates an image icon using image path
        super("knight",color,new ImageIcon("img/"+color+"knight"+".png"));
    }
    //Variable to return copy of current Piece
    //Used to clone Pieces to place on tiles
    public Knight newInstance(){
        Knight clone =  new Knight(getColor());
        return clone;
    }
    //Method that overides parent calculatemoves method 
    //Used to calculate legal moves for the knight
    public void calculateMoves(Square[][] board, Square tile, int turn){
        //Uses ez move method to calucate horse moves using 2 for y incriment and 1 for x icriment with a limit of 1
        ezMove(board, tile, 2, 1,1);
        //Uses ez move method to calucate horse moves using 1 for y incriment and 2 for x icriment with a limit of 1
        ezMove(board, tile, 1, 2,1);

    }
}
//Creates a King Object that is a child of the Piece Object
class King extends Piece {
    //Constructor for king object
    public King(String color){
        //Uses parent Piece class constructor and creates an image icon using image path
        super("king",color,new ImageIcon("img/"+color+"king"+".png"));
    }
    //Variable to return copy of current Piece
    //Used to clone Pieces to place on tiles
    public King newInstance(){
        King clone =  new King(getColor());
        return clone;
    }
    //Method that overides parent calculatemoves method 
    //Used to calculate legal moves for the king
    public void calculateMoves(Square[][] board, Square tile, int turn){
        //Uses ez move method to calculate diagonal movements of king with a limit of 1
        ezMove(board, tile, 1, 1,1);
        //Uses ez move method to calculate horizontal and vertical movements of king with a limit of 1
        ezMove(board, tile, 0, 1,1);
        ezMove(board, tile, 1, 0,1);
        //Creates int arrays for short and long castle movements for king 
        int[] shortCastle = shortCastle(board, tile);
        int[] longCastle = longCastle(board, tile);
        //Gets clone arraylist of legalmoves
        ArrayList<int[]> temp = getLegalMoves();
        //Checks to see if king can short castle
        //If he can, adds short castle cords to the clone arraylist of legal moves
        if(shortCastle!=null) {
            temp.add(shortCastle);
        }
        //Checks to see if king can long castle
        //If he can, adds long castle cords to the clone arraylist of legal moves
        if(longCastle!= null){
            temp.add(longCastle);
        }
        //sets legal moves equal to the clone arraylist which now has castle cordinates in it
        setLegalMoves(temp);
    }
    //Determines if king can short castle or not and if so what cordinates
    public int[] shortCastle(Square[][]board, Square tile){
        //Sets boolean for castling to true and creates a null array for the cordinates of short castling
        boolean castle = true;
        int[] shortCas = null;
        int castleLocation;
        //If Color is equal to white it sets castlelocation to 7 (row of castling for white)
        if(getColor().equals("white")){
            castleLocation = 7;
        }
        //If Color is black it sets castlelocation to 0 (row of castling for black)
        else{
            castleLocation = 0;
        }
        //Checks to see if there is a rook in the castling location adn if it has moved yet and whether the king has moved yet
        if(board[castleLocation][7].getPiece()!= null && board[castleLocation][7].getPiece().getHasMoved() == false && getHasMoved()==false){
            //If all the conditions for castling are met, it loops through tiles between king and rook to make sure they are empty
            for(int i =5; i<7; i++){
                if(board[castleLocation][i].getPiece() != null){
                    //If they are not, it sets castling equal to false, and the king cannot short castle
                    castle= false;
                }
            }
            //If it goes through the loop without becoming false the king can short castle
            if(castle==true){
                //Calculates short castle cordinates for the king 
                shortCas = new int[2];
                shortCas[0] = castleLocation;
                shortCas[1] = 6;
                //Sets the short castle cords to the calcualated cordinates
                setShortCastleCords(shortCas);
            }
        }
        //Returns the short castle cords
        return shortCas;
    }
    //Determines if king can long castle or not and if so what cordinates
    public int[] longCastle(Square[][]board, Square tile){
        //Sets boolean for castling to true and creates a null array for the cordinates of long castling
        boolean castle = true;
        int[] longCas = null;
        int castleLocation;
        //If Color is equal to white it sets castlelocation to 7 (row of castling for white)
        if(getColor().equals("white")){
            castleLocation = 7;
        }
        //If Color is black it sets castlelocation to 0 (row of castling for black)
        else{
            castleLocation = 0;
        }
        //Checks to see if there is a rook in the castling location adn if it has moved yet and whether the king has moved yet
        if(board[castleLocation][0].getPiece()!= null && board[castleLocation][0].getPiece().getHasMoved() == false && getHasMoved()==false){
            //If all the conditions for castling are met, it loops through tiles between king and rook to make sure they are empty
            for(int i =3; i>0; i--){
                if(board[castleLocation][i].getPiece() != null){
                    //If they are not, it sets castling equal to false, and the king cannot long castle
                    castle= false;
                }
            }
            //If it goes through the loop without becoming false the king can long castle
            if(castle==true){
                //Calculates long castle cordinates for the king 
                longCas = new int[2];
                longCas[0] = castleLocation;
                longCas[1] = 2;
                //Sets the long castle cords to the calcualated cordinates
                setLongCastleCords(longCas);
            }
        }
        //Returns the short castle cords
        return longCas;
    }
}
//Creates a Queen Object that is a child of the Piece Object
class Queen extends Piece {
    //Constructor for queen object
    public Queen(String color){
        //Uses parent Piece class constructor and creates an image icon using image path
        super("queen",color,new ImageIcon("img/"+color+"queen"+".png"));
    }
    //Variable to return copy of current Piece
    //Used to clone Pieces to place on tiles
    public Queen newInstance(){
        Queen clone =  new Queen(getColor());
        return clone;
    }
    //Method that overides parent calculatemoves method 
    //Used to calculate legal moves for the queen
    public void calculateMoves(Square[][] board, Square tile, int turn){
        //Calculates diagonal movement for queen
        ezMove(board, tile, 1, 1,9999);
        //Calculates horizontal movement for queen
        ezMove(board, tile, 0, 1,9999);
        //calculates vertical movement for queen
        ezMove(board, tile, 1, 0,9999);
    }
}
//Creates a Rook Object that is a child of the Piece Object
class Rook extends Piece {
    //Constructor for rook object
    public Rook(String color){
        //Uses parent Piece class constructor and creates an image icon using image path
        super("rook",color,new ImageIcon("img/"+color+"rook"+".png"));
    }
    //Variable to return copy of current Piece
    //Used to clone Pieces to place on tiles
    public Rook newInstance(){
        Rook clone =  new Rook(getColor());
        return clone;
    }
    //Method that overides parent calculatemoves method 
    //Used to calculate legal moves for the rook
    public void calculateMoves(Square[][] board, Square tile, int turn){
        //Calculates horizontal movement for rook
        ezMove(board, tile, 0, 1,9999);
        //Calculates vertical movement for rook
        ezMove(board, tile, 1, 0,9999);
    }
        
}
//Creates a Pawn Object that is a child of the Piece Object
class Pawn extends Piece {
    //Constructor for pawn object
    public Pawn(String color){
        //Uses parent Piece class constructor and creates an image icon using image path
        super("pawn",color,new ImageIcon("img/"+color+"pawn"+".png"));
    }
    //Variable to return copy of current Piece
    //Used to clone Pieces to place on tiles
    public Pawn newInstance(){
        Pawn clone =  new Pawn(getColor());
        return clone;
    }
    //Method that overides parent calculatemoves method 
    //Used to calculate legal moves for the pawn
    public void calculateMoves(Square[][] board, Square tile, int turn){
        //Gets corsd for pawn
        int[] cords = tile.getCords();
        ArrayList<int[]> allMoves = new ArrayList<int[]>();
        //Creates arrays for single moves
        int[] singleMove = new int[2];
        //Creates arrays for double moves
        int[] doubleMove = new int[2];
        //Creates arrays for left and right moves
        int[] takeLeft = new int[2];
        int[] takeRight = new int[2];
        //Creates boolean to see if movement is in bounds
        boolean inBounds = false;
        int direction; 
        //If piece is black
        if(getColor().equals("black")){
            //Sets direction to -1 since black pawns move down
            direction = -1;
            //Ensures it is in bounds (row is less than 7)
            if(cords[0]<7){
                inBounds = true;
            }
        }
        //If piece is white
        else{
            //Sets directino to 1 since white pawns move up
            direction = 1;
            //Ensures it is in bounds (row is greater than 4)
            if(cords[0]>0){
                inBounds = true;
            }
        }
        //Calculates the single move for black or white pawns
        //Single tile infort of pawn
        singleMove[0] = cords[0]-(1*direction);
        singleMove[1] = cords[1];
        //Calculates the take left moves for black or white pawns
        //Tile diagonally left of pawn
        takeLeft[0] = cords[0]-(1*direction);
        takeLeft[1] = cords[1]-(1*direction);
        //Caclualtes the take right moves for black or white pawns
        //Tile diagonally right of pawn
        takeRight[0] = cords[0]-(1*direction);
        takeRight[1] = cords[1]+(1*direction);
        //Checks to see if the piece is in bounds and that the tile infront of the pawn is empty
        if(inBounds && board[singleMove[0]][singleMove[1]].getPiece() == null){
            //If so it allows the pawn to move forward
            allMoves.add(singleMove);
        }
        //Checks to see if pawn if white
        if(getColor().equals("white")){
            //If so it is in bounds and that if there is a piece to its diagonal left and that its a different color
            if(cords[1]>0 && cords[0]>0 && board[takeLeft[0]][takeLeft[1]].getPiece() != null && diffColor(board[takeLeft[0]][takeLeft[1]])){
                //If it is it allows the pawn to capture that piece
                allMoves.add(takeLeft);
            }
            //If so it is in bounds and that if there is a piece to its diagonal right and that its a different color
            if(cords[1]<7 && cords[0]>0 && board[takeRight[0]][takeRight[1]].getPiece() != null && diffColor(board[takeRight[0]][takeRight[1]])){
                //If it is it allows the pawn to capture that piece
                allMoves.add(takeRight);
            }
            //Checks for enpassant
            //If the piece directly to the right of is not null and is a pawn
            //And the piece has enpassant true and a enpassant turn of equal to 1 minus the current turn
            if(cords[1]<7 && board[cords[0]][cords[1]+1].getPiece()!= null && board[cords[0]][cords[1]+1].getPiece() instanceof Pawn && board[cords[0]][cords[1]+1].getPiece().getEnPassant() && board[cords[0]][cords[1]+1].getPiece().getEnPasTurn() == turn-1){
                //Then it allows the piece to take digonally right
                allMoves.add(takeRight);
                //Sets enpass cords to the digonal right
                setEnPasCords(takeRight);
            }
            //If the piece directly to the left of is not null and is a pawn
            //And the piece has enpassant true and a enpassant turn of equal to 1 minus the current turn
            if(cords[1] >0 && board[cords[0]][cords[1]-1].getPiece()!= null && board[cords[0]][cords[1]-1].getPiece() instanceof Pawn && board[cords[0]][cords[1]-1].getPiece().getEnPassant() && board[cords[0]][cords[1]-1].getPiece().getEnPasTurn() == turn-1){
                //Then it allows the piece to take digonally left
                allMoves.add(takeLeft);
                //Sets enpass cords to the digonal left
                setEnPasCords(takeLeft);
            }
        }
        //Checks to see if pawn if black
        if(getColor().equals("black")){
            //If so it is in bounds and that if there is a piece to its diagonal left and that its a different color
            if(cords[1]<7  && cords[0]<7 && board[takeLeft[0]][takeLeft[1]].getPiece() != null && diffColor(board[takeLeft[0]][takeLeft[1]]) ){
                //If it is it allows the pawn to capture that piece
                allMoves.add(takeLeft);
            }
            //If so it is in bounds and that if there is a piece to its diagonal right and that its a different color
            if(cords[1]>0  && cords[0]<7 && board[takeRight[0]][takeRight[1]].getPiece() != null && diffColor(board[takeRight[0]][takeRight[1]])){
                //If it is it allows the pawn to capture that piece
                allMoves.add(takeRight);
            }
            //Checks for enpassant
            //If the piece directly to the left of is not null and is a pawn
            //And the piece has enpassant true and a enpassant turn of equal to 1 minus the current turn
            if(cords[1]<7 && board[cords[0]][cords[1]+1].getPiece()!= null && board[cords[0]][cords[1]+1].getPiece() instanceof Pawn && board[cords[0]][cords[1]+1].getPiece().getEnPassant() && board[cords[0]][cords[1]+1].getPiece().getEnPasTurn() == turn-1){
                //Then it allows the piece to take digonally left
                allMoves.add(takeLeft);
                //Sets enpass cords to the digonal left
                setEnPasCords(takeLeft);
            }
            //If the piece directly to the right of is not null and is a pawn
            //And the piece has enpassant true and a enpassant turn of equal to 1 minus the current turn
            if(cords[1] >0 && board[cords[0]][cords[1]-1].getPiece()!= null && board[cords[0]][cords[1]-1].getPiece() instanceof Pawn && board[cords[0]][cords[1]-1].getPiece().getEnPassant() && board[cords[0]][cords[1]-1].getPiece().getEnPasTurn() == turn-1){
                //Then it allows the piece to take digonally right
                allMoves.add(takeRight);
                //Sets enpass cords to the digonal right
                setEnPasCords(takeRight);
            }
        }
        //Checks to see if the pawn has moved or not
        if(getHasMoved() == false){
            //If it hasn't then the pawn can double move
            doubleMove[0] = cords[0]-2*direction;
            doubleMove[1] = cords[1];
            //The pawn can only double move if the pieces infornt of it are null
            if(board[doubleMove[0]][doubleMove[1]].getPiece() == null){
                //If the piece is black, it checks to see if the tile below it is empty
                if(getColor().equals("black") && board[doubleMove[0]-1][doubleMove[1]].getPiece() ==null){
                    //If so it lets the pawn double move
                    allMoves.add(doubleMove); 
                }
                //If the piece is white it checks to see if the tile above it is empty
                else if(getColor().equals("white") && board[doubleMove[0]+1][doubleMove[1]].getPiece() ==null){
                    //If so it lets the pawn double move
                    allMoves.add(doubleMove); 
                }
            }
        }
        //Sets the legal moves of the pawn to the allmoves array from the begining
        setLegalMoves(allMoves);
    }
}