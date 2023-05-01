import javax.swing.*;
import java.io.*;

public class Runner {
    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame("GENETICS APP");
        Screen screen = new Screen();

        frame.add(screen);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}