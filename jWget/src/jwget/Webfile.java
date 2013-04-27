/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jwget;

import jwget.FileTypeMap.FileType;

/**
 *
 * @author Joao
 */
public class Webfile {

    private String fileName;    // Name of the file to save
    private String url;         // URL to download/parse
    private int level;          // Level of deepness relative to the starting page
    private FileType type;      // Type of file

    public Webfile() {
    }

    /**
     * Class constructor
     *
     * @param fileName
     * @param url
     * @param level
     * @param type
     */
    public Webfile(String fileName, String url, int level, FileType type) {
        this.fileName = fileName;
        this.url = url;
        this.level = level;
        this.type = type;
    }

    /**
     * Class constructor
     *
     * @param fileName
     * @param url
     * @param level
     *
     */
    public Webfile(String fileName, String url, int level) {
        this.fileName = fileName;
        this.url = url;
        this.level = level;

    }

    /**
     *
     * GETTERS AND SETTERS - BEGIN
     *
     */
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public FileType getType() {
        return type;
    }

    public void setType(FileType type) {
        this.type = type;
    }
    /**
     *
     * GETTERS AND SETTERS - END
     *
     */
}
