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
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import static jwget.Webfile.FileType.HTML;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Downloader - This class extends the Thread class and is responsible for the
 * webfiles download
 *
 * @author Joao
 */
public class Downloader extends Thread {

    private String folderPath;          //Folder to save the files
    private boolean dlImages;             // Download images
    private boolean dlVideos;             // Download videos
    private boolean dlCss;                // Download stylesheets
    private boolean dlJs;                 // Download javascript
    private int deepLevel;                // Level of deepness to crawl for websites    
    private ConcurrentLinkedQueue<Webfile> websiteQueue = new ConcurrentLinkedQueue();   // Concurrent queue for websites (on hold to get downloaded)
    private ConcurrentLinkedQueue<Webfile> controlQueue = new ConcurrentLinkedQueue();   // Concurrent queue for websites (already downloaded, control dups)

    public Downloader() {
    }

    /**
     * Class constructor
     *
     * @param folderPath
     * @param dlImages
     * @param dlVideos
     * @param dlCss
     * @param dlJs
     * @param deepLevel
     * @param websiteQueue
     * @param controlQueue
     */
    public Downloader(String folderPath, boolean dlImages, boolean dlVideos, boolean dlCss, boolean dlJs, int deepLevel, ConcurrentLinkedQueue<Webfile> websiteQueue, ConcurrentLinkedQueue<Webfile> controlQueue) {
        this.folderPath = folderPath;
        this.dlImages = dlImages;
        this.dlVideos = dlVideos;
        this.dlCss = dlCss;
        this.dlJs = dlJs;
        this.deepLevel = deepLevel;
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
        boolean crawl = true;
        while (crawl) {

            // Retrieve webfile from queue
            Webfile wf = this.websiteQueue.poll();

            // Check if reached last level
            if (wf.getLevel() >= this.deepLevel) {
                this.interrupt();
            }

            // Parse the file
            switch (wf.getType()) {
                case HTML:
                    parseHtml(wf);
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
    }

    private void parseHtml(Webfile wf) {

        // Name of the file
        String fileName = "index.html";

        try {
            // Connect to server
            Document doc = Jsoup.connect(wf.getUrl()).get();

            Elements links = doc.select("a[href]"); // a with href
            for (Element element : links) {
                
            }

            // Save to file
            FileWriter fstream = new FileWriter(this.folderPath + "\\" + fileName);
            PrintWriter out = new PrintWriter(fstream);
            out.println(doc.toString());
            out.close();
            System.out.println(doc.toString());
        } catch (MalformedURLException ex) {
            Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void parseCSS(Webfile wf) {
    }

    private void parseJS(Webfile wf) {
    }
}
