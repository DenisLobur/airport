import java.time.LocalDate
import slick.jdbc.PostgresProfile.api._

package object model {
  implicit val dateTimeToStringMapper =
    MappedColumnType.base[LocalDate, String](
      (d: LocalDate) => d.toString,
      (s: String) => LocalDate.parse(s))
}
