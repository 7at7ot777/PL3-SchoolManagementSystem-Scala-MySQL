import java.io.{File, FileWriter, PrintWriter}
import scala.io.Source
import scala.util.control.Breaks.break

object Course {
  private var id: Int = 0

  def index(): Unit = {
    val filePath = "courses.txt"
    val source = Source.fromFile(filePath)

      println("Courses Index:")
      for (line <- source.getLines()) {
        val recordFields = line.split(",")
        println(s"ID: ${recordFields(0)}, Course Name: ${recordFields(1)}, Instructor: ${recordFields(2)}")
      }
      source.close()
  }

  def read(id: Int): Any = {

    val source = Source.fromFile("courses.txt")
    var flag = 0
    for (line <- source.getLines()) {
      // Assuming records are comma-separated values
      val recordFields = line.split(",")

      // Process the fields as needed
      val examId: Int = recordFields(0).toInt
      if (id == examId) {
        flag = 1
        println(s"Exam with ID $id: ID = ${recordFields(0)}, Course Name = ${recordFields(1)}, Instructor = ${recordFields(2)}")
        source.close()
        break
      }

    }
    if (flag == 0) println(s"Exam with id = ${id} is not found")
    source.close()
  }

  def create(id: Int, name: String, instructor: String): Unit = {
    val writer = new FileWriter("courses.txt", true)
    writer.write(s"$id,$name,$instructor\n")
    writer.close()
    println("Course Created Successfully")
  }

  def deleteLineFromFile(id: Int): Unit = {
    val filePath = "courses.txt"
    val source = Source.fromFile(filePath)
    val lines = source.getLines().filterNot { line =>
      val recordFields = line.split(",")
      val courseId = recordFields(0).toInt
      id == courseId
    }.toList
    source.close()

    val writer = new PrintWriter(new File(filePath))
    lines.foreach(writer.println)
    writer.close()
  }

  def update(id: Int, name: String, instructor: String): Unit = {
    deleteLineFromFile(id)
    create(id, name, instructor)
    println("\nCourse Updated Successfully")
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
    println("Please enter instructor name: ")
    val instructor = scala.io.StdIn.readLine()
    update(id, name, instructor)
  }

  def deleteCourseWindow(): Unit = {
    println("Please enter course id: ")
    val id: Int = scala.io.StdIn.readLine().toInt
    deleteLineFromFile(id)
  }
}
