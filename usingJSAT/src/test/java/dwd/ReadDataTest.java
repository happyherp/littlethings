package dwd;

import java.util.Calendar;
import java.util.List;

import org.junit.Test;

import org.junit.Assert;

public class ReadDataTest
{
    
    @Test
    public void testWind(){
        
        List<WindDataPoint> data = new ReadWindData().readStream(
                ReadDataTest.class.getResourceAsStream("/produkt_wind_Terminwerte_19370101_20110331_00003.txt"));
        
        Assert.assertEquals(555976, data.size());
        WindDataPoint first = data.get(0);        
        Assert.assertEquals("3", first.getStationId());
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(first.getMeasurementDate());
        Assert.assertEquals(1937, cal.get(Calendar.YEAR));
        Assert.assertEquals(0, cal.get(Calendar.MONTH));
        Assert.assertEquals(1, cal.get(Calendar.DAY_OF_MONTH));
        Assert.assertEquals(0, cal.get(Calendar.HOUR_OF_DAY));
        Assert.assertEquals(0, cal.get(Calendar.MINUTE));
        
        Assert.assertEquals(5, first.getQuality());
        Assert.assertEquals(6.2f, first.getWindSpeed(), 0.001);
        Assert.assertNull(first.getWindDirection());
 
    }
    
    
    @Test
    public void testTemp(){
        
        List<TempDataPoint> data = new ReadTempData().readStream(
                ReadDataTest.class.getResourceAsStream("/produkt_temp_Terminwerte_19500401_20110331_00003.txt"));
        
        Assert.assertEquals(534716, data.size());
        TempDataPoint first = data.get(0);        
        Assert.assertEquals("3", first.getStationId());
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(first.getMeasurementDate());
        Assert.assertEquals(1950, cal.get(Calendar.YEAR));
        Assert.assertEquals(3, cal.get(Calendar.MONTH));
        Assert.assertEquals(1, cal.get(Calendar.DAY_OF_MONTH));
        Assert.assertEquals(1, cal.get(Calendar.HOUR_OF_DAY));
        Assert.assertEquals(0, cal.get(Calendar.MINUTE));
        
        Assert.assertEquals(5, first.getQuality());
        Assert.assertEquals(5.7f, first.getAirTemp(), 0.001);
        Assert.assertEquals(83.0f, first.getRelHumidity(), 0.001);
    }
    
    @Test
    public void testConverage(){
        
        List<CoverageDataPoint> data = new ReadCoverageData().readStream(
                ReadDataTest.class.getResourceAsStream("/produkt_synop_Terminwerte_19500401_20110401_00003.txt"));
        
        Assert.assertEquals(347555, data.size());
        CoverageDataPoint first = data.get(0);        
        Assert.assertEquals("3", first.getStationId());
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(first.getMeasurementDate());
        Assert.assertEquals(1950, cal.get(Calendar.YEAR));
        Assert.assertEquals(3, cal.get(Calendar.MONTH));
        Assert.assertEquals(1, cal.get(Calendar.DAY_OF_MONTH));
        Assert.assertEquals(0, cal.get(Calendar.HOUR_OF_DAY));
        Assert.assertEquals(0, cal.get(Calendar.MINUTE));
        
        Assert.assertEquals(1, first.getQuality());
        Assert.assertEquals(8, (int)first.getCoverageDegree());
    }
    
    @Test
    public void testSunshine(){
        
        List<SunshineDataPoint> data = new ReadSunshineData().readStream(
                ReadDataTest.class.getResourceAsStream("/produkt_sonne_Terminwerte_20141204_20160605_00044.txt"));
        
        Assert.assertEquals(10200, data.size());
        SunshineDataPoint first = data.get(0);        
        Assert.assertEquals("44", first.getStationId());
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(first.getMeasurementDate());
        Assert.assertEquals(2014, cal.get(Calendar.YEAR));
        Assert.assertEquals(11, cal.get(Calendar.MONTH));
        Assert.assertEquals(4, cal.get(Calendar.DAY_OF_MONTH));
        Assert.assertEquals(3, cal.get(Calendar.HOUR_OF_DAY));
        Assert.assertEquals(0, cal.get(Calendar.MINUTE));
        
        Assert.assertEquals(10, first.getQuality());
        Assert.assertEquals(0.0d, (double)first.getSunshineMinutes(), 0.001);
    }
    
    @Test
    public void testRain(){
        
        List<RainDataPoint> data = new ReadRainData().readStream(
                ReadDataTest.class.getResourceAsStream("/produkt_synop_Terminwerte_19950901_20120403_00003.txt"));
        
        Assert.assertEquals(135476, data.size());
        RainDataPoint first = data.get(0);        
        Assert.assertEquals("3", first.getStationId());
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(first.getMeasurementDate());
        Assert.assertEquals(1995, cal.get(Calendar.YEAR));
        Assert.assertEquals(8, cal.get(Calendar.MONTH));
        Assert.assertEquals(1, cal.get(Calendar.DAY_OF_MONTH));
        Assert.assertEquals(0, cal.get(Calendar.HOUR_OF_DAY));
        Assert.assertEquals(0, cal.get(Calendar.MINUTE));
        
        Assert.assertEquals(1, first.getQuality());
        Assert.assertEquals(0.0d, (double)first.getAmountMM(), 0.001d);
        Assert.assertNull(first.getType());
    }    
}
