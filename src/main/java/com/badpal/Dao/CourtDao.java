/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.badpal.Dao;

import com.badpal.Model.Booking;
import com.badpal.Model.Court;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Shaz
 */
public interface CourtDao {

    List<Court> retrieveAll(String location, Booking bookingDetails) throws SQLException;

    Court retrieve(String courtId) throws SQLException;

    List<String> retrieveCities() throws SQLException;
}
