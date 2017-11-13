package model

import slick.jdbc.PostgresProfile.api._

case class Company(idComp: Int, name: String)

class CompanyTable(tag: Tag) extends Table[Company](tag, "companies") {
  val idComp = column[Int]("id_comp", O.PrimaryKey, O.AutoInc)
  val name = column[String]("name")

  def * = (idComp, name) <> (Company.apply _ tupled, Company.unapply)
}

object CompanyTable {
  val table = TableQuery[CompanyTable]
}