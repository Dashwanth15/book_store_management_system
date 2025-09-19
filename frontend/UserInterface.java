package frontend;

import backend.Book;
import backend.BookInventory;
import backend.CartItem;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class UserInterface extends JFrame {
    private JTable bookTable;
    private DefaultTableModel tableModel;
    private ArrayList<CartItem> cart;
    private JButton addToCartButton, viewCartButton, checkoutButton, logoutButton;
    private JTextField quantityField;
    private BookInventory inventory;

    public UserInterface(BookInventory inventory) {
        this.inventory = inventory;

        setTitle("User Interface - Online Bookstore");
        setSize(700, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cart = new ArrayList<>();

        // Table model with Stock column added
        tableModel = new DefaultTableModel(new Object[]{"Title", "Price", "Stock"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false; // all cells not editable here
            }
        };
        bookTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(bookTable);

        // Load books from inventory
        loadBooksToTable();

        // Quantity input with label
        JPanel quantityPanel = new JPanel();
        quantityPanel.setBorder(BorderFactory.createTitledBorder("Enter Quantity for Selected Book"));
        quantityPanel.add(new JLabel("Quantity:"));
        quantityField = new JTextField(5);
        quantityPanel.add(quantityField);

        // Buttons
        addToCartButton = new JButton("Add to Cart");
        viewCartButton = new JButton("View Cart");
        checkoutButton = new JButton("Checkout");
        logoutButton = new JButton("Logout");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Actions"));
        buttonPanel.add(addToCartButton);
        buttonPanel.add(viewCartButton);
        buttonPanel.add(checkoutButton);
        buttonPanel.add(logoutButton);

        add(quantityPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Actions
        addToCartButton.addActionListener(e -> addSelectedBookToCart());
        viewCartButton.addActionListener(e -> showCart());
        checkoutButton.addActionListener(e -> checkoutAction());
        logoutButton.addActionListener(e -> logout());

        setVisible(true);
    }

    private void loadBooksToTable() {
        tableModel.setRowCount(0);
        for (Book book : inventory.getBooks()) {
            tableModel.addRow(new Object[]{book.getTitle(), book.getPrice(), book.getStock()});
        }
    }

    private void addSelectedBookToCart() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book.");
            return;
        }

        String quantityText = quantityField.getText().trim();
        if (quantityText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter quantity.");
            return;
        }

        try {
            int quantity = Integer.parseInt(quantityText);
            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this, "Quantity must be at least 1.");
                return;
            }

            int stock = (int) tableModel.getValueAt(selectedRow, 2);
            if (quantity > stock) {
                JOptionPane.showMessageDialog(this, "Quantity exceeds available stock.");
                return;
            }

            String title = (String) tableModel.getValueAt(selectedRow, 0);
            double price = (double) tableModel.getValueAt(selectedRow, 1);
            cart.add(new CartItem(new Book(title, price, stock), quantity));

            JOptionPane.showMessageDialog(this, "Added " + quantity + " to cart.");
            quantityField.setText("");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid quantity.");
        }
    }

    private void showCart() {
        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cart is empty.");
            return;
        }

        StringBuilder cartDetails = new StringBuilder();
        double total = 0;

        for (CartItem item : cart) {
            double subtotal = item.getBook().getPrice() * item.getQuantity();
            cartDetails.append(item.getBook().getTitle())
                    .append(" x").append(item.getQuantity())
                    .append(" - $").append(String.format("%.2f", subtotal)).append("\n");
            total += subtotal;
        }

        cartDetails.append("\nTotal: $").append(String.format("%.2f", total));
        JOptionPane.showMessageDialog(this, cartDetails.toString(), "Cart", JOptionPane.INFORMATION_MESSAGE);
    }

    private void checkoutAction() {
        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cart is empty.");
            return;
        }

        StringBuilder summary = new StringBuilder("Checkout Summary:\n\n");
        double total = 0;

        for (CartItem item : cart) {
            double subtotal = item.getBook().getPrice() * item.getQuantity();
            summary.append(item.getBook().getTitle())
                    .append(" x").append(item.getQuantity())
                    .append(" - $").append(String.format("%.2f", subtotal)).append("\n");
            total += subtotal;
        }

        summary.append("\nTotal: $").append(String.format("%.2f", total));
        int confirm = JOptionPane.showConfirmDialog(this, summary.toString() + "\n\nConfirm Purchase?", "Checkout", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            // Update inventory stock
            for (CartItem item : cart) {
                Book book = inventory.getBookByTitle(item.getBook().getTitle());
                if (book != null) {
                    int newStock = book.getStock() - item.getQuantity();
                    inventory.updateStock(book.getTitle(), newStock);
                }
            }
            cart.clear();
            loadBooksToTable();
            JOptionPane.showMessageDialog(this, "Purchase successful! Thank you!");
        }
    }

    private void logout() {
        dispose();
        new BookstoreLogin(inventory);
    }
}
