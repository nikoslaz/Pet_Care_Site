package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import database.tables.EditPetKeepersTable;
import database.tables.EditPetOwnersTable;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

public class Login extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        HttpSession session = request.getSession();
        EditPetKeepersTable petKeepersTable = new EditPetKeepersTable();
        EditPetOwnersTable petOwnersTable = new EditPetOwnersTable();

        try {
            JSONObject jsonResponse = new JSONObject();
            String userType = null;
            String userID = null;

            userID = petKeepersTable.databaseToPetKeepers2(username, password);
            if (userID != null) {
                userType = "petkeeper";
            } else {
                userID = petOwnersTable.databaseToPetOwners2(username, password);
                if (userID != null) {
                    userType = "petowner";
                }
            }

            if (userType != null) {
                session.setAttribute("username", username);
                session.setAttribute("userId", userID);
                session.setMaxInactiveInterval(120);

                jsonResponse.put("success", true);
                jsonResponse.put("userType", userType);
                jsonResponse.put("userId", userID); // Send userID to the client

                response.setContentType("application/json");
                response.getWriter().write(jsonResponse.toString());
            } else {
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Invalid credentials");

                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write(jsonResponse.toString());
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public String getServletInfo() {
        return "Login servlet for handling user login requests.";
    }
}
