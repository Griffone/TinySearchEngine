/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.project;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
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
    
    public SortedList<T> union(SortedList<T> other) {
        if (list.isEmpty())
            return other;
        else if (other.list.isEmpty())
            return this;
        SortedList<T> newList = new SortedList();
        Iterator<T> it = list.iterator();
        ListIterator<T> lt = other.list.listIterator();
        while (it.hasNext()) {
            T item = it.next();
            int compare = -1;
            while (lt.hasNext() && compare < 0) {
                T o = lt.next();
                compare = o.compareTo(item);
                if (compare < 0)
                    newList.list.add(o);
                else if (compare > 0)
                    lt.previous();
            }
            newList.list.add(item);
        }
        while (lt.hasNext()) newList.list.add(lt.next());
        return newList;
    }
    
    public List<T> toList() {
        return list;
    }
}