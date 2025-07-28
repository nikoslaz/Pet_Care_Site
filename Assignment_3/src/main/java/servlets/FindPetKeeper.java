package servlets;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import database.tables.EditPetKeepersTable;
import java.sql.SQLException;
import mainClasses.PetKeeper;
import mainClasses.PetKeeperDistance;

public class FindPetKeeper extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String userLocation = request.getParameter("userLocation");

        try {
            EditPetKeepersTable editTable = new EditPetKeepersTable();
            ArrayList<PetKeeper> petKeeperList = editTable.getAvailableKeepers("all"); // Fetch all Pet Keepers

            petKeeperList = combineAndSortPetKeepers(userLocation, petKeeperList);

            String sortedPetKeepersJson = convertListToJson(petKeeperList);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(sortedPetKeepersJson);
        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            response.getWriter().println("Failed to retrieve Pet Keepers.");
        }
    }

    private ArrayList<PetKeeper> combineAndSortPetKeepers(String userLocation, ArrayList<PetKeeper> petKeepers) {
        String[] userCoords = userLocation.split(",");
        double userLat = Double.parseDouble(userCoords[0]);
        double userLon = Double.parseDouble(userCoords[1]);

        // Creating a list of PetKeeperDistance objects
        ArrayList<PetKeeperDistance> petKeeperDistances = new ArrayList<>();
        for (PetKeeper keeper : petKeepers) {
            double distance = calculateDistance(userLat, userLon, keeper.getLat(), keeper.getLon());
            petKeeperDistances.add(new PetKeeperDistance(keeper, distance));
        }

        // Sorting the PetKeeperDistance list
        Collections.sort(petKeeperDistances, (pkd1, pkd2) -> Double.compare(pkd1.getDistance(), pkd2.getDistance()));

        // Extracting the sorted list of PetKeepers
        ArrayList<PetKeeper> sortedPetKeepers = new ArrayList<>();
        for (PetKeeperDistance pkd : petKeeperDistances) {
            sortedPetKeepers.add(pkd.getPetKeeper());
        }

        return sortedPetKeepers;
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the earth in kilometers
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    private String convertListToJson(ArrayList<PetKeeper> petKeepers) {
        return new Gson().toJson(petKeepers);
    }
}
