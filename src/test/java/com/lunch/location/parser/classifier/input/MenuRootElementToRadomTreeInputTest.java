package com.lunch.location.parser.classifier.input;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;
import com.lunch.location.services.parser.MenuPosTagger;
import com.lunch.location.services.parser.classifier.input.MenuRootElementToRandomTreeInput;

import edu.stanford.nlp.ling.TaggedWord;
import weka.core.Instance;

public class MenuRootElementToRadomTreeInputTest {
	
	private MenuRootElementToRandomTreeInput toInput;
	
	@Mock
	private MenuPosTagger tagger;
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		toInput = new MenuRootElementToRandomTreeInput(tagger);
	}
	
	@Test
	public void itShoudConvertSentenceToInstance() {
		//given
		String theSentence = "the sentence";
		TaggedWord w1 = new TaggedWord("w1", "NE"); // gezaehlt als NE
		TaggedWord w2 = new TaggedWord("w2", "VMFIN"); // gezaehlt als Gruppe V
		TaggedWord w3 = new TaggedWord("w2", "VMFIN"); // gezaehlt als Gruppe V
		TaggedWord w4 = new TaggedWord("w3", "VVINF"); // gezaehlt als Gruppe V
		TaggedWord w5 = new TaggedWord("w4", "NE"); // gezaehlt als NE
		TaggedWord w6 = new TaggedWord("w5", "ART"); // gezaehlt als ART
		TaggedWord w7 = new TaggedWord("w6", "NN"); // gezaehlt als NN
		TaggedWord w8 = new TaggedWord("w7", "PIS"); // dieser Tag wird nicht beachtet
		
		List<TaggedWord> sentence = Lists.newArrayList(w1, w2, w3, w4, w5, w6, w7, w8);
		List<List<TaggedWord>> sentences = new ArrayList<>();
		sentences.add(sentence);
		when(tagger.posTag(theSentence)).thenReturn(sentences);
		
		//when
		Instance result = toInput.convert(theSentence);
		
		//then
		Assert.assertEquals(result.attribute(0).name(), "words");
		Assert.assertEquals(result.value(0), 8.0, 0.0001);
		
		Assert.assertEquals(result.attribute(1).name(), "sentences");
		Assert.assertEquals(result.value(1), 1.0, 0.0001);
		
		Assert.assertEquals(result.attribute(2).name(), "NN");
		Assert.assertEquals(result.value(2), 1.0 / 8.0, 0.0001);

		Assert.assertEquals(result.attribute(3).name(), "NE");
		Assert.assertEquals(result.value(3), 2.0 / 8.0, 0.0001);

		Assert.assertEquals(result.attribute(4).name(), "ART");
		Assert.assertEquals(result.value(4), 1.0 / 8.0, 0.0001);

		Assert.assertEquals(result.attribute(5).name(), "ADJA");
		Assert.assertEquals(result.value(5), 0.0, 0.0001);

		Assert.assertEquals(result.attribute(11).name(), "XY");
		Assert.assertEquals(result.value(11), 0.0, 0.0001);

		Assert.assertEquals(result.attribute(12).name(), "V");
		Assert.assertEquals(result.value(12), 3.0 / 8.0, 0.0001);
	}

}
