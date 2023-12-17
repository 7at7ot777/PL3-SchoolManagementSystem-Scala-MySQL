import SchoolManagementSystem.studentActor
import akka.actor.{ActorSystem, Props}

import java.io.{File, FileWriter, PrintWriter}
import java.sql.{Connection, Statement}
import scala.collection.mutable.ArrayBuffer
import scala.io.Source
import scala.util.Using
import scala.util.control.Breaks.break

object Student {
  private var id: Int = 0

  var connection: Connection = null
  connection = DatabaseConfig.getConnection
  val statement = connection.createStatement()


  val system = ActorSystem("TeacherSystem")
  val studentActor = system.actorOf(Props[StudentActor], "studentActor")

  def index(): Unit = {
    val getAllStudents = "SELECT * FROM student"
    val resultSet = statement.executeQuery(getAllStudents)
    println()
    while (resultSet.next()) {
      println(s"id = ${resultSet.getInt("StudentID")} first name: ${resultSet.getString("FirstName")} ,last name: ${resultSet.getString("LastName")}")
    }
    println()
  }


  def read(id: Int): Any = {

    val sqlQuery = "SELECT Student.StudentID, Student.FirstName, Student.LastName, " +
      "Course.CourseID AS EnrolledCourseID, Course.CourseName AS EnrolledCourseName, " +
      "Exam.ExamID, Exam.ExamDate, Grade.GradeValue " +
      "FROM Student " +
      "LEFT JOIN Attendance ON Student.StudentID = Attendance.StudentID " +
      "LEFT JOIN Course ON Attendance.CourseID = Course.CourseID " +
      "LEFT JOIN Exam ON Course.CourseID = Exam.CourseID " +
      "LEFT JOIN Grade ON Student.StudentID = Grade.StudentID AND Exam.ExamID = Grade.ExamID " +
      "WHERE Student.StudentID = ?"

    val preparedStatement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)
    preparedStatement.setString(1, id.toString)

    val resultSet = preparedStatement.executeQuery()

    println()
    while (resultSet.next) {
      val studentID = resultSet.getInt("StudentID")
      val firstName = resultSet.getString("FirstName")
      val lastName = resultSet.getString("LastName")
      val enrolledCourseID = resultSet.getInt("EnrolledCourseID")
      val enrolledCourseName = resultSet.getString("EnrolledCourseName")
      val examID = resultSet.getInt("ExamID")
      val examDate = resultSet.getDate("ExamDate")
      val gradeValue = resultSet.getString("GradeValue")
      // Print or process the retrieved data as needed
      println("Student ID: " + studentID)
      println("First Name: " + firstName)
      println("Last Name: " + lastName)
      println()
      println("Enrolled Course ID: " + enrolledCourseID)
      println("Enrolled Course Name: " + enrolledCourseName)
      println()
      println("Exam ID: " + examID)
      println("Exam Date: " + examDate)
      println("Grade: " + gradeValue)
    }
    println()


  }

  def createNewStudentWindow(): Unit = {
    println("please enter first name ")
    var firstName = scala.io.StdIn.readLine()
    println("please enter last name ")
    var lastName = scala.io.StdIn.readLine()
    this.id += 1
    studentActor ! CreateStudent(firstName, lastName)

    //    Student.create(this.id, name, grade)
  }

  def create(firstName: String, lastName: String): Unit = {

    val insertStudentQuery = "INSERT INTO Student (FirstName, LastName) VALUES (?, ?)"
    val preparedStatement = connection.prepareStatement(insertStudentQuery)
    preparedStatement.setString(1, firstName)
    preparedStatement.setString(2, lastName)

    // Execute the query
    val rowsAffected = preparedStatement.executeUpdate

    // Check if the insertion was successful
    if (rowsAffected > 0) System.out.println("New student inserted successfully!")
    else System.out.println("Failed to insert a new student.")
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
      println("Student updated successfully!")
    } else {
      println(s"No student found with StudentID: $id")
    }

  }

  def getAllStudentGrades(studentId: Int): Unit = {
    val selectGradesQuery =
      """
        |SELECT
        |    Student.FirstName,
        |    Student.LastName,
        |    Course.CourseName,
        |    Exam.ExamDate,
        |    Grade.GradeValue
        |FROM
        |    Student
        |JOIN Grade ON Student.StudentID = Grade.StudentID
        |JOIN Exam ON Grade.ExamID = Exam.ExamID
        |JOIN Course ON Exam.CourseID = Course.CourseID
        |WHERE
        |    Student.StudentID = ?;
         """.stripMargin

    val preparedStatement = connection.prepareStatement(selectGradesQuery)

    preparedStatement.setInt(1, studentId)

    val resultSet = preparedStatement.executeQuery()
    println()
    while (resultSet.next()) {
      val firstName = resultSet.getString("FirstName")
      val lastName = resultSet.getString("LastName")
      val courseName = resultSet.getString("CourseName")
      val examDate = resultSet.getDate("ExamDate")
      val gradeValue = resultSet.getString("GradeValue")
      println(s"$firstName $lastName - $courseName ($examDate): $gradeValue")

    }
    println()

  }

  def updateStudentWindow(): Unit = {
    println("please enter student id ")
    val id = scala.io.StdIn.readLine().toInt
    println("please enter student name ")
    val name = scala.io.StdIn.readLine()
    println("please enter student grade ")
    val grade = scala.io.StdIn.readLine()
    studentActor ! UpdateStudent(id, name, grade)
  }


  def readStudentWindow(): Unit = {
    println("please enter student id ")
    val id = scala.io.StdIn.readLine().toInt
    val result: Array[String] = Student.read(id) match {
      case arr: Array[String] => arr
      case _ => Array("0", "No Name", "No Grade")
    }
    println(s"\nThe result id = ${result(0)} name = ${result(1)} grade is ${result(2)}")
    //    result
  }

  def deleteStudentWindow(): Unit = {

    println("please enter student id ")
    val id: Int = scala.io.StdIn.readLine().toInt
    this.destroy(id)

  }

  def recordAttendance(studentIds: ArrayBuffer[Int], courseId: Int, status: String): Unit = {
    val insertQuery =
      """
        |INSERT INTO Attendance (StudentID, CourseID, Date, Status)
        |VALUES (?, ?, NOW(), ?);
        |""".stripMargin

    val preparedStatement = connection.prepareStatement(insertQuery)
    for (studentId <- studentIds) {
      preparedStatement.setInt(1, studentId)
      preparedStatement.setInt(2, courseId)
      preparedStatement.setString(3, status)
      preparedStatement.addBatch()
      val batchResult = preparedStatement.executeBatch()
      println(s"Attendance recorded successfully for ${batchResult.sum} students in CourseID $courseId.")
    }
  }
}




