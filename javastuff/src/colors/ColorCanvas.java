package colors;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created by Carlos Freund on 12.11.2015.
 */
public class ColorCanvas extends JFrame {

    JLabel label;

    public ColorCanvas(){
        super();
    }

    public void setImage(BufferedImage image){
        if (this.label == null){
            this.label = new JLabel(new ImageIcon(image));
            this.add(this.label);
        }else{
            this.label.setIcon(new ImageIcon(image));
        }
    }


    public static void main(String[] args) throws Exception{

        ColorCanvas frame = new ColorCanvas();

        //BufferedImage smart = ImageIO.read(new File("images/smart.png"));
        //BufferedImage smart = ImageIO.read(new File("output/colorAndLight.bmp"));
        BufferedImage smart =  createGradient();

        frame.setImage(smart);

        frame.setVisible(true);
        frame.pack();
        Thread.sleep(1000);

        for (int i = 0;i<=100;i++){
            BufferedImage image = AddColors.illuminate(smart, 1F,1F*i/100,1F*(100-i)/100);
            frame.setImage(image);
            Thread.sleep(100);
        }
    }


    public static BufferedImage createGradient(){
        BufferedImage img = new BufferedImage(400,400, BufferedImage.TYPE_INT_RGB);

        for (int y = 0;y<img.getHeight();y++){
            for(int x=0;x<img.getWidth();x++){
                int color;
                if (y<200){
                    color = 0x000000 + x % 255;
                }else{
                    color = 0x000000 + (400-x) % 255;
                }

                img.setRGB(x,y,color);
            }
        }
        return img;
    }

}
