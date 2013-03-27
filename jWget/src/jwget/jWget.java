/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jwget;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 *
 * @author Joao
 */
public class jWget {
    private String url;                   // URL to download
    private String websitePath;           // Path to save all files
    private int deepLevel;                // Level of deepness to crawl for websites
    private String dateTime;              // Date and time of the download
    private ConcurrentLinkedQueue<String> websiteQueue = new ConcurrentLinkedQueue();   // Concurrent queue for websites (on hold to get downloaded)
    private static final Executor executor = Executors.newCachedThreadPool();           // Thread pool
            
    public jWget() {
    }

    /**
     * Class constructor
     *
     * @param url
     * @param websitePath
     * @param deepLevel
     */
    public jWget(String url, String websitePath, int deepLevel) {
        this.url = url;
        this.websitePath = websitePath;
        this.deepLevel = deepLevel;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWebsitePath() {
        return websitePath;
    }

    public void setWebsitePath(String websitePath) {
        this.websitePath = websitePath;
    }

    public int getDeepLevel() {
        return deepLevel;
    }

    public void setDeepLevel(int deepLevel) {
        this.deepLevel = deepLevel;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public ConcurrentLinkedQueue<String> getWebsiteQueue() {
        return websiteQueue;
    }

    public void setWebsiteQueue(ConcurrentLinkedQueue<String> websiteQueue) {
        this.websiteQueue = websiteQueue;
    }

    /**
     * Parses a given URL to extract to domain
     *
     * @return String url
     * @throws URISyntaxException
     */
    public String getDomain() throws URISyntaxException {
        if (this.getUrl().isEmpty()) {
            return null;
        } else {
            URI uri = new URI(this.getUrl());
            String domain = uri.getHost();
            return domain.startsWith("www.") ? domain.substring(4) : domain;
        }
    }

    /**
     * Formats the class URL for missing protocol
     */
    public void formatUrl() {
        if (!this.url.startsWith("http://") || !this.url.startsWith("https://")) {
            this.url = "http://" + this.url;
        }
    }

    /**
     * Updates the history file
     */
    public void updateHistory() {
        try {
            // Create file
            FileWriter fstream = new FileWriter("history.csv", true);
            PrintWriter  out = new PrintWriter (fstream);
            out.println(this.dateTime + ";" + this.url + ";" + this.websitePath + ";" + this.deepLevel);
            //Close the output stream
            out.close();
        } catch (Exception e) { //Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
    }    
    
    /**
     * Updates to the current date and time
     */
    public void updateDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        setDateTime(dateFormat.format(date));
    }
    
    /**
     * Main method to coordinate the download and parse threads
     *
     * @return boolean
     * @throws URISyntaxException
     */
    public boolean execute() throws URISyntaxException {
        // Update date and time
        updateDate();
        
        // Dowlonad summary
        System.out.println(
                this.dateTime
                + "\nStarting new download for: "
                + this.url
                + "\nExtracting files to: "
                + this.websitePath
                + "\nLevel of deepness: "
                + this.deepLevel);

        // Format URL
        formatUrl();

        // Update the hisory file
        updateHistory();

        // Begin the downloads
        for(int i = 0; i < this.deepLevel; i++) {
            Downloader d = new Downloader(this.url, this.websitePath, getDomain() + ".html");
            executor.execute(d);
        }

        return true;
    }
}
