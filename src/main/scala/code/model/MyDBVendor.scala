package code.model

import net.liftweb.mapper.{ ConnectionIdentifier, ConnectionManager, Schemifier }
import java.sql.{ Connection, DriverManager }
import net.liftweb.common.{ Box, Empty, Full }
import net.liftweb.util.Props

object MyDBVendor extends ConnectionManager {
  private var pool: List[Connection] = Nil
  private var poolSize = 0
  private val maxPoolSize = 4

  private lazy val chooseDriver = Props.mode match {
    case Props.RunModes.Production => "com.mysql.jdbc.Driver"
    case _ => "com.mysql.jdbc.Driver"
  }

  private lazy val chooseURL = Props.mode match {
    case Props.RunModes.Production => "jdbc:mysql://localhost:3306/startup"
    case _ => "jdbc:mysql://localhost:3306/startup"
  }

  private def createOne: Box[Connection] = try {
    val driverName: String = Props.get("mysql.db.driver") openOr chooseDriver
    val dbUrl: String = Props.get("mysql.db.url") openOr chooseURL
    Class.forName(driverName)
    Full((Props.get("mysql.db.user"), Props.get("mysql.db.password")) match {
      case (Full(user), Full(pwd)) => DriverManager.getConnection(dbUrl, user, pwd)
      case _ => DriverManager.getConnection(dbUrl)
    })
  } catch {
    case e: Exception => e.printStackTrace; Empty
  }

  def newConnection(name: ConnectionIdentifier): Box[Connection] =
    synchronized {
      pool match {
        case Nil if poolSize < maxPoolSize => {
          val ret = createOne
          poolSize = poolSize + 1
          ret.foreach(c => pool = c :: pool)
          ret
        }

        case Nil =>
          wait(1000L)
          newConnection(name)

        case x :: xs => try {
          x.setAutoCommit(false)
          Full(x)
        } catch {
          case e: Exception => try {
            pool = xs
            poolSize = poolSize - 1
            x.close
            newConnection(name)
          } catch {
            case e: Exception => newConnection(name)
          }
        }
      }
    }

  def releaseConnection(conn: Connection): Unit = synchronized {
    pool = conn :: pool
    notify
  }

}