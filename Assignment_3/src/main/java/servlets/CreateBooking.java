package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import database.tables.EditBookingsTable;
import static java.lang.System.out;
import mainClasses.Booking;

public class CreateBooking extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            String bookingJson = request.getParameter("booking");
            Gson gson = new Gson();
            Booking newBooking = gson.fromJson(bookingJson, Booking.class);

            EditBookingsTable bookingsTable = new EditBookingsTable();
            bookingsTable.createNewBooking(newBooking);

            out.print("{\"message\":\"Booking successfully created.\"}");
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}
