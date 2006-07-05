package com.webreach.mirth.client.ui.browsers.event;

import com.webreach.mirth.client.core.ClientException;
import com.webreach.mirth.client.ui.Frame;
import com.webreach.mirth.client.ui.PlatformUI;
import com.webreach.mirth.client.ui.UIConstants;
import com.webreach.mirth.model.SystemEvent;
import com.webreach.mirth.model.filters.SystemEventFilter;
import java.awt.Font;
import java.util.Calendar;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.AlternateRowHighlighter;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.ConditionalHighlighter;
import org.jdesktop.swingx.decorator.HighlighterPipeline;

public class EventBrowser extends javax.swing.JPanel
{
    private JScrollPane eventPane;
    private JXTable eventTable;
    private Frame parent;
    private List<SystemEvent> systemEventList;
    
    public EventBrowser()
    {
        this.parent = PlatformUI.MIRTH_FRAME;
        initComponents();
        
        String[] values = new String[SystemEvent.Level.values().length + 1];
        values[0] = "ALL";
        for (int i = 1; i < values.length; i++)
            values[i] = SystemEvent.Level.values()[i-1].toString();
        
        levelComboBox.setModel(new javax.swing.DefaultComboBoxModel(values));
        
        eventPane = new JScrollPane();
        
        eventPane.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                deselectRows();
            }
        });
        
        eventPane.setViewportView(eventTable);
        
        jPanel2.removeAll();  
        
        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(eventPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 526, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(eventPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)
        );
        
        jPanel2.updateUI();
    }
    
    public void loadNew()
    {
        // use the start filters and make the table.
        eventField.setText("");
        long currentTime = System.currentTimeMillis();
        mirthDatePicker1.setDateInMillis(currentTime);
        mirthDatePicker2.setDateInMillis(currentTime);
        
        filterButtonActionPerformed(null);
        clearDescription();
    }
    
    public void makeEventTable(SystemEventFilter filter) {
        eventTable = new JXTable();
        try 
        {
            systemEventList = parent.mirthClient.getSystemEvents(filter);
        } 
        catch (ClientException ex)
        {
            systemEventList = null;
            ex.printStackTrace();
        }
        
        if (systemEventList == null)
            return;
                
        Object[][] tableData = new Object[systemEventList.size()][4];
        
        for (int i=0; i < systemEventList.size(); i++)
        {
            SystemEvent systemEvent = systemEventList.get(i);
            
            tableData[i][0] = systemEvent.getId();
            
            Calendar calendar = systemEvent.getDate();
            
            tableData[i][1] = String.format("%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS", calendar);
            
            tableData[i][2] = systemEvent.getLevel().toString();
            tableData[i][3] = systemEvent.getEvent();
        }
                
        
        eventTable.setModel(new javax.swing.table.DefaultTableModel(
                tableData,
                new String []
        {
            "Event ID", "Date", "Level", "Event"
        }
        ) {
            boolean[] canEdit = new boolean []
            {
                false, false, false, false
            };
            
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        
        eventTable.setSelectionMode(0);

        eventTable.getColumnExt("Event ID").setMaxWidth(UIConstants.MAX_WIDTH);
        eventTable.getColumnExt("Date").setMaxWidth(UIConstants.MAX_WIDTH);
        eventTable.getColumnExt("Level").setMaxWidth(UIConstants.MAX_WIDTH);

        eventTable.packTable(UIConstants.COL_MARGIN);
        
        eventTable.setRowHeight(20);
        eventTable.setColumnMargin(2);
        eventTable.setOpaque(true);
        eventTable.setRowSelectionAllowed(true);
        HighlighterPipeline highlighter = new HighlighterPipeline();
        highlighter.addHighlighter(AlternateRowHighlighter.beige);
        eventTable.setHighlighters(highlighter);
        
        eventPane.setViewportView(eventTable);
        
        eventTable.getSelectionModel().addListSelectionListener(new ListSelectionListener()
        {
            public void valueChanged(ListSelectionEvent evt)
            {
                EventListSelected(evt);
            }
        });
    }
    
    public void deselectRows()
    {
        eventTable.clearSelection();
    }
    
    public void clearDescription()
    {
        description.setText("Select an event to see its description.");
    }
    
    private void EventListSelected(ListSelectionEvent evt)
    {
        if (!evt.getValueIsAdjusting())
        {
            int row = eventTable.getSelectedRow();

            if(row >= 0)
            {
                description.setText(systemEventList.get(row).getDescription() + "\n" + systemEventList.get(row).getAttributes());
                description.setCaretPosition(0);
            }
        }
    }
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        filterPanel = new javax.swing.JPanel();
        eventLabel = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        eventField = new javax.swing.JTextField();
        filterButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        mirthDatePicker1 = new com.webreach.mirth.client.ui.MirthDatePicker();
        mirthDatePicker2 = new com.webreach.mirth.client.ui.MirthDatePicker();
        levelComboBox = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        descriptionPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        description = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        filterPanel.setBackground(new java.awt.Color(255, 255, 255));
        filterPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Filter By", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(0, 0, 0)));
        eventLabel.setText("Event:");

        jLabel3.setText("Start Date:");

        filterButton.setText("Filter");
        filterButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filterButtonActionPerformed(evt);
            }
        });

        jLabel2.setText("End Date:");

        jLabel1.setText("Level:");

        org.jdesktop.layout.GroupLayout filterPanelLayout = new org.jdesktop.layout.GroupLayout(filterPanel);
        filterPanel.setLayout(filterPanelLayout);
        filterPanelLayout.setHorizontalGroup(
            filterPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(filterPanelLayout.createSequentialGroup()
                .add(37, 37, 37)
                .add(filterPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(filterPanelLayout.createSequentialGroup()
                        .add(eventLabel)
                        .add(6, 6, 6))
                    .add(filterPanelLayout.createSequentialGroup()
                        .add(filterPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(jLabel1)
                            .add(jLabel3))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)))
                .add(filterPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(filterButton)
                    .add(filterPanelLayout.createSequentialGroup()
                        .add(mirthDatePicker1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabel2)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(mirthDatePicker2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(filterPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, levelComboBox, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, eventField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE)))
                .addContainerGap(125, Short.MAX_VALUE))
        );
        filterPanelLayout.setVerticalGroup(
            filterPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(filterPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(filterPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jLabel2)
                    .add(filterPanelLayout.createSequentialGroup()
                        .add(filterPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(eventLabel)
                            .add(eventField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(filterPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(levelComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel1))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(filterPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(filterPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                .add(jLabel3)
                                .add(mirthDatePicker2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(mirthDatePicker1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 14, Short.MAX_VALUE)
                .add(filterButton)
                .addContainerGap())
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 564, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE)
        );

        descriptionPanel.setBackground(new java.awt.Color(255, 255, 255));
        description.setColumns(20);
        description.setEditable(false);
        description.setRows(5);
        description.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        jScrollPane2.setViewportView(description);

        jLabel4.setText("Description:");

        org.jdesktop.layout.GroupLayout descriptionPanelLayout = new org.jdesktop.layout.GroupLayout(descriptionPanel);
        descriptionPanel.setLayout(descriptionPanelLayout);
        descriptionPanelLayout.setHorizontalGroup(
            descriptionPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(descriptionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(descriptionPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel4)
                    .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 544, Short.MAX_VALUE))
                .addContainerGap())
        );
        descriptionPanelLayout.setVerticalGroup(
            descriptionPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(descriptionPanelLayout.createSequentialGroup()
                .add(jLabel4)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE)
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(filterPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(descriptionPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(filterPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(descriptionPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void filterButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filterButtonActionPerformed
        if (mirthDatePicker1.getDate() != null && mirthDatePicker2.getDate() != null)
        {
            if (mirthDatePicker1.getDateInMillis() > mirthDatePicker2.getDateInMillis())
            {
                JOptionPane.showMessageDialog(parent, "Start date cannot be after the end date.");
                return;
            }
        }
        
        SystemEventFilter filter = new SystemEventFilter();
        if (!eventField.getText().equals(""))
            filter.setEvent(eventField.getText());
        
        if (!((String)levelComboBox.getSelectedItem()).equalsIgnoreCase("ALL"))
        {
            for (int i = 0; i < SystemEvent.Level.values().length; i++)
            {
                if (((String)levelComboBox.getSelectedItem()).equalsIgnoreCase(SystemEvent.Level.values()[i].toString()))
                    filter.setLevel(SystemEvent.Level.values()[i]);
            }
        }
        
        if (mirthDatePicker1.getDate() != null)
        {
            Calendar calendarStart = Calendar.getInstance();
            calendarStart.setTimeInMillis(mirthDatePicker1.getDateInMillis());
            filter.setStartDate(calendarStart);
        }
        if (mirthDatePicker2.getDate() != null)
        {
            Calendar calendarEnd = Calendar.getInstance();
            calendarEnd.setTimeInMillis(mirthDatePicker2.getDateInMillis());
            filter.setEndDate(calendarEnd);
        }
        
        makeEventTable(filter);
    }//GEN-LAST:event_filterButtonActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea description;
    private javax.swing.JPanel descriptionPanel;
    private javax.swing.JTextField eventField;
    private javax.swing.JLabel eventLabel;
    private javax.swing.JButton filterButton;
    private javax.swing.JPanel filterPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JComboBox levelComboBox;
    private com.webreach.mirth.client.ui.MirthDatePicker mirthDatePicker1;
    private com.webreach.mirth.client.ui.MirthDatePicker mirthDatePicker2;
    // End of variables declaration//GEN-END:variables
    
}
