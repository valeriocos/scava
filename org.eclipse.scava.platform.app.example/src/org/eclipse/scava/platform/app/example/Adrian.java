package org.eclipse.scava.platform.app.example;

import org.eclipse.scava.platform.Platform;
import org.eclipse.scava.repository.model.Project;
import org.eclipse.scava.platform.app.example.util.ProjectCreationUtil;

import com.mongodb.Mongo;

public class Adrian {
	
	public static void main(String[] args) throws Exception {
		//Connection to the database
		Mongo mongo = new Mongo();
		Platform platform = new Platform(mongo);
		//Call to class ProjectCreationUtil
		
		//Project pdb = ProjectCreationUtil.createProjectWithNewsGroup("Log4J", "news.gmane.org", "gmane.comp.jakarta.log4j.user", false, null, null, 119, 10000);
        
        //Project pdb = ProjectCreationUtil.createProjectWithNewsGroup("mozilladevwebapps", "news.mozliia.org", "mozilla.dev.webapps", false, null, null, 119, 10000);
        
        //bugzilla
        //Project pdb = ProjectCreationUtil.createProjectWithBugTrackingSystem("bugzilla", "https://bugzilla.redhat.com/xmlrpc.cgi", "Bugzilla", null);
        //Project pdb = ProjectCreationUtil.createProjectWithBugTrackingSystem("epsilon", "https://bugs.eclipse.org/bugs/xmlrpc.cgi", "epsilon", null);
        
        //Redmine 
        //Project pdb = ProjectCreationUtil.createProjectWithBugTrackingSystemRedmine("utp","http://forge.modelio.org");

        
        //Jira
        //Project pdb = ProjectCreationUtil.createProjectWithBugTrackingSystemJira("Alambic", "korkonty@edgehill.ac.uk", "CrossMiner", "https://castalia.atlassian.net/");
        
        //github
        Project pdb = ProjectCreationUtil.createProjectWithBugTrackingSystemGitHub("Adrián Cabrera", "33efa61d8313b8447ede48444c2a73166a06dbf6", "scavaTest", "crossminer", "scava");

		
		pdb.getExecutionInformation().setMonitor(true);			//??
		//Add to the database
		platform.getProjectRepositoryManager().getProjectRepository().getProjects().add(pdb);
		//Database synchronisation
		platform.getProjectRepositoryManager().getProjectRepository().sync();
		mongo.close();
	}
}
