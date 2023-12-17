import SchoolManagementSystem.connection
import Student.statement

import java.io.{File, FileWriter, PrintWriter}
import scala.io.Source
import scala.util.control.Breaks.break

object Course {
  private var id: Int = 0

  def index(): Unit = {
    val query =
      """
        |SELECT Course.CourseID, Course.CourseName, Teacher.FirstName, Teacher.LastName
        |FROM Course
        |JOIN Teacher ON Course.TeacherID = Teacher.TeacherID;
        |""".stripMargin

    val resultSet = statement.executeQuery(query)

    while (resultSet.next()) {
      val courseID = resultSet.getInt("CourseID")
      val courseName = resultSet.getString("CourseName")
      val firstName = resultSet.getString("FirstName")
      val lastName = resultSet.getString("LastName")

      println(s"CourseID: $courseID, CourseName: $courseName, Teacher: $firstName $lastName")
    }
  }

  def read(id: Int): Any = {
    val query =
      """
        |SELECT Course.CourseID, Course.CourseName, Teacher.FirstName, Teacher.LastName
        |FROM Course
        |JOIN Teacher ON Course.TeacherID = Teacher.TeacherID
        |WHERE course.CourseID = ?;
        |""".stripMargin

    val preparedStatement = connection.prepareStatement(query)
    preparedStatement.setInt(1, id)

    val resultSet = preparedStatement.executeQuery()

    while (resultSet.next()) {
      val courseID = resultSet.getInt("CourseID")
      val courseName = resultSet.getString("CourseName")
      val firstName = resultSet.getString("FirstName")
      val lastName = resultSet.getString("LastName")

      println(s"CourseID: $courseID, CourseName: $courseName, Teacher: $firstName $lastName")
    }  }

  def create(id: Int, name: String, instructor: String): Unit = {
    val writer = new FileWriter("courses.txt", true)
    writer.write(s"$id,$name,$instructor\n")
    writer.close()
    println("Course Created Successfully")
  }

  def destroy(id: Int): Unit = {
    val disableForeignKeyChecks = connection.prepareStatement("SET foreign_key_checks = 0")

    disableForeignKeyChecks.execute();
    val deleteStudentQuery = "DELETE FROM course WHERE CourseID = ?"

    val preparedStatement = connection.prepareStatement(deleteStudentQuery)

    // Set value for the prepared statement
    preparedStatement.setInt(1, id)

    // Execute the delete query
    val rowsAffected = preparedStatement.executeUpdate()

    // Check if the deletion was successful
    if (rowsAffected > 0) {
      println("Course deleted successfully!")
    } else {
      println(s"No Course found with StudentID: $id")
    }
  }


    def update(id: Int, name: String): Unit = {
      val updateQuery =
        """
          |UPDATE Course
          |SET CourseName = ?
          |WHERE CourseID = ?;
          |""".stripMargin

      val preparedStatement = connection.prepareStatement(updateQuery)
      preparedStatement.setString(1, name)
      preparedStatement.setInt(2, id)

      val rowsUpdated = preparedStatement.executeUpdate()

      if (rowsUpdated > 0) {
        println(s"Course with CourseID $id updated successfully.")
      } else {
        println(s"No course found with CourseID $id.")
      }
  }

  def createNewCourseWindow(): Unit = {
    println("Please enter course name: ")
    val name = scala.io.StdIn.readLine()
    println("Please enter instructor name: ")
    val instructor = scala.io.StdIn.readLine()
    this.id += 1
    create(this.id, name, instructor)
  }

  def readCourseWindow(): Unit = {
    println("Please enter course id: ")
    val id = scala.io.StdIn.readLine().toInt
    read(id)

  }

  def updateCourseWindow(): Unit = {
    println("Please enter course id: ")
    val id = scala.io.StdIn.readLine().toInt
    println("Please enter course name: ")
    val name = scala.io.StdIn.readLine()
    update(id, name)
  }

  def deleteCourseWindow(): Unit = {
    println("Please enter course id: ")
    val id: Int = scala.io.StdIn.readLine().toInt
    destroy(id)
  }
}
