import akka.actor.{Actor, ActorLogging}

// Define messages for communication
case class CreateTeacher( firstName: String, lastName: String,courseName:String)
case class ReadTeacher(id: Int)
case class UpdateTeacher(id: Int, name: String, subject: String)
case class DeleteTeacher(id: Int)
case class IndexTeachers()

class TeacherActor extends Actor with ActorLogging {
  // Actor state

  override def receive: Receive = {
    case CreateTeacher( fname, lname,courseName) =>
      Teacher.create( fname,lname,courseName)
      log.info(s"Teacher created successfully.")

    case ReadTeacher(id) =>
      Teacher.read(id)
      log.info(s"Teacher with ID $id has beean read")


    case UpdateTeacher(id, name, subject) =>
      Teacher.update(id, name, subject)
      log.info(s"Teacher with ID $id updated successfully.")

    case DeleteTeacher(id) =>
      Teacher.destroy(id)
      log.info(s"Teacher with ID $id deleted successfully.")

    case IndexTeachers() =>
      Teacher.index()
      log.info(s"Teacher index list is displayed.")
  }
}
