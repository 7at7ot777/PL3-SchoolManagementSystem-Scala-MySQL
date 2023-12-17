import SchoolManagementSystem.connection

import java.io.{File, FileWriter, PrintWriter}
import scala.io.Source
import scala.util.control.Breaks.break

object Exam {
  private var id: Int = 0

  def index(): Unit = {
    val query =
      """
        |SELECT Exam.ExamID, Course.CourseName, Exam.ExamDate
        |FROM Exam
        |JOIN Course ON Exam.CourseID = Course.CourseID;
        |""".stripMargin

    val preparedStatement = connection.prepareStatement(query)
    val resultSet = preparedStatement.executeQuery()

    while (resultSet.next()) {
      val examID = resultSet.getInt("ExamID")
      val courseName = resultSet.getString("CourseName")
      val examDate = resultSet.getDate("ExamDate")

      println(s"ExamID: $examID, CourseName: $courseName, ExamDate: $examDate")
    }


  }

  def read(id: Int): Any = {

        val query =
          """
            |SELECT Exam.ExamID, Course.CourseName, Exam.ExamDate
            |FROM Exam
            |JOIN Course ON Exam.CourseID = Course.CourseID
            |WHERE Exam.ExamID = ?;
            |""".stripMargin

        val preparedStatement = connection.prepareStatement(query)
        preparedStatement.setInt(1, id)

        val resultSet = preparedStatement.executeQuery()

        while (resultSet.next()) {
          val examID = resultSet.getInt("ExamID")
          val courseName = resultSet.getString("CourseName")
          val examDate = resultSet.getDate("ExamDate")

          println(s"ExamID: $examID, CourseName: $courseName, ExamDate: $examDate")
        }
      }


  def create(): Unit = {
   Course.index()
    println("===============")
    println("Choose course id from the ID's mentioned above")
    var courseId = scala.io.StdIn.readLine().toInt

    val insertQuery =
      """
        |INSERT INTO Exam (CourseID, ExamDate)
        |VALUES (?, NOW());
        |""".stripMargin

    val preparedStatement = connection.prepareStatement(insertQuery)
    preparedStatement.setInt(1, courseId)

    val rowsInserted = preparedStatement.executeUpdate()

    if (rowsInserted > 0) {
      println("Exam data inserted successfully.")
    } else {
      println("Failed to insert exam data.")
    }
  }

  def destroy(id: Int): Unit = {
    var disableForeignKeyChecks = connection.prepareStatement("SET foreign_key_checks = 0")

    disableForeignKeyChecks.execute();
    val deleteStudentQuery = "DELETE FROM exam WHERE ExamID = ?"

    val preparedStatement = connection.prepareStatement(deleteStudentQuery)

    // Set value for the prepared statement
    preparedStatement.setInt(1, id)

    // Execute the delete query
    val rowsAffected = preparedStatement.executeUpdate()

    // Check if the deletion was successful
    if (rowsAffected > 0) {
      println("Teacher deleted successfully!")
    } else {
      println(s"No Teacher found with StudentID: $id")
    }

  }

  def update(id: Int, course: String, date: String): Unit = {
    Course.index()
    println("===============")
    println("Choose course id from the ID's mentioned above")
    var courseId = scala.io.StdIn.readLine().toInt
    val updateQuery =
      """
        |UPDATE Exam
        |SET CourseID = ?, ExamDate = NOW()
        |WHERE ExamID = ?;
        |""".stripMargin

    val preparedStatement = connection.prepareStatement(updateQuery)
    preparedStatement.setInt(1, courseId)
    preparedStatement.setInt(2, id)

    val rowsUpdated = preparedStatement.executeUpdate()

    if (rowsUpdated > 0) {
      println(s"Exam with ExamID $id updated successfully.")
    } else {
      println(s"No exam found with ExamID $id.")
    }
  }

  def createNewExamWindow(): Unit = {
    println("Please enter course name: ")
    val course = scala.io.StdIn.readLine()
    println("Please enter exam date: ")
    val date = scala.io.StdIn.readLine()

    create()
  }

  def readExamWindow(): Unit = {
    println("Please enter exam id: ")
    val id = scala.io.StdIn.readLine().toInt
    read(id)


  }

  def updateExamWindow(): Unit = {
    println("Please enter exam id: ")
    val id = scala.io.StdIn.readLine().toInt
    println("Please enter course name: ")
    val course = scala.io.StdIn.readLine()
    println("Please enter exam date: ")
    val date = scala.io.StdIn.readLine()
    update(id, course, date)
  }

  def deleteExamWindow(): Unit = {
    println("Please enter exam id: ")
    val id: Int = scala.io.StdIn.readLine().toInt
    destroy(id)
  }
}
