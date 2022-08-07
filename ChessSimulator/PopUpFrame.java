//Ayush Patel
//June 11, 2021
//AP Computer Science
package chess;
//Importing GUI classes
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
//Creates a Pop Up Frame 
//Used for the win screen Pop Up
class PopUpFrame extends JFrame implements ActionListener {
    //Declaring and Initializing variables
    JFrame f = new JFrame("Win Screen");
    JLabel label1;
    JLabel label2;
    //Constructor for the Pop Up Frame
    //Takes in winner string which is the color of the winning team
    PopUpFrame(String winner){
        //Sets the Size of the frame to a 400 wide and 200 tall rectangle
        f.setSize(400, 200);
        //Gets the dimensions of the computer screen
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        //Uses computer screen dimensions to place the frame in the middle of the screen
        f.setLocation(dim.width/2-f.getSize().width/2, dim.height/2-f.getSize().height/2);

        //Depending on who won the game, it creates diffeent text to place on the win screen pop up
        //If both players won...
        if(winner.equals("both")){
             //It states that its a stalemate
            label1 = new JLabel("Stalemate!");
            label2 = new JLabel("There is no winner!");
        }
        //If one player did win...
        else{
            //It states congratulations and tells what color won
            label1 = new JLabel("Congratulations!");
            label2 = new JLabel(winner + " is the winner!");
        }
        //Sets the fonts for both labels to Verdana and the size of the text to 25
        label1.setFont(new Font("Verdana", 1, 25));
        label2.setFont(new Font("Verdana", 1, 25));
  
        //Create a button to close the popup
        JButton close = new JButton("OK");
  
        //Adds an action listener to the button so that the computer knows when it's pressed
        close.addActionListener(this);
  
        //Creates a transparent JButton used to format the frame using the flowlayout
        JButton empty = new JButton("                                                                                                              ");
        empty.setOpaque(true);
        empty.setEnabled(false);
        empty.setContentAreaFilled(false);
        empty.setBorderPainted(false);
        //Sets the layout for the frame to a flowlayout so each piece of text is on a new line
        f.setLayout(new FlowLayout());
        //Adds text to frame
        f.add(label1);
        f.add(label2);
        //Adds empty button to frame to create empty line
        f.add(empty);
        //Adds close button to frame
        f.add(close);
        //Sets the frame to visible
        f.setVisible(true);
    }
  
    //If a button on the frame is pressed it....
    public void actionPerformed(ActionEvent e){  
        //Creates a string containing the name of the button pressed; 
        String name = e.getActionCommand();
        //If the name of the button is ok
        if (name.equals("OK")) {
            //It closes the pop up frame
            f.dispose();
        }
    }
}