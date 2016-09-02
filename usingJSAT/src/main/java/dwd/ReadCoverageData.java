package dwd;

public class ReadCoverageData extends ReadData<CoverageDataPoint>
{

    @Override
    public CoverageDataPoint createDataPoint()
    {
        return new CoverageDataPoint();
    }

    @Override
    public int getRowSize()
    {
        return 5;
    }

    @Override
    public void addExtraData(String[] row, CoverageDataPoint dp)
    {
        dp.setCoverageDegree(this.readInt(row[3]));       
    }

    @Override
    public String getExpectedHeader()
    {
        return "STATIONS_ID; MESS_DATUM; QUALITAETS_NIVEAU; GESAMT_BEDECKUNGSGRAD;eor";
    }



}
