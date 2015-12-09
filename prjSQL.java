
package prjSQL;
//STEP 1. Import required packages
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;
/**
 * @author karissamouser
 * part number = DRESSBRAND IS MY PARTS NUMBER
 * Write a Java program that will allow all four CRUD operations
 */
public class prjSQL {
	// 1. CONSTANTS
	private final static String DBF_NAME = "javasql"; 
	private final static String TABLE_NAME = "weddinginventory";
	   // 2.DBF
	private final String userName = "root";
	    // The mySQL password (may be empty "" )
	private final String password = "root";
	    // Name of the computer running mySQL
	private final String serverName = "localhost";
	    // Port of the MySQL server (default is 3306 or 8889 on MAMP)
	private final int portNumber = 8889;
		
	//3. MAIN
	public static void main(String[] argv) {
	
	 // Simulate data for Enterprise Resource Planning this is for the dressBrands to fill out 
	//name venue new last name stationary color number of invites needed
	String[ ] dataInput = {"white dress", "vera wang", "700.00", "four in stock", "243.00"};
	prjSQL app = new prjSQL();
	
	app.createTable(TABLE_NAME);
	app.insertData(dataInput, TABLE_NAME);
	
	app.showTable(TABLE_NAME);
	
	// Simulate new data input by user for UPDATE
	dataInput[0] = "off white dress";
	dataInput[1] = "fux prada";
	dataInput[2] = "207.00";
	dataInput[3] = "five in stock";
	dataInput[4] = "112.00";
	
	app.update(dataInput, 1 , TABLE_NAME);
	app.showTable(TABLE_NAME);
	app.delete(3,TABLE_NAME);
	app.showTable(TABLE_NAME);
	app.getdressBrand(TABLE_NAME);
	//app.dropTable(TABLE_NAME); 
	//DROP TABLE IF NEEDED
	} // end of main( )

/**
* createTable - create a new table in the database
*  basic createTable( ) method
*  process
*  1. Open a connection Connect to a database**********************
*  2. Set up an SQL statement. 
*  3. Send the SQL statement to the database server using executeUpdate( ) 
*/
public void createTable(String tableName)
{
	//Connect var
	Connection conn = null;
	String sql = "";
	//rs is ResultSet
	//ResultSet to hold the reply from the database and a boolean flag so the
	//program knows if a table already exists or not.
	ResultSet rs = null;
	boolean createTable = true;
	//Connect to MySQL 1 Open a connection 
		try
		{
		conn = this.getConnection();
		System.out.println("You made it, take control your database now! - Connect to a database. -");
		}
		catch (SQLException e)
		{
		System.out.println("Connection Failed! Check output console");
		e.printStackTrace();
		return;
		}
		//now we can create a table
	try
	{
	//Check to see if a table already exists
	DatabaseMetaData meta = conn.getMetaData();
	rs = meta.getTables(null, null, "%", null);
	//Loop through looking for the table name
		while (rs.next())
		{
			if(rs.getString(3).equals(tableName))
				{
				createTable=false;
				break;
				}
		}
		if(createTable)
		{
		// SQL CODE HERE: CREATE TABLE 2 Set up an SQL statement. NOT RETURNING DATA **********************************
		String createString =	
		"CREATE TABLE " + tableName + " ( " +
		"id INTEGER NOT NULL AUTO_INCREMENT, " +
		"dressColor varchar(40) NOT NULL, " +
		"dressBrand varchar(40) NOT NULL, " +
		"dressPrice char(100) NOT NULL, " +
		"inStock varchar(20) NOT NULL, " +
		"discount char(100), " +
		"PRIMARY KEY (id))";
		//3 Send the SQL statement to the database 
		this.executeUpdate(conn, createString);
		System.out.println("Created table named:" +
		tableName);
		}
			else // table already exists do nothing
				{
				System.out.println(tableName + " Sorry that table is already in the database. - a new table was already created - This SQL statements is not returning any data.");
				}
			}
			catch (SQLException e)
			{
				System.out.println("ERROR: Could not create the table named: " + tableName);
				e.printStackTrace();
				return;
			}
		//release the resources we are using 4 Release the resources using releaseResource( )******************************
		finally { releaseResource(rs, null, conn); }
} // end of createTable( )


 /**
  * We have our connection. we have our table, Now we want to add new records into the table.
  * Don’t forget to call the function in main( )
	 * insertData
	 */
 public void insertData(String[ ] dataArray, String
thisTable)
 {
	 Connection conn = null;
	 String sql = "";
	 try 
	 {
	 conn = this.getConnection();
	 } catch (SQLException e) 
	 	{
		 System.out.println("ERROR: Could not connect to the database");
		 e.printStackTrace();
		 }
			 // Insert the data
		 try
		 {
		 /* SQL CODE HERE:
		 * sql = "INSERT INTO Insert records into the table.
		 */
		 sql = "INSERT INTO " + thisTable + " (dressColor, dressBrand, dressPrice, inStock, discount) VALUES("
		 + "'" + dataArray[0] + "', "
		 + "'" + dataArray[1] + "', "
		 + "'" + dataArray[2] + "', "
		 + "'" + dataArray[3] + "', "
		 + "'" + dataArray[4] + "')";
		 this.executeUpdate(conn, sql);
		 System.out.println("Inserted a record: Dress Color: " +
		dataArray[0] + " Brand: " + dataArray[1] + " Price: " + dataArray[2]+ " , "  + dataArray[3] + " , Discount:  "  + dataArray[4]);
		 }
			 catch (SQLException e)
			 {
			 System.out.println("ERROR: Could not insert the data using this SQL: " + sql);
			 e.printStackTrace();
			 }
				 // Release the resources
				 finally { releaseResource(null, null, conn); }
 }// end of insertData( )
 
			 
 /**
  *  showTable( ) method  Display all the data in the table for any given category
  */
  public void showTable(String tableName)
  {
	  // set variables to hold the data from the table.
	  String sql = "";
	  Statement stmt = null;
	  ResultSet rs = null;
	  int id = 0;
	  String dressColor = "";
	  String dressBrand = "";
	  String dressPrice = "";
	  String inStock = "";
	  String discount = "";
	
	  // Open up a connection to MySQL
	  Connection conn = null;
		  try {
		  conn = this.getConnection();
		  }
			 catch (SQLException e)
		  	{
				 System.out.println("ERROR: Could not connect to the database");
				 e.printStackTrace();
		  	}

		  // Use the SQL SELECT command to extract all of the fields from the table 
		  try
		  {
			  sql = "SELECT * FROM weddinginventory";
			  // Run the SQL and save in Result Set
			  stmt = conn.createStatement( );
			  //Use the executeQuery( ) to run the query
			  rs = stmt.executeQuery(sql);
			  System.out.println("\nID\tDRESSCOLORS\t\tdressBrand\t\t PRICE  |  INSTOCK  |  Discount - SQL statements is returning a ResultSet");

			  System.out.println("***************************************************************************************");
				   while (rs.next())
				   {
					   id = rs.getInt("id");
					   dressColor = rs.getString("dressColor");
					   dressBrand = rs.getString("dressBrand");
					   dressPrice = rs.getString("dressPrice");
					   inStock = rs.getString("inStock");
					   discount = rs.getString("discount");
					   System.out.printf("%d\t%s\t\t%s\t\t%s, %s   %s \n",
					   id, dressColor, dressBrand, dressPrice, inStock , discount);
				   }
		   }
			   catch (SQLException e)
			   {
				   System.out.println("ERROR: Could not SELECT data using this SQL: " + sql);
				   e.printStackTrace();
			   }
		   // Release the resources
		   finally { releaseResource(rs, stmt, conn); }
   } // end of showTable( )
		  
   /**
   * update( ) - update a specific record Update a record in the database with new data
   * @param thisTable
   */
   public void update(String[ ] dataArray, int thisID, String thisTable){
   Connection conn = null;
   String sql = "";

	   try
	   {
		   conn = this.getConnection();
	   }
	   	catch (SQLException e)
	   	{
	   		System.out.println("ERROR: Could not connect to the database");
	   		e.printStackTrace();
	   	}

   // Update a record
   try
   {
	   /* REFERENCE SQL: dressColor, dressBrand, dressPrice, inStock, discount
	   * sql = "UPDATE weddinginventory SQL statements that return a ResultSet with data."
	   */
	   sql = "UPDATE " + thisTable
	   + " SET dressColor ='" + dataArray[0] + "', "
	   + "dressBrand ='" + dataArray[1] + "', "
	   + "dressPrice ='" + dataArray[2] + "', "
	   + "inStock ='" + dataArray[3] + "', "
	   + "discount ='" + dataArray[4] + "' "
	   + "WHERE id =" + thisID;
	   this.executeUpdate(conn, sql);
	   System.out.println("\n NOTE: Updates are happening");
	   
	   	}
	   	catch (SQLException e)
	   	{
   		System.out.println("ERROR: Could not update the"
	   		+ "record using this SQL: " + sql);
   		e.printStackTrace();
}
// Release the resources
   finally { releaseResource(null, null, conn); }
   }// end of insertData( )
			   
			   

   public  void getdressBrand(String tableName) {
	   
	  // set variables to hold the data from the table.
	  String sql = "";
	  Statement stmt = null;
	  ResultSet rs = null;
	  int id = 0;
	  String dressColor = "";
	
	  String dressBrand = "";
	  String dressBrandChars = "";
	  String dressPrice = "";
	  String inStock = "";
	  String discount = "";
	
	   //Open up a connection to MySQL
	  Connection conn = null;
	  try {
	  conn = this.getConnection();
	  	}
	  	catch (SQLException e)
	  	{
	  System.out.println("ERROR: Could not connect to the database");
	  e.printStackTrace();
	  	}

		  // Display all the data in the table for any given category 
		  // Select the data
		
	  try
	  {
	  sql = "SELECT id, dressBrand FROM weddinginventory";
	  
	  // Run the SQL and save in Result Set
	  stmt = conn.createStatement( );
	  //Use the executeQuery( ) to run the query
	  rs = stmt.executeQuery(sql);
	  System.out.println("\nID\tDRESSBRAND - Display all the data in the table for any given category ");

		  System.out.println("*******************");
		   while (rs.next())
		   {
			   id = rs.getInt("id");
			   dressBrand = rs.getString("dressBrand");
			   
			   System.out.printf("%d\t%s %s   %s \n",
			   id, dressColor, dressBrand, dressPrice, inStock , discount);}
			   }
			   catch (SQLException e)
			   {
			   System.out.println("ERROR: Could not SELECT data using this SQL: " + sql);
			   e.printStackTrace();
			   }
			   // Release the resources
				   finally { releaseResource(rs, stmt, conn);
				   }
   }
   
   /**
* delete( ) - remove a record based on id **************************************
*/
public void delete(int thisID, String thisTable)
{
Connection conn = null;
String sql = "";

	try
	{
	conn = this.getConnection();

	} catch (SQLException e) {
			System.out.println("ERROR: Could not connect to the database");
			e.printStackTrace();
							}

	 // Delete a record
	 try
	 {
	   	 /* REFERENCE SQL:
	   	 * sql = "DELETE FROM customer WHERE id = 15";
	   	 */
	   	 sql = "DELETE FROM " + thisTable + " WHERE id = " +
   			 thisID;
	   	 this.executeUpdate(conn, sql);
	   	System.out.println("\n Delete 3 ");
   	 	}
   	 	catch (SQLException e)
   	 	{
   	 		System.out.println("ERROR: Could delete the record using this SQL: " + sql);
   	 		e.printStackTrace();
   	 	}
   	 // Release the resources
	 finally { releaseResource(null, null, conn); }
 }// end of delete( )

 /**
 * dropTable
 */
 public void dropTable(String tableName)
 {
	 String sql = "";
	 Connection conn = null;
	 try
	 {
		 conn = this.getConnection();
	 } 
	 	catch (SQLException e)
	 {
	 		System.out.println("ERROR: Could not connect to the database");
	 		e.printStackTrace();
	 }
 try
 {
	 sql = "DROP TABLE " + tableName;
	 this.executeUpdate(conn, sql);
	 System.out.println("Dropped the table named:" + tableName);
 }
 	catch (SQLException e)
 	{
		 System.out.println("ERROR: Could not drop the table using this SQL: " + sql);
		 e.printStackTrace();
		 return;
	 }
 	finally { releaseResource(null,null, conn);}
 } // end of dropTable( )
   
			   
			   
			   
			   
			   
			   
			  
			  
			  
			  
			  
			  
			  
			 
			 
			 
			 
			 
			 
			 
 /* ******************************
  * CRUD methods.
  *********************************/
  /**
  */
  public Connection getConnection() throws SQLException
  {
  Connection conn = null;
  Properties connectionProps = new Properties();
  connectionProps.put("user", this.userName);
  connectionProps.put("password", this.password);
  conn = DriverManager.getConnection("jdbc:mysql://"
  + this.serverName + ":" + this.portNumber + "/"
 + DBF_NAME,
  connectionProps);
  return conn;
  }
  
  /**
   * CREATE/INSERT/UPDATE/DELETE/DROP
    */
    public boolean executeUpdate(Connection conn, String
   command) throws SQLException
    {
    Statement stmt = null;
    try
    {
    stmt = conn.createStatement();
    stmt.executeUpdate(command); // This will throw a SQLException if it fails
    return true;
    }
    finally
    {
    // This will run whether we throw an exception or not
    if (stmt != null) { stmt.close(); }
    }
    } // end of executeUpdate( )

  
  
  /**
  * releaseResource( ) Run SQL statements that return a ResultSet with data.
  */
  
  public void releaseResource(ResultSet rs, Statement ps,
 Connection conn )
  {
  if (rs != null)
  {
  try { rs.close(); }
  catch (SQLException e) { /* ignored */}
  }
  if (ps != null)
  {
  try { ps.close(); }
  catch (SQLException e) { /* ignored */}
  }
  if (conn != null)
  {
  try { conn.close();}
  catch (SQLException e) { /* ignored */}
  }
  } // end of releaseResource( )




	    }//END******************************