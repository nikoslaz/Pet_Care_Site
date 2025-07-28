package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import database.tables.EditPetKeepersTable;
import jakarta.servlet.http.HttpSession;
import mainClasses.PetKeeper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UpdatePetKeeper extends HttpServlet {

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
        HttpSession session = request.getSession(false);
        String username = (String) session.getAttribute("username");
        JSONObject jsonObject = new JSONObject(sb.toString());
        PetKeeper PetKeeper = new PetKeeper();
        PetKeeper.setUsername(username);
        PetKeeper.setFirstname(jsonObject.getString("first_name"));
        PetKeeper.setLastname(jsonObject.getString("last_name"));
        PetKeeper.setPassword(jsonObject.getString("password"));
        PetKeeper.setAddress(jsonObject.getString("address"));
        PetKeeper.setCity(jsonObject.getString("town"));
        PetKeeper.setJob(jsonObject.getString("job"));
        PetKeeper.setBirthdate(jsonObject.getString("birthday"));
        PetKeeper.setLat(jsonObject.getDouble("lat"));
        PetKeeper.setLon(jsonObject.getDouble("lon"));
        PetKeeper.setPersonalpage(jsonObject.getString("url"));
        PetKeeper.setTelephone(jsonObject.getString("phone"));
        PetKeeper.setCountry(jsonObject.getString("country"));
        PetKeeper.setProperty(jsonObject.getString("propertyDescription"));
        EditPetKeepersTable editPetKeepersTable = new EditPetKeepersTable();

        JSONObject jsonResponse = new JSONObject();

        try {
            editPetKeepersTable.updatePetKeeper2(PetKeeper);
            jsonResponse.put("status", "success");
            jsonResponse.put("message", "Update successful.");
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (SQLException ex) {
            jsonResponse.put("status", "error");
            jsonResponse.put("message", "Database error: " + ex.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Logger.getLogger(UpdatePetKeeper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            jsonResponse.put("status", "error");
            jsonResponse.put("message", "Class not found error: " + ex.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Logger.getLogger(UpdatePetKeeper.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Send JSON response
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(jsonResponse);
        out.flush();
    }
}
