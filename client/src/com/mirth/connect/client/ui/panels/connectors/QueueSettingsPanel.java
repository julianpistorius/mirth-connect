/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL
 * license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */

package com.mirth.connect.client.ui.panels.connectors;

import org.apache.commons.lang.math.NumberUtils;

import com.mirth.connect.client.ui.ChannelSetup;
import com.mirth.connect.client.ui.UIConstants;
import com.mirth.connect.client.ui.components.MirthFieldConstraints;
import com.mirth.connect.donkey.model.channel.QueueConnectorProperties;
import com.mirth.connect.model.MessageStorageMode;

public class QueueSettingsPanel extends javax.swing.JPanel {
    private ChannelSetup channelSetup;
    
    public QueueSettingsPanel() {
        initComponents();
        retryIntervalField.setDocument(new MirthFieldConstraints(0, false, false, true));
        retryCountField.setDocument(new MirthFieldConstraints(0, false, false, true));
    }

    public void setChannelSetup(ChannelSetup channelSetup) {
        this.channelSetup = channelSetup;
    }

    public void setProperties(QueueConnectorProperties properties) {
        if (properties.isQueueEnabled()) {
            if (properties.isSendFirst()) {
                queueAttemptFirstRadio.setSelected(true);
                queueAttemptFirstRadioActionPerformed(null);
            } else {
                queueAlwaysRadio.setSelected(true);
                queueAlwaysRadioActionPerformed(null);
            }
        } else {
            queueNeverRadio.setSelected(true);
            queueNeverRadioActionPerformed(null);
        }

        retryIntervalField.setText(String.valueOf(properties.getRetryIntervalMillis()));
        retryCountLabel.setEnabled(true);
        retryCountField.setEnabled(true);
        retryCountField.setText(Integer.toString(properties.getRetryCount()));
    }

    public void fillProperties(QueueConnectorProperties properties) {

        if (queueAlwaysRadio.isSelected()) {
            properties.setQueueEnabled(true);
            properties.setSendFirst(false);
        } else if (queueNeverRadio.isSelected()) {
            properties.setQueueEnabled(false);
            properties.setSendFirst(false);
        } else {
            properties.setQueueEnabled(true);
            properties.setSendFirst(true);
        }

        properties.setRegenerateTemplate(regenerateTemplateCheckbox.isSelected());

        properties.setRetryIntervalMillis(NumberUtils.toInt(retryIntervalField.getText(), -1));
        
        properties.setRetryCount(NumberUtils.toInt(retryCountField.getText(), -1));
    }

    public boolean checkProperties(QueueConnectorProperties properties, boolean highlight) {
        boolean valid = true;

        // TODO: Queue properties checks don't work properly with ints
        if (properties.isQueueEnabled() || properties.getRetryCount() > 0) {
            if (properties.getRetryIntervalMillis() <= 0) {
                valid = false;
                if (highlight) {
                    retryIntervalField.setBackground(UIConstants.INVALID_COLOR);
                }
            }
        }
        
        if (properties.getRetryCount() < 0) {
            valid = false;
            
            if (highlight) {
                retryCountField.setBackground(UIConstants.INVALID_COLOR);
            }
        }

        return valid;
    }

    public void resetInvalidProperties() {
        retryIntervalField.setBackground(null);
        retryCountField.setBackground(null);
    }
    
    public void updateQueueWarning(MessageStorageMode messageStorageMode) {
        switch (messageStorageMode) {
            case RAW: case METADATA: case DISABLED:
                if (queueAlwaysRadio.isSelected() || queueAttemptFirstRadio.isSelected()) {
                    queueWarningLabel.setText("<html>Queueing is not supported by the current message storage mode</html>");
                } else {
                    queueWarningLabel.setText("");
                }
                break;
                
            default:
                queueWarningLabel.setText("");
                break;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        queueButtonGroup = new javax.swing.ButtonGroup();
        regenerateTemplateButtonGroup = new javax.swing.ButtonGroup();
        retryButtonGroup = new javax.swing.ButtonGroup();
        queueMessagesLabel = new javax.swing.JLabel();
        queueAlwaysRadio = new com.mirth.connect.client.ui.components.MirthRadioButton();
        retryIntervalLabel = new javax.swing.JLabel();
        retryIntervalField = new com.mirth.connect.client.ui.components.MirthTextField();
        queueNeverRadio = new com.mirth.connect.client.ui.components.MirthRadioButton();
        queueAttemptFirstRadio = new com.mirth.connect.client.ui.components.MirthRadioButton();
        retryCountLabel = new javax.swing.JLabel();
        retryCountField = new com.mirth.connect.client.ui.components.MirthTextField();
        regenerateTemplateCheckbox = new com.mirth.connect.client.ui.components.MirthCheckBox();
        queueWarningLabel = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, new java.awt.Color(204, 204, 204)), "Queue/Retry Settings", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        queueMessagesLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        queueMessagesLabel.setText("Queue Messages:");

        queueAlwaysRadio.setBackground(new java.awt.Color(255, 255, 255));
        queueAlwaysRadio.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        queueButtonGroup.add(queueAlwaysRadio);
        queueAlwaysRadio.setText("Always");
        queueAlwaysRadio.setToolTipText("");
        queueAlwaysRadio.setMargin(new java.awt.Insets(0, 0, 0, 0));
        queueAlwaysRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                queueAlwaysRadioActionPerformed(evt);
            }
        });

        retryIntervalLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        retryIntervalLabel.setText("Retry Interval (ms):");

        retryIntervalField.setToolTipText("<html>The amount of time that should elapse between retry attempts to send messages in the queue.</html>");

        queueNeverRadio.setBackground(new java.awt.Color(255, 255, 255));
        queueNeverRadio.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        queueButtonGroup.add(queueNeverRadio);
        queueNeverRadio.setText("Never");
        queueNeverRadio.setToolTipText("");
        queueNeverRadio.setMargin(new java.awt.Insets(0, 0, 0, 0));
        queueNeverRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                queueNeverRadioActionPerformed(evt);
            }
        });

        queueAttemptFirstRadio.setBackground(new java.awt.Color(255, 255, 255));
        queueAttemptFirstRadio.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        queueButtonGroup.add(queueAttemptFirstRadio);
        queueAttemptFirstRadio.setSelected(true);
        queueAttemptFirstRadio.setText("Attempt First");
        queueAttemptFirstRadio.setToolTipText("");
        queueAttemptFirstRadio.setMargin(new java.awt.Insets(0, 0, 0, 0));
        queueAttemptFirstRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                queueAttemptFirstRadioActionPerformed(evt);
            }
        });

        retryCountLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        retryCountLabel.setText("Retry Count:");

        retryCountField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                retryCountFieldKeyReleased(evt);
            }
        });

        regenerateTemplateCheckbox.setBackground(new java.awt.Color(255, 255, 255));
        regenerateTemplateCheckbox.setText("Regenerate Template");

        queueWarningLabel.setForeground(new java.awt.Color(255, 0, 0));
        queueWarningLabel.setText("<html>test text</html>");
        queueWarningLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(queueMessagesLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(queueAlwaysRadio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(queueAttemptFirstRadio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(queueNeverRadio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(regenerateTemplateCheckbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(retryIntervalLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(retryCountLabel, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(retryCountField, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(retryIntervalField, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(queueWarningLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(queueMessagesLabel)
                    .addComponent(queueAlwaysRadio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(queueAttemptFirstRadio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(queueNeverRadio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(regenerateTemplateCheckbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(retryCountLabel)
                            .addComponent(retryCountField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(retryIntervalLabel)
                            .addComponent(retryIntervalField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(queueWarningLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(13, 13, 13))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void queueAlwaysRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_queueAlwaysRadioActionPerformed
        regenerateTemplateCheckbox.setEnabled(true);
        retryIntervalField.setEnabled(true);
        retryIntervalLabel.setEnabled(true);
        channelSetup.saveDestinationPanel();
        
        MessageStorageMode messageStorageMode = channelSetup.getMessageStorageMode();
        channelSetup.updateQueueWarning(messageStorageMode);
        updateQueueWarning(messageStorageMode);
    }//GEN-LAST:event_queueAlwaysRadioActionPerformed

    private void queueNeverRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_queueNeverRadioActionPerformed
        regenerateTemplateCheckbox.setEnabled(false);
        
        if (NumberUtils.toInt(retryCountField.getText()) == 0) {
            retryIntervalField.setEnabled(false);
            retryIntervalLabel.setEnabled(false);
        }
        
        channelSetup.saveDestinationPanel();

        MessageStorageMode messageStorageMode = channelSetup.getMessageStorageMode();
        channelSetup.updateQueueWarning(messageStorageMode);
        updateQueueWarning(messageStorageMode);
    }//GEN-LAST:event_queueNeverRadioActionPerformed

    private void queueAttemptFirstRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_queueAttemptFirstRadioActionPerformed
        regenerateTemplateCheckbox.setEnabled(true);
        retryIntervalField.setEnabled(true);
        retryIntervalLabel.setEnabled(true);
        channelSetup.saveDestinationPanel();
        
        MessageStorageMode messageStorageMode = channelSetup.getMessageStorageMode();
        channelSetup.updateQueueWarning(messageStorageMode);
        updateQueueWarning(messageStorageMode);
    }//GEN-LAST:event_queueAttemptFirstRadioActionPerformed

    private void retryCountFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_retryCountFieldKeyReleased
        if (NumberUtils.toInt(retryCountField.getText()) > 0) {
            retryIntervalField.setEnabled(true);
            retryIntervalLabel.setEnabled(true);
        } else if (queueNeverRadio.isSelected()) {
            retryIntervalField.setEnabled(false);
            retryIntervalLabel.setEnabled(false);
        }
    }//GEN-LAST:event_retryCountFieldKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.mirth.connect.client.ui.components.MirthRadioButton queueAlwaysRadio;
    private com.mirth.connect.client.ui.components.MirthRadioButton queueAttemptFirstRadio;
    private javax.swing.ButtonGroup queueButtonGroup;
    private javax.swing.JLabel queueMessagesLabel;
    private com.mirth.connect.client.ui.components.MirthRadioButton queueNeverRadio;
    private javax.swing.JLabel queueWarningLabel;
    private javax.swing.ButtonGroup regenerateTemplateButtonGroup;
    private com.mirth.connect.client.ui.components.MirthCheckBox regenerateTemplateCheckbox;
    private javax.swing.ButtonGroup retryButtonGroup;
    private com.mirth.connect.client.ui.components.MirthTextField retryCountField;
    private javax.swing.JLabel retryCountLabel;
    private com.mirth.connect.client.ui.components.MirthTextField retryIntervalField;
    private javax.swing.JLabel retryIntervalLabel;
    // End of variables declaration//GEN-END:variables
}
