package ui;

import dao.DBConnection;
import dao.Patient;
import model.PatientModel;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.util.List;

public class PatientPanel extends JPanel {
    private JTextArea display;

    public PatientPanel() {
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(4, 2));
        JTextField nameField = new JTextField();
        JTextField ageField = new JTextField();
        JTextField genderField = new JTextField();

        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Age:"));
        formPanel.add(ageField);
        formPanel.add(new JLabel("Gender:"));
        formPanel.add(genderField);

        JButton addBtn = new JButton("Add Patient");
        JButton viewBtn = new JButton("View Patients");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addBtn);
        buttonPanel.add(viewBtn);

        display = new JTextArea();
        display.setEditable(false);

        add(formPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(new JScrollPane(display), BorderLayout.SOUTH);

        addBtn.addActionListener(e -> {
            try (Connection conn = DBConnection.getConnection()) {
                PatientModel model = new PatientModel(0, nameField.getText(),
                        Integer.parseInt(ageField.getText()), genderField.getText());

                new Patient(conn).addPatient(model);
                JOptionPane.showMessageDialog(this, "Patient added!");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        viewBtn.addActionListener(e -> {
            try (Connection conn = DBConnection.getConnection()) {
                List<PatientModel> list = new Patient(conn).getAllPatients();
                display.setText("");
                for (PatientModel p : list) {
                    display.append(p.getId() + " - " + p.getName() + " - " +
                            p.getAge() + " - " + p.getGender() + "\n");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
}
