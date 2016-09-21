package com.golfzon.devsupport.util;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;

public class ASTUtil {
	
	public static List<ASTNode> getArguments(ASTNode node) {
	    List result = null;
	    if ((node instanceof ClassInstanceCreation))
	      result = ((ClassInstanceCreation)node).arguments();
	    else if ((node instanceof ConstructorInvocation))
	      result = ((ConstructorInvocation)node).arguments();
	    else if ((node instanceof MethodInvocation))
	      result = ((MethodInvocation)node).arguments();
	    else if ((node instanceof SuperConstructorInvocation))
	      result = ((SuperConstructorInvocation)node).arguments();
	    else if ((node instanceof SuperMethodInvocation)) {
	      result = ((SuperMethodInvocation)node).arguments();
	    }
	    return result;
	}
	
	public static IMethodBinding resolveBinding(ASTNode node) {
	    IMethodBinding binding = null;
	    if ((node instanceof ClassInstanceCreation))
	      binding = ((ClassInstanceCreation)node).resolveConstructorBinding();
	    else if ((node instanceof ConstructorInvocation))
	      binding = ((ConstructorInvocation)node).resolveConstructorBinding();
	    else if ((node instanceof MethodInvocation))
	      binding = ((MethodInvocation)node).resolveMethodBinding();
	    else if ((node instanceof SuperConstructorInvocation))
	      binding = ((SuperConstructorInvocation)node).resolveConstructorBinding();
	    else if ((node instanceof SuperMethodInvocation)) {
	      binding = ((SuperMethodInvocation)node).resolveMethodBinding();
	    }
	    return binding;
	}
	
	public static String extractString(ASTNode node) {
	    String result = null;

	    if ((node instanceof StringLiteral)) {
	      result = ((StringLiteral)node).getLiteralValue();
	    }
	    else if ((node instanceof Name)) {
	      Name name = (Name)node;
	      Object value = name.resolveConstantExpressionValue();
	      if ((value != null) && ((value instanceof String))) {
	        result = value.toString();
	      }

	    }
	    else if ((node instanceof FieldAccess)) {
	      FieldAccess accesser = (FieldAccess)node;
	      Object constantValue = accesser.resolveFieldBinding().getConstantValue();
	      if ((constantValue != null) && ((constantValue instanceof String))) {
	        result = constantValue.toString();
	      }

	    } else {
	    	System.out.println("extract string fail on node : " + node);
	    }
	    return result;
	}
	
	public static boolean isStringLiteral(int index, List<ASTNode> params) {
	    return (params != null) && (params.size() > index) && ((params.get(index) instanceof StringLiteral));
	}

}
