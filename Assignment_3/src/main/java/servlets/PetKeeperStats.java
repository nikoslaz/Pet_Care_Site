package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import com.google.gson.Gson;
import database.tables.EditPetKeepersTable;
import mainClasses.Stats;

public class PetKeeperStats extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int keeperId = Integer.parseInt(request.getParameter("keeperId"));
            EditPetKeepersTable editTable = new EditPetKeepersTable();
            Stats stats = editTable.getPetKeeperStatistics(keeperId);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            new Gson().toJson(stats, response.getWriter());
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Keeper ID");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred");
        }
    }
}
