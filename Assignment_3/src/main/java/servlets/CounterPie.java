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

public class CounterPie extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(CounterPie.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        try (Connection conn = DB_Connection.getConnection(); Statement stmt = conn.createStatement()) {

            JsonObject jsonResponse = new JsonObject();

            // Query to count the number of pet keepers
            String queryKeepers = "SELECT COUNT(*) AS keeperCount FROM petkeepers";
            ResultSet rsKeepers = stmt.executeQuery(queryKeepers);
            if (rsKeepers.next()) {
                jsonResponse.addProperty("keepers", rsKeepers.getInt("keeperCount"));
            }

            // Query to count the number of pet owners
            String queryOwners = "SELECT COUNT(*) AS ownerCount FROM petowners";
            ResultSet rsOwners = stmt.executeQuery(queryOwners);
            if (rsOwners.next()) {
                jsonResponse.addProperty("owners", rsOwners.getInt("ownerCount"));
            }

            response.getWriter().write(jsonResponse.toString());
        } catch (SQLException | ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Error in PetOwnerKeeperCount Servlet", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
