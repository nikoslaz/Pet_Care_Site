package servlets;

import database.DB_Connection;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PetInfo extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String petId = request.getParameter("petId");

        // Check if petId is a number
        if (!petId.matches("\\d+")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid pet ID");
            return;
        }

        try (Connection conn = DB_Connection.getConnection(); Statement stmt = conn.createStatement()) {

            String query = "SELECT * FROM pets WHERE pet_id = " + petId;
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
                String petInfo = formatPetInfo(rs);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(petInfo);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Pet not found");
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing request");
        }
    }

    private String formatPetInfo(ResultSet rs) throws IOException, SQLException {
        // Format and return pet information in JSON format
        return String.format("{\"name\": \"%s\", \"type\": \"%s\", \"breed\": \"%s\", \"description\": \"%s\"}",
                rs.getString("name"), rs.getString("type"), rs.getString("breed"), rs.getString("description"));
    }
}
