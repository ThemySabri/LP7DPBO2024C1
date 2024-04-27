/*
 * Bismillah
 * Saya Themy Sabri Syuhada dengan NIM 2203903
 * Dengan ini saya menyatakan bahwa mengerjakan latihan praktikum 7 
 * dengan sejujur-jujurnya seperti yang telah dispesifikasikan
 * Aamiin
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {

    int frameWidth = 360;
    int frameHeight = 640;

    Image backgroundImage;
    Image birdImage;
    Image lowerPipeImage;
    Image upperPipeImage;

    // Player
    int playerStartPosX = frameWidth / 8;
    int playerStartPosY = frameHeight / 2;
    int playerWidth = 34;
    int playerHeight = 24;
    Player player;

    // Pipe
    int pipeStartPosX = frameWidth;
    int pipeStartPosY = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;
    ArrayList<Pipe> pipes;

    Timer gameloop;
    Timer pipesCooldown;

    int gravity = 1;
    JLabel scoreLabel;
    int skor = 0;
    private App app;

    public FlappyBird(App app) {
        this.app = app;
        setPreferredSize(new Dimension(frameWidth, frameHeight));
        setFocusable(true);
        addKeyListener(this);
        // setBackground(Color.blue);

        backgroundImage = new ImageIcon(getClass().getResource("Assets/background.png")).getImage();
        birdImage = new ImageIcon(getClass().getResource("Assets/bird.png")).getImage();
        lowerPipeImage = new ImageIcon(getClass().getResource("Assets/lowerPipe.png")).getImage();
        upperPipeImage = new ImageIcon(getClass().getResource("Assets/upperPipe.png")).getImage();

        scoreLabel = new JLabel("" + skor);
        scoreLabel.setForeground(Color.BLACK);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 40));
        scoreLabel.setBounds(5, 5, 120, 35);
        add(scoreLabel);

        player = new Player(playerStartPosX, playerStartPosY, playerWidth, playerHeight, birdImage);
        pipes = new ArrayList<Pipe>();

        pipesCooldown = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placePipes();
            }
        });
        pipesCooldown.start();
        gameloop = new Timer(1000 / 60, this);
        gameloop.start();

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        g.drawImage(backgroundImage, 0, 0, frameWidth, frameHeight, null);

        g.drawImage(player.getImage(), player.getPosX(), player.getPosY(), player.getWidth(), player.getHeight(), null);

        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.getImage(), pipe.getPosX(), pipe.getPosY(), pipe.getWidth(), pipe.getHeight(), null);
        }
    }

    public void placePipes() {
        int randompipeStartPosY = (int) (pipeStartPosY - pipeHeight / 4 - Math.random() * (pipeHeight / 2));
        int openingspace = frameHeight / 4;
        Pipe upperPipe = new Pipe(pipeStartPosX, randompipeStartPosY, pipeWidth, pipeHeight, upperPipeImage);
        pipes.add(upperPipe);

        Pipe lowerPipe = new Pipe(pipeStartPosX, randompipeStartPosY + pipeHeight + openingspace, pipeWidth, pipeHeight,
                lowerPipeImage);
        lowerPipe.setPassed(true);
        pipes.add(lowerPipe);
    }

    public void move() {
        boolean end = false;
        player.setVelocityY(player.getVelocityY() + gravity);
        player.setPosY(player.getPosY() + player.getVelocityY());
        player.setPosY(Math.max(player.getPosY(), 0));

        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            pipe.setPosX(pipe.getPosX() + pipe.getVelocityX());

            if ((player.getPosX() < pipe.getPosX() + pipe.getWidth() - 4
                    && player.getPosX() + player.getWidth() > pipe.getPosX() + 4 &&
                    player.getPosY() < pipe.getPosY() + pipe.getHeight() - 4
                    && player.getPosY() + player.getHeight() > pipe.getPosY() + 4) ||
                    (player.getPosY() < 0 || player.getPosY() + player.getHeight() > frameHeight + 50)) {
                end = true;
                gameloop.stop();
                pipesCooldown.stop();
            }

            if (player.getPosX() > pipe.getPosX() && player.getPosX() < pipe.getPosX() + pipe.getWidth()) {
                if (!pipe.isPassed()) {
                    skor++;
                    scoreLabel.setText("" + skor);
                    pipe.setPassed(true);
                }
            }
        }
        if (end == true) {
            JOptionPane.showMessageDialog(null, "Main yang bener makanya. Cupu lu!");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            player.setVelocityY(-10);
        } else if (e.getKeyCode() == KeyEvent.VK_R) {
            Restart();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public void Restart() {
        player.setPosX(playerStartPosX);
        player.setPosY(playerStartPosY);
        player.setVelocityY(0);
        pipes.clear();
        skor = 0;
        scoreLabel.setText("" + skor);

        gameloop.start();
        pipesCooldown.start();
    }
}
