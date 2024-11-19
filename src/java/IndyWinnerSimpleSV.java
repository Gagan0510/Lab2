/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
/*
 * File Name: IndyWinnerSimpleSV
 * Author: Gagandeep kaur Sangha, ID 041004212
 * Course: CST8288
 * Lab:2
 * Date:11/17/2024
 * Professor: Sazzad Hossain
*/
import java.io.IOException;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.sql.*;
import jakarta.servlet.annotation.WebServlet;

/**
 * This servlet fetches information about Indianapolis 500 winners from a database
 * using JDBC and displays it as an HTML table. 
 * 
 * Limitations:
 * - Not thread-safe
 * - Does not follow SOLID principles
 * - Lacks Data Access Object (DAO) pattern implementation
 * - No pagination support
 * 
 * This is a basic, straightforward approach to querying a database and 
 * formatting the result set directly into an HTML table.
 */
@WebServlet(urlPatterns = {"/IndyWinnerSimpleSV"})
public class IndyWinnerSimpleSV extends HttpServlet {

    private final StringBuilder buffer = new StringBuilder();

    /**
     * Processes HTTP <code>POST</code> requests.
     *
     * @param request  the HTTP request object
     * @param response the HTTP response object
     * @throws ServletException if a servlet-related error occurs
     * @throws IOException if an input/output error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    /**
     * Processes HTTP <code>GET</code> requests.
     *
     * @param request  the HTTP request object
     * @param response the HTTP response object
     * @throws ServletException if an error specific to servlets occurs
     * @throws IOException if there is an input/output error
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get the page parameter from the request (default to page 1)
        int page = 1;
        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            try {
                page = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                page = 1; // default to page 1 if invalid
            }
        }

        // Calculate the OFFSET for pagination
        int offset = (page - 1) * 10;  // Show 10 winners per page

        // Prepare the response
        String uri = request.getRequestURI();
        response.setContentType("text/html");

        // Create buffer to store the HTML content
        StringBuilder buffer = new StringBuilder();

        // Format the page header
        formatPageHeader(buffer);

        // Query the database for winners (limited by the OFFSET and LIMIT)
        String query = "SELECT * FROM IndyWinners ORDER BY year DESC LIMIT 10 OFFSET " + offset;
        sqlQuery("com.mysql.cj.jdbc.Driver",  // Database Driver
                "jdbc:mysql://localhost:3306/IndyWinners",  // Database URL
                "root", "Gagan@0510",  // Database username and password
                query, buffer, uri);  // Query and buffer

        // Add pagination controls
        formatPaginationControls(page, buffer);

        // End HTML structure
        buffer.append("</html>");

        // Send the formatted page back to the client
        try (java.io.PrintWriter out = new java.io.PrintWriter(response.getOutputStream())) {
            out.println(buffer.toString());
            out.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Formats the HTML header for the response page.
     *
     * @param buffer the StringBuilder used to construct the HTML content
     */
    private void formatPageHeader(StringBuilder buffer) {
        // Begin HTML structure
        buffer.append("<!DOCTYPE html>")
                .append("<html lang='en'>")
                .append("<head>")
                .append("<meta charset='UTF-8'>")
                .append("<meta name='viewport' content='width=device-width, initial-scale=1.0'>")
                .append("<title>Indy 500 Winners</title>")
                .append("<style>")
                .append("body { font-family: Arial, sans-serif; background-color: #f0f0f0; }")
                .append("h2 { text-align: center; color: #333; }")
                .append("</style>")
                .append("</head>")
                .append("<body>");

        // Page header
        buffer.append("<header>")
                .append("<h2>Indianapolis 500 Winners</h2>")
                .append("</header>")
                .append("<hr>")
                .append("<div style='margin: 20px;'>");
    }

    /**
     * Executes a given SQL query using the specified JDBC driver, database URL, and credentials,
     * then formats the result set into an HTML table.
     *
     * @param driverName     the name of the JDBC driver
     * @param connectionURL  the URL for the database connection
     * @param user           the database username
     * @param pass           the password for the database
     * @param query          the SQL query to be executed
     * @param buffer         the StringBuilder used to format and store the HTML table output
     * @param uri            the request URI
     * @return true if the query executes successfully
     */
    private void sqlQuery(String driverName,
                          String connectionURL,
                          String user, String pass,
                          String query,
                          StringBuilder buffer,
                          String uri) {
        boolean rc = true;

        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        /*	Keep stats for how long it takes to connect and execute the query		*/
        long startMS = System.currentTimeMillis();
        /*	Keep the number of rows in the ResultSet										*/
        int rowCount = 0;

        try {
            /*	Create an instance of the JDBC driver so that it has
             *	a chance to register itself
             *		Get a new database connection.
             *		Create a statement object that we can execute queries	with
             *		Execute the query
             */
            Class.forName(driverName);
            con = DriverManager.getConnection(connectionURL, user, pass);
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);

            /*	Format the result set into an HTML table								*/
            rowCount = resultSetToHTML(rs, buffer, uri);

        } catch (Exception ex) {
            /*	Send the error back to the client										*/
            buffer.append("Exception!");
            buffer.append(ex.toString());
            rc = false;
        } finally {
            try {
                /*	Always close properly													*/
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException sqlEx) {
                /*	Ignore any errors here													*/
            }
        }

        /*	If we queried the table successfully, output some statistics		*/
        if (rc) {
            long elapsed = System.currentTimeMillis() - startMS;
            buffer.append("<br><i> (");
            buffer.append(rowCount);
            buffer.append(" rows in ");
            buffer.append(elapsed);
            buffer.append("ms) </i>");
        }

    }

    /**
     * <p>
     * Given a JDBC ResultSet, format the results into a simple HTML table
     * @param rs JDBC ResultSet
     * @param buffer Free space for building strings
     * @param uri Requesting URI (could be used for form actions)
     * @return The number of rows in the ResultSet
     */
    private int resultSetToHTML(java.sql.ResultSet rs,
                                StringBuilder buffer,
                                String uri)
            throws Exception {

        int rowCount = 0;

        /*	Create the table and center it												*/
        buffer.append("<center><table border>");

        /*	Start the table row
         *	create table header values from meta data
         *	Process all available rows in the database for the query
         */
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        buffer.append("<tr>");
        for (int i = 0; i < columnCount; i++) {
            buffer.append("<th>");
            buffer.append(rsmd.getColumnLabel(i + 1));
            buffer.append("</th>");
        }

        buffer.append("</tr>");

        /*	Now walk through the entire ResultSet and get each row				*/
        while (rs.next()) {
            rowCount++;

            buffer.append("<tr>");
            for (int i = 0; i < columnCount; i++) {
                String data = rs.getString(i + 1);
                buffer.append("<td>");
                buffer.append(data);
                buffer.append("</td>");
            }
            /*	End the table row																*/
            buffer.append("</tr>");
        }

        /*	End the table																		*/
        buffer.append("</table></center>");

        /*	Could create a new form here and add a button resulting
         *	in new input from the client for maybe a next page??
         */
        return rowCount;
    }

    /**
     * Formats pagination controls for navigating between pages of results.
     *
     * @param currentPage The current page number.
     * @param buffer The StringBuilder used to construct the HTML content.
     */
    private void formatPaginationControls(int currentPage, StringBuilder buffer) {
        // Add a "Continue" button or link to go to the next page
        buffer.append("<div style='text-align: center;'>");
        buffer.append("<br>");
        buffer.append("<a href='?page=").append(currentPage + 1).append("'>Continue</a>");
        buffer.append("</div>");
    }
}