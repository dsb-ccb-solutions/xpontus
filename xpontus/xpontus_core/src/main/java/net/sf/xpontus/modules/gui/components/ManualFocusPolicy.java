/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.xpontus.modules.gui.components;

import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.awt.Window;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import java.util.HashMap;
import java.util.HashSet;


/**
 *
 * @author Propriétaire
 */
public class ManualFocusPolicy extends FocusTraversalPolicy
    implements Serializable {
    private HashMap firstComponents = new HashMap();
    private HashMap forwardMap = new HashMap();
    private HashMap backwardMap = new HashMap();

    /**
     * @param firstComponent
     */
    public void setFirstComponent(Container focusCycleRoot,
        Component firstComponent) {
        firstComponents.put(focusCycleRoot, firstComponent);
    }

    public void setNextFocusableComponent(Component left, Component right) {
        forwardMap.put(left, right);
        backwardMap.put(right, left);
    }

    public void unsetNextFocusableComponent(Component left, Component right) {
        forwardMap.remove(left);
        backwardMap.remove(right);
    }

    public Component getComponentAfter(Container focusCycleRoot,
        Component aComponent) {
        Component hardCoded = aComponent;
        Component prevHardCoded;
        HashSet sanity = new HashSet();

        do {
            prevHardCoded = hardCoded;
            hardCoded = (Component) forwardMap.get(hardCoded);

            if (hardCoded == null) {
                return null;
            }

            if (sanity.contains(hardCoded)) {
                // cycle detected; bail
                return null;
            }

            sanity.add(hardCoded);
        } while (!accept(hardCoded));

        return hardCoded;
    }

    public Component getComponentBefore(Container focusCycleRoot,
        Component aComponent) {
        Component hardCoded = aComponent;
        Component prevHardCoded;
        HashSet sanity = new HashSet();

        do {
            prevHardCoded = hardCoded;
            hardCoded = (Component) backwardMap.get(hardCoded);

            if (hardCoded == null) {
                return null;
            }

            if (sanity.contains(hardCoded)) {
                // cycle detected; bail
                return null;
            }

            sanity.add(hardCoded);
        } while (!accept(hardCoded));

        return hardCoded;
    }

    /** Retrieve the first component set the the focusCycleRoot
     * @param focusCycleRoot
     * @return Component The component set as the first component, or null.
     */
    public Component getFirstComponent(Container focusCycleRoot) {
        Object obj = firstComponents.get(focusCycleRoot);

        if (obj != null) {
            return (Component) obj;
        } else {
            return null;
        }
    }

    public Component getLastComponent(Container focusCycleRoot) {
        return null;
    }

    public Component getDefaultComponent(Container focusCycleRoot) {
        return getFirstComponent(focusCycleRoot);
    }

    private boolean accept(Component aComponent) {
        if (!(aComponent.isVisible() && aComponent.isDisplayable() &&
                aComponent.isFocusable() && aComponent.isEnabled())) {
            return false;
        }

        // Verify that the Component is recursively enabled. Disabling a
        // heavyweight Container disables its children, whereas disabling
        // a lightweight Container does not.
        if (!(aComponent instanceof Window)) {
            for (Container enableTest = aComponent.getParent();
                    enableTest != null; enableTest = enableTest.getParent()) {
                if (!(enableTest.isEnabled() || enableTest.isLightweight())) {
                    return false;
                }

                if (enableTest instanceof Window) {
                    break;
                }
            }
        }

        return true;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

    private void readObject(ObjectInputStream in)
        throws IOException, ClassNotFoundException {
        in.defaultReadObject();
    }
}
