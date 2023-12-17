import akka.actor.{ActorSystem, Props}

import java.sql.Connection
import scala.util.control.Breaks.break

object SchoolManagementSystem extends App {

  //Database
  var connection: Connection = null
  connection = DatabaseConfig.getConnection


  val system = ActorSystem("StudentSystem")
  val studentActor = system.actorOf(Props[StudentActor], "studentActor")

  var choice = 1
  while (choice != 0) {
    println(
      """
        |Please Choose The Operation
        |1-Open Student Module
        |2-Open Teacher Module
        |3-Open Course Module
        |4-Open Exam Module
        |5-To Terminate
        ================================================================================================""".stripMargin)

    choice = scala.io.StdIn.readLine().toInt
    choice match {
      case 1 => studentCruds()
      case 2 => teacherCruds()
      case 3 => courseCruds()
      case 4 => examCruds()
      case 5 => choice = 0
    }
  }
  println("Program Closed Successfully")


  def studentCruds(): Unit = {
    var choice = 1
    while (choice != 0) {
      println(
        """
          |Please Choose The Operation
          |1-Create new student
          |2-Read a student
          |3-Update student
          |4-Delete student
          |5-Show all students
          |6-Get Student Grades
          |7-To Go Back
            =============================================================================================""".stripMargin)

      choice = scala.io.StdIn.readLine().toInt
      choice match {
        case 1
        =>
          studentActor ! Student.createNewStudentWindow()
        case 2
        =>
          println("Please enter student id:")
          val id = scala.io.StdIn.readInt()
          studentActor ! ReadStudent(id)
        case 3
        =>
          Student.updateStudentWindow()
        case 4
        =>
          println("Please enter student id:")
          val id = scala.io.StdIn.readInt()
          studentActor ! DeleteStudent(id)
        case 5
        =>
          studentActor ! IndexStudents()
        case 6
        =>
          println("Please enter student id:")
          val id = scala.io.StdIn.readInt()
          studentActor ! getAllStudentGrades(id)
        case 7
        =>
          choice = 0
        case _ =>
          println("Invalid choice. Please try again.")
      }

    }
  }

  def teacherCruds(): Unit = {

    val system = ActorSystem("ActorSystem")
    val teacherActor = system.actorOf(Props[TeacherActor], "teacherActor")

    var choice = 1
    while (choice != 0) {
      println(
        """
          |Please Choose The Operation
          |1-Create new teacher
          |2-Read a teacher
          |3-Update teacher
          |4-Delete teacher
          |5-Show all teachers
          |6-To Go Back
          ========================================================================================================""".stripMargin)

      choice = scala.io.StdIn.readLine().toInt

      choice match {
        case 1 =>
          teacherActor ! Teacher.createNewTeacherWindow()

        case 2 =>
          println("Please enter teacher id:")
          val id = scala.io.StdIn.readInt()
          teacherActor ! ReadTeacher(id)

        case 3 =>
          Teacher.updateTeacherWindow()

        case 4 =>
          println("Please enter teacher id:")
          val id = scala.io.StdIn.readInt()
          teacherActor ! DeleteTeacher(id)

        case 5 =>
          teacherActor ! IndexTeachers()

        case 6 =>
          choice = 0

        case _ =>
          println("Invalid choice. Please try again.")
      }


    }
  }


  def courseCruds(): Unit = {
    val system = ActorSystem("ActorSystem")
    val courseActor = system.actorOf(Props[CourseActor], "courseActor")

    var choice = 1
    while (choice != 0) {
      println(
        """
          |Please Choose The Operation
          |1-Read a course
          |2-Update course
          |3-Delete course
          |4-Show all courses
          |5-To Go Back
          ========================================================================================================""".stripMargin)

      choice = scala.io.StdIn.readLine().toInt

      choice match {
        case 1 =>
          println("Please enter course id:")
          val id = scala.io.StdIn.readInt()
          courseActor ! ReadCourse(id)

        case 2 =>
          Course.updateCourseWindow()

        case 3 =>
          println("Please enter course id:")
          val id = scala.io.StdIn.readInt()
          courseActor ! DeleteCourse(id)

        case 4 =>
          courseActor ! IndexCourses()

        case 5 =>
          choice = 0

        case _ =>
          println("Invalid choice. Please try again.")
      }
    }
  }



  def examCruds(): Unit = {
    val system = ActorSystem("ActorSystem")
    val examActor = system.actorOf(Props[ExamActor], "examActor")

    var choice = 1
    while (choice != 0) {
      println(
        """
          |Please Choose The Operation
          |1-Create new exam
          |2-Read an exam
          |3-Update exam
          |4-Delete exam
          |5-Show all exams
          |6-To Go Back
         ========================================================================================================""".stripMargin)

      choice = scala.io.StdIn.readLine().toInt

      choice match {
        case 1 =>
          examActor ! Exam.createNewExamWindow()

        case 2 =>
          println("Please enter exam id:")
          val id = scala.io.StdIn.readInt()
          examActor ! ReadExam(id)

        case 3 =>
          Exam.updateExamWindow()

        case 4 =>
          println("Please enter exam id:")
          val id = scala.io.StdIn.readInt()
          examActor ! DeleteExam(id)

        case 5 =>
          examActor ! IndexExams()

        case 6 =>
          choice = 0

        case _ =>
          println("Invalid choice. Please try again.")
      }
    }
  }


}
//  Student.create(1, "Mohamed", "1 E3dady")
//  Student.create(2, "Ahmed", "2 E3dady")//  //  val result: Array[String] = Student.read(5).asInstanceOf[Array[String]]
//  val result: Array[String] = Student.read(5) match {
//    case arr: Array[String] => arr
//    case _ =>Array("0", "No Name", "No Grade")
//  }
//  result.foreach(value => println(s"Found value: $value"))
//  println(s"\nThe result id = ${result(0)} name = ${result(1)} grade is ${result(2)}")

//  Student.deleteLineFromFile(2);
//
//  Student.update(1,"Hathout","Kolya")