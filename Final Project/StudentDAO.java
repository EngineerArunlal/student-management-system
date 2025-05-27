import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    public void addStudent(Student student) throws SQLException {
        String sql = "INSERT INTO students (first_name, last_name, dob, class, section, phone, address) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, student.getFirstName());
            stmt.setString(2, student.getLastName());
            stmt.setDate(3, student.getDob());
            stmt.setString(4, student.getStudentClass());
            stmt.setString(5, student.getSection());
            stmt.setString(6, student.getPhone());
            stmt.setString(7, student.getAddress());
            stmt.executeUpdate();
        }
    }

    public List<Student> getAllStudents() throws SQLException {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Student s = new Student();
                s.setId(rs.getInt("id"));
                s.setFirstName(rs.getString("first_name"));
                s.setLastName(rs.getString("last_name"));
                s.setDob(rs.getDate("dob"));
                s.setStudentClass(rs.getString("class"));
                s.setSection(rs.getString("section"));
                s.setPhone(rs.getString("phone"));
                s.setAddress(rs.getString("address"));
                students.add(s);
            }
        }
        return students;
    }

    public void updateStudent(Student student) throws SQLException {
        String sql = "UPDATE students SET first_name=?, last_name=?, dob=?, class=?, section=?, phone=?, address=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, student.getFirstName());
            stmt.setString(2, student.getLastName());
            stmt.setDate(3, student.getDob());
            stmt.setString(4, student.getStudentClass());
            stmt.setString(5, student.getSection());
            stmt.setString(6, student.getPhone());
            stmt.setString(7, student.getAddress());
            stmt.setInt(8, student.getId());
            stmt.executeUpdate();
        }
    }

    public void deleteStudent(int id) throws SQLException {
        String sql = "DELETE FROM students WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // 🔍 Search method
    public List<Student> searchStudents(String searchTerm) throws SQLException {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE " +
                "first_name LIKE ? OR last_name LIKE ? OR class LIKE ? OR section LIKE ? OR phone LIKE ? OR address LIKE ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            String keyword = "%" + searchTerm + "%";
            for (int i = 1; i <= 6; i++) {
                stmt.setString(i, keyword);
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Student s = new Student();
                    s.setId(rs.getInt("id"));
                    s.setFirstName(rs.getString("first_name"));
                    s.setLastName(rs.getString("last_name"));
                    s.setDob(rs.getDate("dob"));
                    s.setStudentClass(rs.getString("class"));
                    s.setSection(rs.getString("section"));
                    s.setPhone(rs.getString("phone"));
                    s.setAddress(rs.getString("address"));
                    students.add(s);
                }
            }
        }
        return students;
    }
}
