package com.golfzon.devsupport.link;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.hyperlink.AbstractHyperlinkDetector;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.jface.text.hyperlink.IHyperlinkDetector;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.ITextEditor;

import com.golfzon.devsupport.constant.CommonConst;
import com.golfzon.devsupport.db.SQLExecutor;
import com.golfzon.devsupport.util.StringUtil;

public class SPHyperlinkDetector extends AbstractHyperlinkDetector implements IHyperlinkDetector {
	

	@Override
	public IHyperlink[] detectHyperlinks(ITextViewer textViewer, IRegion region, boolean canShowMultipleHyperlinks) {
		// TODO Auto-generated method stub
		
		int offset = region.getOffset();
		ITextEditor editor = (ITextEditor) getAdapter(ITextEditor.class);
		IEditorInput editorInput = editor.getEditorInput();
		IJavaElement element = (IJavaElement) editorInput.getAdapter(IJavaElement.class);
		if ((!(element instanceof ICompilationUnit)) || (!(editorInput instanceof FileEditorInput))) {
			return null;
		}
		
		IProject project = ((FileEditorInput)editorInput).getFile().getProject();
			
		SPStringFinder linkFinder = new SPStringFinder(((FileEditorInput)editorInput).getFile());
		SPStringRegion source = linkFinder.getRegionAt(offset);
		if (source == null) return null;
		
		String leftText = SQLExecutor.getSPText(CommonConst.DEV_ENV, source.getDbNm(), source.getCatalNm(), source.getSpNm());
		String rightText = SQLExecutor.getSPText(CommonConst.QA_ENV, source.getDbNm(), source.getCatalNm(), source.getSpNm());
		
		if(StringUtil.isEmpty(leftText) && StringUtil.isEmpty(rightText)) return null;
				
		return new IHyperlink[] { new SPCompareHyperlink(source, project, leftText, rightText) };
		
	}

}
