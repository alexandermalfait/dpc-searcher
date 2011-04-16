package be.alex.dpc.tests;

import be.alex.dpc.Util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MemoryUsageWithArraysTest {

    public static void main(String[] args) {
        List<Object[]> words = new ArrayList<Object[]>(5000000);

        for(int offset = 0; offset < 5000000; offset++) {
            Object[] word = new Object[4];

            word[0] = "testje" + offset;
            word[1] = offset * 2;
            word[2] = offset * 3;

            HashSet<Integer> flags = new HashSet<Integer>();

            flags.add(4);
            flags.add(5);

            word[3] = flags.toArray();

            if(offset % 10000 == 0) {
                System.out.println("Created " + offset + " arrays, used memory: " + Util.getUsedMemoryFormatted());
            }

            words.add(word);
        }
    }

}