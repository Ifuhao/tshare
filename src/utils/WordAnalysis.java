package utils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;

public class WordAnalysis {
	// 设置识别单词的词性
	@SuppressWarnings({ "unused", "serial" })
	private static Set<String> expectedNature = new HashSet<String>() {{
        add("n");add("v");add("vd");add("vn");add("vf");
        add("vx");add("vi");add("vl");add("vg");
        add("nt");add("nz");add("nw");add("nl");
        add("ng");add("userDefine");add("wh");
    }};
    
	/**
	 * 将一句话进行分词
	 * @param sentence
	 * @return
	 */
	public static String[] split(String sentence) {
		Result res = ToAnalysis.parse(sentence);
		List<Term> terms = res.getTerms();
		String words[] = new String[terms.size()];
		for(int i=0;i<words.length;i++) {
			words[i] = terms.get(i).getName();
		}
		return words;
	}
}
