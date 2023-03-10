import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.net.*;

public class Screen extends JPanel implements ActionListener {

    private JTextField pInput, qInput, killAAInput,killAaInput, killaaInput; 
    private double P, Q, AA, Aa, aa;
    private int numOfAlleles;
    private JButton updateButton, killButton, reproduceButton;
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

        reproduceButton = new JButton();
        reproduceButton.setBounds(250, 100, 200, 30);
        reproduceButton.setText("REPRODUCE POPULATION");
        this.add(reproduceButton);
        reproduceButton.addActionListener(this);

        killAAInput = new JTextField();
        killAAInput.setBounds(50, 800, 100, 30);
        killAAInput.setText("AA%");
        this.add(killAAInput);

        killAaInput = new JTextField();
        killAaInput.setBounds(150, 800, 100, 30);
        killAaInput.setText("Aa%");
        this.add(killAaInput);

        killaaInput = new JTextField();
        killaaInput.setBounds(250, 800, 100, 30);
        killaaInput.setText("aa%");
        this.add(killaaInput);

        killButton = new JButton();
        killButton.setBounds(350, 800, 280, 30);
        killButton.setText("Perform Natral Selection");
        this.add(killButton);
        killButton.addActionListener(this);

        P = 0.7;
        Q = 0.3;
        numOfAlleles = 20000;

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

        g.drawString("Total population: " + numOfAlleles/2, 50, 100);
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

        drawAlleles(g);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == updateButton){
            P = Double.valueOf(pInput.getText());
            Q = Double.valueOf(qInput.getText());

            redrawAlleles = true;
            updateAlleles();
            repaint();
        }
        if(e.getSource() == killButton){
            naturalSelection(Integer.parseInt(killAAInput.getText()), Integer.parseInt(killAaInput.getText()), Integer.parseInt(killaaInput.getText()));
            updateAlleles();
            repaint();
        }
        if(e.getSource() == reproduceButton){
            numOfAlleles = 2*10000;

        }
    }

    public void drawAlleles(Graphics g){
        // for (int i=0; i<populationList.size(); i++){
        //     int x = (int)((Math.random()*650) + 50);
        //     int y = (int)((Math.random()*400) + 250);
        //     if (populationList.get(i).equals("aa")){
        //         g.setColor(Color.gray);
        //     } else {
        //         g.setColor(Color.red);
        //     }
        //     g.fillOval(x, y, 50, 50);
        //     g.setColor(Color.black);
        //     g.drawString(populationList.get(i), x+20, y+20);
        //     System.out.println(2);
        // }
        // redrawAlleles = false;

        int AA_ct = 0;
        int Aa_ct = 0;
        int aa_ct = 0;

        for (int i=0; i<populationList.size(); i++){
            if (populationList.get(i).equals("aa")){
                aa_ct++;
            } else if (populationList.get(i).equals("Aa")){
                Aa_ct++;
            } else {
                AA_ct++;
            }
        }
        
        int x = 0;
        int y = 250;
        int j = 0;

        for (int i=0; i<AA_ct; i++){
            g.setColor(Color.red);
            g.fillOval(x, y, 5, 5);
            // g.setColor(Color.black);
            // g.drawString("AA", x+20, y+20);
            j++;
            x += 5;
            if (j%200 == 0) {
                x = 0;
                y += 5;
            }
        }

        
        for (int i=0; i<Aa_ct; i++){
            g.setColor(Color.pink);
            g.fillOval(x, y, 5, 5);
            // g.setColor(Color.black);
            // g.drawString("Aa", x+20, y+20);
            j++;
            x += 5;
            if (j%200 == 0) {
                x = 0;
                y += 5;
            }
        }

        for (int i=0; i<aa_ct; i++){
            g.setColor(Color.gray);
            g.fillOval(x, y, 5, 5);
            // g.setColor(Color.black);
            // g.drawString("aa", x+20, y+20);
            j++;
            x += 5;
            if (j%200 == 0) {
                x = 0;
                y += 5;
            }
        }

        redrawAlleles = false;
    }

    public void updateAlleles(){
        AA = Math.round(P * P * 100.0)/100.0;
        Aa = Math.round(2 * P * Q * 100.0)/100.0;
        aa = Math.round(Q * Q * 100.0)/100.0;

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
    public void naturalSelection(int AA_in, int Aa_in, int aa_in){
        System.out.println(AA + ", " + Aa + ", " + aa);
        double AAdouble = AA_in/100.0;
        double Aadouble = Aa_in/100.0;
        double aadouble = aa_in/100.0;

        //System.out.println(AAdouble);

        double AAnum = AA*numOfAlleles*(1-AAdouble);
        double Aanum = Aa*numOfAlleles*(1-Aadouble);
        double aanum = aa*numOfAlleles*(1-aadouble);
        
        System.out.println("AAnum = " + (AA*numOfAlleles));

        AA = AAnum/numOfAlleles;
        Aa = Aanum/numOfAlleles;
        aa = aanum/numOfAlleles;


        System.out.println(AA + ", " + Aa+ ", " + aa);
        
        P = Math.sqrt(AA);
        Q = Math.sqrt(aa);
        double pround = Math.round(P*100.0)/100.0;
        double qround = Math.round(Q*100.0)/100.0;

        pInput.setText(pround + "");
        qInput.setText(qround + "");

        System.out.println("in natural selection -- P: " + P + " Q: " + Q);
        //drawAlleles(g);

        numOfAlleles = AAnum + Aanum + aanum;
    }
}
    