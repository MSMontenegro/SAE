package Presentation.preferenceSoftwareArchitectureEvaluation;

import java.io.IOException;

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
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import DomainModel.SoftwareArchitectureSpecificationEntity.Architecture;
import Presentation.controllerSoftwareArchitectureEvaluation.SoftwareArchitectureEvaluationPPController;
import Presentation.preferences.DoubleFieldEditor;
import Presentation.preferences.Messages;

/**
 * To specify a software architecture by JUCMNav
 * 
 * @author: María Eva Villarreal Guzmán. E-mail: villarrealguzman@gmail.com
 */
public class SoftwareArchitectureEvaluationPreferencePage extends FieldEditorPreferencePage
		implements IWorkbenchPreferencePage {

	/**
	 * Attributes
	 */
	private Button btnEvaluate;
	private ComboViewer cmbSystem;
	private ComboViewer cmbUnit;
	private SoftwareArchitectureEvaluationPPController viewController;
	private Composite cSystemName;
	private GridData gridData;
	private Table tableSoftArc;
	private TableColumn colPathSoftArc;
	private TableColumn colObjectSoftArc;
	private TableColumn colNameSoftArc;
	private TableViewer tblViewerQualityRequirement;
	private Table table;
	private TableColumn colCheck;
	private TableColumn colObject;
	private TableColumn colQualityAttribute;
	private TableColumn colDescriptionScenario;
	private DoubleFieldEditor simulationTime;

	/**
	 * Contructor
	 */
	public SoftwareArchitectureEvaluationPreferencePage() {
		super(GRID);
		try {
			noDefaultAndApplyButton();
			System.runFinalization();
			Runtime.getRuntime().gc();
			this.setViewController(SoftwareArchitectureEvaluationPPController.getViewController());
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
					viewController.setModel(cmbSystem);
					cmbSystemItemStateChanged();
					prepareView(2);
				}
			});

			gridData = new GridData();
			gridData.horizontalSpan = 4;

			Label labelEmptyOne = new Label(parent, SWT.NULL);
			labelEmptyOne.setLayoutData(gridData);

			gridData = new GridData();
			gridData.horizontalSpan = 4;

			Group gSoftwareArchitecture = new Group(parent, SWT.NONE);
			gSoftwareArchitecture.setLayoutData(gridData);
			gSoftwareArchitecture.setText(Messages.getString("UCM2DEVS_SoftArcSpec_Group"));
			gSoftwareArchitecture.setLayout(layout);

			// Create column names
			String[] columnNames = new String[] { "", Messages.getString("UCM2DEVS_Name_Column"),
					Messages.getString("UCM2DEVS_Path_Column") };

			// Create styles
			int style = SWT.FULL_SELECTION | SWT.BORDER;

			// create table
			tableSoftArc = new Table(gSoftwareArchitecture, style);
			TableLayout tableLayout = new TableLayout();
			tableSoftArc.setLayout(tableLayout);
			gridData = new GridData(GridData.FILL_BOTH);
			tableSoftArc.setLayoutData(gridData);
			tableSoftArc.setLinesVisible(true);
			tableSoftArc.setHeaderVisible(true);

			colObjectSoftArc = new TableColumn(tableSoftArc, SWT.NONE);
			colObjectSoftArc.setWidth(0);
			colObjectSoftArc.setText("");

			colNameSoftArc = new TableColumn(tableSoftArc, SWT.NONE);
			colNameSoftArc.setWidth(100);
			colNameSoftArc.setText(Messages.getString("UCM2DEVS_Name_Column"));

			colPathSoftArc = new TableColumn(tableSoftArc, SWT.NONE);
			colPathSoftArc.setWidth(300);
			colPathSoftArc.setText(Messages.getString("UCM2DEVS_Path_Column"));

			for (int i = 0; i < 4; i++) {
				TableItem item = new TableItem(tableSoftArc, SWT.NONE);
			}

			tableSoftArc.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					tableSoftArc.showSelection();
					if (tableSoftArc.getSelectionIndex() != -1) {
						if (viewController.isNotChecked(table)) {
							viewController.setModelArchitecture(
									(Architecture) tableSoftArc.getItem(tableSoftArc.getSelectionIndex()).getData());
							prepareView(3);
						} else {
							prepareView(4);
						}
					} else {
						prepareView(2);
					}
				}
			});

			gridData = new GridData();
			gridData.horizontalSpan = 4;

			Label labelEmptyTwo = new Label(parent, SWT.NULL);
			labelEmptyTwo.setLayoutData(gridData);

			gridData = new GridData();
			gridData.horizontalSpan = 4;
			gridData.horizontalAlignment = GridData.FILL;

			Group gQualityRequirement = new Group(parent, SWT.NONE);
			gQualityRequirement.setLayoutData(gridData);
			gQualityRequirement.setText(Messages.getString("UCM2DEVS_QualityRequirement_Group"));
			gQualityRequirement.setLayout(new GridLayout(2, false));

			// Create column names
			String[] columnNamesReq = new String[] { "", Messages.getString("UCM2DEVS_DescriptionObject_Column"),
					Messages.getString("UCM2DEVS_QualityAttribute_Column"),
					Messages.getString("UCM2DEVS_DescriptionScenario_Column") };
			// Create styles
			int styleReq = SWT.FULL_SELECTION | SWT.BORDER | SWT.CHECK;
			// create table
			table = new Table(gQualityRequirement, styleReq);
			TableLayout tableLayoutReq = new TableLayout();
			table.setLayout(tableLayoutReq);
			gridData = new GridData(GridData.FILL_BOTH);
			gridData.horizontalSpan = 4;
			table.setLayoutData(gridData);
			table.setLinesVisible(true);
			table.setHeaderVisible(true);
			// Create columns
			colCheck = new TableColumn(table, SWT.NONE);
			colCheck.setWidth(35);

			colObject = new TableColumn(table, SWT.NONE);
			colObject.setWidth(0);
			colObject.setText(Messages.getString("UCM2DEVS_Object_Column"));

			colQualityAttribute = new TableColumn(table, SWT.NONE);
			colQualityAttribute.setWidth(100);
			colQualityAttribute.setText(Messages.getString("UCM2DEVS_QualityAttribute_Column"));

			colDescriptionScenario = new TableColumn(table, SWT.NONE);
			colDescriptionScenario.setWidth(265);
			colDescriptionScenario.setText(Messages.getString("UCM2DEVS_DescriptionScenario_Column"));

			for (int i = 0; i < 4; i++) {
				TableItem item = new TableItem(table, SWT.NONE);
				item.setText("");
			}

			// Create TableViewer
			tblViewerQualityRequirement = new TableViewer(table);
			tblViewerQualityRequirement.setUseHashlookup(true);
			tblViewerQualityRequirement.setColumnProperties(columnNamesReq);

			// Create the cell editors
			CellEditor[] editors = new CellEditor[columnNamesReq.length];
			editors[0] = null;
			editors[1] = null;
			editors[2] = null;
			editors[3] = null;

			// Assign the cell editors to the viewer
			tblViewerQualityRequirement.setCellEditors(editors);
			table.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event event) {
					if (event.detail == SWT.CHECK) {
						if (viewController.isNotChecked(table)) {
							prepareView(3);
						} else {
							prepareView(4);
						}
					}
				}
			});

			gridData = new GridData();
			gridData.horizontalSpan = 4;

			Label labelEmptyThree = new Label(parent, SWT.NULL);
			labelEmptyThree.setLayoutData(gridData);

			Composite cSimulationTime = new Composite(parent, SWT.RIGHT);
			cSimulationTime.setLayout(layout);
			gridData = new GridData();
			gridData.horizontalAlignment = GridData.END;
			cSimulationTime.setLayoutData(gridData);

			gridData = new GridData();
			gridData.horizontalAlignment = GridData.END;
			gridData.grabExcessHorizontalSpace = true;
			gridData.widthHint = 100;

			simulationTime = new DoubleFieldEditor(Messages.getString("UCM2DEVS_SimulationTime_Label"),
					Messages.getString("UCM2DEVS_SimulationTime_Label") + ":", cSimulationTime);
			simulationTime.getTextControl(cSimulationTime).setLayoutData(gridData);
			simulationTime.setMinRange(0.0);
			simulationTime.setPage(this);
			addField(simulationTime);

			gridData = new GridData();
			gridData.horizontalAlignment = GridData.END;
			gridData.grabExcessHorizontalSpace = true;
			gridData.widthHint = 75;

			cmbUnit = new ComboViewer(parent, SWT.READ_ONLY);
			cmbUnit.setContentProvider(ArrayContentProvider.getInstance());
			cmbUnit.getCombo().setLayoutData(gridData);
			loadComboUnit();
			cmbUnit.getCombo().addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					prepareView(5);
				}
			});

			gridData = new GridData();
			gridData.horizontalSpan = 4;

			Label labelEmptyFour = new Label(parent, SWT.NULL);
			labelEmptyFour.setLayoutData(gridData);

			gridData = new GridData();
			gridData.horizontalSpan = 4;
			gridData.widthHint = 100;
			gridData.horizontalAlignment = GridData.END;
			gridData.verticalAlignment = SWT.BOTTOM;
			gridData.grabExcessHorizontalSpace = true;

			btnEvaluate = new Button(parent, SWT.PUSH);
			btnEvaluate.setText(Messages.getString("UCM2DEVS_Evaluate_Buttom"));
			btnEvaluate.setLayoutData(gridData);
			btnEvaluate.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					try {
						parent.setCursor(cursorWait);
						viewController.evaluate();
						clearView();
						parent.setCursor(cursorNotWait);
						prepareView(1);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			});

			this.prepareView(1);
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

	public ComboViewer getCmbSystem() {
		return cmbSystem;
	}

	public void setCmbSystem(ComboViewer cboSystem) {
		this.cmbSystem = cboSystem;
	}

	public DoubleFieldEditor getSimulationTime() {
		return simulationTime;
	}

	public void setSimulationTime(DoubleFieldEditor simulationTime) {
		this.simulationTime = simulationTime;
	}

	public ComboViewer getCmbUnit() {
		return cmbUnit;
	}

	public void setCmbUnit(ComboViewer cmbUnit) {
		this.cmbUnit = cmbUnit;
	}

	public SoftwareArchitectureEvaluationPPController getViewController() {
		return viewController;
	}

	public void setViewController(SoftwareArchitectureEvaluationPPController viewController) {
		this.viewController = viewController;
	}

	public Button getBtnEvaluate() {
		return btnEvaluate;
	}

	public void setBtnEvaluate(Button btnEvaluate) {
		this.btnEvaluate = btnEvaluate;
	}

	public Table getTableSoftArc() {
		return tableSoftArc;
	}

	public void setTableSoftArc(Table table) {
		this.tableSoftArc = table;
	}

	public TableViewer getTblViewerQualityRequirement() {
		return tblViewerQualityRequirement;
	}

	public void setTblViewerQualityRequirement(TableViewer tblViewerQualityRequirement) {
		this.tblViewerQualityRequirement = tblViewerQualityRequirement;
	}

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
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

	public void clearView() {
		cmbSystem.setSelection(StructuredSelection.EMPTY);
		this.getTableSoftArc().clearAll();
		this.getTblViewerQualityRequirement().getTable().clearAll();
		cmbUnit.setSelection(StructuredSelection.EMPTY);
		simulationTime.setStringValue("0.1");
	}

	/**
	 * prepare the view for the different actions that are possible
	 * 
	 * @param pabm
	 */
	public void prepareView(int pabm) {
		this.getCmbSystem().getCombo().setFocus();
		if (!getViewController().existSystemTrueWithArchitecture()) {
			this.getViewController().createErrorDialog(Messages.getString("UCM2DEVS_NoSavedSystemArch_ErrorDialog"));
			pabm = 0;
		}
		if (!getViewController().existSystemTrueWithQualityRequirementTrue()) {
			this.getViewController().createErrorDialog(Messages.getString("UCM2DEVS_NoSavedSystemQR_ErrorDialog"));
			pabm = 0;
		}
		Object valueCmbUnit = ((IStructuredSelection) this.getCmbUnit().getSelection()).getFirstElement();
		switch (pabm) {
		case 0:// No system with architecture or quality requirement
			this.getCmbSystem().getCombo().setEnabled(false);
			this.getTableSoftArc().setEnabled(false);
			this.getTblViewerQualityRequirement().getTable().setEnabled(false);
			this.getSimulationTime().setStringValue("0.1");
			this.getSimulationTime().setEnabled(false);
			this.getCmbUnit().getCombo().setEnabled(false);
			if (!(valueCmbUnit == null) && this.getCmbUnit().getCombo().isEnabled()) {
				this.getBtnEvaluate().setEnabled(true);
			} else {
				this.getBtnEvaluate().setEnabled(false);
			}
			break;
		case 1: // There are systems with architecture or quality requirement
			this.getCmbSystem().getCombo().setEnabled(true);
			this.getTableSoftArc().setEnabled(false);
			this.getTblViewerQualityRequirement().getTable().setEnabled(false);
			this.getSimulationTime().setStringValue("0.1");
			this.getSimulationTime().setEnabled(false);
			this.getCmbUnit().getCombo().setEnabled(false);
			if (!(valueCmbUnit == null) && this.getCmbUnit().getCombo().isEnabled()) {
				this.getBtnEvaluate().setEnabled(true);
			} else {
				this.getBtnEvaluate().setEnabled(false);
			}
			break;
		case 2: // With system selected
			this.getTableSoftArc().setEnabled(true);
			this.getTblViewerQualityRequirement().getTable().setEnabled(false);
			this.getSimulationTime().setEnabled(false);
			this.getCmbUnit().getCombo().setEnabled(false);
			if (!(valueCmbUnit == null) && this.getCmbUnit().getCombo().isEnabled()) {
				this.getBtnEvaluate().setEnabled(true);
			} else {
				this.getBtnEvaluate().setEnabled(false);
			}

			break;
		case 3:// With architecture selected
			this.getTblViewerQualityRequirement().getTable().setEnabled(true);
			this.getSimulationTime().setEnabled(false);
			this.getCmbUnit().getCombo().setEnabled(false);
			if (!(valueCmbUnit == null) && this.getCmbUnit().getCombo().isEnabled()) {
				this.getBtnEvaluate().setEnabled(true);
			} else {
				this.getBtnEvaluate().setEnabled(false);
			}
			break;
		case 4:// With requirement selected
			this.getSimulationTime().setEnabled(true);
			this.getCmbUnit().getCombo().setEnabled(true);
			if (!(valueCmbUnit == null) && this.getCmbUnit().getCombo().isEnabled()) {
				this.getBtnEvaluate().setEnabled(true);
			} else {
				this.getBtnEvaluate().setEnabled(false);
			}
			break;
		case 5:// With unit selected
			this.getSimulationTime().setEnabled(true);
			this.getCmbUnit().getCombo().setEnabled(true);
			if (!(valueCmbUnit == null) && this.getCmbUnit().getCombo().isEnabled()) {
				this.getBtnEvaluate().setEnabled(true);
			} else {
				this.getBtnEvaluate().setEnabled(false);
			}
			break;

		}
	}

	/**
	 * When a system is selected, fill table with its quality requirements and
	 * prepare the view
	 */
	private void cmbSystemItemStateChanged() {// GEN-FIRST:event_cmbNombreItemStateChanged
		this.fillTableSoftArc();
		this.fillTableQR();
	}

	/**
	 * Fill table with system's quality requirements (quality attribute,
	 * description and condition)
	 */
	public void fillTableSoftArc() {
		this.getViewController().setModelPaths(
				(DomainModel.AnalysisEntity.System) ((IStructuredSelection) this.getCmbSystem().getSelection())
						.getFirstElement());
	}

	/**
	 * // * Fill table with system's quality requirements (quality attribute, //
	 * * description and condition) //
	 */
	public void fillTableQR() {
		this.getViewController().setModelQualityRequirement(
				(DomainModel.AnalysisEntity.System) ((IStructuredSelection) this.getCmbSystem().getSelection())
						.getFirstElement());
	}

}