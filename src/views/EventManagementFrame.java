package views;

import controllers.EventController;
import models.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.table.JTableHeader;
import javax.swing.RowFilter;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class EventManagementFrame extends JFrame {
    private JTable eventsTable;
    private DefaultTableModel tableModel;
    private User loggedInUser;
    private JTextField searchField;
    private JComboBox<String> filterComboBox;

    public EventManagementFrame(User user) {
        this.loggedInUser = user;
        setTitle("My Events");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        try {
            // Main panel with modern styling
            JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
            mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
            mainPanel.setBackground(new Color(250, 250, 252));

            // Header panel with title and search
            JPanel headerPanel = new JPanel(new BorderLayout(20, 0));
            headerPanel.setBackground(new Color(250, 250, 252));
            
            // Title with modern styling
            JLabel titleLabel = new JLabel("My Booked Events");
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
            titleLabel.setForeground(new Color(33, 33, 33));
            headerPanel.add(titleLabel, BorderLayout.WEST);

            // Search and filter panel
            JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
            searchPanel.setBackground(new Color(250, 250, 252));

            // Search field with modern styling
            searchField = new JTextField(20);
            searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
            ));
            searchField.setBackground(Color.WHITE);
            searchField.setForeground(new Color(33, 33, 33));
            searchField.setCaretColor(new Color(0, 120, 212));
            searchPanel.add(searchField);

            // Filter dropdown with modern styling
            String[] filterOptions = {"All Events", "Today", "This Week", "This Month"};
            filterComboBox = new JComboBox<>(filterOptions);
            filterComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            filterComboBox.setBackground(Color.WHITE);
            filterComboBox.setForeground(new Color(33, 33, 33));
            filterComboBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
            ));
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
            eventsTable = new JTable(tableModel);
            eventsTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            eventsTable.setRowHeight(45);
            eventsTable.setShowGrid(false);
            eventsTable.setIntercellSpacing(new Dimension(0, 0));
            eventsTable.setSelectionBackground(new Color(230, 240, 255));
            eventsTable.setSelectionForeground(new Color(33, 33, 33));

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
            JScrollPane scrollPane = new JScrollPane(eventsTable);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.getViewport().setBackground(Color.WHITE);
            mainPanel.add(scrollPane, BorderLayout.CENTER);

            // Button panel with modern styling
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.setBackground(new Color(250, 250, 252));

            // Refresh button with modern styling
            JButton refreshButton = new JButton("Refresh");
            refreshButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
            refreshButton.setBackground(new Color(0, 120, 212));
            refreshButton.setForeground(Color.WHITE);
            refreshButton.setFocusPainted(false);
            refreshButton.setBorderPainted(false);
            refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            refreshButton.setPreferredSize(new Dimension(120, 40));
            refreshButton.addActionListener(e -> refreshEvents());
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

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error initializing My Events: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void filter() {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        eventsTable.setRowSorter(sorter);
        
        String searchText = searchField.getText().toLowerCase();
        String filterOption = (String) filterComboBox.getSelectedItem();
        
        List<RowFilter<DefaultTableModel, Object>> filters = new ArrayList<>();
        filters.add(RowFilter.regexFilter("(?i)" + searchText));
        
        if (!filterOption.equals("All Events")) {
            filters.add(new RowFilter<DefaultTableModel, Object>() {
                @Override
                public boolean include(Entry<? extends DefaultTableModel, ? extends Object> entry) {
                    String date = (String) entry.getValue(2); // Date column
                    // Implement date filtering logic here
                    return true;
                }
            });
        }
        
        sorter.setRowFilter(RowFilter.andFilter(filters));
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

    class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setFont(new Font("Segoe UI", Font.BOLD, 12));
            setBackground(new Color(220, 53, 69));
            setForeground(Color.WHITE);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
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
            button = new JButton();
            button.setOpaque(true);
            button.setFont(new Font("Segoe UI", Font.BOLD, 12));
            button.setBackground(new Color(220, 53, 69));
            button.setForeground(Color.WHITE);
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
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
                    int row = eventsTable.getSelectedRow();
                    if (row != -1) {
                        // Convert row index from view to model
                        int modelRow = eventsTable.convertRowIndexToModel(row);
                        String bookingIdStr = tableModel.getValueAt(modelRow, 0).toString();
                        int bookingId = Integer.parseInt(bookingIdStr);
                        
                        int confirm = JOptionPane.showConfirmDialog(
                            EventManagementFrame.this,
                            "Are you sure you want to cancel this event?",
                            "Confirm Cancellation",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE
                        );
                        
                        if (confirm == JOptionPane.YES_OPTION) {
                            if (EventController.cancelBooking(loggedInUser.getId(), bookingId)) {
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
}
