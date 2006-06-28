/** TransformerPane.java
 *
 *  @author franciscos 
 *  Created on May 26, 2006, 5:08 PM
 */


package com.webreach.mirth.client.ui.editors.transformer;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;
import org.jdesktop.swingx.action.ActionFactory;
import org.jdesktop.swingx.action.BoundAction;
import org.jdesktop.swingx.decorator.AlternateRowHighlighter;
import org.jdesktop.swingx.decorator.HighlighterPipeline;
import com.webreach.mirth.client.ui.Frame;
import com.webreach.mirth.model.Transformer;
import com.webreach.mirth.model.Step;
import com.webreach.mirth.client.ui.Constants;
import com.webreach.mirth.client.ui.editors.*;



public class TransformerPane extends JPanel {	
	
	/** CONSTRUCTOR
     * 
     *  Frame - the parent where this panel & its tasks will be loaded
     *  Transformer - the data model
     */
    public TransformerPane( Frame p ) {
        parent = p;
        prevSelRow = -1; 	// no row by default
        initComponents();
    }
    
    /** load( Transformer t )
     *  now that the components have been initialized...
     */
    public void load( Transformer t ) {
    	transformer = t;
    	
    	// we need to clear all the old data before we load the new
        makeTransformerTable();
        
    	// add any existing steps to the model
        List<Step> list = transformer.getSteps();
        ListIterator<Step> li = list.listIterator();
        while ( li.hasNext() ) {
        	Step s = li.next();
        	int row = s.getSequenceNumber();
        	setRowData( s, row );
        }
        
    	int rowCount = transformerTableModel.getRowCount();

        // select the first row if there is one
		if ( rowCount > 0 ) {
			transformerTable.setRowSelectionInterval( 0, 0 );
			prevSelRow = 0;
			System.out.println("prevSelRow: " + prevSelRow );
		} else
			stepPanel.showCard( BLANK_TYPE );
    	
    	parent.setCurrentContentPage( this );
    	parent.setCurrentTaskPaneContainer( transformerTaskPaneContainer );
    	
    	updateStepNumbers();
    }
    
    /** This method is called from within the constructor to
     *  initialize the form.
     */
    public void initComponents() {
        
        // the available panels (cards)
        stepPanel = new CardPanel();
        blankPanel = new BlankPanel();
        mapperPanel = new MapperPanel();
        jsPanel = new JavaScriptPanel();
        smtpPanel = new SMTPPanel();
        jdbcPanel = new JDBCPanel();
        alertPanel = new AlertPanel();
        // 		establish the cards to use in the Transformer
        stepPanel.addCard( blankPanel, BLANK_TYPE );
        stepPanel.addCard( mapperPanel, MAPPER_TYPE );
        stepPanel.addCard( jsPanel, JAVASCRIPT_TYPE );
        stepPanel.addCard( smtpPanel, SMTP_TYPE );
        stepPanel.addCard( jdbcPanel, JDBC_TYPE );
        stepPanel.addCard( alertPanel, ALERT_TYPE );

        transformerTablePane = new JScrollPane();
        
        // make and place the task pane in the parent Frame
	    transformerTaskPaneContainer = new JXTaskPaneContainer();
	    
	    viewTasks = new JXTaskPane();
        viewTasks.setTitle("Mirth Views");
        viewTasks.setFocusable(false);
        viewTasks.add(initActionCallback( "accept",
        		ActionFactory.createBoundAction( "accept", "Back to Channels", "B" ), 
        		new ImageIcon( Frame.class.getResource( "images/resultset_previous.png" )) ));
        parent.setNonFocusable(viewTasks);
        transformerTaskPaneContainer.add(viewTasks);
	    
	    transformerTasks = new JXTaskPane();
	    transformerTasks.setTitle( "Transformer Tasks" );
	    transformerTasks.setFocusable( false );
        
        // add new step task
        transformerTasks.add( initActionCallback( "addNewStep",
        		ActionFactory.createBoundAction( "addNewStep", "Add New Step", "N" ),
        		new ImageIcon( Frame.class.getResource( "images/add.png" )) ));
        // delete step task
        transformerTasks.add( initActionCallback( "deleteStep",
        		ActionFactory.createBoundAction( "deleteStep", "Delete Step", "X" ),
        		new ImageIcon( Frame.class.getResource( "images/delete.png" )) )); 
	    // move step up task
	    transformerTasks.add( initActionCallback( "moveStepUp",
	    		ActionFactory.createBoundAction( "moveStepUp", "Move Step Up", "U" ),
	    		new ImageIcon( Frame.class.getResource( "images/arrow_up.png" )) ));
	    // move step down task
        transformerTasks.add( initActionCallback( "moveStepDown",
        		ActionFactory.createBoundAction( "moveStepDown", "Move Step Down", "D" ),
        		new ImageIcon( Frame.class.getResource( "images/arrow_down.png" )) ));
        // add the tasks to the taskpane, and the taskpane to the mirth client
        transformerTaskPaneContainer.add( transformerTasks );
        parent.setNonFocusable( transformerTasks );
        parent.setCurrentTaskPaneContainer( transformerTaskPaneContainer );
        
        makeTransformerTable();

        // BGN LAYOUT
        transformerTablePane.setBorder( BorderFactory.createEmptyBorder() );
        stepPanel.setBorder( BorderFactory.createEmptyBorder() );
        vSplitPane = new JSplitPane( JSplitPane.VERTICAL_SPLIT,
         		transformerTablePane, stepPanel );
        vSplitPane.setContinuousLayout( true );
        vSplitPane.setDividerLocation( 200 );
        this.setLayout( new BorderLayout() );
        this.add( vSplitPane, BorderLayout.CENTER );
        this.setBorder( BorderFactory.createEmptyBorder() );
        // END LAYOUT
        
        parent.setCurrentContentPage( this );
        
        updateTaskPane();

        // select the first row if there is one
		if ( transformerTableModel.getRowCount() > 0 ) {
			transformerTable.setRowSelectionInterval( 0, 0 );
			prevSelRow = 0;
		}
        
    }  // END initComponents()
    
    public void makeTransformerTable() {
        transformerTable = new JXTable();
        
        transformerTable.setModel(new DefaultTableModel( 
        		new String [] { "#", "Name", "Type", "Data" }, 0 ) {	// Data column is hidden
            		boolean[] canEdit = new boolean [] {
            				false, true, true, true
            		};
            
            		public boolean isCellEditable( int rowIndex, int columnIndex ) {
            			return canEdit[columnIndex];
            		}
        		});
        
        transformerTableModel = (DefaultTableModel)transformerTable.getModel();

        String[] comboBoxValues = new String[] { 
        		MAPPER_TYPE, JAVASCRIPT_TYPE, SMTP_TYPE, JDBC_TYPE, ALERT_TYPE };
        
        // Set the combobox editor on the type column, and add action listener
	    MyComboBoxEditor comboBox = new MyComboBoxEditor( comboBoxValues );
	    ((JComboBox)comboBox.getComponent()).addItemListener( new ItemListener() {
            public void itemStateChanged( ItemEvent evt ) {            	
            	String type = evt.getItem().toString();
            	stepPanel.showCard( type );
            }
        });
	    
	    transformerTable.setSelectionMode( 0 );		// only select one row at a time
        transformerTable.getColumnExt( STEP_NUMBER_COL ).setMaxWidth( 30 );
        transformerTable.getColumnExt( STEP_NUMBER_COL ).setMinWidth( 30 );
        transformerTable.getColumnExt( STEP_TYPE_COL ).setMaxWidth( 150 );
        transformerTable.getColumnExt( STEP_TYPE_COL ).setMinWidth( 80 );
        transformerTable.getColumnExt( STEP_TYPE_COL ).setCellEditor( comboBox );
        transformerTable.getColumnExt( STEP_DATA_COL ).setVisible( false );
        
        transformerTable.setSortable( false );
        transformerTable.setRowHeight( Constants.ROW_HEIGHT );
        transformerTable.setColumnMargin( Constants.COL_MARGIN );
        transformerTable.setOpaque( true );
        transformerTable.setRowSelectionAllowed( true );
        HighlighterPipeline highlighter = new HighlighterPipeline();
        highlighter.addHighlighter( AlternateRowHighlighter.beige );
        transformerTable.setHighlighters( highlighter );
        transformerTable.setBorder( BorderFactory.createEmptyBorder() );
        transformerTablePane.setBorder( BorderFactory.createEmptyBorder() );
        
        transformerTablePane.setViewportView( transformerTable );
        
        transformerTable.getSelectionModel().addListSelectionListener(
        		new ListSelectionListener() {
            public void valueChanged( ListSelectionEvent evt ) {
            	if ( !saving ) TransformerListSelected(evt);
            }
        });
    }    
    
    // for the task pane
    public BoundAction initActionCallback( 
    		String callbackMethod, BoundAction boundAction, ImageIcon icon ) {
    	
        if(icon != null) boundAction.putValue(Action.SMALL_ICON, icon);
        boundAction.registerCallback(this,callbackMethod);
        return boundAction;
    }

    // called when a table row is (re)selected
    private void TransformerListSelected( ListSelectionEvent evt ) {
        int row = transformerTable.getSelectedRow();
        int last = evt.getLastIndex();
        String type = "";

    	saveData();
    	
        if ( isValid( row ) ) {
        	type = (String)transformerTable.getValueAt( row, STEP_TYPE_COL );
        	transformerTable.setRowSelectionInterval( row, row );
        	prevSelRow = row;
        } else if ( isValid ( last ) ) {
        	type = (String)transformerTable.getValueAt( last, STEP_TYPE_COL );
        	transformerTable.setRowSelectionInterval( last, last );
        	prevSelRow = last;
        }

        stepPanel.showCard( type );
    	loadData();
    	
        updateTaskPane();
    }
    
	// returns true if the row is a valid index in the existing model
    private boolean isValid( int row ) {
    	return ( row >= 0 && row < transformerTableModel.getRowCount() );
    }
    
    // returns true if the variable name is unique
    private boolean isUnique( String var ) {
    	boolean unique = true;
    	
    	for ( int i = 0;  i < transformerTableModel.getRowCount();  i++ ) {
    		String temp = "";
    		
    		Map<Object,Object> data = 
    			(Map<Object,Object>)transformerTableModel.getValueAt( i, STEP_DATA_COL );
    		
    		if ( data != null ) temp = (String)data.get( "Variable" );
    		
    		if ( var.trim().equalsIgnoreCase( temp ) ) unique = false;
    	}
    		
    	return unique;
    }
    
    // returns a unique default var name
    private String getUniqueName() {
    	String base = "$newVar";
    	int i = 0;
    	
    	while ( true ) {
    		String var = base + i;
    		if ( isUnique( var ) ) return var;
    		i++;
    	}
    }
    
    // sets the data from the previously used panel into the
    // previously selected Step object
    private void saveData() {
    	if ( transformerTable.isEditing() )
    		transformerTable.getCellEditor( transformerTable.getEditingRow(), 
    				transformerTable.getEditingColumn() ).stopCellEditing();
    	
    	saving = true;
    	
    	if ( isValid( prevSelRow ) ) {
    		Map<Object, Object> data = new HashMap<Object, Object>();
        	String type = (String)
        			transformerTable.getValueAt( prevSelRow, STEP_TYPE_COL );
    	
	    	if ( type == MAPPER_TYPE ) data = mapperPanel.getData();
	    	else if ( type == JAVASCRIPT_TYPE ) data = jsPanel.getData();
	    	else if ( type == SMTP_TYPE ) data = smtpPanel.getData();
	    	else if ( type == JDBC_TYPE ) data = jdbcPanel.getData();
	    	else if ( type == ALERT_TYPE ) data = alertPanel.getData();
    	
	    	// save the data to the the most recently selected Step
	    	transformerTableModel.setValueAt( data, prevSelRow, STEP_DATA_COL );
    	}
    		
    	saving = false;
    }
        
    // loads the data object into the correct panel
    private void loadData() {
    	int row = transformerTable.getSelectedRow();
    	
    	if ( isValid( row ) ) {
    		String type = (String)transformerTableModel.getValueAt( row, STEP_TYPE_COL );
	    	Map<Object, Object> data = (Map<Object, Object>)
					transformerTableModel.getValueAt( row, STEP_DATA_COL );
	    	
	    	if ( type == MAPPER_TYPE ) mapperPanel.setData( data );
	    	else if ( type == JAVASCRIPT_TYPE ) jsPanel.setData( data );
	    	else if ( type == SMTP_TYPE ) smtpPanel.setData( data );
	    	else if ( type == JDBC_TYPE ) jdbcPanel.setData( data );
	    	else if ( type == ALERT_TYPE ) alertPanel.setData( data );
    	}
    }

    private void setRowData( Step step, int row ) {
    	Object[] tableData = new Object[NUMBER_OF_COLUMNS];
        
    	tableData[STEP_NUMBER_COL] = step.getSequenceNumber();
    	tableData[STEP_NAME_COL] = step.getName();
    	tableData[STEP_TYPE_COL] = step.getType();
    	tableData[STEP_DATA_COL] = step.getData();

    	transformerTableModel.addRow( tableData );
    	transformerTable.setRowSelectionInterval( row, row );
        
    }
    
    /** void addNewStep()
     *  add a new step to the end of the list
     */
    public void addNewStep() {
    	int row = transformerTable.getRowCount();
    	Step step = new Step();
    	Map<Object,Object> data = new HashMap<Object,Object>();
    	
    	data.put("Variable", getUniqueName() );
    	
    	step.setSequenceNumber( row );
		step.setName( "New Step" );
		step.setType( MAPPER_TYPE );	// mapper type by default
		step.setData( data );

		setRowData( step, row );
    	prevSelRow = row;
    }
    
    /** void deleteStep(MouseEvent evt)
     *  delete all selected rows
     */
    public void deleteStep() {
    	int row = transformerTable.getSelectedRow();
    	if ( isValid( row ) )
    		transformerTableModel.removeRow( row );
    	
    	if ( isValid ( row ) )
    		transformerTable.setRowSelectionInterval( row, row );
    	else if ( isValid( row - 1 ) )
    		transformerTable.setRowSelectionInterval( row - 1, row - 1 );
    	else 
    		stepPanel.showCard( BLANK_TYPE );
    	
    	updateStepNumbers();
    }
    
    public void moveStepUp() { moveStep( -1 ); }
    public void moveStepDown() { moveStep( 1 ); }
    
    /** void moveStep( int i )
     *  move the selected row i places
     */
    public void moveStep( int i ) {
    	int selRow = transformerTable.getSelectedRow();
    	int moveTo = selRow + i;
    	
    	saveData();
    	
    	// we can't move past the first or last row
    	if ( moveTo >= 0 && moveTo < transformerTable.getRowCount() ) {
        	transformerTableModel.moveRow( selRow, selRow, moveTo );
        	transformerTable.setRowSelectionInterval( moveTo, moveTo );
    	}
    	
    	updateStepNumbers();
    }
    
    /** void accept(MouseEvent evt)
     *  returns a vector of vectors to the caller of this.
     */
    public void accept() {
    	System.out.println(prevSelRow);
    	saveData();
    	List<Step> list = new ArrayList<Step>();
    	for ( int i = 0;  i < transformerTable.getRowCount();  i++ ) {
    		Step step = new Step();
    		step.setSequenceNumber( Integer.parseInt(
    				transformerTable.getValueAt( i, STEP_NUMBER_COL ).toString() ));
    		step.setName( (String)transformerTableModel.getValueAt( i, STEP_NAME_COL ));
    		step.setType( (String)transformerTableModel.getValueAt( i, STEP_TYPE_COL ));
    		step.setData( (Object)transformerTableModel.getValueAt( i, STEP_DATA_COL ));
    		
    		list.add( step );
    	}
    	
    	transformer.setSteps( list );
    	transformerTableModel.setDataVector( null, new String[] {} );
    
    	// reset the task pane and content to channel edit page
    	parent.channelEditPage.setSourceVariableList();
    	parent.channelEditPage.setDestinationVariableList();
    	parent.setCurrentContentPage( parent.channelEditPage );
    	parent.setCurrentTaskPaneContainer( parent.taskPaneContainer );
    	//if ( modified ) parent.showSaveButton();
    }
    
    /** void updateStepNumbers()
     *  traverses the table and updates all data numbers, both in the model
     *  and the view, after any change to the table
     */
    private void updateStepNumbers() {    	
    	int rowCount = transformerTableModel.getRowCount();
    	for ( int i = 0;  i < rowCount;  i++ )
    		transformerTableModel.setValueAt( i, i, STEP_NUMBER_COL );
    }
    
    /** updateTaskPane()
     *  configure the task pane so that it shows only relevant tasks
     */
    private void updateTaskPane() {
    	int rowCount = transformerTableModel.getRowCount();
    	if ( rowCount <= 0 )
        	parent.setVisibleTasks( transformerTasks, 1, -1, false );
        else if ( rowCount == 1 ) {
        	parent.setVisibleTasks( transformerTasks, 0, -1, true );
        	parent.setVisibleTasks( transformerTasks, 2, -1, false );
        } else 
        	parent.setVisibleTasks( transformerTasks, 0, -1, true );
    }
    
    
//............................................................................\\
    
    // the passed arguments to the constructor
    private Frame parent;
    private Transformer transformer;
    
    // fields
    private JXTable transformerTable;
    private DefaultTableModel transformerTableModel;
    private JScrollPane transformerTablePane;
    private JSplitPane vSplitPane;
    private boolean saving;				// allow the selection listener to breathe
    JXTaskPaneContainer transformerTaskPaneContainer;
    JXTaskPane transformerTasks;
    JXTaskPane viewTasks;
    
    
    // this little sucker is used to track the last row that had
    // focus after a new row is selected
    private int prevSelRow;
     
    // panels using CardLayout
    protected CardPanel stepPanel;			// the card holder
    protected BlankPanel blankPanel;		// the cards
    protected MapperPanel mapperPanel; 		//    \/
    protected JavaScriptPanel jsPanel;      //    \/
    protected SMTPPanel smtpPanel;      	//    \/
    protected JDBCPanel jdbcPanel;			//    \/
    protected AlertPanel alertPanel;		//    \/
    
    // transformer constants
    public static final int STEP_NUMBER_COL  = 0;
    public static final int STEP_NAME_COL  = 1;
    public static final int STEP_TYPE_COL  = 2;
    public static final int STEP_DATA_COL = 3;
    public static final int NUMBER_OF_COLUMNS = 4;
    public static final String BLANK_TYPE = "";
    public static final String MAPPER_TYPE = "Mapper";
    public static final String JAVASCRIPT_TYPE = "JavaScript";
    public static final String SMTP_TYPE = "SMTP";
    public static final String JDBC_TYPE = "JDBC";
    public static final String ALERT_TYPE = "Alerts";
    
}
