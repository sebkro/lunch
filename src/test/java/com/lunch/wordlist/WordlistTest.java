package com.lunch.wordlist;

import de.ptr.nlp.*;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.core.Is.is;
//import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

public class WordlistTest {

    @Test
    public void testWords(){
        WordTree worte = new Wortliste().wortliste();
        worte.addWordJ("Hallo", "ITALIENISCH");
        worte.addWordJ("Hai", "FISCH", "ESSEN");

        assertThat(worte.queryWordJ("Hai").get(), containsInAnyOrder("FISCH", "ESSEN"));
    }

    @Test
    public void testText(){
        WordTree worte = new Wortliste().wortliste();
        worte.addWordJ("Hallo", "ITALIENISCH");
        worte.addWordJ("Heute", "TRINKEN");
        worte.addWordJ("Hai", "FISCH", "ESSEN");

        List<String> parsed = worte.parseTextJ("Hallo, heute essen wir Hai - ok?");
        System.out.println(parsed);
        assertThat(parsed,
                containsInAnyOrder("FISCH", "ESSEN", "ITALIENISCH", "TRINKEN"));
    }

    @Test
    public void testInitializedWords(){
        WordTree worte = new Wortliste().init().wortliste();
        String gericht = "Gebackene Thai Ente mit Pak Choi und Koriander in würziger Ducksauce.";
        List<String> parsed =worte.parseTextJ(gericht);
        assertThat(parsed,
                containsInAnyOrder("ESSEN", "ESSEN", "ESSEN", "FLEISCH", "VEGETARISCH", "VEGETARISCH", "GEMUESE", "GEMUESE"));
    }

    @Test
    public void testInitializedClassify(){
        WordTree worte = new Wortliste().init().wortliste();
        String gericht = "Gebackene Thai Ente mit Pak Choi und Koriander in würziger Ducksauce.";
        List<String> parsed = Tag.classifyJ(worte.parseTextJ(gericht));
        assertThat(parsed,
                contains("ESSEN", "GEMUESE", "FLEISCH"));
    }

}
