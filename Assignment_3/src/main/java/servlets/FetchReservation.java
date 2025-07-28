package servlets;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import database.DB_Connection;

public class FetchReservation extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String petKeeperId = request.getParameter("userId"); // Assuming you get keeper_id from request
        JSONArray reservationsArray = new JSONArray();
        String query = "SELECT * FROM bookings WHERE keeper_id = " + petKeeperId + " AND status = 'requested'";

        try (Connection conn = DB_Connection.getConnection(); Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                JSONObject reservation = new JSONObject();
                reservation.put("booking_id", rs.getInt("booking_id"));
                reservation.put("owner_id", rs.getInt("owner_id"));
                reservation.put("pet_id", rs.getInt("pet_id"));
                reservation.put("fromdate", rs.getDate("fromdate").toString());
                reservation.put("todate", rs.getDate("todate").toString());
                reservation.put("status", rs.getString("status"));
                reservation.put("price", rs.getDouble("price"));
                reservationsArray.put(reservation);
            }

            response.setContentType("application/json");
            response.getWriter().write(reservationsArray.toString());
        } catch (SQLException | ClassNotFoundException ex) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred");
            // Log the exception
        }
    }
}
