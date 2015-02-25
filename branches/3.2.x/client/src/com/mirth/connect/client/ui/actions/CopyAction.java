/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.client.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.mirth.connect.client.ui.components.MirthTextInterface;

/** Allows for Copying in text components. */
public class CopyAction extends AbstractAction {

    MirthTextInterface comp;

    public CopyAction(MirthTextInterface comp) {
        super("Copy");
        this.comp = comp;
    }

    public void actionPerformed(ActionEvent e) {
        comp.copy();
    }

    public boolean isEnabled() {
        return comp.isEnabled() && comp.getSelectedText() != null;
    }
}
