package be.alex.dpc.tests;

import be.alex.dpc.SearchService;

public class MemoryDumpTest {
    public static void main(String[] args) {
        SearchService searcher = new SearchService();

        searcher.readDatabase();

        searcher.dumpMemory("memory.dump");
    }
}
