import java.io.*;
import java.nio.file.*;
import java.util.*;

public class FileIndexer {
    private final Map<String, Set<String>> wordToFileMap = new HashMap<>();


    public void indexFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            System.out.println("Invalid file: " + filePath);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split("\\W+");  // Split by non-word characters
                for (String token : tokens) {
                    if (!token.isEmpty()) {  // Ignore empty tokens
                        wordToFileMap.computeIfAbsent(token.toLowerCase(), k -> new HashSet<>()).add(filePath);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file " + filePath + ": " + e.getMessage());
        }
    }




}
