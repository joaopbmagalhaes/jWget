/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jwget;

/**
 *
 * @author Isaac
 */
public class FileTypeManager {

    private boolean dlAll;                // Download all types of files
    private boolean dlImages;             // Download images
    private boolean dlAudio;              // Download audio
    private boolean dlVideos;             // Download videos
    private boolean dlCss;                // Download stylesheets
    private boolean dlJs;                 // Download javascript
    private boolean dlOther;              // Download other files
    private String txtOther;              // Other downloadable extensions

    public FileTypeManager(boolean dlAll, boolean dlImages, boolean dlAudio, boolean dlVideos, boolean dlCss, boolean dlJs, boolean dlOther, String txtOther) {
        this.dlAll = dlAll;
        this.dlImages = dlImages;
        this.dlAudio = dlAudio;
        this.dlVideos = dlVideos;
        this.dlCss = dlCss;
        this.dlJs = dlJs;
        this.dlOther = dlOther;
        this.txtOther = txtOther;
    }

    public boolean isDlAll() {
        return dlAll;
    }

    public void setDlAll(boolean dlAll) {
        this.dlAll = dlAll;
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

    public String getTxtOther() {
        return txtOther;
    }

    public void setTxtOther(String txtOther) {
        this.txtOther = txtOther;
    }

    /**
     * Check if webfiles should be downloaded or not
     *
     * @param wf
     * @return
     */
    public boolean canDownload(String ext) {
        String fileType = FileTypeMap.getInstance().getFileType(ext);

        // Checks if the extension exists
        if (fileType == null) {
            return false;
        }

        // Download all html files and all other files if checkbox selected
        if (this.dlAll || ext.equalsIgnoreCase("html")) {
            return true;
        }

        // Check for css files
        if (ext.equals("css")
                && dlCss) {
            return true;
        }

        // Check for js files
        if (ext.equals("js")
                && dlJs) {
            return true;
        }

        // Check for images files
        if (fileType.equals("img")
                && dlImages) {
            return true;
        }

        // Check for audio files
        if (fileType.equals("audio")
                && dlAudio) {
            return true;
        }

        // Check for video files
        if (fileType.equals("video")
                && dlVideos) {
            return true;
        }

        // Check for other files
        if (fileType.equals("other")
                && dlOther) {
            return true;
        }

        // Check for other file extensions
        String otherExt[] = getTextExt();
        if (otherExt != null) {
            for (int k = 0; k < otherExt.length; k++) {
                if (otherExt[k].equalsIgnoreCase(ext)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Returns a list of extensions to download
     *
     * @return
     */
    private String[] getTextExt() {
        String ext = this.txtOther;
        if (!ext.isEmpty()) {
            ext = ext.replace(" ", "");
            return ext.split(",");
        } else {
            return null;
        }
    }
}
