package servlets;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import database.DB_Connection;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DeleteUsers extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        StringBuilder requestData = new StringBuilder();
        String line;
        while ((line = request.getReader().readLine()) != null) {
            requestData.append(line);
        }

        JsonObject jsonObject = JsonParser.parseString(requestData.toString()).getAsJsonObject();
        JsonArray idsArray = jsonObject.get("ids").getAsJsonArray();
        String userType = jsonObject.get("type").getAsString();

        String table = "";
        if ("petowner".equals(userType)) {
            table = "petowners"; // Corrected table name for owners
        } else if ("petkeeper".equals(userType)) {
            table = "petkeepers"; // Corrected table name for keepers
        }

        try (Connection conn = DB_Connection.getConnection()) {
            conn.setAutoCommit(false);
            try (Statement stmt = conn.createStatement()) {
                for (int i = 0; i < idsArray.size(); i++) {
                    int userId = idsArray.get(i).getAsInt();
                    String sql = "DELETE FROM " + table + " WHERE keeper_id = " + userId; // Adjust column name if necessary
                    stmt.executeUpdate(sql);
                }
            }
            conn.commit();
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace(); // Log the exception
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DeleteUsers.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
