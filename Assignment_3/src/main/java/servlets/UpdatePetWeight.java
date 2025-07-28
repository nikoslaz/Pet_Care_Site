package servlets;

import java.io.IOException;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import database.tables.EditPetsTable;

public class UpdatePetWeight extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String petId = request.getParameter("pet_id");
        String weight = request.getParameter("weight");
        EditPetsTable edit = new EditPetsTable();

        try {
            edit.updatePetWeight(petId, weight);
            response.getWriter().println("Weight of pet with ID " + petId + " was successfully updated.");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            response.getWriter().println("Failed to update pet weight.");
        }
    }

}
