package servlets;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import com.google.gson.Gson;
import database.tables.EditPetOwnersTable;
import mainClasses.PetOwner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminOwners extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            // Create an instance of EditPetOwnersTable to fetch pet owners
            EditPetOwnersTable editPetOwners = new EditPetOwnersTable();
            ArrayList<PetOwner> owners = editPetOwners.getOwners();

            // Check if the owners list is empty
            if (owners.isEmpty()) {
                // Respond with 404 Not Found if no owners are found
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            // Convert the list of owners to JSON
            Gson gson = new Gson();
            String json = gson.toJson(owners);

            // Send the JSON response back to the client
            try (PrintWriter out = response.getWriter()) {
                out.print(json);
            }

            // Set the response status to OK
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (ClassNotFoundException | SQLException e) {
            // Log the exception and send a 500 Internal Server Error response
            Logger.getLogger(AdminOwners.class.getName()).log(Level.SEVERE, null, e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error fetching data");
        }
    }
}
