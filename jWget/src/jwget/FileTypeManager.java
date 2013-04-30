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

    public FileTypeManager(boolean dlAll, String txtImages, String txtAudio, String txtVideos, String txtOther, boolean dlImages, boolean dlAudio, boolean dlVideos, boolean dlCss, boolean dlJs, boolean dlOther) {
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

    /**
     * Check if webfiles should be downloaded or not
     *
     * @param wf
     * @return
     */
    public boolean canDownload(String ext) {
        if (this.dlAll || ext.equalsIgnoreCase("html")) {
            return true;
        }

        // Check for web files
        if (FileTypeMap.getFileType(ext).equals("web")
                && (ext.equals("css") || ext.equals("js"))) {
            return true;
        }

        // Check for images files
        if (FileTypeMap.getFileType(ext).equals("img")) {
            if (this.dlImages) {
                return true;
            } else {
                String[] imgExt = getTextExt(this.txtImages);
                for (int i = 0; i < imgExt.length; i++) {
                    if (imgExt[i].equals(ext)) {
                        return true;
                    }
                }
            }
        }

        // Check for audio files
        if (FileTypeMap.getFileType(ext).equals("audio")) {
            if (this.dlAudio) {
                return true;
            } else {
                String[] audioExt = getTextExt(this.txtAudio);
                for (int i = 0; i < audioExt.length; i++) {
                    if (audioExt[i].equals(ext)) {
                        return true;
                    }
                }
            }
        }

        // Check for video files
        if (FileTypeMap.getFileType(ext).equals("video")) {
            if (this.dlVideos) {
                return true;
            } else {
                String[] videoExt = getTextExt(this.txtVideos);
                for (int i = 0; i < videoExt.length; i++) {
                    if (videoExt[i].equals(ext)) {
                        return true;
                    }
                }
            }
        }

        // Check for other files
        if (FileTypeMap.getFileType(ext).equals("other")) {
            if (this.dlOther) {
                return true;
            } else {
                String[] otherExt = getTextExt(this.txtOther);
                for (int i = 0; i < otherExt.length; i++) {
                    if (otherExt[i].equals(ext)) {
                        return true;
                    }
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
    public String[] getTextExt(String text) {
        if (!text.isEmpty()) {
            text = text.replace(" ", "");
            return text.split(",");
        } else {
            return null;
        }
    }
}
