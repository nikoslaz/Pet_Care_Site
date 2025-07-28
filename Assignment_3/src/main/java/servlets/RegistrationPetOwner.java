package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import org.json.JSONObject;
import database.tables.EditPetOwnersTable;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import mainClasses.PetOwner;

public class RegistrationPetOwner extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("doPost registration");
        // Parse JSON data from client
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
        System.out.println(jsonObject);
        // Extract user data and create PetOwner object
        PetOwner petOwner = new PetOwner();
        petOwner.setFirstname(jsonObject.getString("first_name"));
        petOwner.setLastname(jsonObject.getString("last_name"));
        petOwner.setUsername(jsonObject.getString("username"));
        petOwner.setEmail(jsonObject.getString("email"));
        petOwner.setPassword(jsonObject.getString("password"));
        petOwner.setAddress(jsonObject.getString("address"));
        petOwner.setCity(jsonObject.getString("town"));
        petOwner.setJob(jsonObject.getString("job"));
        petOwner.setBirthdate(jsonObject.getString("birthday"));
        petOwner.setGender(jsonObject.getString("gender"));
        petOwner.setLat(jsonObject.getDouble("lat"));
        petOwner.setLon(jsonObject.getDouble("lon"));
        petOwner.setPersonalpage(jsonObject.getString("url"));
        petOwner.setTelephone(jsonObject.getString("phone"));
        petOwner.setCountry(jsonObject.getString("country"));

        EditPetOwnersTable editPetOwnersTable = new EditPetOwnersTable();
        JSONObject jsonResponse = new JSONObject();

        try {
            // Check if user already exists in the database
            if (editPetOwnersTable.databaseToPetOwners(petOwner.getUsername(), petOwner.getPassword()) != null) {
                // User already exists, send error response
                jsonResponse.put("status", "error");
                jsonResponse.put("message", "User with provided username or password already exists.");
                response.setStatus(HttpServletResponse.SC_CONFLICT);
            } else {
                editPetOwnersTable.addNewPetOwner(petOwner);
                response.setStatus(HttpServletResponse.SC_OK);
                jsonResponse.put("status", "success");
                jsonResponse.put("message", "Registration successful.");
                response.setStatus(HttpServletResponse.SC_OK);
            }
        } catch (SQLException ex) {
            jsonResponse.put("status", "error");
            jsonResponse.put("message", "Database error: " + ex.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Logger.getLogger(RegistrationPetOwner.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            jsonResponse.put("status", "error");
            jsonResponse.put("message", "Class not found error: " + ex.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Logger.getLogger(RegistrationPetOwner.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Send JSON response
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(jsonResponse);
        out.flush();
    }
}
