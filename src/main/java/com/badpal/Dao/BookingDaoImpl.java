/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.badpal.Dao;

import com.badpal.DBConnection;
import com.badpal.Model.Booking;
import com.badpal.Model.BookingDetails;
import com.badpal.Model.Court;
import com.badpal.Model.CourtImage;
import com.badpal.Model.User;
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
 * @author garyl
 */
public class BookingDaoImpl implements BookingDao {

    private final Connection connection;

    public BookingDaoImpl() {
        connection = DBConnection.getConnection();
    }

    @Override
    public int insert(String courtId, User user, Booking booking, int price) throws SQLException {
        String query = "INSERT booking(client_id, court_id) VALUES(?, ?);";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, user.getId());
        ps.setString(2, courtId);
        if (ps.executeUpdate() < 0) {
            return -1;
        }
        System.out.println(ps);

        String glueQuery = "SELECT id FROM booking WHERE client_id = ? AND court_id = ?;";
        ps = connection.prepareStatement(glueQuery);
        ps.setString(1, user.getId());
        ps.setString(2, courtId);
        ResultSet rs = ps.executeQuery();
        rs.next();
        System.out.println(ps);

        String detailQuery = "INSERT booking_details(date, time, duration, booking_id, price_in_cents) VALUES (?, ?, ?, ?, ?);";
        ps = connection.prepareStatement(detailQuery);
        ps.setDate(1, new Date(booking.getDate().getDate()));
        ps.setTime(2, booking.getTime());
        ps.setTime(3, booking.getDuration());
        ps.setString(4, rs.getString(1));
        ps.setInt(5, booking.getPriceInCents());

        System.out.println(ps);
        return ps.executeUpdate();

    }

    @Override
    public List<BookingDetails> retrieve(String userId) throws SQLException {
        String query = "SELECT b.id id, c.name name, img, price_in_cents, date, time, duration, state, city FROM booking b JOIN booking_details bd ON bd.booking_id = b.id JOIN court_image ci ON b.court_id =  ci.court_id JOIN court c ON c.id = b.court_id WHERE client_id = ? GROUP BY b.id;";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, userId);
        ResultSet rs = ps.executeQuery();
        List<BookingDetails> booking_list = new ArrayList<>();
        while (rs.next()) {
            // Set up court
            Court currentBooking = new Court();
            currentBooking.setName(rs.getString(2));

            // Setup image
            Blob image = rs.getBlob(3);
            List<Blob> imageList = new ArrayList<>();
            imageList.add(image);
            CourtImage ci = new CourtImage();
            ci.setImages(imageList);
            currentBooking.setImages(ci);
            currentBooking.setState(rs.getString(8));
            currentBooking.setCity(rs.getString(9));

            // Setup booking
            Booking booking = new Booking();
            booking.setId(rs.getString(1));
            booking.setPriceInCents(rs.getInt(4));
            booking.setDate(rs.getDate(5));
            booking.setTime(rs.getTime(6));
            booking.setDuration(rs.getTime(7));

            // Setup booking details and add into the list
            BookingDetails bd = new BookingDetails();
            bd.setCourt(currentBooking);
            bd.setBooking(booking);
            booking_list.add(bd);
        }
        return booking_list;
    }

    @Override
    public int delete(String bookingId) throws SQLException {
        String queryDeleteFromBookingDetails = "DELETE FROM booking_details WHERE booking_id = ?;";
        String queryDeleteFromBooking = "DELETE FROM booking WHERE id = ?";

        PreparedStatement ps = connection.prepareStatement(queryDeleteFromBookingDetails);
        ps.setString(1, bookingId);
        ps.executeUpdate();

        ps = connection.prepareStatement(queryDeleteFromBooking);
        ps.setString(1, bookingId);
        return ps.executeUpdate();
    }

}
