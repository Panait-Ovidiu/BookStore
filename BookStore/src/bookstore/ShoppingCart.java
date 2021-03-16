package bookstore;

import java.util.ArrayList;

class ShoppingCart {

    private final ArrayList<Item> items;

    ShoppingCart() {
        this.items = new ArrayList<>();
    }

    public ArrayList<Item> getItems() {
        return this.items;
    }

    public int getNrOfBooks() {
        int books = 0;
        for (Item item : items) {
            books += item.getQuantity();
        }
        return books;
    }

    public String getTotalPrice() {
        double total = 0;
        for (Item item : items) {
            total += (item.getBook().getPrice() * item.getQuantity());
        }
        return String.format("%.2f", total);
    }

    public void addToCart(Book book, int quantity) {
        if (items != null) {
            for (Item pointer : items) {
                if (pointer.getBook().getID() == book.getID()) {
                    pointer.setQuantity(pointer.getQuantity() + quantity);
                    return;
                }
            }
        }
        this.items.add(new Item(book, quantity));
    }

    public void removeFromCar(Item item) {
        this.items.remove(item);
    }

}
