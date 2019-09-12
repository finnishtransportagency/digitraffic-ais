package fi.livi.digitraffic.meri.domain;

public class Point implements Geometry {

    public final double longitude;
    public final double latitude;

    public Point(final double longitude, final double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
