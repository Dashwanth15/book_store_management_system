package frontend;

import backend.BookInventory;
import backend.Book;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class AdminInterface extends JFrame {
    private BookInventory inventory;
    private JTable bookTable;
    private DefaultTableModel tableModel;
    private JTextField titleField, priceField, stockField;
    private JButton addButton, updateButton, deleteButton, logoutButton;

    public AdminInterface(BookInventory inventory) {
        this.inventory = inventory;

        setTitle("Admin Interface - Manage Books");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Table model with columns: Title, Price, Stock
        tableModel = new DefaultTableModel(new Object[]{"Title", "Price", "Stock"}, 0);
        bookTable = new JTable(tableModel);
        loadBooksToTable();

        add(new JScrollPane(bookTable), BorderLayout.CENTER);

        // Input panel
        JPanel inputPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputPanel.add(new JLabel("Title:"));
        inputPanel.add(new JLabel("Price:"));
        inputPanel.add(new JLabel("Stock:"));
        inputPanel.add(new JLabel("")); // empty for spacing

        titleField = new JTextField();
        priceField = new JTextField();
        stockField = new JTextField();

        inputPanel.add(titleField);
        inputPanel.add(priceField);
        inputPanel.add(stockField);

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout());

        addButton = new JButton("Add Book");
        updateButton = new JButton("Update Book");
        deleteButton = new JButton("Delete Book");
        logoutButton = new JButton("Logout");

        buttonsPanel.add(addButton);
        buttonsPanel.add(updateButton);
        buttonsPanel.add(deleteButton);
        buttonsPanel.add(logoutButton);

        add(inputPanel, BorderLayout.NORTH);
        add(buttonsPanel, BorderLayout.SOUTH);

        // Button listeners
        addButton.addActionListener(e -> addBook());
        updateButton.addActionListener(e -> updateBook());
        deleteButton.addActionListener(e -> deleteBook());
        logoutButton.addActionListener(e -> logout());

        // Table row selection listener
        bookTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && bookTable.getSelectedRow() != -1) {
                int row = bookTable.getSelectedRow();
                titleField.setText((String) tableModel.getValueAt(row, 0));
                priceField.setText(tableModel.getValueAt(row, 1).toString());
                stockField.setText(tableModel.getValueAt(row, 2).toString());
                titleField.setEnabled(false);  // prevent changing title on update
            }
        });

        setVisible(true);
    }

    private void loadBooksToTable() {
        tableModel.setRowCount(0);
        for (Book b : inventory.getBooks()) {
            tableModel.addRow(new Object[]{b.getTitle(), b.getPrice(), b.getStock()});
        }
    }

    private void addBook() {
        String title = titleField.getText().trim();
        String priceStr = priceField.getText().trim();
        String stockStr = stockField.getText().trim();

        if (title.isEmpty() || priceStr.isEmpty() || stockStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }

        try {
            double price = Double.parseDouble(priceStr);
            int stock = Integer.parseInt(stockStr);

            if (inventory.getBookByTitle(title) != null) {
                JOptionPane.showMessageDialog(this, "Book with this title already exists.");
                return;
            }

            inventory.getBooks().add(new Book(title, price, stock));
            loadBooksToTable();
            clearFields();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number format for price or stock.");
        }
    }

    private void updateBook() {
        String title = titleField.getText().trim();
        String priceStr = priceField.getText().trim();
        String stockStr = stockField.getText().trim();

        if (title.isEmpty() || priceStr.isEmpty() || stockStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }

        try {
            double price = Double.parseDouble(priceStr);
            int stock = Integer.parseInt(stockStr);

            Book book = inventory.getBookByTitle(title);
            if (book == null) {
                JOptionPane.showMessageDialog(this, "Book not found.");
                return;
            }

            inventory.updateBook(title, price, stock);
            loadBooksToTable();
            clearFields();
            titleField.setEnabled(true);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number format for price or stock.");
        }
    }

    private void deleteBook() {
        String title = titleField.getText().trim();
        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a book to delete.");
            return;
        }

        Book book = inventory.getBookByTitle(title);
        if (book == null) {
            JOptionPane.showMessageDialog(this, "Book not found.");
            return;
        }

        inventory.getBooks().remove(book);
        loadBooksToTable();
        clearFields();
        titleField.setEnabled(true);
    }

    private void logout() {
        dispose();
        // You can add code here to show login screen again if needed
    }

    private void clearFields() {
        titleField.setText("");
        priceField.setText("");
        stockField.setText("");
    }
}
