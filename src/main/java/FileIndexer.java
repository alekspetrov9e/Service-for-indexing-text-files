import java.io.*;
import java.nio.file.*;
import java.util.*;

public class FileIndexer {
    private final Map<String, Set<String>> wordToFileMap = new HashMap<>();


    public Map<String, Set<String>> getWordToFileMap() {
        return wordToFileMap;
    }

    public void indexFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            System.out.println("Invalid file: " + filePath);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            String[] filepath_contents = filePath.split("/");
            String filename = filepath_contents[filepath_contents.length - 1];
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split("\\W+"); // Split by non-word characters
                for (String token : tokens) {
                    if (!token.isEmpty()) {  // Ignore empty tokens
                        wordToFileMap.computeIfAbsent(token.toLowerCase(), k -> new HashSet<>()).add(filename);
                    }
                }

            }
            System.out.println("Indexing file " + filename + " is completed successfully");
        } catch (IOException e) {
            System.err.println("Error reading file " + filePath + ": " + e.getMessage());
        }
    }

    public void indexDirectory(String dirPath) {
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(dirPath))) {
            for (Path path : directoryStream) {
                if (Files.isRegularFile(path)) {
                    indexFile(path.toString());
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading directory " + dirPath);
        }
    }

    public void query(String word) {
        Set<String> files = wordToFileMap.get(word.toLowerCase());
        if (files == null || files.isEmpty()) {
            System.out.println("No files found containing word: " + word);
        } else {
            System.out.println("Files containing '" + word + "':");
            files.forEach(System.out::println);
        }
    }


}
