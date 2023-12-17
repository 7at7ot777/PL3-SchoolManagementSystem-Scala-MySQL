import akka.actor.{Actor, ActorLogging}

// Define messages for communication
case class CreateCourse(id: Int, name: String, department: String)
case class ReadCourse(id: Int)
case class UpdateCourse(id: Int, name: String)
case class DeleteCourse(id: Int)
case class IndexCourses()

class CourseActor extends Actor with ActorLogging {
  // Actor state

  override def receive: Receive = {
    case CreateCourse(id, name, department) =>
      Course.create(id, name, department)
      log.info(s"Course with ID $id created successfully.")

    case ReadCourse(id) =>
      Course.read(id)
      log.info(s"Course with ID $id has been read")

    case UpdateCourse(id, name) =>
      Course.update(id, name)
      log.info(s"Course with ID $id updated successfully.")

    case DeleteCourse(id) =>
      Course.destroy(id)
      log.info(s"Course with ID $id deleted successfully.")

    case IndexCourses() =>
      Course.index()
      log.info(s"Course index list is displayed.")
  }
}
