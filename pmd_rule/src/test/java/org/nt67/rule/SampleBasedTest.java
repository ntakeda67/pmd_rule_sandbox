package org.nt67.rule;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.*;

import net.sourceforge.pmd.PMD;
import net.sourceforge.pmd.PMDException;
import net.sourceforge.pmd.Rule;
import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.RuleSet;
import net.sourceforge.pmd.RuleSets;
import net.sourceforge.pmd.SourceType;

/**
 * サンプルコードを基にしたテストケースを実装するためのスケルトン。
 * サンプルコードに実装したルールを適用し、期待通りの結果が返ってくるか確かめる。
 *
 * @author toda_k
 */
abstract class SampleBasedTest {
	/**
	 * サンプルコードを配置するディレクトリ
	 */
	private static final File TEST_SAMPLE_ROOT_DIR = new File("src/test/java/org/nt67/rule/sample");

	/**
	 * @return 自作ルールのインスタンス（not null）
	 */
	protected abstract Rule createRule();

	private final Logger logger = Logger.getLogger(getClass().getName());

	protected final void assertPass(String sampleFileName) {
		assertResult(sampleFileName, true);
	}

	protected final void assertViolation(String sampleFileName) {
		assertResult(sampleFileName, false);
	}

	private final void assertResult(String sampleFileName, boolean expected) {
		File sampleFile = new File(TEST_SAMPLE_ROOT_DIR, sampleFileName.concat(".java"));
		try {
			assertEquals(sampleFile.getName(), expected, test(sampleFile));
		} catch (FileNotFoundException e) {
			logger.log(Level.SEVERE, "sample code not found", e);
			throw new RuntimeException(e);
		}
	}

	private boolean test(File file) throws FileNotFoundException {
		RuleSet ruleset = new RuleSet();
		Rule rule = createRule();

		if (rule.getMessage() == null) {
			// AbstractJavaRule#addViolation()でNullPointerExceptionが出るのを回避する
			rule.setMessage("");
		}

		ruleset.addRule(rule);
		RuleContext ctx = new RuleContext();
		ctx.setSourceCodeFilename(file.getName());
		ctx.setSourceType(SourceType.JAVA_16);
		RuleSets ruleSets = new RuleSets();
		ruleSets.addRuleSet(ruleset);

		Reader reader = null;
		try {
			reader = new InputStreamReader(new BufferedInputStream(new FileInputStream(file)), "UTF-8");
			new PMD().processFile(reader, ruleSets, ctx, SourceType.JAVA_16);
			return ctx.getReport().getViolationTree().size() == 0;
		} catch (PMDException e) {
			logger.log(Level.SEVERE, "PMDException caused", e);
			throw new RuntimeException(e);
		} catch (UnsupportedEncodingException e) {
			logger.log(Level.SEVERE, "UnsupportedEncodingException caused", e);
			throw new RuntimeException(e);
		} finally {
			try {
			    if (reader != null){
				reader.close();  // PMD 4.2.5では受け取ったReaderを閉じてくれているが、念のため自分でも閉じる
			    }
			} catch (IOException ignore) {
			}
		}
	}
}