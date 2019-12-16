package lt.zymantas.vocabulary.RenameMe;


import lt.zymantas.vocabulary.Vocabulary;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VocabularyTest {

    @Test
    @DisplayName("Should return 4 groups of vocabulary")
    public void shouldReturnFourGroups() {
        Vocabulary vocabulary = new Vocabulary();
        Map<String, Map<String, Integer>> grouped = vocabulary.getGroup();
        assertTrue(grouped.keySet().contains("A-G"));
        assertTrue(grouped.keySet().contains("H-N"));
        assertTrue(grouped.keySet().contains("O-U"));
        assertTrue(grouped.keySet().contains("V-Z"));
    }

    @Test
    @DisplayName("Should throw exception if not English alphabet character is present")
    public void shouldThrowExceptionWithCharacterŽ() {
        Vocabulary vocabulary = new Vocabulary();
        Assertions.assertThrows(IllegalArgumentException.class, () -> vocabulary.put("Ž"));
    }

    @Test
    @DisplayName("Test if border characters are placed in good group")
    public void shouldBePutInGoodGroupWithBorderValues() {
        Vocabulary vocabulary = new Vocabulary();
        vocabulary.put("A");
        vocabulary.put("g");
        vocabulary.put("H");
        vocabulary.put("n");
        vocabulary.put("O");
        vocabulary.put("u");
        vocabulary.put("v");
        vocabulary.put("Z");

        Map<String, Integer> aG = vocabulary.getGroup().get("A-G");
        assertEquals(1, aG.get("A"));
        assertEquals(1, aG.get("g"));

        Map<String, Integer> hN = vocabulary.getGroup().get("H-N");
        assertEquals(1, hN.get("H"));
        assertEquals(1, hN.get("n"));

        Map<String, Integer> oU = vocabulary.getGroup().get("O-U");
        assertEquals(1, oU.get("O"));
        assertEquals(1, oU.get("u"));

        Map<String, Integer> vZ = vocabulary.getGroup().get("V-Z");
        assertEquals(1, vZ.get("v"));
        assertEquals(1, vZ.get("Z"));
    }

    @Test
    public void testMultiThreading() {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        Vocabulary vocabulary = new Vocabulary();
        List<Callable<LocalDateTime>> callable = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            callable.add(() -> {
                for (int j = 0; j < 100; j++) {
                    vocabulary.put("test");
                }
                return LocalDateTime.now();
            });
        }

        try {
            executor.invokeAll(callable);
            assertEquals(500, vocabulary.getGroup().get("O-U").get("test"));
        } catch (Exception e) {

        } finally {
            executor.shutdown();
        }
    }
}
