/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jwget;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Configuration file with location of the HTML files and other resources
 *
 * @author Joao
 */
public class Config {

    private String root;                  // Root url
    private String domain;                // Domain
    private String folderPath;            // Path to save all files
    private boolean dlAll;                // Download all types of files
    private String txtImages;             // Downloadable images extensions
    private String txtAudio;              // Downloadable audio extensions
    private String txtVideos;             // Downloadable video extensions
    private String txtOther;              // Other downloadable extensions
    private boolean dlImages;             // Download images
    private boolean dlAudio;              // Download audio
    private boolean dlVideos;             // Download videos
    private boolean dlCss;                // Download stylesheets
    private boolean dlJs;                 // Download javascript
    private boolean dlOther;              // Download other files
    private int deepLevel;                // Level of deepness to crawl for websites
    private String dateTime;              // Date and time of the request
    private static final int NCORES = Runtime.getRuntime().availableProcessors();           // Number of cores the current computer has
    private ConcurrentLinkedQueue<Webfile> controlQueue = new ConcurrentLinkedQueue();      // Concurrent queue for websites (already downloaded, control dups)
    //  private final ExecutorService executor1 = Executors.newFixedThreadPool(NCORES + 1);      // Thread pool
    private final PausableThreadPoolExecutor executor = new PausableThreadPoolExecutor(NCORES + 1, NCORES + 1, 10, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());// Thread pool
    private AtomicInteger countLinks = new AtomicInteger(0);                                // Counter of the number of links to be downloaded

    public Config() {
    }

    /**
     * Class constructor
     * 
     * @param root
     * @param domain
     * @param folderPath
     * @param dlAll
     * @param txtImages
     * @param txtAudio
     * @param txtVideos
     * @param dlImages
     * @param dlAudio
     * @param dlVideos
     * @param dlCss
     * @param dlJs
     * @param deepLevel
     * @param dateTime 
     */
    public Config(String root, String domain, String folderPath, boolean dlAll, String txtImages, String txtAudio, String txtVideos, String txtOther, boolean dlImages, boolean dlAudio, boolean dlVideos, boolean dlCss, boolean dlJs, boolean dlOther, int deepLevel, String dateTime) {
        this.root = root;
        this.domain = domain;
        this.folderPath = folderPath;
        this.dlAll = dlAll;
        this.txtImages = txtImages;
        this.txtAudio = txtAudio;
        this.txtVideos = txtVideos;
        this.txtOther = txtOther;
        this.dlImages = dlImages;
        this.dlAudio = dlAudio;
        this.dlVideos = dlVideos;
        this.dlCss = dlCss;
        this.dlJs = dlJs;
        this.dlOther = dlOther;
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

    public boolean isDlAll() {
        return dlAll;
    }

    public void setDlAll(boolean dlAll) {
        this.dlAll = dlAll;
    }

    public String getTxtImages() {
        return txtImages;
    }

    public void setTxtImages(String txtImages) {
        this.txtImages = txtImages;
    }

    public String getTxtAudio() {
        return txtAudio;
    }

    public void setTxtAudio(String txtAudio) {
        this.txtAudio = txtAudio;
    }

    public String getTxtVideos() {
        return txtVideos;
    }

    public void setTxtVideos(String txtVideos) {
        this.txtVideos = txtVideos;
    }

    public String getTxtOther() {
        return txtOther;
    }

    public void setTxtOther(String txtOther) {
        this.txtOther = txtOther;
    }

    public boolean isDlAudio() {
        return dlAudio;
    }

    public void setDlAudio(boolean dlAudio) {
        this.dlAudio = dlAudio;
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

    public boolean isDlOther() {
        return dlOther;
    }

    public void setDlOther(boolean dlOther) {
        this.dlOther = dlOther;
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

    public PausableThreadPoolExecutor getExecutor() {
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
    public final String extractDomain(String url) throws URISyntaxException {
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
    
    /**
     * Check if webfiles should be downloaded or not
     * 
     * @param wf
     * @return 
     */
    public boolean canDownload(String ext) {
        if(this.dlAll || ext.equalsIgnoreCase("html"))
            return true;
        
        // Check for images
        if(this.dlImages) {
            
        }
        String[] imgExt = getImageExt();
        
        
        return false;
    }
    
    /**
     * Returns a list of extensions to download
     * @return 
     */
    public String[] getImageExt() {
        if(!this.txtImages.isEmpty()) {
            this.txtImages = this.txtImages.replace(" ", "");
            return this.txtImages.split(",");
        } else {
            return null;
        }
    }
    
    /**
     * Returns a list of extensions to download
     * @return 
     */
    public String[] getAudioExt() {
        if(!this.txtAudio.isEmpty()) {
            this.txtAudio = this.txtAudio.replace(" ", "");
            return this.txtAudio.split(",");
        } else {
            return null;
        }
    }
    
    /**
     * Returns a list of extensions to download
     * @return 
     */
    public String[] getVideoExt() {
        if(!this.txtVideos.isEmpty()) {
            this.txtVideos = this.txtVideos.replace(" ", "");
            return this.txtVideos.split(",");
        } else {
            return null;
        }
    }
}