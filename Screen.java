import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import javax.imageio.ImageIO;

public class Screen extends JPanel implements ActionListener {

    private JTextField pInput, qInput, killAAInput, killAaInput, killaaInput, populationInput;
    private JButton updateButton, killButton, showPop, showGraph;
    private JSlider pSlider, qSlider;

    private Hashtable<Integer, JLabel> labelTable;
    private ArrayList<String> popList;
    private ArrayList<Double> AAlist, Aalist, aalist;

    private Font f1 = new Font(Font.MONOSPACED, Font.BOLD, 30);
    private Font f2 = new Font(Font.DIALOG_INPUT, Font.BOLD | Font.ITALIC, 20);
    private Font f3 = new Font(Font.DIALOG, Font.BOLD, 15);
    private Font f4 = new Font(Font.SANS_SERIF, Font.BOLD, 14);
    private Font f5 = new Font(Font.SERIF, Font.PLAIN, 12);

    private BufferedImage pool;

    private double P, Q, AA, Aa, aa;
    private int popSize = 10000;
    private boolean popPage = true;

    public Screen() throws IOException {

        AAlist = new ArrayList<Double>();
        Aalist = new ArrayList<Double>();
        aalist = new ArrayList<Double>();

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
        labelTable = new Hashtable<Integer, JLabel>();
        labelTable.put(0, new JLabel("0.0"));
        labelTable.put(50, new JLabel("0.5"));
        labelTable.put(100, new JLabel("1.0"));

        pSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
        pSlider.setLabelTable(labelTable);
        pSlider.setBounds(50, 80, 200, 50);
        pSlider.setMajorTickSpacing(10);
        pSlider.setMinorTickSpacing(5);
        pSlider.setPaintTicks(true);
        pSlider.setPaintLabels(true);
        pSlider.setFont(f5);
        pSlider.setSnapToTicks(true);
        pInput = new JTextField();
        pInput.setBounds(50, 130, 200, 30);
        pInput.setText("0.5");
        add(pSlider);
        add(pInput);

        qSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
        qSlider.setLabelTable(labelTable);
        qSlider.setBounds(50, 210, 200, 50);
        qSlider.setMajorTickSpacing(10);
        qSlider.setMinorTickSpacing(5);
        qSlider.setPaintTicks(true);
        qSlider.setPaintLabels(true);
        qSlider.setFont(f5);
        qSlider.setSnapToTicks(true);
        qInput = new JTextField();
        qInput.setBounds(50, 260, 200, 30);
        qInput.setText("0.5");
        add(qSlider);
        add(qInput);

        P = Double.valueOf(pInput.getText());
        Q = Double.valueOf(qInput.getText());

        populationInput = new JTextField();
        populationInput.setBounds(50, 340, 200, 30);
        populationInput.setText("10000");
        add(populationInput);

        // updateButton = new JButton();
        // updateButton.setBounds(50, 390, 200, 30);
        // updateButton.setFont(f3);
        // updateButton.setText("UPDATE VALUES");
        // add(updateButton);
        // updateButton.addActionListener(this);

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
        killButton.setFont(f3);
        killButton.setText("NATURAL SELECTION");
        add(killButton);
        killButton.addActionListener(this);

        showPop = new JButton();
        showPop.setBounds(600, 300, 200, 30);
        showPop.setFont(f3);
        showPop.setText("SHOW POPULATION");
        add(showPop);
        showPop.addActionListener(this);

        showGraph = new JButton();
        showGraph.setBounds(600, 350, 200, 30);
        showGraph.setFont(f3);
        showGraph.setText("SHOW GRAPH");
        add(showGraph);
        showGraph.addActionListener(this);

        pool = ImageIO.read(new File("pool.jpeg"));
        setLayout(null);
        setFocusable(true);
    }

    public Dimension getPreferredSize() {
        return new Dimension(1200, 800);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.white);
        g.fillRect(0, 0, 10000, 10000);
        g.setColor(Color.black);

        updateAlleles();

        setUpSidePanel(g);
        checkPQ();

        popSize = Integer.valueOf(populationInput.getText());

        g.setFont(f1);
        g.drawString("GENETICS SIMULATION", 570, 50);

        g.setFont(f2);
        g.drawString("AA: " + AA, 400, 100);
        g.drawString("Aa: " + Aa, 400, 130);
        g.drawString("aa: " + aa, 400, 160);

        // gene pool
        g.drawImage(pool, 860, 50, null);
        g.setColor(Color.black);

        g.setColor(Color.red);
        g.fillOval(950, 100, 80, 50);
        g.setColor(Color.gray);
        g.fillOval(1020, 100, 80, 50);
        g.setColor(Color.black);
        g.setFont(f5);
        g.drawString("A: " + String.format("%,.0f", P * popSize * 2), 970, 130);
        g.drawString("a: " + String.format("%,.0f", Q * popSize * 2), 1040, 130);

        g.setFont(f2);
        g.drawString("GENE POOL", 950, 240);

        if (popPage) {
            g.setFont(f1);
            g.drawString("POPULATION", 400, 300);

            drawAlleles(g);
        } else {
            g.setColor(Color.black);
            g.setFont(f1);
            g.drawString("GRAPH", 400, 300);

            g.fillRect(400, 550, 5, 150);
            g.fillRect(400, 700, 450, 5);
            g.drawString("1", 390, 555);
            g.drawString("0", 390, 705);

            g.setFont(f4);
            g.drawString("Generations: " + AAlist.size(), 400, 330);
            if (!AAlist.isEmpty()) {
                int step = 30;
                int graphLength = step * (AAlist.size() - 1);
                if (graphLength > 400) {
                    step = 400 / (AAlist.size() - 1);
                }

                for (int i = 0; i < AAlist.size(); i++) {
                    int x = 400 + i * step;
                    int y = (int) (700 - AAlist.get(i) * 150);
                    g.setColor(Color.red);
                    g.fillOval(x, y, 3, 3);
                }
                for (int i = 0; i < Aalist.size(); i++) {
                    int x = 400 + i * step;
                    int y = (int) (700 - Aalist.get(i) * 150);
                    g.setColor(Color.pink);
                    g.fillOval(x, y, 3, 3);
                }
                for (int i = 0; i < aalist.size(); i++) {
                    int x = 400 + i * step;
                    int y = (int) (700 - aalist.get(i) * 150);
                    g.setColor(Color.gray);
                    g.fillOval(x, y, 3, 3);
                }
            }
        }

        repaint();
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == updateButton) {
            P = Double.valueOf(pInput.getText());
            Q = Double.valueOf(qInput.getText());

            popSize = Integer.valueOf(populationInput.getText());

            AAlist.clear();
            Aalist.clear();
            aalist.clear();
        }
        if (e.getSource() == killButton) {

            naturalSelection(Integer.parseInt(killAAInput.getText()), Integer.parseInt(killAaInput.getText()),
                    Integer.parseInt(killaaInput.getText()));

        }
        if (e.getSource() == showPop) {
            popPage = true;
        }
        if (e.getSource() == showGraph) {
            popPage = false;
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

        int x = 350;
        int y = 400;
        int j = 0;

        for (int i = 0; i < AA_ct; i++) {
            g.setColor(Color.red);
            g.fillOval(x, y, 4, 4);
            // g.setColor(Color.black);
            // g.drawString("AA", x+20, y+20);
            j++;
            x += 4;
            if (j % 200 == 0) {
                x = 350;
                y += 4;
            }
        }

        for (int i = 0; i < Aa_ct; i++) {
            g.setColor(Color.pink);
            g.fillOval(x, y, 4, 4);
            // g.setColor(Color.black);
            // g.drawString("Aa", x+20, y+20);
            j++;
            x += 4;
            if (j % 200 == 0) {
                x = 350;
                y += 4;
            }
        }

        for (int i = 0; i < aa_ct; i++) {
            g.setColor(Color.gray);
            g.fillOval(x, y, 4, 4);
            // g.setColor(Color.black);
            // g.drawString("aa", x+20, y+20);
            j++;
            x += 4;
            if (j % 200 == 0) {
                x = 350;
                y += 4;
            }
        }

    }

    // Recreates entire population based on current p and q values
    public void updateAlleles() {

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

        // pInput.setText(String.format("%,.3f", P));
        // qInput.setText(String.format("%,.3f", Q));
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
        AAlist.add(AA);
        Aalist.add(Aa);
        aalist.add(aa);
        updateAlleles();
    }

    public double round(double a) {
        return Math.round(a * 100.00) / 100.00;
    }

    public void setUpSidePanel(Graphics g) {

        g.setFont(f2);
        g.drawString("Values", 43, 30);

        // Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
        // labelTable.put(0, new JLabel("0.0"));
        // labelTable.put(50, new JLabel("0.5"));
        // labelTable.put(100, new JLabel("1.0"));

        // p
        g.setFont(f3);
        g.drawString("p", 50, 70);

        // g.setFont(f5);
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
        g.setFont(f3);
        g.drawString("q", 50, 200);

        // g.setFont(f5);
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
        g.setFont(f3);
        g.drawString("Population", 50, 330);
        // populationInput.setBounds(50, 340, 200, 30);
        // add(populationInput);

        // update button
        // updateButton.setBounds(50, 390, 200, 30);
        // updateButton.setFont(f3);
        // add(updateButton);

        g.setFont(f3);
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
        // killButton.setFont(f3);
        // add(killButton);

        g.setFont(f2);
        g.drawString("p^2 + 2pq + q^2 = 1", 40, 730);

        g.drawLine(300, 20, 300, 780);
    }

    public void checkPQ() {
        // sync p/q sliders and inputs
        double pSliderInput = pSlider.getValue() / 100.0;
        double qSliderInput = qSlider.getValue() / 100.0;
        double pFieldInput = Double.valueOf(pInput.getText());
        double qFieldInput = Double.valueOf(qInput.getText());
        boolean pOff = false;
        boolean qOff = false;
        if (pSliderInput != P) {
            pInput.setText("" + pSliderInput);
            P = pSliderInput;
            pOff = true;
        } else if (pFieldInput != P) {
            pSlider.setValue((int) (pFieldInput * 100.0));
            P = pFieldInput;
            pOff = true;
        } else if (qSliderInput != Q) {
            qInput.setText("" + qSliderInput);
            Q = qSliderInput;
            qOff = true;
        } else if (qFieldInput != Q) {
            qSlider.setValue((int) (qFieldInput * 100.0));
            Q = qFieldInput;
            qOff = true;
        }

        pSliderInput = pSlider.getValue() / 100.0;
        qSliderInput = qSlider.getValue() / 100.0;
        pFieldInput = Double.valueOf(pInput.getText());
        qFieldInput = Double.valueOf(qInput.getText());
        // if (Math.abs(pSliderInput - P) > 0.1 || Math.abs(pFieldInput - P) > 0.1) {
        // System.out.println(P + "PPPPPPP");
        // System.out.println(pSliderInput);
        // System.out.println(pInput.getText());

        // pOff = true;
        // }
        // if (Math.abs(qSliderInput - Q) > 0.1 || Math.abs(qFieldInput - Q) > 0.1) {
        // System.out.println(Q + "QQQQQQQ");
        // System.out.println(qSliderInput);
        // System.out.println(qInput.getText());

        // qOff = true;
        // }
        if (pOff) { // p is changed
            // System.out.println("P OFF");
            Q = 1 - P;
            qInput.setText("" + Q);
            qSlider.setValue((int) (Q * 100.0));
            pOff = false;
        } else if (qOff) {
            // System.out.println("Q OFF");
            P = 1 - Q;
            pInput.setText("" + P);
            pSlider.setValue((int) (P * 100.0));
            qOff = false;
        }
    }
}
