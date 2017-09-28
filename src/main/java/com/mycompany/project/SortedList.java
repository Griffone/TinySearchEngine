/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.project;

import java.util.ArrayList;
import java.util.function.Consumer;

/**
 *
 * @author thegr
 * @param <T>
 */
public class SortedList<T extends Comparable<T>> {
    
    private final ArrayList<T> list;
    
    public SortedList() {
        list = new ArrayList();
    }
    
    public T insert(T object) {
        int index = binarySearch(object, 0, list.size());
        if (index == list.size()) {
            list.add(object);
            return object;
        }
        if (object.compareTo(list.get(index)) == 0)
            return list.get(index);
        list.add(index, object);
        return object;
    }
    
    private int binarySearch(T key, int lo, int hi) {
        if (hi <= lo)
            return hi;
        
        int mid = lo + (hi - lo) / 2;
        int cmp = key.compareTo(list.get(mid));
        if (cmp < 0)
            return binarySearch(key, lo, mid);
        else if (cmp > 0)
            return binarySearch(key, mid + 1, hi);
        else
            return mid;
    }
    
    public void forEach(Consumer<? super T> action) {
        list.forEach(action);
    }
    
    public T find(T key) {
        int index = binarySearch(key, 0, list.size());
        T obj = list.get(index);
        return (obj.compareTo(key) == 0) ? obj : null;
    }
}