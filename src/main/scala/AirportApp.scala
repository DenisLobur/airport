import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await
import model._
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

  //  exec(CompanyTable.table.schema.drop.asTry andThen CompanyTable.table.schema.create)
  //  exec(TripTable.table.schema.drop.asTry andThen TripTable.table.schema.create)
  //  exec(PassengerTable.table.schema.drop.asTry andThen PassengerTable.table.schema.create)
  //  exec(PassInTripTable.table.schema.drop.asTry andThen PassInTripTable.table.schema.create)

  println(CompanyTable.table.schema.createStatements.mkString + "\n"
    + PassengerTable.table.schema.createStatements.mkString + "\n"
    + TripTable.table.schema.createStatements.mkString + "\n"
    + PassInTripTable.table.schema.createStatements.mkString
  )


  //  exec(CompanyTable.table ++= companiesData)
  //  exec(TripTable.table ++= tripData)
  //  exec(PassengerTable.table ++= passengerData)
  //  exec(PassInTripTable.table ++= passInTripData)

  // Exercises

  /** Task 63
    * Find the names of different passengers that ever travelled more than once occupying seats with the same number
    */
  def _63(): Unit = {
    val query = passInTripRepository.table
      .join(passengerRepository.table)
      .on(_.idPsg === _.idPsg)
      .groupBy {
        case (pasintrip, pas) => (pasintrip.place, pasintrip.idPsg, pas.name)
      }
      .map {
        case ((place, idPas, name), group) => (group.size, name)
      }
      .filter(p => p._1 > 1)
      .map(_._2)
      .result

    println("\n=> " + query.statements.mkString.toUpperCase)
    exec(query).foreach(println)
  }

  /** Task 67
    * Find out the number of routes with the greatest number of flights (trips).
    * Notes.
    * 1) A - B and B - A are to be considered DIFFERENT routes.
    * 2) Use the Trip table only. */
  def _67(): Unit = {
    val max = tripRepository.table
      .groupBy {
        case t => (t.townFrom, t.townTo)
      }
      .map {
        case ((townFrom, townTo), group) => group.size
      }
      .groupBy {
        _ => true
      }
      .map {
        case (_, group) => group.max
      }

    val query = tripRepository.table
      .groupBy {
        case t => (t.townFrom, t.townTo)
      }
      .map {
        case ((townFrom, townTo), group) => (townFrom, townTo, group.size)
      }
      .filter {
        case (townFrom, townTo, size) => size in max
      }
      .length
      .result

    println("\n=> " + query.statements.mkString.toUpperCase)
    println(exec(query))
  }

  /** Task 68
    * Find out the number of routes with the greatest number of flights (trips).
    * Notes.
    * 1) A - B and B - A are to be considered the SAME route.
    * 2) Use the Trip table only. */
  def _68(): Unit = {
    val subQuery = (tripRepository.table.map(t => (t.idComp, t.townFrom, t.townTo)) unionAll
      tripRepository.table.map(t => (t.idComp, t.townTo, t.townFrom)))
      .groupBy {
        case t => (t._2, t._3)
      }
      .map {
        case (_, group) => group.size
      }

    val max = subQuery
      .groupBy {
        _ => true
      }
      .map {
        case (_, group) => group.max
      }

    val query = subQuery
      .filter {
        _ in max
      }
      .length
      .result

    println("\n=> " + query.statements.mkString.toUpperCase)
    println(exec(query) / 2)
  }

  /** Task #72
    * Among the customers using a single airline, find distinct passengers who have flown most frequently.
    * Result set: passenger name, number of trips. */
  def _72(): Unit = {
    val passAndComp = passInTripRepository.table
      .join(tripRepository.table)
      .on(_.tripNo === _.tripNo)
      .groupBy {
        case (pasintrip, pas) => (pasintrip.idPsg, pas.idComp)
      }
      .map {
        case ((idPas, idComp), group) => (idPas, idComp, group.size)
      }

    val companyPassMaximum = passAndComp
      .map {
        case (idPas, idComp, maximum) => maximum
      }
      .groupBy {
        _ => true
      }
      .map {
        case (_, group) => group.max
      }

    val idPasWithMax = passAndComp
      .filter {
        case pas => pas._3 in companyPassMaximum
      }
      .map {
        case pas => (pas._1, pas._3)
      }

    val passNames = idPasWithMax
      .join(passengerRepository.table)
      .on(_._1 === _.idPsg)
      .map {
        case (idPasMax, pas) => (pas.name, idPasMax._2)
      }

    println("\n=> " + passNames.result.statements.mkString.toUpperCase)
    exec(passNames.result).foreach(println)
  }

  _63()
  _67()
  _68()
  _72()
}
