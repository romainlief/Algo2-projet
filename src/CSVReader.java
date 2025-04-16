import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CSVReader {
    public static String directory = "GTFS";
    public static String[] entreprises = { "SNCB", "TEC", "STIB", "DELIJN" };

    public static void readFiles() {
        for (String entreprise : entreprises) {
            String filePath = directory + "/" + entreprise + "/";
        }
    }
}