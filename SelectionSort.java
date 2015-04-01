/*
 * Author:          Josh Kerbaugh
 * Creation Date:   2/17/2015
 * Due Date:        2/26/2015
 * Assignment:      Assignment 1
 * Filename:        Score.java
 * Purpose:         This class is used for the /s cheat it will sort to a
 * specific index specified by the user.
 */

import java.util.ArrayList;

/**
 * This class is used for the /s cheat it will sort to a specific index
 * specified by the user.
 * @author Josh Kerbaugh
 * @version 1.0
 * @since 2015-26-2
 */
public class SelectionSort {
    /**
     * This method sorts an array list to a specific index.
     * @param data The array list to sort
     * @param sortToIdx The index you wish to sort to
     */
    public static void doSelectionSort(ArrayList<Card> data, int sortToIdx) {
        if (data == null)
            return;
        if (data.size() == 0 || data.size() == 1)
            return;
        int smallestIndex, smallest;

        for (int curIndex = 0; curIndex < sortToIdx; curIndex++) {
            smallest = data.get(curIndex).cardValue;
            smallestIndex = curIndex;

            for (int i = curIndex + 1; i < data.size(); i++) {
                if (smallest > data.get(i).cardValue) {
                    smallest = data.get(i).cardValue;
                    smallestIndex = i;
                }
            }
            if (smallestIndex == curIndex)
                return;
            else {
                Card temp = data.get(curIndex);
                data.set(curIndex, data.get(smallestIndex));
                data.set(smallestIndex, temp);
            }

        }
    }
}
