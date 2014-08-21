/*******************************************************************************
 * Copyright (c) 2012 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.xpect.ui.launching;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.internal.junit.launcher.JUnitLaunchConfigurationConstants;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorPart;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
@SuppressWarnings("restriction")
public class JUnitLaunchShortcut extends org.eclipse.jdt.junit.launcher.JUnitLaunchShortcut {

	private JUnitJavaElementDelegate delegate;

	@Override
	protected ILaunchConfigurationWorkingCopy createLaunchConfiguration(IJavaElement element) throws CoreException {
		ILaunchConfigurationWorkingCopy wc = super.createLaunchConfiguration(element);
		if (delegate != null)
			wc.setAttribute(JUnitLaunchConfigurationConstants.ATTR_TEST_METHOD_NAME, delegate.getDescription().getDisplayName());
		return wc;
	}

	@Override
	public void launch(IEditorPart editor, String mode) {
		try {
			delegate = new JUnitJavaElementDelegate(editor);
			if (delegate.getJavaElement() != null)
				super.launch(new StructuredSelection(delegate), mode);
			else
				super.launch(editor, mode);
		} finally {
			delegate = null;
		}
	}

	@Override
	public void launch(ISelection selection, String mode) {
		if (selection instanceof IStructuredSelection) {
			try {
				IStructuredSelection newSelection = LaunchShortcutUtil.replaceWithJavaElementDelegates((IStructuredSelection) selection);
				Object element = newSelection.getFirstElement();
				if (element instanceof JUnitJavaElementDelegate)
					delegate = (JUnitJavaElementDelegate) element;
				super.launch(newSelection, mode);
			} finally {
				delegate = null;
			}
		}
	}
}
