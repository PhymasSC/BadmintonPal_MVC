/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.badpal.Dao;

import com.badpal.DBConnection;
import com.badpal.Model.Booking;
import com.badpal.Model.Court;
import com.badpal.Model.CourtAvailability;
import com.badpal.Model.CourtImage;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author S58031
 */
public class CourtDaoImpl implements CourtDao {

    private final Connection connection;

    public CourtDaoImpl() {
        connection = DBConnection.getConnection();
    }

    @Override
    public List<Court> retrieveAll(String location, Booking details) throws SQLException {

        String query = "SELECT * FROM court c JOIN court_availability ca ON c.id = ca.court_id JOIN court_image ci ON ci.court_id = c.id WHERE available_day = DAYNAME(?) AND opening_hour < ? AND closed_hour > ADDTIME(?, ?)";

        if (!location.equals("all")) {
            query += "AND city = ? ";
        }

        query += "GROUP BY (c.id)";

        PreparedStatement ps = connection.prepareStatement(query);
        ps.setDate(1, new Date(details.getDate().getTime()));
        ps.setTime(2, details.getTime());
        ps.setTime(3, details.getTime());
        ps.setTime(4, details.getDuration());

        if (!location.equals("all")) {
            ps.setString(5, location);
        }

        List<Court> courtList = new ArrayList<>();
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Court currentCourt = new Court();
            currentCourt.setId(rs.getString(1));
            currentCourt.setName(rs.getString(2));
            currentCourt.setState(rs.getString(3));
            currentCourt.setCity(rs.getString(4));
            currentCourt.setLatitude(rs.getFloat(5));
            currentCourt.setLongitude(rs.getFloat(6));
            currentCourt.setPriceInCents(rs.getInt(13));

            // Setup image
            Blob image = rs.getBlob(16);
            List<Blob> imageList = new ArrayList<>();
            imageList.add(image);
            CourtImage ci = new CourtImage();
            ci.setImages(imageList);

            currentCourt.setImages(ci);
            courtList.add(currentCourt);
        }
        return courtList;
    }

    @Override
    public Court retrieve(String courtId) throws SQLException {
        String query = "SELECT c.id court_id, name, state, city, latitude, longitude FROM court c JOIN court_availability ca ON c.id = ca.court_id JOIN court_image ci ON ci.court_id = c.id WHERE c.id= ? GROUP BY available_day;";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, courtId);
        ResultSet rs = ps.executeQuery();
        Court court = new Court();
        if (!rs.next()) {
            return null;
        }

        // Setup basic informations
        court.setId(rs.getString(1));
        court.setName(rs.getString(2));
        court.setState(rs.getString(3));
        court.setCity(rs.getString(4));
        court.setLatitude(rs.getFloat(5));
        court.setLongitude(rs.getFloat(6));

        String imgQuery = "SELECT img FROM court_image WHERE court_id= ?";

        ps = connection.prepareStatement(imgQuery);
        ps.setString(1, courtId);
        rs = ps.executeQuery();

        // Setup images
        List<Blob> imageList = new ArrayList<>();
        CourtImage ci = new CourtImage();
        while (rs.next()) {
            Blob image = rs.getBlob(1);
            imageList.add(image);
        }
        ci.setImages(imageList);
        court.setImages(ci);

        String availabilityQuery = "SELECT available_day, LOWER(TIME_FORMAT(opening_hour, '%r')) opening_hour, LOWER(TIME_FORMAT(closed_hour, '%r')) closed_hour, price_in_cent_per_hour / 100 AS price FROM court_availability WHERE court_id = ?;";

        ps = connection.prepareStatement(availabilityQuery);
        ps.setString(1, courtId);
        rs = ps.executeQuery();

        List<CourtAvailability> availabilities = new ArrayList<>();
        while (rs.next()) {
            CourtAvailability courtAvailability = new CourtAvailability();
            courtAvailability.setAvailableDay(rs.getString(1));
            courtAvailability.setOpeningHour(rs.getString(2));
            courtAvailability.setClosingHour(rs.getString(3));
            courtAvailability.setPricePerHour(rs.getString(4));
            availabilities.add(courtAvailability);
        }
        court.setAvailability(availabilities);

        return court;
    }

    @Override
    public List<String> retrieveCities() throws SQLException {
        String query = "SELECT DISTINCT city FROM court;";
        PreparedStatement ps = connection.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        List<String> cityList = new ArrayList<>();
        while (rs.next()) {
            cityList.add(rs.getString(1));
        }
        return cityList;
    }

}
