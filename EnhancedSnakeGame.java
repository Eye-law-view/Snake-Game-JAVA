import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Random;

public class EnhancedSnakeGame extends JPanel implements ActionListener {
    private final int WIDTH = 600;
    private final int HEIGHT = 400;
    private final int DOT_SIZE = 20; // Increased size for better visibility
    private LinkedList<Point> snake; // Snake body
    private Point food; // Food position
    private char direction; // Current direction of the snake
    private boolean running; // Game state
    private Timer timer; // Timer for game loop
    private Random random;

    public EnhancedSnakeGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(new Color(30, 30, 30)); // Dark background for contrast
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        if (direction != 'S') direction = 'W';
                        break;
                    case KeyEvent.VK_DOWN:
                        if (direction != 'W') direction = 'S';
                        break;
                    case KeyEvent.VK_LEFT:
                        if (direction != 'D') direction = 'A';
                        break;
                    case KeyEvent.VK_RIGHT:
                        if (direction != 'A') direction = 'D';
                        break;
                }
            }
        });
        
        initializeGame();
    }

    private void initializeGame() {
        snake = new LinkedList<>();
        snake.add(new Point(5, 5)); // Initial position of the snake
        direction = 'D'; // Start moving right
        running = true;

        random = new Random();
        spawnFood(); // Spawn initial food

        timer = new Timer(100, this); // Timer to control game speed
        timer.start();
    }

    private void spawnFood() {
        int x = random.nextInt(WIDTH / DOT_SIZE);
        int y = random.nextInt(HEIGHT / DOT_SIZE);
        food = new Point(x, y);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (running) {
            drawFood(g);
            drawSnake(g);
            drawScore(g);
        } else {
            showGameOver(g);
        }
        
        Toolkit.getDefaultToolkit().sync(); // Sync the graphics
    }

    private void drawFood(Graphics g) {
        g.setColor(Color.RED);
        g.fillRoundRect(food.x * DOT_SIZE, food.y * DOT_SIZE, DOT_SIZE, DOT_SIZE, 10, 10); // Rounded food
    }

    private void drawSnake(Graphics g) {
        for (int i = 0; i < snake.size(); i++) {
            Point p = snake.get(i);
            if (i == 0) { // Head of the snake
                g.setColor(new Color(0, 200, 0)); // Bright green for head
                g.fillRoundRect(p.x * DOT_SIZE, p.y * DOT_SIZE, DOT_SIZE, DOT_SIZE, 10, 10); 
            } else { // Body of the snake
                g.setColor(new Color(0, 150, 0)); // Darker green for body
                g.fillRoundRect(p.x * DOT_SIZE, p.y * DOT_SIZE, DOT_SIZE, DOT_SIZE, 10, 10); 
            }
        }
    }

    private void drawScore(Graphics g) {
        g.setColor(Color.WHITE);
        g.drawString("Score: " + (snake.size() - 1), 10, 20); // Display score
    }

    private void showGameOver(Graphics g) {
        g.setColor(Color.RED);
        g.drawString("Game Over", WIDTH / 2 - 30, HEIGHT / 2);
        g.drawString("Score: " + (snake.size() - 1), WIDTH / 2 - 30, HEIGHT / 2 + 20);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            moveSnake();
            checkCollision();
            checkFood();
            repaint();
        }
    }

    private void moveSnake() {
        Point head = snake.getFirst();
        
        Point newHead = new Point(head.x, head.y);
        
        switch (direction) {
            case 'W': newHead.y--; break; // Move up
            case 'S': newHead.y++; break; // Move down
            case 'A': newHead.x--; break; // Move left
            case 'D': newHead.x++; break; // Move right
        }
        
        snake.addFirst(newHead); // Add new head to the snake
    }

    private void checkCollision() {
        Point head = snake.getFirst();

        // Check wall collision
        if (head.x < 0 || head.x >= WIDTH / DOT_SIZE || head.y < 0 || head.y >= HEIGHT / DOT_SIZE) {
            running = false;
            timer.stop();
        }

        // Check self collision
        for (int i = 1; i < snake.size(); i++) {
            if (head.equals(snake.get(i))) {
                running = false;
                timer.stop();
                break;
            }
        }
    }

    private void checkFood() {
        Point head = snake.getFirst();

        if (head.equals(food)) { // If the snake eats the food
            spawnFood(); // Spawn new food
            snake.add(new Point(-1, -1)); // Grow the snake by adding a new segment at the end
        } else {
            snake.removeLast(); // Remove last segment of the snake if not eating food
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Enhanced Snake Game");
        EnhancedSnakeGame gamePanel = new EnhancedSnakeGame();
        
        frame.add(gamePanel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}