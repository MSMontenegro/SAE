package Presentation.preferenceSoftwareArchitectureSpecification;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import Presentation.controllerSoftwareArchitectureSpecification.ManagementSoftwareArchitectureSpecificationPPController;
import Presentation.preferences.Messages;

/**
 * To search, consult, edit or remove a quality requirement
 * 
 * @author: Micaela Montenegro. E-mail: mica.montenegro.sistemas@gmail.com
 */
public class ManagementSoftwareArchitectureSpecificationPreferencePage extends FieldEditorPreferencePage
		implements IWorkbenchPreferencePage {
	/**
	 * Attributes
	 */
	// private Group groupQualityRequirement;
	private TableViewer tblViewerSpecificationArchitecture;
	private Table table;
	private GridData gridData;
	private StringFieldEditor txtName;
	private StringFieldEditor txtPath;
	private ComboViewer cmbUnit;
	private ComboViewer cmbSystem;
	private Button btnBrowse;
	private Button btnAdd;
	private Button btnConsult;
	private TableColumn colObject;
	private TableColumn colName;
	private TableColumn colPath;
	private TableColumn colUnit;
	private ManagementSoftwareArchitectureSpecificationPPController viewController;
	private Composite cSpecification;
	private FileDialog chooseFile;

	/**
	 * Constructor
	 */
	public ManagementSoftwareArchitectureSpecificationPreferencePage() {
		super(GRID);
		try {
			noDefaultAndApplyButton();
			System.runFinalization();
			Runtime.getRuntime().gc();
			this.setViewController(ManagementSoftwareArchitectureSpecificationPPController.getViewController());
			this.getViewController().setForm(this);
		} catch (Exception e) {
			System.err.print(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.preference.FieldEditorPreferencePage#createContents(org
	 * .eclipse.swt.widgets.Composite)
	 */
	protected Control createContents(final Composite parent) {

		if (viewController.isConnection()) {
			final Cursor cursorWait = parent.getDisplay().getSystemCursor(SWT.CURSOR_WAIT);
			final Cursor cursorNotWait = parent.getDisplay().getSystemCursor(SWT.CURSOR_ARROW);

			GridLayout layout = new GridLayout();
			layout.numColumns = 4;
			parent.setLayout(layout);

			Composite cSystemName = new Composite(parent, SWT.NULL);
			cSystemName.setLayout(layout);
			gridData = new GridData();
			gridData.horizontalSpan = 4;
			gridData.horizontalAlignment = GridData.FILL;
			cSystemName.setLayoutData(gridData);

			Label labelSn = new Label(cSystemName, SWT.NONE);
			labelSn.setText(Messages.getString("UCM2DEVS_SystemName_Label") + ":");

			gridData = new GridData();
			gridData.widthHint = 200;
			gridData.grabExcessHorizontalSpace = true;

			cmbSystem = new ComboViewer(cSystemName, SWT.READ_ONLY);
			cmbSystem.setContentProvider(ArrayContentProvider.getInstance());
			cmbSystem.getCombo().addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					if (((IStructuredSelection) cmbSystem.getSelection()).getFirstElement() != "") {
						getCmbUnit().setSelection(StructuredSelection.EMPTY);
						getTable().clearAll();
						viewController.setModel(cmbSystem);
						cmbSystemItemStateChanged();
						prepareView(1);
					} else {
						clearSpecification();
						prepareView(0);
					}
				}
			});
			cmbSystem.getCombo().setLayoutData(gridData);

			gridData = new GridData();
			gridData.horizontalSpan = 4;

			Label labelEmptyOne = new Label(cSystemName, SWT.NULL);
			labelEmptyOne.setLayoutData(gridData);

			gridData = new GridData();
			gridData.horizontalSpan = 4;
			gridData.horizontalAlignment = GridData.FILL;

			Group gSpecificationArchitecture = new Group(cSystemName, SWT.NONE);
			gSpecificationArchitecture.setLayoutData(gridData);
			gSpecificationArchitecture.setText(Messages.getString("UCM2DEVS_SoftArcSpec_Group"));
			gSpecificationArchitecture.setLayout(new GridLayout(2, false));

			// Create column names
			String[] columnNames = new String[] { Messages.getString("UCM2DEVS_Object_Column"),
					Messages.getString("UCM2DEVS_Name_Column"), Messages.getString("UCM2DEVS_Path_Column"),
					Messages.getString("UCM2DEVS_Unit_Column") };
			// Create styles
			int style = SWT.FULL_SELECTION | SWT.BORDER;
			// create table
			table = new Table(gSpecificationArchitecture, style);
			TableLayout tableLayout = new TableLayout();
			table.setLayout(tableLayout);
			gridData = new GridData(GridData.FILL_BOTH);
			gridData.horizontalSpan = 4;
			table.setLayoutData(gridData);
			table.setLinesVisible(true);
			table.setHeaderVisible(true);
			// Create columns

			colObject = new TableColumn(table, SWT.NONE);
			colObject.setWidth(0);
			colObject.setText(Messages.getString("UCM2DEVS_Object_Column"));

			colName = new TableColumn(table, SWT.NONE);
			colName.setWidth(100);
			colName.setText(Messages.getString("UCM2DEVS_Name_Column"));

			colPath = new TableColumn(table, SWT.NONE);
			colPath.setWidth(200);
			colPath.setText(Messages.getString("UCM2DEVS_Path_Column"));

			colUnit = new TableColumn(table, SWT.NONE);
			colUnit.setWidth(150);
			colUnit.setText(Messages.getString("UCM2DEVS_Unit_Column"));

			for (int i = 0; i < 4; i++) {
				TableItem item = new TableItem(table, SWT.NONE);
				item.setText("Item " + i);
			}

			// Create TableViewer
			tblViewerSpecificationArchitecture = new TableViewer(table);
			tblViewerSpecificationArchitecture.setUseHashlookup(true);
			tblViewerSpecificationArchitecture.setColumnProperties(columnNames);

			// Create the cell editors
			CellEditor[] editors = new CellEditor[columnNames.length];
			editors[0] = null;
			editors[1] = null;
			editors[2] = null;
			editors[3] = null;

			// Assign the cell editors to the viewer
			tblViewerSpecificationArchitecture.setCellEditors(editors);

			table.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					table.showSelection();
					prepareView(4);
				}
			});

			cSpecification = new Composite(gSpecificationArchitecture, SWT.NONE);
			gridData = new GridData();
			gridData.grabExcessHorizontalSpace = true;
			gridData.horizontalAlignment = GridData.FILL;
			cSpecification.setLayoutData(gridData);

			txtName = new StringFieldEditor(Messages.getString("UCM2DEVS_Name_Label"),
					Messages.getString("UCM2DEVS_Name_Label") + ":", cSpecification);

			txtPath = new StringFieldEditor(Messages.getString("UCM2DEVS_Path_Label"),
					Messages.getString("UCM2DEVS_Path_Label") + ":", cSpecification);

			Label labelUnit = new Label(cSpecification, SWT.NONE);
			labelUnit.setText(Messages.getString("UCM2DEVS_Unit_Label") + ":");

			cmbUnit = new ComboViewer(cSpecification, SWT.READ_ONLY);
			cmbUnit.setContentProvider(ArrayContentProvider.getInstance());

			btnConsult = new Button(cSpecification, SWT.PUSH);
			btnConsult.setText(Messages.getString("UCM2DEVS_Consult_Buttom"));
			btnConsult.setToolTipText(Messages.getString("UCM2DEVS_Consult_ToolTip"));
			btnConsult.setLayoutData(gridData);
			btnConsult.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					parent.setCursor(cursorWait);
					TableItem item = table.getItem(table.getSelectionIndex());
					viewController.openJUCMNavEditor(parent, item.getText(2) + "\\" + item.getText(1));
					parent.setCursor(cursorNotWait);
				}
			});

			btnBrowse = new Button(cSpecification, SWT.PUSH);
			btnBrowse.setText(Messages.getString("UCM2DEVS_Browse_Buttom"));
			btnBrowse.setLayoutData(gridData);
			btnBrowse.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					// Open a FileDialog that show only jucm file
					chooseFile = new FileDialog(parent.getShell(), SWT.OPEN);
					chooseFile.setFilterNames(new String[] { Messages.getString("UCM2DEVS_JucmFiles_Label") });
					chooseFile.setFilterExtensions(new String[] { "*.jucm" });
					String filePath = chooseFile.open();
					if (filePath != null) {
						if (!viewController.isUCMDuplicate(filePath)) {
							String result = viewController.addToCmb(filePath);
							if (!result.equals("")) {
								viewController.createErrorDialog(result);
							} else {
								prepareView(2);
							}
						} else {
							viewController.createErrorDialog(Messages.getString("UCM2DEVS_UCMExists_ErrorDialog"));
						}
					}

					/**
					 * parent.setCursor(cursorWait); int var =
					 * viewController.save(); parent.setCursor(cursorNotWait);
					 * if (var == 0) {
					 * viewController.createObjectSuccessDialog(); } else if
					 * (var == 1) {
					 * viewController.createObjectDontUpdateErrorDialog(); }
					 **/
				}
			});

			btnAdd = new Button(cSpecification, SWT.PUSH);
			btnAdd.setText(Messages.getString("UCM2DEVS_Add_Buttom"));
			btnAdd.setLayoutData(gridData);
			btnAdd.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					parent.setCursor(cursorWait);
					if (viewController.save()) {
						parent.setCursor(cursorNotWait);
						viewController.createObjectSuccessDialog();
					} else {
						parent.setCursor(cursorNotWait);
						viewController.createObjectDontUpdateErrorDialog();
					}
					/**
					 * if (viewController.createDeleteRequirementDialog() ==
					 * true) { parent.setCursor(cursorWait);
					 * viewController.remove(); parent.setCursor(cursorNotWait);
					 * }
					 **/
				}
			});

			this.prepareView(0);
			return new Composite(parent, SWT.NULL);
		} else {
			viewController.createErrorDialog(Messages.getString("UCM2DEVS_ConnectionDatabase_ErrorDialog"));

			GridLayout layout = new GridLayout();
			layout.numColumns = 4;
			parent.setLayout(layout);

			Composite cErrorMessage = new Composite(parent, SWT.NULL);
			cErrorMessage.setLayout(layout);
			gridData = new GridData();
			gridData.horizontalSpan = 4;
			gridData.horizontalAlignment = GridData.FILL;
			cErrorMessage.setLayoutData(gridData);

			Label labelSn = new Label(cErrorMessage, SWT.NONE);
			labelSn.setText(Messages.getString("UCM2DEVS_ConnectionDatabase_ErrorDialog"));
		}

		return null;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors
	 * ()
	 */
	@Override
	protected void createFieldEditors() {
	}

	/**
	 * Getters and Setters
	 */

	public ComboViewer getCmbUnit() {
		return cmbUnit;
	}

	public Composite getcSpecification() {
		return cSpecification;
	}

	public void setcSpecification(Composite cSpecification) {
		this.cSpecification = cSpecification;
	}

	public TableViewer getTblViewerSpecificationArchitecture() {
		return tblViewerSpecificationArchitecture;
	}

	public void setTblViewerSpecificationArchitecture(TableViewer tblViewerSpecificationArchitecture) {
		this.tblViewerSpecificationArchitecture = tblViewerSpecificationArchitecture;
	}

	public GridData getGridData() {
		return gridData;
	}

	public void setGridData(GridData gridData) {
		this.gridData = gridData;
	}

	public StringFieldEditor getTxtName() {
		return txtName;
	}

	public void setTxtName(StringFieldEditor txtName) {
		this.txtName = txtName;
	}

	public StringFieldEditor getTxtPath() {
		return txtPath;
	}

	public void setTxtPath(StringFieldEditor txtPath) {
		this.txtPath = txtPath;
	}

	public TableColumn getColName() {
		return colName;
	}

	public void setColName(TableColumn colName) {
		this.colName = colName;
	}

	public TableColumn getColPath() {
		return colPath;
	}

	public void setColPath(TableColumn colPath) {
		this.colPath = colPath;
	}

	public TableColumn getColUnit() {
		return colUnit;
	}

	public void setColUnit(TableColumn colUnit) {
		this.colUnit = colUnit;
	}

	public void setCmbUnit(ComboViewer cmbUnit) {
		this.cmbUnit = cmbUnit;
	}

	public Button getBtnBrowse() {
		return btnBrowse;
	}

	public void setBtnBrowse(Button btnBrowse) {
		this.btnBrowse = btnBrowse;
	}

	public Button getBtnAdd() {
		return btnAdd;
	}

	public void setBtnAdd(Button btnAdd) {
		this.btnAdd = btnAdd;
	}

	public Button getBtnConsult() {
		return btnConsult;
	}

	public void setBtnConsult(Button btnConsult) {
		this.btnConsult = btnConsult;
	}

	public ManagementSoftwareArchitectureSpecificationPPController getViewController() {
		return viewController;
	}

	public void setViewController(ManagementSoftwareArchitectureSpecificationPPController viewController) {
		this.viewController = viewController;
	}

	public ComboViewer getCmbSystem() {
		return cmbSystem;
	}

	public void setCmbSystem(ComboViewer cmbSystem) {
		this.cmbSystem = cmbSystem;
	}

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	public TableColumn getColObject() {
		return colObject;
	}

	public void setColObject(TableColumn colObject) {
		this.colObject = colObject;
	}

	/**
	 * Load systems with state=true and requirements with state=true in the
	 * combo
	 */
	public void loadCmbSystem() {
		this.getViewController().setModel();
	}

	/**
	 * When a system is selected, fill table with its quality requirements and
	 * prepare the view
	 */
	private void cmbSystemItemStateChanged() {// GEN-FIRST:event_cmbNombreItemStateChanged
		this.fillTable();
	}

	/**
	 * Fill table with system's software architecture specifications (name, path
	 * and unit)
	 */
	public void fillTable() {
		this.getViewController().setModelPaths(
				(DomainModel.AnalysisEntity.System) ((IStructuredSelection) this.getCmbSystem().getSelection())
						.getFirstElement());
	}

	/**
	 * load combo with units
	 */
	public void loadCmbUnit() {
		this.getViewController().setModelUnit();
	}

	/**
	 * Clean the quality scenario (description, quality attribute, condition and
	 * parts)
	 */
	public void clearSpecification() {
		txtName.getTextControl(this.getcSpecification()).setText("");
		txtPath.getTextControl(this.getcSpecification()).setText("");
		cmbUnit.setSelection(StructuredSelection.EMPTY);
	}

	/**
	 * Clean the system selected and the quality scenario
	 */
	public void clearView() {
		cmbSystem.getCombo().clearSelection();
		this.clearSpecification();
	}

	/**
	 * prepare the view for the different actions that are possible
	 * 
	 * @param pabm
	 */
	public void prepareView(int pabm) {
		if (!getViewController().existSystemTrue()) {
			this.getViewController().createErrorDialog(Messages.getString("UCM2DEVS_NoSavedSystems_ErrorDialog"));
			pabm = 3;
		}
		switch (pabm) {
		case 0:// Search specification
			this.clearSpecification();
			this.getCmbSystem().getCombo().setEnabled(true);
			loadCmbSystem();

			this.getTblViewerSpecificationArchitecture().getTable().setEnabled(false);
			this.getTxtName().getTextControl(this.getcSpecification()).setEnabled(false);
			this.getTxtPath().getTextControl(this.getcSpecification()).setEnabled(false);
			this.getCmbUnit().getCombo().setEnabled(false);

			this.getBtnBrowse().setEnabled(false);
			this.getBtnAdd().setEnabled(false);
			this.getBtnConsult().setEnabled(false);

			break;
		case 1:// With system selected
			this.getTblViewerSpecificationArchitecture().getTable().setEnabled(true);
			this.getTxtName().getTextControl(this.getcSpecification()).setEnabled(false);
			this.getTxtPath().getTextControl(this.getcSpecification()).setEnabled(false);
			this.getCmbUnit().getCombo().setEnabled(false);

			this.getBtnBrowse().setEnabled(true);
			this.getBtnAdd().setEnabled(true);
			this.getBtnConsult().setEnabled(false);

			break;
		case 2:// With browse specification
			this.getCmbUnit().getCombo().setEnabled(true);

			break;
		case 3:// No saved system
			this.clearSpecification();
			this.getCmbSystem().getCombo().setEnabled(false);

			this.getTblViewerSpecificationArchitecture().getTable().setEnabled(false);
			this.getTxtName().getTextControl(this.getcSpecification()).setEnabled(false);
			this.getTxtPath().getTextControl(this.getcSpecification()).setEnabled(false);
			this.getCmbUnit().getCombo().setEnabled(false);
			this.getBtnBrowse().setEnabled(false);
			this.getBtnAdd().setEnabled(false);
			this.getBtnConsult().setEnabled(false);

			break;
		case 4:// with specification selected
			this.clearSpecification();
			this.getCmbSystem().getCombo().setEnabled(true);
			this.getTblViewerSpecificationArchitecture().getTable().setEnabled(true);
			this.getTxtName().getTextControl(this.getcSpecification()).setEnabled(false);
			this.getTxtPath().getTextControl(this.getcSpecification()).setEnabled(false);
			this.getCmbUnit().getCombo().setEnabled(false);
			this.getBtnBrowse().setEnabled(true);
			this.getBtnAdd().setEnabled(true);
			this.getBtnConsult().setEnabled(true);

			break;
		}
	}

	/**
	 * Sets the view whit quality scenario of the quality requirement selected
	 */
	public void setView() {
		this.getViewController().getView();
	}

}
