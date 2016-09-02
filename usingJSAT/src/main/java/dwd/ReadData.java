package dwd;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public abstract class ReadData<T extends BaseDataPoint>
{
    
    public abstract T createDataPoint();
    
    
    public void readZip(File f){
        
       
        try
        {
            ZipFile zipFile= new ZipFile(f);
            for (ZipEntry entry: Collections.list( zipFile.entries())){
                if (entry.getName().startsWith("produkt")){
                    
                    
                     zipFile.getInputStream(entry);
                    
                    
                }
                
            }
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        
    }
    
    public List<T> readStream(InputStream is)
    {

        try
        {
            
            SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMddHH");
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String header = reader.readLine();
            if (!header.equals(this.getExpectedHeader())){
                throw new RuntimeException("Bad Header: "+header);
            }
            
            List<T> l = new ArrayList<>();
            
            String line = reader.readLine();
            while (line != null && line.endsWith("eor"))
            {
                
                String[] row = line.split(";");
                if (row.length != getRowSize()){
                    throw new RuntimeException("Bad input length: "+line);
                }
                for(int i = 0;i<row.length;i++){
                    row[i] = row[i].trim();
                }
                
                T dp = this.createDataPoint();
                dp.setStationId(row[0]);
                dp.setMeasurementDate(dateformat.parse(row[1]));
                dp.setQuality(Integer.parseInt(row[2]));
                
                addExtraData(row, dp);
                
                l.add(dp);

                line = reader.readLine();
            }
            return l;

        } catch (IOException | ParseException e)
        {
            throw new RuntimeException(e);

        }

    }


    public abstract  int getRowSize();
    
    public abstract String getExpectedHeader();


    public abstract void addExtraData(String[] row, T dp);
    
    public Double readDouble(String s){
        Double d = Double.parseDouble(s);
        if (d==-999.0d){
            return null;
        }
        return d;
    }
    
    public Integer readInt(String string)
    {
        Integer d = Integer.parseInt(string);
        if (d==-999){
            return null;
        }
        return d;
    }
    
    public Boolean readBool(String s)
    {
        Integer i = this.readInt(s);
        if (i==null){
            return null;
        }else if (i==1){
            return true;
        }else if(i==0){
            return false;
        }else{
            throw new RuntimeException("Invalid for a Bool: "+s);
        }
    }

}
