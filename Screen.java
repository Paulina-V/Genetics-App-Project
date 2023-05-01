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

        updateAlleles();

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

        setUpSidePanel(g);
        checkPQ();

        int newPop = Integer.valueOf(populationInput.getText());
        if (newPop != popSize) {
            updateAlleles();
            popSize = newPop;
        }

        g.setFont(f1);
        g.drawString("GENETICS SIMULATION", 570, 50);

        g.setFont(f2);
        g.setColor(Color.red);
        g.drawString("AA: " + AA, 400, 100);
        g.setColor(Color.pink);
        g.drawString("Aa: " + Aa, 400, 130);
        g.setColor(Color.gray);
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

            g.setFont(f3);
            g.setColor(Color.red);
            g.drawString("AA: " + AA * popSize, 1000, 300);
            g.setColor(Color.pink);
            g.drawString("Aa: " + Aa * popSize, 1000, 330);
            g.setColor(Color.gray);
            g.drawString("aa: " + aa * popSize, 1000, 360);

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
            updateAlleles();

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
    }

    // Repopulates using kill percent of AA, Aa, and aa; population remains constant
    public void naturalSelection(int AA_in, int Aa_in, int aa_in) {

        double oldPopSize = popSize;

        // current numbers
        double current_AA = popSize * AA;
        double current_Aa = popSize * Aa;
        double current_aa = popSize * aa;

        // decimal percent of who stays alive
        double remaining_AA_percent = 1 - AA_in / 100.0;
        double remaining_Aa_percent = 1 - Aa_in / 100.0;
        double remaining_aa_percent = 1 - aa_in / 100.0;

        // new people - those who will survive the natural selection
        double new_AA = current_AA * remaining_AA_percent;
        double new_Aa = current_Aa * remaining_Aa_percent;
        double new_aa = current_aa * remaining_aa_percent;

        // new population
        popSize = (int) Math.round(new_AA + new_Aa + new_aa);

        // scaling people up to input population
        double final_AA = new_AA * oldPopSize / popSize;
        double final_Aa = new_Aa * oldPopSize / popSize;
        double final_aa = new_aa * oldPopSize / popSize;
        popSize = (int) Math.round(final_AA + final_Aa + final_aa);

        // converting back to int percents
        AA = final_AA / popSize;
        AA = round(AA);

        Aa = final_Aa / popSize;
        Aa = round(Aa);

        aa = final_aa / popSize;
        aa = round(aa);

        P = (final_AA * 2 + final_Aa) / (popSize * 2);
        pInput.setText("" + P);
        pSlider.setValue((int) (P * 100.0));
        Q = (final_aa * 2 + final_Aa) / (popSize * 2);
        qInput.setText("" + Q);
        qSlider.setValue((int) (Q * 100.0));

        AAlist.add(AA);
        Aalist.add(Aa);
        aalist.add(aa);
    }

    public double round(double a) {
        return Math.round(a * 100.00) / 100.00;
    }

    public void setUpSidePanel(Graphics g) {

        g.setFont(f2);
        g.drawString("Values", 43, 30);

        g.setFont(f3);
        g.drawString("p", 50, 70);
        g.drawString("q", 50, 200);

        g.drawString("Population", 50, 330);

        g.drawString("Kill AA%", 50, 460);
        g.drawString("Kill Aa%", 50, 530);
        g.drawString("Kill aa%", 50, 600);

        g.setFont(f2);
        g.drawString("p^2 + 2pq + q^2 = 1", 40, 730);

        g.drawLine(300, 20, 300, 780);
    }

    public void checkPQ() {
        // sync p/q sliders and inputs
        boolean pOff = false;
        boolean qOff = false;
        if (Math.abs(pSlider.getValue() / 100.0 - P) > 0.05) {

            pInput.setText("" + (pSlider.getValue() / 100.0));
        }
        if (Double.valueOf(pInput.getText()) != P) {
            pSlider.setValue((int) (Double.valueOf(pInput.getText()) * 100.0));
            P = Double.valueOf(pInput.getText());
            pOff = true;
        }
        if (Math.abs(qSlider.getValue() / 100.0 - Q) > 0.05) {
            qInput.setText("" + (qSlider.getValue() / 100.0));
        }
        if (Double.valueOf(qInput.getText()) != Q) {
            qSlider.setValue((int) (Double.valueOf(qInput.getText()) * 100.0));
            Q = Double.valueOf(qInput.getText());
            qOff = true;
        }

        if (pOff) { // p is changed
            Q = 1 - P;
            qInput.setText("" + Q);
            qSlider.setValue((int) (Q * 100.0));
            updateAlleles();
            pOff = false;
        } else if (qOff) {
            P = 1 - Q;
            pInput.setText("" + P);
            pSlider.setValue((int) (P * 100.0));
            updateAlleles();
            qOff = false;
        }
    }
}
