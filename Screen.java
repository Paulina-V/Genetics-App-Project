import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import javax.imageio.ImageIO;

public class Screen extends JPanel implements ActionListener {

    private JTextField pInput, qInput, killAAInput, killAaInput, killaaInput, populationInput;
    private JSlider pSlider, qSlider;
    private double P, Q, AA, Aa, aa;
    private int popSize = 10000;
    private JButton updateButton, killButton, reproduceButton;
    private BufferedImage pool;
    private ArrayList<String> popList;

    private Font f1 = new Font(Font.DIALOG_INPUT, Font.BOLD | Font.ITALIC, 20);
    private Font f2 = new Font(Font.DIALOG, Font.BOLD, 15);
    private Font f3 = new Font(Font.SANS_SERIF, Font.BOLD, 14);
    private Font f4 = new Font(Font.SERIF, Font.PLAIN, 12);

    public Screen() throws IOException {

        popList = new ArrayList<String>();
        AA = P * P;
        Aa = 2 * P * Q;
        aa = Q * Q;
        for (double i = 0; i < AA * popSize; i++) {
            popList.add("AA");
        }
        for (double i = 0; i < Aa * popSize; i++) {
            popList.add("Aa");
        }
        for (double i = 0; i < aa * popSize; i++) {
            popList.add("aa");
        }

        // set up side panel stuff
        Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
        labelTable.put(0, new JLabel("0.0"));
        labelTable.put(50, new JLabel("0.5"));
        labelTable.put(100, new JLabel("1.0"));

        pSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
        pSlider.setLabelTable(labelTable);
        pSlider.setBounds(50, 80, 200, 50);
        pSlider.setMajorTickSpacing(10);
        pSlider.setMinorTickSpacing(1);
        pSlider.setPaintTicks(true);
        pSlider.setPaintLabels(true);
        pSlider.setFont(f4);
        pInput = new JTextField();
        pInput.setBounds(50, 130, 200, 30);
        pInput.setText("0.5");
        add(pSlider);
        add(pInput);

        qSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
        qSlider.setLabelTable(labelTable);
        qSlider.setBounds(50, 210, 200, 50);
        qSlider.setMajorTickSpacing(10);
        qSlider.setMinorTickSpacing(1);
        qSlider.setPaintTicks(true);
        qSlider.setPaintLabels(true);
        qSlider.setFont(f4);
        qInput = new JTextField();
        qInput.setBounds(50, 260, 200, 30);
        qInput.setText("0.5");
        add(qSlider);
        add(qInput);

        populationInput = new JTextField();
        populationInput.setBounds(50, 340, 200, 30);
        populationInput.setText("10000");
        add(populationInput);

        updateButton = new JButton();
        updateButton.setBounds(50, 390, 200, 30);
        updateButton.setFont(f2);
        updateButton.setText("UPDATE VALUES");
        add(updateButton);

        killAAInput = new JTextField();
        killAAInput.setBounds(50, 470, 200, 30);
        killAAInput.setText("0");
        add(killAAInput);

        killAaInput = new JTextField();
        killAaInput.setBounds(50, 540, 200, 30);
        killAaInput.setText("0");
        add(killAaInput);

        killaaInput = new JTextField();
        killaaInput.setBounds(50, 610, 200, 30);
        killaaInput.setText("0");
        add(killaaInput);

        killButton = new JButton();
        killButton.setBounds(50, 650, 200, 30);
        killButton.setFont(f2);
        killButton.setText("NATURAL SELECTION");
        add(killButton);

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

        setUpSidePanel(g);

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

        pInput.setText(""+pSlider.getValue());
        // qInput.setText(""+pSlider.getValue());
        P = Integer.valueOf(pInput.getText());
        // Q =

        AA = P * P;
        Aa = 2 * P * Q;
        aa = Q * Q;

        popList.clear();

        for (double i = 0; i < AA * popSize; i++) {
            popList.add("AA");
        }
        for (double i = 0; i < Aa * popSize; i++) {
            popList.add("Aa");
        }
        for (double i = 0; i < aa * popSize; i++) {
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

        P = (final_AA * 2 + final_Aa) / (popSize * 2);
        Q = (final_aa * 2 + final_Aa) / (popSize * 2);
        System.out.println("P: " + P + " Q: " + Q + " sum: " + (P + Q));

        updateAlleles();
    }

    public double round(double a) {
        return Math.round(a * 100.00) / 100.00;
    }

    public void setUpSidePanel(Graphics g) {

        g.setFont(f1);
        g.drawString("Values", 45, 50);

        // Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
        // labelTable.put(0, new JLabel("0.0"));
        // labelTable.put(50, new JLabel("0.5"));
        // labelTable.put(100, new JLabel("1.0"));

        // p
        g.setFont(f2);
        g.drawString("p", 50, 70);

        // g.setFont(f4);
        // pSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
        // pSlider.setLabelTable(labelTable);
        // pSlider.setBounds(50, 80, 200, 50);
        // pSlider.setMajorTickSpacing(10);
        // pSlider.setMinorTickSpacing(1);
        // pSlider.setPaintTicks(true);
        // pSlider.setPaintLabels(true);
        // pInput = new JTextField();
        // pInput.setBounds(50, 130, 200, 30);
        // pInput.setText("0.5");
        // add(pSlider);
        // add(pInput);

        // q
        g.setFont(f2);
        g.drawString("q", 50, 200);

        // g.setFont(f4);
        // qSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
        // qSlider.setLabelTable(labelTable);
        // qSlider.setBounds(50, 210, 200, 50);
        // qSlider.setMajorTickSpacing(10);
        // qSlider.setMinorTickSpacing(1);
        // qSlider.setPaintTicks(true);
        // qSlider.setPaintLabels(true);
        // qInput = new JTextField();
        // qInput.setBounds(50, 260, 200, 30);
        // qInput.setText("0.5");
        // add(qSlider);
        // add(qInput);

        // population
        g.setFont(f2);
        g.drawString("Population", 50, 330);
        // populationInput.setBounds(50, 340, 200, 30);
        // add(populationInput);

        // update button
        // updateButton.setBounds(50, 390, 200, 30);
        // updateButton.setFont(f2);
        // add(updateButton);

        g.setFont(f2);
        // AA
        g.drawString("Kill AA%", 50, 460);
        // killAAInput.setBounds(50, 470, 200, 30);
        // add(killAAInput);

        // Aa
        g.drawString("Kill Aa%", 50, 530);
        // killAaInput.setBounds(50, 540, 200, 30);
        // add(killAaInput);

        // aa
        g.drawString("Kill aa%", 50, 600);
        // killaaInput.setBounds(50, 610, 200, 30);
        // add(killaaInput);

        // // nat select button
        // killButton.setBounds(50, 650, 200, 30);
        // killButton.setFont(f2);
        // add(killButton);
    }
}
