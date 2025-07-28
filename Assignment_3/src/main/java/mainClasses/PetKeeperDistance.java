package mainClasses;

public class PetKeeperDistance {

    private PetKeeper petKeeper;
    private double distance;

    public PetKeeperDistance(PetKeeper petKeeper, double distance) {
        this.petKeeper = petKeeper;
        this.distance = distance;
    }

    public PetKeeper getPetKeeper() {
        return petKeeper;
    }

    public double getDistance() {
        return distance;
    }
}
