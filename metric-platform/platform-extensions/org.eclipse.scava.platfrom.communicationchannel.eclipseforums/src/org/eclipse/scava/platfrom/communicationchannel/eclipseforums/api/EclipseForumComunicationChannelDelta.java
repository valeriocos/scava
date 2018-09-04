package org.eclipse.scava.platfrom.communicationchannel.eclipseforums.api;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scava.platform.delta.communicationchannel.CommunicationChannelDelta;

public class EclipseForumComunicationChannelDelta extends CommunicationChannelDelta {

	private static final long serialVersionUID = 1L;

	private List<EclipseForumsPost> eclipseForumPosts = new ArrayList<EclipseForumsPost>();

	public List<EclipseForumsPost> getEclipseForumPosts() {
		return eclipseForumPosts;
	}

}
