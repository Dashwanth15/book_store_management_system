import backend.BookInventory;
import frontend.BookstoreLogin;

public class Main {
    public static void main(String[] args) {
        // Create the shared BookInventory instance
        BookInventory inventory = new BookInventory();

        // Launch the login window with the inventory shared
        new BookstoreLogin(inventory);
    }
}
