package dwd;

public class ReadSunshineData extends ReadData<SunshineDataPoint>
{

    @Override
    public SunshineDataPoint createDataPoint()
    {
        return new SunshineDataPoint();
    }

    @Override
    public int getRowSize()
    {
        return 6;
    }

    @Override
    public void addExtraData(String[] row, SunshineDataPoint dp)
    {
        dp.setSunshineMinutes(this.readDouble(row[4]));
    }

    @Override
    public String getExpectedHeader()
    {
        return "STATIONS_ID; MESS_DATUM; QUALITAETS_NIVEAU; STRUKTUR_VERSION; STUNDENSUMME_SONNENSCHEIN;eor";
    }

}
