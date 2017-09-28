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
        docs = new SortedList();
        lastDocument = null;
    }
    
    @Override
    public void insert(Word word, Attributes atrbts) {
        if (lastDocument == null || lastDocument.document.compareTo(atrbts.document) != 0)
            lastDocument = docs.insert(new DocumentWrap(atrbts.document));
        WordWrap ww = lastDocument.words.insert(new WordWrap(word.word));
        AttributeWrap aw = ww.attributes.insert(new AttributeWrap(word, atrbts));
        aw.count++;
    }

    @Override
    public List<Document> search(String string) {
        List<Document> list = new LinkedList();
        docs.forEach(doc -> {
            WordWrap word = doc.words.find(new WordWrap(string));
            if (word != null)
                list.add(doc.document);
        });
        return list;
    }
    
    private final SortedList<DocumentWrap> docs;
    private DocumentWrap lastDocument;
    
    private class DocumentWrap implements Comparable<DocumentWrap> {
        public final Document document;
        public final SortedList<WordWrap> words;
        
        public DocumentWrap(Document document) {
            this.document = document;
            words = new SortedList();
        }

        @Override
        public int compareTo(DocumentWrap o) {
            return document.compareTo(o.document);
        }
    }
    
    private class WordWrap implements Comparable<WordWrap> {
        public final String string;
        public final SortedList<AttributeWrap> attributes;
        
        public WordWrap(String string) {
            this.string = string;
            attributes = new SortedList();
        }

        @Override
        public int compareTo(WordWrap o) {
            return string.compareToIgnoreCase(o.string);
        }
    }
    
    private class AttributeWrap implements Comparable<AttributeWrap> {
        public final PartOfSpeech pos;
        public final int occurance;
        public int count = 0;
        
        public AttributeWrap(Word word, Attributes attr) {
            pos = word.pos;
            occurance = attr.occurrence;
        }

        @Override
        public int compareTo(AttributeWrap o) {
            return pos.compareTo(o.pos);
        }
    }
}
