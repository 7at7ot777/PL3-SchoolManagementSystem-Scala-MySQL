import SchoolManagementSystem.connection
import Student.statement

import java.io.{File, FileWriter, PrintWriter}
import java.sql.{PreparedStatement, SQLException, Statement}
import javax.swing.UIManager.getInt
import scala.io.Source
import scala.util.control.Breaks.break

object Teacher {
  private var id: Int = 0

    def index(): Unit = {
      val teachers = "SELECT * FROM teacher"
      val resultSet = statement.executeQuery(teachers)
      println()
      while (resultSet.next()) {
        println(s"id = ${resultSet.getInt("TeacherID")} first name: ${resultSet.getString("FirstName")} ,last name: ${resultSet.getString("LastName")}")
      }
      println()
  }

  def read(id: Int)= {
    val getTeacherWithCoursesQuery =
      """
        |SELECT
        |  Teacher.TeacherID,
        |  Teacher.FirstName AS TeacherFirstName,
        |  Teacher.LastName AS TeacherLastName,
        |  Course.CourseID,
        |  Course.CourseName
        |FROM
        |  Teacher
        |JOIN Course ON Teacher.TeacherID = Course.TeacherID
        |WHERE
        |  Teacher.TeacherID = ?;
      """.stripMargin

    val preparedStatement = connection.prepareStatement(getTeacherWithCoursesQuery)

    try {
      // Set value for the prepared statement
      preparedStatement.setInt(1, id)

      // Execute the query
      val resultSet = preparedStatement.executeQuery()

      // Process the result set
      println()
      while (resultSet.next()) {
        println(s"Teacher ID: ${resultSet.getInt("TeacherID")}, Teacher Name: ${resultSet.getString("TeacherFirstName")} ${resultSet.getString("TeacherLastName")}, " +
          s"Course ID: ${resultSet.getInt("CourseID")}, Course Name: ${resultSet.getString("CourseName")}")
      }
      println()
    } catch {
      case e: SQLException =>
        e.printStackTrace()
    } finally {
      // Close resources in the reverse order of their creation
      if (preparedStatement != null) preparedStatement.close()
    }
      }




  def create(firstName: String, lastName: String,courseName:String): Unit = {
    val insertTeacherSQL = "INSERT INTO Teacher (FirstName, LastName) VALUES (?, ?)";
  val teacherStatement = connection.prepareStatement(insertTeacherSQL,Statement.RETURN_GENERATED_KEYS)
      teacherStatement.setString(1, firstName);
      teacherStatement.setString(2, lastName);
      teacherStatement.executeUpdate();

      val generatedKeys = teacherStatement.getGeneratedKeys;
      var teacherId = -1;
      if (generatedKeys.next()) {
        teacherId = generatedKeys.getInt(1);
      }

      // Insert into Course table with the assigned teacherId
      val insertCourseSQL = "INSERT INTO Course (CourseName, TeacherID) VALUES (?, ?)";
    val courseStatement = connection.prepareStatement(insertCourseSQL)
        courseStatement.setString(1, courseName);
        courseStatement.setInt(2, teacherId);
        courseStatement.executeUpdate();




  }


  def destroy(id: Int): Unit = {
    val deleteStudentQuery = "DELETE FROM Student WHERE StudentID = ?"

    val preparedStatement = connection.prepareStatement(deleteStudentQuery)

    // Set value for the prepared statement
    preparedStatement.setInt(1, id)

    // Execute the delete query
    val rowsAffected = preparedStatement.executeUpdate()

    // Check if the deletion was successful
    if (rowsAffected > 0) {
      println("Student deleted successfully!")
    } else {
      println(s"No student found with StudentID: $id")
    }

  }

  def update(id: Int, firstName: String, lastName: String): Unit = {
    val updateStudentQuery = "UPDATE Student SET FirstName = ?, LastName = ? WHERE StudentID = ?"
    var preparedStatement = connection.prepareStatement(updateStudentQuery)
    preparedStatement.setString(1, firstName)
    preparedStatement.setString(2, lastName)
    preparedStatement.setString(3, id.toString)

    val rowsAffected = preparedStatement.executeUpdate()

    // Check if the update was successful
    if (rowsAffected > 0) {
      println("Teacher updated successfully!")
    } else {
      println(s"No teacher found with TeacherID: $id")

    }
  }

  def createNewTeacherWindow(): Unit = {
    println("Please enter teacher first name: ")
    val firstName = scala.io.StdIn.readLine()
    println("Please enter teacher last name : ")
    val lastName = scala.io.StdIn.readLine()
    println("Please enter the taught subject : ")
    val courseName = scala.io.StdIn.readLine()
    this.id += 1
    create(firstName, lastName,courseName)
  }

  def readTeacherWindow(): Unit = {
    println("Please enter teacher id: ")
    val id = scala.io.StdIn.readLine().toInt
   read(id)


  }

  def updateTeacherWindow(): Unit = {
    println("Please enter teacher id: ")
    val id = scala.io.StdIn.readLine().toInt
    println("Please enter teacher first name: ")
    val fname = scala.io.StdIn.readLine()
    println("Please enter teacher last name: ")
    val lsubject = scala.io.StdIn.readLine()
    update(id, fname, lsubject)
  }

  def deleteTeacherWindow(): Unit = {
    println("Please enter teacher id: ")
    val id: Int = scala.io.StdIn.readLine().toInt
    destroy(id)
  }
}
