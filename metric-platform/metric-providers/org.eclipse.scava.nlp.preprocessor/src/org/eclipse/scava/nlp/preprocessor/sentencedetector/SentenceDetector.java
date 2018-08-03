package org.eclipse.scava.nlp.preprocessor.sentencedetector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import opennlp.tools.sentdetect.SentenceDetectorME;

public class SentenceDetector
{
	private static SentenceDetectorME sentenceDetector;
	
	static
	{
		sentenceDetector=SentenceDectorSingleton.getInstance().getSentenceDector();
	}
	
	public static List<String> detect(String text)
	{
		List<String> sentences = Arrays.stream(sentenceDetector.sentDetect(text)).collect(Collectors.toList());
		return sentences;
	}
	
	public static List<String> detect(String[] preSentences)
	{
		List<String> sentences = new ArrayList<String>();
		for(String preSentence : preSentences)
		{
			sentences.addAll(detect(preSentence));
		}
		return sentences;
	}
}
