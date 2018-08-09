package org.eclipse.scava.nlp.requestreplydetector.example;

import org.eclipse.scava.nlp.predictionmanager.Prediction;
import org.eclipse.scava.nlp.requestreplydetector.RequestReplyDetector;

public class RunExample
{
	public static void main(String[] args)
	{
		String test = "I would like to know if the model is going to be loaded in memory or not? In other words, if the model is going to eat 4GB of RAM.";
		
		Prediction prediction = RequestReplyDetector.predict(test);
		
		System.out.println("Finish");
	
	}
}
