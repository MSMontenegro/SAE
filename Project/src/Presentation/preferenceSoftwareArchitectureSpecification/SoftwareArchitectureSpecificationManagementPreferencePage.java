package Presentation.preferenceSoftwareArchitectureSpecification;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
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

import Presentation.controllerSoftwareArchitectureSpecification.SoftwareArchitectureSpecificationPPController;
import Presentation.preferences.Messages;

/**
 * To specify a software architecture by JUCMNav
 * 
 * @author: Micaela Montenegro. E-mail: mica.montenegro.sistemas@gmail.com
 */
public class SoftwareArchitectureSpecificationManagementPreferencePage extends FieldEditorPreferencePage
		implements IWorkbenchPreferencePage {

	/**
	 * Attributes
	 */
	private Button btnBrowseUCM;
	private Button btnSave;
	private Button btnAdd;
	private Button btnConsult;
	private Button btnDelete;
	private ComboViewer cmbUnit;
	private ComboViewer cmbSystem;
	private SoftwareArchitectureSpecificationPPController viewController;
	private FileDialog chooseFile;
	private Composite cSystemName;
	private Composite cUnit;
	private GridData gridData;
	private TableViewer tblViewerQualityRequirement;
	private Table table;
	private TableColumn colObject;
	private TableColumn colPath;
	private TableColumn colName;

	/**
	 * Contructor
	 */
	public SoftwareArchitectureSpecificationManagementPreferencePage() {
		super(GRID);
		try {
			noDefaultAndApplyButton();
			System.runFinalization();
			Runtime.getRuntime().gc();
			this.setViewController(SoftwareArchitectureSpecificationPPController.getViewController());
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

			cSystemName = new Composite(parent, SWT.NULL);
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
			cmbSystem.getCombo().setLayoutData(gridData);
			loadCombo();
			cmbSystem.getCombo().addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					if (((IStructuredSelection) cmbSystem.getSelection()).getFirstElement() != null) {
						getCmbUnit().setSelection(StructuredSelection.EMPTY);
						getTable().clearAll();
						viewController.setModel(cmbSystem);
						cmbSystemItemStateChanged();
						prepareView(1);
					}
				}
			});

			gridData = new GridData();
			gridData.horizontalSpan = 4;

			Label labelEmptyOne = new Label(parent, SWT.NULL);
			labelEmptyOne.setLayoutData(gridData);

			gridData = new GridData();
			gridData.horizontalSpan = 4;

			Group gQualityRequirement = new Group(parent, SWT.NONE);
			gQualityRequirement.setLayoutData(gridData);
			gQualityRequirement.setText(Messages.getString("UCM2DEVS_SoftArcSpec_Group"));
			gQualityRequirement.setLayout(layout);

			// Create column names
			String[] columnNames = new String[] { Messages.getString("UCM2DEVS_Object_Column"),
					Messages.getString("UCM2DEVS_Name_Column"), Messages.getString("UCM2DEVS_Path_Column") };
			// Create styles
			int style = SWT.FULL_SELECTION | SWT.BORDER;
			// create table
			table = new Table(gQualityRequirement, style);
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
			colPath.setWidth(300);
			colPath.setText(Messages.getString("UCM2DEVS_Path_Column"));

			for (int i = 0; i < 8; i++) {
				TableItem item = new TableItem(table, SWT.NONE);
				item.setText("Item " + i);
			}

			// Create TableViewer
			tblViewerQualityRequirement = new TableViewer(table);
			tblViewerQualityRequirement.setUseHashlookup(true);
			tblViewerQualityRequirement.setColumnProperties(columnNames);

			// Create the cell editors
			CellEditor[] editors = new CellEditor[columnNames.length];
			editors[0] = null;
			editors[1] = null;
			editors[2] = null;

			// Assign the cell editors to the viewer
			tblViewerQualityRequirement.setCellEditors(editors);

			Composite cButtoms = new Composite(gQualityRequirement, SWT.RIGHT);
			cButtoms.setLayout(layout);
			gridData = new GridData();
			gridData.horizontalSpan = 4;
			gridData.horizontalAlignment = GridData.END;
			cButtoms.setLayoutData(gridData);

			gridData = new GridData();
			gridData.horizontalAlignment = GridData.END;
			gridData.grabExcessHorizontalSpace = true;
			gridData.widthHint = 75;

			btnAdd = new Button(cButtoms, SWT.PUSH);
			btnAdd.setText(Messages.getString("UCM2DEVS_Add_Buttom"));
			btnAdd.setToolTipText(Messages.getString("UCM2DEVS_Add_ToolTip"));
			btnAdd.setLayoutData(gridData);
			btnAdd.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					// Open a FileDialog that show only jucm file
					chooseFile = new FileDialog(parent.getShell(), SWT.OPEN);
					chooseFile.setFilterNames(new String[] { Messages.getString("UCM2DEVS_JucmFiles_Label") });
					chooseFile.setFilterExtensions(new String[] { "*.jucm" });
					String filePath = chooseFile.open();
					if (filePath != null) {
						if (!viewController.isUCMDuplicate(filePath)) {
							String result = viewController.addToTable(filePath);
							if (!result.equals("")){
								viewController.createErrorDialog(result);
							}
							prepareView(2);
						} else {
							viewController.createErrorDialog(Messages.getString("UCM2DEVS_UCMExists_ErrorDialog"));
						}
					}
					
				}
			});

			btnConsult = new Button(cButtoms, SWT.PUSH);
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
					prepareView(2);
				}
			});

			btnDelete = new Button(cButtoms, SWT.PUSH);
			btnDelete.setText(Messages.getString("UCM2DEVS_Delete_Buttom"));
			btnDelete.setToolTipText(Messages.getString("UCM2DEVS_Delete_ToolTip"));
			btnDelete.setLayoutData(gridData);
			btnDelete.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					parent.setCursor(cursorWait);
					viewController.deleteToTable();
					parent.setCursor(cursorNotWait);
				}
			});

			layout = new GridLayout();
			layout.numColumns = 4;
			parent.setLayout(layout);

			cUnit = new Composite(parent, SWT.NULL);
			cUnit.setLayout(layout);
			gridData = new GridData();
			gridData.horizontalSpan = 4;
			gridData.horizontalAlignment = GridData.FILL;
			cUnit.setLayoutData(gridData);

			Label labelU = new Label(cUnit, SWT.NONE);
			labelU.setText(Messages.getString("UCM2DEVS_UnitParameter_Label") + ":");

			gridData = new GridData();
			gridData.widthHint = 200;
			gridData.grabExcessHorizontalSpace = true;

			cmbUnit = new ComboViewer(cUnit, SWT.READ_ONLY);
			cmbUnit.setContentProvider(ArrayContentProvider.getInstance());
			cmbUnit.getCombo().setLayoutData(gridData);
			loadComboUnit();
			cmbUnit.getCombo().addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					if (((IStructuredSelection) cmbUnit.getSelection()).getFirstElement() != null) {
						viewController.setModelUnit(cmbUnit);
						prepareView(2);
					}
				}
			});

			gridData = new GridData();
			gridData.horizontalSpan = 2;

			Label labelEmptyTree = new Label(parent, SWT.NULL);
			labelEmptyTree.setLayoutData(gridData);

			gridData = new GridData();
			gridData.horizontalSpan = 4;
			gridData.widthHint = 100;
			gridData.horizontalAlignment = GridData.END;
			gridData.verticalAlignment = SWT.BOTTOM;
			gridData.grabExcessHorizontalSpace = true;

			btnSave = new Button(parent, SWT.PUSH);
			btnSave.setText(Messages.getString("UCM2DEVS_Save_Buttom"));
			btnSave.setLayoutData(gridData);
			btnSave.addSelectionListener(new SelectionAdapter() {
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

	public Button getBtnFileUCM() {
		return btnBrowseUCM;
	}

	public Button getBtnBrowseUCM() {
		return btnBrowseUCM;
	}

	public void setBtnBrowseUCM(Button btnBrowseUCM) {
		this.btnBrowseUCM = btnBrowseUCM;
	}

	public Button getBtnConsult() {
		return btnConsult;
	}

	public void setBtnConsult(Button btnConsult) {
		this.btnConsult = btnConsult;
	}

	public void setBtnFileUCM(Button btnFileUCM) {
		this.btnBrowseUCM = btnFileUCM;
	}

	public ComboViewer getCmbSystem() {
		return cmbSystem;
	}

	public void setCmbSystem(ComboViewer cboSystem) {
		this.cmbSystem = cboSystem;
	}

	public SoftwareArchitectureSpecificationPPController getViewController() {
		return viewController;
	}

	public void setViewController(SoftwareArchitectureSpecificationPPController viewController) {
		this.viewController = viewController;
	}

	public FileDialog getChooseFile() {
		return chooseFile;
	}

	public void setChooseFile(FileDialog chooseFile) {
		this.chooseFile = chooseFile;
	}

	public Button getBtnSave() {
		return btnSave;
	}

	public void setBtnSave(Button btnSave) {
		this.btnSave = btnSave;
	}

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	public Button getBtnAdd() {
		return btnAdd;
	}

	public void setBtnAdd(Button btnAdd) {
		this.btnAdd = btnAdd;
	}

	public Button getBtnDelete() {
		return btnDelete;
	}

	public void setBtnDelete(Button btnDelete) {
		this.btnDelete = btnDelete;
	}

	public ComboViewer getCmbUnit() {
		return cmbUnit;
	}

	public void setCmbUnit(ComboViewer cmbUnit) {
		this.cmbUnit = cmbUnit;
	}

	/**
	 * load combo with system with state=true
	 */
	public void loadCombo() {
		this.getViewController().setModel();
	}

	/**
	 * load combo with units
	 */
	public void loadComboUnit() {
		this.getViewController().setModelUnit();
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
		Object valueCmbUnit = ((IStructuredSelection) cmbUnit.getSelection()).getFirstElement();
		switch (pabm) {
		case 0: // with open the form
			this.getCmbSystem().getCombo().setEnabled(true);
			this.getTable().setEnabled(false);
			this.getBtnAdd().setEnabled(false);
			this.getBtnDelete().setEnabled(false);
			this.getBtnConsult().setEnabled(false);
			if (!(valueCmbUnit == null)) {
				this.getBtnSave().setEnabled(true);
			} else {
				this.getBtnSave().setEnabled(false);
			}
			this.getCmbUnit().getCombo().setEnabled(false);
			clearView();
			break;
		case 1:// with system selected
			this.getCmbSystem().getCombo().setEnabled(true);
			this.getTable().setEnabled(true);
			this.getBtnAdd().setEnabled(true);
			this.getBtnDelete().setEnabled(true);
			this.getBtnConsult().setEnabled(true);
			this.getCmbUnit().getCombo().setEnabled(true);
			// this.getCmbUnit().setSelection(StructuredSelection.EMPTY);
			if (!(valueCmbUnit == null) && this.getTable().getItemCount() > 0) {
				this.getBtnSave().setEnabled(true);
			} else {
				this.getBtnSave().setEnabled(false);
			}
			break;
		case 2: // with architecture selected
			this.getCmbSystem().getCombo().setEnabled(true);
			this.getTable().setEnabled(true);
			this.getBtnAdd().setEnabled(true);
			this.getBtnDelete().setEnabled(true);
			this.getBtnConsult().setEnabled(true);
			this.getCmbUnit().getCombo().setEnabled(true);
			if (!(valueCmbUnit == null) && this.getTable().getItemCount() > 0) {
				this.getBtnSave().setEnabled(true);
			} else {
				this.getBtnSave().setEnabled(false);
			}
			break;
		case 3:
			this.getCmbSystem().getCombo().setEnabled(false);
			this.getTable().setEnabled(false);
			this.getBtnAdd().setEnabled(false);
			this.getBtnDelete().setEnabled(false);
			this.getBtnConsult().setEnabled(false);
			if (!(valueCmbUnit == null) && this.getTable().getItemCount() > 0) {
				this.getBtnSave().setEnabled(true);
			} else {
				this.getBtnSave().setEnabled(false);
			}
			this.getCmbUnit().getCombo().setEnabled(false);
			break;
		case 4: // with delete all arquitectures
			this.getCmbSystem().getCombo().setEnabled(true);
			this.getTable().setEnabled(true);
			this.getBtnAdd().setEnabled(true);
			this.getBtnDelete().setEnabled(true);
			this.getBtnConsult().setEnabled(true);
			this.getCmbUnit().getCombo().setEnabled(false);
			this.getBtnSave().setEnabled(true);
			break;
		}

	}

	/**
	 * When a system is selected, fill table with its quality requirements and
	 * prepare the view
	 */
	private void cmbSystemItemStateChanged() {// GEN-FIRST:event_cmbNombreItemStateChanged
		this.fillTable();
	}

	/**
	 * Fill table with system's quality requirements (quality attribute,
	 * description and condition)
	 */
	public void fillTable() {
		this.getViewController().setModelPaths(
				(DomainModel.AnalysisEntity.System) ((IStructuredSelection) this.getCmbSystem().getSelection())
						.getFirstElement());
	}

	public void clearView() {
		this.getCmbSystem().setSelection(StructuredSelection.EMPTY);
		this.getCmbUnit().setSelection(StructuredSelection.EMPTY);
		this.getTable().clearAll();
	}

}