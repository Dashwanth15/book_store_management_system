package backend;

import java.util.ArrayList;

public class BookInventory {
    private ArrayList<Book> books = new ArrayList<>();

    public BookInventory() {
        // Initialize with sample books and stock
        books.add(new Book("Effective Java", 45.99, 10));
        books.add(new Book("Clean Code", 39.99, 8));
        books.add(new Book("Java Concurrency", 49.99, 6));
        books.add(new Book("Design Patterns", 42.50, 5));
    }

    // Get all books
    public ArrayList<Book> getBooks() {
        return books;
    }

    // Get a book by its title
    public Book getBookByTitle(String title) {
        for (Book b : books) {
            if (b.getTitle().equalsIgnoreCase(title)) {
                return b;
            }
        }
        return null;
    }

    // Add a new book
    public void addBook(String title, double price, int stock) {
        books.add(new Book(title, price, stock));
    }

    // Update an existing book
    public void updateBook(String title, double price, int stock) {
        Book book = getBookByTitle(title);
        if (book != null) {
            book.setPrice(price);
            book.setStock(stock);
        }
    }

    // Delete a book by its title
    public void deleteBook(String title) {
        books.removeIf(book -> book.getTitle().equalsIgnoreCase(title));
    }

    // Update only stock of a book
    public void updateStock(String title, int newStock) {
        Book book = getBookByTitle(title);
        if (book != null) {
            book.setStock(newStock);
        }
    }
}
