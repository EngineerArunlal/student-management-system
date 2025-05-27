import java.time.LocalDate;

public class Course {
    private int courseId;
    private String courseName;
    private String duration;
    private String Department;
    private String instructor;
    private int credit;

    // Getters and Setters
    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public String getDepartment() { return Department; }
    public void setDepartment(String description) { this.Department = Department; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public String getInstructor() { return instructor; }
    public void setInstructor(String instructor) { this.instructor = instructor; }

    public int getCredit() { return credit; }
    public void setCredit(int credit) { this.credit = credit; }


}
