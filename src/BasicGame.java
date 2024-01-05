import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class BasicGame extends JPanel implements ActionListener, KeyListener {

    @Override
    public void actionPerformed(ActionEvent actionEvent) {          //Running the game until gameOver == 0 (false)
        if (!gameOver) {
            updateGame();
            setChance();
            checkCollisions();
            repaint();
        }
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
        //No action needed
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {                     //Making the basket move with the given input
        if (keyEvent.getKeyCode() == KeyEvent.VK_D) {
            basket.x += 2;
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_A) {
            basket.x -= 2;
        }

        // Adjust the x-coordinate limits
        basket.x = Math.max(0, Math.min(basket.x, frameWidth / tileSize - 5));              //Preventing the basket from getting over the edges

        repaint();
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        //No action needed
    }

    private static class Tile {
        int x;
        int y;

        Tile(int x, int y) {               //Constructor of Tile
            this.x = x;
            this.y = y;
        }
    }

    public static class FallingObject {
        int x;
        int y;
        Color color;

        FallingObject(int x, int y, Color color) {          //Constructor of FallingObject
            this.x = x;
            this.y = y;
            this.color = color;
        }
    }
    //Declaring necessary variables
    List<FallingObject> fallingObjects;

    Random random;

    int frameWidth;
    int frameHeight;
    int tileSize = 20;

    Tile basket;

    Timer gameLoop;
    int Score = 0;
    int velocityX = 0;
    int velocityY = 2;          // Increasing the velocity for faster falling
    boolean gameOver = false;       //Declaring the default gameOver variable

    public BasicGame(int frameWidth, int frameHeight) {
        addKeyListener(this);
        setFocusable(true);

        this.frameWidth = frameWidth;                       //Constructor of the frame
        this.frameHeight = frameHeight;
        setPreferredSize(new Dimension(frameWidth, frameHeight));
        setBackground(Color.DARK_GRAY);                 //Setting the background color

        basket = new Tile(12, 22);              //Creating the basket object
        fallingObjects = new ArrayList<>();             //Making an ArrayList for fallingObjects

        gameLoop = new Timer(200, this);        //Timer for the gameLoop with a delay of 200 milliseconds
        gameLoop.start();
        random = new Random();
    }

    public void paintComponent(Graphics g) {              //Drawing the Components
        super.paintComponent(g);
        draw(g);
    }

    public void placeFallingObject(Color color) {
        int x = random.nextInt(frameWidth / tileSize);          //Method for placing the balls
        fallingObjects.add(new FallingObject(x, 0, color));
    }

    public void setChance() {
        int chance = random.nextInt(200) + 1;               //Placing the balls with respect to chance variable
        if (chance > 20 && chance < 150) {
            placeFallingObject(Color.green);
        } else if (chance <= 20 && chance > 4) {
            placeFallingObject(Color.red);
        } else {
            placeFallingObject(Color.yellow);
        }
    }

            //Updates the game state, including the movement of the basket and falling objects
    public void updateGame() {
        basket.x += velocityX;

        basket.x = Math.max(0, Math.min(basket.x, frameWidth / tileSize - 5));          //Preventing the basket from getting over the edges

        for (FallingObject fallingObject : fallingObjects) {                //Making the balls fall
            fallingObject.x += velocityX;
            fallingObject.y += velocityY;

            if (fallingObject.y > frameHeight) {
                fallingObjects.remove(fallingObject);           //Making the balls disappear after they exceed the frameHeight
                break;
            }
        }
    }
            //Checking the collisions between the basket and the falling objects and updating the score
    public void checkCollisions() {
        for (FallingObject fallingObject : fallingObjects) {
            int basketX = basket.x * tileSize;
            int basketY = basket.y * tileSize;

            int fallingObjectX = fallingObject.x * tileSize;
            int fallingObjectY = fallingObject.y * tileSize;

            if (basketX < fallingObjectX + tileSize &&                  //Checking the collision with coordinates
                    basketX + 5 * tileSize > fallingObjectX &&
                    basketY < fallingObjectY + tileSize &&
                    basketY + tileSize > fallingObjectY) {

                fallingObjects.remove(fallingObject);               //Removing the balls if they collide with the basket and adding score according to ball color
                if (fallingObject.color.equals(Color.green)) {
                    Score += 1;
                } else if (fallingObject.color.equals(Color.yellow)) {
                    Score += 5;
                } else if (fallingObject.color.equals(Color.red)) {
                    Score -= 3;
                }

                break;
            }
        }
    }
            //Drawing the game components to the panel
    public void draw(Graphics g) {
        g.setColor(Color.white);            //Setting the color of the basket

        int basketX = basket.x * tileSize;
        int basketY = basket.y * tileSize;

        g.fillRect(basketX, basketY, 5 * tileSize, tileSize);               //Drawing the basket

        for (FallingObject fallingObject : fallingObjects) {
            g.setColor(fallingObject.color);
            g.fillOval(fallingObject.x * tileSize, fallingObject.y * tileSize, tileSize, tileSize);
        }

        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        g.drawString("Score: " + Score, tileSize - 20, tileSize);           //Making the Score work
        g.setColor(Color.white);
    }
}
