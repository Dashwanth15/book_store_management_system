package backend;

public class Book {
    private String title;
    private double price;
    private int stock;

    public Book(String title, double price, int stock) {
        this.title = title;
        this.price = price;
        this.stock = stock;
    }

    public String getTitle() {
        return title;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}


