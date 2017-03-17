package Presentation.preferenceReports;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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

import DomainModel.AnalysisEntity.QualityRequirement;
import DomainModel.SoftwareArchitectureSpecificationEntity.Architecture;
import Presentation.controllerReports.ReportsPPController;
import Presentation.preferences.Messages;

/**
 * To view a report for a specific architecture and a specific quality
 * requirement
 * 
 * @author: Florencia Rossini. E-mail: flori.rossini@gmail.com
 */
public class ReportsPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	/**
	 * Attributes
	 */
	private Button btnSystemViewReport;
	private Button btnResponsabilityViewReport;
	private ComboViewer cmbSystem;
	private ReportsPPController viewController;
	private Composite cSystemName;
	private GridData gridData;
	private Table tableSimulation;
	private TableColumn colPathSoftArc;
	private TableColumn colObjectSimulation;
	private TableColumn colNameSoftArc;
	private TableViewer tblViewerQualityRequirement;
	private Table tableQualityRequirement;
	private TableColumn colObject;
	private TableColumn colQRQualityAttribute;
	private TableColumn colDescriptionScenario;
	private TableViewer tblViewerReport;
	private Table tableReport;
	private TableColumn colIndicatorType;
	private TableColumn colReportQualityAttribute;
	private TableColumn colViewReportButtom;

	/**
	 * Contructor
	 */
	public ReportsPreferencePage() {
		super(GRID);
		noDefaultAndApplyButton();
		this.setViewController(ReportsPPController.getViewController());
		this.getViewController().setForm(this);
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
	protected Control createContents(Composite parent) {
		if (viewController.isConnection()) {
			if (!viewController.isConnection()) {
				viewController.createErrorDialog(Messages.getString("UCM2DEVS_ConnectionDatabase_ErrorDialog"));
			}

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
			gridData.horizontalAlignment = GridData.FILL;
			gridData.grabExcessHorizontalSpace = true;

			cmbSystem = new ComboViewer(cSystemName, SWT.READ_ONLY);
			cmbSystem.setContentProvider(ArrayContentProvider.getInstance());
			cmbSystem.getCombo().setLayoutData(gridData);
			loadCombo();
			cmbSystem.getCombo().addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					if (((IStructuredSelection) cmbSystem.getSelection()).getFirstElement() != "") {
						tableQualityRequirement.clearAll();
						viewController.setModel(cmbSystem);
						cmbSystemItemStateChanged();
						viewController.setModel(
								(Architecture) tableSimulation.getItem(0).getData());
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

			Group gSimulation = new Group(parent, SWT.NONE);
			gSimulation.setLayoutData(gridData);
			gSimulation.setText(Messages.getString("UCM2DEVS_SoftArcSpec_Group"));
			gSimulation.setLayout(new GridLayout(2, false));

			// Create column names
			String[] columnNames = new String[] { "", Messages.getString("UCM2DEVS_Name_Column"),
					Messages.getString("UCM2DEVS_Path_Column") };

			// Create styles
			int style = SWT.FULL_SELECTION | SWT.BORDER;

			// create table

			// Table: Simulation
			tableSimulation = new Table(gSimulation, style);
			TableLayout tableLayout = new TableLayout();
			tableSimulation.setLayout(tableLayout);
			gridData = new GridData(GridData.FILL_BOTH);
			tableSimulation.setLayoutData(gridData);
			tableSimulation.setLinesVisible(true);
			tableSimulation.setHeaderVisible(true);

			colObjectSimulation = new TableColumn(tableSimulation, SWT.NONE);
			colObjectSimulation.setWidth(0);
			colObjectSimulation.setText("");

			colNameSoftArc = new TableColumn(tableSimulation, SWT.NONE);
			colNameSoftArc.setWidth(100);
			colNameSoftArc.setText(Messages.getString("UCM2DEVS_Name_Column"));

			colPathSoftArc = new TableColumn(tableSimulation, SWT.NONE);
			colPathSoftArc.setWidth(300);
			colPathSoftArc.setText(Messages.getString("UCM2DEVS_Path_Column"));

			for (int i = 0; i < 5; i++) {
				TableItem item = new TableItem(tableSimulation, SWT.NONE);
			}

			tableSimulation.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					tableSimulation.showSelection();
					if (tableSimulation.getSelectionIndex() != -1) {
						viewController.setModel(
								(Architecture) tableSimulation.getItem(tableSimulation.getSelectionIndex()).getData());
						fillTableQR();
						prepareView(1);
					} else {
						prepareView(0);
					}
				}
			});
			

			gridData = new GridData();
			gridData.horizontalSpan = 4;

			Label labelEmptyTwo = new Label(parent, SWT.NULL);
			labelEmptyTwo.setLayoutData(gridData);

			gridData = new GridData();
			gridData.horizontalSpan = 4;

			// Table: Quality Requirement

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
			tableQualityRequirement = new Table(gQualityRequirement, styleReq);
			TableLayout tableLayoutReq = new TableLayout();
			tableQualityRequirement.setLayout(tableLayoutReq);
			gridData = new GridData(GridData.FILL_BOTH);
			gridData.horizontalSpan = 4;
			tableQualityRequirement.setLayoutData(gridData);
			tableQualityRequirement.setLinesVisible(true);
			tableQualityRequirement.setHeaderVisible(true);

			// Create columns
			colObject = new TableColumn(tableQualityRequirement, SWT.NONE);
			colObject.setWidth(0);
			colObject.setText(Messages.getString("UCM2DEVS_Object_Column"));

			colQRQualityAttribute = new TableColumn(tableQualityRequirement, SWT.NONE);
			colQRQualityAttribute.setWidth(100);
			colQRQualityAttribute.setText(Messages.getString("UCM2DEVS_QualityAttribute_Column"));

			colDescriptionScenario = new TableColumn(tableQualityRequirement, SWT.NONE);
			colDescriptionScenario.setWidth(300);
			colDescriptionScenario.setText(Messages.getString("UCM2DEVS_DescriptionScenario_Column"));

			for (int i = 0; i < 5; i++) {
				TableItem item = new TableItem(tableQualityRequirement, SWT.NONE);
				item.setText("");
			}

			// Create TableViewer
			tblViewerQualityRequirement = new TableViewer(tableQualityRequirement);
			tblViewerQualityRequirement.setUseHashlookup(true);
			tblViewerQualityRequirement.setColumnProperties(columnNamesReq);

			// Create the cell editors
			CellEditor[] editorsQR = new CellEditor[columnNamesReq.length];
			editorsQR[0] = null;
			editorsQR[1] = null;
			editorsQR[2] = null;

			// Assign the cell editors to the viewer
			tblViewerQualityRequirement.setCellEditors(editorsQR);
			tableQualityRequirement.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event event) {
					viewController.setQualityAttribute((QualityRequirement) tableQualityRequirement
							.getItem(tableQualityRequirement.getSelectionIndex()).getData());
					viewController.setQualityRequirement((QualityRequirement) tableQualityRequirement
							.getItem(tableQualityRequirement.getSelectionIndex()).getData());
					fillTableReport();
					prepareView(2);
				}
			});


			gridData = new GridData();
			gridData.horizontalSpan = 4;

			Label labelEmptyTree = new Label(parent, SWT.NULL);
			labelEmptyTree.setLayoutData(gridData);
			
			gridData = new GridData();
			gridData.horizontalSpan = 4;
			
			// Table: Report
			Group gReport = new Group(parent, SWT.NONE);
			gReport.setLayoutData(gridData);
			gReport.setText(Messages.getString("UCM2DEVS_QualityRequirement_Group"));
			gReport.setLayout(new GridLayout(2, false));

			// Create column names
			String[] columnReport = new String[] { "", Messages.getString("UCM2DEVS_DescriptionObject_Column"),
					Messages.getString("UCM2DEVS_QualityAttribute_Column"),
					Messages.getString("UCM2DEVS_DescriptionScenario_Column") };
			// Create styles
			int styleReport = SWT.FULL_SELECTION | SWT.BORDER;
			// create table
			tableReport = new Table(gReport, styleReport);
			TableLayout tableLayoutReport = new TableLayout();
			tableReport.setLayout(tableLayoutReport);
			gridData = new GridData(GridData.FILL_BOTH);
			gridData.horizontalSpan = 4;
			tableReport.setLayoutData(gridData);
			tableReport.setLinesVisible(true);
			tableReport.setHeaderVisible(true);

			// Create columns
			colReportQualityAttribute = new TableColumn(tableReport, SWT.NONE);
			colReportQualityAttribute.setWidth(100);
			colReportQualityAttribute.setText(Messages.getString("UCM2DEVS_QualityAttribute_Column"));

			colIndicatorType = new TableColumn(tableReport, SWT.NONE);
			colIndicatorType.setWidth(150);
			colIndicatorType.setText(Messages.getString("UCM2DEVS_IndicatorType_Column"));

			colViewReportButtom = new TableColumn(tableReport, SWT.NONE);
			colViewReportButtom.setWidth(100);
			colViewReportButtom.setText("");

			// Create five table editors for color
			TableEditor[] reportEditors = new TableEditor[3];

			TableItem itemResp = new TableItem(tableReport, SWT.NONE);
			reportEditors[0] = new TableEditor(tableReport);
			btnResponsabilityViewReport = new Button(tableReport, SWT.PUSH);
			// btnResponsabilityViewReport.setText("View Report...");
			reportEditors[0].grabHorizontal = true;
			reportEditors[0].setEditor(btnResponsabilityViewReport, itemResp, 2);

			btnResponsabilityViewReport.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent event) {
					viewController.printReportResponsability();
				}
			});

			TableItem itemSys = new TableItem(tableReport, SWT.NONE);
			reportEditors[1] = new TableEditor(tableReport);
			btnSystemViewReport = new Button(tableReport, SWT.PUSH);
			// btnSystemViewReport.setText("View Report...");
			reportEditors[1].grabHorizontal = true;
			reportEditors[1].setEditor(btnSystemViewReport, itemSys, 2);

			btnSystemViewReport.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent event) {
					viewController.printReportSystem();
				}
			});

			// Create TableViewer
			tblViewerReport = new TableViewer(tableReport);
			tblViewerReport.setUseHashlookup(true);
			tblViewerReport.setColumnProperties(columnReport);

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

	public ComboViewer getCmbSystem() {
		return cmbSystem;
	}

	public void setCmbSystem(ComboViewer cboSystem) {
		this.cmbSystem = cboSystem;
	}

	public ReportsPPController getViewController() {
		return viewController;
	}

	public void setViewController(ReportsPPController viewController) {
		this.viewController = viewController;
	}

	public Button getBtnSystemViewReport() {
		return btnSystemViewReport;
	}

	public void setBtnSystemViewReport(Button btnSystemViewReport) {
		this.btnSystemViewReport = btnSystemViewReport;
	}

	public Button getBtnResponsabilityViewReport() {
		return btnResponsabilityViewReport;
	}

	public void setBtnResponsabilityViewReport(Button btnResponsabilityViewReport) {
		this.btnResponsabilityViewReport = btnResponsabilityViewReport;
	}

	public Table getTableSimulation() {
		return tableSimulation;
	}

	public void setTableSimulation(Table tableSimulation) {
		this.tableSimulation = tableSimulation;
	}

	public TableViewer getTblViewerQualityRequirement() {
		return tblViewerQualityRequirement;
	}

	public void setTblViewerQualityRequirement(TableViewer tblViewerQualityRequirement) {
		this.tblViewerQualityRequirement = tblViewerQualityRequirement;
	}

	public Table getTableQualityRequirement() {
		return tableQualityRequirement;
	}

	public void setTableQualityRequirement(Table tableQualityRequirement) {
		this.tableQualityRequirement = tableQualityRequirement;
	}

	public Table getTableReport() {
		return tableReport;
	}

	public void setTableReport(Table tableReport) {
		this.tableReport = tableReport;
	}

	/**
	 * load combo with system with state=true
	 */
	public void loadCombo() {
		this.getViewController().setModel();
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
		switch (pabm) {
		case 0: // There are systems with architecture or quality requirement
			this.getCmbSystem().getCombo().setEnabled(true);
			this.getTableSimulation().setEnabled(false);
			this.getTblViewerQualityRequirement().getTable().setEnabled(false);
			this.getBtnSystemViewReport().setEnabled(false);
			this.getBtnResponsabilityViewReport().setEnabled(false);
			break;
		case 1: // With system selected
			this.getTableSimulation().setEnabled(true);
			this.getTblViewerQualityRequirement().getTable().setEnabled(true);
			this.getBtnSystemViewReport().setEnabled(false);
			this.getBtnResponsabilityViewReport().setEnabled(false);
			this.getBtnResponsabilityViewReport().setText("");
			this.getBtnSystemViewReport().setText("");
			this.getTableReport().getItem(0).setText(1, "");
			this.getTableReport().getItem(1).setText(1, "");
			this.getTableReport().getItem(0).setText(0, "");
			this.getTableReport().getItem(1).setText(0, "");
			break;
		case 2:// With architecture selected
			this.getTableSimulation().setEnabled(true);
			this.getTblViewerQualityRequirement().getTable().setEnabled(true);
			this.getBtnSystemViewReport().setEnabled(true);
			this.getBtnResponsabilityViewReport().setEnabled(true);
			this.getBtnResponsabilityViewReport().setText(Messages.getString("UCM2DEVS_ViewReport_Buttom"));
			this.getBtnSystemViewReport().setText(Messages.getString("UCM2DEVS_ViewReport_Buttom"));
			this.getTableReport().getItem(0).setText(1, Messages.getString("UCM2DEVS_ResponsibilityReport_Item"));
			this.getTableReport().getItem(1).setText(1, Messages.getString("UCM2DEVS_SystemReport_Item"));
			break;
		}
	}

	/**
	 * When a system is selected, fill table with its quality requirements and
	 * prepare the view
	 */
	private void cmbSystemItemStateChanged() {// GEN-FIRST:event_cmbNombreItemStateChanged
		this.fillTableSimulation();
	}

	/**
	 * Fill table with system's quality requirements (quality attribute,
	 * description and condition)
	 */
	public void fillTableSimulation() {
		this.getViewController().setModelSimulation(
				(DomainModel.AnalysisEntity.System) ((IStructuredSelection) this.getCmbSystem().getSelection())
						.getFirstElement());
	}

	/**
	 * // * Fill table with system's quality requirements (quality attribute, //
	 * * description and condition) //
	 */
	public void fillTableQR() {
		this.getViewController().setModelQualityRequirement();
	}

	public void fillTableReport() {
		this.getViewController().setModelReport();
	}

}