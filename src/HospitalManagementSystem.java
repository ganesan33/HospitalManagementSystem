
import java.sql.*;
import java.util.Scanner;

public class HospitalManagementSystem {
    private Connection connection;
    private Scanner scanner;

    public HospitalManagementSystem() {
        scanner = new Scanner(System.in);
        try {
            connection = DatabaseConnection.getConnection();
            System.out.println("Database connected successfully!");
        } catch (Exception e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
    }

    public void addPatient() {
        System.out.print("Enter patient name: ");
        String name = scanner.nextLine();
        System.out.print("Enter age: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // Clear buffer
        System.out.print("Enter gender: ");
        String gender = scanner.nextLine();
        System.out.print("Enter contact number: ");
        String contact = scanner.nextLine();
        System.out.print("Enter address: ");
        String address = scanner.nextLine();
        System.out.print("Enter symptoms: ");
        String symptoms = scanner.nextLine();
        System.out.print("Enter admission fee: ");
        double admissionFee = scanner.nextDouble();
        scanner.nextLine(); // Clear buffer
        System.out.print("Enter admission date (yyyy-MM-dd): ");
        String admissionDate = scanner.nextLine();

        String sql = "INSERT INTO patients (name, age, gender, contact_number, address, symptoms, admission_fee, admission_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setInt(2, age);
            stmt.setString(3, gender);
            stmt.setString(4, contact);
            stmt.setString(5, address);
            stmt.setString(6, symptoms);
            stmt.setDouble(7, admissionFee);
            stmt.setString(8, admissionDate);
            stmt.executeUpdate();
            System.out.println("Patient added successfully!");
        } catch (SQLException e) {
            System.out.println("Error adding patient: " + e.getMessage());
        }
    }

    public void showAllPatients() {
        String sql = "SELECT * FROM patients";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("Patient Information:");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("patient_id"));
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("Age: " + rs.getInt("age"));
                System.out.println("Gender: " + rs.getString("gender"));
                System.out.println("Contact: " + rs.getString("contact_number"));
                System.out.println("Address: " + rs.getString("address"));
                System.out.println("Symptoms: " + rs.getString("symptoms"));
                System.out.println("Admission Fee: " + rs.getDouble("admission_fee"));
                System.out.println("Admission Date: " + rs.getDate("admission_date"));
                System.out.println("Discharge Date: " + rs.getDate("discharge_date"));
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving patients: " + e.getMessage());
        }
    }

    public void showAllDoctors() {
        String sql = "SELECT * FROM doctors";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("Doctor Information:");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("doctor_id"));
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("Specialization: " + rs.getString("specialization"));
                System.out.println("Contact: " + rs.getString("contact_number"));
                System.out.println("Availability: " + rs.getString("availability"));
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving doctors: " + e.getMessage());
        }
    }

    public void bookAppointment() {
        try {
            showAllDoctors();
            System.out.print("\nEnter patient name: ");
            String patientName = scanner.nextLine();
            System.out.print("Enter doctor ID: ");
            int doctorId = scanner.nextInt();
            scanner.nextLine(); // Clear buffer
            System.out.print("Enter date (yyyy-MM-dd): ");
            String date = scanner.nextLine();
            System.out.print("Enter time (HH:mm): ");
            String time = scanner.nextLine();

            int patientId = getPatientIdByName(patientName);

            String sql = "INSERT INTO appointments (patient_id, doctor_id, appointment_date, appointment_time, status) VALUES (?, ?, ?, ?, 'Scheduled')";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, patientId);
                pstmt.setInt(2, doctorId);
                pstmt.setString(3, date);
                pstmt.setString(4, time);
                pstmt.executeUpdate();
                System.out.println("Appointment booked successfully!");
            }
        } catch (SQLException e) {
            System.out.println("Error booking appointment: " + e.getMessage());
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
                throw new SQLException("Patient not found: " + name);
            }
        }
    }

    public static void main(String[] args) {
        HospitalManagementSystem hms = new HospitalManagementSystem();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nHospital Management System");
            System.out.println("1. Add Patient");
            System.out.println("2. View All Patients");
            System.out.println("3. View All Doctors");
            System.out.println("4. Book Appointment");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Clear buffer

            switch (choice) {
                case 1:
                    hms.addPatient();
                    break;
                case 2:
                    hms.showAllPatients();
                    break;
                case 3:
                    hms.showAllDoctors();
                    break;
                case 4:
                    hms.bookAppointment();
                    break;
                case 5:
                    System.out.println("Thank you for using the system!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}