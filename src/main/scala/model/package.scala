import java.time.LocalDateTime
import slick.jdbc.PostgresProfile.api._

package object model {
  implicit val dateTimeToStringMapper =
    MappedColumnType.base[LocalDateTime, String](
      (d: LocalDateTime) => d.toString,
      (s: String) => LocalDateTime.parse(s))
}
