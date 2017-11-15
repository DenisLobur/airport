import java.time.LocalDateTime

import model.{Company, PassInTrip, Passenger, Trip}

object data {

  def companiesData: Seq[Company] = Seq(
    Company(1, "Don_avia"),
    Company(2, "Aeroflot"),
    Company(3, "Dale_avia"),
    Company(4, "air_France"),
    Company(5, "British_AW")
  )

  def passengerData: Seq[Passenger] = Seq(
    Passenger(1, "Bruce Willis"),
    Passenger(2, "George Clooney"),
    Passenger(3, "Kevin Costner"),
    Passenger(4, "Donald Sutherland"),
    Passenger(5, "Jennifer Lopez"),
    Passenger(6, "Ray Liotta"),
    Passenger(7, "Samuel L. Jackson"),
    Passenger(8, "Nikole Kidman"),
    Passenger(9, "Alan Rickman"),
    Passenger(10, "Kurt Russell"),
    Passenger(11, "Harrison Ford"),
    Passenger(12, "Russell Crowe"),
    Passenger(13, "Steve Martin"),
    Passenger(14, "Michael Caine"),
    Passenger(15, "Angelina Jolie"),
    Passenger(16, "Mel Gibson"),
    Passenger(17, "Michael Douglas"),
    Passenger(18, "John Travolta"),
    Passenger(19, "Sylvester Stallone"),
    Passenger(20, "Tommy Lee Jones"),
    Passenger(21, "Catherine Zeta-Jones"),
    Passenger(22, "Antonio Banderas"),
    Passenger(23, "Kim Basinger"),
    Passenger(24, "Sam Neill"),
    Passenger(25, "Gary Oldman"),
    Passenger(26, "Clint Eastwood"),
    Passenger(27, "Brad Pitt"),
    Passenger(28, "Johnny Depp"),
    Passenger(29, "Pierce Brosnan"),
    Passenger(30, "Sean Connery"),
    Passenger(31, "Bruce Willis"),
    Passenger(37, "Mullah Omar")
  )

  def tripData: Seq[Trip] = Seq(
    Trip(1100, 4, "Boeing", "Rostov", "Paris", LocalDateTime.parse("1900-01-01T14:30:00.000"), LocalDateTime.parse("1900-01-01T17:50:00.000")),
    Trip(1101, 4, "Boeing", "Paris", "Rostov", LocalDateTime.parse("1900-01-01T08:12:00.000"), LocalDateTime.parse("1900-01-01T11:45:00.000")),
    Trip(1123, 3, "TU-154", "Rostov", "Vladivostok", LocalDateTime.parse("1900-01-01T16:20:00.000"), LocalDateTime.parse("1900-01-01T03:40:00.000")),
    Trip(1124, 3, "TU-154", "Vladivostok", "Rostov", LocalDateTime.parse("1900-01-01T09:00:00.000"), LocalDateTime.parse("1900-01-01T19:50:00.000")),
    Trip(1145, 2, "IL-86", "Moscow", "Rostov", LocalDateTime.parse("1900-01-01T09:35:00.000"), LocalDateTime.parse("1900-01-01T11:23:00.000")),
    Trip(1146, 2, "IL-86", "Rostov", "Moscow", LocalDateTime.parse("1900-01-01T17:55:00.000"), LocalDateTime.parse("1900-01-01T20:01:00.000")),
    Trip(1181, 1, "TU-134", "Rostov", "Moscow", LocalDateTime.parse("1900-01-01T06:12:00.000"), LocalDateTime.parse("1900-01-01T08:01:00.000")),
    Trip(1182, 1, "TU-134", "Moscow", "Rostov", LocalDateTime.parse("1900-01-01T12:35:00.000"), LocalDateTime.parse("1900-01-01T14:30:00.000")),
    Trip(1187, 1, "TU-134", "Rostov", "Moscow", LocalDateTime.parse("1900-01-01T15:42:00.000"), LocalDateTime.parse("1900-01-01T17:39:00.000")),
    Trip(1188, 1, "TU-134", "Moscow", "Rostov", LocalDateTime.parse("1900-01-01T22:50:00.000"), LocalDateTime.parse("1900-01-01T00:48:00.000")),
    Trip(1195, 1, "TU-154", "Rostov", "Moscow", LocalDateTime.parse("1900-01-01T23:30:00.000"), LocalDateTime.parse("1900-01-01T01:11:00.000")),
    Trip(1196, 1, "TU-154", "Moscow", "Rostov", LocalDateTime.parse("1900-01-01T04:00:00.000"), LocalDateTime.parse("1900-01-01T05:45:00.000")),
    Trip(7771, 5, "Boeing", "London", "Singapore", LocalDateTime.parse("1900-01-01T01:00:00.000"), LocalDateTime.parse("1900-01-01T11:00:00.000")),
    Trip(7772, 5, "Boeing", "Singapore", "London", LocalDateTime.parse("1900-01-01T12:00:00.000"), LocalDateTime.parse("1900-01-01T02:00:00.000")),
    Trip(7773, 5, "Boeing", "London", "Singapore", LocalDateTime.parse("1900-01-01T03:00:00.000"), LocalDateTime.parse("1900-01-01T13:00:00.000")),
    Trip(7774, 5, "Boeing", "Singapore", "London", LocalDateTime.parse("1900-01-01T14:00:00.000"), LocalDateTime.parse("1900-01-01T06:00:00.000")),
    Trip(7775, 5, "Boeing", "London", "Singapore", LocalDateTime.parse("1900-01-01T09:00:00.000"), LocalDateTime.parse("1900-01-01T20:00:00.000")),
    Trip(7776, 5, "Boeing", "Singapore", "London", LocalDateTime.parse("1900-01-01T18:00:00.000"), LocalDateTime.parse("1900-01-01T08:00:00.000")),
    Trip(7777, 5, "Boeing", "London", "Singapore", LocalDateTime.parse("1900-01-01T18:00:00.000"), LocalDateTime.parse("1900-01-01T06:00:00.000")),
    Trip(7778, 5, "Boeing", "Singapore", "London", LocalDateTime.parse("1900-01-01T22:00:00.000"), LocalDateTime.parse("1900-01-01T12:00:00.000")),
    Trip(8881, 5, "Boeing", "London", "Paris", LocalDateTime.parse("1900-01-01T03:00:00.000"), LocalDateTime.parse("1900-01-01T04:00:00.000")),
    Trip(8882, 5, "Boeing", "Paris", "London", LocalDateTime.parse("1900-01-01T22:00:00.000"), LocalDateTime.parse("1900-01-01T23:00:00.000"))
  )

  def passInTripData: Seq[PassInTrip] = Seq(
    PassInTrip(1100, LocalDateTime.parse("2003-04-29T00:00:00.000"), 1, "1a"),
    PassInTrip(1123, LocalDateTime.parse("2003-04-05T00:00:00.000"), 3, "2a"),
    PassInTrip(1123, LocalDateTime.parse("2003-04-08T00:00:00.000"), 1, "4c"),
    PassInTrip(1123, LocalDateTime.parse("2003-04-08T00:00:00.000"), 6, "4b"),
    PassInTrip(1124, LocalDateTime.parse("2003-04-02T00:00:00.000"), 2, "2d"),
    PassInTrip(1145, LocalDateTime.parse("2003-04-05T00:00:00.000"), 3, "2c"),
    PassInTrip(1181, LocalDateTime.parse("2003-04-01T00:00:00.000"), 1, "1a"),
    PassInTrip(1181, LocalDateTime.parse("2003-04-01T00:00:00.000"), 6, "1b"),
    PassInTrip(1181, LocalDateTime.parse("2003-04-01T00:00:00.000"), 8, "3c"),
    PassInTrip(1181, LocalDateTime.parse("2003-04-13T00:00:00.000"), 5, "1b"),
    PassInTrip(1182, LocalDateTime.parse("2003-04-13T00:00:00.000"), 5, "4b"),
    PassInTrip(1187, LocalDateTime.parse("2003-04-14T00:00:00.000"), 8, "3a"),
    PassInTrip(1188, LocalDateTime.parse("2003-04-01T00:00:00.000"), 8, "3a"),
    PassInTrip(1182, LocalDateTime.parse("2003-04-13T00:00:00.000"), 9, "6d"),
    PassInTrip(1145, LocalDateTime.parse("2003-04-25T00:00:00.000"), 5, "1d"),
    PassInTrip(1187, LocalDateTime.parse("2003-04-14T00:00:00.000"), 10, "3d"),
    PassInTrip(8882, LocalDateTime.parse("2005-11-06T00:00:00.000"), 37, "1a"),
    PassInTrip(7771, LocalDateTime.parse("2005-11-07T00:00:00.000"), 37, "1c"),
    PassInTrip(7772, LocalDateTime.parse("2005-11-07T00:00:00.000"), 37, "1a"),
    PassInTrip(8881, LocalDateTime.parse("2005-11-08T00:00:00.000"), 37, "1d"),
    PassInTrip(7778, LocalDateTime.parse("2005-11-05T00:00:00.000"), 10, "2a"),
    PassInTrip(7772, LocalDateTime.parse("2005-11-29T00:00:00.000"), 10, "3a"),
    PassInTrip(7771, LocalDateTime.parse("2005-11-04T00:00:00.000"), 11, "4a"),
    PassInTrip(7771, LocalDateTime.parse("2005-11-07T00:00:00.000"), 11, "1b"),
    PassInTrip(7771, LocalDateTime.parse("2005-11-09T00:00:00.000"), 11, "5a"),
    PassInTrip(7772, LocalDateTime.parse("2005-11-07T00:00:00.000"), 12, "1d"),
    PassInTrip(7773, LocalDateTime.parse("2005-11-07T00:00:00.000"), 13, "2d"),
    PassInTrip(7772, LocalDateTime.parse("2005-11-29T00:00:00.000"), 13, "1b"),
    PassInTrip(8882, LocalDateTime.parse("2005-11-13T00:00:00.000"), 14, "3d"),
    PassInTrip(7771, LocalDateTime.parse("2005-11-14T00:00:00.000"), 14, "4d"),
    PassInTrip(7771, LocalDateTime.parse("2005-11-16T00:00:00.000"), 14, "5d"),
    PassInTrip(7772, LocalDateTime.parse("2005-11-29T00:00:00.000"), 14, "1c")
  )

}
