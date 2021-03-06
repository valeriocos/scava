/*********************************************************************
* Copyright (c) 2017 FrontEndART Software Ltd.
*
* This program and the accompanying materials are made
* available under the terms of the Eclipse Public License 2.0
* which is available at https://www.eclipse.org/legal/epl-2.0/
*
* SPDX-License-Identifier: EPL-2.0
**********************************************************************/

package org.eclipse.scava.plugin.event.notifier;

import java.util.ArrayList;

import org.eclipse.scava.plugin.event.Event;

public class NotifierEvent extends Event<INotifierEventListener> implements INotifierEvent {
	
	@Override
	public void invoke() {
		ArrayList<INotifierEventListener> copyOfListeners = new ArrayList<>(getListeners());
		copyOfListeners.forEach(listener -> listener.handle());
	}
	
}
