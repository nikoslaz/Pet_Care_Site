package servlets;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import database.tables.EditPetsTable;

public class DeletePet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String petId = request.getParameter("pet_id");

        EditPetsTable edit = new EditPetsTable();
        try {
            edit.deletePet(petId);
            response.getWriter().println("Pet with ID " + petId + " was successfully deleted.");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            response.getWriter().println("Failed to delete pet.");
        }
    }

}
