package be.alex.dpc.tools;

import be.alex.dpc.Database;
import org.jaxen.JaxenException;
import org.jaxen.SimpleNamespaceContext;
import org.jaxen.jdom.JDOMXPath;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.JDOMParseException;
import org.jdom.input.SAXBuilder;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SentenceOriginalReparser {

    private Logger logger = Logger.getLogger(getClass().getSimpleName());

    private final File folder;

    private Database database;

    public static void main(String[] args) throws Exception {
        SentenceOriginalReparser reparser = new SentenceOriginalReparser(new File("M:\\workspace\\rubymine\\DPC\\data"));

        reparser.reparseOriginals();
    }

    public SentenceOriginalReparser(File folder) {
        this.folder = folder;
    }

    public void reparseOriginals() throws JDOMException, IOException, JaxenException {
        database = new Database();

        for(File subFolder : folder.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isDirectory();
            }
        })) {
            File[] dutchXmlFiles = subFolder.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith("nl-tei.xml");
                }
            });

            for(File dutchXmlFile : dutchXmlFiles) {
                reparseFile(dutchXmlFile);

                database.commitChanges();
            }
        }
    }

    @SuppressWarnings({"unchecked"})
    private void reparseFile(File dutchXmlFile) throws JDOMException, IOException, JaxenException {
        try {
            Matcher matcher = Pattern.compile("^(.+)-tei.xml$").matcher(dutchXmlFile.getName());

            matcher.matches();

            String documentName = matcher.group(1);

            long documentId = database.getDocumentId(documentName);

            if(documentId != 54) {
                return;
            }

            logger.info("Reparsing " + dutchXmlFile + " => " + documentName + " ("  + documentId + ")");

            List<Element> xOriginals = getOriginalSentences(dutchXmlFile);

            int position = 0;
            for(Element xOriginal : xOriginals) {
                database.updateSentenceOriginal(documentId, position, xOriginal.getTextTrim());

                position++;
            }
        }
        catch(JDOMParseException e) {
            logger.log(Level.SEVERE, "Could not parse " + dutchXmlFile.getPath(), e);
        }
    }

    @SuppressWarnings({"unchecked"})
    private List<Element> getOriginalSentences(File dutchXmlFile) throws JDOMException, IOException, JaxenException {
        Document document = new SAXBuilder().build(dutchXmlFile);

        logger.info(document.getRootElement().toString());

        HashMap<String,String> map = new HashMap();
        map.put( "tei", "http://www.tei-c.org/ns/1.0");

        JDOMXPath xPath = new JDOMXPath("//tei:seg[@type='original']");
        xPath.setNamespaceContext(new SimpleNamespaceContext(map));

        return (List<Element>) xPath.selectNodes(document);
    }
}
