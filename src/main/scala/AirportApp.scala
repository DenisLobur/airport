import java.text.SimpleDateFormat
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

  /** Task #63
    * Find the names of different passengers that ever travelled more than once occupying seats with the same number */
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

    println("\n#63 => " + query.statements.mkString.toUpperCase)
    exec(query).foreach(println)
  }

  /** Task #67
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

    println("\n#67 => " + query.statements.mkString.toUpperCase)
    println(exec(query))
  }

  /** Task #68
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

    println("\n#68 => " + query.statements.mkString.toUpperCase)
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

    val query = idPasWithMax
      .join(passengerRepository.table)
      .on(_._1 === _.idPsg)
      .map {
        case (idPasMax, pas) => (pas.name, idPasMax._2)
      }

    println("\n#72 => " + query.result.statements.mkString.toUpperCase)
    exec(query.result).foreach(println)
  }

  /** Task #77
    * Find the days with the maximum number of flights departed from Rostov. Result set: number of trips, date. */
  def _77(): Unit = {
    val rostovTrips = (for {
      pasIntrip <- PassInTripTable.table
      t <- TripTable.table if pasIntrip.tripNo === t.tripNo
    } yield (pasIntrip, t))
      .filter {
        case (pasIntrip, trip) => (trip.townFrom === "Rostov")
      }
      .groupBy {
        case (pasIntrip, trip) => (pasIntrip.date, pasIntrip.tripNo)
      }
      .map { case ((date, tripNo), group) => (group.countDistinct, date) }

    val query = rostovTrips.sortBy(_._2.asc)

    println("\n#77 => " + query.result.statements.mkString.toUpperCase)
    exec(query.result).foreach(println)
  }

  /** Task #87
    * Considering that a passenger lives in his first flight departure town, find those passengers among dwellers of other cities who visited Moscow more than once.
    * Result set: passenger's name, number of visits to Moscow. */
  def _87(): Unit = {
    val subQuery = passInTripRepository.table
      .join(tripRepository.table)
      .on(_.tripNo === _.tripNo)
      .join(passengerRepository.table)
      .on { case
        ((pasInTrip, trip), pas) => pasInTrip.idPsg === pas.idPsg
      }
      .map {
        case ((pasInTrip, trip), pas) => (pas.idPsg, pas.name, pasInTrip.date, trip.townFrom, trip.townTo)
      }

    val idPsgMsc = subQuery
      .groupBy {
        case (pId, name, date, tf, tt) => pId
      }
      .map {
        case (pId, group) => (pId, group.map(_._3).min)
      }
      .join(subQuery)
      .on(_._2 === _._3
      )
      .map {
        case (firstTrip, trips) => (trips._1, trips._4)
      }
      .distinct
      .filter {
        case (id, city) => city =!= "Moscow"
      }
      .map(_._1)

    val query = subQuery.
      filter {
        case (pId, name, date, townFrom, townTo) => townTo === "Moscow" && pId.in(idPsgMsc)
      }
      .map {
        case (pId, name, date, townFrom, townTo) => (name, townTo)
      }
      .groupBy {
        case (name, townTo) => name
      }
      .map {
        case (name, group) => (name, group.size)
      }
      .filter {
        case (name, num) => num > 1
      }

    println("\n#87 => " + query.result.statements.mkString.toUpperCase)
    exec(query.result).foreach(println)
  }

  /** Task #88
    * Among the clients which only use a single company, find the different passengers who have flown more often than others.
    * Result set: passenger name, number of trips, and company name. */
  def _88(): Unit = {
    val passanger = passInTripRepository.table
      .join(passengerRepository.table)
      .on(_.idPsg === _.idPsg)
      .join(tripRepository.table)
      .on {
        case ((pasInTrip, pas), t) => pasInTrip.tripNo === t.tripNo
      }
      .map {
        case ((pasInTrip, pas), t) => (pasInTrip.idPsg, t.idComp, pasInTrip.tripNo, pas.name)
      }
      .groupBy {
        case (pas, comp, trip, name) => pas
      }
      .map {
        case (pas, group) => (
          pas, group.map(_._2).countDistinct, group.map(_._3).size, group.map(_._2).max, group.map(_._4).max)
      }
      .filter {
        case (pas, compNo, tripNo, comp, name) => compNo === 1
      }
      .map(t => (t._1, t._3, t._4, t._5))

    val maximumTime = passanger.map(_._2).max

    val query = passanger
      .join(companyRepository.table)
      .on(_._3 === _.idComp)
      .map {
        case (pas, comp) => (pas._4.getOrElse("no name"), pas._2, comp.name)
      }
      .filter {
        case (pas, tripNo, comp) => tripNo === maximumTime
      }

    println("\n#88 => " + query.result.statements.mkString.toUpperCase)
    exec(query.result).foreach(println)
  }

  /** Task #95
    * Using the Pass_in_Trip table, calculate for each airline company:
    * 1) the number of performed flights;
    * 2) the number of the used types of planes;
    * 3) the number of different passengers that have been transported;
    * 4) the total number of passengers that have been transported by the company.
    * Output: airline name, 1), 2), 3), 4). */
  def _95(): Unit = {
    val subQuery = passInTripRepository.table
      .join(tripRepository.table)
      .on(_.tripNo === _.tripNo)
      .map {
        case (pasInTrip, trip) => (trip.idComp, pasInTrip.tripNo, trip.plane, pasInTrip.idPsg)
      }

    val queryIdComp = subQuery
      .groupBy {
        case (company, trip, plane, pas) => company
      }
      .map {
        case (company, group) => (
          company, group.map(_._2).size,
          group.map(_._3).countDistinct,
          group.map(_._4).countDistinct,
          group.map(_._3).size
        )
      }
    val query = queryIdComp
      .join(companyRepository.table)
      .on(_._1 === _.idComp)
      .map {
        case (req, c) => (c.name, req._2, req._3, req._4, req._5)
      }

    println("\n#95 => " + query.result.statements.mkString.toUpperCase)
    exec(query.result).foreach(println)
  }

  /** Task #102
    * Find the names of the different passengers, which flew only by the same route (there and back or in the one direction). */
  def _102(): Unit = {
    val trips = tripRepository.table
      .map {
        f =>
          (f.tripNo, Case If (f.townTo < f.townFrom) Then f.townTo Else f.townFrom,
            Case If (f.townTo < f.townFrom) Then f.townFrom Else f.townTo)
      }

    val query = trips
      .join(passInTripRepository.table)
      .on(_._1 === _.tripNo)
      .map {
        case (tr, pit) => (tr._2, tr._3, pit.idPsg)
      }
      .join(passengerRepository.table)
      .on(_._3 === _.idPsg)
      .map {
        case (tr, p) => (p.name, tr._1, tr._2)
      }
      .groupBy {
        case (passname, town1, town2) => passname
      }
      .map {
        case (passname, group) => (passname, group.map(_._2).countDistinct, group.map(_._3).countDistinct)
      }
      .filter {
        s => (s._2 === 1) && (s._3 === 1)
      }
      .map(_._1)

    println("\n#102 => " + query.result.statements.mkString.toUpperCase)
    exec(query.result).foreach(println)
  }

  /** Task #103
    * Find out the three smallest and three greatest trip numbers.
    * Output them in one row of six columns ordered from the least trip number to the greatest one.
    * Note. Consider that the Trip table contains 6 or more rows. */
  def _103(): Unit = {
    val tripMax = tripRepository.table
      .sortBy(_.tripNo.desc)
      .take(3)
      .map(_.tripNo)
    val tripMin = tripRepository.table
      .sortBy(_.tripNo.asc)
      .take(3)
      .map(_.tripNo)
    val query = tripMin.take(1).join(tripMin.drop(1).take(1)).
      join(tripMin.drop(2).take(1)).map { case (s1, s2) => (s1._1, s1._2, s2) }.
      join(tripMax.drop(2).take(1)).map { case (s1, s2) => (s1._1, s1._2, s1._3, s2) }.
      join(tripMax.drop(1).take(1)).map { case (s1, s2) => (s1._1, s1._2, s1._3, s1._4, s2) }.
      join(tripMax.take(1)).map { case (s1, s2) => (s1._1, s1._2, s1._3, s1._4, s1._5, s2) }

    println("\n#103 => " + query.result.statements.mkString.toUpperCase)
    exec(query.result).foreach(println)
  }

  /** Task #107
    * Find the company, trip number, and trip date for the fifth passenger (in time order), who had flown from the Rostov city in the April 2003.
    * Note. Take into account for this task that two flights cannot simultaneously fly off from Rostov . */
  def _107(): Unit = {
    val format1 = new SimpleDateFormat("yyyyMMdd HH:mm:ss").parse("20030401 00:00:00")
    val date1 = new java.sql.Date(format1.getTime)
    val format2 = new SimpleDateFormat("yyyyMMdd HH:mm:ss").parse("20030501 00:00:00")
    val date2 = new java.sql.Date(format2.getTime)

    val query = passInTripRepository.table
      .join(tripRepository.table)
      .on(_.tripNo === _.tripNo)
      .join(companyRepository.table)
      .on {
        case ((pit, t), c) => t.idComp === c.idComp
      }
      .map {
        case ((pit, t), c) => (t.townFrom, c.name, pit.tripNo, pit.date, pit.idPsg)
      }
      .filter {
        s => s._1 === "Rostov"
      }
      .filter {
        s => s._4.asColumnOf[java.sql.Date] > date1 && s._4.asColumnOf[java.sql.Date] < date2
      }
      .sortBy {
        s => s._4.desc
      }
      .drop(4)
      .take(1)
      .map {
        s => (s._2, s._3, s._4)
      }

    println("\n#107 => " + query.result.statements.mkString.toUpperCase)
    exec(query.result).foreach(println)
  }

  /** Task #114
    * Find the names of the different passengers, which flew more often than others in the same seat.
    * Output: name and quantity of the flights in the same seat. */
  def _114(): Unit = {
    val subQuery = passInTripRepository.table
      .join(passengerRepository.table)
      .on(_.idPsg === _.idPsg)
      .map {
        case (pasInTrip, pas) => (pas.name, pasInTrip.place)
      }
      .groupBy {
        case (name, place) => (name, place)
      }
      .map {
        case ((name, place), group) => (name, group.size)
      }

    val maximum = subQuery
      .map(_._2)
      .groupBy { _ => true }
      .map {
        case (_, group) => group.max
      }

    val query = subQuery.
      filter {
        case (name, times) => times in maximum
      }

    println("\n#114 => " + query.result.statements.mkString.toUpperCase)
    exec(query.result).foreach(println)
  }

  _63()
  _67()
  _68()
  _72()
  _77()
  _87()
  _88()
  _95()
  _102()
  _103()
  _107()
  _114()
}
