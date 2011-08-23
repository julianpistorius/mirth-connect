/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is Mirth.
 *
 * The Initial Developer of the Original Code is
 * WebReach, Inc.
 * Portions created by the Initial Developer are Copyright (C) 2006
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 *   Gerald Bortis <geraldb@webreachinc.com>
 *
 * ***** END LICENSE BLOCK ***** */


package com.webreach.mirth.client.ui;

import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;

/**
 * Creates the status bar for the Mirth client application.
 */
public class StatusBar extends javax.swing.JPanel
{
    
    /** Creates new form StatusBar */
    public StatusBar()
    {
        initComponents();
        left.setText("Connected to: " + PlatformUI.SERVER_NAME);
        left.setIcon(new ImageIcon(com.webreach.mirth.client.ui.Frame.class.getResource("images/server.png")));
        progressBar.setEnabled(false);
        progressBar.setForeground(UIManager.getColor("TaskPaneContainer.backgroundGradientEnd"));
        this.setBorder(new BevelBorder(BevelBorder.LOWERED));
    }
    
    public void setWorking(boolean working)
    {
        progressBar.setIndeterminate(working);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        left = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        left.setText("jLabel1");

        progressBar.setDoubleBuffered(true);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(left)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 535, Short.MAX_VALUE)
                .add(progressBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(8, 8, 8))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(left)
            .add(layout.createSequentialGroup()
                .add(4, 4, 4)
                .add(progressBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 9, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel left;
    private javax.swing.JProgressBar progressBar;
    // End of variables declaration//GEN-END:variables
    
}