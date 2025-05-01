import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class HospitalManagementSystem extends JFrame {
    private Connection conn;

    public HospitalManagementSystem() {
        setTitle("Hospital Management System - Login");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initial login panel
        JPanel loginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        JLabel titleLabel = new JLabel("Hospital Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        
        JButton connectButton = new JButton("Connect to Database");
        connectButton.setPreferredSize(new Dimension(200, 40));
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        loginPanel.add(titleLabel, gbc);
        
        gbc.gridy = 1;
        loginPanel.add(connectButton, gbc);
        
        add(loginPanel, BorderLayout.CENTER);

        // Add action listener to connect button
        connectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                connectToDatabase();
            }
        });
    }
    
    private void connectToDatabase() {
        try {
            // Step 1: Load JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
    
            // Step 2: Connect to your database
            String url = "jdbc:mysql://localhost:3306/hospital";
            String username = "root";
            String password = "Tanmay123";
    
            conn = DriverManager.getConnection(url, username, password);
    
            JOptionPane.showMessageDialog(this, "Connected to database successfully!");
            
            // Once connected, launch the main application UI
            launchMainApplication();
            
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to connect to database: " + ex.getMessage(), 
                                         "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void launchMainApplication() {
        // Close the current login window
        this.dispose();
        
        // Create a new frame for the main application
        JFrame mainFrame = new JFrame("Hospital Management System");
        mainFrame.setSize(800, 600);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);
        
        // Create a tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Create simple panels
        JPanel patientPanel = createPatientPanel();
        JPanel doctorPanel = createDoctorPanel();
        JPanel appointmentPanel = createAppointmentPanel();
        
        // Add tabs
        tabbedPane.addTab("Patients", patientPanel);
        tabbedPane.addTab("Doctors", doctorPanel);
        tabbedPane.addTab("Appointments", appointmentPanel);
        
        mainFrame.add(tabbedPane, BorderLayout.CENTER);
        mainFrame.setVisible(true);
    }
    
    private JPanel createPatientPanel() {
        // Create a simple patient panel
        final JPanel panel = new JPanel(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridLayout(4, 2));
        final JTextField nameField = new JTextField();
        final JTextField ageField = new JTextField();
        final JTextField genderField = new JTextField();
        
        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Age:"));
        formPanel.add(ageField);
        formPanel.add(new JLabel("Gender:"));
        formPanel.add(genderField);
        
        JButton addButton = new JButton("Add Patient");
        JButton viewButton = new JButton("View Patients");
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(viewButton);
        
        final JTextArea display = new JTextArea(10, 40);
        display.setEditable(false);
        
        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.CENTER);
        panel.add(new JScrollPane(display), BorderLayout.SOUTH);
        
        // Add action listeners
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String name = nameField.getText();
                    int age = Integer.parseInt(ageField.getText());
                    String gender = genderField.getText();
                    
                    // Basic validation
                    if (name.isEmpty() || gender.isEmpty()) {
                        JOptionPane.showMessageDialog(panel, "Please fill in all fields");
                        return;
                    }
                    
                    // Insert into database
                    String sql = "INSERT INTO patients (name, age, gender) VALUES (?, ?, ?)";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, name);
                    stmt.setInt(2, age);
                    stmt.setString(3, gender);
                    
                    int result = stmt.executeUpdate();
                    if (result > 0) {
                        JOptionPane.showMessageDialog(panel, "Patient added successfully!");
                        nameField.setText("");
                        ageField.setText("");
                        genderField.setText("");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(panel, "Age must be a number");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(panel, "Database error: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });
        
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT * FROM patients");
                    
                    display.setText(""); // Clear previous data
                    display.append("ID\tName\tAge\tGender\n");
                    display.append("-------------------------------\n");
                    
                    while (rs.next()) {
                        int id = rs.getInt("id");
                        String name = rs.getString("name");
                        int age = rs.getInt("age");
                        String gender = rs.getString("gender");
                        
                        display.append(id + "\t" + name + "\t" + age + "\t" + gender + "\n");
                    }
                    
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(panel, "Database error: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });
        
        return panel;
    }
    
    private JPanel createDoctorPanel() {
        // Create a simple doctor panel
        final JPanel panel = new JPanel(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridLayout(3, 2));
        final JTextField nameField = new JTextField();
        final JTextField specializationField = new JTextField();
        
        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Specialization:"));
        formPanel.add(specializationField);
        
        JButton addButton = new JButton("Add Doctor");
        JButton viewButton = new JButton("View Doctors");
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(viewButton);
        
        final JTextArea display = new JTextArea(15, 40);
        display.setEditable(false);
        
        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.CENTER);
        panel.add(new JScrollPane(display), BorderLayout.SOUTH);
        
        // Add action listeners
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String name = nameField.getText();
                    String specialization = specializationField.getText();
                    
                    // Basic validation
                    if (name.isEmpty() || specialization.isEmpty()) {
                        JOptionPane.showMessageDialog(panel, "Please fill in all fields");
                        return;
                    }
                    
                    // Insert into database
                    String sql = "INSERT INTO doctors (name, specialization) VALUES (?, ?)";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, name);
                    stmt.setString(2, specialization);
                    
                    int result = stmt.executeUpdate();
                    if (result > 0) {
                        JOptionPane.showMessageDialog(panel, "Doctor added successfully!");
                        nameField.setText("");
                        specializationField.setText("");
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(panel, "Database error: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });
        
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT * FROM doctors");
                    
                    display.setText(""); // Clear previous data
                    display.append("ID\tName\tSpecialization\n");
                    display.append("-------------------------------\n");
                    
                    while (rs.next()) {
                        int id = rs.getInt("id");
                        String name = rs.getString("name");
                        String specialization = rs.getString("specialization");
                        
                        display.append(id + "\t" + name + "\t" + specialization + "\n");
                    }
                    
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(panel, "Database error: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });
        
        return panel;
    }
    
    private JPanel createAppointmentPanel() {
        // Create a simple appointment panel
        final JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        JLabel titleLabel = new JLabel("Book Appointment");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);
        
        gbc.gridwidth = 1;
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Patient ID:"), gbc);
        
        gbc.gridx = 1;
        final JTextField patientIdField = new JTextField(15);
        panel.add(patientIdField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Doctor ID:"), gbc);
        
        gbc.gridx = 1;
        final JTextField doctorIdField = new JTextField(15);
        panel.add(doctorIdField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Date (YYYY-MM-DD):"), gbc);
        
        gbc.gridx = 1;
        final JTextField dateField = new JTextField(15);
        panel.add(dateField, gbc);
        
        JButton bookButton = new JButton("Book Appointment");
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(bookButton, gbc);
        
        // Display area for appointments
        final JTextArea display = new JTextArea(10, 40);
        display.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(display);
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        panel.add(scrollPane, gbc);
        
        // View appointments button
        JButton viewButton = new JButton("View Appointments");
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weighty = 0;
        panel.add(viewButton, gbc);
        
        // Add action listeners
        bookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int patientId = Integer.parseInt(patientIdField.getText());
                    int doctorId = Integer.parseInt(doctorIdField.getText());
                    String date = dateField.getText();
                    
                    // Basic validation
                    if (date.isEmpty()) {
                        JOptionPane.showMessageDialog(panel, "Please fill in all fields");
                        return;
                    }
                    
                    // Check if appointments table exists
                    try {
                        // Try creating the appointments table if it doesn't exist
                        Statement stmt = conn.createStatement();
                        stmt.execute("CREATE TABLE IF NOT EXISTS appointments (" +
                                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                                    "patient_id INT, " +
                                    "doctor_id INT, " +
                                    "appointment_date DATE, " +
                                    "FOREIGN KEY (patient_id) REFERENCES patients(id), " +
                                    "FOREIGN KEY (doctor_id) REFERENCES doctors(id))");
                    } catch (SQLException ex) {
                        // If there's an error creating the table, just log it and continue
                        System.err.println("Note: Could not create appointments table: " + ex.getMessage());
                    }
                    
                    // Insert into database
                    String sql = "INSERT INTO appointments (patient_id, doctor_id, appointment_date) VALUES (?, ?, ?)";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setInt(1, patientId);
                    stmt.setInt(2, doctorId);
                    stmt.setString(3, date);
                    
                    int result = stmt.executeUpdate();
                    if (result > 0) {
                        JOptionPane.showMessageDialog(panel, "Appointment booked successfully!");
                        patientIdField.setText("");
                        doctorIdField.setText("");
                        dateField.setText("");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(panel, "Patient ID and Doctor ID must be numbers");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(panel, "Database error: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });
        
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(
                        "SELECT a.id, p.name AS patient_name, d.name AS doctor_name, a.appointment_date " +
                        "FROM appointments a " +
                        "JOIN patients p ON a.patient_id = p.id " +
                        "JOIN doctors d ON a.doctor_id = d.id"
                    );
                    
                    display.setText(""); // Clear previous data
                    display.append("ID\tPatient\tDoctor\tDate\n");
                    display.append("-------------------------------\n");
                    
                    while (rs.next()) {
                        int id = rs.getInt("id");
                        String patientName = rs.getString("patient_name");
                        String doctorName = rs.getString("doctor_name");
                        String appointmentDate = rs.getString("appointment_date");
                        
                        display.append(id + "\t" + patientName + "\t" + doctorName + "\t" + appointmentDate + "\n");
                    }
                    
                } catch (SQLException ex) {
                    // The table might not exist yet
                    display.setText("No appointments found or appointments table doesn't exist yet.");
                    ex.printStackTrace();
                }
            }
        });
        
        return panel;
    }

    public static void main(String[] args) {
        try {
            // Set the look and feel to the system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new HospitalManagementSystem().setVisible(true);
            }
        });
    }
}