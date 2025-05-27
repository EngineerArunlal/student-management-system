import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {

    public void addCourse(Course course) throws SQLException {
        String sql = "INSERT INTO courses (course_id, course_name, description, duration, start_date, end_date, instructor) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, course.getCourseId());
            stmt.setString(2, course.getCourseName());
            stmt.setString(3, course.getDepartment());
            stmt.setString(4, course.getDuration());
            stmt.setInt (5, course.getCredit ()); // Assuming LocalDate
            stmt.setString(6, course.getInstructor());

            stmt.executeUpdate();
        }
    }

    public List<Course> getAllCourses() throws SQLException {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM courses";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Course course = new Course();
                course.setCourseId(rs.getInt("course_id"));
                course.setCourseName(rs.getString("course_name"));
                course.setInstructor(rs.getString("instructor"));
                course.setDepartment(rs.getString("description"));
                course.setDuration(rs.getString("duration"));



                courses.add(course);
            }
        }
        return courses;
    }

    public void updateCourse(Course course) throws SQLException {
        String sql = "UPDATE courses SET course_name=?, description=?, duration=?, start_date=?, end_date=?, instructor=? WHERE course_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt ( 1 , course.getCourseId());
            stmt.setString(2, course.getCourseName());
            stmt.setString(3, course.getInstructor());
            stmt.setString ( 4 , course.getDepartment());
            stmt.setInt (5,course.getCredit ());
            stmt.setString(6, course.getDuration());


            stmt.executeUpdate();
        }
    }

    public void deleteCourse(int courseId) throws SQLException {
        String sql = "DELETE FROM courses WHERE course_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, courseId);
            stmt.executeUpdate();
        }
    }
}
