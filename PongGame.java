import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.io.*;
//import javax.sound.sampled.*;

public class PongGame extends JPanel implements KeyListener, ActionListener {
    private int ballX = 200, ballY = 100;
    private int paddle1Y = 150, paddle2Y = 150;
    private int ballSpeedX = 1, ballSpeedY = 1;
    private int player1Score = 0, player2Score = 0;
    private boolean ballDirectionRight = true;
    private boolean gamePaused = false;
    private boolean gameStarted = false;
   // private Clip bounceSound;
    private int gameTimer = 0;
    private int maxGameTime = 180; 
    
    // 3 minutes (60 seconds * 3)
    private boolean showInstructions = true;
    
    // Flag to show instructions at the beginning
    private static int userCount = 0;
     // Static variable to keep track of the number of users

    private String player1Name = "Player 1";
    private String player2Name = "Player 2";

    public PongGame() {
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        Timer timer = new Timer(5, this);
        timer.start();

       // try {
            // Load bounce sound
          //  AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("bounce.wav"));
           // bounceSound = AudioSystem.getClip();
          //  bounceSound.open(audioInputStream);
       // } catch (Exception e) {
      //      e.printStackTrace();
      //  }

        if (showInstructions) {
            displayInstructions();
        }

        // Get player names
        getPlayerNames();

        // Welcome dialog
        displayWelcomeMessage();

        // Display "Let's Start" window
        displayLetsStartWindow();
    }

    public void actionPerformed(ActionEvent e) {
        if (!gamePaused && gameStarted) {
            if (ballDirectionRight) {
                ballX += ballSpeedX;
            } else {
                ballX -= ballSpeedX;
            }
            ballY += ballSpeedY;

            // Ball collision with top and bottom of the window
            if (ballY <= 0 || ballY >= 360) {
                ballSpeedY = -ballSpeedY;
            }

            // Player 1 scores a point
            if (ballX <= 0) {
                player2Score++;
                resetBall();
            }

            // Player 2 scores a point
            if (ballX >= 780) {
                player1Score++;
                resetBall();
            }

            // Ball collision with player 1's paddle
            if (ballX <= 50 && ballY >= paddle1Y && ballY <= paddle1Y + 80) {
                ballDirectionRight = true;
              //  playBounceSound();
            }

            // Ball collision with player 2's paddle
            if (ballX >= 720 && ballY >= paddle2Y && ballY <= paddle2Y + 80) {
                ballDirectionRight = false;
               // playBounceSound();
            }

            // Increase ball speed every 15 seconds
            gameTimer++;
            if (gameTimer % 900 == 0 && ballSpeedX < 5) {
                ballSpeedX++;
                ballSpeedY++;
            }

            // Check for game over conditions
            if (gameTimer >= maxGameTime || player1Score >= 5 || player2Score >= 5) {
                gamePaused = true;
                showWinner();
            }

            repaint();
        }
    }

    public void paint(Graphics g) {
        // Draw the background
        g.setColor(new Color(0, 102, 0)); 
        
        // Beautiful background color
        g.fillRect(0, 0, 800, 400);

        // Draw the paddles
        g.setColor(Color.WHITE);
        g.fillRect(50, paddle1Y, 10, 80);
        g.fillRect(740, paddle2Y, 10, 80);

        // Draw the ball
        g.setColor(Color.YELLOW); // Ball color
        g.fillOval(ballX, ballY, 20, 20);

        // Draw the scores and player names
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.setColor(Color.WHITE); // Text color
        g.drawString(player1Name + ": " + player1Score, 50, 30);
        g.drawString(player2Name + ": " + player2Score, 700, 30);

        if (gameStarted) {
            g.drawString("Time: " + (maxGameTime - gameTimer) / 60 + "s", 360, 30);
        }

        if (gamePaused) {
            // Display game over message
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.drawString("Game Over", 320, 180);
        }
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (!gameStarted && key == KeyEvent.VK_SPACE) {
            gameStarted = true;
        }

        if (key == KeyEvent.VK_P) {
            gamePaused = !gamePaused;
        }

        if (!gamePaused) {
            if (key == KeyEvent.VK_W && paddle1Y > 0) {
                paddle1Y -= 3;
            }
            if (key == KeyEvent.VK_S && paddle1Y < 320) {
                paddle1Y += 3;
            }
            if (key == KeyEvent.VK_UP && paddle2Y > 0) {
                paddle2Y -= 3;
            }
            if (key == KeyEvent.VK_DOWN && paddle2Y < 320) {
                paddle2Y += 3;
            }
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    public void resetBall() {
        ballX = 400;
        ballY = 200;
        ballSpeedX = 1;
        ballSpeedY = 1;
    }

  /* public void playBounceSound() {
        bounceSound.setFramePosition(0);
        bounceSound.start();
    } */ 

    public void showWinner() {
        String winner = "";
        if (player1Score > player2Score) {
            winner = player1Name;
        } else if (player2Score > player1Score) {
            winner = player2Name;
        } else {
            winner = "It's a tie!";
        }
        JOptionPane.showMessageDialog(this, winner + " wins!", "Game Over", JOptionPane.PLAIN_MESSAGE);
        thankYouForPlaying();
    }

    public void displayInstructions() {
        String instructionsHTML = "<html><body style='text-align: left; padding: 10px; width: 300px;'>" +
                "<h1>Pong Game Instructions</h1>" +
                "<p><strong>Player 1 controls:</strong><br>W: Move paddle up<br>S: Move paddle down</p>" +
                "<p><strong>Player 2 controls:</strong><br>Up Arrow: Move paddle up<br>Down Arrow: Move paddle down</p>" +
                "<p><strong>Game Controls:</strong><br>Press SPACE to start the game.<br>Press P to pause the game.</p>" +
                "<p>The first player to score 5 points wins, or the game ends after 3 minutes.</p>" +
                "</body></html>";

        JOptionPane.showMessageDialog(this, instructionsHTML, "Instructions", JOptionPane.INFORMATION_MESSAGE);
    }

    public void getPlayerNames() {
        player1Name = JOptionPane.showInputDialog("Enter the name for Player 1:");
        player2Name = JOptionPane.showInputDialog("Enter the name for Player 2:");
    }

    public void thankYouForPlaying() {
        JOptionPane.showMessageDialog(this, "Thank you for playing my game.", "Game Over", JOptionPane.PLAIN_MESSAGE);
    }

    public void displayWelcomeMessage() {
        String welcomeMessage = "Welcome to Pong Game!\n";
        if (userCount == 0) {
            welcomeMessage += "You are the first user to play.";
        } else {
            welcomeMessage += "You are user number " + (userCount + 1) + " to play.";
        }

        JOptionPane.showMessageDialog(this, welcomeMessage, "Welcome", JOptionPane.INFORMATION_MESSAGE);
        userCount++;
    }

    public void displayLetsStartWindow() {
        JOptionPane.showMessageDialog(this, "Let's Start", "Welcome", JOptionPane.PLAIN_MESSAGE);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Pong Game");
        //pong game class with object game to use its functionalities
        PongGame game = new PongGame();
        frame.add(game);
        frame.setSize(800, 400);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
