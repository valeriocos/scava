package org.eclipse.scava.nlp.preprocessor.sentencedetector;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.util.InvalidFormatException;

class SentenceDectorSingleton
{
	
	private static SentenceDectorSingleton singleton = new SentenceDectorSingleton();
	private static SentenceDetectorME sentenceDetector;
	
	private SentenceDectorSingleton()
	{
		InputStream binFile;
		try
		{
			binFile = getModelBin();
			SentenceModel model = new SentenceModel(binFile);
			sentenceDetector = new SentenceDetectorME(model);
		} catch (FileNotFoundException | ModelExceptions e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidFormatException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
	
	private void checkModelFile(Path path) throws ModelExceptions
	{
		if(!Files.exists(path))
        {
        	throw new ModelExceptions("The file "+path+" has not been found"); 
        }
	}
	
	private InputStream getModelBin() throws ModelExceptions, FileNotFoundException
	{
		String path = getClass().getProtectionDomain().getCodeSource().getLocation().getFile();
		if (path.endsWith("bin/"))
			path = path.substring(0, path.lastIndexOf("bin/"));
		File file= new File(path+"models/en-sent.bin");
		checkModelFile(file.toPath());
		return new FileInputStream(file);
	}
	
	public static SentenceDectorSingleton getInstance()
	{
		return singleton;
	}
	
	public SentenceDetectorME getSentenceDector()
	{
		return sentenceDetector;
	}
}
