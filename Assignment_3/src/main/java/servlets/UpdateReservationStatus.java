package servlets;

import database.DB_Connection;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import org.json.JSONObject;

public class UpdateReservationStatus extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid request data");
            return;
        }

        JSONObject jsonObject = new JSONObject(sb.toString());
        int bookingId = jsonObject.getInt("bookingId");
        String status = jsonObject.getString("status");

        try (Connection conn = DB_Connection.getConnection(); Statement stmt = conn.createStatement()) {
            String sql = "UPDATE bookings SET status = '" + status + "' WHERE booking_id = " + bookingId;
            int updatedRows = stmt.executeUpdate(sql);

            if (updatedRows > 0) {
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred");
            // Log exception
        }
    }
}
