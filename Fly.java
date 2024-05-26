import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class Fly extends JPanel implements ActionListener, KeyListener {


    int sirina = 563;
    int duzina = 1000;

    //Ptica
    int pticaX = sirina / 8;
    int pticaY = sirina / 2;
    int sirinaPtice = 34;
    int duzinaPtice = 24;

    class Ptica {
        int x = pticaX;
        int y = pticaY;
        int sirina = sirinaPtice;
        int duzina = duzinaPtice;
        Image z;

        Ptica(Image z) {
            this.z = z;
        }
    }
    // Cijevi

    int cijevX = duzina;
    int cijevY = 0;
    int cijevSirina = 64;
    int cijevDuzina = 512;

    class Cijev {
        int x = cijevX;
        int y = cijevY;
        int sirina = cijevSirina;
        int duzina = cijevDuzina;

        Image img;
        boolean prosao = false;

        Cijev(Image img) {
            this.img = img;
        }
    }


    // Inic.
    Ptica ptica;
    Timer gameLoop;
    Timer timerZaCijevi;

    // Varijable brzine
    int brzinaY = 0;
    int brzinaX = -4;
    int gravitacija = 1;
    ArrayList<Cijev> cijevi;
    Random random = new Random();
    boolean gameOver = false;
    double score = 0;


    // Slike
    Image pozadina;
    Image pticaSlika;
    Image gornjaCijevImg;
    Image donjaCijevImg;


    Fly() {
        setPreferredSize(new Dimension(duzina, sirina));
        setFocusable(true);
        addKeyListener(this);
        // Slike
        pozadina = new ImageIcon(getClass().getResource("./bg.jpg")).getImage();
        pticaSlika = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
        gornjaCijevImg = new ImageIcon(getClass().getResource("./top.png")).getImage();
        donjaCijevImg = new ImageIcon(getClass().getResource("./bottom.png")).getImage();

        // Ptica
        ptica = new Ptica(pticaSlika);
        cijevi = new ArrayList<Cijev>();

        // Timer za cijevi
        timerZaCijevi = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                postavljanjeCijevi();
            }
        });
        timerZaCijevi.start();

        // Timer
        gameLoop = new Timer(1000 / 60, this);
        gameLoop.start();


    }

    // Cijevi
    public void postavljanjeCijevi() {
        int randomCijevY = (int) (cijevY - cijevDuzina / 4 - Math.random() * (cijevDuzina / 2));
        int slobodanProstor = sirina / 4;

        Cijev gornja = new Cijev(gornjaCijevImg);
        gornja.y = randomCijevY;
        cijevi.add(gornja);
        Cijev donja = new Cijev(donjaCijevImg);
        donja.y = gornja.y + cijevDuzina + slobodanProstor;
        cijevi.add(donja);


    }

    // Pozadina
    public void paintComponent(Graphics x) {
        super.paintComponent(x);
        draw(x);
    }

    public void draw(Graphics x) {
        x.drawImage(pozadina, 0, 0, duzina, sirina, null);

        x.drawImage(pticaSlika, ptica.x, ptica.y, ptica.sirina, ptica.duzina, null);

        for (int i = 0; i < cijevi.size(); i++) {
            Cijev cijev = cijevi.get(i);
            x.drawImage(cijev.img, cijev.x, cijev.y, cijev.sirina, cijev.duzina, null);
        }

        x.setColor(Color.white);

        x.setFont(new Font("Arial", Font.PLAIN, 32));
        if (gameOver) {
            x.drawString("Game Over: " + String.valueOf((int) score), 10, 35);
        } else {
            x.drawString(String.valueOf((int) score), 10, 35);
        }

    }

    public void move() {
        brzinaY += gravitacija;
        ptica.y += brzinaY;
        ptica.y = Math.max(ptica.y, 0);

        for (int i = 0; i < cijevi.size(); i++) {
            Cijev cijev = cijevi.get(i);
            cijev.x += brzinaX;

            if (!cijev.prosao && ptica.x > cijev.x + cijev.sirina) {
                score += 0.5;
                cijev.prosao = true;
            }
            if (collision(ptica, cijev)) {
                gameOver = true;
            }
        }
        if (ptica.y > sirina) {
            gameOver = true;
        }
    }

    // Uvjeti kolizijee
    boolean collision(Ptica a, Cijev b) {
        return a.x < b.x + b.sirina &&
                a.x + a.sirina > b.x &&
                a.y < b.y + b.duzina &&
                a.y + a.duzina > b.y;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver) {
            timerZaCijevi.stop();
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            brzinaY = -9;

            if (gameOver) {
                ptica.y = pticaY;
                brzinaY = 0;
                cijevi.clear();
                gameOver = false;
                score = 0;
                gameLoop.start();
                timerZaCijevi.start();
            }
        }
    }



    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}
