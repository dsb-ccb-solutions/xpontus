/*
 *
 *
 * Copyright (C) 2005-2008 Yves Zoundi
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 *
 */
package net.sf.xpontus.plugins;

import org.apache.commons.lang.text.StrBuilder;


/**
 *
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class SimplePluginDescriptor {
    private String author;
    private String version;
    private String homepage;
    private String description;
    private String category;
    private String builtin;
    private String id;
    private String displayname;
    private String archive;
    private String packagename;
    private String license;
    private String date;
    private String contributors;
    private String dependencies;

    public SimplePluginDescriptor() {
    }

    public String getDependencies() {
        return dependencies;
    }

    public void setDependencies(String dependencies) {
        this.dependencies = dependencies;
    }

    
    public void print() {
        StrBuilder str = new StrBuilder();
        str.append("Id:" + id);
        str.appendNewLine();
        str.append("Builtin:" + builtin);
        str.appendNewLine();
        str.append("Category:" + category);
        str.appendNewLine();
        str.append("DisplayName:" + displayname);
        str.appendNewLine();
        str.append("Description:" + description);
        str.appendNewLine();
        str.append("Version:" + version);
        str.appendNewLine();
        str.append("License:" + license);
        str.appendNewLine();
        str.append("Author:" + author);
        str.appendNewLine();
        str.append("Date:" + date);
        str.appendNewLine();
        str.append("Homepage:" + homepage);
        System.out.println(str.toString());
    }

    public String toString() {
        return id;
    }

    public String getPackagename() {
        return packagename;
    }

    public void setPackagename(String packagename) {
        this.packagename = packagename;
    }

    public String getArchive() {
        return archive;
    }

    public void setArchive(String archive) {
        this.archive = archive;
    }

    public String getContributors() {
        return contributors;
    }

    public void setContributors(String contributors) {
        this.contributors = contributors;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBuiltin() {
        return builtin;
    }

    public void setBuiltin(String builtin) {
        this.builtin = builtin;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
