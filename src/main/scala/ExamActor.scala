import akka.actor.{Actor, ActorLogging}

// Define messages for communication
case class CreateExam()
case class ReadExam(id: Int)
case class UpdateExam(id: Int, subject: String, date: String)
case class DeleteExam(id: Int)
case class IndexExams()

class ExamActor extends Actor with ActorLogging {
  // Actor state

  override def receive: Receive = {
    case CreateExam() =>
      Exam.create()
      log.info(s"Exam created successfully.")

    case ReadExam(id) =>
      Exam.read(id)
      log.info(s"Exam with ID $id has been read")

    case UpdateExam(id, subject, date) =>
      Exam.update(id, subject, date)
      log.info(s"Exam with ID $id updated successfully.")

    case DeleteExam(id) =>
      Exam.destroy(id)
      log.info(s"Exam with ID $id deleted successfully.")

    case IndexExams() =>
      Exam.index()
      log.info(s"Exam index list is displayed.")
  }
}
