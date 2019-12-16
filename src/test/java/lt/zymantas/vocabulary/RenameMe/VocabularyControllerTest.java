package lt.zymantas.vocabulary.RenameMe;

import lt.zymantas.vocabulary.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VocabularyController.class)
public class VocabularyControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private VocabularyService vocabularyService;

    @Test
    public void createVocabularyEndpointTest() throws Exception {
        Map<String, List<Item>> serviceResult = new HashMap<>();
        List<Item> itemList = new ArrayList<>();
        itemList.add(new Item("result", 9));
        serviceResult.put("RESULT", itemList);

        MockMultipartFile firstFile = new MockMultipartFile("files", "filename", "text/plain", "test".getBytes());
        MockMultipartFile secondFile = new MockMultipartFile("files", "filename2", "text/plain", "test".getBytes());

        given(vocabularyService.getVocabulary(any()))
                .willReturn(serviceResult);

        mvc.perform(MockMvcRequestBuilders.multipart("/vocabulary")
                .file(firstFile)
                .file(secondFile)).andExpect(status().isOk())
                .andExpect(jsonPath("$.RESULT", hasSize(1)))
                .andExpect(jsonPath("$.RESULT.[0].word", is("result")))
                .andExpect(jsonPath("$.RESULT.[0].count", is(9)));
    }

    @Test
    public void shouldReturn500WhenExceptionIsThrown() throws Exception {
        given(vocabularyService.getVocabulary(any()))
                .willThrow(new FileReadException("Failed", new Exception()));

        mvc.perform(MockMvcRequestBuilders.multipart("/vocabulary"))
                .andExpect(status().is5xxServerError());
    }
}
