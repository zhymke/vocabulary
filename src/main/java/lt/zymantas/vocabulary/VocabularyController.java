package lt.zymantas.vocabulary;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/vocabulary")
public class VocabularyController {

    private final VocabularyService vocabularyService;

    public VocabularyController(VocabularyService vocabularyService) {
        this.vocabularyService = vocabularyService;
    }


    @ExceptionHandler({FileReadException.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Failed to read submitted files")
    public void handleException() {
        //
    }

    @PostMapping
    public Map<String, List<Item>> createVocabulary(@RequestParam("files") List<MultipartFile> files, HttpServletRequest re) throws IOException, InterruptedException {
        return vocabularyService.getVocabulary(files);
    }
}
