package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FirstConf extends JFrame implements ActionListener{
    private JTextField xTerminals = new JTextField(3);
    private JTextField yTerminals = new JTextField(3);
    private JButton ok = new JButton("Ok");
    public FirstConf() {
        super("First Config");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 100);
        setVisible(true);
        setLayout(new GridLayout(3, 2));
        JLabel naglowek = new JLabel("Podaj ilość okienek :");
        JLabel xLabel = new JLabel("Poziom:");
        JLabel yLabel = new JLabel("Pion:");
        add(naglowek);
        add(ok);
        add(xLabel);
        add(xTerminals);
        add(yLabel);
        add(yTerminals);
        ok.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if(source == ok) {
            this.setVisible(false);
            Okno okno = new Okno(Integer.parseInt(xTerminals.getText()),Integer.parseInt(yTerminals.getText()));
            this.dispose();
        }
    }
}
