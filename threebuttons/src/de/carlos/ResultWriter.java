package de.carlos;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class ResultWriter {

    /**
     * Hier wird in die Datei geschrieben.
     * 
     * @param a
     * @param b
     * @param c
     * @param result
     */
    public static void writeToFile(double a, double b, double c, double result) {

	File f = new File("outfile.txt");
	PrintWriter writer = null;
	try {

	    //Das "true" sorgt dafür, dass nicht überschrieben wird.
	    writer = new PrintWriter(new FileOutputStream(f, true));
	    writer.write(a + ";" + b + ";" + c + ";" + result + "\n");

	} catch (FileNotFoundException e) {
	    throw new RuntimeException(e);
	} finally {
	    if (writer != null)
		writer.close();
	}

    }

}
