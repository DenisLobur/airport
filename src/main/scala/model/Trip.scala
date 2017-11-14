package model

import java.time.LocalDateTime

import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Future

case class Trip(tripNo: Int, idComp: Int, plane: String, townFrom: String, townTo: String, timeOut: LocalDateTime, timeIn: LocalDateTime)

class TripTable(tag: Tag) extends Table[Trip](tag, "trips") {
  val tripNo = column[Int]("trip_no", O.PrimaryKey)
  val idComp = column[Int]("id_comp")
  val plane = column[String]("plane")
  val townFrom = column[String]("town_from")
  val townTo = column[String]("town_to")
  val timeOut = column[LocalDateTime]("time_out")
  val timeIn = column[LocalDateTime]("time_in")

  val companyFk = foreignKey("company_id_fk", idComp, TableQuery[CompanyTable])(_.idComp)

  def * = (tripNo, idComp, plane, townFrom, townTo, timeOut, timeIn) <> (Trip.apply _ tupled, Trip.unapply)
}

object TripTable {
  val table = TableQuery[TripTable]
}

class TripRepository(db: Database) {
  val tripTableQuery = TableQuery[TripTable]

  def create(trip: Trip): Future[Trip] = {
    db.run(tripTableQuery returning tripTableQuery += trip)
  }

  def update(trip: Trip): Future[Int] = {
    db.run(tripTableQuery.filter(_.tripNo === trip.tripNo).update(trip))
  }

  def delete(trip: Trip): Future[Int] = {
    db.run(tripTableQuery.filter(_.tripNo === trip.tripNo).delete)
  }

  def getById(tripId: Int): Future[Trip] = {
    db.run(tripTableQuery.filter(_.tripNo === tripId).result.head)
  }
}