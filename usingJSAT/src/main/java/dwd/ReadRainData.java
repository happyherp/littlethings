package dwd;

public class ReadRainData extends ReadData<RainDataPoint>
{

    @Override
    public RainDataPoint createDataPoint()
    {
        return new RainDataPoint();
    }

    @Override
    public int getRowSize()
    {
        return 7;
    }

    @Override
    public void addExtraData(String[] row, RainDataPoint dp)
    {
        dp.setRain(this.readBool(row[3]));
        dp.setAmountMM(this.readDouble(row[4]));
        Integer type = this.readInt(row[5]);
        if (type == null)
        {
            dp.setType(null);
        } else
        {
            switch (type)
            {
            case 0:
                dp.setType(RainType.NONE);
                break;
            case 6:
                dp.setType(RainType.RAIN);
                break;
            case 7:
                dp.setType(RainType.SNOW);
                break;
            case 8:
                dp.setType(RainType.RAIN_AND_SNOW);
                break;
            default:
                dp.setType(RainType.OTHER);
            }
        }

    }

    @Override
    public String getExpectedHeader()
    {
        return "STATIONS_ID; MESS_DATUM; QUALITAETS_NIVEAU; NIEDERSCHLAG_GEFALLEN_IND;NIEDERSCHLAGSHOEHE;NIEDERSCHLAGSFORM;eor";
    }

}
