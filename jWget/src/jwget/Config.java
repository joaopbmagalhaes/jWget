/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jwget;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Configuration file with location of the HTML files and other resources
 * 
 * @author Joao
 */
public class Config {
    private String domain;                // Domain
    private String folderPath;            // Path to save all files
    private boolean dlImages;             // Download images
    private boolean dlVideos;             // Download videos
    private boolean dlCss;                // Download stylesheets
    private boolean dlJs;                 // Download javascript
    private int deepLevel;                // Level of deepness to crawl for websites
    private String dateTime;              // Date and time of the request
    private static final int NCORES = Runtime.getRuntime().availableProcessors();           // Number of cores the current computer has
    private ConcurrentLinkedQueue<Webfile> controlQueue = new ConcurrentLinkedQueue();      // Concurrent queue for websites (already downloaded, control dups)
    private final ExecutorService executor = Executors.newFixedThreadPool(NCORES+1); // Thread pool

    public Config() {
    }

    /**
     * Class constructor
     * 
     * @param domain
     * @param folderPath
     * @param dlImages
     * @param dlVideos
     * @param dlCss
     * @param dlJs
     * @param deepLevel
     * @param dateTime 
     */
    public Config(String domain, String folderPath, boolean dlImages, boolean dlVideos, boolean dlCss, boolean dlJs, int deepLevel, String dateTime) {
        this.domain = domain;
        this.folderPath = folderPath;
        this.dlImages = dlImages;
        this.dlVideos = dlVideos;
        this.dlCss = dlCss;
        this.dlJs = dlJs;
        this.deepLevel = deepLevel;
        this.dateTime = dateTime;
    }

    /**
     *
     * GETTERS AND SETTERS - BEGIN
     *
     */
    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    public boolean isDlImages() {
        return dlImages;
    }

    public void setDlImages(boolean dlImages) {
        this.dlImages = dlImages;
    }

    public boolean isDlVideos() {
        return dlVideos;
    }

    public void setDlVideos(boolean dlVideos) {
        this.dlVideos = dlVideos;
    }

    public boolean isDlCss() {
        return dlCss;
    }

    public void setDlCss(boolean dlCss) {
        this.dlCss = dlCss;
    }

    public boolean isDlJs() {
        return dlJs;
    }

    public void setDlJs(boolean dlJs) {
        this.dlJs = dlJs;
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

    public ConcurrentLinkedQueue<Webfile> getControlQueue() {
        return controlQueue;
    }

    public void setControlQueue(ConcurrentLinkedQueue<Webfile> controlQueue) {
        this.controlQueue = controlQueue;
    }

    public ExecutorService getExecutor() {
        return executor;
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
    public static String extractDomain(String url) throws URISyntaxException {
        if (url.isEmpty()) {
            return null;
        } else {
            if(!url.startsWith("http") && !url.startsWith("https")){
                 url = "http://" + url;
            }
            URI uri = new URI(url);
            String domain = uri.getHost();
            return domain.startsWith("www.") ? domain.substring(4) : domain;
        }
    }
    
    /**
     * Checks if a given Webfile is inside the initial domain
     * 
     * @param wf
     * @return
     * @throws URISyntaxException 
     */
    public boolean isInDomain(Webfile wf) throws URISyntaxException {
        String wfDomain = Config.extractDomain(wf.getUrl());
        if(wfDomain.equalsIgnoreCase(this.domain))
            return true;
        else
            return false;
    }
    
    /**
     * Checks if a given Webfile is inside the allowed deep level
     * @param wf
     * @return 
     */
    public boolean isInDeepLevel(Webfile wf) {
        if(wf.getLevel() <= this.getDeepLevel())
            return true;
        else
            return false;
    }
}
