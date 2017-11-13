package model

import java.time.LocalDate
import slick.jdbc.PostgresProfile.api._

case class PassInTrip(tripNo: Int, date: LocalDate, idPsg: Int, place: String)

class PassInTripTable(tag: Tag) extends Table[PassInTrip](tag, "passengers_in_trip") {
  val tripNo = column[Int]("trip_no", O.PrimaryKey)
  val date = column[LocalDate]("date")
  val idPsg = column[Int]("id_psg")
  val place = column[String]("place")

  def * = (tripNo, date, idPsg, place) <> (PassInTrip.apply _ tupled, PassInTrip.unapply)
}

object PassInTripTable {
  val table = TableQuery[PassInTripTable]
}
