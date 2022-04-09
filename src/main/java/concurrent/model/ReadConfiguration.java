package concurrent.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ReadConfiguration {

    private static final String ITERATIONS_QUANTITY_FIELD_NAME = "iterationsQuantity";
    private static final String BODIES_QUANTITY_FIELD_NAME = "bodiesQuantity";
    private static final String UPPER_BOUNDARY_FIELD_NAME = "upperBoundary";
    private static final String LOWER_BOUNDARY_FIELD_NAME = "lowerBoundary";
    private static final String RIGHTER_BOUNDARY_FIELD_NAME = "righterBoundary";
    private static final String LEFTER_BOUNDARY_FIELD_NAME = "lefterBoundary";
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
        return Double.parseDouble(this.properties.getProperty("dt"));
    }

    public int bodiesQuantity(){
        return Integer.parseInt(this.properties.getProperty(ReadConfiguration.BODIES_QUANTITY_FIELD_NAME));
    }

    public int iterationsQuantity(){
        return Integer.parseInt(this.properties.getProperty(ReadConfiguration.ITERATIONS_QUANTITY_FIELD_NAME));
    }

    public int upperBoundary(){
        return Integer.parseInt(this.properties.getProperty(ReadConfiguration.UPPER_BOUNDARY_FIELD_NAME));
    }

    public int lowerBoundary(){
        return Integer.parseInt(this.properties.getProperty(ReadConfiguration.LOWER_BOUNDARY_FIELD_NAME));
    }

    public int righterBoundary(){
        return Integer.parseInt(this.properties.getProperty(ReadConfiguration.RIGHTER_BOUNDARY_FIELD_NAME));
    }

    public int lefterBoundary(){
        return Integer.parseInt(this.properties.getProperty(ReadConfiguration.LEFTER_BOUNDARY_FIELD_NAME));
    }

}
