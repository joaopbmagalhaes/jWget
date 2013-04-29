/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jwget;

/**
 * Downloader - This class extends the Thread class and is responsible for the
 * webfiles downloading and parsing
 *
 * @author Joao
 */
public abstract class Downloader implements Runnable {

    protected Webfile wf;             // Webfile to parse and download
    protected Config jConfig;         // Main class with all the info
    protected static final String[] PARSE_TAGS = {"link", "img", "script", "a"};     // HTML tags to be parsed

    public Downloader() {
    }

    /**
     * Class constructor
     *
     * @param jConfig
     * @param wf
     */
    public Downloader(Config jConfig, Webfile wf) {
        this.wf = wf;
        this.jConfig = jConfig;
    }

    /**
     *
     * GETTERS AND SETTERS - BEGIN
     *
     */
    public Webfile getWebfile() {
        return wf;
    }

    public void setWebfile(Webfile wf) {
        this.wf = wf;
    }

    public Config getConfig() {
        return jConfig;
    }

    public void setConfig(Config config) {
        this.jConfig = config;
    }

    /**
     *
     * GETTERS AND SETTERS - END
     *
     */
    @Override
    public abstract void run();

    /**
     * Controls if a webfile is indeed of that deep level or not
     *
     * @param wf
     * @return boolean
     */
    protected void controlDeepLevel(Webfile wf) {
//        Webfile[] cq = (Webfile[]) this.jConfig.getControlQueue().toArray();
//        for(int i=0 ; i < cq.length; i++) {
//            if(cq[i].equals(wf))
//                wf.setLevel(cq[i].getLevel());
//        }
    }
}
