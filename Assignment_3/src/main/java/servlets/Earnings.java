package servlets;

import com.google.gson.JsonObject;
import database.DB_Connection;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Earnings extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(Earnings.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        try (Connection conn = DB_Connection.getConnection(); Statement stmt = conn.createStatement()) {

            JsonObject jsonResponse = new JsonObject();

            // Update the table name to 'bookings'
            String query = "SELECT SUM(price) AS totalEarnings FROM bookings WHERE status='Finished'";
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                double totalEarnings = rs.getDouble("totalEarnings");
                double keeperEarnings = totalEarnings * 0.85;
                double appEarnings = totalEarnings * 0.15;

                jsonResponse.addProperty("keeperEarnings", keeperEarnings);
                jsonResponse.addProperty("appEarnings", appEarnings);

                LOGGER.info("Total Earnings: " + totalEarnings);
                LOGGER.info("Keeper Earnings: " + keeperEarnings);
                LOGGER.info("App Earnings: " + appEarnings);
            } else {
                LOGGER.warning("No earnings data found.");
                jsonResponse.addProperty("message", "No earnings data available");
            }

            response.getWriter().write(jsonResponse.toString());
        } catch (SQLException | ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Error in Earnings Servlet", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
