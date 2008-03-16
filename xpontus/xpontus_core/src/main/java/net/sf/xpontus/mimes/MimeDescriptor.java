/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.xpontus.mimes;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Propriétaire
 */
public class MimeDescriptor {
    private String mime, description;
    private List<String> fileTypes = new ArrayList<String>();

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getFileTypes() {
        return fileTypes;
    }

    public void setFileTypes(List<String> fileTypes) {
        this.fileTypes = fileTypes;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }
    
    
}
