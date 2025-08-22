package AdomLogisticsConsoleApp.ObjectClass;

public class Driver {
    public String driverID;
    public String name;
    private String licenseNumber;
    private String phoneNumber;
    private String emailAddress;
    private String address;
    public double proximity;
    public boolean assigned;
    private String assignedRoutes;
    private int delayCount;
    public String infractions;
    public String status;
    private int experienceScore;
    public int lineIdx;
    public int delays;
    public int routes;
    public int numInfractions;

    /**
     * Full constructor for Driver.
     */
    public Driver(String driverID,
            String name,
            String licenseNumber,
            String phoneNumber,
            String emailAddress,
            String address,
            double proximity,
            boolean assigned,
            String assignedRoutes,
            int delayCount,
            String infractions,
            String status) {
        this.driverID = driverID;
        this.name = name;
        this.licenseNumber = licenseNumber;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.address = address;
        this.proximity = proximity;
        this.assigned = assigned;
        this.assignedRoutes = assignedRoutes;
        this.delayCount = delayCount;
        this.infractions = infractions;
        this.status = status;
    }

    public Driver(String id, double proximity, int experienceScore, int lineIdx, String name, int delays,
            int numInfractions, int routes) {
        this.driverID = id;
        this.proximity = proximity;
        this.experienceScore = experienceScore;
        this.lineIdx = lineIdx;
        this.name = name;
        this.delays = delays;
        this.numInfractions = numInfractions;
        this.routes = routes;
    }
    // Getters and setters for every field

    public Driver() {
    }

    public Driver(String driverID2, double proximity2, int i, int entryStart, String string, int delays2,
            String infractions2, int routes2) {
        //TODO Auto-generated constructor stub
    }

    public String getDriverID() {
        return driverID;
    }

    public void setDriverID(String driverID) {
        this.driverID = driverID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getProximity() {
        return proximity;
    }

    public void setProximity(double proximity) {
        this.proximity = proximity;
    }

    public boolean isAssigned() {
        return assigned;
    }

    public void setAssigned(boolean assigned) {
        this.assigned = assigned;
    }

    public String getAssignedRoutes() {
        return assignedRoutes;
    }

    public void setAssignedRoutes(String assignedRoutes) {
        this.assignedRoutes = assignedRoutes;
    }

    public int getDelayCount() {
        return delayCount;
    }

    public void setDelayCount(int delayCount) {
        this.delayCount = delayCount;
    }

    public String getInfractions() {
        return infractions;
    }

    public void setInfractions(String infractions) {
        this.infractions = infractions;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}