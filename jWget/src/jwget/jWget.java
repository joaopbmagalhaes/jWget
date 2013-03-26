/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jwget;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author Joao
 */
public class jWget {
    private String url;                                                                 // URL to download
    private String websitePath;                                                         // Path to save all files
    private int deepLevel;                                                              // Level of deepness to crawl for websites
    private ConcurrentLinkedQueue<String> websiteQueue = new ConcurrentLinkedQueue();   // Concurrent queue for websites (on hold to get downloaded)
    
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
    
    /**
     * Parses a given URL to extract to domain
     * 
     * @return String url
     * @throws URISyntaxException 
     */
    public String getDomain() throws URISyntaxException {
        if(this.getUrl().isEmpty())
            return null;
        else {
            URI uri = new URI(this.getUrl());
            String domain = uri.getHost();
            return domain.startsWith("www.") ? domain.substring(4) : domain;
        }
    }    
    
    /**
     * Main method to coordinate the download and parse threads
     * 
     * @return boolean
     * @throws URISyntaxException 
     */
    public boolean execute() throws URISyntaxException {
        // Dowlonad summary
        System.out.println("Starting new download for: "
                 + this.url
                 + "\nExtracting files to: "
                 + this.websitePath
                 + "\nLevel of deepness: "
                 + this.deepLevel);
        
        // Format URL
        formatUrl();
        
        // Begin the downloads
        Downloader d = new Downloader(this.url, this.websitePath, getDomain() + ".html");
        d.start();
        
        return true;
    }
    
    /**
     * Formats the class URL for missing protocol
     */
    public void formatUrl() {
        if(!this.url.startsWith("http://") || !this.url.startsWith("https://"))
            this.url = "http://" + this.url;
    }    
}
