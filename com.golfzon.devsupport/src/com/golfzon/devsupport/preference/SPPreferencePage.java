package com.golfzon.devsupport.preference;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class SPPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	
	public SPPreferencePage() {
		super(GRID);
	}

	@Override
	public void init(IWorkbench workbench) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void createFieldEditors() {
		// TODO Auto-generated method stub
		addField(new RadioGroupFieldEditor("DbSelection", "DB Selection", 3, 
										  new String[][] {{"DEV", "dev"}, {"QA", "qa"}}, getFieldEditorParent(),true));
		
		 addField(new FileFieldEditor("settingFile", "select db setting file", getFieldEditorParent()));		
	}

}
