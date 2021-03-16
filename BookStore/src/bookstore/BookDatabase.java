package bookstore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

class BookDatabase {

    private final String FILE_PATH = "BookDatabase.txt";
    private final ArrayList<Book> bookDatabase;

    BookDatabase() {
        this.bookDatabase = new ArrayList<>();

        Load();
    }

    public ArrayList<Book> getBookDatabaseNoModify() {
        return new ArrayList<>(bookDatabase);
    }

    public ArrayList<Book> getBookDatabaseModify() {
        return bookDatabase;
    }

    public void addToBookDatabase(Book book) {
        this.bookDatabase.add(book);
    }

    private void Load() {
        Scanner input;
        File file = new File(FILE_PATH);
        try {
            if (!file.exists()) {
                System.out.println("Error: File didn't exist, but has been created");
                try {
                    boolean created;
                    created = file.createNewFile();
                    if (!created) {
                        throw new IOException("File could not be created");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            input = new Scanner(file);
            if (!input.hasNext()) {
                System.out.println("Book Database is Empty");
                return;
            }
            String line;
            String[] lines;
            while (input.hasNext()) {
                line = Decrypt(input.nextLine());
                lines = line.split("#");
                if (lines.length > 7) {
                    ArrayList<Review> reviews = new ArrayList<>();
                    for (int i = 7; i < lines.length; i += 3) {
                        reviews.add(new Review(lines[i], lines[i + 1], lines[i + 2]));
                    }
                    bookDatabase.add(new Book(Integer.parseInt(lines[0]), lines[1], lines[2],
                            Integer.parseInt(lines[3]), Integer.parseInt(lines[4]), lines[5],
                            Double.parseDouble(lines[6]), reviews));
                } else {
                    bookDatabase.add(new Book(Integer.parseInt(lines[0]), lines[1], lines[2],
                            Integer.parseInt(lines[3]), Integer.parseInt(lines[4]), lines[5], Double.parseDouble(lines[6])));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void Upload() {
        try {
            PrintWriter printWriter = new PrintWriter(FILE_PATH, "UTF-8");
            for (Book book : bookDatabase) {
                printWriter.println(book.SendEncryption());
            }
            printWriter.close();
        } catch (IOException e) {
            Logger.getLogger(UserDatabase.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private String Decrypt(String line) {
        int aux;
        char[] array = line.toCharArray();
        for (int i = 0; i < array.length; i++) {
            aux = (int) (array[i]);
            aux -= 3;
            array[i] = (char) aux;
        }
        return String.valueOf(array);
    }
}