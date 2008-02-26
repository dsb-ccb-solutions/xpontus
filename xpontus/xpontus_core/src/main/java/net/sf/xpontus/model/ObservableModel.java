/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.xpontus.model;

import java.io.Serializable;

import java.util.Observable;


/**
 *
 * @author Propri�taire
 */
public class ObservableModel extends Observable implements Serializable {
    /**
     * default constructor
     */
    public ObservableModel() {
    }

    /**
     * notify the observers that the model has changed
     */
    protected void updateView() {
        setChanged();
        notifyObservers();
    }
}
