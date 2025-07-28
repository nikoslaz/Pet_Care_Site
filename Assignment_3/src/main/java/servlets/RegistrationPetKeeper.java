package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import org.json.JSONObject;
import database.tables.EditPetKeepersTable;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import mainClasses.PetKeeper;

public class RegistrationPetKeeper extends HttpServlet {

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

        PetKeeper PetKeeper = new PetKeeper();
        PetKeeper.setFirstname(jsonObject.getString("first_name"));
        PetKeeper.setLastname(jsonObject.getString("last_name"));
        PetKeeper.setUsername(jsonObject.getString("username"));
        PetKeeper.setEmail(jsonObject.getString("email"));
        PetKeeper.setPassword(jsonObject.getString("password"));
        PetKeeper.setAddress(jsonObject.getString("address"));
        PetKeeper.setCity(jsonObject.getString("town"));
        PetKeeper.setJob(jsonObject.getString("job"));
        PetKeeper.setBirthdate(jsonObject.getString("birthday"));
        PetKeeper.setGender(jsonObject.getString("gender"));
        PetKeeper.setLat(jsonObject.getDouble("lat"));
        PetKeeper.setLon(jsonObject.getDouble("lon"));
        PetKeeper.setPersonalpage(jsonObject.getString("url"));
        PetKeeper.setTelephone(jsonObject.getString("phone"));
        PetKeeper.setCountry(jsonObject.getString("country"));
        PetKeeper.setProperty(jsonObject.getString("propertyDescription"));
        PetKeeper.setDogkeeper(jsonObject.getString("dog"));
        PetKeeper.setCatkeeper(jsonObject.getString("cat"));
        PetKeeper.setDogprice(jsonObject.getInt("dogprice"));
        PetKeeper.setCatprice(jsonObject.getInt("catprice"));

        EditPetKeepersTable editPetKeepersTable = new EditPetKeepersTable();
        JSONObject jsonResponse = new JSONObject();

        try {
            // Check if user already exists in the database
            if (editPetKeepersTable.databaseToPetKeepers(PetKeeper.getUsername(), PetKeeper.getPassword()) != null) {
                // User already exists, send error response
                jsonResponse.put("status", "error");
                jsonResponse.put("message", "User with provided username or password already exists.");
                response.setStatus(HttpServletResponse.SC_CONFLICT);
            } else {
                editPetKeepersTable.addNewPetKeeper(PetKeeper);
                response.setStatus(HttpServletResponse.SC_OK);
                jsonResponse.put("status", "success");
                jsonResponse.put("message", "Registration successful.");
                response.setStatus(HttpServletResponse.SC_OK);
            }
        } catch (SQLException ex) {
            jsonResponse.put("status", "error");
            jsonResponse.put("message", "Database error: " + ex.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Logger.getLogger(RegistrationPetKeeper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            jsonResponse.put("status", "error");
            jsonResponse.put("message", "Class not found error: " + ex.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Logger.getLogger(RegistrationPetKeeper.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Send JSON response
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(jsonResponse);
        out.flush();
    }
}
