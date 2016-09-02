package dwd;

public class ReadTempData extends ReadData<TempDataPoint>
{

    @Override
    public TempDataPoint createDataPoint()
    {
        return new TempDataPoint();
    }

    @Override
    public int getRowSize()
    {
        return 7;
    }

    @Override
    public void addExtraData(String[] row, TempDataPoint dp)
    {
        dp.setAirTemp(this.readDouble(row[4]));
        dp.setRelHumidity(this.readDouble(row[5]));
        
    }

    @Override
    public String getExpectedHeader()
    {
        return "STATIONS_ID; MESS_DATUM; QUALITAETS_NIVEAU; STRUKTUR_VERSION; LUFTTEMPERATUR;REL_FEUCHTE;eor";
    }

}
