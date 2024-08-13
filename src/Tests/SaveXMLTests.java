package Tests;

import files.XMLFilesPIWO;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.awt.*;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class SaveXMLTests {

    @Test
    public void readXML(){
        XMLFilesPIWO xml = null;
        try {
            xml = new XMLFilesPIWO();
            xml.setNamefile("/home/mateusz/Dokumenty/polibuda/ProgramowanieObiektowe/src/Tests/example1x3.xml");
            xml.loadFile();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void savetoXML(){
        try {
            XMLFilesPIWO xml = new XMLFilesPIWO();
            xml.setNamefile("example1x3.xml");
            List<Color> colores = new ArrayList<Color>();
            for (int i=0;i<10;i++){
                colores.add(new Color(25*i,25*i,25*i));
            }
            List<Color> colores1 = new ArrayList<Color>();
            for (int i=0;i<10;i++){
                colores1.add(new Color(1*i/2,1*i+2,10*i));
            }
            Duration d = Duration.ofMinutes(12);
            xml.createScene(colores,3,d);
            xml.createScene(colores1,2,d);
            xml.createScene(colores,1,d);
            xml.saveToFile();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
    }
}
