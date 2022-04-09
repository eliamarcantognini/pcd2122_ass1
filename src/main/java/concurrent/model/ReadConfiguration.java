package concurrent.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ReadConfiguration {

    private static final String ITERATIONS_QUANTITY_FIELD_NAME = "iterations_initial_quantity";
    private static final String BODIES_QUANTITY_FIELD_NAME = "bodies_initial_quantity";
    private static final String DT_FIELD_NAME = "delta_incremental_time";
    private static final String UPPER_BOUNDARY_FIELD_NAME = "upper_boundary";
    private static final String LOWER_BOUNDARY_FIELD_NAME = "lower_boundary";
    private static final String RIGHTER_BOUNDARY_FIELD_NAME = "righter_boundary";
    private static final String LEFTER_BOUNDARY_FIELD_NAME = "lefter_boundary";
    private final Properties properties;

    public ReadConfiguration(final String fileName) throws IOException {
        this.properties = new Properties();
        String path = new File("src/main/resources/"+fileName)
                .getAbsolutePath();
        FileInputStream inputStream = new FileInputStream(path);
        this.properties.load(inputStream);
//        this.properties.load(getClass().getResourceAsStream(fileName));
    }

    public double getDT(){
        return Double.parseDouble(this.properties.getProperty(ReadConfiguration.DT_FIELD_NAME));
    }

    public int getBodiesQuantity(){
        return Integer.parseInt(this.properties.getProperty(ReadConfiguration.BODIES_QUANTITY_FIELD_NAME));
    }

    public int getIterationsQuantity(){
        return Integer.parseInt(this.properties.getProperty(ReadConfiguration.ITERATIONS_QUANTITY_FIELD_NAME));
    }

    public int getUpperBoundary(){
        return Integer.parseInt(this.properties.getProperty(ReadConfiguration.UPPER_BOUNDARY_FIELD_NAME));
    }

    public int getLowerBoundary(){
        return Integer.parseInt(this.properties.getProperty(ReadConfiguration.LOWER_BOUNDARY_FIELD_NAME));
    }

    public int getRighterBoundary(){
        return Integer.parseInt(this.properties.getProperty(ReadConfiguration.RIGHTER_BOUNDARY_FIELD_NAME));
    }

    public int getLefterBoundary(){
        return Integer.parseInt(this.properties.getProperty(ReadConfiguration.LEFTER_BOUNDARY_FIELD_NAME));
    }

}
