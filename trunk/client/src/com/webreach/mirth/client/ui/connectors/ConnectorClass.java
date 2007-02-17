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


package com.webreach.mirth.client.ui.connectors;

import java.util.Properties;

/** Used to extend from for all of the Connectors.
 *  Each method is re-implemented in each Connector.
 */
public class ConnectorClass extends javax.swing.JPanel
{
    String name;
    Properties properties = new Properties();    
    
    /** Creates new form ConnectorClass */
    public ConnectorClass()
    {
        initComponents();
    }
    
    /** Gets the name of the connector */
    public String getName()
    {
        return name;
    }
    
    /** Gets a properties object with all of the current data 
     *  in the connector's form.
     */
    public Properties getProperties()
    {
        return properties;
    }
    
    /** Sets all of the current data in the connector's form 
     *  to the data in the properties object parameter
     */
    public void setProperties(Properties props)
    {
    }
    
    /** Gets a properties object with all of the default settings
     *  for that form as the data.
     */
    public Properties getDefaults()
    {
        return properties;
    }
    
    public boolean checkProperties(Properties props)
    {
        return true;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    
}
