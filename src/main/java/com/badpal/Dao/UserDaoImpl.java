/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.badpal.Dao;

/**
 *
 * @author Shaz
 */
import at.favre.lib.crypto.bcrypt.BCrypt;
import com.badpal.DBConnection;
import com.badpal.Model.User;
import java.io.InputStream;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 *
 */
public class UserDaoImpl implements UserDao {

    private final Connection connection;

    public UserDaoImpl() {
        connection = DBConnection.getConnection();
    }

    @Override
    public int insert(User client) throws SQLException {
        String query = "INSERT INTO client (first_name, last_name, username, phone_number, email_address, password) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, client.getFirstName());
        ps.setString(2, client.getLastName());
        ps.setString(3, client.getUsername());
        ps.setString(4, client.getPhoneNo());
        ps.setString(5, client.getEmail());
        ps.setString(6, client.getPassword());

        return ps.executeUpdate();
    }

    @Override
    public User retrieve(String email) throws SQLException {
        String query = " SELECT * FROM client WHERE email_address = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();

        User client = new User();
        if (!rs.next()) {
            return null;
        }

        client.setId(rs.getString(1));
        client.setFirstName(rs.getString(2));
        client.setLastName(rs.getString(3));
        client.setUsername(rs.getString(4));
        client.setPhoneNo(rs.getString(5));
        client.setEmail(rs.getString(6));
        client.setPassword(rs.getString(7));
        client.setProfilePic(rs.getBlob(8));
        client.setCoverPic(rs.getBlob(9));
        System.out.println(client.getCoverPic());
        return client;
    }

    @Override
    public int update(User user, InputStream profilePic, InputStream coverPic) throws SQLException {
        String query = "UPDATE client SET first_name =  ?, last_name = ?, username = ?, phone_number = ?, email_address = ? ";
        boolean hasPasswordChanged = false, hasProfilePicChanged = false, hasCoverPicChanged = false;
        if (user.getPassword() != null) {
            query += ", password = ? ";
            hasPasswordChanged = true;
        }

        if (profilePic != null) {
            query += ", profile_pic = ? ";
            hasProfilePicChanged = true;
        }

        if (coverPic != null) {
            query += ", cover_photo = ? ";
            hasCoverPicChanged = true;
        }

        query += "WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, user.getFirstName());
        ps.setString(2, user.getLastName());
        ps.setString(3, user.getUsername());
        ps.setString(4, user.getPhoneNo());
        ps.setString(5, user.getEmail());

        if (hasPasswordChanged) {
            ps.setString(6, BCrypt.withDefaults().hashToString(12, user.getPassword().toCharArray()));
        }

        if (hasProfilePicChanged) {
            ps.setBlob(
                    hasPasswordChanged
                            ? 7 : 6,
                    profilePic);
        }

        if (hasCoverPicChanged) {
            ps.setBlob(
                    hasPasswordChanged
                            ? (hasProfilePicChanged ? 8 : 7)
                            : (hasProfilePicChanged ? 7 : 6),
                    coverPic);
        }

        ps.setString(hasPasswordChanged
                ? (hasProfilePicChanged
                        ? (hasCoverPicChanged ? 9 : 8)
                        : 7)
                : (hasProfilePicChanged ? (hasCoverPicChanged ? 8 : 7)
                        : 6),
                user.getId());
        return ps.executeUpdate();
    }

}
