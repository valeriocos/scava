package org.eclipse.scava.nlp.preprocessor.examples;

import java.util.List;

import org.eclipse.scava.nlp.preprocessor.sentencedetector.SentenceDetector;

class SentenceDetectionExample
{
	public static void main(String[] args)
	{
		String text = new String("Reasons for sticking with Java 5:     * only override annotations and HotSpotDiagnosticMXBean require JDK 6" + 
				"    * Mac OS X still has no Java 6    * Europe and Ganymede Eclipse platform should run on Java 5");
		
		String[] preSentences = text.split("\\h{2,}");
		
		List<String> sentences = SentenceDetector.detect(text);
		
		System.out.println("First");
		
		sentences=SentenceDetector.detect(preSentences);
		
		System.out.println("Second");
		
		text="Build ID: M20071023-1652Steps To Reproduce:The following EOL is inconsistent. The first two statements return true, and the last two return false."
				+ "-- These return true'foo'.isTypeOf(String).println();'foo'.isKindOf(String).println();-- These return false!String.isType('foo').println();"
				+ "String.isKind('foo').println();More information:";
		
		sentences = SentenceDetector.detect(text);
		
		System.out.println("End");
	}
}
