package concurrent.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Configuration {

    private static final String ITERATIONS_QUANTITY_FIELD_NAME = "iterations_initial_quantity";
    private static final String BODIES_QUANTITY_FIELD_NAME = "bodies_initial_quantity";
    private static final String DT_FIELD_NAME = "delta_incremental_time";
    private static final String UPPER_BOUNDARY_FIELD_NAME = "upper_boundary";
    private static final String LOWER_BOUNDARY_FIELD_NAME = "lower_boundary";
    private static final String RIGHTER_BOUNDARY_FIELD_NAME = "righter_boundary";
    private static final String LEFTER_BOUNDARY_FIELD_NAME = "lefter_boundary";
    private final Properties properties;

    public Configuration(final String fileName) throws FileNotFoundException {
        this.properties = new Properties();
        String pathForProject = new File("src/main/resources/"+fileName)
                .getAbsolutePath();
        File configurationFile = new File(pathForProject);
        File configurationFileJar = new File(fileName);
        FileInputStream inputStream;
        if(configurationFile.exists()){
            inputStream = new FileInputStream(configurationFile);
        } else {
            inputStream = new FileInputStream(configurationFileJar);
        }

        try {
            this.properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double getDT(){
        return Double.parseDouble(this.properties.getProperty(Configuration.DT_FIELD_NAME));
    }

    public int getBodiesQuantity(){
        return Integer.parseInt(this.properties.getProperty(Configuration.BODIES_QUANTITY_FIELD_NAME));
    }

    public int getIterationsQuantity(){
        return Integer.parseInt(this.properties.getProperty(Configuration.ITERATIONS_QUANTITY_FIELD_NAME));
    }

    public int getUpperBoundary(){
        return Integer.parseInt(this.properties.getProperty(Configuration.UPPER_BOUNDARY_FIELD_NAME));
    }

    public int getLowerBoundary(){
        return Integer.parseInt(this.properties.getProperty(Configuration.LOWER_BOUNDARY_FIELD_NAME));
    }

    public int getRighterBoundary(){
        return Integer.parseInt(this.properties.getProperty(Configuration.RIGHTER_BOUNDARY_FIELD_NAME));
    }

    public int getLefterBoundary(){
        return Integer.parseInt(this.properties.getProperty(Configuration.LEFTER_BOUNDARY_FIELD_NAME));
    }

}
