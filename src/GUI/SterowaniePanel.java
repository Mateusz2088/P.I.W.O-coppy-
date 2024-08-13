package GUI;

import files.XMLFilesPIWO;
import komponent.Kafelek;
import komponentLaptopNet.LaptopConnection;
import org.xml.sax.SAXException;
import scene.Scena;
import scene.Terminal;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

public class SterowaniePanel extends JPanel implements ActionListener {
    JLabel nothing = new JLabel("");
    JLabel sceneLabel = new JLabel("GUI.Scena nr");
    JTextField sceneNumberInput = new JTextField("1");
    JButton saveBtn = new JButton("Zapisz do pliku");
    JButton openBtn = new JButton("Otwórz plik");
    JButton saveScene = new JButton("Zapisz zmiany w scenie");
    JButton saveIPConfig = new JButton("Zapisz konfiguracje terminali");
    JButton sendToTerminal = new JButton("Wyślij na urządzenie");
    List<Scena> scenaList;
    List<Kafelek> kafelki;

    Okno okno;

    public SterowaniePanel(List<Scena> scenaList, List<Kafelek> kafelki, Okno okno){
        setLayout(new GridLayout(14,2));
        add(sceneLabel);
        add(sceneNumberInput);
        saveBtn.addActionListener(this::actionPerformed);
        openBtn.addActionListener(this::actionPerformed);
        saveScene.addActionListener(this::actionPerformed);
        saveIPConfig.addActionListener(this::actionPerformed);
        sendToTerminal.addActionListener(this::actionPerformed);
        add(saveBtn);
        add(openBtn);
        add(saveScene);
        add(saveIPConfig);
        add(sendToTerminal);
        this.scenaList = scenaList;
        this.kafelki = kafelki;
        this.okno = okno;
        /*
        Czas trwania danej sceny
        dodaj scenę
        exportuj do pliku
        Uruchom
        Wgraj plik

         */
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source==saveBtn){
            FileDialog fd = new FileDialog(okno,"ZAPISZ",FileDialog.SAVE);
            try {
                XMLFilesPIWO plikXML = new XMLFilesPIWO();
                plikXML.setNamefile(fd.getDirectory()+fd.getFile());
                plikXML.setScenaList(scenaList);
                plikXML.saveToFile();
            } catch (ParserConfigurationException ex) {
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (SAXException ex) {
                throw new RuntimeException(ex);
            } catch (TransformerException ex) {
                throw new RuntimeException(ex);
            }
        }
        if(source==saveScene){
            Scena scena = new Scena();
            scena.setSceneId(Integer.parseInt(sceneNumberInput.getText()));
            for (int i = 0; i<kafelki.size();i++){
                scena.addTerminalToList(kafelki.get(i).getColor(),i,kafelki.get(i).getIp());
            }
            if(scenaList.size()<Integer.parseInt(sceneNumberInput.getText())){
                scenaList.add(scena);
            }else{
                scenaList.set(Integer.parseInt(sceneNumberInput.getText())-1,scena);
            }
        }
        if(source==sendToTerminal){
            LaptopConnection connection = new LaptopConnection();
            Scena scena=scenaList.get(Integer.parseInt(sceneNumberInput.getText())-1);
            for (int i =0; i<scena.getTerminals().size();i++){
                Terminal terminal= scena.getTerminal(i);
                try {
                    connection.startConnection(terminal.address(),1978);
                    Color c = terminal.color();
                    int cR = c.getRed();
                    int cG = c.getGreen();
                    int cB = c.getBlue();
                    String messages = String.valueOf(cR)+" "+String.valueOf(cG)+" "+String.valueOf(cB);
                    String response = connection.sendMessage(messages);
                    connection.stopConnection();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
            try {
                Thread.sleep(scena.getTime());
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
