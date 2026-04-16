package dbnew;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;

/**
 * OrderPanel.java
 * Displays all orders in a JTable and provides Add / Delete / Refresh actions.
 */
public class Orderpanel extends JPanel {

    // ---- Colors ----
    private static final Color ORANGE      = new Color(255, 111, 0);
    private static final Color DARK_ORANGE = new Color(220, 85, 0);
    private static final Color WHITE       = Color.WHITE;
    private static final Color LIGHT_GRAY  = new Color(245, 245, 245);
    private static final Color TEXT_DARK   = new Color(40, 40, 40);

    // ---- Table ----
    private JTable ordersTable;
    private DefaultTableModel tableModel;

    // ---- Form fields ----
    private JTextField customerNameField;
    private JTextField foodItemField;
    private JTextField quantityField;
    private JTextField priceField;
    private JComboBox<String> statusCombo;

    public Orderpanel() {
        setBackground(LIGHT_GRAY);
        setLayout(new BorderLayout(0, 0));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        add(buildHeader(),    BorderLayout.NORTH);
        add(buildTable(),     BorderLayout.CENTER);
        add(buildFormPanel(), BorderLayout.SOUTH);

        loadOrders(); // populate table on startup
    }

    // =========================================================
    //  Header
    // =========================================================
    private JPanel buildHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(LIGHT_GRAY);
        panel.setBorder(new EmptyBorder(0, 0, 14, 0));

        JLabel title = new JLabel("Order Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(TEXT_DARK);
        panel.add(title, BorderLayout.WEST);

        return panel;
    }

    // =========================================================
    //  Table (scroll pane)
    // =========================================================
    private JScrollPane buildTable() {
        // Column names matching food_orders columns
        String[] columns = {"ID", "Customer Name", "Food Item", "Qty", "Price (₹)", "Status", "Order Date"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false; // read-only table
            }
        };

        ordersTable = new JTable(tableModel);
        ordersTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        ordersTable.setRowHeight(30);
        ordersTable.setSelectionBackground(new Color(255, 200, 150));
        ordersTable.setSelectionForeground(TEXT_DARK);
        ordersTable.setGridColor(new Color(230, 230, 230));
        ordersTable.setShowVerticalLines(true);
        ordersTable.setFillsViewportHeight(true);

        // Style the header
        JTableHeader header = ordersTable.getTableHeader();
        header.setBackground(ORANGE);
        header.setForeground(WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setPreferredSize(new Dimension(0, 36));

        // Narrow the ID column
        ordersTable.getColumnModel().getColumn(0).setPreferredWidth(40);
        ordersTable.getColumnModel().getColumn(3).setPreferredWidth(45);

        JScrollPane scrollPane = new JScrollPane(ordersTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        scrollPane.setBackground(WHITE);
        return scrollPane;
    }

    // =========================================================
    //  Form panel (fields + buttons)
    // =========================================================
    private JPanel buildFormPanel() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(LIGHT_GRAY);
        wrapper.setBorder(new EmptyBorder(16, 0, 0, 0));

        // White card around the form
        JPanel card = new JPanel(new BorderLayout(0, 12));
        card.setBackground(WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                new EmptyBorder(16, 18, 16, 18)
        ));

        // Form label
        JLabel formTitle = new JLabel("Add New Order");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        formTitle.setForeground(ORANGE);
        card.add(formTitle, BorderLayout.NORTH);

        // Fields row
        JPanel fieldsRow = new JPanel(new GridLayout(1, 5, 10, 0));
        fieldsRow.setBackground(WHITE);

        customerNameField = createField("Customer Name");
        foodItemField     = createField("Food Item");
        quantityField     = createField("Quantity");
        priceField        = createField("Unit Price (₹)");

        statusCombo = new JComboBox<>(new String[]{"Pending", "Delivered"});
        statusCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        statusCombo.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Status", 0, 0,
                new Font("Segoe UI", Font.PLAIN, 11), TEXT_DARK));

        fieldsRow.add(customerNameField);
        fieldsRow.add(foodItemField);
        fieldsRow.add(quantityField);
        fieldsRow.add(priceField);
        fieldsRow.add(statusCombo);
        card.add(fieldsRow, BorderLayout.CENTER);

        // Buttons row
        JPanel buttonsRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttonsRow.setBackground(WHITE);

        JButton addBtn     = createButton("➕  Add Order",    ORANGE,      WHITE);
        JButton deleteBtn  = createButton("🗑  Delete Order",  new Color(200, 50, 50), WHITE);
        JButton refreshBtn = createButton("🔄  Refresh Table", new Color(60, 60, 60),  WHITE);

        // Action listeners
        addBtn.addActionListener(e -> addOrder());
        deleteBtn.addActionListener(e -> deleteOrder());
        refreshBtn.addActionListener(e -> loadOrders());

        buttonsRow.add(addBtn);
        buttonsRow.add(deleteBtn);
        buttonsRow.add(refreshBtn);
        card.add(buttonsRow, BorderLayout.SOUTH);

        wrapper.add(card, BorderLayout.CENTER);
        return wrapper;
    }

    /** Convenience: styled text field with a titled border as placeholder. */
    private JTextField createField(String placeholder) {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200)),
                        placeholder, 0, 0,
                        new Font("Segoe UI", Font.PLAIN, 11), TEXT_DARK),
                new EmptyBorder(2, 4, 2, 4)));
        return field;
    }

    /** Convenience: styled button. */
    private JButton createButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(155, 36));

        // Hover effect
        Color hover = bg.darker();
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(hover); }
            public void mouseExited(java.awt.event.MouseEvent e)  { btn.setBackground(bg);    }
        });
        return btn;
    }

    // =========================================================
    //  CRUD Methods
    // =========================================================

    /** Loads (or reloads) all rows from food_orders into the JTable. */
    public void loadOrders() {
        tableModel.setRowCount(0); // clear existing rows

        String sql = "SELECT id, customer_name, food_item, quantity, total_price, status, order_date "
                   + "FROM food_orders ORDER BY id DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("customer_name"),
                        rs.getString("food_item"),
                        rs.getInt("quantity"),
                        String.format("%.2f", rs.getDouble("total_price")),
                        rs.getString("status"),
                        rs.getDate("order_date")
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Failed to load orders:\n" + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Validates form and inserts a new row into food_orders. */
    private void addOrder() {
        // Read field values
        String customer = customerNameField.getText().trim();
        String food     = foodItemField.getText().trim();
        String qtyStr   = quantityField.getText().trim();
        String priceStr = priceField.getText().trim();
        String status   = (String) statusCombo.getSelectedItem();

        // Basic validation
        if (customer.isEmpty() || food.isEmpty() || qtyStr.isEmpty() || priceStr.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill in all fields before adding an order.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int qty;
        double unitPrice;
        try {
            qty       = Integer.parseInt(qtyStr);
            unitPrice = Double.parseDouble(priceStr);
            if (qty <= 0 || unitPrice <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Quantity and Price must be positive numbers.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double totalPrice = qty * unitPrice;

        String sql = "INSERT INTO food_orders (customer_name, food_item, quantity, total_price, status, order_date) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, customer);
            ps.setString(2, food);
            ps.setInt(3, qty);
            ps.setDouble(4, totalPrice);
            ps.setString(5, status);
            ps.setDate(6, java.sql.Date.valueOf(LocalDate.now()));

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this,
                    "Order added successfully! Total: ₹" + String.format("%.2f", totalPrice),
                    "Success", JOptionPane.INFORMATION_MESSAGE);

            clearForm();
            loadOrders();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Failed to add order:\n" + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Deletes the selected row from food_orders. */
    private void deleteOrder() {
        int selectedRow = ordersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a row in the table to delete.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String customerName = (String) tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete order #" + id + " for \"" + customerName + "\"?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) return;

        String sql = "DELETE FROM food_orders WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this,
                    "Order #" + id + " deleted successfully.",
                    "Deleted", JOptionPane.INFORMATION_MESSAGE);
            loadOrders();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Failed to delete order:\n" + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Clears all form input fields. */
    private void clearForm() {
        customerNameField.setText("");
        foodItemField.setText("");
        quantityField.setText("");
        priceField.setText("");
        statusCombo.setSelectedIndex(0);
    }
}
