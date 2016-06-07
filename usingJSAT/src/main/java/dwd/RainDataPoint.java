package dwd;

public class RainDataPoint extends BaseDataPoint
{
    
    private Boolean rain;
    
    private Double amountMM;
    
    private RainType type;

    public Boolean getRain()
    {
        return rain;
    }

    public void setRain(Boolean rain)
    {
        this.rain = rain;
    }

    public Double getAmountMM()
    {
        return amountMM;
    }

    public void setAmountMM(Double amountMM)
    {
        this.amountMM = amountMM;
    }

    public RainType getType()
    {
        return type;
    }

    public void setType(RainType type)
    {
        this.type = type;
    }
    
    

}
