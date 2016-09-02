package dwd;

public class ReadWindData extends ReadData<WindDataPoint>
{

    @Override
    public WindDataPoint createDataPoint()
    {
        return new WindDataPoint();
    }
    
    @Override    
    public void addExtraData(String[] row, WindDataPoint dp)
    {
        dp.setWindSpeed(readDouble(row[4]));
        dp.setWindDirection(readDouble(row[5]));
    }

    @Override
    public int getRowSize()
    {
        return 7;
    }

    @Override
    public String getExpectedHeader()
    {
        return "STATIONS_ID; MESS_DATUM; QUALITAETS_NIVEAU; STRUKTUR_VERSION; WINDGESCHWINDIGKEIT;WINDRICHTUNG;eor";
    }

}
