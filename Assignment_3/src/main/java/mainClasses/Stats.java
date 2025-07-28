/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mainClasses;

public class Stats {

    private int totalBookings;
    private int totalHostingDays;

    public Stats() {
        this.totalBookings = 0;
        this.totalHostingDays = 0;
    }

    public void incrementTotalBookings() {
        this.totalBookings++;
    }

    public void addHostingDays(int days) {
        this.totalHostingDays += days;
    }

    // Getters
    public int getTotalBookings() {
        return totalBookings;
    }

    public int getTotalHostingDays() {
        return totalHostingDays;
    }

    // Setters, if necessary
}
