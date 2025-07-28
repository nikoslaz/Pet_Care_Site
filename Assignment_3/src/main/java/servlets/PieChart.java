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

public class PieChart extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        try (Connection conn = DB_Connection.getConnection(); Statement stmt = conn.createStatement()) {

            JsonObject jsonResponse = new JsonObject();

            // Query to count the number of cats
            String queryCats = "SELECT COUNT(*) AS catCount FROM pets WHERE type='Cat'";
            ResultSet rsCats = stmt.executeQuery(queryCats);
            if (rsCats.next()) {
                jsonResponse.addProperty("cats", rsCats.getInt("catCount"));
            }

            // Query to count the number of dogs
            String queryDogs = "SELECT COUNT(*) AS dogCount FROM pets WHERE type='Dog'";
            ResultSet rsDogs = stmt.executeQuery(queryDogs);
            if (rsDogs.next()) {
                jsonResponse.addProperty("dogs", rsDogs.getInt("dogCount"));
            }

            response.getWriter().write(jsonResponse.toString());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
