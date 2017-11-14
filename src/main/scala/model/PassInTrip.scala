package model

import java.time.LocalDateTime

import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Future

case class PassInTrip(tripNo: Int, date: LocalDateTime, idPsg: Int, place: String)

class PassInTripTable(tag: Tag) extends Table[PassInTrip](tag, "passengers_in_trip") {
  val tripNo = column[Int]("trip_no")
  val date = column[LocalDateTime]("date")
  val idPsg = column[Int]("id_psg")
  val place = column[String]("place")

  val pk = primaryKey("passenger_in_trip_pk", (tripNo, date, idPsg))

  val tripNoFk = foreignKey("trip_no_fk", tripNo, TableQuery[TripTable])(_.tripNo)
  val passengerFk = foreignKey("passenger_id_fk", idPsg, TableQuery[PassengerTable])(_.idPsg)

  def * = (tripNo, date, idPsg, place) <> (PassInTrip.apply _ tupled, PassInTrip.unapply)
}

object PassInTripTable {
  val table = TableQuery[PassInTripTable]
}

class PassInTripRepository(db: Database) {
  val passInTripTableQuery = TableQuery[PassInTripTable]

  def create(passInTrip: PassInTrip): Future[PassInTrip] = {
    db.run(passInTripTableQuery returning passInTripTableQuery += passInTrip)
  }

  def update(passInTrip: PassInTrip): Future[Int] = {
    db.run(passInTripTableQuery.filter(_.tripNo === passInTrip.tripNo).update(passInTrip))
  }

  def delete(passInTrip: PassInTrip): Future[Int] = {
    db.run(passInTripTableQuery.filter(_.tripNo === passInTrip.tripNo).delete)
  }

  def getById(passInTripId: Int): Future[PassInTrip] = {
    db.run(passInTripTableQuery.filter(_.tripNo === passInTripId).result.head)
  }
}
