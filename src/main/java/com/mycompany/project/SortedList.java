/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.project;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.function.Consumer;

/**
 *  An always sorted list of comparable objects.
 *  One special syntax is the insert one, check the function definition for it
 * 
 * @author Griffone
 * @param <T extends Comparable<T>>
 */
public class SortedList<T extends Comparable<T>> {
    
    private final ArrayList<T> list;
    
    /**
     * Creates the SortedList
     */
    public SortedList() {
        list = new ArrayList();
    }
    
    /**
     * 
     * Tries to find the object in the list, inserting it in place if none were found
     * Because it can return an object that already exists one should use:
     * T t = insert(new T())
     * 
     * @param object the object to insert
     * @return The inserted object (if one already exists in the list, no insertion will actually happen, and you should use the returned object in that case)
     */
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
    
    /**
     * Perform provided action for every T in the list
     * 
     * @param action lambda expression to perform
     */
    public void forEach(Consumer<? super T> action) {
        list.forEach(action);
    }
    
    public SortedList<T> bubbleSort(Comparator<? super T> comp) {
        int r = list.size() - 1;
        boolean swapped = true;
        while (swapped && r >= 0) {
            swapped = false;
            for (int i = 0; i < r; i++)
                if (comp.compare(list.get(i), list.get(i + 1)) > 0) {
                    T x = list.get(i);
                    list.set(i, list.get(i + 1));
                    list.set(i + 1, x);
                    swapped = true;
                }
            r--;
        }
        return this;
    }
    
    /**
     * Tries to find key in the list
     * 
     * @param key a comparable key
     * @return Object if one similar to key is found or null otherwise
     */
    public T find(T key) {
        int index = binarySearch(key, 0, list.size());
        T obj = list.get(index);
        return (obj.compareTo(key) == 0) ? obj : null;
    }
    
    /**
     * Unite two sorted lists, where any objects that compareTo(obj) == 0 are only copied once
     * 
     * @param other the other list
     * @return new sorted list, that is a union of the two
     */
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
                else if (compare >= 0)
                    lt.previous();
            }
            newList.list.add(item);
        }
        while (lt.hasNext()) newList.list.add(lt.next());
        return newList;
    }
}