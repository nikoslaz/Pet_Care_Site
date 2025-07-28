package servlets;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import database.DB_Connection;
import org.json.JSONArray;
import org.json.JSONObject;

public class FetchMessages extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String bookingId = request.getParameter("bookingId");

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        try (Connection con = DB_Connection.getConnection()) {
            Statement stmt = con.createStatement();
            String query = "SELECT * FROM messages WHERE booking_id = '" + bookingId + "'";
            ResultSet rs = stmt.executeQuery(query);
            JSONArray messages = new JSONArray();

            while (rs.next()) {
                JSONObject message = new JSONObject();
                message.put("message_id", rs.getInt("message_id"));
                message.put("booking_id", rs.getInt("booking_id"));
                message.put("message", rs.getString("message"));
                message.put("sender", rs.getString("sender"));
                message.put("datetime", rs.getTimestamp("datetime").toString());
                messages.put(message);
            }

            out.print(messages.toString());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
