package com.golfzon.devsupport.item;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.eclipse.compare.IEncodedStreamContentAccessor;
import org.eclipse.compare.ITypedElement;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.graphics.Image;

public class CompareItem implements IEncodedStreamContentAccessor, ITypedElement {
	
	private final String fileNm;
	private final String contents;
	
	public CompareItem(String fileNm, String contents) {
		this.fileNm = fileNm;	
		this.contents = contents;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return fileNm;
	}

	@Override
	public Image getImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return ITypedElement.TEXT_TYPE;
	}

	@Override
	public InputStream getContents() throws CoreException {
		// TODO Auto-generated method stub
//		String encoding = ResourcesPlugin.getEncoding();
		try {
			return new ByteArrayInputStream(contents.getBytes(ResourcesPlugin.getEncoding()));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public String getCharset() throws CoreException {
		return ResourcesPlugin.getEncoding();
	}
}
