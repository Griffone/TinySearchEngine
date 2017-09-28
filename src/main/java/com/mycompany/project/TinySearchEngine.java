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
        String[] words = string.split(" ");
        boolean sorting = false;
        SortedList<Result> list = new SortedList();
        for (String word : words) {
            if (word.compareToIgnoreCase("sortby") == 0)
                sorting = true;
            else if (sorting) {
                // TODO: sort results
            } else
                list = list.union(find(word));
        }
        List<Document> results = new LinkedList();
        list.forEach(doc -> results.add(doc.document));
        return results;
    }
    
    private final SortedList<DocumentWrap> docs;
    private DocumentWrap lastDocument;
    
    private SortedList<Result> find(String word) {
        SortedList<Result> list = new SortedList();
        docs.forEach(doc -> {
            WordWrap ww = doc.words.find(new WordWrap(word));
            if (ww != null)
                ww.attributes.forEach(attr -> {
                    list.insert(new Result(doc.document, attr));
                });
        });
        return list;
    }
    
    private class Result implements Comparable<Result> {

        public final Document document;
        public final AttributeWrap attr;
        
        Result(Document document, AttributeWrap attr) {
            this.document = document;
            this.attr = attr;
        }
        
        @Override
        public int compareTo(Result o) {
            if (document.compareTo(o.document) == 0)
                return attr.compareTo(o.attr);
            else
                return document.compareTo(o.document);
        }
        
    }
    
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
