package AdomLogisticsConsoleApp.ObjectClass;

public class Vehicle {
    private String entryId;
    private String registrationNumber;
    private String vehicleType;
    private double mileage;
    private double fuelUsage;
    private String driverID;
    private String maintenanceHistory;
    private String maintenanceSchedule;
    private String status;

    /**
     * Full constructor for Vehicle.
     */
    public Vehicle(String entryId,
            String registrationNumber,
            String vehicleType,
            double mileage,
            double fuelUsage,
            String driverID,
            String maintenanceHistory,
            String maintenanceSchedule,
            String status) {
        this.entryId = entryId;
        this.registrationNumber = registrationNumber;
        this.vehicleType = vehicleType;
        this.mileage = mileage;
        this.fuelUsage = fuelUsage;
        this.driverID = driverID;
        this.maintenanceHistory = maintenanceHistory;
        this.maintenanceSchedule = maintenanceSchedule;
        this.status = status;
    }

    // Getters and setters for every field

    public String getEntryId() {
        return entryId;
    }

    public void setEntryId(String entryId) {
        this.entryId = entryId;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public double getMileage() {
        return mileage;
    }

    public void setMileage(double mileage) {
        this.mileage = mileage;
    }

    public double getFuelUsage() {
        return fuelUsage;
    }

    public void setFuelUsage(double fuelUsage) {
        this.fuelUsage = fuelUsage;
    }

    public String getDriverID() {
        return driverID;
    }

    public void setDriverID(String driverID) {
        this.driverID = driverID;
    }

    public String getMaintenanceHistory() {
        return maintenanceHistory;
    }

    public void setMaintenanceHistory(String maintenanceHistory) {
        this.maintenanceHistory = maintenanceHistory;
    }

    public String getMaintenanceSchedule() {
        return maintenanceSchedule;
    }

    public void setMaintenanceSchedule(String maintenanceSchedule) {
        this.maintenanceSchedule = maintenanceSchedule;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
