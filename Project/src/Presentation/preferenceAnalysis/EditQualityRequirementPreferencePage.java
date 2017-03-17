package Presentation.preferenceAnalysis;

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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import DomainModel.AnalysisEntity.Metric;
import DomainModel.AnalysisEntity.QualityAttribute;
import DomainModel.AnalysisEntity.QualityRequirement;
import DomainModel.AnalysisEntity.ResponseMeasureType;
import Presentation.controllerAnalysis.EditQualityRequirementPPController;
import Presentation.preferences.DoubleFieldEditor;
import Presentation.preferences.Messages;

/**
 * To search, consult, edit or remove a quality requirement
 * 
 * @author: Micaela Montenegro. E-mail: mica.montenegro.sistemas@gmail.com
 */
public class EditQualityRequirementPreferencePage extends FieldEditorPreferencePage
		implements IWorkbenchPreferencePage {
	/**
	 * Attributes
	 */
	private Group groupQualityRequirement;
	private TableViewer tblViewerQualityRequirement;
	private Table table;
	private GridData gridData;
	private ComboViewer cmbQualityAttribute;
	private Text txtDescription;
	private Text txtDescriptionStimulusSource;
	private Text txtDescriptionStimulus;
	private Text txtDescriptionEnvironment;
	private Text txtDescriptionArtifact;
	private Text txtDescriptionResponse;
	private Text txtDescriptionResponseMeasure;
	private StringFieldEditor txtValueStimulusSource;
	private StringFieldEditor txtValueStimulus;
	private StringFieldEditor txtValueEnvironment;
	private StringFieldEditor txtValueResponse;
	private DoubleFieldEditor txtValueResponseMeasure;
	private ComboViewer cmbMetric;
	private ComboViewer cmbTypeStimulusSource;
	private ComboViewer cmbTypeStimulus;
	private ComboViewer cmbTypeEnvironment;
	private ComboViewer cmbTypeArtifact;
	private ComboViewer cmbTypeResponse;
	private ComboViewer cmbTypeResponseMeasure;
	private ComboViewer cmbUnit;
	private ComboViewer cmbSystem;
	private Group gStimulusSource;
	private Group gResponse;
	private Group gStimulus;
	private Group gArtifact;
	private Group gEnvironment;
	private Group gResponseMeasure;
	private Button btnSave;
	private Button btnRemove;
	private TableColumn colObject;
	private TableColumn colQualityAttribute;
	private TableColumn colDescriptionScenario;
	private EditQualityRequirementPPController viewController;

	/**
	 * Constructor
	 */
	public EditQualityRequirementPreferencePage() {
		super(GRID);
		try {
			noDefaultAndApplyButton();
			System.runFinalization();
			Runtime.getRuntime().gc();
			this.setViewController(EditQualityRequirementPPController.getViewController());
			this.getViewController().setFormSearch(this);
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
						clearScenario();
						prepareView(7);
						cmbSystemItemStateChanged();
					} else {
						clearScenario();
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

			Group gQualityRequirement = new Group(cSystemName, SWT.NONE);
			gQualityRequirement.setLayoutData(gridData);
			gQualityRequirement.setText(Messages.getString("UCM2DEVS_QualityRequirement_Group"));
			gQualityRequirement.setLayout(new GridLayout(2, false));

			// Create column names
			String[] columnNames = new String[] { Messages.getString("UCM2DEVS_DescriptionObject_Column"),
					Messages.getString("UCM2DEVS_QualityAttribute_Column"),
					Messages.getString("UCM2DEVS_DescriptionScenario_Column") };
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

			colQualityAttribute = new TableColumn(table, SWT.NONE);
			colQualityAttribute.setWidth(200);
			colQualityAttribute.setText(Messages.getString("UCM2DEVS_QualityAttribute_Column"));

			colDescriptionScenario = new TableColumn(table, SWT.NONE);
			colDescriptionScenario.setWidth(200);
			colDescriptionScenario.setText(Messages.getString("UCM2DEVS_DescriptionScenario_Column"));

			for (int i = 0; i < 4; i++) {
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

			table.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					table.showSelection();
					getViewController()
							.setModel((QualityRequirement) table.getItem(table.getSelectionIndex()).getData());
					getViewController().getFormSearch().setView();
					prepareView(6);
					if (viewController.getManager().getMetric().getName().equals("Number of failures")) {
						cmbUnit.getCombo().setEnabled(false);
					}
				}
			});

			gridData = new GridData();
			gridData.horizontalSpan = 1;
			gridData.widthHint = 100;
			gridData.horizontalAlignment = GridData.END;
			gridData.verticalAlignment = SWT.BOTTOM;
			gridData.grabExcessHorizontalSpace = true;

			GridLayout layout1 = new GridLayout();
			layout.numColumns = 4;
			parent.setLayout(layout1);

			gridData = new GridData();
			gridData.horizontalSpan = 4;
			gridData.horizontalAlignment = GridData.FILL;

			Group gScenario = new Group(cSystemName, SWT.NONE);
			gScenario.setLayoutData(gridData);
			gScenario.setText(Messages.getString("UCM2DEVS_Scenario_Group"));
			gScenario.setLayout(new GridLayout(2, false));

			Label labelD = new Label(gScenario, SWT.NONE);
			labelD.setText(Messages.getString("UCM2DEVS_Description_Label") + ":");

			gridData = new GridData();
			gridData.horizontalAlignment = GridData.FILL;
			gridData.grabExcessHorizontalSpace = true;

			txtDescription = new Text(gScenario, SWT.BORDER | SWT.MULTI);
			txtDescription.setLayoutData(gridData);

			Label labelQA = new Label(gScenario, SWT.NONE);
			labelQA.setText(Messages.getString("UCM2DEVS_QualityAttribute_Label") + ":");

			cmbQualityAttribute = new ComboViewer(gScenario, SWT.READ_ONLY);
			cmbQualityAttribute.setContentProvider(ArrayContentProvider.getInstance());
			cmbQualityAttribute.getCombo().addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					if (((IStructuredSelection) cmbQualityAttribute.getSelection()).getFirstElement() != "") {
						cmbQualityAttributeItemStateChanged();
					} else {
						clearParts();
						prepareView(2);
					}
				}
			});

			gridData = new GridData();
			gridData.horizontalSpan = 4;

			Label labelEmptyTree = new Label(cSystemName, SWT.NULL);
			labelEmptyTree.setLayoutData(gridData);

			gridData = new GridData();
			gridData.horizontalSpan = 4;
			gridData.horizontalAlignment = GridData.FILL;

			TabFolder folder = new TabFolder(cSystemName, SWT.NONE);
			folder.setLayoutData(gridData);

			/*---------------------------------------------------------------------------*/
			TabItem tabStimulusSource = new TabItem(folder, SWT.NONE);
			tabStimulusSource.setText(Messages.getString("UCM2DEVS_StimulusSource_Label"));

			gStimulusSource = new Group(folder, SWT.NONE);
			gStimulusSource.setLayoutData(gridData);

			gridData = new GridData();
			gridData.horizontalAlignment = GridData.FILL;
			gridData.grabExcessHorizontalSpace = true;

			Label labelSST = new Label(gStimulusSource, SWT.NONE);
			labelSST.setText(Messages.getString("UCM2DEVS_Type_Label") + ":");

			cmbTypeStimulusSource = new ComboViewer(gStimulusSource, SWT.READ_ONLY);
			cmbTypeStimulusSource.setContentProvider(ArrayContentProvider.getInstance());

			txtValueStimulusSource = new StringFieldEditor(Messages.getString("UCM2DEVS_ValueStimulusSource_Label"),
					Messages.getString("UCM2DEVS_ValueStimulusSource_Label") + ":", gStimulusSource);

			Label labelDSS = new Label(gStimulusSource, SWT.NONE);
			labelDSS.setText(Messages.getString("UCM2DEVS_Description_Label") + ":");

			txtDescriptionStimulusSource = new Text(gStimulusSource, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
			txtDescriptionStimulusSource.setLayoutData(gridData);

			gridData = new GridData();
			gridData.horizontalSpan = 4;
			gridData.horizontalAlignment = GridData.FILL;

			tabStimulusSource.setControl(gStimulusSource);

			/*---------------------------------------------------------------------------*/
			TabItem tabStimulus = new TabItem(folder, SWT.NONE);
			tabStimulus.setText(Messages.getString("UCM2DEVS_Stimulus_Label"));

			gStimulus = new Group(folder, SWT.NONE);
			gStimulus.setLayoutData(gridData);

			gridData = new GridData();
			gridData.horizontalAlignment = GridData.FILL;
			gridData.grabExcessHorizontalSpace = true;

			Label labelST = new Label(gStimulus, SWT.NONE);
			labelST.setText(Messages.getString("UCM2DEVS_Type_Label") + ":");

			cmbTypeStimulus = new ComboViewer(gStimulus, SWT.READ_ONLY);
			cmbTypeStimulus.setContentProvider(ArrayContentProvider.getInstance());

			txtValueStimulus = new StringFieldEditor(Messages.getString("UCM2DEVS_ValueStimulus_Label"),
					Messages.getString("UCM2DEVS_ValueStimulus_Label") + ":", gStimulus);

			Label labelDS = new Label(gStimulus, SWT.NONE);
			labelDS.setText(Messages.getString("UCM2DEVS_Description_Label") + ":");

			txtDescriptionStimulus = new Text(gStimulus, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
			txtDescriptionStimulus.setLayoutData(gridData);

			tabStimulus.setControl(gStimulus);

			/*---------------------------------------------------------------------------*/
			TabItem tabEnvironment = new TabItem(folder, SWT.NONE);
			tabEnvironment.setText(Messages.getString("UCM2DEVS_Environment_Label"));

			gEnvironment = new Group(folder, SWT.NONE);
			gEnvironment.setLayoutData(gridData);

			gridData = new GridData();
			gridData.horizontalAlignment = GridData.FILL;
			gridData.grabExcessHorizontalSpace = true;

			Label labelET = new Label(gEnvironment, SWT.NONE);
			labelET.setText(Messages.getString("UCM2DEVS_Type_Label") + ":");

			cmbTypeEnvironment = new ComboViewer(gEnvironment, SWT.READ_ONLY);
			cmbTypeEnvironment.setContentProvider(ArrayContentProvider.getInstance());

			txtValueEnvironment = new StringFieldEditor(Messages.getString("UCM2DEVS_ValueEnvironment_Label"),
					Messages.getString("UCM2DEVS_ValueEnvironment_Label") + ":", gEnvironment);

			Label labelDE = new Label(gEnvironment, SWT.NONE);
			labelDE.setText(Messages.getString("UCM2DEVS_Description_Label") + ":");

			txtDescriptionEnvironment = new Text(gEnvironment, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
			txtDescriptionEnvironment.setLayoutData(gridData);

			tabEnvironment.setControl(gEnvironment);

			/*---------------------------------------------------------------------------*/
			TabItem tabArtifact = new TabItem(folder, SWT.NONE);
			tabArtifact.setText(Messages.getString("UCM2DEVS_Artifact_Label"));

			gArtifact = new Group(folder, SWT.NONE);
			gArtifact.setLayoutData(gridData);

			gridData = new GridData();
			gridData.horizontalAlignment = GridData.FILL;
			gridData.grabExcessHorizontalSpace = true;

			Label labelAT = new Label(gArtifact, SWT.NONE);
			labelAT.setText(Messages.getString("UCM2DEVS_Type_Label") + ":");

			cmbTypeArtifact = new ComboViewer(gArtifact, SWT.READ_ONLY);
			cmbTypeArtifact.setContentProvider(ArrayContentProvider.getInstance());

			Label labelDA = new Label(gArtifact, SWT.NONE);
			labelDA.setText(Messages.getString("UCM2DEVS_Description_Label") + ":");

			txtDescriptionArtifact = new Text(gArtifact, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
			txtDescriptionArtifact.setLayoutData(gridData);

			StringFieldEditor text = new StringFieldEditor("", "", gArtifact);
			text.getLabelControl(gArtifact).setVisible(false);
			text.getTextControl(gArtifact).setVisible(false);

			tabArtifact.setControl(gArtifact);

			/*---------------------------------------------------------------------------*/
			TabItem tabResponse = new TabItem(folder, SWT.NONE);
			tabResponse.setText(Messages.getString("UCM2DEVS_Response_Label"));

			gResponse = new Group(folder, SWT.NONE);
			gResponse.setLayoutData(gridData);

			gridData = new GridData();
			gridData.horizontalAlignment = GridData.FILL;
			gridData.grabExcessHorizontalSpace = true;

			Label labelRT = new Label(gResponse, SWT.NONE);
			labelRT.setText(Messages.getString("UCM2DEVS_Type_Label") + ":");

			cmbTypeResponse = new ComboViewer(gResponse, SWT.READ_ONLY);
			cmbTypeResponse.setContentProvider(ArrayContentProvider.getInstance());

			txtValueResponse = new StringFieldEditor(Messages.getString("UCM2DEVS_ValueResponse_Label"),
					Messages.getString("UCM2DEVS_ValueResponse_Label") + ":", gResponse);

			Label labelDR = new Label(gResponse, SWT.NONE);
			labelDR.setText(Messages.getString("UCM2DEVS_Description_Label") + ":");

			txtDescriptionResponse = new Text(gResponse, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
			txtDescriptionResponse.setLayoutData(gridData);

			/*---------------------------------------------------------------------------*/
			tabResponse.setControl(gResponse);

			TabItem tabResponseMeasure = new TabItem(folder, SWT.NONE);
			tabResponseMeasure.setText(Messages.getString("UCM2DEVS_ResponseMeasure_Label"));

			gResponseMeasure = new Group(folder, SWT.NONE);
			gResponseMeasure.setLayoutData(gridData);

			gridData = new GridData();
			gridData.horizontalAlignment = GridData.FILL;

			Label labelRMM = new Label(gResponseMeasure, SWT.NONE);
			labelRMM.setText(Messages.getString("UCM2DEVS_Type_Label") + ":");

			cmbTypeResponseMeasure = new ComboViewer(gResponseMeasure, SWT.READ_ONLY);
			cmbTypeResponseMeasure.setContentProvider(ArrayContentProvider.getInstance());
			cmbTypeResponseMeasure.getCombo().addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					if (((IStructuredSelection) cmbTypeResponseMeasure.getSelection()).getFirstElement() != "") {
						cmbTypeResponseMeasureItemStateChanged();
					} else {
						getCmbMetric().getCombo().clearSelection();
						getCmbUnit().getCombo().clearSelection();
					}
				}
			});

			Label labelRMT = new Label(gResponseMeasure, SWT.NONE);
			labelRMT.setText(Messages.getString("UCM2DEVS_Metric_Label") + ":");

			cmbMetric = new ComboViewer(gResponseMeasure, SWT.READ_ONLY);
			cmbMetric.setContentProvider(ArrayContentProvider.getInstance());
			cmbMetric.getCombo().addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					if (((IStructuredSelection) cmbMetric.getSelection()).getFirstElement() != "") {
						Metric m = (Metric) ((IStructuredSelection) cmbMetric.getSelection()).getFirstElement();
						if (!m.getName().equals("Number of failures")) {
							cmbMetricItemStateChanged();
						}
					} else {
						getCmbUnit().getCombo().clearSelection();
					}
				}
			});

			txtValueResponseMeasure = new DoubleFieldEditor(
					Messages.getString("UCM2DEVS_ValueResponseMeasure_Label") + ":",
					Messages.getString("UCM2DEVS_ValueResponseMeasure_Label") + ":", gResponseMeasure);
			txtValueResponseMeasure.setMinRange(0.0);
			txtValueResponseMeasure.setPage(this);

			Label labelRMU = new Label(gResponseMeasure, SWT.NONE);
			labelRMU.setText(Messages.getString("UCM2DEVS_Unit_Label") + ":");

			cmbUnit = new ComboViewer(gResponseMeasure, SWT.READ_ONLY);
			cmbUnit.setContentProvider(ArrayContentProvider.getInstance());

			Label labelDRM = new Label(gResponseMeasure, SWT.NONE);
			labelDRM.setText(Messages.getString("UCM2DEVS_Description_Label") + ":");

			txtDescriptionResponseMeasure = new Text(gResponseMeasure,
					SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
			txtDescriptionResponseMeasure.setLayoutData(gridData);

			tabResponseMeasure.setControl(gResponseMeasure);

			/*---------------------------------------------------------------------------*/
			gridData = new GridData();
			gridData.horizontalSpan = 2;

			Label labelEmptyFour = new Label(cSystemName, SWT.NULL);
			labelEmptyFour.setLayoutData(gridData);

			Composite cButtoms = new Composite(cSystemName, SWT.RIGHT);
			cButtoms.setLayout(layout);
			gridData = new GridData();
			gridData.horizontalSpan = 4;
			gridData.horizontalAlignment = GridData.END;
			cButtoms.setLayoutData(gridData);

			gridData = new GridData();
			gridData.horizontalAlignment = GridData.END;
			gridData.grabExcessHorizontalSpace = true;
			gridData.widthHint = 100;

			btnSave = new Button(cButtoms, SWT.PUSH);
			btnSave.setText(Messages.getString("UCM2DEVS_Save_Buttom"));
			btnSave.setLayoutData(gridData);
			btnSave.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					parent.setCursor(cursorWait);
					int var = viewController.save();
					parent.setCursor(cursorNotWait);
					if (var == 0) {
						viewController.createObjectSuccessDialog();
					} else if (var == 1) {
						viewController.createObjectDontUpdateErrorDialog();
					}
				}
			});

			btnRemove = new Button(cButtoms, SWT.PUSH);
			btnRemove.setText(Messages.getString("UCM2DEVS_Remove_Buttom"));
			btnRemove.setLayoutData(gridData);
			btnRemove.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					if (viewController.createDeleteRequirementDialog() == true) {
						parent.setCursor(cursorWait);
						viewController.remove();
						parent.setCursor(cursorNotWait);
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
	public Text getTxtDescription() {
		return txtDescription;
	}

	public void setTxtDescription(Text txtDescription) {
		this.txtDescription = txtDescription;
	}

	public ComboViewer getCmbQualityAttribute() {
		return cmbQualityAttribute;
	}

	public void setCmbQualityAttribute(ComboViewer cmbQualityAttribute) {
		this.cmbQualityAttribute = cmbQualityAttribute;
	}

	public Text getTxtDescriptionStimulusSource() {
		return txtDescriptionStimulusSource;
	}

	public void setTxtDescriptionStimulusSource(Text txtDescriptionStimulusSource) {
		this.txtDescriptionStimulusSource = txtDescriptionStimulusSource;
	}

	public Text getTxtDescriptionStimulus() {
		return txtDescriptionStimulus;
	}

	public void setTxtDescriptionStimulus(Text txtDescriptionStimulus) {
		this.txtDescriptionStimulus = txtDescriptionStimulus;
	}

	public Text getTxtDescriptionEnvironment() {
		return txtDescriptionEnvironment;
	}

	public void setTxtDescriptionEnvironment(Text txtDescriptionEnvironment) {
		this.txtDescriptionEnvironment = txtDescriptionEnvironment;
	}

	public Text getTxtDescriptionArtifact() {
		return txtDescriptionArtifact;
	}

	public void setTxtDescriptionArtifact(Text txtDescriptionArtifact) {
		this.txtDescriptionArtifact = txtDescriptionArtifact;
	}

	public Text getTxtDescriptionResponse() {
		return txtDescriptionResponse;
	}

	public void setTxtDescriptionResponse(Text txtDescriptionResponse) {
		this.txtDescriptionResponse = txtDescriptionResponse;
	}

	public Text getTxtDescriptionResponseMeasure() {
		return txtDescriptionResponseMeasure;
	}

	public void setTxtDescriptionResponseMeasure(Text txtDescriptionResponseMeasure) {
		this.txtDescriptionResponseMeasure = txtDescriptionResponseMeasure;
	}

	public StringFieldEditor getTxtValueStimulusSource() {
		return txtValueStimulusSource;
	}

	public void setTxtValueStimulusSource(StringFieldEditor txtValueStimulusSource) {
		this.txtValueStimulusSource = txtValueStimulusSource;
	}

	public StringFieldEditor getTxtValueStimulus() {
		return txtValueStimulus;
	}

	public void setTxtValueStimulus(StringFieldEditor txtValueStimulus) {
		this.txtValueStimulus = txtValueStimulus;
	}

	public StringFieldEditor getTxtValueEnvironment() {
		return txtValueEnvironment;
	}

	public void setTxtValueEnvironment(StringFieldEditor txtValueEnvironment) {
		this.txtValueEnvironment = txtValueEnvironment;
	}

	public StringFieldEditor getTxtValueResponse() {
		return txtValueResponse;
	}

	public void setTxtValueResponse(StringFieldEditor txtValueResponse) {
		this.txtValueResponse = txtValueResponse;
	}

	public DoubleFieldEditor getTxtValueResponseMeasure() {
		return txtValueResponseMeasure;
	}

	public void setTxtValueResponseMeasure(DoubleFieldEditor txtValueResponseMeasure) {
		this.txtValueResponseMeasure = txtValueResponseMeasure;
	}

	public ComboViewer getCmbMetric() {
		return cmbMetric;
	}

	public void setCmbMetric(ComboViewer cmbMetric) {
		this.cmbMetric = cmbMetric;
	}

	public ComboViewer getCmbTypeStimulusSource() {
		return cmbTypeStimulusSource;
	}

	public void setCmbTypeStimulusSource(ComboViewer cmbTypeStimulusSource) {
		this.cmbTypeStimulusSource = cmbTypeStimulusSource;
	}

	public ComboViewer getCmbTypeStimulus() {
		return cmbTypeStimulus;
	}

	public void setCmbTypeStimulus(ComboViewer cmbTypeStimulus) {
		this.cmbTypeStimulus = cmbTypeStimulus;
	}

	public ComboViewer getCmbTypeEnvironment() {
		return cmbTypeEnvironment;
	}

	public void setCmbTypeEnvironment(ComboViewer cmbTypeEnvironment) {
		this.cmbTypeEnvironment = cmbTypeEnvironment;
	}

	public ComboViewer getCmbTypeArtifact() {
		return cmbTypeArtifact;
	}

	public void setCmbTypeArtifact(ComboViewer cmbTypeArtifact) {
		this.cmbTypeArtifact = cmbTypeArtifact;
	}

	public ComboViewer getCmbTypeResponse() {
		return cmbTypeResponse;
	}

	public void setCmbTypeResponse(ComboViewer cmbTypeResponse) {
		this.cmbTypeResponse = cmbTypeResponse;
	}

	public ComboViewer getCmbTypeResponseMeasure() {
		return cmbTypeResponseMeasure;
	}

	public void setCmbTypeResponseMeasure(ComboViewer cmbTypeResponseMeasure) {
		this.cmbTypeResponseMeasure = cmbTypeResponseMeasure;
	}

	public ComboViewer getCmbUnit() {
		return cmbUnit;
	}

	public void setCmbUnit(ComboViewer cmbUnit) {
		this.cmbUnit = cmbUnit;
	}

	public Button getBtnSave() {
		return btnSave;
	}

	public void setBtnSave(Button btnSave) {
		this.btnSave = btnSave;
	}

	public Button getBtnRemove() {
		return btnRemove;
	}

	public void setBtnRemove(Button btnRemove) {
		this.btnRemove = btnRemove;
	}

	public EditQualityRequirementPPController getViewController() {
		return viewController;
	}

	public void setViewController(EditQualityRequirementPPController viewController) {
		this.viewController = viewController;
	}

	public ComboViewer getCmbSystem() {
		return cmbSystem;
	}

	public void setCmbSystem(ComboViewer cmbSystem) {
		this.cmbSystem = cmbSystem;
	}

	public Group getGroupQualityRequirement() {
		return groupQualityRequirement;
	}

	public void setGroupQualityRequirement(Group groupQualityRequirement) {
		this.groupQualityRequirement = groupQualityRequirement;
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

	public TableColumn getColObject() {
		return colObject;
	}

	public void setColObject(TableColumn colObject) {
		this.colObject = colObject;
	}

	public TableColumn getColQualityAttribute() {
		return colQualityAttribute;
	}

	public void setColQualityAttribute(TableColumn colQualityAttribute) {
		this.colQualityAttribute = colQualityAttribute;
	}

	public TableColumn getColDescriptionScenario() {
		return colDescriptionScenario;
	}

	public void setColDescriptionScenario(TableColumn colDescriptionScenario) {
		this.colDescriptionScenario = colDescriptionScenario;
	}

	/**
	 * Load systems with state=true and requirements with state=true in the
	 * combo
	 */
	public void loadCmbSystem() {
		this.getViewController().setModelSystemSearch();
	}

	/**
	 * When a system is selected, fill table with its quality requirements and
	 * prepare the view
	 */
	private void cmbSystemItemStateChanged() {// GEN-FIRST:event_cmbNombreItemStateChanged
		this.fillTable();
		this.prepareView(1);
	}

	/**
	 * Fill table with system's quality requirements (quality attribute,
	 * description and condition)
	 */
	public void fillTable() {
		this.getViewController().setModelQualityRequirement(
				(DomainModel.AnalysisEntity.System) ((IStructuredSelection) this.getCmbSystem().getSelection())
						.getFirstElement());
	}

	/**
	 * When a quality attribute is selected, enables the especification and load
	 * the generic scenario
	 */
	private void cmbQualityAttributeItemStateChanged() {// GEN-FIRST:event_cmbNombreItemStateChanged
		this.prepareView(3);
		this.loadGenericScenario(
				(QualityAttribute) ((IStructuredSelection) this.getCmbQualityAttribute().getSelection())
						.getFirstElement());
	}

	/**
	 * Load the types of the generic scenario for a specific quality attribute
	 * 
	 * @param qualityAtribute
	 */
	public void loadGenericScenario(QualityAttribute qualityAttribute) {
		this.getViewController().setModelStimulusSourceTypes(qualityAttribute);
		this.getViewController().setModelStimulusTypes(qualityAttribute);
		this.getViewController().setModelEnvironmentTypes(qualityAttribute);
		this.getViewController().setModelArtifactTypes(qualityAttribute);
		this.getViewController().setModelResponseTypes(qualityAttribute);
		this.getViewController().setModelResponseMeasureTypes(qualityAttribute);
	}

	/**
	 * When a response measure type is selected, load the metrics
	 */
	private void cmbTypeResponseMeasureItemStateChanged() {// GEN-FIRST:event_cmbNombreItemStateChanged
		this.prepareView(4);
		this.loadCmbMetric(
				(ResponseMeasureType) ((IStructuredSelection) this.getCmbTypeResponseMeasure().getSelection())
						.getFirstElement());
	}

	/**
	 * Load all metrics for a specific response measure type param
	 * responseMeasureType
	 */
	public void loadCmbMetric(ResponseMeasureType responseMeasureType) {
		this.getViewController().setModelMetric(responseMeasureType);
	}

	/**
	 * When a metric is selected, load the units
	 */
	private void cmbMetricItemStateChanged() {// GEN-FIRST:event_cmbNombreItemStateChanged
		this.prepareView(5);
		this.loadCmbUnit((Metric) ((IStructuredSelection) this.getCmbMetric().getSelection()).getFirstElement());
	}

	/**
	 * Load all units for a specific metric param metric
	 */
	public void loadCmbUnit(Metric metric) {
		this.getViewController().setModelUnit(metric);
	}

	/**
	 * Clean the quality scenario (description, quality attribute, condition and
	 * parts)
	 */
	public void clearScenario() {
		txtDescription.setText("");
		cmbQualityAttribute.setSelection(StructuredSelection.EMPTY);
		this.clearParts();
	}

	/**
	 * Clean the parts of quality scenario (descriptions, types and values)
	 */
	public void clearParts() {
		txtDescriptionStimulusSource.setText("");
		txtDescriptionStimulus.setText("");
		txtDescriptionEnvironment.setText("");
		txtDescriptionArtifact.setText("");
		txtDescriptionResponse.setText("");
		txtDescriptionResponseMeasure.setText("");

		cmbTypeStimulusSource.setSelection(StructuredSelection.EMPTY);
		cmbTypeStimulus.setSelection(StructuredSelection.EMPTY);
		cmbTypeEnvironment.setSelection(StructuredSelection.EMPTY);
		cmbTypeArtifact.setSelection(StructuredSelection.EMPTY);
		cmbTypeResponse.setSelection(StructuredSelection.EMPTY);
		cmbTypeResponseMeasure.setSelection(StructuredSelection.EMPTY);
		cmbMetric.setSelection(StructuredSelection.EMPTY);
		cmbUnit.setSelection(StructuredSelection.EMPTY);

		txtValueStimulusSource.getTextControl(gStimulusSource).setText("");
		txtValueStimulus.setStringValue("");
		txtValueEnvironment.setStringValue("");
		txtValueResponse.setStringValue("");
		txtValueResponseMeasure.setStringValue("0.1");
	}

	/**
	 * Clean the system selected and the quality scenario
	 */
	public void clearView() {
		cmbSystem.getCombo().clearSelection();
		this.clearScenario();
	}

	/**
	 * prepare the view for the different actions that are possible
	 * 
	 * @param pabm
	 */
	public void prepareView(int pabm) {
		this.getCmbSystem().getCombo().setFocus();
		if (!getViewController().existSystemTrueWithQualityRequirementTrue()) {
			this.getViewController().createErrorDialog(Messages.getString("UCM2DEVS_NoSavedSystemQR_ErrorDialog"));
			pabm = 8;
		}
		switch (pabm) {
		case 0:// Search quality requirement
			this.clearScenario();
			this.getCmbSystem().getCombo().setEnabled(true);
			loadCmbSystem();

			this.getTblViewerQualityRequirement().getTable().setEnabled(false);
			this.getTxtDescription().setEnabled(false);
			this.getCmbQualityAttribute().getCombo().setEnabled(false);

			this.getTxtDescriptionStimulusSource().setEnabled(false);
			this.getTxtDescriptionStimulus().setEnabled(false);
			this.getTxtDescriptionEnvironment().setEnabled(false);
			this.getTxtDescriptionArtifact().setEnabled(false);
			this.getTxtDescriptionResponse().setEnabled(false);
			this.getTxtDescriptionResponseMeasure().setEnabled(false);

			this.getCmbTypeStimulusSource().getCombo().setEnabled(false);
			this.getCmbTypeStimulus().getCombo().setEnabled(false);
			this.getCmbTypeEnvironment().getCombo().setEnabled(false);
			this.getCmbTypeArtifact().getCombo().setEnabled(false);
			this.getCmbTypeResponse().getCombo().setEnabled(false);
			this.getCmbTypeResponseMeasure().getCombo().setEnabled(false);
			this.getCmbMetric().getCombo().setEnabled(false);
			this.getCmbUnit().getCombo().setEnabled(false);

			this.getTxtValueStimulusSource().setEnabled(false, gStimulusSource);
			this.getTxtValueStimulus().setEnabled(false, gStimulus);
			this.getTxtValueEnvironment().setEnabled(false, gEnvironment);
			this.getTxtValueResponse().setEnabled(false, gResponse);
			this.getTxtValueResponseMeasure().setEnabled(false, gResponseMeasure);

			this.getBtnSave().setEnabled(false);
			this.getBtnRemove().setEnabled(false);

			break;
		case 1:// With system selected
			this.getTblViewerQualityRequirement().getTable().setEnabled(true);

			break;
		case 2:// With quality requirement selected
			this.getTxtDescription().setEnabled(true);
			this.getCmbQualityAttribute().getCombo().setEnabled(true);

			this.getBtnSave().setEnabled(true);
			this.getBtnRemove().setEnabled(true);

			break;
		case 3:// With quality attribute selected
			this.getTxtDescriptionStimulusSource().setEnabled(true);
			this.getTxtDescriptionStimulus().setEnabled(true);
			this.getTxtDescriptionEnvironment().setEnabled(true);
			this.getTxtDescriptionArtifact().setEnabled(true);
			this.getTxtDescriptionResponse().setEnabled(true);
			this.getTxtDescriptionResponseMeasure().setEnabled(true);

			this.getCmbTypeStimulusSource().getCombo().setEnabled(true);
			this.getCmbTypeStimulus().getCombo().setEnabled(true);
			this.getCmbTypeEnvironment().getCombo().setEnabled(true);
			this.getCmbTypeArtifact().getCombo().setEnabled(true);
			this.getCmbTypeResponse().getCombo().setEnabled(true);
			this.getCmbTypeResponseMeasure().getCombo().setEnabled(true);

			this.getTxtValueStimulusSource().setEnabled(true, gStimulusSource);
			this.getTxtValueStimulus().setEnabled(true, gStimulus);
			this.getTxtValueEnvironment().setEnabled(true, gEnvironment);
			this.getTxtValueResponse().setEnabled(true, gResponse);
			this.getTxtValueResponseMeasure().setEnabled(true, gResponseMeasure);

			break;
		case 4:// With type response measure selected
			this.getCmbMetric().getCombo().setEnabled(true);

			this.getCmbUnit().setSelection(StructuredSelection.EMPTY);
			this.getCmbUnit().getCombo().setEnabled(false);

			break;
		case 5:// With metric selected
			this.getCmbUnit().getCombo().setEnabled(true);

			break;
		case 6: // With bottom consult selected
			this.getTxtDescription().setEnabled(true);
			this.getCmbQualityAttribute().getCombo().setEnabled(false);

			this.getTxtDescriptionStimulusSource().setEnabled(true);
			this.getTxtDescriptionStimulus().setEnabled(true);
			this.getTxtDescriptionEnvironment().setEnabled(true);
			this.getTxtDescriptionArtifact().setEnabled(true);
			this.getTxtDescriptionResponse().setEnabled(true);
			this.getTxtDescriptionResponseMeasure().setEnabled(true);

			this.getCmbTypeStimulusSource().getCombo().setEnabled(true);
			this.getCmbTypeStimulus().getCombo().setEnabled(true);
			this.getCmbTypeEnvironment().getCombo().setEnabled(true);
			this.getCmbTypeArtifact().getCombo().setEnabled(true);
			this.getCmbTypeResponse().getCombo().setEnabled(true);
			this.getCmbTypeResponseMeasure().getCombo().setEnabled(true);

			this.getTxtValueStimulusSource().setEnabled(true, gStimulusSource);
			this.getTxtValueStimulus().setEnabled(true, gStimulus);
			this.getTxtValueEnvironment().setEnabled(true, gEnvironment);
			this.getTxtValueResponse().setEnabled(true, gResponse);
			this.getTxtValueResponseMeasure().setEnabled(true, gResponseMeasure);

			this.getCmbMetric().getCombo().setEnabled(true);

			this.getCmbUnit().getCombo().setEnabled(true);

			this.getBtnSave().setEnabled(true);
			this.getBtnRemove().setEnabled(true);

			break;

		case 7:// Search quality requirement
			this.getCmbSystem().getCombo().setEnabled(true);

			this.clearScenario();

			this.getTblViewerQualityRequirement().getTable().setEnabled(true);
			this.getTxtDescription().setEnabled(false);
			this.getCmbQualityAttribute().getCombo().setEnabled(false);

			this.getTxtDescriptionStimulusSource().setEnabled(false);
			this.getTxtDescriptionStimulus().setEnabled(false);
			this.getTxtDescriptionEnvironment().setEnabled(false);
			this.getTxtDescriptionArtifact().setEnabled(false);
			this.getTxtDescriptionResponse().setEnabled(false);
			this.getTxtDescriptionResponseMeasure().setEnabled(false);

			this.getCmbTypeStimulusSource().getCombo().setEnabled(false);
			this.getCmbTypeStimulus().getCombo().setEnabled(false);
			this.getCmbTypeEnvironment().getCombo().setEnabled(false);
			this.getCmbTypeArtifact().getCombo().setEnabled(false);
			this.getCmbTypeResponse().getCombo().setEnabled(false);
			this.getCmbTypeResponseMeasure().getCombo().setEnabled(false);
			this.getCmbMetric().getCombo().setEnabled(false);
			this.getCmbUnit().getCombo().setEnabled(false);

			this.getTxtValueStimulusSource().setEnabled(false, gStimulusSource);
			this.getTxtValueStimulus().setEnabled(false, gStimulus);
			this.getTxtValueEnvironment().setEnabled(false, gEnvironment);
			this.getTxtValueResponse().setEnabled(false, gResponse);
			this.getTxtValueResponseMeasure().setEnabled(false, gResponseMeasure);

			this.getBtnRemove().setEnabled(false);
			this.getBtnSave().setEnabled(false);
			break;

		case 8:// No saved system with requirement
			this.clearScenario();
			this.getCmbSystem().getCombo().setEnabled(false);

			this.getTblViewerQualityRequirement().getTable().setEnabled(false);
			this.getTxtDescription().setEnabled(false);
			this.getCmbQualityAttribute().getCombo().setEnabled(false);

			this.getTxtDescriptionStimulusSource().setEnabled(false);
			this.getTxtDescriptionStimulus().setEnabled(false);
			this.getTxtDescriptionEnvironment().setEnabled(false);
			this.getTxtDescriptionArtifact().setEnabled(false);
			this.getTxtDescriptionResponse().setEnabled(false);
			this.getTxtDescriptionResponseMeasure().setEnabled(false);

			this.getCmbTypeStimulusSource().getCombo().setEnabled(false);
			this.getCmbTypeStimulus().getCombo().setEnabled(false);
			this.getCmbTypeEnvironment().getCombo().setEnabled(false);
			this.getCmbTypeArtifact().getCombo().setEnabled(false);
			this.getCmbTypeResponse().getCombo().setEnabled(false);
			this.getCmbTypeResponseMeasure().getCombo().setEnabled(false);
			this.getCmbMetric().getCombo().setEnabled(false);
			this.getCmbUnit().getCombo().setEnabled(false);

			this.getTxtValueStimulusSource().setEnabled(false, gStimulusSource);
			this.getTxtValueStimulus().setEnabled(false, gStimulus);
			this.getTxtValueEnvironment().setEnabled(false, gEnvironment);
			this.getTxtValueResponse().setEnabled(false, gResponse);
			this.getTxtValueResponseMeasure().setEnabled(false, gResponseMeasure);

			this.getBtnSave().setEnabled(false);
			this.getBtnRemove().setEnabled(false);

			break;

		}
	}

	/**
	 * Sets the view whit quality scenario of the quality requirement selected
	 */
	public void setView() {
		this.getViewController().getView();
	}

	/**
	 * Load all quality attributes in the combo
	 */
	public void loadCmbQualityAttribute() {
		this.getViewController().setModelQualityAttribute();

	}

}
