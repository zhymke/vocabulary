package lt.zymantas.vocabulary;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
@Log4j2
public class VocabularyService {

    private final String LETTERS = "[a-zA-Z]+";

    public Map<String, List<Item>> getVocabulary(List<MultipartFile> files) {
        Vocabulary vocabulary = new Vocabulary();
        readWithMultipleThreads(vocabulary, files);
        Map<String, List<Item>> result = mapToItemDto(vocabulary.getGroup());
        writeToFiles(result);
        return result;
    }

    private void readWithMultipleThreads(Vocabulary vocabulary, List<MultipartFile> files) {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        try {
            executor.invokeAll(getCallableList(vocabulary, files));
        } catch (Exception e) {
            log.error("Failed to read files ", e);
            throw new FileReadException("Failed to read submitted files", e);
        } finally {
            executor.shutdown();
        }
    }

    private List<Callable<LocalDateTime>> getCallableList(Vocabulary vocabulary, List<MultipartFile> files) {
        List<Callable<LocalDateTime>> callableList = new ArrayList<>();
        files.forEach(f -> callableList.add(() -> {
            readFile(f.getBytes(), vocabulary);
            return LocalDateTime.now();
        }));
        return callableList;
    }

    private void readFile(byte[] file, Vocabulary vocabulary) {
        Pattern pattern = Pattern.compile(LETTERS);
        Matcher matcher = pattern.matcher(new String(file));
        while (matcher.find()) {
            vocabulary.put(matcher.group());
        }
    }

    private Map<String, List<Item>> mapToItemDto(Map<String, Map<String, Integer>> grouped) {
        Map<String, List<Item>> result = new HashMap<>();
        grouped.forEach((k, v) -> {
            List<Item> list = new ArrayList<>();
            v.forEach((word, count) -> list.add(new Item(word, count)));
            result.put(k, list);
        });
        return result;
    }

    private void writeToFiles(Map<String, List<Item>> grouped) {
        grouped.forEach((k, v) ->
        {
            clearFile(k);
            v.forEach(item -> write(k, item));
        });
    }

    private void clearFile(String fileName){
        try {
            if (Files.exists(Paths.get(fileName))) {
                Files.newBufferedWriter(Paths.get(fileName), StandardOpenOption.TRUNCATE_EXISTING);
            } else {
                Files.newBufferedWriter(Paths.get(fileName), StandardOpenOption.CREATE);
            }
        } catch (IOException e) {
            log.error("Failed writing to files ", e);
        }
    }
    private void write(String fileName, Item item){
        try {
            Files.write(Paths.get(fileName), (item.toString() + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            log.error("Failed writing to files ", e);
        }
    }
}
