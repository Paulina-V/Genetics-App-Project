import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

public class Screen extends JPanel implements ActionListener {

    private JTextField pInput, qInput;
    private double P, Q, AA, Aa, aa;
    private int numOfAlleles;
    private JButton updateButton;

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

        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawString("P:", 50, 40);
        g.drawString("Q:", 150, 40);

        AA = P * P;
        Aa = 2 * P * Q;
        aa = Q * Q;

        g.drawString("Total number of alleles: " + numOfAlleles, 50, 100);
        g.drawString("AA: " + AA, 50, 130);
        g.drawString("Aa: " + Aa, 50, 160);
        g.drawString("aa: " + aa, 50, 190);

        repaint();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == updateButton){
            P = Double.valueOf(pInput.getText());
            Q = Double.valueOf(qInput.getText());
        }
    }
    
}
