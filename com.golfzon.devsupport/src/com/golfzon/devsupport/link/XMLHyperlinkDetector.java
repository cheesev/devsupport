package com.golfzon.devsupport.link;

import java.util.StringTokenizer;

import javax.xml.xpath.XPathExpressionException;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.hyperlink.AbstractHyperlinkDetector;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.jface.text.hyperlink.IHyperlinkDetector;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.w3c.dom.Node;

import com.golfzon.devsupport.constant.CommonConst;
import com.golfzon.devsupport.db.SQLExecutor;
import com.golfzon.devsupport.util.ProjectUtil;
import com.golfzon.devsupport.util.StringUtil;
import com.golfzon.devsupport.util.XmlUtil;

public class XMLHyperlinkDetector extends AbstractHyperlinkDetector implements IHyperlinkDetector {
	
	private static final String REPOSITORY_PKG =  "repository";
	private static final String PROCEDURE_CALL_CMD = "CALL";
	
	@Override
	public IHyperlink[] detectHyperlinks(ITextViewer textViewer, IRegion region, boolean canShowMultipleHyperlinks) {
		// TODO Auto-generated method stub

		IDocument doc = textViewer.getDocument();
		
		int offset = region.getOffset();
		IRegion lineRegion = null;
		SPStringRegion spRegion = null;
		String candidate = null;
		Node currentNode = null;
		String dbNm = null;
		StringTokenizer tokenizer = null;
		String token = null;		
		
		String leftText = null;
		String rightText = null;
		SPStringRegion source = null;
		IProject project = null;
		
		try {
			project = ProjectUtil.getJavaProject(doc).getProject();
			
			lineRegion = doc.getLineInformationOfOffset(offset);
			candidate = doc.get(lineRegion.getOffset(), lineRegion.getLength());
			currentNode = getCurrentNode(doc, region.getOffset());		
			
			String namespace = XmlUtil.getNamespace(currentNode.getOwnerDocument());
			String[] namespaceArr = namespace.split("\\.");			
			int repoIdx = StringUtil.containIdx(namespaceArr, REPOSITORY_PKG);
			
			if(repoIdx < 0) return null;
			
			dbNm = namespaceArr[repoIdx + 1];
			
			tokenizer = new StringTokenizer(candidate, "\n ");
			while(tokenizer.hasMoreTokens()) {
				token = tokenizer.nextToken().trim();
				if(PROCEDURE_CALL_CMD.equals(token.toUpperCase())) {
					token = tokenizer.nextToken().trim();	// next call string, comes procedure name  
					offset = lineRegion.getOffset() + candidate.indexOf(token);
					spRegion = new SPStringRegion(offset, token.length(), token, dbNm);
					break;
				}
			}			
			if(spRegion == null) return null;
			
			SPStringFinder linkFinder = new SPStringFinder(spRegion);
			source = linkFinder.getRegionAt(offset);
			
			if (source == null) return null;
			
			leftText = SQLExecutor.getSPText(CommonConst.DEV_ENV, source.getDbNm(), source.getCatalNm(), source.getSpNm());
			rightText = SQLExecutor.getSPText(CommonConst.QA_ENV, source.getDbNm(), source.getCatalNm(), source.getSpNm());
			
			if(StringUtil.isEmpty(leftText) && StringUtil.isEmpty(rightText)) return null;
			
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new IHyperlink[] { new SPCompareHyperlink(source, project, leftText, rightText) };
		
	}
	
	private Node getCurrentNode(IDocument document, int offset)
	{
		// get the current node at the offset (returns either: element,
		// doctype, text)
		IndexedRegion inode = null;
		IStructuredModel sModel = null;
		try {
			sModel = StructuredModelManager.getModelManager().getExistingModelForRead(document);
			if (sModel != null) {
				inode = sModel.getIndexedRegion(offset);
				if (inode == null) {
					inode = sModel.getIndexedRegion(offset - 1);
				}
			}
		}
		finally
		{
			if (sModel != null)
				sModel.releaseFromRead();
		}

		if (inode instanceof Node)
		{
			return (Node)inode;
		}
		return null;
	}
	
	

}
