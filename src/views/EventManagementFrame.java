package views;

import controllers.EventController;
import models.User;
import utils.AnimationUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.table.JTableHeader;
import javax.swing.RowFilter;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.awt.geom.RoundRectangle2D;

public class EventManagementFrame extends JFrame {
    private JTable eventsTable;
    private DefaultTableModel tableModel;
    private User loggedInUser;
    private JTextField searchField;
    private JComboBox<String> filterComboBox;
    private JPanel mainPanel;
    private static final Color PRIMARY_COLOR = new Color(0, 120, 212);
    private static final Color SECONDARY_COLOR = new Color(51, 51, 51);
    private static final Color HOVER_COLOR = new Color(0, 100, 200);
    private static final Color SECONDARY_HOVER_COLOR = new Color(70, 70, 70);
    private static final Color DANGER_COLOR = new Color(220, 53, 69);
    private static final Color DANGER_HOVER_COLOR = new Color(200, 35, 51);

    public EventManagementFrame(User user) {
        this.loggedInUser = user;
        setTitle("My Events");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        try {
            // Main panel with rounded corners
            mainPanel = new JPanel(new BorderLayout(20, 20)) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setColor(getBackground());
                    g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
                }
            };
            mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
            mainPanel.setBackground(new Color(250, 250, 252));

            // Header panel with title and search
            JPanel headerPanel = new JPanel(new BorderLayout(20, 0)) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    super.paintComponent(g);
                }
            };
            headerPanel.setBackground(new Color(250, 250, 252));
            
            // Title with modern styling
            JLabel titleLabel = new JLabel("My Booked Events") {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    super.paintComponent(g);
                }
            };
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
            titleLabel.setForeground(new Color(33, 33, 33));
            headerPanel.add(titleLabel, BorderLayout.WEST);

            // Search and filter panel
            JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0)) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    super.paintComponent(g);
                }
            };
            searchPanel.setBackground(new Color(250, 250, 252));

            // Search field with modern styling
            searchField = createStyledTextField();
            searchPanel.add(searchField);

            // Filter dropdown with modern styling
            String[] filterOptions = {"All Events", "Today", "This Week", "This Month"};
            filterComboBox = createStyledComboBox(filterOptions);
            searchPanel.add(filterComboBox);

            headerPanel.add(searchPanel, BorderLayout.EAST);
            mainPanel.add(headerPanel, BorderLayout.NORTH);

            // Table with modern styling
            String[] columnNames = {"ID", "Event Name", "Date", "Time", "Location", "Actions"};
            tableModel = new DefaultTableModel(columnNames, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return column == 5;
                }
            };
            eventsTable = createStyledTable(tableModel);
            
            // Table header styling
            JTableHeader header = eventsTable.getTableHeader();
            header.setFont(new Font("Segoe UI", Font.BOLD, 14));
            header.setBackground(new Color(240, 240, 240));
            header.setForeground(new Color(33, 33, 33));
            header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)));

            // Add cancel button to each row
            eventsTable.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
            eventsTable.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JCheckBox()));

            // Scroll pane with modern styling
            JScrollPane scrollPane = createStyledScrollPane(eventsTable);
            mainPanel.add(scrollPane, BorderLayout.CENTER);

            // Button panel with modern styling
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    super.paintComponent(g);
                }
            };
            buttonPanel.setBackground(new Color(250, 250, 252));

            // Back button with animation
            JButton backButton = createStyledButton("Back", SECONDARY_COLOR, SECONDARY_HOVER_COLOR);
            backButton.setPreferredSize(new Dimension(120, 40));
            backButton.addActionListener(e -> {
                AnimationUtils.addSlideAnimation(mainPanel, mainPanel.getX(), -mainPanel.getWidth(), 300);
                Timer timer = new Timer(300, evt -> {
                    dispose();
                    new EventFrame(loggedInUser).setVisible(true);
                });
                timer.setRepeats(false);
                timer.start();
            });
            AnimationUtils.addClickAnimation(backButton);
            buttonPanel.add(backButton);

            // Refresh button with animation
            JButton refreshButton = createStyledButton("Refresh", PRIMARY_COLOR, HOVER_COLOR);
            refreshButton.setPreferredSize(new Dimension(120, 40));
            refreshButton.addActionListener(e -> refreshEvents());
            AnimationUtils.addClickAnimation(refreshButton);
            buttonPanel.add(refreshButton);

            mainPanel.add(buttonPanel, BorderLayout.SOUTH);

            add(mainPanel);
            refreshEvents();

            // Add search functionality
            searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                public void changedUpdate(javax.swing.event.DocumentEvent e) { filter(); }
                public void removeUpdate(javax.swing.event.DocumentEvent e) { filter(); }
                public void insertUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            });

            // Add filter functionality
            filterComboBox.addActionListener(e -> filter());

            // Initial animation
            mainPanel.setLocation(-mainPanel.getWidth(), mainPanel.getY());
            AnimationUtils.addSlideAnimation(mainPanel, -mainPanel.getWidth(), 0, 500);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error initializing My Events: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField(20) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                super.paintComponent(g);
            }
        };
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        field.setBackground(Color.WHITE);
        field.setForeground(new Color(33, 33, 33));
        field.setCaretColor(PRIMARY_COLOR);
        return field;
    }

    private JComboBox<String> createStyledComboBox(String[] items) {
        JComboBox<String> comboBox = new JComboBox<String>(items) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                super.paintComponent(g);
            }
        };
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboBox.setBackground(Color.WHITE);
        comboBox.setForeground(new Color(33, 33, 33));
        comboBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        return comboBox;
    }

    private JTable createStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                super.paintComponent(g);
            }
        };
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(45);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(230, 240, 255));
        table.setSelectionForeground(new Color(33, 33, 33));
        return table;
    }

    private JScrollPane createStyledScrollPane(JTable table) {
        JScrollPane scrollPane = new JScrollPane(table) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                super.paintComponent(g);
            }
        };
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        return scrollPane;
    }

    private JButton createStyledButton(String text, Color bgColor, Color hoverColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 15, 15));
                super.paintComponent(g);
            }
        };
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        AnimationUtils.addHoverAnimation(button, bgColor, hoverColor);
        return button;
    }

    class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setFont(new Font("Segoe UI", Font.BOLD, 12));
            setBackground(DANGER_COLOR);
            setForeground(Color.WHITE);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            AnimationUtils.addHoverAnimation(this, DANGER_COLOR, DANGER_HOVER_COLOR);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setColor(getBackground());
                    g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 15, 15));
                    super.paintComponent(g);
                }
            };
            button.setOpaque(true);
            button.setFont(new Font("Segoe UI", Font.BOLD, 12));
            button.setBackground(DANGER_COLOR);
            button.setForeground(Color.WHITE);
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            AnimationUtils.addHoverAnimation(button, DANGER_COLOR, DANGER_HOVER_COLOR);
            
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                try {
                    int selectedRow = eventsTable.getSelectedRow();
                    String eventId = tableModel.getValueAt(selectedRow, 0).toString();
                    System.out.println("Attempting to cancel event with ID: " + eventId);
                    String eventName = tableModel.getValueAt(selectedRow, 1).toString();
                    
                    int confirm = JOptionPane.showConfirmDialog(
                        EventManagementFrame.this,
                        "Are you sure you want to cancel the event: " + eventName + "?",
                        "Confirm Cancellation",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                    );
                    
                    if (confirm == JOptionPane.YES_OPTION) {
                        boolean success = EventController.cancelEvent(loggedInUser.getId(), eventId);
                        if (success) {
                            JOptionPane.showMessageDialog(
                                EventManagementFrame.this,
                                "Event cancelled successfully!",
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE
                            );
                            refreshEvents();
                        } else {
                            JOptionPane.showMessageDialog(
                                EventManagementFrame.this,
                                "Failed to cancel event. Please try again.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                            );
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(
                        EventManagementFrame.this,
                        "Error during cancellation: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
            isPushed = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    }

    private void refreshEvents() {
        try {
            tableModel.setRowCount(0);
            List<Map<String, String>> bookedEvents = EventController.getBookedEvents(loggedInUser.getId());
            
            for (Map<String, String> event : bookedEvents) {
                Object[] row = {
                    event.get("id"),
                    event.get("name"),
                    event.get("date"),
                    event.get("time"),
                    event.get("location"),
                    "Cancel"
                };
                tableModel.addRow(row);
            }
            
            // Hide the ID column
            if (eventsTable.getColumnCount() > 0) {
                eventsTable.getColumnModel().getColumn(0).setMinWidth(0);
                eventsTable.getColumnModel().getColumn(0).setMaxWidth(0);
                eventsTable.getColumnModel().getColumn(0).setWidth(0);
                
                // Set column widths for visible columns
                if (eventsTable.getColumnCount() > 5) {
                    eventsTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Event Name
                    eventsTable.getColumnModel().getColumn(2).setPreferredWidth(120); // Date
                    eventsTable.getColumnModel().getColumn(3).setPreferredWidth(100); // Time
                    eventsTable.getColumnModel().getColumn(4).setPreferredWidth(200); // Location
                    eventsTable.getColumnModel().getColumn(5).setPreferredWidth(100); // Cancel
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error refreshing events: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void filter() {
        String searchText = searchField.getText().toLowerCase();
        String filterOption = (String) filterComboBox.getSelectedItem();
        
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        eventsTable.setRowSorter(sorter);
        
        List<RowFilter<Object, Object>> filters = new ArrayList<>();
        
        // Search filter
        if (!searchText.isEmpty()) {
            filters.add(RowFilter.regexFilter("(?i)" + searchText, 1)); // Search in Event Name column
        }
        
        // Date filter
        if (!"All Events".equals(filterOption)) {
            // Add date filtering logic here
            // This is a placeholder - you'll need to implement the actual date filtering
        }
        
        if (!filters.isEmpty()) {
            sorter.setRowFilter(RowFilter.andFilter(filters));
        } else {
            sorter.setRowFilter(null);
        }
    }
}
