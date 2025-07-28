package servlets;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import database.DB_Connection;
import java.util.Date;
import java.text.SimpleDateFormat;


public class SendMessage extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Date currentDate = new Date();
        String bookingId = "1";
        String messageContent = request.getParameter("message");
        String sender = "keeper";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datetime = sdf.format(currentDate);

        try (Connection con = DB_Connection.getConnection()) {
            Statement stmt = con.createStatement();
            String query = "INSERT INTO messages (booking_id, message, sender, datetime) VALUES "
                    + "('" + bookingId + "', '" + messageContent + "', '" + sender + "', '" + datetime + "')";
            int result = stmt.executeUpdate(query);
            if (result > 0) {
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
