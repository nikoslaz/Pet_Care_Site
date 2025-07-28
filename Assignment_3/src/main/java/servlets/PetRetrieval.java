package servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import mainClasses.Pet;
import database.tables.EditPetsTable;

public class PetRetrieval extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String type = request.getParameter("type");
        String breed = request.getParameter("breed");
        EditPetsTable edit = new EditPetsTable();

        try {
            ArrayList<Pet> pets = edit.databaseToPets2(type, breed);
            Gson gson = new Gson();
            String petsJson = gson.toJson(pets);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(petsJson);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            response.getWriter().println("Failed to retrieve pets.");
        }
    }

}
