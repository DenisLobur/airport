package model

import java.time.LocalDate
import slick.jdbc.PostgresProfile.api._

case class Trip(tripNo: Int, idComp: Int, plane: String, townFrom: String, townTo: String, timeOut: LocalDate, timeIn: LocalDate)

class TripTable(tag: Tag) extends Table[Trip](tag, "trips") {
  val tripNo = column[Int]("trip_no")
  val idComp = column[Int]("id_comp")
  val plane = column[String]("plane")
  val townFrom = column[String]("town_from")
  val townTo = column[String]("town_to")
  val timeOut = column[LocalDate]("time_out")
  val timeIn = column[LocalDate]("time_in")

  def * = (tripNo, idComp, plane, townFrom, townTo, timeOut, timeIn) <> (Trip.apply _ tupled, Trip.unapply)
}

object TripTable {
  val table = TableQuery[TripTable]
}