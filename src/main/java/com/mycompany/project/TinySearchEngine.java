/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.project;

import se.kth.id1020.*;
import se.kth.id1020.util.*;
import java.util.*;

/**
 *
 * @author Griffone
 */
public class TinySearchEngine implements TinySearchEngineBase {

    public static void main(String[] args) throws Exception {
        TinySearchEngineBase engine = new TinySearchEngine();
        Driver.run(engine);
    }

    TinySearchEngine() {
        words = new ArrayList();
    }
    
    @Override
    public void insert(Word word, Attributes atrbts) {
        int index = binarySearch(word.word, 0, words.size());
        if (index == words.size()) {
            words.add(new WordWrapper(word, atrbts));
            return;
        }
        WordWrapper wrap = words.get(index);
        if (wrap.string.compareToIgnoreCase(word.word) == 0)
            wrap.attributes.add(new WordAttribute(word, atrbts));
        else
            words.add(index, new WordWrapper(word, atrbts));
    }

    @Override
    public List<Document> search(String string) {
        List<Document> list = new LinkedList<>();
        WordWrapper wrap = words.get(binarySearch(string, 0, words.size()));
        if (wrap.string.compareToIgnoreCase(string) == 0)
            wrap.attributes.forEach((attribute) -> {
                if (!list.contains(attribute.document))
                    list.add(attribute.document);
            });
        return list;
    }
    
    private final ArrayList<WordWrapper> words;
    
    private class WordAttribute {
        public final PartOfSpeech pos;
        public final Document document;
        public final int occurance;
        
        public WordAttribute(Word word, Attributes attr) {
            pos = word.pos;
            document = attr.document;
            occurance = attr.occurrence;
        }
    }
    
    private class WordWrapper {
        public final String string;
        public final List<WordAttribute> attributes;
        
        public WordWrapper(Word word, Attributes attr) {
            string = word.word;
            attributes = new LinkedList<>();
            attributes.add(new WordAttribute(word, attr));
        }
    }
    
    private int binarySearch(String key, int lo, int hi) {
        if (hi <= lo)
            return hi;
        
        int mid = lo + (hi - lo) / 2;
        int cmp = key.compareToIgnoreCase(words.get(mid).string);
        if (cmp < 0)
            return binarySearch(key, lo, mid);
        else if (cmp > 0)
            return binarySearch(key, mid + 1, hi);
        else
            return mid;
    }
}
