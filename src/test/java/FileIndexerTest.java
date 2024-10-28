import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class FileIndexerTest {
    private FileIndexer indexer;
    private File testDir;
    private File testFile1;
    private File testFile2;
    private File emptyFile;

    @BeforeEach
    public void setUp() throws IOException {
        indexer = new FileIndexer();
        testDir = Files.createTempDirectory("testDir").toFile();

        // Create test files
        testFile1 = new File(testDir, "test1.txt");
        testFile2 = new File(testDir, "test2.txt");
        emptyFile = new File(testDir, "empty.txt");

        // Write content to test files
        try (FileWriter writer = new FileWriter(testFile1)) {
            writer.write("Hello world! This is a test file.");
        }
        try (FileWriter writer = new FileWriter(testFile2)) {
            writer.write("Another file with different content. The world is vast.");
        }
        if (!emptyFile.createNewFile()) {
            throw new IOException("Failed to create empty test file");
        }
    }

    @AfterEach
    public void tearDown() {
        for (File file : Objects.requireNonNull(testDir.listFiles())) {
            if (!file.delete()) {
                System.err.println("Warning: Failed to delete file " + file.getName());
            }
        }
        if (!testDir.delete()) {
            System.err.println("Warning: Failed to delete directory " + testDir.getName());
        }
    }

    @Test
    public void testIndexSingleFile() {
        indexer.indexFile(testFile1.getPath());

        Set<String> filesContainingHello = indexer.getWordToFileMap().get("hello");
        assertTrue(filesContainingHello.contains("test1.txt"));

        Set<String> filesContainingWorld = indexer.getWordToFileMap().get("world");
        assertTrue(filesContainingWorld.contains("test1.txt"));
    }

    @Test
    public void testIndexDirectory() {
        indexer.indexDirectory(testDir.getPath());

        Set<String> filesContainingHello = indexer.getWordToFileMap().get("hello");
        assertTrue(filesContainingHello.contains("test1.txt"));

        Set<String> filesContainingWorld = indexer.getWordToFileMap().get("world");
        assertTrue(filesContainingWorld.contains("test1.txt"));
        assertTrue(filesContainingWorld.contains("test2.txt"));
    }

    @Test
    public void testCaseInsensitiveSearch() {
        indexer.indexDirectory(testDir.getPath());

        Set<String> filesContainingHello = indexer.getWordToFileMap().get("HELLO".toLowerCase());
        assertTrue(filesContainingHello.contains("test1.txt"));

        Set<String> filesContainingWorld = indexer.getWordToFileMap().get("WORLD".toLowerCase());
        assertTrue(filesContainingWorld.contains("test1.txt"));
        assertTrue(filesContainingWorld.contains("test2.txt"));
    }

    @Test
    public void testEmptyFileIndexing() {
        indexer.indexFile(emptyFile.getPath());

        Set<String> filesContainingAnyWord = indexer.getWordToFileMap().get("anyword");
        assertTrue(filesContainingAnyWord == null || filesContainingAnyWord.isEmpty());
    }

    @Test
    public void testQueryForNonExistentWord() {
        indexer.indexDirectory(testDir.getPath());

        Set<String> filesContainingNonExistentWord = indexer.getWordToFileMap().get("nonexistentword");
        assertTrue(filesContainingNonExistentWord == null || filesContainingNonExistentWord.isEmpty());
    }
    @Test
    public void testSpecialCharactersAndPunctuation() throws IOException {
        File specialCharFile = new File(testDir, "specialCharFile.txt");
        try (FileWriter writer = new FileWriter(specialCharFile)) {
            writer.write("Hello, world! Testing punctuation: should, ignore.");
        }

        indexer.indexFile(specialCharFile.getPath());

        Set<String> filesContainingHello = indexer.getWordToFileMap().get("hello");
        assertTrue(filesContainingHello.contains("specialCharFile.txt"));

        Set<String> filesContainingTesting = indexer.getWordToFileMap().get("testing");
        assertTrue(filesContainingTesting.contains("specialCharFile.txt"));

        Set<String> filesContainingShould = indexer.getWordToFileMap().get("should");
        assertTrue(filesContainingShould.contains("specialCharFile.txt"));

        Set<String> filesContainingIgnore = indexer.getWordToFileMap().get("ignore");
        assertTrue(filesContainingIgnore.contains("specialCharFile.txt"));

        // Ensure punctuation is ignored, no entries for "," or ":"
        assertNull(indexer.getWordToFileMap().get(","));
        assertNull(indexer.getWordToFileMap().get(":"));
    }

    @Test
    public void testWordInMultipleFiles() throws IOException {
        File additionalFile = new File(testDir, "additionalFile.txt");
        try (FileWriter writer = new FileWriter(additionalFile)) {
            writer.write("world of code, where the world is vast.");
        }

        indexer.indexFile(testFile1.getPath());  // Contains "world" and "hello"
        indexer.indexFile(additionalFile.getPath());  // Contains "world"

        Set<String> filesContainingWorld = indexer.getWordToFileMap().get("world");
        assertTrue(filesContainingWorld.contains("test1.txt"));
        assertTrue(filesContainingWorld.contains("additionalFile.txt"));

        // Ensure the word is indexed in both files
        assertEquals(2, filesContainingWorld.size());
    }

    @Test
    public void testQueryEmptyString() {
        indexer.indexFile(testFile1.getPath());

        Set<String> filesContainingEmpty = indexer.getWordToFileMap().get("");
        assertNull(filesContainingEmpty);

        Set<String> filesContainingWhitespace = indexer.getWordToFileMap().get(" ");
        assertNull(filesContainingWhitespace);
    }
}

