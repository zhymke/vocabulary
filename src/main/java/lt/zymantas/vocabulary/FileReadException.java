package lt.zymantas.vocabulary;

public class FileReadException extends RuntimeException {
    public FileReadException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
