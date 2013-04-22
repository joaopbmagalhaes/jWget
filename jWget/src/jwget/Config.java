/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jwget;

/**
 * Configuration file with location of the HTML files and other resources
 * 
 * @author Joao
 */
public class Config {
    private String url;                   // URL to download
    private String folderPath;            // Path to save all files
    private boolean dlImages;             // Download images
    private boolean dlVideos;             // Download videos
    private boolean dlCss;                // Download stylesheets
    private boolean dlJs;                 // Download javascript
    private int deepLevel;                // Level of deepness to crawl for websites
    private String dateTime;              // Date and time of the request

    public Config() {
    }

    public Config(String url, String folderPath, boolean dlImages, boolean dlVideos, boolean dlCss, boolean dlJs, int deepLevel, String dateTime) {
        this.url = url;
        this.folderPath = folderPath;
        this.dlImages = dlImages;
        this.dlVideos = dlVideos;
        this.dlCss = dlCss;
        this.dlJs = dlJs;
        this.deepLevel = deepLevel;
        this.dateTime = dateTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
}
