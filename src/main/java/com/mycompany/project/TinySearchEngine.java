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
        SortSetting ss = new SortSetting();
        SortedList<Result> list = new SortedList();
        for (String word : words) {
            if (word.compareToIgnoreCase("sortby") == 0)
                sorting = true;
            else if (sorting) {
                ss.updateSetting(word);
            } else
                list = list.union(find(word));
        }
        return sortResults(list, ss);
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
    
    private List<Document> sortResults(SortedList<Result> results, SortSetting setting) {
        LinkedList<Document> list = new LinkedList();
        List<Result> sortedResults = null;
        switch(setting.sortby) {
            case SORTBY_DOCUMENT:
                if (setting.ascendingOrder)
                    sortedResults = results.bubbleSort((i0, i1) ->
                           ((Result)i1).document.compareTo(((Result)i0).document)
                    );
                else
                    sortedResults = results.bubbleSort((i0, i1) ->
                            ((Result)i0).document.compareTo(((Result)i1).document)
                    );
                break;
                
            case SORTBY_COUNT:
                if (setting.ascendingOrder)
                    sortedResults = results.bubbleSort((i0, i1) ->
                            ((Result)i0).attr.count - ((Result)i1).attr.count
                    );
                else
                    sortedResults = results.bubbleSort((i0, i1) ->
                            ((Result)i1).attr.count - ((Result)i0).attr.count
                    );
                break;
                
            case SORTBY_POPULARITY:
                if (setting.ascendingOrder)
                    sortedResults = results.bubbleSort((i0, i1) ->
                            ((Result)i0).document.popularity - ((Result)i1).document.popularity
                    );
                else
                    sortedResults = results.bubbleSort((i0, i1) ->
                            ((Result)i1).document.popularity - ((Result)i0).document.popularity
                    );
                break;
                
            case SORTBY_OCCURANCE:
                if (setting.ascendingOrder)
                    sortedResults = results.bubbleSort((i0, i1) ->
                            ((Result)i0).attr.occurance - ((Result)i1).attr.occurance
                    );
                else
                    sortedResults = results.bubbleSort((i0, i1) ->
                            ((Result)i1).attr.occurance - ((Result)i0).attr.occurance
                    );
                break;
        }
        sortedResults.forEach(doc -> list.add(doc.document));
        return list;
    }
    
    private enum SortBy {
        SORTBY_DOCUMENT,
        SORTBY_COUNT,
        SORTBY_POPULARITY,
        SORTBY_OCCURANCE
    }
    
    private class SortSetting {
        public SortBy sortby = SortBy.SORTBY_DOCUMENT;
        public boolean ascendingOrder = false;
        
        public SortSetting updateSetting(String word) {
            if (word.compareToIgnoreCase("document") == 0 || word.compareToIgnoreCase("doc") == 0)
                sortby = SortBy.SORTBY_DOCUMENT;
            else if (word.compareToIgnoreCase("count") == 0)
                sortby = SortBy.SORTBY_COUNT;
            else if (word.compareToIgnoreCase("popularity") == 0 || word.compareToIgnoreCase("pop") == 0)
                sortby = SortBy.SORTBY_POPULARITY;
            else if (word.compareToIgnoreCase("occurance") == 0 || word.compareToIgnoreCase("occ") == 0)
                sortby = SortBy.SORTBY_OCCURANCE;
            else if (word.compareToIgnoreCase("asc") == 0 || word.compareToIgnoreCase("down") == 0)
                ascendingOrder = true;
            else if (word.compareToIgnoreCase("decs") == 0 || word.compareToIgnoreCase("up") == 0)
                ascendingOrder = false;
            return this;
        }
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
