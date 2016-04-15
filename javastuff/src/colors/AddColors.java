package colors;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Carlos Freund on 11.11.2015.
 */
public class AddColors {

     public static  BufferedImage addImages(BufferedImage... images){

         int width = 0;
         int height = 0;
         for (BufferedImage image: images){
             width = Math.max(width, image.getWidth());
             height = Math.max(height, image.getHeight());
         }

         BufferedImage newImage = new BufferedImage(width, height,images[0].getType());

         for (BufferedImage image: images){
             for (int x = 0; x<image.getWidth();x++){
                 for(int y = 0; y<image.getHeight();y++){
                     int oldColor = newImage.getRGB(x,y);
                     int newColor = oldColor + image.getRGB(x,y);
                     newImage.setRGB(x,y, newColor);
                 }
             }
         }

        return newImage;
     }

    public static BufferedImage illuminate(BufferedImage image, float redP, float greenP, float blueP){

        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(),image.getType());

        for (int x = 0; x<image.getWidth();x++){
            for(int y = 0; y<image.getHeight();y++){
                int oldColor = image.getRGB(x,y);
                int alphaPart = oldColor & 0xFF000000;
                int redPart =     (int)((oldColor & 0xFF0000) * redP) & 0xFF0000;
                int greenPart =   (int)((oldColor & 0x00FF00) * greenP) & 0xFF00;
                int bluePart =    (int)((oldColor & 0x0000FF) * blueP) & 0xFF;
                int newColor = alphaPart | redPart | greenPart | bluePart;
                newImage.setRGB(x,y, newColor);
            }
        }
        return newImage;
    }



    public static  void main(String[] args) throws IOException{

        BufferedImage imgColor = ImageIO.read(new File("images/color.bmp"));
        BufferedImage imgAnd = ImageIO.read(new File("images/and.bmp"));
        BufferedImage imgLight = ImageIO.read(new File("images/light.bmp"));
        BufferedImage imgSmart = ImageIO.read(new File("images/smart.png"));

        BufferedImage mixed = addImages(imgColor, imgAnd, imgLight);
        boolean worked = ImageIO.write(mixed, "BMP", new File("output/colorAndLight.bmp"));
        if (!worked){
            throw new RuntimeException("Could not write file");
        }

        BufferedImage inRed = illuminate(mixed, 0.9F,0.1F,1.0F);
        worked =  ImageIO.write(inRed, "BMP", new File("output/inRed.bmp"));
        if (!worked){
            throw new RuntimeException("Could not write file");
        }

        BufferedImage greenSmart = illuminate(imgSmart, 0.0F,1.0F,0.0F);
        worked =  ImageIO.write(greenSmart, "BMP", new File("output/redSmart.bmp"));
        if (!worked){
            throw new RuntimeException("Could not write file");
        }



    }
}
