/*********************************************************************
* Copyright (c) 2017 FrontEndART Software Ltd.
*
* This program and the accompanying materials are made
* available under the terms of the Eclipse Public License 2.0
* which is available at https://www.eclipse.org/legal/epl-2.0/
*
* SPDX-License-Identifier: EPL-2.0
*
* Contributors:
*    Zsolt J�nos Szamosv�lgyi
*    Endre Tam�s V�radi
*    Gerg� Balogh
**********************************************************************/
package org.eclipse.scava.plugin.usermonitoring.event.element;

import org.eclipse.scava.plugin.usermonitoring.event.EventManager;
import org.eclipse.scava.plugin.usermonitoring.event.IEventListener;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.texteditor.IElementStateListener;

public class ResourceElementEventListener implements IElementStateListener, IEventListener {

	IEditorInput input;

	public ResourceElementEventListener(IEditorInput input) {
		super();
		this.input = input;
	}

	@Override
	public void elementDirtyStateChanged(Object element, boolean isDirty) {
		if (!isDirty && input.equals(element)) {
			EventManager.processEvent(new ResourceElementEvent(input,ResourceElementStateType.SAVED));
		}
			
	}

	@Override
	public void elementDeleted(Object element) {
		if (input.equals(element)) {
		EventManager.processEvent(new ResourceElementEvent(input,ResourceElementStateType.DELETED));
		}
	}

	@Override
	public void elementContentAboutToBeReplaced(Object element) {
		
	}

	@Override
	public void elementContentReplaced(Object element) {
		
	}


	@Override
	public void elementMoved(Object originalElement, Object movedElement) {
		
	}

}
