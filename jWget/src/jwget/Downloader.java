/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jwget;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Downloader - This class extends the Thread class nad is responsible for the webpage download
 * @author Joao
 */
public class Downloader extends Thread {
    private String folderPath;          //Folder to save the files
    private ConcurrentLinkedQueue<Webfile> websiteQueue = new ConcurrentLinkedQueue();   // Concurrent queue for websites (on hold to get downloaded)
    private ConcurrentLinkedQueue<Webfile> controlQueue = new ConcurrentLinkedQueue();   // Concurrent queue for websites (already downloaded, control dups)
    
    public Downloader() {
    }

    /**
     * Class constructor
     * 
     * @param folderPath
     * @param websiteQueue
     * @param controlQueue
     */
    public Downloader(String folderPath, ConcurrentLinkedQueue<Webfile> websiteQueue, ConcurrentLinkedQueue<Webfile> controlQueue) {
        this.folderPath = folderPath;
        this.websiteQueue = websiteQueue;
        this.controlQueue = controlQueue;
    }

    
    /**
     * 
     * GETTERS AND SETTERS - BEGIN
     * 
     */
    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    
    public ConcurrentLinkedQueue<Webfile> getWebsiteQueue() {
        return websiteQueue;
    }

    public void setWebsiteQueue(ConcurrentLinkedQueue<Webfile> websiteQueue) {
        this.websiteQueue = websiteQueue;
    }

    public ConcurrentLinkedQueue<Webfile> getControlQueue() {
        return controlQueue;
    }

    public void setControlQueue(ConcurrentLinkedQueue<Webfile> controlQueue) {
        this.controlQueue = controlQueue;
    }
    /**
     * 
     * GETTERS AND SETTERS - END
     * 
     */
    
    /**
     * Parses a given URL to extract to domain
     *
     * @return String url
     * @throws URISyntaxException
     */
    public String getDomain(String url) throws URISyntaxException {
        if (url.isEmpty()) {
            return null;
        } else {
            URI uri = new URI(url);
            String domain = uri.getHost();
            return domain.startsWith("www.") ? domain.substring(4) : domain;
        }
    }    
    
    @Override
    public void run() {
        try {
            Document doc = Jsoup.connect("<websiteUrl>").get();

            FileWriter fstream = new FileWriter(this.folderPath + "\\<fileName>");
            PrintWriter out = new PrintWriter(fstream);
            out.println(doc.toString());
            System.out.println(doc.toString());
            out.close();
        } catch (MalformedURLException ex) {
            Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
