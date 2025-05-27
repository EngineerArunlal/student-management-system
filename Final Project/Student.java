public class Student {
    private int id;
    private String firstName;
    private String lastName;
    private java.sql.Date dob;
    private String studentClass;
    private String section;
    private String phone;
    private String address;

    // Constructors
    public Student() {}

    public Student(int id, String firstName, String lastName, java.sql.Date dob, String studentClass, String section, String phone, String address) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.studentClass = studentClass;
        this.section = section;
        this.phone = phone;
        this.address = address;
    }

    // Getters and setters
    // ... (generate via IDE or write manually)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public java.sql.Date getDob() { return dob; }
    public void setDob(java.sql.Date dob) { this.dob = dob; }

    public String getStudentClass() { return studentClass; }
    public void setStudentClass(String studentClass) { this.studentClass = studentClass; }

    public String getSection() { return section; }
    public void setSection(String section) { this.section = section; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

}
