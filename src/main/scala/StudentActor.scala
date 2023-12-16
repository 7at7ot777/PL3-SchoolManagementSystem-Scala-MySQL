import akka.actor.{Actor, ActorLogging, ActorSystem, Props}

// Define messages for communication
case class CreateStudent( firstName: String, lastName: String)
case class ReadStudent(id: Int)
case class UpdateStudent(id: Int, firstName: String, lastName: String)
case class DeleteStudent(id: Int)
case class IndexStudents()
case class getAllStudentGrades(studentId:Int)


class StudentActor extends Actor with ActorLogging {


  override def receive: Receive = {
    case CreateStudent(name, grade) =>
      Student.create(name, grade)
      log.info(s"A new student created successfully.")
    case getAllStudentGrades(id) =>
      Student.getAllStudentGrades(id)
      log.info(s"Student grades displayed")

    case ReadStudent(id) =>
//      val result = Student.read(id) match {
//        case arr: Array[String] => arr
//        case _ => Array("0", "No Name", "No Grade")
//      }
      Student.read(id)
      log.info(s"Student with ID $id has beean read")

    case UpdateStudent(id, firstName, lastName ) =>
      Student.update(id, firstName, lastName )
      log.info(s"Student with ID $id updated successfully.")

    case DeleteStudent(id) =>
      Student.destroy(id)
      log.info(s"Student with ID $id deleted successfully.")

    case IndexStudents() =>
      Student.index()
      log.info(s"Student index list is displayed.")

  }
}
