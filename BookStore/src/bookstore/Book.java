package bookstore;

import java.util.ArrayList;

public class Book implements Comparable<Book> {

    private final int ID;
    private static int maxID = -1;
    private final String title;
    private final String author;
    private final int year;
    private final int pages;
    private final String description;
    private final double price;
    private ArrayList<Review> reviews = new ArrayList<>();

    Book(int ID, String title, String author, int year, int pages, String description, double price) {
        if (ID > maxID) {
            maxID = ID;
        }
        this.ID = ID;
        this.title = title;
        this.author = author;
        this.year = year;
        this.pages = pages;
        this.description = description;
        this.price = price;
    }

    Book(int ID, String title, String author, int year, int pages, String description, double price, ArrayList<Review> reviews) {
        if (ID > maxID) {
            maxID = ID;
        }
        this.ID = ID;
        this.title = title;
        this.author = author;
        this.year = year;
        this.pages = pages;
        this.description = description;
        this.price = price;
        this.reviews = reviews;
    }

    Book(String title, String author, int year, int pages, String description, double price) {
        this.ID = ++maxID;
        this.title = title;
        this.author = author;
        this.year = year;
        this.pages = pages;
        this.description = description;
        this.price = price;
    }

    public int getID() {
        return this.ID;
    }

    public String getTitle() {
        return this.title;
    }

    public String getAuthor() {
        return this.author;
    }

    public int getYear() {
        return this.year;
    }

    public int getPages() {
        return this.pages;
    }

    public String getDescription() {
        return this.description;
    }

    public double getPrice() {
        return this.price;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public void addReview(Review review) {
        reviews.add(review);
    }

    public boolean isThisTheBook(String title, String author) {
        return this.title.equals(title) && this.author.equals(author);
    }

    private String Encrypt(String line) {
        int aux;
        char[] array = line.toCharArray();
        for (int i = 0; i < array.length; i++) {
            aux = (int) (array[i]);
            aux += 3;

            array[i] = (char) aux;
        }
        return new String(array);
    }

    public String SendEncryption() {
        StringBuilder string = new StringBuilder();
        string.append(ID).append("#")
                .append(title).append("#")
                .append(author).append("#")
                .append(year).append("#")
                .append(pages).append("#")
                .append(description).append("#")
                .append(price).append("#");
        if (reviews != null) {
            for (Review review : reviews) {
                string.append(review.getUser()).append("#")
                        .append(review.getMessage()).append("#")
                        .append(review.getDate()).append("#");
            }
        }
        return Encrypt(string.toString());
    }

    @Override
    public int compareTo(Book book) {
        return Integer.compare(this.ID, book.ID);
    }
}
