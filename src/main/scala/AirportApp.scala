import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await
import model._

import scala.concurrent.duration.Duration


object AirportApp extends App {
  println("Hello airport")

  def exec[T](program: DBIO[T]): T = Await.result(db.run(program), Duration.Inf)
  val db = Database.forURL("jdbc:postgresql://127.0.0.1/airport?user=postgres&password=123456789")

  val companyRepository = new CompanyRepository(db)
  val passengerRepository = new PassengerRepository(db)
  val tripRepository = new TripRepository(db)

  exec(CompanyTable.table.schema.create)
  println(CompanyTable.table.schema.createStatements.mkString)

}
