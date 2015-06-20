package de.carlos.simplexFood;

import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;


/**
 * 
 * Exports the current schema of the database. 
 * 
 * 
 * @author carlos
 *
 */
public class ExportSchema {

	public static void main(String[] args) {
		
	    Configuration config = HibernateUtil.getConfiguration();
	    config.setProperty("hbm2ddl.auto", "create");
		SchemaExport export = new SchemaExport(config);
		export.setDelimiter(";");
		export.create(true, false);
		

	}

}
