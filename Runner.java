import javax.swing.*;
import java.io.*;

public class Runner {
    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame("GENETICS APP");
        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Screen screen = new Screen();
        frame.add(screen);

        frame.setVisible(true);
    }
}