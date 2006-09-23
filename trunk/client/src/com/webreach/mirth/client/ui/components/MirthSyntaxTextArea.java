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


package com.webreach.mirth.client.ui.components;

import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;

import com.webreach.mirth.client.ui.Frame;
import com.webreach.mirth.client.ui.PlatformUI;
import com.webreach.mirth.client.ui.actions.CopyAction;
import com.webreach.mirth.client.ui.actions.CutAction;
import com.webreach.mirth.client.ui.actions.DeleteAction;
import com.webreach.mirth.client.ui.actions.PasteAction;
import com.webreach.mirth.client.ui.actions.SelectAllAction;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.ElementIterator;

import org.syntax.jedit.JEditTextArea;
import org.syntax.jedit.SyntaxDocument;

/**
 * Mirth's implementation of the JTextArea. Adds enabling of the save button in
 * parent. Also adds a trigger button (right click) editor menu with Cut, Copy,
 * Paste, Delete, and Select All.
 */
public class MirthSyntaxTextArea extends JEditTextArea implements MirthTextInterface {
	private Frame parent;

	//private JPopupMenu menu;

	private CutAction cutAction;

	private CopyAction copyAction;

	private PasteAction pasteAction;

	private DeleteAction deleteAction;

	private SelectAllAction selectAllAction;

	public MirthSyntaxTextArea(boolean lineNumbers) {
		super(lineNumbers);
		this.parent = PlatformUI.MIRTH_FRAME;

		cutAction = new CutAction(this);
		copyAction = new CopyAction(this);
		pasteAction = new PasteAction(this);
		deleteAction = new DeleteAction(this);
		selectAllAction = new SelectAllAction(this);

		popup = new JPopupMenu();
		popup.add(cutAction);
		popup.add(copyAction);
		popup.add(pasteAction);
		popup.add(deleteAction);
		popup.addSeparator();
		popup.add(selectAllAction);
		this.popupHandler = new PopUpHandler() {
		
			public void showPopupMenu(JPopupMenu menu, MouseEvent evt) {
				if (evt.isPopupTrigger()) {
					menu.getComponent(0).setEnabled(cutAction.isEnabled());
					menu.getComponent(1).setEnabled(copyAction.isEnabled());
					menu.getComponent(2).setEnabled(pasteAction.isEnabled());
					menu.getComponent(3).setEnabled(deleteAction.isEnabled());
					menu.getComponent(5).setEnabled(selectAllAction.isEnabled());

					menu.show(evt.getComponent(), evt.getX(), evt.getY());
				}
		
			}
		
		};
		/*
		this.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mousePressed(java.awt.event.MouseEvent evt) {
				showPopupMenu(evt);
			}

			public void mouseReleased(java.awt.event.MouseEvent evt) {
				showPopupMenu(evt);
			}
		});
		*/
	}

	/**
	 * Shows the popup menu for the trigger button
	 */
	//private void showPopupMenu(JPopupMenu menu, java.awt.event.MouseEvent evt) {
	//
	//}

	/**
	 * Overrides setDocument(Document doc) so that a document listener is added
	 * to the current document to listen for changes.
	 */
	public void setDocument(SyntaxDocument doc) {
		
		super.setDocument(doc);
		this.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
			}

			public void removeUpdate(DocumentEvent e) {
				parent.enableSave();
			}

			public void insertUpdate(DocumentEvent e) {
				parent.enableSave();
			}
		});
		
	}

	/**
	 * Overrides setText(String t) so that the save button is disabled when
	 * Mirth sets the text of a field.
	 */
    public void setText(String t)
    {
    	super.setText(t);
        parent.disableSave();
    }
    public String getText() {
		StringBuffer sb = new StringBuffer();
		// Get paragraph element
		Element paragraph = getDocument().getDefaultRootElement();

		// Get number of content elements
		int contentCount = paragraph.getElementCount();

		// Get index ranges for each content element.
		// Each content element represents one line.
		// Each line includes the terminating newline.
		for (int i = 0; i < contentCount; i++) {
			Element e = paragraph.getElement(i);
			int rangeStart = e.getStartOffset();
			int rangeEnd = e.getEndOffset();
			try {
				String text = getText(rangeStart, rangeEnd - rangeStart);
				sb.append(text.replaceAll("\\n", ""));
				sb.append("\r");
				
			} catch (Exception ex) {
			}

		}
		String retval = sb.toString();
		if (retval.length() > 0) {
			retval = retval.substring(0, retval.length() -1);
		}
		return retval;

	}
	public void replaceSelection(String text) {
		setSelectedText(text);
		
	}
}
