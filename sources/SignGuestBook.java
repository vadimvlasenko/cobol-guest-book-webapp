import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class SignGuestBook extends HttpServlet {

    private static final String DATABASE_URL = "jdbc:postgresql://localhost/guestbookdb";
    private static final String DATABASE_USER = "postgres";
    private static final String DATABASE_PASSWORD = "password";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String answer = request.getParameter("answer");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String comment = request.getParameter("comment");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // HTML header and title
        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>GnuCOBOL Sample Guest Book - Signed Guest Book</title></head><body>");

        // Links to view and sign the guest book
        out.println("<h2><a href='/view-guest-book'>View Guest Book</a> | <a href='/sign-guest-book.html'>Sign Guest Book</a></h2>");

        // Process the new entry
        if (answer != null && answer.equals("20") && name != null && comment != null && !comment.trim().isEmpty()) {
            if (name.trim().isEmpty()) {
                name = "Anonymous";
            }
            // Insert into database
            try (Connection conn = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
                 PreparedStatement ps = conn.prepareStatement("INSERT INTO GUEST_ENTRY(GUEST_NAME, GUEST_EMAIL, GUEST_COMMENT) VALUES (?, ?, ?)")) {
                ps.setString(1, name);
                ps.setString(2, email);
                ps.setString(3, comment);
                ps.executeUpdate();
                out.println("<h2 style='text-align:center;'>Thank you for signing the guest book!</h2>");
            } catch (SQLException e) {
                out.println("<h2 style='color:red;'>Database error: " + e.getMessage() + "</h2>");
            }
        } else {
            out.println("<h2 style='color:red;'>Wrong answer or missing comment. Not saving entry.</h2>");
        }

        // Footer
        out.println("</body></html>");
    }
}