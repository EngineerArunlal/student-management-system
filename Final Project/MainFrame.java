// MainFrame.java
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class MainFrame extends JFrame {
    private final JTable table;
    private final DefaultTableModel tableModel;

    private final JTextField txtId, txtFirstName, txtLastName, txtDOB, txtClass, txtSection, txtPhone, txtAddress, txtSearch;
    private final StudentDAO studentDAO;

    // Color palette
    private final Color mainBg = new Color(187, 222, 251); // Light blue
    private final Color panelBg = new Color(33, 150, 243); // Blue
    private final Color headerBg = new Color(25, 118, 210); // Darker blue
    private final Color btnGreen = new Color(76, 175, 80);
    private final Color btnRed = new Color(244, 67, 54);
    private final Color btnGray = new Color(158, 158, 158);
    private final Color btnOrange = new Color(255, 112, 67);
    private final Color white = Color.WHITE;

    public MainFrame() {
        studentDAO = new StudentDAO();

        // Set the logo as the window icon
        try {
            ImageIcon img = new ImageIcon("Logo.png");
            setIconImage(img.getImage());
        } catch (Exception e) {
            System.out.println("Logo image not found.");
        }

        setTitle("Student Management System");
        setSize(1150, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 0));
        getContentPane().setBackground(mainBg);

        // Title
        JLabel titleLabel = new JLabel("Student Management System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(white);
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(headerBg);
        titlePanel.setBorder(new EmptyBorder(18, 0, 18, 0));
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        add(titlePanel, BorderLayout.NORTH);

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        searchPanel.setOpaque(false);
        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        txtSearch = new JTextField(18);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtSearch.setBorder(new EmptyBorder(8, 12, 8, 12));
        JButton btnSearch = createButton("Search", btnOrange, white);
        searchPanel.add(searchLabel);
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);

        // Table
        String[] columns = {"ID", "First Name", "Last Name", "DOB", "Class", "Section", "Phone", "Address"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel) {
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? white : new Color(225, 245, 254));
                } else {
                    c.setBackground(new Color(144, 202, 249));
                }
                c.setForeground(Color.BLACK);
                return c;
            }
        };
        table.setRowHeight(32);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        table.setGridColor(new Color(200, 200, 200));
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 16));
        header.setBackground(headerBg);
        header.setForeground(white);
        header.setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 10));
        scrollPane.getViewport().setBackground(white);

        // Left panel (search + table)
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setOpaque(false);
        leftPanel.add(searchPanel, BorderLayout.NORTH);
        leftPanel.add(scrollPane, BorderLayout.CENTER);

        // Right panel (form)
        JPanel rightPanel = new RoundedPanel(30, panelBg);
        rightPanel.setPreferredSize(new Dimension(370, 0));
        rightPanel.setLayout(new BorderLayout());
        rightPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JPanel formPanel = new JPanel();
        formPanel.setOpaque(false);
        formPanel.setLayout(new GridLayout(8, 2, 12, 18));

        Font labelFont = new Font("Segoe UI", Font.BOLD, 15);
        Font inputFont = new Font("Segoe UI", Font.PLAIN, 15);

        txtId = createTextField(inputFont, true);
        txtFirstName = createTextField(inputFont, false);
        txtLastName = createTextField(inputFont, false);
        txtDOB = createTextField(inputFont, false);
        txtClass = createTextField(inputFont, false);
        txtSection = createTextField(inputFont, false);
        txtPhone = createTextField(inputFont, false);
        txtAddress = createTextField(inputFont, false);

        addField(formPanel, "ID", txtId, labelFont);
        addField(formPanel, "First Name", txtFirstName, labelFont);
        addField(formPanel, "Last Name", txtLastName, labelFont);
        addField(formPanel, "DOB (yyyy-mm-dd)", txtDOB, labelFont);
        addField(formPanel, "Class", txtClass, labelFont);
        addField(formPanel, "Section", txtSection, labelFont);
        addField(formPanel, "Phone", txtPhone, labelFont);
        addField(formPanel, "Address", txtAddress, labelFont);

        rightPanel.add(formPanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonsPanel = new JPanel(new GridLayout(1, 4, 12, 0));
        buttonsPanel.setOpaque(false);
        JButton btnAdd = createButton("Add", btnGreen, white);
        JButton btnUpdate = createButton("Update", headerBg, white);
        JButton btnDelete = createButton("Delete", btnRed, white);
        JButton btnClear = createButton("Clear", btnGray, white);
        btnAdd.setToolTipText("Add Student");
        btnUpdate.setToolTipText("Update Student");
        btnDelete.setToolTipText("Delete Student");
        btnClear.setToolTipText("Clear Form");

        buttonsPanel.add(btnAdd);
        buttonsPanel.add(btnUpdate);
        buttonsPanel.add(btnDelete);
        buttonsPanel.add(btnClear);

        rightPanel.add(buttonsPanel, BorderLayout.SOUTH);

        add(leftPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);

        // Listeners
        loadStudents();
        btnAdd.addActionListener(e -> addStudent());
        btnUpdate.addActionListener(e -> updateStudent());
        btnDelete.addActionListener(e -> deleteStudent());
        btnClear.addActionListener(e -> clearForm());
        btnSearch.addActionListener(e -> searchStudents());

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    txtId.setText(tableModel.getValueAt(row, 0).toString());
                    txtFirstName.setText(tableModel.getValueAt(row, 1).toString());
                    txtLastName.setText(tableModel.getValueAt(row, 2).toString());
                    txtDOB.setText(tableModel.getValueAt(row, 3).toString());
                    txtClass.setText(tableModel.getValueAt(row, 4).toString());
                    txtSection.setText(tableModel.getValueAt(row, 5).toString());
                    txtPhone.setText(tableModel.getValueAt(row, 6).toString());
                    txtAddress.setText(tableModel.getValueAt(row, 7).toString());
                }
            }
        });
    }

    // Helper to create rounded panel
    static class RoundedPanel extends JPanel {
        private final int cornerRadius;
        private final Color bgColor;
        public RoundedPanel(int radius, Color bgColor) {
            super();
            this.cornerRadius = radius;
            this.bgColor = bgColor;
            setOpaque(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bgColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
        }
    }

    private JTextField createTextField(Font font, boolean disabled) {
        JTextField field = new JTextField();
        field.setFont(font);
        field.setBorder(new EmptyBorder(8, 12, 8, 12));
        field.setBackground(white);
        field.setEnabled(!disabled);
        field.setDisabledTextColor(Color.GRAY);
        return field;
    }

    private JButton createButton(String text, Color bg, Color fg) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 18));
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFocusPainted(false);
        button.setBorder(new RoundedBorder(24, bg));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setMargin(new Insets(14, 32, 14, 32)); // More padding

        // Set a minimum width for all buttons
        button.setPreferredSize(new Dimension(120, 48));

        // Subtle shadow effect
        button.setBorderPainted(false);
        button.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 30));
                g2.fillRoundRect(6, 8, c.getWidth() - 12, c.getHeight() - 8, 24, 24);
                g2.dispose();
                super.paint(g, c);
            }
        });

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bg.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bg);
            }
        });
        return button;
    }

    // Helper for rounded button border
    static class RoundedBorder extends LineBorder {
        private final int radius;
        public RoundedBorder(int radius, Color color) {
            super(color, 1, true);
            this.radius = radius;
        }
        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(this.radius+2, this.radius+2, this.radius+2, this.radius+2);
        }
    }

    private void addField(JPanel panel, String labelText, JTextField field, Font labelFont) {
        JLabel label = new JLabel(labelText);
        label.setFont(labelFont);
        label.setForeground(white);
        panel.add(label);
        panel.add(field);
    }

    private void loadStudents() {
        try {
            List<Student> students = studentDAO.getAllStudents();
            tableModel.setRowCount(0);
            for (Student s : students) {
                tableModel.addRow(new Object[]{
                        s.getId(), s.getFirstName(), s.getLastName(), s.getDob(),
                        s.getStudentClass(), s.getSection(), s.getPhone(), s.getAddress()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading students: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchStudents() {
        String term = txtSearch.getText().trim();
        try {
            List<Student> students = term.isEmpty() ? studentDAO.getAllStudents() : studentDAO.searchStudents(term);
            tableModel.setRowCount(0);
            for (Student s : students) {
                tableModel.addRow(new Object[]{
                        s.getId(), s.getFirstName(), s.getLastName(), s.getDob(),
                        s.getStudentClass(), s.getSection(), s.getPhone(), s.getAddress()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error searching students: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addStudent() {
        Student s = getStudentFromForm();
        if (s != null) {
            try {
                studentDAO.addStudent(s);
                JOptionPane.showMessageDialog(this, "Student added successfully!");
                loadStudents();
                clearForm();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error adding student: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateStudent() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select a student to update.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Student s = getStudentFromForm();
        if (s != null) {
            try {
                s.setId(Integer.parseInt(txtId.getText()));
                studentDAO.updateStudent(s);
                JOptionPane.showMessageDialog(this, "Student updated successfully!");
                loadStudents();
                clearForm();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error updating student: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteStudent() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select a student to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = Integer.parseInt(txtId.getText());
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this student?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                studentDAO.deleteStudent(id);
                JOptionPane.showMessageDialog(this, "Student deleted successfully!");
                loadStudents();
                clearForm();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error deleting student: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        txtId.setText("");
        txtFirstName.setText("");
        txtLastName.setText("");
        txtDOB.setText("");
        txtClass.setText("");
        txtSection.setText("");
        txtPhone.setText("");
        txtAddress.setText("");
    }

    private Student getStudentFromForm() {
        String firstName = txtFirstName.getText().trim();
        String lastName = txtLastName.getText().trim();
        String dobStr = txtDOB.getText().trim();
        String studentClass = txtClass.getText().trim();
        String section = txtSection.getText().trim();
        String phone = txtPhone.getText().trim();
        String address = txtAddress.getText().trim();

        if (firstName.isEmpty() || lastName.isEmpty() || dobStr.isEmpty() || studentClass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        try {
            java.util.Date utilDate = new SimpleDateFormat("yyyy-MM-dd").parse(dobStr);
            java.sql.Date dob = new java.sql.Date(utilDate.getTime());

            Student s = new Student();
            s.setFirstName(firstName);
            s.setLastName(lastName);
            s.setDob(dob);
            s.setStudentClass(studentClass);
            s.setSection(section);
            s.setPhone(phone);
            s.setAddress(address);
            return s;

        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Use yyyy-MM-dd.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
