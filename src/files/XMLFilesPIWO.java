package files;

import org.w3c.dom.*;
import org.xml.sax.SAXException;
import scene.Scena;
import scene.Terminal;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.io.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class XMLFilesPIWO {
    private DocumentBuilderFactory factory;
    private DocumentBuilder builder;
    Document doc;
    Element root;
    private String namefile;
    private List<Scena> scenaList = new ArrayList<>();

    public List<Scena> getScenaList() {
        return scenaList;
    }

    public void setScenaList(List<Scena> scenaList) {
        this.scenaList = scenaList;
        generateFileStructure();
    }

    private void generateFileStructure() {
        for (int i =0; i<scenaList.size();i++){
            createScene(generateColorList(scenaList,i),scenaList.get(i).getSceneId(), scenaList.get(i).getTime());
        }
    }

    private List<Color> generateColorList(List<Scena> sc, int id){
        List<Color> colors = new ArrayList<>();
        for(int i=0; i<sc.size();i++){
            Terminal terminal = sc.get(id).getTerminal(i);
            colors.add(terminal.color());
        }
        return colors;
    }

    public XMLFilesPIWO() throws ParserConfigurationException, IOException, SAXException {
        factory = DocumentBuilderFactory.newInstance();
        builder = factory.newDocumentBuilder();
        //root
        doc = builder.newDocument();
        root = doc.createElement("root");
        doc.appendChild(root);
    }
    public void saveToFile() throws TransformerException, FileNotFoundException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new FileOutputStream(namefile));
        transformer.transform(source,result);
    }

    public String getNamefile() {

        return namefile;
    }
    public void createScene(List<Color> colorList, int idSceny, Duration durationTime){
        Element scene = doc.createElement("scene");
        root.appendChild(scene);
        scene.setAttribute("SceneId",String.valueOf(idSceny));
        Element duration = doc.createElement("duration");
        scene.appendChild(duration);
        duration.setTextContent(String.valueOf(durationTime));
        for(int i=0; i<colorList.size(); i++){
            Element terminal = doc.createElement("tetminal");
            terminal.setAttribute("id",String.valueOf(i));
            terminal.setTextContent(String.valueOf(colorList.get(i)));
            scene.appendChild(terminal);
        }
    }
    public void loadFile() throws ParserConfigurationException, IOException, SAXException {
        this.factory = DocumentBuilderFactory.newInstance();
        try {
            this.factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            this.builder = factory.newDocumentBuilder();
            this.doc = builder.parse(new File(namefile));
            doc.getDocumentElement().normalize();
            System.out.println("Root Element :" + doc.getDocumentElement().getNodeName());
            System.out.println("------");
            NodeList list = doc.getElementsByTagName("scene");
            for (int temp = 0; temp < list.getLength(); temp++) {
                Node node = list.item(temp);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String id = element.getAttribute("SceneId");
                    Scena scena = new Scena();
                    scena.setSceneId(Integer.parseInt(id));
                    for(int termId=0;termId<element.getElementsByTagName("tetminal").getLength();termId++){
                        Element subEl = (Element)  element.getElementsByTagName("tetminal").item(termId);
                        Color c = Color.decode(subEl.getTextContent());
                        Integer idTerm= Integer.parseInt(subEl.getAttribute("id"));
                        scena.addTerminalToList(c,idTerm,"");
                    }
                    scenaList.add(scena);
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

    }


    private static void printNote(NodeList nodeList) {

        for (int count = 0; count < nodeList.getLength(); count++) {

            Node tempNode = nodeList.item(count);

            // make sure it's element node.
            if (tempNode.getNodeType() == Node.ELEMENT_NODE) {

                // get node name and value
                System.out.println("\nNode Name =" + tempNode.getNodeName() + " [OPEN]");
                System.out.println("Node Value =" + tempNode.getTextContent());

                if (tempNode.hasAttributes()) {

                    // get attributes names and values
                    NamedNodeMap nodeMap = tempNode.getAttributes();
                    for (int i = 0; i < nodeMap.getLength(); i++) {
                        Node node = nodeMap.item(i);
                        System.out.println("attr name : " + node.getNodeName());
                        System.out.println("attr value : " + node.getNodeValue());
                    }

                }

                if (tempNode.hasChildNodes()) {
                    // loop again if has child nodes
                    printNote(tempNode.getChildNodes());
                }

                System.out.println("Node Name =" + tempNode.getNodeName() + " [CLOSE]");

            }

        }

    }

    // read file from project resource's folder.
    private static InputStream readXmlFileIntoInputStream(final String fileName) {
        return XMLFilesPIWO.class.getClassLoader().getResourceAsStream(fileName);
    }

    public void setNamefile(String namefile) {
        this.namefile = namefile;
    }
    public void updateScene(List<Color> colorList, int idSceny, Duration durationTime){
        NodeList listOfScenes = doc.getElementsByTagName("scene");
        for(int i=0;i<listOfScenes.getLength();i++) {
            Node scene = listOfScenes.item(i);
            if (scene.getNodeType() == Node.ELEMENT_NODE) {
                String id = scene.getAttributes().getNamedItem("id").getTextContent();
                if (String.valueOf(idSceny).equals(id.trim())) {
                    NodeList childScene = scene.getChildNodes();
                    for (int j = 0; j < childScene.getLength(); j++) {
                        Node item = childScene.item(j);
                        if (item.getNodeType() == Node.ELEMENT_NODE) {
                            if ("terminal".equalsIgnoreCase(item.getNodeName())) {
                                Element e = (Element) item;
                                String itemId = e.getAttribute("id");
                                item.setTextContent(String.valueOf(colorList.get(Integer.parseInt(itemId))));
                                ;
                            }
                            if ("duration".equalsIgnoreCase(item.getNodeName())) {
                                item.setTextContent(String.valueOf(durationTime));
                            }
                        }
                    }
                    root.removeChild((Element)scene);
                    root.appendChild((Element)scene);
                }
            }
        }
    }
}
