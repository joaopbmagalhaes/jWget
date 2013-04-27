/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jwget;

/**
 *
 * @author Isaac
 */
public class DownloaderParseJs extends Downloader {

    public DownloaderParseJs() {
        super();
    }

    /**
     * Class constructor
     *
     * @param jConfig
     * @param wf
     */
    public DownloaderParseJs(Config jConfig, Webfile wf) {
        super(jConfig, wf);
    }

    /**
     * Parses a Javascript file and saves it
     */
    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
