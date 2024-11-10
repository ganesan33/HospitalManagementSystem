import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class HospitalManagementSystemGUI extends JFrame {
    private Connection connection;
    private JTextArea textArea;
    private JTextField nameField, ageField, genderField, contactField, addressField, symptomsField, admissionFeeField, admissionDateField, dischargeField, roomField;
    private JButton addPatientButton, updatePatientButton, showAllPatientsButton, showAllDoctorsButton, bookAppointmentButton, showAllAppointmentsButton, assignRoomButton, showAllRoomsButton;
    private JButton generateBillButton, viewBillHistoryButton, makePaymentButton;
    private JPanel billingPanel;
    public HospitalManagementSystemGUI() {
        try {
            connection = DatabaseConnection.getConnection();
            System.out.println("Database connected successfully!");
        } catch (Exception e) {
            System.out.println("Database connection error: " + e.getMessage());
        }

        setTitle("Hospital Management System");
        setSize(800, 800);  // Increased size to accommodate all components
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Create main panel with vertical BoxLayout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Patient Information Panel
        JPanel patientInfoPanel = new JPanel(new GridLayout(9, 2, 5, 5));
        patientInfoPanel.setBorder(BorderFactory.createTitledBorder("Patient Information"));

        // Initialize text fields (keep your existing initializations)
        nameField = new JTextField();
        ageField = new JTextField();
        genderField = new JTextField();
        contactField = new JTextField();
        addressField = new JTextField();
        symptomsField = new JTextField();
        admissionFeeField = new JTextField();
        admissionDateField = new JTextField();
        dischargeField = new JTextField();

        // Add components to patient info panel
        patientInfoPanel.add(new JLabel("Name: "));
        patientInfoPanel.add(nameField);
        patientInfoPanel.add(new JLabel("Age: "));
        patientInfoPanel.add(ageField);
        patientInfoPanel.add(new JLabel("Gender: "));
        patientInfoPanel.add(genderField);
        patientInfoPanel.add(new JLabel("Contact Number: "));
        patientInfoPanel.add(contactField);
        patientInfoPanel.add(new JLabel("Address: "));
        patientInfoPanel.add(addressField);
        patientInfoPanel.add(new JLabel("Symptoms: "));
        patientInfoPanel.add(symptomsField);
        patientInfoPanel.add(new JLabel("Admission Fee: "));
        patientInfoPanel.add(admissionFeeField);
        patientInfoPanel.add(new JLabel("Admission Date (yyyy-MM-dd): "));
        patientInfoPanel.add(admissionDateField);
        patientInfoPanel.add(new JLabel("Discharge Date (yyyy-MM-dd): "));
        patientInfoPanel.add(dischargeField);

        // Patient Action Buttons Panel
        JPanel patientActionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        patientActionPanel.setBorder(BorderFactory.createTitledBorder("Patient Actions"));

        // Initialize buttons
        addPatientButton = new JButton("Add Patient");
        updatePatientButton = new JButton("Update Patient");
        showAllPatientsButton = new JButton("View All Patients");
        showAllDoctorsButton = new JButton("View All Doctors");
        bookAppointmentButton = new JButton("Book Appointment");
        showAllAppointmentsButton = new JButton("View All Appointments");
        assignRoomButton = new JButton("Assign Room");
        showAllRoomsButton = new JButton("View All Rooms");
        generateBillButton = new JButton("Generate Bill");
        viewBillHistoryButton = new JButton("View Bill History");
        makePaymentButton = new JButton("Make Payment");

        // Add buttons to patient action panel
        patientActionPanel.add(addPatientButton);
        patientActionPanel.add(updatePatientButton);
        patientActionPanel.add(showAllPatientsButton);
        patientActionPanel.add(assignRoomButton);

        // Hospital Services Panel
        JPanel servicesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        servicesPanel.setBorder(BorderFactory.createTitledBorder("Hospital Services"));
        servicesPanel.add(showAllDoctorsButton);
        servicesPanel.add(bookAppointmentButton);
        servicesPanel.add(showAllAppointmentsButton);
        servicesPanel.add(showAllRoomsButton);

        // Billing Panel
        JPanel billingPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        billingPanel.setBorder(BorderFactory.createTitledBorder("Billing"));
        billingPanel.add(generateBillButton);
        billingPanel.add(viewBillHistoryButton);
        billingPanel.add(makePaymentButton);

        // Text Area
        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(750, 300));

        // Add all panels to main panel
        mainPanel.add(patientInfoPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(patientActionPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(servicesPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(billingPanel);

        // Add components to frame
        add(mainPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Add action listeners (keep your existing action listeners)
        addPatientButton.addActionListener(e -> addPatient());
        updatePatientButton.addActionListener(e -> updatePatient());
        showAllPatientsButton.addActionListener(e -> showAllPatients());
        showAllDoctorsButton.addActionListener(e -> showAllDoctors());
        bookAppointmentButton.addActionListener(e -> bookAppointment());
        showAllAppointmentsButton.addActionListener(e -> showAllAppointments());
        assignRoomButton.addActionListener(e -> assignRoom());
        showAllRoomsButton.addActionListener(e -> showAllRooms());
        generateBillButton.addActionListener(e -> generateBill());
        viewBillHistoryButton.addActionListener(e -> viewBillHistory());
        makePaymentButton.addActionListener(e -> makePayment());
    }

    private void addPatient() {
        String name = nameField.getText();
        int age;
        try {
            age = Integer.parseInt(ageField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid age value.");
            return;
        }
        String gender = genderField.getText();
        String contact = contactField.getText();
        String address = addressField.getText();
        String symptoms = symptomsField.getText();
        double admissionFee;
        try {
            admissionFee = Double.parseDouble(admissionFeeField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid admission fee value.");
            return;
        }
        String admissionDate = admissionDateField.getText();
        String dischargeDate = dischargeField.getText().isEmpty() ? null : dischargeField.getText();

        String sql = "INSERT INTO patients (name, age, gender, contact_number, address, symptoms, admission_fee, admission_date, discharge_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setInt(2, age);
            stmt.setString(3, gender);
            stmt.setString(4, contact);
            stmt.setString(5, address);
            stmt.setString(6, symptoms);
            stmt.setDouble(7, admissionFee);
            stmt.setString(8, admissionDate);
            stmt.setString(9, dischargeDate);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Patient added successfully!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error adding patient: " + e.getMessage());
        }
    }
    private void updatePatient() {
        // Ask for the patient name
        String patientName = JOptionPane.showInputDialog(this, "Enter patient name to update:");
        if (patientName == null || patientName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Patient name is required.");
            return;
        }

        try {
            // Check if patient exists in the database
            int patientId = getPatientIdByName(patientName);
            if (patientId == -1) {
                JOptionPane.showMessageDialog(this, "Patient not found: " + patientName);
                return;
            }

            // Ask the user for the field to update
            String[] fields = {"Name", "Age", "Gender", "Contact Number", "Address", "Symptoms", "Admission Fee", "Admission Date", "Discharge Date"};
            String fieldToUpdate = (String) JOptionPane.showInputDialog(
                    this,
                    "Select the field to update:",
                    "Update Patient",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    fields,
                    fields[0]
            );

            if (fieldToUpdate == null || fieldToUpdate.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Field selection is required.");
                return;
            }

            // Ask for the new value for the selected field
            String newValue = JOptionPane.showInputDialog(this, "Enter new value for " + fieldToUpdate + ":");
            if (newValue == null || newValue.isEmpty()) {
                JOptionPane.showMessageDialog(this, "New value is required.");
                return;
            }

            // SQL query to update the specific field
            String sql = "UPDATE patients SET " + getDatabaseFieldName(fieldToUpdate) + " = ? WHERE patient_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                // Convert and set the appropriate data type for the field
                switch (fieldToUpdate) {
                    case "Age":
                        stmt.setInt(1, Integer.parseInt(newValue));
                        break;
                    case "Admission Fee":
                        stmt.setDouble(1, Double.parseDouble(newValue));
                        break;
                    case "Admission Date":
                    case "Discharge Date":
                        stmt.setDate(1, java.sql.Date.valueOf(newValue));
                        break;
                    default:
                        stmt.setString(1, newValue);
                }
                stmt.setInt(2, patientId);
                int rowsUpdated = stmt.executeUpdate();

                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(this, "Patient " + fieldToUpdate + " updated successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update patient " + fieldToUpdate + ".");
                }
            }
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error updating patient: " + e.getMessage());
        }
    }

    // Helper method to map GUI fields to database column names
    private String getDatabaseFieldName(String fieldName) {
        switch (fieldName) {
            case "Name": return "name";
            case "Age": return "age";
            case "Gender": return "gender";
            case "Contact Number": return "contact_number";
            case "Address": return "address";
            case "Symptoms": return "symptoms";
            case "Admission Fee": return "admission_fee";
            case "Admission Date": return "admission_date";
            case "Discharge Date": return "discharge_date";
            default: throw new IllegalArgumentException("Unknown field: " + fieldName);
        }
    }

    private int getPatientIdByName(String name) throws SQLException {
        String sql = "SELECT patient_id FROM patients WHERE name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("patient_id");
            } else {
                return -1;
            }
        }
    }

    private void showAllPatients() {
        String sql = "SELECT * FROM patients";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            StringBuilder patientsList = new StringBuilder();
            while (rs.next()) {
                patientsList.append("ID: ").append(rs.getInt("patient_id")).append("\n")
                        .append("Name: ").append(rs.getString("name")).append("\n")
                        .append("Age: ").append(rs.getInt("age")).append("\n")
                        .append("Gender: ").append(rs.getString("gender")).append("\n")
                        .append("Contact: ").append(rs.getString("contact_number")).append("\n")
                        .append("Address: ").append(rs.getString("address")).append("\n")
                        .append("Symptoms: ").append(rs.getString("symptoms")).append("\n")
                        .append("Admission Fee: ").append(rs.getDouble("admission_fee")).append("\n")
                        .append("Admission Date: ").append(rs.getDate("admission_date")).append("\n")
                        .append("Discharge Date: ").append(rs.getDate("discharge_date")).append("\n\n");
            }
            textArea.setText(patientsList.toString());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error retrieving patients: " + e.getMessage());
        }
    }

    private void showAllDoctors() {
        String sql = "SELECT * FROM doctors";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            StringBuilder doctorsList = new StringBuilder();
            while (rs.next()) {
                doctorsList.append("ID: ").append(rs.getInt("doctor_id")).append("\n")
                        .append("Name: ").append(rs.getString("name")).append("\n")
                        .append("Specialization: ").append(rs.getString("specialization")).append("\n")
                        .append("Contact: ").append(rs.getString("contact_number")).append("\n")
                        .append("Availability: ").append(rs.getString("availability")).append("\n\n");
            }
            textArea.setText(doctorsList.toString());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error retrieving doctors: " + e.getMessage());
        }
    }
    private boolean isDoctorAvailable(int doctorId, String appointmentDate) throws SQLException {
        String sql = "SELECT availability FROM doctors WHERE doctor_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, doctorId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                // Check if doctor is available
                String availability = rs.getString("availability");
                if (!"Available".equals(availability)) {
                    return false;
                }

                // Check if doctor already has appointment at that time
                sql = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND appointment_date = ?";
                try (PreparedStatement apptStmt = connection.prepareStatement(sql)) {
                    apptStmt.setInt(1, doctorId);
                    apptStmt.setString(2, appointmentDate);
                    ResultSet apptRs = apptStmt.executeQuery();
                    if (apptRs.next()) {
                        return apptRs.getInt(1) == 0;
                    }
                }
            }
        }
        return false;
    }

    // Add this method to check if patient already has a room
    private boolean isPatientAssignedRoom(int patientId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM rooms WHERE patient_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, patientId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    private void bookAppointment() {
        String patientName = JOptionPane.showInputDialog(this, "Enter patient name:");
        if (patientName == null || patientName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Patient name is required.");
            return;
        }

        try {
            int patientId = getPatientIdByName(patientName);
            if (patientId == -1) {
                JOptionPane.showMessageDialog(this, "Patient not found.");
                return;
            }

            String doctorIdStr = JOptionPane.showInputDialog(this, "Enter doctor ID for appointment:");
            if (doctorIdStr == null || doctorIdStr.isEmpty()) {
                return;
            }
            int doctorId = Integer.parseInt(doctorIdStr);

            String date = JOptionPane.showInputDialog(this, "Enter appointment date (yyyy-MM-dd):");
            if (date == null || date.isEmpty()) {
                return;
            }

            // Check doctor availability
            if (!isDoctorAvailable(doctorId, date)) {
                JOptionPane.showMessageDialog(this, "Doctor is not available for the selected date.");
                return;
            }

            String time = JOptionPane.showInputDialog(this, "Enter appointment time (HH:mm):");
            if (time == null || time.isEmpty()) {
                return;
            }

            String sql = "INSERT INTO appointments (patient_id, doctor_id, appointment_date, appointment_time, status) VALUES (?, ?, ?, ?, 'Scheduled')";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, patientId);
                pstmt.setInt(2, doctorId);
                pstmt.setString(3, date);
                pstmt.setString(4, time);
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Appointment booked successfully!");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error booking appointment: " + e.getMessage());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid doctor ID format.");
        }
    }

    private void showAllAppointments() {
        String sql = "SELECT * FROM appointments";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            StringBuilder appointmentsList = new StringBuilder();
            while (rs.next()) {
                appointmentsList.append("Appointment ID: ").append(rs.getInt("appointment_id")).append("\n")
                        .append("Patient ID: ").append(rs.getInt("patient_id")).append("\n")
                        .append("Doctor ID: ").append(rs.getInt("doctor_id")).append("\n")
                        .append("Date: ").append(rs.getDate("appointment_date")).append("\n")
                        .append("Time: ").append(rs.getTime("appointment_time")).append("\n")
                        .append("Status: ").append(rs.getString("status")).append("\n\n");
            }
            textArea.setText(appointmentsList.toString());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error retrieving appointments: " + e.getMessage());
        }
    }
    private void assignRoom() {
        String patientName = JOptionPane.showInputDialog(this, "Enter patient name:");
        if (patientName == null || patientName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Patient name is required.");
            return;
        }

        try {
            int patientId = getPatientIdByName(patientName);
            if (patientId == -1) {
                JOptionPane.showMessageDialog(this, "Patient not found.");
                return;
            }

            // Check if patient already has a room
            if (isPatientAssignedRoom(patientId)) {
                JOptionPane.showMessageDialog(this, "Patient is already assigned to a room.");
                return;
            }

            String roomNumber = JOptionPane.showInputDialog(this, "Enter room number:");
            if (roomNumber == null || roomNumber.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Room number is required.");
                return;
            }

            if (isRoomAvailable(roomNumber)) {
                String sql = "INSERT INTO rooms (patient_id, room_number) VALUES (?, ?)";
                try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                    pstmt.setInt(1, patientId);
                    pstmt.setString(2, roomNumber);
                    pstmt.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Room assigned successfully!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Room is already occupied.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error assigning room: " + e.getMessage());
        }
    }

    private void showAllRooms() {
        String sql = "SELECT p.name, r.room_number FROM patients p JOIN rooms r ON p.patient_id = r.patient_id";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            StringBuilder roomsList = new StringBuilder();
            while (rs.next()) {
                roomsList.append("Patient Name: ").append(rs.getString("name")).append("\n")
                        .append("Room Number: ").append(rs.getString("room_number")).append("\n\n");
            }
            textArea.setText(roomsList.toString());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error retrieving rooms: " + e.getMessage());
        }
    }

    private boolean isRoomAvailable(String roomNumber) throws SQLException {
        String sql = "SELECT COUNT(*) FROM rooms WHERE room_number = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, roomNumber);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
        }
        return false;
    }
    private void generateBill() {
        String patientName = JOptionPane.showInputDialog(this, "Enter patient name:");
        if (patientName == null || patientName.isEmpty()) {
            return;
        }

        try {
            int patientId = getPatientIdByName(patientName);
            if (patientId == -1) {
                JOptionPane.showMessageDialog(this, "Patient not found.");
                return;
            }

            // Get room charges and doctor fees for reference
            double roomCharges = calculateRoomCharges(patientId);
            double doctorFees = calculateDoctorFees(patientId);

            // Show the calculated charges as reference
            JOptionPane.showMessageDialog(this,
                    "Calculated Charges (For Reference):\n" +
                            "Room Charges: $" + roomCharges + "\n" +
                            "Doctor Fees: $" + doctorFees + "\n" +
                            "Total Calculated: $" + (roomCharges + doctorFees));

            // Ask for manual entry of charges
            String roomChargesStr = JOptionPane.showInputDialog(this, "Enter room charges amount:");
            String doctorFeesStr = JOptionPane.showInputDialog(this, "Enter doctor fees amount:");

            if (roomChargesStr == null || doctorFeesStr == null) {
                return;
            }

            double manualRoomCharges = Double.parseDouble(roomChargesStr);
            double manualDoctorFees = Double.parseDouble(doctorFeesStr);
            double totalAmount = manualRoomCharges + manualDoctorFees;

            // Create bill in database
            String sql = "INSERT INTO billing (patient_id, total_amount, paid_amount, balance_amount, bill_date, payment_status) VALUES (?, ?, 0, ?, CURDATE(), 'PENDING')";
            try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setInt(1, patientId);
                pstmt.setDouble(2, totalAmount);
                pstmt.setDouble(3, totalAmount);
                pstmt.executeUpdate();

                // Get the generated bill_id
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    int billId = rs.getInt(1);
                    // Add billing items with manual amounts
                    addBillingItem(billId, "Room Charges", manualRoomCharges);
                    addBillingItem(billId, "Doctor Fees", manualDoctorFees);
                }
            }

            JOptionPane.showMessageDialog(this, "Bill generated successfully!\nTotal Amount: $" + totalAmount);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error generating bill: " + e.getMessage());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid amount format. Please enter valid numbers.");
        }
    }

    private void addBillingItem(int billId, String description, double amount) throws SQLException {
        String sql = "INSERT INTO billing_items (bill_id, item_type, description, quantity, unit_price, total_price, date_added) VALUES (?, ?, ?, 1, ?, ?, CURDATE())";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, billId);
            pstmt.setString(2, description);
            pstmt.setString(3, description);
            pstmt.setDouble(4, amount);
            pstmt.setDouble(5, amount);
            pstmt.executeUpdate();
        }
    }

    private double calculateRoomCharges(int patientId) throws SQLException {
        // Get room details and calculate charges
        String sql = "SELECT DATEDIFF(COALESCE(discharge_date, CURDATE()), admission_date) as days FROM patients WHERE patient_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, patientId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int days = rs.getInt("days");
                // Assuming room rate is $100 per day
                return days * 100.0;
            }
        }
        return 0.0;
    }

    private double calculateDoctorFees(int patientId) throws SQLException {
        // Calculate total doctor fees from appointments
        String sql = "SELECT COUNT(*) as visits FROM appointments WHERE patient_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, patientId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int visits = rs.getInt("visits");
                // Assuming doctor fee is $50 per visit
                return visits * 50.0;
            }
        }
        return 0.0;
    }

    private void viewBillHistory() {
        String patientName = JOptionPane.showInputDialog(this, "Enter patient name:");
        if (patientName == null || patientName.isEmpty()) {
            return;
        }

        try {
            int patientId = getPatientIdByName(patientName);
            if (patientId == -1) {
                JOptionPane.showMessageDialog(this, "Patient not found.");
                return;
            }

            String sql = "SELECT b.*, p.name FROM billing b JOIN patients p ON b.patient_id = p.patient_id WHERE b.patient_id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, patientId);
                ResultSet rs = pstmt.executeQuery();

                StringBuilder billHistory = new StringBuilder();
                billHistory.append("Bill History for Patient: ").append(patientName).append("\n\n");

                while (rs.next()) {
                    billHistory.append("Bill ID: ").append(rs.getInt("bill_id")).append("\n")
                            .append("Date: ").append(rs.getDate("bill_date")).append("\n")
                            .append("Total Amount: $").append(rs.getDouble("total_amount")).append("\n")
                            .append("Paid Amount: $").append(rs.getDouble("paid_amount")).append("\n")
                            .append("Balance: $").append(rs.getDouble("balance_amount")).append("\n")
                            .append("Status: ").append(rs.getString("payment_status")).append("\n\n");
                }

                textArea.setText(billHistory.toString());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error retrieving bill history: " + e.getMessage());
        }
    }
    private void makePayment() {
        String billIdStr = JOptionPane.showInputDialog(this, "Enter Bill ID:");
        if (billIdStr == null || billIdStr.isEmpty()) {
            return;
        }

        try {
            int billId = Integer.parseInt(billIdStr);

            // First get the current bill details
            String checkSql = "SELECT total_amount, paid_amount FROM billing WHERE bill_id = ?";
            double totalAmount = 0;
            double alreadyPaid = 0;

            try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
                checkStmt.setInt(1, billId);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    totalAmount = rs.getDouble("total_amount");
                    alreadyPaid = rs.getDouble("paid_amount");
                } else {
                    JOptionPane.showMessageDialog(this, "Bill not found.");
                    return;
                }
            }

            String amountStr = JOptionPane.showInputDialog(this, "Enter payment amount:");
            if (amountStr == null || amountStr.isEmpty()) {
                return;
            }

            double amount = Double.parseDouble(amountStr);
            String[] paymentMethods = {"Cash", "Credit Card", "Debit Card", "Insurance"};
            String paymentMethod = (String) JOptionPane.showInputDialog(
                    this,
                    "Select payment method:",
                    "Payment Method",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    paymentMethods,
                    paymentMethods[0]
            );

            if (paymentMethod != null) {
                // Check if payment would exceed the total amount
                double totalAfterPayment = alreadyPaid + amount;
                if (totalAfterPayment > totalAmount) {
                    int response = JOptionPane.showConfirmDialog(this,
                            "Payment amount exceeds the bill by $" + (totalAfterPayment - totalAmount) +
                                    "\nDo you want to proceed? The excess amount will be recorded as overpayment.",
                            "Payment Exceeds Bill",
                            JOptionPane.YES_NO_OPTION);

                    if (response != JOptionPane.YES_OPTION) {
                        return;
                    }
                }

                // Record payment
                String sql = "INSERT INTO payments (bill_id, amount_paid, payment_date, payment_method, payment_status) VALUES (?, ?, CURDATE(), ?, 'COMPLETED')";
                try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                    pstmt.setInt(1, billId);
                    pstmt.setDouble(2, amount);
                    pstmt.setString(3, paymentMethod);
                    pstmt.executeUpdate();

                    // Update billing table
                    sql = "UPDATE billing SET paid_amount = paid_amount + ?, " +
                            "balance_amount = CASE WHEN (paid_amount + ?) > total_amount THEN 0 ELSE (total_amount - (paid_amount + ?)) END, " +
                            "payment_status = CASE WHEN (paid_amount + ?) >= total_amount THEN 'PAID' ELSE 'PARTIAL' END " +
                            "WHERE bill_id = ?";
                    try (PreparedStatement updateStmt = connection.prepareStatement(sql)) {
                        updateStmt.setDouble(1, amount);
                        updateStmt.setDouble(2, amount);
                        updateStmt.setDouble(3, amount);
                        updateStmt.setDouble(4, amount);
                        updateStmt.setInt(5, billId);
                        updateStmt.executeUpdate();
                    }

                    if (totalAfterPayment > totalAmount) {
                        JOptionPane.showMessageDialog(this,
                                "Payment processed successfully!\n" +
                                        "Overpayment amount: $" + (totalAfterPayment - totalAmount));
                    } else {
                        JOptionPane.showMessageDialog(this, "Payment processed successfully!");
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error processing payment: " + e.getMessage());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid number format.");
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new HospitalManagementSystemGUI().setVisible(true);
        });
    }
}
