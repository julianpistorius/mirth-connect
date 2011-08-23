/*
 * ReferenceListPanel.java
 *
 * Created on February 26, 2007, 11:09 AM
 */

package com.webreach.mirth.client.ui.panels.reference;

import java.util.ArrayList;

/**
 * 
 * @author brendanh
 */
public class ReferenceListPanel extends javax.swing.JPanel
{

    /**
     * Creates new form ReferenceListPanel
     */
    public ReferenceListPanel(String title, ArrayList<ReferenceListItem> items)
    {
        initComponents();
        variableReferenceTable = new VariableReferenceTable(title, items);
        variableReferenceTable.setDragEnabled(true);
        variableReferenceTable.setTransferHandler(new ReferenceListHandler(items));
        variableReferenceScrollPane.setViewportView(variableReferenceTable);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents()
    {
        variableReferenceScrollPane = new javax.swing.JScrollPane();
        variableReferenceTable = new com.webreach.mirth.client.ui.panels.reference.VariableReferenceTable();

        setBackground(new java.awt.Color(255, 255, 255));
        variableReferenceScrollPane.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        variableReferenceScrollPane.setMinimumSize(new java.awt.Dimension(0, 0));
        variableReferenceScrollPane.setViewportView(variableReferenceTable);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(variableReferenceScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 12, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(variableReferenceScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 12, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane variableReferenceScrollPane;
    private com.webreach.mirth.client.ui.panels.reference.VariableReferenceTable variableReferenceTable;
    // End of variables declaration//GEN-END:variables

}