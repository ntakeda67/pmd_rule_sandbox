package org.nt67.rule;

public class CustomRuleSample {
    void sample() {
	try {
	    throw new RuntimeException();
	} catch (RuntimeException e) {
	}
 
   }
}