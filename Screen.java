import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.net.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Screen extends JPanel implements ActionListener {

    private JTextField pInput, qInput, killAAInput, killAaInput, killaaInput, populationInput;
    private double P = 0.5;
    private double Q = 0.5;
    private double AA, Aa, aa;
    private int popSize = 10000;
    private JButton updateButton, killButton, reproduceButton;
    private BufferedImage pool;
    private ArrayList<String> popList;

    public Screen() throws IOException {
        

        popList = new ArrayList<String>();
        AA = P*P;
        Aa = 2*P*Q;
        aa = Q*Q; 
        for(double i = 0; i < AA*popSize; i++){
            popList.add("AA");
        }
        for(double i = 0; i < Aa*popSize; i++){
            popList.add("Aa");
        }
        for(double i = 0; i < aa*popSize; i++){
            popList.add("aa");
        }

        pInput = new JTextField();
        pInput.setBounds(50, 50, 100, 30);
        pInput.setText("0.5");
   /*     pInput.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseExited(MouseEvent e) {
                double p = Double.valueOf(pInput.getText());
                if (p < 0.0) {
                    p = 0.0;
                }
                double q = 1.0 - p;
                qInput.setText(String.format("%f", q));
                repaint();
            }
        }); */
        this.add(pInput);

        qInput = new JTextField();
        qInput.setBounds(150, 50, 100, 30);
        qInput.setText("0.5");
   /*     qInput.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseExited(MouseEvent e) {
                double q = Double.valueOf(qInput.getText());
                if (q < 0.0) {
                    q = 0.0;
                }
                double p = 1.0 - q;
                pInput.setText(String.format("%f", p));
                repaint();
            }
        }); */
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
        reproduceButton.setVisible(false);

        killAAInput = new JTextField();
        killAAInput.setBounds(50, 210, 100, 30);
        killAAInput.setText("AA%");
        this.add(killAAInput);
        killAAInput.setVisible(true);

        killAaInput = new JTextField();
        killAaInput.setBounds(150, 210, 100, 30);
        killAaInput.setText("Aa%");
        this.add(killAaInput);

        killaaInput = new JTextField();
        killaaInput.setBounds(250, 210, 100, 30);
        killaaInput.setText("aa%");
        this.add(killaaInput);

        killButton = new JButton();
        killButton.setBounds(350, 210, 280, 30);
        killButton.setText("Perform Natral Selection");
        this.add(killButton);
        killButton.addActionListener(this);

        populationInput = new JTextField();
        populationInput.setBounds(170, 80, 100, 30);
        populationInput.setText(popSize + "");
        populationInput.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //field.setText("");
                //repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
            //    if (field.getText().isEmpty())
            //        field.setText(name);
            }
        });
        this.add(populationInput);

        pool = ImageIO.read(new File("pool.jpeg"));
        setLayout(null);
        setFocusable(true);
    }

    public Dimension getPreferredSize() {
        return new Dimension(1000, 1000);
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

        //updateAlleles();

        g.drawString("Total population: ", 50, 100);
        g.setColor(Color.red);
        g.drawString("AA: " + String.format("%,.3f", AA), 50, 130);
        g.drawString("Aa: " + String.format("%,.3f", Aa), 50, 160);
        g.setColor(Color.black);
        g.drawString("aa: " + String.format("%,.3f", aa), 50, 190);

        g.setColor(Color.red);
        g.fillOval(550, 100, 50, 50);
        g.setColor(Color.gray);
        g.fillOval(630, 100, 50, 50);
        g.setColor(Color.black);
        g.drawString("A: " + String.format("%,.0f", P * popSize*2), 560, 130);
        g.drawString("a: " + String.format("%,.0f", Q * popSize*2), 640, 130);

        drawAlleles(g);
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == updateButton) {
            P = Double.valueOf(pInput.getText());
            Q = Double.valueOf(qInput.getText());

            popSize = Integer.valueOf(populationInput.getText());
            updateAlleles();
        }
        if (e.getSource() == killButton) {

            naturalSelection(Integer.parseInt(killAAInput.getText()), Integer.parseInt(killAaInput.getText()),
                Integer.parseInt(killaaInput.getText()));

        }
        
        repaint();
    }

    public void drawAlleles(Graphics g) {
        int AA_ct = 0;
        int Aa_ct = 0;

        int aa_ct = 0;
        for (int i = 0; i < popList.size(); i++) {
            if (popList.get(i).equals("aa")) {
                aa_ct++;
            } else if (popList.get(i).equals("Aa")) {
                Aa_ct++;
            } else {
                AA_ct++;
            }
        }

        int x = 0;
        int y = 250;
        int j = 0;

        for (int i = 0; i < AA_ct; i++) {
            g.setColor(Color.red);
            g.fillOval(x, y, 5, 5);
            // g.setColor(Color.black);
            // g.drawString("AA", x+20, y+20);
            j++;
            x += 5;
            if (j % 200 == 0) {
                x = 0;
                y += 5;
            }
        }

        for (int i = 0; i < Aa_ct; i++) {
            g.setColor(Color.pink);
            g.fillOval(x, y, 5, 5);
            // g.setColor(Color.black);
            // g.drawString("Aa", x+20, y+20);
            j++;
            x += 5;
            if (j % 200 == 0) {
                x = 0;
                y += 5;
            }
        }

        for (int i = 0; i < aa_ct; i++) {
            g.setColor(Color.gray);
            g.fillOval(x, y, 5, 5);
            // g.setColor(Color.black);
            // g.drawString("aa", x+20, y+20);
            j++;
            x += 5;
            if (j % 200 == 0) {
                x = 0;
                y += 5;
            }
        }

    }

    // Recreates entire population based on current p and q values
    public void updateAlleles() {

        
        AA = P*P;
        Aa = 2*P*Q;
        aa = Q*Q; 

        popList.clear();

        for(double i = 0; i < AA*popSize; i++){
            popList.add("AA");
        }
        for(double i = 0; i < Aa*popSize; i++){
            popList.add("Aa");
        }
        for(double i = 0; i < aa*popSize; i++){
            popList.add("aa");
        }
        

        
        pInput.setText(String.format("%,.3f", P));
        qInput.setText(String.format("%,.3f", Q));
    }

    // Repopulates using kill percent of AA, Aa, and aa; population remains constant
    public void naturalSelection(int AA_in, int Aa_in, int aa_in) {

        double oldPopSize = popSize;

        // current numbers
        double current_AA = popSize * AA; // 4900
        // System.out.println("current AA" + current_AA);
        double current_Aa = popSize * Aa; // 4200
        double current_aa = popSize * aa; // 900
        System.out.println("old " + current_AA + " " + current_Aa + " " + current_aa);

        // decimal percent of who stays alive
        double remaining_AA_percent = 1 - AA_in / 100.0; // 0.5
        // System.out.print("percent" + remaining_AA_percent);
        double remaining_Aa_percent = 1 - Aa_in / 100.0; // 1
        double remaining_aa_percent = 1 - aa_in / 100.0; // 1

        // new people - those who will survive the natural selection
        double new_AA = current_AA * remaining_AA_percent; // 2450
        double new_Aa = current_Aa * remaining_Aa_percent; // 4200
        double new_aa = current_aa * remaining_aa_percent; // 900
        System.out.println("new" + new_AA + " " + new_Aa + " " + new_aa);

        // new population
        popSize = (int) Math.round(new_AA + new_Aa + new_aa); // 7550
        // System.out.println("population" + new_population);

        // scaling people up to input population
        double final_AA = new_AA * oldPopSize / popSize; // 3,245.0331125828
        double final_Aa = new_Aa * oldPopSize / popSize; // 5,562.9139072848
        double final_aa = new_aa * oldPopSize / popSize; // 1,192.0529801325
        System.out.println("final" + final_AA + " " + final_Aa + " " + final_aa);
        popSize = (int) Math.round(final_AA + final_Aa + final_aa); // 7550

        // converting back to int percents
        // System.out.println(population);
        AA = final_AA / popSize; // 0.3245033112582781
        AA = round(AA);

        Aa = final_Aa / popSize; // 0.5562913907284769
        Aa = round(Aa);

        aa = final_aa / popSize; // 0.11920529801324503
        aa = round(aa);
        System.out.println("percents" + AA + " " + Aa + " " + aa);

        P = (final_AA * 2 + final_Aa)/(popSize*2);
        Q = (final_aa * 2 + final_Aa)/(popSize*2);
        System.out.println("P: " + P + " Q: " + Q + " sum: " + (P+Q));
        updateAlleles();
    }
    public double round(double a){
        return Math.round(a*100.00)/100.00;
    }
}
