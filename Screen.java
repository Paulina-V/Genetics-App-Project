import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.net.*;

public class Screen extends JPanel implements ActionListener {

    private JTextField pInput, qInput;
    private double P, Q, AA, Aa, aa;
    private int numOfAlleles;
    private JButton updateButton;
    private BufferedImage pool, whiteCircle, redCircle;

    public Screen() throws IOException {
        setLayout(null);
        setFocusable(true);

        pInput = new JTextField();
        pInput.setBounds(50, 50, 100, 30);
        pInput.setText("0.7");
        this.add(pInput);

        qInput = new JTextField();
        qInput.setBounds(150, 50, 100, 30);
        qInput.setText("0.3");
        this.add(qInput);

        updateButton = new JButton();
        updateButton.setBounds(250, 50, 200, 30);
        updateButton.setText("UPDATE VALUES");
        this.add(updateButton);
        updateButton.addActionListener(this);

        P = 0.7;
        Q = 0.3;
        numOfAlleles = 200;

        pool = ImageIO.read(new File("pool.jpeg"));
        whiteCircle = ImageIO.read(new File("whitecircle.png"));
        redCircle = ImageIO.read(new File("redcircle.png"));

        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.white);
        g.fillRect(0, 0, 1000, 1000);
        g.drawImage(pool, 100, 300, null);

        g.setColor(Color.blue);
        g.drawString("P:", 50, 40);
        g.drawString("Q:", 150, 40);

        AA = P * P;
        Aa = 2 * P * Q;
        aa = Q * Q;

        g.drawString("Total number of alleles: " + numOfAlleles, 50, 100);
        g.setColor(Color.red);
        g.drawString("AA: " + AA, 50, 130);
        g.drawString("Aa: " + Aa, 50, 160);
        g.setColor(Color.black);
        g.drawString("aa: " + aa, 50, 190);

        g.drawImage(whiteCircle, 300, 400, null);
        g.drawImage(redCircle, 400, 400, null);
        g.drawString("A: " + Math.round(P*numOfAlleles), 350, 460);
        g.drawString("a: " + Math.round(Q*numOfAlleles), 450, 460);

        repaint();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == updateButton){
            P = Double.valueOf(pInput.getText());
            Q = Double.valueOf(qInput.getText());
        }
    }
    
}
