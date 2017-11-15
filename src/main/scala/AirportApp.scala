import java.text.SimpleDateFormat
import java.time.LocalDateTime

import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await
import model.{Passenger, _}
import data._

import scala.concurrent.duration.Duration


object AirportApp extends App {
  println("Hello airport")

  def exec[T](program: DBIO[T]): T = Await.result(db.run(program), Duration.Inf)

  val db = Database.forURL("jdbc:postgresql://localhost/airport?user=postgres&password=123456789")

  val companyRepository = new CompanyRepository(db)
  val passengerRepository = new PassengerRepository(db)
  val tripRepository = new TripRepository(db)
  val passInTripRepository = new PassInTripRepository(db)

  exec(CompanyTable.table.schema.drop.asTry andThen CompanyTable.table.schema.create)
  exec(TripTable.table.schema.drop.asTry andThen TripTable.table.schema.create)
  exec(PassengerTable.table.schema.drop.asTry andThen PassengerTable.table.schema.create)
  exec(PassInTripTable.table.schema.drop.asTry andThen PassInTripTable.table.schema.create)

  println(CompanyTable.table.schema.createStatements.mkString + "\n"
    + PassengerTable.table.schema.createStatements.mkString + "\n"
    + TripTable.table.schema.createStatements.mkString + "\n"
    + PassInTripTable.table.schema.createStatements.mkString
  )


  exec(CompanyTable.table ++= companiesData)
  exec(TripTable.table ++= tripData)
  exec(PassengerTable.table ++= passengerData)
  exec(PassInTripTable.table ++= passInTripData)

  // Exercises
  //Task 63(2) Find the names of different passengers that ever travelled more than once occupying seats with the same number
  // select ID_psg, place from Pass_in_trip group by ID_psg, place HAVING COUNT(*) >= 2
//  val _63 = passInTrip.get()
//  val z = Await.result(_63, Duration.Inf)
//  z.foreach(println)
//  passInTrip.select63()

}
