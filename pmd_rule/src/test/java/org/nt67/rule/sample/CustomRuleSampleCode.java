package org.nt67.rule.sample;

import java.util.logging.Logger;

public class CustomRuleSampleCode {
    Logger logger = Logger.getLogger(getClass().getName());

    void sample() {
	try {
	    throw new RuntimeException();
	} catch (RuntimeException e){
	    throw e;
	}
    }
}
