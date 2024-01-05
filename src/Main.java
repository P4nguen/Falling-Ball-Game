import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        int frameHeight = 500;      //Adjusting the frame sizes
        int frameWidth = 500;

        JFrame frame = new JFrame("Project Frame");     //Making a new frame

        frame.setSize(frameWidth, frameHeight);     //Setting Frame size
        frame.setLocationRelativeTo(null);      //Making the frame appear in the middle of the screen
        frame.setResizable(false);      //Making the frame unable to resize
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);       //Setting the close operation to exit on close

        BasicGame game = new BasicGame(frameWidth, frameHeight);
        frame.add(game);        //Adding the game to the frame
        frame.setVisible(true);     //Making the frame visible
        frame.pack();

    }
}