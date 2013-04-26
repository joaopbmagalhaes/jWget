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
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Configuration file with location of the HTML files and other resources
 *
 * @author Joao
 */
public class Config {

    private String root;            // Root url
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
    private final ExecutorService executor = Executors.newFixedThreadPool(NCORES + 1); // Thread pool
    private AtomicInteger countLinks = new AtomicInteger(0);              // Counter of the number of Links to be downloaded

    public Config() {
    }

    /**
     * Class constructor
     *
     * @param root
     * @param domain
     * @param folderPath
     * @param dlImages
     * @param dlVideos
     * @param dlCss
     * @param dlJs
     * @param deepLevel
     * @param dateTime
     */
    public Config(String root, String domain, String folderPath, boolean dlImages, boolean dlVideos, boolean dlCss, boolean dlJs, int deepLevel, String dateTime) throws URISyntaxException {
        this.root = root;
        this.domain = extractDomain(domain);
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
    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

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

    public int incrementCountLinks() {
        return countLinks.incrementAndGet();
    }

    public int decrementCountLinks() {
        return countLinks.decrementAndGet();
    }

    public int getCountLinks() {
        return countLinks.get();
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
    public String extractDomain(String url) throws URISyntaxException {
        URI uri = buildURI(url);
        String dom = uri.getHost();
        return dom.startsWith("www.") ? dom.substring(4) : dom;
    }

    /**
     * Builds URI
     *
     * @param url
     * @return
     * @throws URISyntaxException
     */
    public URI buildURI(String url) throws URISyntaxException {
        if (url.isEmpty()) {
            return null;
        } else {
            if (url.startsWith("/")) {
                url = this.domain + url;
            }
            if (!url.startsWith("http") && !url.startsWith("https")) {
                url = "http://" + url;
            }
            return new URI(url);
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
        String wfDomain = extractDomain(wf.getUrl());
        if (wfDomain.equalsIgnoreCase(this.domain)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks if a given Webfile is inside the allowed deep level
     *
     * @param wf
     * @return
     */
    public boolean isInDeepLevel(Webfile wf) {
        if (wf.getLevel() <= this.getDeepLevel()) {
            return true;
        } else {
            return false;
        }
    }
}
