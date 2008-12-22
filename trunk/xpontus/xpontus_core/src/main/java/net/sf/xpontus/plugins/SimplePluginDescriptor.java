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
public class SimplePluginDescriptor
{
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

    public SimplePluginDescriptor()
    {
    }

    /**
     * @return
     */
    public String getDependencies()
    {
        return dependencies;
    }

    /**
     * @param dependencies
     */
    public void setDependencies(String dependencies)
    {
        this.dependencies = dependencies;
    }

    public void print()
    {
        StrBuilder str = new StrBuilder("Id:").append(id).appendNewLine()
                                              .append("Builtin:").append(builtin)
                                              .appendNewLine()
                                              .append("Category:")
                                              .append(category).appendNewLine()
                                              .append("DisplayName:")
                                              .append(displayname)
                                              .appendNewLine()
                                              .append("Description:")
                                              .append(description)
                                              .appendNewLine().append("Version:")
                                              .append(version).appendNewLine()
                                              .append("License:").append(license)
                                              .appendNewLine().append("Author:")
                                              .append(author).appendNewLine()
                                              .append("Date:").append(date)
                                              .appendNewLine()
                                              .append("Homepage:")
                                              .append(homepage);
        System.out.println(str.toString());
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return id;
    }

    /**
     * @return
     */
    public String getPackagename()
    {
        return packagename;
    }

    /**
     * @param packagename
     */
    public void setPackagename(String packagename)
    {
        this.packagename = packagename;
    }

    /**
     * @return
     */
    public String getArchive()
    {
        return archive;
    }

    /**
     * @param archive
     */
    public void setArchive(String archive)
    {
        this.archive = archive;
    }

    /**
     * @return
     */
    public String getContributors()
    {
        return contributors;
    }

    /**
     * @param contributors
     */
    public void setContributors(String contributors)
    {
        this.contributors = contributors;
    }

    /**
     * @return
     */
    public String getDate()
    {
        return date;
    }

    /**
     * @param date
     */
    public void setDate(String date)
    {
        this.date = date;
    }

    /**
     * @return
     */
    public String getLicense()
    {
        return license;
    }

    /**
     * @param license
     */
    public void setLicense(String license)
    {
        this.license = license;
    }

    /**
     * @return
     */
    public String getAuthor()
    {
        return author;
    }

    /**
     * @param author
     */
    public void setAuthor(String author)
    {
        this.author = author;
    }

    /**
     * @return
     */
    public String getBuiltin()
    {
        return builtin;
    }

    /**
     * @param builtin
     */
    public void setBuiltin(String builtin)
    {
        this.builtin = builtin;
    }

    /**
     * @return
     */
    public String getCategory()
    {
        return category;
    }

    /**
     * @param category
     */
    public void setCategory(String category)
    {
        this.category = category;
    }

    /**
     * @return
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * @param description
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * @return
     */
    public String getDisplayname()
    {
        return displayname;
    }

    /**
     * @param displayname
     */
    public void setDisplayname(String displayname)
    {
        this.displayname = displayname;
    }

    /**
     * @return
     */
    public String getHomepage()
    {
        return homepage;
    }

    /**
     * @param homepage
     */
    public void setHomepage(String homepage)
    {
        this.homepage = homepage;
    }

    /**
     * @return
     */
    public String getId()
    {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id)
    {
        this.id = id;
    }

    /**
     * @return
     */
    public String getVersion()
    {
        return version;
    }

    /**
     * @param version
     */
    public void setVersion(String version)
    {
        this.version = version;
    }
}
