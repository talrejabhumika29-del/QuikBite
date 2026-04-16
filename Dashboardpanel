package dbnew;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.*;

/**
 * DashboardPanel.java
 * Shows summary stat cards: Total Orders, Pending Orders, Total Revenue.
 * Data is fetched live from the food_orders table.
 */
public class Dashboardpanel extends JPanel {

    // Stat labels (updated on refresh)
    private JLabel totalOrdersValue;
    private JLabel pendingOrdersValue;
    private JLabel totalRevenueValue;

    // Colors
    private static final Color ORANGE       = new Color(255, 111, 0);
    private static final Color DARK_ORANGE  = new Color(220, 85, 0);
    private static final Color WHITE        = Color.WHITE;
    private static final Color LIGHT_GRAY   = new Color(245, 245, 245);
    private static final Color TEXT_DARK    = new Color(40, 40, 40);
    private static final Color TEXT_MUTED   = new Color(120, 120, 120);

    public Dashboardpanel() {
        setBackground(LIGHT_GRAY);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(30, 30, 30, 30));

        // ----- Header -----
        JLabel header = new JLabel("Dashboard Overview");
        header.setFont(new Font("Segoe UI", Font.BOLD, 26));
        header.setForeground(TEXT_DARK);
        header.setBorder(new EmptyBorder(0, 0, 24, 0));
        add(header, BorderLayout.NORTH);

        // ----- Stat Cards Row -----
        JPanel cardsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        cardsPanel.setBackground(LIGHT_GRAY);

        // Create the three stat cards
        JPanel totalCard   = buildCard("Total Orders",   "0", new Color(255, 140, 0));
        JPanel pendingCard = buildCard("Pending Orders", "0", new Color(230, 81, 0));
        JPanel revenueCard = buildCard("Total Revenue",  "₹0.00", new Color(191, 54, 12));

        // Grab the value labels so we can update them later
        totalOrdersValue   = (JLabel) ((JPanel) totalCard.getComponent(1)).getComponent(0);
        pendingOrdersValue = (JLabel) ((JPanel) pendingCard.getComponent(1)).getComponent(0);
        totalRevenueValue  = (JLabel) ((JPanel) revenueCard.getComponent(1)).getComponent(0);

        cardsPanel.add(totalCard);
        cardsPanel.add(pendingCard);
        cardsPanel.add(revenueCard);

        // Wrap cards in a top-aligned container
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(LIGHT_GRAY);
        wrapper.add(cardsPanel, BorderLayout.NORTH);

        // ----- Quick Info Label -----
        JLabel info = new JLabel("<html><br/><span style='color:#888;font-size:12px;'>"
                + "Click <b>Refresh Dashboard</b> on the toolbar or switch tabs to update stats.</span></html>");
        info.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        wrapper.add(info, BorderLayout.CENTER);

        add(wrapper, BorderLayout.CENTER);

        // Load data immediately
        loadStats();
    }

    /**
     * Builds a single stat card panel with a coloured stripe at top.
     */
    private JPanel buildCard(String title, String initialValue, Color accentColor) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(0, 0, 20, 0)
        ));

        // Top colour stripe
        JPanel stripe = new JPanel();
        stripe.setBackground(accentColor);
        stripe.setPreferredSize(new Dimension(0, 7));
        card.add(stripe, BorderLayout.NORTH);

        // Content area
        JPanel content = new JPanel();
        content.setBackground(WHITE);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(18, 20, 0, 20));

        // Value label (big number)
        JLabel valueLabel = new JLabel(initialValue);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        valueLabel.setForeground(accentColor);
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Title label
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLabel.setForeground(TEXT_MUTED);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleLabel.setBorder(new EmptyBorder(6, 0, 0, 0));

        content.add(valueLabel);
        content.add(titleLabel);
        card.add(content, BorderLayout.CENTER);

        return card;
    }

    /**
     * Fetches stats from the database and updates the card labels.
     * Call this whenever you want fresh data.
     */
    public void loadStats() {
        try (Connection conn = DBConnection.getConnection()) {

            // Total orders
            String sqlTotal = "SELECT COUNT(*) FROM food_orders";
            try (PreparedStatement ps = conn.prepareStatement(sqlTotal);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    totalOrdersValue.setText(String.valueOf(rs.getInt(1)));
                }
            }

            // Pending orders
            String sqlPending = "SELECT COUNT(*) FROM food_orders WHERE status = 'Pending'";
            try (PreparedStatement ps = conn.prepareStatement(sqlPending);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    pendingOrdersValue.setText(String.valueOf(rs.getInt(1)));
                }
            }

            // Total revenue
            String sqlRevenue = "SELECT SUM(total_price) FROM food_orders";
            try (PreparedStatement ps = conn.prepareStatement(sqlRevenue);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    double revenue = rs.getDouble(1);
                    totalRevenueValue.setText(String.format("₹%.2f", revenue));
                }
            }

        } catch (SQLException e) {
            // Show a gentle error; stats stay at last known value
            JOptionPane.showMessageDialog(this,
                    "Could not load dashboard stats:\n" + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
