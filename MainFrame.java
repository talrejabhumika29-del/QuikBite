package dbnew;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * MainFrame.java
 * The main application window.
 * Contains a sidebar for navigation and a content area for panels.
 */
public class MainFrame extends JFrame {

    // Color palette
    private static final Color ORANGE      = new Color(255, 111, 0);
    private static final Color DARK_ORANGE = new Color(200, 70, 0);
    private static final Color SIDEBAR_BG  = new Color(35, 35, 35);
    private static final Color WHITE       = Color.WHITE;
    private static final Color LIGHT_GRAY  = new Color(245, 245, 245);

    // Panels
    private Dashboardpanel dashboardPanel;
    private Orderpanel     orderPanel;

    // Content area (CardLayout)
    private JPanel     contentArea;
    private CardLayout cardLayout;

    public MainFrame() {
        // ---- Window settings ----
        setTitle("QuickBite - Food Order Manager");
        setSize(900, 620);
        setMinimumSize(new Dimension(800, 580));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // centre on screen

        // ---- Root layout: sidebar | content ----
        setLayout(new BorderLayout());

        add(buildTopBar(),  BorderLayout.NORTH);
        add(buildSidebar(), BorderLayout.WEST);
        add(buildContent(), BorderLayout.CENTER);

        setVisible(true);
    }

    // =========================================================
    //  Top bar
    // =========================================================
    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(ORANGE);
        bar.setPreferredSize(new Dimension(0, 54));
        bar.setBorder(new EmptyBorder(0, 20, 0, 20));

        // App logo + name
        JLabel logo = new JLabel("🍔  QuickBite");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        logo.setForeground(WHITE);
        bar.add(logo, BorderLayout.WEST);

        // Subtitle / tagline
        JLabel sub = new JLabel("Food Order Manager");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sub.setForeground(new Color(255, 230, 210));
        bar.add(sub, BorderLayout.EAST);

        return bar;
    }

    // =========================================================
    //  Left sidebar with navigation buttons
    // =========================================================
    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(190, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(20, 0, 20, 0));

        // Navigation label
        JLabel navLabel = new JLabel("  NAVIGATION");
        navLabel.setFont(new Font("Segoe UI", Font.BOLD, 10));
        navLabel.setForeground(new Color(140, 140, 140));
        navLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
        navLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(navLabel);

        // Nav buttons
        JButton dashBtn  = createNavButton("📊  Dashboard");
        JButton orderBtn = createNavButton("📋  Orders");

        // Highlight dashboard by default
        dashBtn.setBackground(ORANGE);
        dashBtn.setForeground(WHITE);

        // Switch to Dashboard
        dashBtn.addActionListener(e -> {
            cardLayout.show(contentArea, "dashboard");
            dashboardPanel.loadStats(); // refresh stats on tab switch
            dashBtn.setBackground(ORANGE);
            dashBtn.setForeground(WHITE);
            orderBtn.setBackground(SIDEBAR_BG);
            orderBtn.setForeground(new Color(200, 200, 200));
        });

        // Switch to Orders
        orderBtn.addActionListener(e -> {
            cardLayout.show(contentArea, "orders");
            orderPanel.loadOrders();  // refresh table on tab switch
            orderBtn.setBackground(ORANGE);
            orderBtn.setForeground(WHITE);
            dashBtn.setBackground(SIDEBAR_BG);
            dashBtn.setForeground(new Color(200, 200, 200));
        });

        sidebar.add(dashBtn);
        sidebar.add(Box.createVerticalStrut(6));
        sidebar.add(orderBtn);
        sidebar.add(Box.createVerticalGlue()); // push remaining space down

        // Bottom version label
        JLabel version = new JLabel("v1.0.0");
        version.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        version.setForeground(new Color(100, 100, 100));
        version.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(version);

        return sidebar;
    }

    /** Creates a styled sidebar navigation button. */
    private JButton createNavButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setBackground(SIDEBAR_BG);
        btn.setForeground(new Color(200, 200, 200));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(190, 44));
        btn.setPreferredSize(new Dimension(190, 44));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(0, 20, 0, 0));
        return btn;
    }

    // =========================================================
    //  Content area (CardLayout)
    // =========================================================
    private JPanel buildContent() {
        cardLayout  = new CardLayout();
        contentArea = new JPanel(cardLayout);
        contentArea.setBackground(LIGHT_GRAY);

        dashboardPanel = new Dashboardpanel();
        orderPanel     = new Orderpanel();

        contentArea.add(dashboardPanel, "dashboard");
        contentArea.add(orderPanel,     "orders");

        // Show dashboard first
        cardLayout.show(contentArea, "dashboard");

        return contentArea;
    }
}
