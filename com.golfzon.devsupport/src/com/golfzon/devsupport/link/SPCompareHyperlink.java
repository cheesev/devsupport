package com.golfzon.devsupport.link;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareEditorInput;
import org.eclipse.compare.CompareUI;
import org.eclipse.compare.structuremergeviewer.DiffNode;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.hyperlink.IHyperlink;

import com.golfzon.devsupport.item.CompareItem;

public class SPCompareHyperlink implements IHyperlink {
	
	private IRegion fUrlRegion;
	private IProject project;
	private String leftItemText;
	private String rightItemText;

	public SPCompareHyperlink(IRegion urlRegion, IProject project) {
		this.fUrlRegion = urlRegion;
		this.project = project;
	}
	
	public SPCompareHyperlink(IRegion urlRegion, IProject project, String leftItemText, String rightItemText) {
		this.fUrlRegion = urlRegion;
		this.project = project;
		this.leftItemText = leftItemText;
		this.rightItemText = rightItemText;
	}

	@Override
	public IRegion getHyperlinkRegion() {
		return fUrlRegion;
	}

	@Override
	public String getTypeLabel() {
		return null;
	}

	@Override
	public String getHyperlinkText() {
		return null;
	}

	@Override
	public void open() {
		
		CompareConfiguration configuration = new CompareConfiguration();
		configuration.setLeftLabel("DEV");
		configuration.setRightLabel("QA");
		CompareUI.openCompareDialog(new CompareEditorInput(configuration) {
		    @Override
		    protected Object prepareInput(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

		        CompareItem left = null;
		        CompareItem right = null;
				left = new CompareItem("Left", leftItemText);
				right = new CompareItem("Right",  rightItemText);
		        
		        DiffNode diffNode = new DiffNode(left, right);
		        return diffNode;
		    }
		});
	}

}
