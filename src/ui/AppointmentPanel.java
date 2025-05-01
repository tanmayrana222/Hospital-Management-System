package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AppointmentPanel extends JPanel {

    private final JTextField patientIdField;
    private JTextField doctorIdField;
    private final JTextField dateField;
    private JButton bookButton;
    private JButton cancelButton;

    public AppointmentPanel() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("Book Appointment");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        gbc.gridwidth = 1;

        // Patient ID
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Patient ID:"), gbc);

        gbc.gridx = 1;
        patientIdField = new JTextField(15);
        add(patientIdField, gbc);

        // Doctor ID
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Doctor ID:"), gbc);

        gbc.gridx = 1;
        doctorIdField = new JTextField(15);
        add(doctorIdField, gbc);

        // Date
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Date (YYYY-MM-DD):"), gbc);

        gbc.gridx = 1;
        dateField = new JTextField(15);
        add(dateField, gbc);

        // Buttons
        bookButton = new JButton("Book");
        cancelButton = new JButton("Cancel");

        gbc.gridx = 0;
        gbc.gridy = 4;
        add(bookButton, gbc);

        gbc.gridx = 1;
        add(cancelButton, gbc);

        // Add action listeners
        bookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bookAppointment();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFields();
            }
        });
    }

    private void bookAppointment() {
        String patientId = patientIdField.getText().trim();
        String doctorId = doctorIdField.getText().trim();
        String date = dateField.getText().trim();

        if (patientId.isEmpty() || doctorId.isEmpty() || date.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, "Appointment booked successfully!");
    }

    private void clearFields() {
        patientIdField.setText("");
        doctorIdField.setText("");
        dateField.setText("");
    }
}
