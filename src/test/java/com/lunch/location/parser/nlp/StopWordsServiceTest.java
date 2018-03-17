package com.lunch.location.parser.nlp;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import com.google.common.collect.Sets;
import com.lunch.location.services.parser.nlp.StopWordsService;

@RunWith(Parameterized.class)
public class StopWordsServiceTest {
	
	private StopWordsService stopwordsService;
	
    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {     
                 { "und", true },
                 { "ja", true },
                 { "in", true },
                 { "bla", false },
                 { "", false }  
           });
    }
    
    @Parameter(0)
    public String input;

    @Parameter(1)
    public boolean expected;
    
	
	@Before
	public void init() {
		Set<String> words = Sets.newHashSet("und", "ja", "in");
		stopwordsService = new StopWordsService(words);
	}
	
	@Test
	public void shouldCalcNormalizedLevenshteinForSingleWordFullMatch() {
		//when
		boolean result = stopwordsService.isStopWord(input);
		
		//then
		Assert.assertEquals(expected, result);
	}
}
