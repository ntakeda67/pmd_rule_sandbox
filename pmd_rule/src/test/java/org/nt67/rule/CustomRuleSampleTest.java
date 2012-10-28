package org.nt67.rule;

import org.junit.Test;
import net.sourceforge.pmd.Rule;

public class CustomRuleSampleTest extends SampleBasedTest {
    
    @Override
    protected Rule createRule() {
	return new CustomTestRule();
    }

    @Test
    public void testVerifyNgRules() {
        assertViolation("NG");
    }
    
    @Test
    public void testVerifyOkRules() {
    	assertPass("OK");
    }

}