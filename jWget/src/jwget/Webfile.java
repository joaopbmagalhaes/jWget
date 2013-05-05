/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jwget;

import java.util.Objects;

/**
 *
 * @author Joao
 */
public class Webfile {

    private String fileName;    // Name of the file to save
    private String url;         // URL to download/parse
    private int level;          // Level of deepness relative to the starting page

    public Webfile() {
    }

    /**
     * Class constructor
     *
     * @param fileName
     * @param url
     * @param level
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

    /**
     *
     * GETTERS AND SETTERS - END
     *
     */

    /**
     * Compare 2 Webfiles
     *
     * @param other
     * @return
     */
    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (!(other instanceof Webfile)) {
            return false;
        }

        Webfile otherWebfile = (Webfile) other;
        if (!this.fileName.equalsIgnoreCase(otherWebfile.getFileName())) {
            return false;
        }
        if (this.level != otherWebfile.getLevel()) {
            return false;
        }

        return this.url.equalsIgnoreCase(otherWebfile.getUrl());
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.fileName);
        hash = 23 * hash + Objects.hashCode(this.url);
        hash = 23 * hash + this.level;
        return hash;
    }
}
