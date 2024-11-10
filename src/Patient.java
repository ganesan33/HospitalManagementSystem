public class Patient {
    private int patientId;
    private String name;
    private int age;
    private String gender;
    private String contactNumber;
    private String address;
    private String symptoms;
    private double admissionFee;
    private String admissionDate;
    private String dischargeDate;

    public Patient(String name, int age, String gender, String contactNumber, String address, String symptoms, double admissionFee, String admissionDate) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.contactNumber = contactNumber;
        this.address = address;
        this.symptoms = symptoms;
        this.admissionFee = admissionFee;
        this.admissionDate = admissionDate;
    }

    // Getters and Setters
    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public double getAdmissionFee() {
        return admissionFee;
    }

    public String getAdmissionDate() {
        return admissionDate;
    }

    public String getDischargeDate() {
        return dischargeDate;
    }

    public void setDischargeDate(String dischargeDate) {
        this.dischargeDate = dischargeDate;
    }
}

