package servlets;

import database.tables.EditPetKeepersTable;
import mainClasses.PetKeeper;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ShowPetKeepers extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String type = request.getParameter("type"); // Get the type parameter from the request
        if (type == null) {
            type = "all"; // Default to "all" if no parameter is provided
        }
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Pet Keepers</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Pet Keepers</h1>");

            try {
                ArrayList<PetKeeper> keepers = getKeepers(type); // Fetch the list of pet keepers based on type
                for (PetKeeper keeper : keepers) {
                    if (keeper.getDogkeeper().equals("true")) {
                        out.println("<p>" + keeper.getUsername() + " - Dog Keeper</p>");
                    } else {
                        out.println("<p>" + keeper.getUsername() + " - Cat Keeper</p>");
                    }

                }
            } catch (Exception e) {
                out.println("<p>Error: " + e.getMessage() + "</p>");
            }

            out.println("</body>");
            out.println("</html>");
        }
    }

    private ArrayList<PetKeeper> getKeepers(String type) throws ClassNotFoundException, SQLException {
        EditPetKeepersTable table = new EditPetKeepersTable();
        return table.getKeepers(type);
    }
}
