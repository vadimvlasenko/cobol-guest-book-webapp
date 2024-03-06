import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class ViewGuestBook extends HttpServlet {

    private static final String DATABASE_URL = "jdbc:postgresql://localhost/guestbookdb";
    private static final String DATABASE_USER = "postgres";
    private static final String DATABASE_PASSWORD = "password";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // HTML header and title
        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>GnuCOBOL Sample Guest Book - View Guest Book</title></head><body>");

        // Link to the sign guest book page
        out.println("<h2>View Guest Book | <a href='/sign-guest-book.html'>Sign Guest Book</a></h2>");

        try (Connection conn = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT GUEST_NAME, GUEST_EMAIL, GUEST_COMMENT, CREATE_DT FROM GUEST_ENTRY ORDER BY CREATE_DT DESC")) {

            while (rs.next()) {
                String name = rs.getString("GUEST_NAME");
                String email = rs.getString("GUEST_EMAIL");
                String date = rs.getString("CREATE_DT");
                String comment = rs.getString("GUEST_COMMENT");

                out.println("<p><table>");
                out.println("<tr><td>Name:</td><td>" + name + "</td></tr>");
                out.println("<tr><td>Email:</td><td>" + email + "</td></tr>");
                out.println("<tr><td>Date:</td><td>" + date + "</td></tr>");
                out.println("<tr><td>Comment:</td><td>" + comment + "</td></tr>");
                out.println("</table></p><hr />");
            }
        } catch (SQLException e) {
            out.println("<h2 style='color:red;'>Database error: " + e.getMessage() + "</h2>");
        }

        // Footer
        out.println("</body></html>");
    }
}
