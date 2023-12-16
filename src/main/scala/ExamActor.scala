import akka.actor.{Actor, ActorLogging}

// Define messages for communication
case class CreateExam(id: Int, subject: String, date: String)
case class ReadExam(id: Int)
case class UpdateExam(id: Int, subject: String, date: String)
case class DeleteExam(id: Int)
case class IndexExams()

class ExamActor extends Actor with ActorLogging {
  // Actor state

  override def receive: Receive = {
    case CreateExam(id, subject, date) =>
      Exam.create(id, subject, date)
      log.info(s"Exam with ID $id created successfully.")

    case ReadExam(id) =>
      Exam.read(id)
      log.info(s"Exam with ID $id has been read")

    case UpdateExam(id, subject, date) =>
      Exam.update(id, subject, date)
      log.info(s"Exam with ID $id updated successfully.")

    case DeleteExam(id) =>
      Exam.deleteLineFromFile(id)
      log.info(s"Exam with ID $id deleted successfully.")

    case IndexExams() =>
      Exam.index()
      log.info(s"Exam index list is displayed.")
  }
}
