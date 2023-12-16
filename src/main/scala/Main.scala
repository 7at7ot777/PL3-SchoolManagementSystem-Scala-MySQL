import sun.jvm.hotspot.HelloWorld.e

import java.sql.{Connection, DriverManager, PreparedStatement}

object Main extends App {
 var connection: Connection = null
 connection = DatabaseConfig.getConnection



}

// val usersQuery = "SELECT * FROM USERS"
// val statement :PreparedStatement = connection.prepareStatement(usersQuery)
//// val bookingStatement: PreparedStatement = connection.prepareStatement(bookingQuery)
//// bookingStatement.setInt(1, booking_id)
//// bookingStatement.setInt(2, billValue)
// val users = statement.executeQuery()
// while(users.next())
//  {
//   println(users.getString("name") )
//  }
