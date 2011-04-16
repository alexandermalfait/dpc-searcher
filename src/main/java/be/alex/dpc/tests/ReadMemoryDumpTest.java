package be.alex.dpc.tests;

import be.alex.dpc.SearchService;

public class ReadMemoryDumpTest {

    public static void main(String[] args) {
        SearchService searcher = new SearchService();

        searcher.readMemoryDump("memory.dump");
    }
}
