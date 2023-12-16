import SchoolManagementSystem.connection
import Student.statement

import java.io.{File, FileWriter, PrintWriter}
import java.sql.SQLException
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




  def create(id: Int, name: String, subject: String): Unit = {
    val writer = new FileWriter("teachers.txt", true)
    writer.write(s"$id,$name,$subject\n")
    writer.close()
    println("Teacher Created Successfully")
  }

  def deleteLineFromFile(id: Int): Unit = {
    val filePath = "teachers.txt"
    val source = Source.fromFile(filePath)
    val lines = source.getLines().filterNot { line =>
      val recordFields = line.split(",")
      val teacherId = recordFields(0).toInt
      id == teacherId
    }.toList
    source.close()

    val writer = new PrintWriter(new File(filePath))
    lines.foreach(writer.println)
    writer.close()
  }

  def update(id: Int, name: String, subject: String): Unit = {
    deleteLineFromFile(id)
    create(id, name, subject)
    println("\nTeacher Updated Successfully")
  }

  def createNewTeacherWindow(): Unit = {
    println("Please enter teacher name: ")
    val name = scala.io.StdIn.readLine()
    println("Please enter teacher subject: ")
    val subject = scala.io.StdIn.readLine()
    this.id += 1
    create(this.id, name, subject)
  }

  def readTeacherWindow(): Unit = {
    println("Please enter teacher id: ")
    val id = scala.io.StdIn.readLine().toInt
   read(id)


  }

  def updateTeacherWindow(): Unit = {
    println("Please enter teacher id: ")
    val id = scala.io.StdIn.readLine().toInt
    println("Please enter teacher name: ")
    val name = scala.io.StdIn.readLine()
    println("Please enter teacher subject: ")
    val subject = scala.io.StdIn.readLine()
    update(id, name, subject)
  }

  def deleteTeacherWindow(): Unit = {
    println("Please enter teacher id: ")
    val id: Int = scala.io.StdIn.readLine().toInt
    deleteLineFromFile(id)
  }
}
