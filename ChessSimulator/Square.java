//Ayush Patel
//June 11, 2021
//AP Computer Science
package chess;
//Importing GUI classes
import javax.swing.*;
import java.awt.*;
//Creates a Square Object that is a child of a JButton to use for each of the tiles of the chess board
public class Square extends JButton{
    //Declares instance variables for square object
    //Each square has..
    //The piece thats on it
    //It's cordinates to locate it
    //and its color to draw it
    private Piece piece = null;
    private int[] cords;
    private String color;
    //Constructer for square object
    public Square(int[] cord1, String color1){
        //Initializes variables
        cords = cord1;
        color = color1;
        this.setOpaque(true);
        //Sets JButton to a 75x75 square
        this.setSize(75,75);
         try {
                UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName() );
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
        //Uses the color to change the background of the JButton to the appropritate RGB values
        if(color.equals("green")){
            //Creates green tiles for the board
            this.setBackground(new Color(118,150,86));
        }
        else if(color.equals("tan")){
            //Creates tan tiles for the board
            this.setBackground(new Color(238, 238, 210));
        }
    }
    //Setter method for piece on the Square
    //Used to move pieces in and off a tile when they move
    public void setPiece(Piece p){
        piece = p;
    }
    //Getter method for piece to detect what pieces are on what tiles and calcualte takes and checks
    public Piece getPiece(){
        return piece;
    }
    //Getter method for color
    //Used to redraw tiles after they turn red when the king is in check
    public String getColor(){
        return color;
    }
    //Creates a copied instance of the square object 
    //Ensures that tiles do not reference same square object
    public Square newInstance(){
        Square copy = new Square(cords, color);
        copy.setPiece(getPiece());
        return copy;
    }
    //Returns cords of tiles
    //Useful in calculating legal moves
    public int[] getCords(){
        return cords;
    }
}