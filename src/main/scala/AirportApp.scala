import slick.jdbc.PostgresProfile.api._
import scala.concurrent.Await
import model._


object AirportApp extends App {
  println("Hello airport")
  val db = Database.forURL("jdbc:postgresql://127.0.0.1/airport?user=postgres&password=123456789")
}
