package org.eclipse.scava.crossflow;
/*******************************************************************************
 * Copyright (c) 2008 The University of York.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * Contributors:
 *     Dimitrios Kolovos - initial API and implementation
 *     Konstantinos Barmpis - adaption for CROSSFLOW
 *     Jonathan Co - adaption for command line execution
 ******************************************************************************/

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.epsilon.common.parse.problem.ParseProblem;
import org.eclipse.epsilon.common.util.StringProperties;
import org.eclipse.epsilon.egl.EglFileGeneratingTemplateFactory;
import org.eclipse.epsilon.egl.EgxModule;
import org.eclipse.epsilon.emc.emf.EmfModel;
import org.eclipse.epsilon.eol.IEolModule;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.exceptions.models.EolModelLoadingException;
import org.eclipse.epsilon.eol.execute.context.Variable;
import org.eclipse.epsilon.eol.models.IRelativePathResolver;
import org.eclipse.epsilon.flexmi.FlexmiResourceFactory;

public class GenerateBaseClasses {

	protected IEolModule module;
	protected List<Variable> parameters = new ArrayList<Variable>();

	protected Object result;

	String projectLocation;
	String modelRelativePath;
	String packageName;

	public void run(String projectLocation, String modelRelativePath) throws Exception {

		this.projectLocation = projectLocation;
		this.modelRelativePath = modelRelativePath;

		execute();
	}

	public void execute() throws Exception {

		EmfModel model = getModel();
		Resource r = model.getResource();
		HashSet<String> languages = findLanguages(r);
		// add java regardless of whether it exists in the model
		languages.add("java");
		//
		for (String language : languages) {

			module = createModule();
			module.getContext().getModelRepository().addModel(model);
			module.parse(getFileURI(language + "/crossflow.egx"));
			
			if (module.getParseProblems().size() > 0) {
				System.err.println("Parse errors occured...");
				for (ParseProblem problem : module.getParseProblems()) {
					System.err.println(problem.toString());
				}
				return;
			}

			for (Variable parameter : parameters) {
				module.getContext().getFrameStack().put(parameter);
			}

			result = execute(module);

			//module.getContext().getModelRepository().dispose();

		}
		
		model.dispose();

	}

	private HashSet<String> findLanguages(Resource r) {

		EClass language = (EClass) r.getContents().get(0).eClass().getEPackage().getEClassifier("Language");
		EAttribute languageName = language.getEAllAttributes().stream().filter(a -> a.getName().equals("name"))
				.findFirst().get();

		HashSet<String> ret = new HashSet<>();

		for (Iterator<EObject> it = r.getAllContents(); it.hasNext();) {
			EObject o;
			if ((o = it.next()).eClass().getName().equals(language.getName())) {
				ret.add(((String) o.eGet(languageName)).toLowerCase());
			}
		}

		return ret;

	}

	protected Object execute(IEolModule module) throws EolRuntimeException {
		return module.execute();
	}

	protected URI getFileURI(String fileName) throws URISyntaxException {

		URI binUri = GenerateBaseClasses.class.getResource(fileName).toURI();
		URI uri = null;

		if (binUri.toString().indexOf("bin") > -1) {
			uri = new URI(binUri.toString().replaceAll("bin", "src"));
		} else {
			uri = binUri;
		}

		return uri;
	}

	public IEolModule createModule() {
		try {
			EglFileGeneratingTemplateFactory templateFactory = new EglFileGeneratingTemplateFactory();
			templateFactory.setOutputRoot(new File(projectLocation).getAbsolutePath());
			return new EgxModule(templateFactory);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public EmfModel getModel() throws Exception {
		EmfModel model = createAndLoadAnEmfModel("org.eclipse.scava.crossflow", modelRelativePath, "Model", true, false,
				false);

		return model;
	}

	private EmfModel createAndLoadAnEmfModel(String metamodelURI, String modelFile, String modelName,
			boolean readOnLoad, boolean storeOnDisposal, boolean isCached) throws EolModelLoadingException {
		final EmfModel theModel = new EmfModel() {
			@Override
			protected ResourceSet createResourceSet() {
				final ResourceSet resourceSet = super.createResourceSet();
				resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("flexmi",
						new FlexmiResourceFactory());
				return resourceSet;
			}
		};
		StringProperties properties = new StringProperties();
		properties.put(EmfModel.PROPERTY_METAMODEL_URI, metamodelURI);
		properties.put(EmfModel.PROPERTY_MODEL_FILE, modelFile);
		properties.put(EmfModel.PROPERTY_NAME, modelName);
		properties.put(EmfModel.PROPERTY_READONLOAD, readOnLoad + "");
		properties.put(EmfModel.PROPERTY_STOREONDISPOSAL, storeOnDisposal + "");
		properties.put(EmfModel.PROPERTY_CACHED, isCached + "");
		theModel.load(properties, (IRelativePathResolver) null);
		return theModel;
	}

}
