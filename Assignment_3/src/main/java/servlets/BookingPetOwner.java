package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import database.tables.EditPetKeepersTable;
import database.tables.EditBookingsTable;
import static java.lang.System.out;
import mainClasses.PetKeeper;
import java.util.ArrayList;

public class BookingPetOwner extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            EditPetKeepersTable petKeepersTable = new EditPetKeepersTable();
            ArrayList<PetKeeper> availableKeepers = petKeepersTable.getAvailableKeepers("all");

            Gson gson = new Gson();
            String keepersJson = gson.toJson(availableKeepers);

            out.print(keepersJson);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            String action = request.getParameter("action");

            EditBookingsTable bookingsTable = new EditBookingsTable();

            if ("create".equals(action)) {
                // Handle creation of a new booking
                String bookingJson = request.getParameter("booking");
                bookingsTable.addBookingFromJSON(bookingJson);
                out.print("{\"message\":\"Booking successfully created.\"}");
            } else if ("update".equals(action)) {
                // Handle updating booking status
                int bookingID = Integer.parseInt(request.getParameter("bookingID"));
                String status = request.getParameter("status");
                bookingsTable.updateBooking(bookingID, status);
                out.print("{\"message\":\"Booking status updated to " + status + ".\"}");
            } else {
                out.print("{\"error\":\"Invalid action.\"}");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet for managing pet keeper bookings";
    }
}
