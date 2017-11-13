package model

import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Future

case class Passenger(idPsg: Int, name: String)

class PassengerTable(tag: Tag) extends Table[Passenger](tag, "passengers") {
  val idPsg = column[Int]("id_psg", O.PrimaryKey, O.AutoInc)
  val name = column[String]("name")

  def * = (idPsg, name) <> (Passenger.apply _ tupled, Passenger.unapply)
}

object PassengerTable {
  val table = TableQuery[PassengerTable]
}

class PassengerRepository(db: Database) {
  val passengerTableQuery = TableQuery[PassengerTable]

  def create(passenger: Passenger): Future[Passenger] = {
    db.run(passengerTableQuery returning passengerTableQuery += passenger)
  }

  def update(passenger: Passenger): Future[Int] = {
    db.run(passengerTableQuery.filter(_.idPsg === passenger.idPsg).update(passenger))
  }

  def delete(passenger: Passenger): Future[Int] = {
    db.run(passengerTableQuery.filter(_.idPsg === passenger.idPsg).delete)
  }

  def getById(passengerId: Int): Future[Passenger] = {
    db.run(passengerTableQuery.filter(_.idPsg === passengerId).result.head)
  }
}
