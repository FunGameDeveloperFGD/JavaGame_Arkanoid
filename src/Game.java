import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Game extends Canvas implements Runnable, KeyListener {

    final int WIDTH = 520;
    final int HEIGHT = 450;

    boolean right, left;
    boolean isRunning;
    Thread thread;
    BufferedImage gameView = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

    Texture t1, t2, t3, t4;
    Sprite sBackground, sBall, sPaddle;
    Sprite[] blocks;

    int n = 0;
    float dx = 5, dy = 4;
    float x, y;

    public Game() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        initFrame();
        addKeyListener(this);

        t1 = new Texture("/images/block01.png");
        t2 = new Texture("/images/background.jpg");
        t3 = new Texture("/images/ball.png");
        t4 = new Texture("/images/paddle.png");

        sBackground = new Sprite(t2);
        sBall = new Sprite(t3);
        sPaddle = new Sprite(t4);
        sPaddle.setPosition(260 - t4.getWidth() / 2, 440);
        sBall.setPosition(260 - t3.getWidth() / 2, 440 - t3.getHeight());

        x = sBall.getX();
        y = sBall.getY();

        blocks = new Sprite[1000];

        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                blocks[n] = new Sprite(t1);
                blocks[n].setPosition(i * 43, j * 20);
                n++;
            }
        }
    }

    public void start() {
        if (thread == null) {
            thread = new Thread(this);
            isRunning = true;
            thread.start();
        }
    }

    private void initFrame() {
        JFrame window = new JFrame("Arkanoid Game!");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.add(this);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        requestFocus();
    }


    private void update() {
        if (right) {
            sPaddle.setX(sPaddle.getX() + 6);
        }
        if (left) {
            sPaddle.setX(sPaddle.getX() - 6);
        }

        if ((sPaddle.getX() + sPaddle.getTexture().getWidth()) >= WIDTH) {
            sPaddle.setX(WIDTH - sPaddle.getTexture().getWidth());
        }
        if (sPaddle.getX() <= 0) {
            sPaddle.setX(0);
        }


        x += dx;
        for (int i = 0; i < n; i++) {
            if (new Rectangle((int)(x+3), (int)(y+3), 6, 6).intersects(blocks[i].getBounds())) {
                blocks[i].setPosition(-100, 0);
                dx = -dx;
            }
        }

        y += dy;
        for (int i = 0; i < n; i++) {
            if (new Rectangle((int)(x+3), (int)(y+3), 6, 6).intersects(blocks[i].getBounds())) {
                blocks[i].setPosition(-100, 0);
                dy = -dy;
            }
        }

        if (x < 0 || x > WIDTH) {
            dx = -dx;
        }
        if (y < 0 || y > HEIGHT) {
            dy = -dy;
        }

        if (new Rectangle((int)x, (int)y, 12, 12).intersects(sPaddle.getBounds())) {
            dy = -(new Random().nextInt(4) % 4 + 2);
        }

        sBall.setPosition((int) x, (int) y);
    }

    private void draw() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = gameView.getGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        {
            g.drawImage(sBackground.getTexture().getImage(), 0, 0, WIDTH, HEIGHT, null);
            g.drawImage(sBall.getTexture().getImage(), sBall.getX(), sBall.getY(), sBall.getTexture().getWidth(), sBall.getTexture().getHeight(), null);
            g.drawImage(sPaddle.getTexture().getImage(), sPaddle.getX(), sPaddle.getY(), sPaddle.getTexture().getWidth(), sPaddle.getTexture().getHeight(), null);

            for (int i = 0; i < n; i++) {
                g.drawImage(blocks[i].getTexture().getImage(), blocks[i].getX(), blocks[i].getY(), blocks[i].getTexture().getWidth(), blocks[i].getTexture().getHeight(), null);
            }
        }

        g.dispose();
        g = bs.getDrawGraphics();
        g.drawImage(gameView, 0, 0, WIDTH, HEIGHT, null);
        bs.show();
    }

    @Override
    public void run() {
        try {
            long lastTime = System.nanoTime();
            double fps = 60.0;
            double ns = 1000000000 / fps;
            double delta = 0;
            while (isRunning) {
                long now = System.nanoTime();
                delta += (now - lastTime) / ns;
                lastTime = now;
                if (delta >= 1) {
                    update();
                    draw();
                    delta--;
                }
            }
        } catch (Exception exception) {
            isRunning = false;
            thread.interrupt();
            exception.printStackTrace();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            right = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            left = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            right = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            left = false;
        }
    }
}
