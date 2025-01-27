package ZLBBS.Racing.dto;

public class RaceDTO {
    private Long id;
    private String driverName;
    private String carInfo;
    private String track;

    // Конструктор для инициализации объекта с параметрами
    public RaceDTO(Long id, String driverName, String carInfo, String track) {
        this.id = id;
        this.driverName = driverName;
        this.carInfo = carInfo;
        this.track = track;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getCarInfo() {
        return carInfo;
    }

    public void setCarInfo(String carInfo) {
        this.carInfo = carInfo;
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }
}
