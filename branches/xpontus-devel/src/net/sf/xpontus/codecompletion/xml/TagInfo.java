/*
 * TagInfo.java
 *
 * Created on February 7, 2007, 7:53 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.sf.xpontus.codecompletion.xml;

import java.util.ArrayList; 

/**
 *
 * @author Yves Zoundi
 */
public class TagInfo {

	private String tagName;
	private boolean hasBody;
	private ArrayList attributes = new ArrayList();
	private ArrayList children   = new ArrayList();
	
	public static final int NONE  = 0;
	public static final int EVENT = 1;
	public static final int FORM  = 2;
	
	/**
	 * RXgN^B
	 * @param tagName ^O?O
	 * @param hasBody qvf???
	 */
	public TagInfo(String tagName,boolean hasBody){
		this.tagName = tagName;
		this.hasBody = hasBody;
	}
	
        
        public String toString(){
            return tagName;
        }
	/**
	 * ^O?O??B
	 * @return
	 */
	public String getTagName(){
		return this.tagName;
	}
	
	/**
	 * qvf?????B
	 * @return
	 */
	public boolean hasBody(){
		return this.hasBody;
	}
	
	/**
	 * ??B
	 * @param attribute
	 */
	public void addAttributeInfo(AttributeInfo attribute){
		int i = 0;
		for(;i<attributes.size();i++){
			AttributeInfo info = (AttributeInfo)attributes.get(i);
			if(info.getAttributeName().compareTo(attribute.getAttributeName()) > 0){
				break;
			}
		}
		this.attributes.add(i,attribute);
	}
	
	/**
	 * S????B
	 * @param attribute
	 */
	public AttributeInfo[] getAttributeInfo(){
		return (AttributeInfo[])this.attributes.toArray(new AttributeInfo[this.attributes.size()]);
	}
	
	/**
	 * K{???B
	 * @return
	 */
	public AttributeInfo[] getRequiredAttributeInfo(){
		ArrayList list = new ArrayList();
		for(int i=0;i<attributes.size();i++){
			AttributeInfo info = (AttributeInfo)attributes.get(i);
			if(info.isRequired()){
				list.add(info);
			}
		}
		return (AttributeInfo[])list.toArray(new AttributeInfo[list.size()]);
	}
	
	/**
	 * w?O???B
	 * @param name
	 * @return
	 */
	public AttributeInfo getAttributeInfo(String name){
		for(int i=0;i<attributes.size();i++){
			AttributeInfo info = (AttributeInfo)attributes.get(i);
			if(info.getAttributeName().equals(name)){
				return info;
			}
		}
		return null;
	}
	
	/**
	 * q^O?OZbg?B
	 * 
	 * @param name q^O?O
	 */
	public void addChildTagName(String name){
		children.add(name);
	}
	
	/**
	 * q^O?O??B
	 * 
	 * @return q^O?OiStringzj
	 */
	public String[] getChildTagNames(){
		return (String[])children.toArray(new String[children.size()]);
	}
} 