package model

import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Future

case class Company(idComp: Int, name: String)

class CompanyTable(tag: Tag) extends Table[Company](tag, "companies") {
  val idComp = column[Int]("id_comp", O.PrimaryKey, O.AutoInc)
  val name = column[String]("name")

  def * = (idComp, name) <> (Company.apply _ tupled, Company.unapply)
}

object CompanyTable {
  val table = TableQuery[CompanyTable]
}

class CompanyRepository(db: Database) {
  val companyTableQuery = TableQuery[CompanyTable]

  def create(company: Company): Future[Company] = {
    db.run(companyTableQuery returning companyTableQuery += company)
  }

  def update(company: Company): Future[Int] = {
    db.run(companyTableQuery.filter(_.idComp === company.idComp).update(company))
  }

  def delete(company: Company): Future[Int] = {
    db.run(companyTableQuery.filter(_.idComp === company.idComp).delete)
  }

  def getById(companyId: Int): Future[Company] = {
    db.run(companyTableQuery.filter(_.idComp === companyId).result.head)
  }
}