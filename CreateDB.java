import java.sql.*;

/**
 * Creates database for golf stat application
 *
 * @author (Thomas Hart)
 * @version (June 16, 2020)
 */
public class CreateDB
{
    public static void main(String[] args)
    {
        // Creating databse URL
        final String DB_URL = "jdbc:derby:GolfStatDB;create=true";

        try
        {
            // Creating databse connection
            Connection conn = DriverManager.getConnection(DB_URL);

            // Dropping existing tables
            dropTables(conn);
            System.out.println("Tables dropped.");

            // Building stats table
            buildTable(conn);
            System.out.println("Stats Table built.");

            // Closing connection
            conn.close();
        }
        catch (Exception ex)
        {
            System.out.println("ERROR: " + ex.getMessage());
        }
    }
  
    // Method to drop existing tables
    public static void dropTables(Connection conn)
    {
        System.out.println("Checking for existing tables.");

        try
        {
            // Creating statement object
            Statement stmt  = conn.createStatement();;

            try
            {
                // Dropping stats table
                stmt.execute("DROP TABLE Stats");
                System.out.println("Stats table dropped.");
            }
            catch(SQLException ex)
            {
            }

        }
        catch(SQLException ex)
        {
            System.out.println("ERROR Dropping Tables: " + ex.getMessage());
            ex.printStackTrace();
        }
    }    
    
    // Method to build table
    public static void buildTable(Connection conn)
    {
        try
        {
            // Creating statement object
            Statement stmt = conn.createStatement();
              
            // Creating table
            stmt.execute("CREATE TABLE Stats" +
                "( CourseRating INT,"+
                "  SlopeRating INT,"+
                "  Par INT," +
                "  Score INT,"+
                "  FairwaysHit INT,"+
                "  FairwaysPotential INT,"+
                "  GreensHit INT,"+
                "  Pars INT,"+
                "  Birdies INT,"+
                "  Eagles INT,"+
                "  PuttsTotal INT )"); 

            System.out.println("Stat table created");
        }
        catch (SQLException ex)
        {
            System.out.println("ERROR Stat Table: " + ex.getMessage());
        }
    }
}
