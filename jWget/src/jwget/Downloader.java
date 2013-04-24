/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jwget;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import static jwget.Webfile.FileType.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Downloader - This class extends the Thread class and is responsible for the
 * webfiles downloading and parsing
 *
 * @author Joao
 */
public class Downloader extends Thread {
    private String fileName;        // Name of the file to save
    private Webfile wf;             // Webfile to parse and download
    private Config jConfig;         // Main class with all the info
    private static final String[] PARSE_TAGS = {"a","img","script","link"};     // HTML tags to be parsed
    
    public Downloader() {
    }

    public Downloader(Config jConfig, Webfile wf) {
        this.jConfig = jConfig;
        this.wf = wf;
    }

    /**
     *
     * GETTERS AND SETTERS - BEGIN
     *
     */
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Webfile getWf() {
        return wf;
    }

    public void setWf(Webfile wf) {
        this.wf = wf;
    }

    
    public Config getConfig() {
        return jConfig;
    }

    public void setConfig(Config config) {
        this.jConfig = config;
    }
    /**
     *
     * GETTERS AND SETTERS - END
     *
     */

    @Override
    public void run() {
        // Parse the file
        switch (wf.getType()) {
            case HTML:
                    try {
                        parseHtml(wf);
                    } catch (URISyntaxException ex) {
                        Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
            case CSS:
                parseCSS(wf);
                break;
            case JS:
                parseJS(wf);
                break;
            default:
                break;
        }
    }

    /**
     * Parses a HTML page and saves it to a file
     * 
     * @param wf
     * @throws URISyntaxException 
     */
    private void parseHtml(Webfile wf) throws URISyntaxException {
        if(!this.jConfig.getControlQueue().contains(wf) // Control for repeated websites
           && this.jConfig.isInDomain(wf)               // and websites outside the initial domain
           && this.jConfig.isInDeepLevel(wf)) {         // and also outside the deep level
            try {
                // Connect to server
                Document doc = Jsoup.connect(wf.getUrl()).get();

                // Name the file to be saved
                nameFile(".html");
                
                // Parse all elements
                for(int i = 0; i<PARSE_TAGS.length; i++) {
                    Elements links = doc.select(PARSE_TAGS[i]);
                    String url = "";
                    for (Element element : links) {
                        if(element.hasAttr("href"))
                            url = element.attr("href");
                        if(element.hasAttr("src"))
                            url = element.attr("src");
                        // TODO control the level of deepness

                        Webfile newWf = new Webfile(url, wf.getLevel()+1, HTML);
                        Downloader dl = new Downloader(this.jConfig, newWf);
                        this.jConfig.getExecutor().execute(dl);
                        this.jConfig.getExecutor().execute(dl);
                    }
                }
                
                // Save the file
                FileWriter fstream = new FileWriter(this.jConfig.getFolderPath() + "\\" + this.fileName);
                PrintWriter out = new PrintWriter(fstream);
                out.println(doc.toString());
                out.close();
            } catch (MalformedURLException ex) {
                Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void parseCSS(Webfile wf) {
    }

    private void parseJS(Webfile wf) {
    }
    
    /**
     * Names the file to be saved
     */
    private void nameFile(String ext) {
        UUID uuid = UUID.randomUUID();
        String randomUUIDString = uuid.toString();
        setFileName(String.format("%s.%s", randomUUIDString, ext));
    }
    
    /**
     * Controls if a webfile is indeed of that deep level or not
     * 
     * @param wf
     * @return boolean
     */
    private boolean controlDeepLevel(Webfile wf) {
        //TODO control the level of deepness of a webfile vs. control queue
        return true;
    }
}
