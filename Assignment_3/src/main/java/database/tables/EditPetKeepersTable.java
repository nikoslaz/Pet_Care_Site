/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.tables;

import com.google.gson.Gson;
import mainClasses.PetKeeper;
import database.DB_Connection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import mainClasses.Stats;
/**
 *
 * @author Mike
 */
public class EditPetKeepersTable {

 
    public void addPetKeeperFromJSON(String json) throws ClassNotFoundException{
         PetKeeper user=jsonToPetKeeper(json);
         addNewPetKeeper(user);
    }
    
     public PetKeeper jsonToPetKeeper(String json){
         Gson gson = new Gson();

        PetKeeper user = gson.fromJson(json, PetKeeper.class);
        return user;
    }
    
    public String petKeeperToJSON(PetKeeper user){
         Gson gson = new Gson();

        String json = gson.toJson(user, PetKeeper.class);
        return json;
    }
    
   
    
    public void updatePetKeeper(String username,String personalpage) throws SQLException, ClassNotFoundException{
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String update="UPDATE petkeepers SET personalpage='"+personalpage+"' WHERE username = '"+username+"'";
        stmt.executeUpdate(update);
    }
    
    public void printPetKeeperDetails(String username, String password) throws SQLException, ClassNotFoundException{
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM petkeepers WHERE username = '" + username + "' AND password='"+password+"'");
            while (rs.next()) {
                System.out.println("===Result===");
                DB_Connection.printResults(rs);
            }

        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }
    
    public PetKeeper databaseToPetKeepers(String username, String password) throws SQLException, ClassNotFoundException{
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM petkeepers WHERE username = '" + username + "' AND password='"+password+"'");
            rs.next();
            String json=DB_Connection.getResultsToJSON(rs);
            Gson gson = new Gson();
            PetKeeper user = gson.fromJson(json, PetKeeper.class);
            return user;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public String databaseToPetKeepers2(String username, String password) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT keeper_id FROM petkeepers WHERE username = '" + username + "' AND password='" + password + "'");
            if (rs.next()) {
                return rs.getString("keeper_id");
            }
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return null;
    }


    
     public ArrayList<PetKeeper> getAvailableKeepers(String type) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ArrayList<PetKeeper> keepers = new ArrayList<PetKeeper>();
        ResultSet rs = null;
         try {
            if("all".equals(type))     
            rs = stmt.executeQuery("SELECT * FROM `petKeepers` WHERE  `petKeepers`.`keeper_id` not in (select keeper_id "
                 + "from `bookings` where `status`='requested' or  `status`='accepted')\n" +"");
            else if ("catKeepers".equals(type))
                 rs = stmt.executeQuery("SELECT * FROM `petKeepers` WHERE `petKeepers`.`catkeeper`='true' AND `petKeepers`.`keeper_id` not in (select keeper_id "
                 + "from `bookings` where `status`='requested' or  `status`='accepted')");         
             else if ("dogKeepers".equals(type))
                 rs = stmt.executeQuery("SELECT * FROM `petKeepers` WHERE `petKeepers`.`dogkeeper`='true' AND `petKeepers`.`keeper_id` not in (select keeper_id "
                 + "from `bookings` where `status`='requested' or  `status`='accepted')");
        
           
            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                PetKeeper keeper = gson.fromJson(json, PetKeeper.class);
                keepers.add(keeper);
            }
            return keepers;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }
    
    
    public ArrayList<PetKeeper> getKeepers(String type) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ArrayList<PetKeeper> keepers = new ArrayList<PetKeeper>();
        ResultSet rs = null;
        try {
            // Check if the type is "all" and build the query accordingly
            String query;
            if ("catkeeper".equals(type)) {
                query = "SELECT * FROM petkeepers WHERE catkeeper= '" + "true" + "'";
            } else if ("dogkeeper".equals(type)) {
                query = "SELECT * FROM petkeepers WHERE dogkeeper= '" + "true" + "'";
            } else if ("all".equals(type)) {
                query = "SELECT * FROM petkeepers";
            } else {
                // Handle invalid type here (e.g., return an empty list or throw an exception)
                System.err.println("Invalid type: " + type);
                return keepers; // Returning an empty list in this example
            }

            rs = stmt.executeQuery(query);

            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                PetKeeper keeper = gson.fromJson(json, PetKeeper.class);
                keepers.add(keeper);
            }
            return keepers;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        } finally {
            // Close the ResultSet, Statement, and Connection
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return null;
    }

    
    
    public String databasePetKeeperToJSON(String username, String password) throws SQLException, ClassNotFoundException{
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM petkeepers WHERE username = '" + username + "' AND password='"+password+"'");
            rs.next();
            String json=DB_Connection.getResultsToJSON(rs);
            return json;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }


     public void createPetKeepersTable() throws SQLException, ClassNotFoundException {

        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        String query = "CREATE TABLE petkeepers "
                + "(keeper_id INTEGER not NULL AUTO_INCREMENT, "
                + "    username VARCHAR(30) not null unique,"
                + "    email VARCHAR(50) not null unique,	"
                + "    password VARCHAR(32) not null,"
                + "    firstname VARCHAR(30) not null,"
                + "    lastname VARCHAR(30) not null,"
                + "    birthdate DATE not null,"
                + "    gender  VARCHAR (7) not null,"
                + "    country VARCHAR(30) not null,"
                + "    city VARCHAR(50) not null,"
                + "    address VARCHAR(50) not null,"
                + "    personalpage VARCHAR(200) not null,"
                + "    job VARCHAR(200) not null,"
                + "    telephone VARCHAR(14),"
                + "    lat DOUBLE,"
                + "    lon DOUBLE,"
                + "    property VARCHAR(10) not null,"
                + "    propertydescription VARCHAR(200),"
                + "    catkeeper VARCHAR(10) not null,"
                + "    dogkeeper VARCHAR(10) not null,"
                + "    catprice INTEGER,"
                + "    dogprice INTEGER,"
                + " PRIMARY KEY (keeper_id))";
        stmt.execute(query);
        stmt.close();
    }
    
    
    /**
     * Establish a database connection and add in the database.
     *
     * @throws ClassNotFoundException
     */
    public void addNewPetKeeper(PetKeeper user) throws ClassNotFoundException {
        try {
            Connection con = DB_Connection.getConnection();

            Statement stmt = con.createStatement();

            String insertQuery = "INSERT INTO "
                    + " petkeepers (username,email,password,firstname,lastname,birthdate,gender,country,city,address,personalpage,"
                    + "job,telephone,lat,lon,property,propertydescription,catkeeper,dogkeeper,catprice,dogprice)"
                    + " VALUES ("
                    + "'" + user.getUsername() + "',"
                    + "'" + user.getEmail() + "',"
                    + "'" + user.getPassword() + "',"
                    + "'" + user.getFirstname() + "',"
                    + "'" + user.getLastname() + "',"
                    + "'" + user.getBirthdate() + "',"
                    + "'" + user.getGender() + "',"
                    + "'" + user.getCountry() + "',"
                    + "'" + user.getCity() + "',"
                    + "'" + user.getAddress() + "',"
                    + "'" + user.getPersonalpage() + "',"
                     + "'" + user.getJob() + "',"
                    + "'" + user.getTelephone() + "',"
                    + "'" + user.getLat() + "',"
                    + "'" + user.getLon() + "',"
                    + "'" + user.getProperty() + "',"
                    + "'" + user.getPropertydescription()+ "',"
                    + "'" + user.getCatkeeper() + "',"
                    + "'" + user.getDogkeeper() + "',"
                    + "'" + user.getCatprice() + "',"
                    + "'" + user.getDogprice() + "'"
                    + ")";
            //stmt.execute(table);
            System.out.println(insertQuery);
            stmt.executeUpdate(insertQuery);
            System.out.println("# The pet owner was successfully added in the database.");

            /* Get the member id from the database and set it to the member */
            stmt.close();

        } catch (SQLException ex) {
            Logger.getLogger(EditPetKeepersTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public PetKeeper getPetKeeperByUsername(String username) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM petkeepers WHERE username = '" + username + "'");
        if (rs.next()) {
            String json = DB_Connection.getResultsToJSON(rs);
            Gson gson = new Gson();
            return gson.fromJson(json, PetKeeper.class);
        }
        return null;
    }

    public void updatePetKeeper2(PetKeeper user) throws ClassNotFoundException, SQLException {
        Connection con = null;
        Statement stmt = null;
        System.out.println(user.getUsername());
        try {
            con = DB_Connection.getConnection();
            stmt = con.createStatement();

            StringBuilder sql = new StringBuilder("UPDATE petkeepers SET ");

            // Append values to SQL query
            if (!user.getFirstname().isEmpty()) {
                System.out.println("firstname");
                System.out.println(sql);
                sql.append("firstname = '").append(user.getFirstname()).append("',");
            }
            if (user.getLastname() != null && !user.getLastname().isEmpty()) {
                System.out.println("lastname");
                sql.append("lastname = '").append(user.getLastname()).append("', ");
            }
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                sql.append("password = '").append(user.getPassword()).append("', ");
            }
            if (user.getAddress() != null && !user.getAddress().isEmpty()) {
                sql.append("address = '").append(user.getAddress()).append("', ");
            }
            if (user.getCity() != null && !user.getCity().isEmpty()) {
                sql.append("city = '").append(user.getCity()).append("', ");
            }
            if (user.getJob() != null && !user.getJob().isEmpty()) {
                sql.append("job = '").append(user.getJob()).append("', ");
            }
            if (user.getBirthdate() != null && !user.getBirthdate().isEmpty()) {
                sql.append("birthdate = '").append(user.getBirthdate()).append("', ");
            }
            if (user.getGender() != null && !user.getGender().isEmpty()) {
                sql.append("gender = '").append(user.getGender()).append("', ");
            }
            if (user.getTelephone() != null && !user.getTelephone().isEmpty()) {
                sql.append("telephone = '").append(user.getTelephone()).append("', ");
            }
            if (user.getCountry() != null && !user.getCountry().isEmpty()) {
                sql.append("country = '").append(user.getCountry()).append("', ");
            }
            if (user.getPersonalpage() != null && !user.getPersonalpage().isEmpty()) {
                sql.append("personalpage").append(user.getPersonalpage()).append("', ");
            }
            if (user.getProperty() != null && !user.getProperty().isEmpty()) {
                sql.append("propertydescription = '").append(user.getProperty()).append("', ");
            }
            if (user.getDogkeeper() != null && !user.getDogkeeper().isEmpty()) {
                sql.append("dogkeeper = '").append(user.getDogkeeper()).append("', ");
            }
            if (user.getCatkeeper() != null && !user.getCatkeeper().isEmpty()) {
                sql.append("catkeeper = '").append(user.getCatkeeper()).append("', ");
            }
            if (user.getDogprice() != 0) {
                sql.append("dogprice = ").append(user.getDogprice()).append(", ");
            }
            if (user.getCatprice() != 0) {
                sql.append("catprice = ").append(user.getCatprice()).append(", ");
            }

            // Remove the last comma and space
            int lastCommaIndex = sql.lastIndexOf(",");
            if (lastCommaIndex != -1) {
                sql = new StringBuilder(sql.substring(0, lastCommaIndex));
            }

            // Complete the SQL query with the WHERE clause
            sql.append(" WHERE username = '").append(user.getUsername()).append("'");
            // Execute the update
            stmt.executeUpdate(sql.toString());

        } catch (SQLException e) {
            // Handle exceptions
            throw e;
        } finally {
            // Close resources
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }

    public Stats getPetKeeperStatistics(int keeperId) {
        Stats stats = new Stats();
        try (Connection con = DB_Connection.getConnection(); Statement stmt = con.createStatement()) {

            String query = "SELECT fromdate, todate FROM bookings WHERE keeper_id = " + keeperId + " AND status = 'finished'";
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                LocalDate fromDate = rs.getDate("fromdate").toLocalDate();
                LocalDate toDate = rs.getDate("todate").toLocalDate();
                long hostingDays = ChronoUnit.DAYS.between(fromDate, toDate);
                stats.incrementTotalBookings();
                stats.addHostingDays((int) hostingDays);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return stats;
    }

}
