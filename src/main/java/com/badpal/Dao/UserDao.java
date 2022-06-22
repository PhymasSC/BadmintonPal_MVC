/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.badpal.Dao;

import com.badpal.Model.User;
import java.io.InputStream;
import java.sql.SQLException;

/**
 *
 * @author Shaz
 */
public interface UserDao {

    int insert(User client) throws SQLException;

    User retrieve(String email) throws SQLException;

    int update(User client, InputStream profilePic, InputStream coverPic) throws SQLException;

}
