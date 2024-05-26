import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {

            int sirina = 563;
            int duzina = 1000;

            JFrame frame = new JFrame("Flying Bird");
            frame.setSize(sirina, duzina);
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            Fly flappy = new Fly();
            frame.add(flappy);
            frame.pack();
            flappy.requestFocus();
            frame.setVisible(true);
    }
}