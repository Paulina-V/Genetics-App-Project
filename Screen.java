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
    private BufferedImage pool;
    private ArrayList<String> momList, dadList, populationList;
    private boolean redrawAlleles = true;

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

        int countA = (int)((numOfAlleles/2) * P);
        int counta = (int)((numOfAlleles/2) * Q);
        momList = new ArrayList<>();
        dadList = new ArrayList<>();
        for (int i=0; i<countA; i++){
            momList.add("A");
            dadList.add("A");
        }
        for (int i=0; i<counta; i++){
            momList.add("a");
            dadList.add("a");
        }
        Collections.shuffle(momList);
        Collections.shuffle(dadList);

        populationList = new ArrayList<>();
        for (int i=0; i<momList.size(); i++){
            String allele = momList.get(i) + dadList.get(i);
            if (allele.equals("aA")){
                allele = "Aa";
            }
            populationList.add(allele);
        }

        pool = ImageIO.read(new File("pool.jpeg"));

        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.white);
        g.fillRect(0, 0, 1000, 1000);
        g.drawImage(pool, 450, 50, null);

        g.setColor(Color.blue);
        g.drawString("P:", 50, 40);
        g.drawString("Q:", 150, 40);

        updateAlleles();

        g.drawString("Total number of alleles: " + numOfAlleles, 50, 100);
        g.setColor(Color.red);
        g.drawString("AA: " + AA, 50, 130);
        g.drawString("Aa: " + Aa, 50, 160);
        g.setColor(Color.black);
        g.drawString("aa: " + aa, 50, 190);

        g.setColor(Color.gray);
        g.fillOval(550, 100, 50, 50);
        g.setColor(Color.red);
        g.fillOval(630, 100, 50, 50);
        g.setColor(Color.black);
        g.drawString("A: " + Math.round(P*numOfAlleles), 560, 130);
        g.drawString("a: " + Math.round(Q*numOfAlleles), 640, 130);

        if (redrawAlleles){
            drawAlleles(g);
            System.out.println(1);
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == updateButton){
            P = Double.valueOf(pInput.getText());
            Q = Double.valueOf(qInput.getText());

            redrawAlleles = true;
            updateAlleles();
            repaint();
        }
    }

    public void drawAlleles(Graphics g){
        for (int i=0; i<populationList.size(); i++){
            int x = (int)((Math.random()*650) + 50);
            int y = (int)((Math.random()*400) + 250);
            if (populationList.get(i).equals("aa")){
                g.setColor(Color.gray);
            } else {
                g.setColor(Color.red);
            }
            g.fillOval(x, y, 50, 50);
            g.setColor(Color.black);
            g.drawString(populationList.get(i), x+20, y+20);
            System.out.println(2);
        }
        redrawAlleles = false;
    }

    public void updateAlleles(){
        AA = P * P;
        Aa = 2 * P * Q;
        aa = Q * Q;

        int countA = (int)((numOfAlleles/2) * P);
        int counta = (int)((numOfAlleles/2) * Q);
        momList = new ArrayList<>();
        dadList = new ArrayList<>();
        for (int i=0; i<countA; i++){
            momList.add("A");
            dadList.add("A");
        }
        for (int i=0; i<counta; i++){
            momList.add("a");
            dadList.add("a");
        }
        Collections.shuffle(momList);
        Collections.shuffle(dadList);

        populationList = new ArrayList<>();
        for (int i=0; i<momList.size(); i++){
            String allele = momList.get(i) + dadList.get(i);
            if (allele.equals("aA")){
                allele = "Aa";
            }
            populationList.add(allele);
        }
    }
    
}
