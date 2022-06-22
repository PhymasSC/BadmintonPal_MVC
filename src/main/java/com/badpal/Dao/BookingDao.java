/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.badpal.Dao;

import com.badpal.Model.Booking;
import com.badpal.Model.BookingDetails;
import com.badpal.Model.Court;
import com.badpal.Model.User;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author garyl
 */
public interface BookingDao {

    int insert(String courtId, User user, Booking book, int price) throws SQLException;

    List<BookingDetails> retrieve(String userId) throws SQLException;

    int delete(String bookingId) throws SQLException;

}
