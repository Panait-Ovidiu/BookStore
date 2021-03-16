package bookstore;

class Item implements Comparable<Item> {

    private final Book book;
    private int quantity;

    Item(Book book, int quantity) {
        this.book = book;
        this.quantity = quantity;
    }

    public Book getBook() {
        return this.book;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public int compareTo(Item item) {
        return Integer.compare(this.book.getID(), item.book.getID());
    }
}
