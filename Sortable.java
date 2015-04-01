/*
 * Author:          Josh Kerbaugh
 * Creation Date:   2/17/2015
 * Due Date:        2/26/2015
 * Assignment:      Assignment 1
 * Filename:        Sortable.java
 * Purpose:         This class is used to check whether a Player has gotten
 * racko in their rack.
 */
import java.lang.Comparable;import java.lang.Iterable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class is used to check whether a Player has gotten racko in their rack.
 * @author Josh Kerbaugh
 * @version 1.0
 * @since 2015-26-2
 */
public class Sortable {
    /**
     * A template function to check whether an iterable list is in
     * consecutive order.
     * @param theRack The Iterable Object itself
     * @return isSorted
     */
    public static boolean isSorted(ArrayList<Card> theRack) {
        boolean sorted = true;

        for (int i = 1, len = theRack.size(); i < len; i++) {
            if (theRack.get(i-1).cardValue.compareTo(theRack.get(i).cardValue) > 0)
                sorted = false;
        }

        return sorted;
    }
}
