package lt.zymantas.vocabulary.RenameMe;

import lt.zymantas.vocabulary.Item;
import lt.zymantas.vocabulary.VocabularyService;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VocabularyServiceTest {

    @Test
    public void readFiles(){
        VocabularyService vocabularyService = new VocabularyService();
        MultipartFile multipartFile = new MockMultipartFile("mock file", "aaaa  aaa a".getBytes(StandardCharsets.UTF_8));
        MultipartFile multipartFile1 = new MockMultipartFile("mock file", "hh hh".getBytes(StandardCharsets.UTF_8));
        MultipartFile multipartFile2 = new MockMultipartFile("mock file", "one two one!".getBytes(StandardCharsets.UTF_8));
        MultipartFile multipartFile3 = new MockMultipartFile("mock file", "yellow! yellow?".getBytes(StandardCharsets.UTF_8));
        MultipartFile multipartFile4 = new MockMultipartFile("mock file", "right left".getBytes(StandardCharsets.UTF_8));
        Map<String, List<Item>> vocabulary =  vocabularyService.getVocabulary(Arrays.asList(multipartFile, multipartFile1, multipartFile2,
                multipartFile3, multipartFile4));

        assertEquals(3, vocabulary.get("A-G").size());
        assertEquals(4, vocabulary.size());
        assertEquals("aaa", vocabulary.get("A-G").get(0).getWord());
        assertEquals(1, vocabulary.get("A-G").get(0).getCount());

        assertEquals(2, vocabulary.get("H-N").size());
        assertEquals("hh", vocabulary.get("H-N").get(0).getWord());
        assertEquals(2, vocabulary.get("H-N").get(0).getCount());
    }
}
