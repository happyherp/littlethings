package dwd;

import java.util.Date;

public class BaseDataPoint
{

    private String stationId;
    private Date measurementDate;
    private int quality;

    public BaseDataPoint()
    {
        super();
    }

    public String getStationId()
    {
        return stationId;
    }

    public void setStationId(String stationId)
    {
        this.stationId = stationId;
    }

    public Date getMeasurementDate()
    {
        return measurementDate;
    }

    public void setMeasurementDate(Date measurementDate)
    {
        this.measurementDate = measurementDate;
    }

    public int getQuality()
    {
        return quality;
    }

    public void setQuality(int quality)
    {
        this.quality = quality;
    }


}