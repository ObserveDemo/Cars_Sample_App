/*
 * Created on 31-May-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.supercars.dataloader;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.supercars.Manufacturer;
import com.supercars.logging.CarLogger;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author v023094
 *
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class ManufacturerDataLoader {

    private final static Logger logger = Logger.getLogger(ManufacturerDataLoader.class.getName());

    static {
        CarLogger.setup(ManufacturerDataLoader.class.getName());
    }

    public List<Manufacturer> getManufacturers() {

        Manufacturer manufacturer = null;
        List manufacturers = new ArrayList();

        try ( Connection connection = Constants.getDBConnection()) {
            String sql = "SELECT MANUFACTURER_ID, NAME, WEB, EMAIL, LOGO FROM MANUFACTURER ORDER BY NAME";

            try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    manufacturer = new Manufacturer();
                    manufacturer.setManufacturerId(resultSet.getInt("MANUFACTURER_ID"));
                    manufacturer.setName(resultSet.getString("NAME"));
                    manufacturer.setWeb(resultSet.getString("WEB"));
                    manufacturer.setEmail(resultSet.getString("EMAIL"));
                    manufacturer.setLogo(resultSet.getString("LOGO"));
                    manufacturers.add(manufacturer);
                }
            }
            connection.close();
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "SQLException getting Manufacturers", ex);
        }

        return manufacturers;
    }

    public Manufacturer getManufacturer(int manufacturerId) {

        Manufacturer manufacturer = new Manufacturer();

        try ( Connection connection = Constants.getDBConnection()) {
            String sql = "SELECT MANUFACTURER_ID, NAME, WEB, EMAIL, LOGO FROM MANUFACTURER WHERE MANUFACTURER_ID = " + manufacturerId;

            try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
                if (resultSet.next()) {
                    manufacturer.setManufacturerId(resultSet.getInt("MANUFACTURER_ID"));
                    manufacturer.setName(resultSet.getString("NAME"));
                    manufacturer.setWeb(resultSet.getString("WEB"));
                    manufacturer.setEmail(resultSet.getString("EMAIL"));
                    manufacturer.setLogo(resultSet.getString("LOGO"));
                }
            }
            connection.close();
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "SQLException getting Manufacturer ID:" + Integer.toString(manufacturerId), ex);
        }

        return manufacturer;
    }
}
