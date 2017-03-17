package Presentation.preferenceConfiguration;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
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
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.hibernate.exception.JDBCConnectionException;

import Presentation.controllerConfiguration.DatabaseConfigurationPPController;
import Presentation.preferences.Messages;

/**
 * To system configuration
 * 
 * @author: María Eva Villarreal Guzmán. E-mail: villarrealguzman@gmail.com
 */

public class DatabaseConfigurationPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	/**
	 * Attributes
	 */
	private Button btnSave;
	private Button btnTestConnection;
	private StringFieldEditor databasePortNumber;
	private StringFieldEditor databaseUserName;
	private StringFieldEditor databaseName;
	private StringFieldEditor databasePassword;
	private DatabaseConfigurationPPController viewController;
	private Composite cDatabaseConfiguration;
	private GridData gridData;

	/**
	 * Constructor
	 */
	public DatabaseConfigurationPreferencePage() {
		super(GRID);
		try {
			noDefaultAndApplyButton();
			System.runFinalization();
			Runtime.getRuntime().gc();
			this.setViewController(DatabaseConfigurationPPController.getViewController());
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
		try {
			final Cursor cursorWait = parent.getDisplay().getSystemCursor(SWT.CURSOR_WAIT);
			final Cursor cursorNotWait = parent.getDisplay().getSystemCursor(SWT.CURSOR_ARROW);

			GridLayout layout = new GridLayout();
			layout.numColumns = 1;
			parent.setLayout(layout);

			// Group for project properties
			final Group groupDatabaseConfiguration = new Group(parent, SWT.SHADOW_ETCHED_IN);
			groupDatabaseConfiguration.setText(Messages.getString("UCM2DEVS_DatabaseConfiguration_Label"));
			groupDatabaseConfiguration.setLayout(layout);

			gridData = new GridData();
			gridData.horizontalAlignment = GridData.FILL;
			groupDatabaseConfiguration.setLayoutData(gridData);

			cDatabaseConfiguration = new Composite(groupDatabaseConfiguration, SWT.NONE);
			gridData = new GridData();
			gridData.grabExcessHorizontalSpace = true;
			gridData.horizontalAlignment = GridData.FILL;
			cDatabaseConfiguration.setLayoutData(gridData);

			databasePortNumber = new StringFieldEditor(Messages.getString("UCM2DEVS_PortNumber_Label"),
					Messages.getString("UCM2DEVS_PortNumber_Label") + ":", cDatabaseConfiguration);
			addField(databasePortNumber);
			databasePortNumber.getTextControl(cDatabaseConfiguration).addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					getBtnSave().setEnabled(false);
				}
			});

			databaseName = new StringFieldEditor(Messages.getString("UCM2DEVS_DatabaseName_Label"),
					Messages.getString("UCM2DEVS_DatabaseName_Label") + ":", cDatabaseConfiguration);
			addField(databaseName);
			databaseName.getTextControl(cDatabaseConfiguration).addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					getBtnSave().setEnabled(false);
				}
			});

			databaseUserName = new StringFieldEditor(Messages.getString("UCM2DEVS_UserName_Label"),
					Messages.getString("UCM2DEVS_UserName_Label") + ":", cDatabaseConfiguration);
			addField(databaseUserName);
			databaseUserName.getTextControl(cDatabaseConfiguration).addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					getBtnSave().setEnabled(false);
				}
			});

			databasePassword = new StringFieldEditor(Messages.getString("UCM2DEVS_Password_Label"),
					Messages.getString("UCM2DEVS_Password_Label") + ":", cDatabaseConfiguration) {
				@Override
				protected void doFillIntoGrid(Composite parent, int numColumns) {
					super.doFillIntoGrid(parent, numColumns);

					getTextControl().setEchoChar('*');
				}
			};
			addField(databasePassword);
			databasePassword.getTextControl(cDatabaseConfiguration).addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					getBtnSave().setEnabled(false);
				}
			});

			new Label(cDatabaseConfiguration, SWT.LEFT);

			gridData = new GridData();
			gridData.horizontalAlignment = GridData.END;
			gridData.verticalAlignment = SWT.BOTTOM;
			gridData.grabExcessHorizontalSpace = true;

			btnTestConnection = new Button(cDatabaseConfiguration, SWT.PUSH);
			btnTestConnection.setText(Messages.getString("UCM2DEVS_TestConnection_Buttom"));
			btnTestConnection.setLayoutData(gridData);
			btnTestConnection.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					parent.setCursor(cursorWait);
					viewController.testConnection();
					parent.setCursor(cursorNotWait);
				}
			});

			new Label(parent, SWT.LEFT);

			gridData = new GridData();
			gridData.horizontalSpan = 1;
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
					viewController.updateConnectionData();
					parent.setCursor(cursorNotWait);
				}
			});

			this.prepareView();
		} catch (

		JDBCConnectionException e) {
			viewController.createErrorDialog(Messages.getString("UCM2DEVS_Postgres_ErrorDialog"));
		}

		return new Composite(parent, SWT.NULL);

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
	public DatabaseConfigurationPPController getViewController() {
		return viewController;
	}

	public void setViewController(DatabaseConfigurationPPController viewController) {
		this.viewController = viewController;
	}

	public Button getBtnSave() {
		return btnSave;
	}

	public void setBtnSave(Button btnSave) {
		this.btnSave = btnSave;
	}

	public Composite getcDatabaseConfiguration() {
		return cDatabaseConfiguration;
	}

	public void setcDatabaseConfiguration(Composite cDatabaseConfiguration) {
		this.cDatabaseConfiguration = cDatabaseConfiguration;
	}

	public Button getBtnTestConnection() {
		return btnTestConnection;
	}

	public void setBtnTestConnection(Button btnTestConnection) {
		this.btnTestConnection = btnTestConnection;
	}

	public StringFieldEditor getDatabasePortNumber() {
		return databasePortNumber;
	}

	public void setDatabasePortNumber(StringFieldEditor databasePortNumber) {
		this.databasePortNumber = databasePortNumber;
	}

	public StringFieldEditor getDatabaseUserName() {
		return databaseUserName;
	}

	public void setDatabaseUserName(StringFieldEditor databaseUserName) {
		this.databaseUserName = databaseUserName;
	}

	public StringFieldEditor getDatabasePassword() {
		return databasePassword;
	}

	public void setDatabasePassword(StringFieldEditor databasePassword) {
		this.databasePassword = databasePassword;
	}

	public StringFieldEditor getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(StringFieldEditor databaseName) {
		this.databaseName = databaseName;
	}

	/**
	 * prepare the view
	 * 
	 */
	public void prepareView() {
		this.getViewController().getView();
		this.getBtnSave().setEnabled(false);
	}
}