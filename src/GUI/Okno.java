package GUI;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import komponent.Kafelek;
import scene.Scena;

public class Okno extends JFrame {
    private JPanel kafelkiPanel = new JPanel();
    private List<Kafelek> kafelki = new ArrayList<Kafelek>();
    private List<Scena> scenaList = new ArrayList<>();
    private SterowaniePanel sterowaniePanel = new SterowaniePanel(scenaList,kafelki,this);

    public Okno(int X, int Y) {
        super("P.I.W.O. v1.0");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setLayout(new GridLayout(1,2));
        setVisible(true);
        addKafelek(X * Y);
        kafelkiPanel.setLayout(new GridLayout(Y, X));
        kafelkiPanel.setSize(X * 100, Y * 100);
        kafelkiPanel.setBorder(new BevelBorder(BevelBorder.RAISED));
        add(kafelkiPanel);
        add(sterowaniePanel);
    }

    public void addKafelek(int numberOfKafelek) {
        for (int i = 0; i < numberOfKafelek; i++) {
            kafelki.add(new Kafelek());
            kafelkiPanel.add(kafelki.get(i));
            int finalI = i;
            kafelki.get(i).addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent mouseEvent) {
                    int modifiers = mouseEvent.getModifiersEx();
                    if ((modifiers & InputEvent.BUTTON1_DOWN_MASK) == InputEvent.BUTTON1_DOWN_MASK) {
                        kafelki.get(finalI).setColor(setMyColor());
                    }
                    if ((modifiers & InputEvent.BUTTON3_DOWN_MASK) == InputEvent.BUTTON3_DOWN_MASK) {
                        kafelki.get(finalI).setIp(setKafelekIP(kafelki.get(finalI).getIp()));
                        System.out.println("Right button pressed.");
                    }
                }
            });
        }
    }
    private Color setMyColor(){
        return JColorChooser.showDialog(this,"Wybierz kolor w scenie",Color.CYAN);
    }
    private String setKafelekIP(String oldIp){
        String newIp = (String) JOptionPane.showInputDialog(this,"Poprzednie IP: "+oldIp+"\nPodaj adres Ip:");
        if(newIp==null){
            return oldIp;
        }else{
            return newIp;
        }
    }
}