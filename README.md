TEXT FILE INDEXER

Service for Indexing Text Files
A Java console application for indexing text files and directories. This program allows users to specify files or directories to index and then search for specific words to identify which files contain them. The application includes tests for indexing, querying, and edge cases using JUnit 5.

Features
Index Files: Index individual text files and tokenize their content for fast word-based searching.
Index Directories: Index all text files within a specified directory.
Case-Insensitive Search: Retrieve files that contain a given word, regardless of the word’s case in the file.
Handles Punctuation and Special Characters: Ignores punctuation and special characters, indexing only words.
Robust Unit Tests: Includes comprehensive tests to validate functionality, covering various cases and error handling.

Setup and Installation
Clone the Repository:

git clone https://github.com/alekspetrov9e/FileIndexer.git
cd FileIndexer

Building:
./gradlew build

Run Tests: 
./gradlew test

Run Program:
Either from Main method or build and then java -jar build/libs/Service_for_indexing_text_files-1.0-SNAPSHOT.jar
