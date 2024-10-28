import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        FileIndexer indexer = new FileIndexer();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the File Indexer!");
        System.out.println("Here are simple usage instructions:");
        System.out.println("First you choose if you want to index a single file");
        System.out.println("or a whole directory of files, providing path to these files");
        System.out.println("After the indexing process is completed, you can select ");
        System.out.println("the options for indexing more files (1,2) or search for a");
        System.out.println("specific word to see in which files it can be found");
        System.out.println("Multiple requests are possible till option for exit is chosen");

        while (true) {
            System.out.println("\nOptions: [1] Index file, [2] Index directory, [3] Search word, [4] Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter file path to index: ");
                    String filePath = scanner.nextLine();
                    indexer.indexFile(filePath);
                    break;

                case 2:
                    System.out.print("Enter directory path to index: ");
                    String dirPath = scanner.nextLine();
                    indexer.indexDirectory(dirPath);
                    break;

                case 3:
                    System.out.print("Enter word to search: ");
                    String word = scanner.nextLine();
                    indexer.query(word);
                    break;

                case 4:
                    System.out.println("Exiting.");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid option.");
            }
        }
    }
}
