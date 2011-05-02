package be.alex.dpc;

public class IntArrays {
    
    public static boolean contains(int[] haystack, int needle) {
        for(int element : haystack) {
            if(element == needle) {
                return true;
            }
        }

        return false;
    }
}
