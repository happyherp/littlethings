package dwd;

public class TempDataPoint extends BaseDataPoint
{
    
    Double airTemp;
    Double relHumidity;
    
    public Double getAirTemp()
    {
        return airTemp;
    }
    public void setAirTemp(Double airTemp)
    {
        this.airTemp = airTemp;
    }
    public Double getRelHumidity()
    {
        return relHumidity;
    }
    public void setRelHumidity(Double relHumidity)
    {
        this.relHumidity = relHumidity;
    }
    
    

}
