/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL
 * license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */

package com.mirth.connect.client.ui.browsers.message;

import java.awt.Component;
import java.text.SimpleDateFormat;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import org.jdesktop.swingx.table.ColumnFactory;
import org.jdesktop.swingx.table.TableColumnExt;

import com.mirth.connect.client.ui.DateCellRenderer;
import com.mirth.connect.client.ui.NumberCellRenderer;
import com.mirth.connect.client.ui.components.MirthTreeTable;

public class MessageBrowserTableColumnFactory extends ColumnFactory {
    @Override
    public TableColumnExt createAndConfigureTableColumn(TableModel model, int index) {
        TableColumnExt column = super.createAndConfigureTableColumn(model, index);
        TableCellRenderer renderer;
        
        switch (index) {
            case 0: // Message ID: Needs to be able to grow since it is a long
            case 5: // Send Attempts: Ditto
            case 6: // Import ID: Ditto
            	renderer = new NumberCellRenderer();
                column.setMaxWidth(500);
                column.setMinWidth(90);
                column.setPreferredWidth(90);
                break;
                
            case 2: // Status
            	column.setMaxWidth(85);
            	column.setMinWidth(85);
            	renderer = new DefaultTableCellRenderer();
                break;
                
            case 4: // Server Id
            	column.setMaxWidth(210);
            	column.setMinWidth(210);
            	renderer = new DefaultTableCellRenderer();
                break;
            	
            case 3: // Date Created
                DateCellRenderer dateCellRenderer = new DateCellRenderer();
                dateCellRenderer.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS"));
                renderer = dateCellRenderer;
                column.setMaxWidth(140);
                column.setMinWidth(140);
                break;
                
            default: renderer = new DefaultTableCellRenderer();
            	break;
            
        }
        column.setCellRenderer(new ItalicCellRenderer(renderer));
        return column;
    }
    
    private class ItalicCellRenderer implements TableCellRenderer {
    	private TableCellRenderer delegateRenderer;
    	
    	public ItalicCellRenderer(TableCellRenderer renderer) {
    		this.delegateRenderer = renderer;
    	}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			Component component = delegateRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			
			if (value != null && component instanceof JLabel) {
				JLabel label = (JLabel) component;
				MessageBrowserTableNode messageNode = (MessageBrowserTableNode) ((MirthTreeTable)table).getPathForRow(row).getLastPathComponent();
				if (!messageNode.isProcessed()) {
					label.setText("<html><i><font color='gray'>" + label.getText() + "</font></i></html>");
				}
			}
			return component;
		}
    	
    }
}