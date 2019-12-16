package lt.zymantas.vocabulary;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Vocabulary {
    private final char[] ALPHABET = "abcdefghijklmnopqrstuvwxyz".toCharArray();

    private ConcurrentMap<String, Integer> vocabularyFromAToG = new ConcurrentHashMap<>();
    private final int LETTER_G_INDEX = 6;

    private ConcurrentMap<String, Integer> vocabularyFromHToN = new ConcurrentHashMap<>();
    private final int LETTER_N_INDEX = 13;

    private ConcurrentMap<String, Integer> vocabularyFromOToU = new ConcurrentHashMap<>();
    private final int LETTER_U_INDEX = 20;

    private ConcurrentMap<String, Integer> vocabularyFromVToZ = new ConcurrentHashMap<>();
    private final int LETTER_Z_INDEX = 25;

    public void put(String word) {
        getBucket(word).compute(word, (k, val) -> val != null ? ++val : 1);
    }

    private ConcurrentMap<String, Integer> getBucket(String word) {
        int searchIndex = Arrays.binarySearch(ALPHABET, word.toLowerCase().charAt(0));

        if (isAGBucket(searchIndex)) {
            return vocabularyFromAToG;
        }

        if (isHNBucket(searchIndex)) {
            return vocabularyFromHToN;
        }

        if (isOUBucket(searchIndex)) {
            return vocabularyFromOToU;
        }

        if (isVZBucket(searchIndex)) {
            return vocabularyFromVToZ;
        }

        throw new IllegalArgumentException(String.format("Value %s is not in the English alphabet"));
    }

    private boolean isAGBucket(int searchIndex) {
        return searchIndex >= 0 && searchIndex <= LETTER_G_INDEX;
    }

    private boolean isHNBucket(int searchIndex) {
        return searchIndex > LETTER_G_INDEX && searchIndex <= LETTER_N_INDEX;
    }

    private boolean isOUBucket(int searchIndex) {
        return searchIndex > LETTER_N_INDEX && searchIndex <= LETTER_U_INDEX;
    }

    private boolean isVZBucket(int searchIndex) {
        return searchIndex > LETTER_U_INDEX && searchIndex <= LETTER_Z_INDEX;
    }

    public Map<String, Map<String, Integer>> getGroup() {
        Map<String, Map<String, Integer>> group = new HashMap<>();
        group.put("A-G", new HashMap<>(vocabularyFromAToG));
        group.put("H-N", new HashMap<>(vocabularyFromHToN));
        group.put("O-U", new HashMap<>(vocabularyFromOToU));
        group.put("V-Z", new HashMap<>(vocabularyFromVToZ));
        return group;
    }
}
