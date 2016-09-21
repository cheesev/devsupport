package com.golfzon.devsupport.link;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;

import com.golfzon.devsupport.util.ASTUtil;
import com.golfzon.devsupport.util.StringUtil;

public class SPStringFinder {
	
	private static final String[] SP_CLASSES = { "AutoDao", "GSqlParameter" } ;
	private static final String[] SP_METHOD = { "execute", "GSqlParameter" };
		
	private List<SPStringRegion> spStrings = new ArrayList();
	
	public SPStringFinder(IFile file) {
		
		ICompilationUnit unit = JavaCore.createCompilationUnitFrom(file);
	    if (unit == null) return;
		
		Visitor v = new Visitor();
		
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setKind(8);
		parser.setResolveBindings(true);
	    parser.setBindingsRecovery(true);
		parser.setSource(unit);
		
		ASTNode classUnit = parser.createAST(null);
		classUnit.accept(v);		
	}
	
	public SPStringFinder(SPStringRegion spRegion) {
		this.spStrings.add(spRegion);		
	}
	
	public String getStringAt(int offset) {
		SPStringRegion r = getRegionAt(offset);
		return r == null ? null : r.getSpString();
	}

	public SPStringRegion getRegionAt(int offset) {
		for (SPStringRegion r : this.spStrings) {
			if (r.isOn(offset)) {
				return r;
			}
		}
		return null;
	}
	
	public boolean isSPCall(IMethodBinding binding, List<ASTNode> params) {
	    String method = binding.getName();
	    String className = binding.getDeclaringClass().getName();
	    
	    boolean b = false;
	    if (!ASTUtil.isStringLiteral(0, params)) {
	      b = false;
	    }
	    else if ( (StringUtil.contains(SP_CLASSES, className)) && (StringUtil.contains(SP_METHOD, method)) ) {
	      b = true;
	    }
	    return b;
	}
	
	public String getSPString(String method, List<ASTNode> params) {
		return ASTUtil.extractString((ASTNode)params.get(0));
	}
	
	
	
	public class Visitor extends ASTVisitor {
	    public Visitor() {
	    }

	    public boolean visit(ClassInstanceCreation node) {
	      return findSPString(node);
	    }

	    public boolean visit(ConstructorInvocation node) {
	      return findSPString(node);
	    }

	    public boolean visit(MethodInvocation node) {
	      return findSPString(node);
	    }

	    public boolean visit(SuperConstructorInvocation node) {
	      return findSPString(node);
	    }

	    public boolean visit(SuperMethodInvocation node) {
	      return findSPString(node);
	    }

	    private boolean findSPString(ASTNode node)
	    {
	      IMethodBinding binding = ASTUtil.resolveBinding(node);

	      if (binding == null) return false;
	      if (binding.isDefaultConstructor()) return false;

	      String method = binding.getName();
	      List params = ASTUtil.getArguments(node);
	      	      
	      if (!SPStringFinder.this.isSPCall(binding, params)) return false;
	      
	      String dbNm = "web";
	      if(params.size() > 2) {
	    	  String param1 = ASTUtil.extractString((ASTNode)params.get(1));	
	    	  dbNm = param1;
	      }

	      String spString = ASTUtil.extractString((ASTNode)params.get(0));
	      if (spString == null) return false;

	      StringLiteral param0 = (StringLiteral)params.get(0);
	      SPStringFinder.this.spStrings.add(new SPStringRegion(param0.getStartPosition(), param0.getLength(), spString, dbNm));
//	      if (ASTUtil.isStringLiteral(1, params)) {
//	        StringLiteral param1 = (StringLiteral)params.get(1);
//	        QueryStringFinder.this.queryStrings.add(new SPStringRegion(param1.getStartPosition(), param1.getLength(), queryString));
//	      }
	      return true;
	    }
	  }
}
