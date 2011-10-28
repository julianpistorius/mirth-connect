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

package com.webreach.mirth.connectors.http;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.DefaultComboBoxModel;

import com.webreach.mirth.client.ui.UIConstants;
import com.webreach.mirth.client.ui.editors.transformer.TransformerPane;
import com.webreach.mirth.connectors.ConnectorClass;
import com.webreach.mirth.model.Channel;
import com.webreach.mirth.model.Connector;
import com.webreach.mirth.model.Step;

/**
 * A form that extends from ConnectorClass. All methods implemented are
 * described in ConnectorClass.
 */
public class HTTPListener extends ConnectorClass
{
    /** Creates new form HTTPListener */

    public HTTPListener()
    {
        name = HTTPListenerProperties.name;
        initComponents();
    }

    public Properties getProperties()
    {
        Properties properties = new Properties();
        properties.put(HTTPListenerProperties.DATATYPE, name);
        properties.put(HTTPListenerProperties.HTTP_ADDRESS, listenerAddressField.getText());
        properties.put(HTTPListenerProperties.HTTP_PORT, listenerPortField.getText());
        properties.put(HTTPListenerProperties.HTTP_RECEIVE_TIMEOUT, receiveTimeoutField.getText());
        properties.put(HTTPListenerProperties.HTTP_BUFFER_SIZE, bufferSizeField.getText());
        properties.put(HTTPListenerProperties.HTTP_EXTENDED_PAYLOAD, UIConstants.YES_OPTION);
        
        if (keepConnectionOpenYesRadio.isSelected())
            properties.put(HTTPListenerProperties.HTTP_KEEP_CONNECTION_OPEN, UIConstants.YES_OPTION);
        else
            properties.put(HTTPListenerProperties.HTTP_KEEP_CONNECTION_OPEN, UIConstants.NO_OPTION);

        properties.put(HTTPListenerProperties.HTTP_RESPONSE_VALUE, (String)responseFromTransformer.getSelectedItem());
        
        if (appendPayloadYesRadio.isSelected())
            properties.put(HTTPListenerProperties.HTTP_APPEND_PAYLOAD, UIConstants.YES_OPTION);
        else
            properties.put(HTTPListenerProperties.HTTP_APPEND_PAYLOAD, UIConstants.NO_OPTION);
        
        if (((String) payloadURLEncodingComboBox.getSelectedItem()).equals(HTTPListenerProperties.PAYLOAD_ENCODING_NONE))
            properties.put(HTTPListenerProperties.HTTP_PAYLOAD_ENCODING, HTTPListenerProperties.PAYLOAD_ENCODING_NONE);
        else if (((String) payloadURLEncodingComboBox.getSelectedItem()).equals(HTTPListenerProperties.PAYLOAD_ENCODING_ENCODE))
            properties.put(HTTPListenerProperties.HTTP_PAYLOAD_ENCODING, HTTPListenerProperties.PAYLOAD_ENCODING_ENCODE);
        else if (((String) payloadURLEncodingComboBox.getSelectedItem()).equals(HTTPListenerProperties.PAYLOAD_ENCODING_DECODE))
            properties.put(HTTPListenerProperties.HTTP_PAYLOAD_ENCODING, HTTPListenerProperties.PAYLOAD_ENCODING_DECODE);

        return properties;
    }

    public void setProperties(Properties props)
    {
        resetInvalidProperties();
        
        listenerAddressField.setText((String) props.get(HTTPListenerProperties.HTTP_ADDRESS));
        listenerPortField.setText((String) props.get(HTTPListenerProperties.HTTP_PORT));
        receiveTimeoutField.setText((String) props.get(HTTPListenerProperties.HTTP_RECEIVE_TIMEOUT));
        bufferSizeField.setText((String) props.get(HTTPListenerProperties.HTTP_BUFFER_SIZE));

        if (((String) props.get(HTTPListenerProperties.HTTP_KEEP_CONNECTION_OPEN)).equals(UIConstants.YES_OPTION))
            keepConnectionOpenYesRadio.setSelected(true);
        else
            keepConnectionOpenNoRadio.setSelected(true);
        
        updateResponseDropDown();
        responseFromTransformer.setSelectedItem((String) props.getProperty(HTTPListenerProperties.HTTP_RESPONSE_VALUE));
        
        if (((String) props.get(HTTPListenerProperties.HTTP_APPEND_PAYLOAD)).equals(UIConstants.YES_OPTION))
            appendPayloadYesRadio.setSelected(true);
        else
            appendPayloadNoRadio.setSelected(true);
        
        if (props.get(HTTPListenerProperties.HTTP_PAYLOAD_ENCODING).equals(HTTPListenerProperties.PAYLOAD_ENCODING_NONE))
            payloadURLEncodingComboBox.setSelectedItem(HTTPListenerProperties.PAYLOAD_ENCODING_NONE);
        else if (props.get(HTTPListenerProperties.HTTP_PAYLOAD_ENCODING).equals(HTTPListenerProperties.PAYLOAD_ENCODING_ENCODE))
            payloadURLEncodingComboBox.setSelectedItem(HTTPListenerProperties.PAYLOAD_ENCODING_ENCODE);
        else if (props.get(HTTPListenerProperties.HTTP_PAYLOAD_ENCODING).equals(HTTPListenerProperties.PAYLOAD_ENCODING_DECODE))
            payloadURLEncodingComboBox.setSelectedItem(HTTPListenerProperties.PAYLOAD_ENCODING_DECODE);
    }

    public Properties getDefaults()
    {
        return new HTTPListenerProperties().getDefaults();
    }

    public boolean checkProperties(Properties props)
    {
        resetInvalidProperties();
        boolean valid = true;
        
        if (((String) props.get(HTTPListenerProperties.HTTP_ADDRESS)).length() == 0)
        {
            valid = false;
            listenerAddressField.setBackground(UIConstants.INVALID_COLOR);   
        }
        if (((String) props.get(HTTPListenerProperties.HTTP_PORT)).length() == 0)
        {
            valid = false;
            listenerPortField.setBackground(UIConstants.INVALID_COLOR);            
        }
        if (((String) props.get(HTTPListenerProperties.HTTP_RECEIVE_TIMEOUT)).length() == 0)
        {
            valid = false;
            receiveTimeoutField.setBackground(UIConstants.INVALID_COLOR);            
        }
        if (((String) props.get(HTTPListenerProperties.HTTP_BUFFER_SIZE)).length() == 0)
        {
            valid = false;
            bufferSizeField.setBackground(UIConstants.INVALID_COLOR);            
        }
        
        return valid;
    }
    
    private void resetInvalidProperties()
    {
        listenerAddressField.setBackground(null);
        listenerPortField.setBackground(null);
        receiveTimeoutField.setBackground(null);
        bufferSizeField.setBackground(null);
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
        keepConnectionOpenGroup = new javax.swing.ButtonGroup();
        appendPayloadGroup = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        bufferSizeField = new com.webreach.mirth.client.ui.components.MirthTextField();
        receiveTimeoutField = new com.webreach.mirth.client.ui.components.MirthTextField();
        listenerAddressField = new com.webreach.mirth.client.ui.components.MirthTextField();
        listenerPortField = new com.webreach.mirth.client.ui.components.MirthTextField();
        keepConnectionOpenYesRadio = new com.webreach.mirth.client.ui.components.MirthRadioButton();
        keepConnectionOpenNoRadio = new com.webreach.mirth.client.ui.components.MirthRadioButton();
        jLabel38 = new javax.swing.JLabel();
        responseFromTransformer = new com.webreach.mirth.client.ui.components.MirthComboBox();
        appendPayloadYesRadio = new com.webreach.mirth.client.ui.components.MirthRadioButton();
        appendPayloadNoRadio = new com.webreach.mirth.client.ui.components.MirthRadioButton();
        jLabel6 = new javax.swing.JLabel();
        payloadURLEncodingComboBox = new com.webreach.mirth.client.ui.components.MirthComboBox();
        jLabel7 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jLabel1.setText("Listener Address:");

        jLabel2.setText("Listener Port:");

        jLabel3.setText("Receive Timeout (ms):");

        jLabel4.setText("Buffer Size:");

        jLabel5.setText("Keep Connection Open:");

        keepConnectionOpenYesRadio.setBackground(new java.awt.Color(255, 255, 255));
        keepConnectionOpenYesRadio.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        keepConnectionOpenGroup.add(keepConnectionOpenYesRadio);
        keepConnectionOpenYesRadio.setText("Yes");
        keepConnectionOpenYesRadio.setMargin(new java.awt.Insets(0, 0, 0, 0));

        keepConnectionOpenNoRadio.setBackground(new java.awt.Color(255, 255, 255));
        keepConnectionOpenNoRadio.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        keepConnectionOpenGroup.add(keepConnectionOpenNoRadio);
        keepConnectionOpenNoRadio.setText("No");
        keepConnectionOpenNoRadio.setMargin(new java.awt.Insets(0, 0, 0, 0));

        jLabel38.setText("Respond from:");

        responseFromTransformer.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        responseFromTransformer.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                responseFromTransformerActionPerformed(evt);
            }
        });

        appendPayloadYesRadio.setBackground(new java.awt.Color(255, 255, 255));
        appendPayloadYesRadio.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        appendPayloadGroup.add(appendPayloadYesRadio);
        appendPayloadYesRadio.setText("Yes");
        appendPayloadYesRadio.setMargin(new java.awt.Insets(0, 0, 0, 0));

        appendPayloadNoRadio.setBackground(new java.awt.Color(255, 255, 255));
        appendPayloadNoRadio.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        appendPayloadGroup.add(appendPayloadNoRadio);
        appendPayloadNoRadio.setText("No");
        appendPayloadNoRadio.setMargin(new java.awt.Insets(0, 0, 0, 0));

        jLabel6.setText("Append Payload Variable:");

        payloadURLEncodingComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "None", "Encode", "Decode" }));
        payloadURLEncodingComboBox.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                payloadURLEncodingComboBoxActionPerformed(evt);
            }
        });

        jLabel7.setText("Payload URL Encoding:");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabel7)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabel6)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabel38)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabel5)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabel4)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabel3)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabel2)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabel1))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(payloadURLEncodingComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 150, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(layout.createSequentialGroup()
                        .add(appendPayloadYesRadio, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(appendPayloadNoRadio, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(responseFromTransformer, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 150, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(layout.createSequentialGroup()
                        .add(keepConnectionOpenYesRadio, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(keepConnectionOpenNoRadio, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(bufferSizeField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 75, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(receiveTimeoutField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 75, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(listenerPortField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 50, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(listenerAddressField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 200, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(listenerAddressField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel1))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(listenerPortField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel2))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(receiveTimeoutField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel3))
                .add(8, 8, 8)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(bufferSizeField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel4))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(keepConnectionOpenYesRadio, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(keepConnectionOpenNoRadio, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel5))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel38)
                    .add(responseFromTransformer, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(appendPayloadYesRadio, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(appendPayloadNoRadio, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel6))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(payloadURLEncodingComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel7))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void payloadURLEncodingComboBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_payloadURLEncodingComboBoxActionPerformed
    {//GEN-HEADEREND:event_payloadURLEncodingComboBoxActionPerformed
// TODO add your handling code here:
    }//GEN-LAST:event_payloadURLEncodingComboBoxActionPerformed

    private void responseFromTransformerActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_responseFromTransformerActionPerformed
    {//GEN-HEADEREND:event_responseFromTransformerActionPerformed
        if (responseFromTransformer.getSelectedIndex() != 0 && !parent.channelEditPanel.synchronousCheckBox.isSelected())
        {
            parent.alertInformation("The synchronize source connector setting has been enabled since it is required to use this feature.");
            parent.channelEditPanel.synchronousCheckBox.setSelected(true);
        }
    }//GEN-LAST:event_responseFromTransformerActionPerformed
    
    public void updateResponseDropDown()
    {               
        boolean visible = parent.channelEditTasks.getContentPane().getComponent(0).isVisible();
        
        String selectedItem = (String) responseFromTransformer.getSelectedItem();
        
        Channel channel = parent.channelEditPanel.currentChannel;
        
        ArrayList<String> variables = new ArrayList<String>();
        
        variables.add("None");
        
        List<Step> stepsToCheck = new ArrayList<Step>();
        stepsToCheck.addAll(channel.getSourceConnector().getTransformer().getSteps());      
        
        for(Connector connector : channel.getDestinationConnectors())
        {
            variables.add(connector.getName());
            stepsToCheck.addAll(connector.getTransformer().getSteps());
        }       
               
        int i = 0;
        for (Iterator it = stepsToCheck.iterator(); it.hasNext();)
        {
            Step step = (Step) it.next();
            Map data;
            data = (Map) step.getData();
            
            if (step.getType().equalsIgnoreCase(TransformerPane.JAVASCRIPT_TYPE))
            {
                Pattern pattern = Pattern.compile(RESULT_PATTERN);
                Matcher matcher = pattern.matcher(step.getScript());
                while (matcher.find())
                {
                    String key = matcher.group(1);
                    variables.add(key);
                }
            }
            else if (step.getType().equalsIgnoreCase(TransformerPane.MAPPER_TYPE))
            {
                if(data.containsKey(UIConstants.IS_GLOBAL))
                {
                    if (((String) data.get(UIConstants.IS_GLOBAL)).equalsIgnoreCase(UIConstants.IS_GLOBAL_RESPONSE))
                        variables.add((String)data.get("Variable"));
                }
            }
        }
        
        responseFromTransformer.setModel(new DefaultComboBoxModel(variables.toArray()));
        
        if(variables.contains(selectedItem))
            responseFromTransformer.setSelectedItem(selectedItem);
        else
            responseFromTransformer.setSelectedIndex(0);
        
        parent.channelEditTasks.getContentPane().getComponent(0).setVisible(visible);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup appendPayloadGroup;
    private com.webreach.mirth.client.ui.components.MirthRadioButton appendPayloadNoRadio;
    private com.webreach.mirth.client.ui.components.MirthRadioButton appendPayloadYesRadio;
    private com.webreach.mirth.client.ui.components.MirthTextField bufferSizeField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.ButtonGroup keepConnectionOpenGroup;
    private com.webreach.mirth.client.ui.components.MirthRadioButton keepConnectionOpenNoRadio;
    private com.webreach.mirth.client.ui.components.MirthRadioButton keepConnectionOpenYesRadio;
    private com.webreach.mirth.client.ui.components.MirthTextField listenerAddressField;
    private com.webreach.mirth.client.ui.components.MirthTextField listenerPortField;
    private com.webreach.mirth.client.ui.components.MirthComboBox payloadURLEncodingComboBox;
    private com.webreach.mirth.client.ui.components.MirthTextField receiveTimeoutField;
    private com.webreach.mirth.client.ui.components.MirthComboBox responseFromTransformer;
    // End of variables declaration//GEN-END:variables

}