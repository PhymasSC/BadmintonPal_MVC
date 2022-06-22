/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.badpal.Controller;

import javax.servlet.annotation.WebServlet;
import at.favre.lib.crypto.bcrypt.BCrypt;
import com.badpal.Dao.UserDaoImpl;
import com.badpal.Model.User;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 *
 * @author SC
 */
@WebServlet("/Authentication")
@MultipartConfig(maxFileSize = 10485760, maxRequestSize = 20971520, fileSizeThreshold = 5242880)
public class UserServlet extends HttpServlet {

    private final String REGISTER_URL = "/Register";
    private final String LOGIN_URL = "/Login";
    private final String PROFILE_URL = "/User";
    private final String HOME_URL = "/";
    private final int BCRYPT_SALT = 10;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {
        String router = request.getParameter("command");
        switch (router.toUpperCase()) {
            case "INSERT":
                insertCustomer(request, response);
                break;
            case "RETRIEVE":
                retrieveCustomer(request, response);
                break;
            case "UPDATE":
                updateCustomer(request, response);
                break;
            case "LOGOUT":
                logout(request, response);
                break;
            default:
                System.err.println("Unsupported Operation! Command `" + router + "` does not exist!");
        }
    }

    private void insertCustomer(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String email = request.getParameter("email");
        String firstName = request.getParameter("first_name");
        String lastName = request.getParameter("last_name");
        String name = request.getParameter("username");
        String phone = request.getParameter("phone");
        String plainPassword = request.getParameter("password");
        String repeatPassword = request.getParameter("password_confirmation");

        if (!plainPassword.equals(repeatPassword)) {
            request.setAttribute("errMsg", "Password does not match!");
            RequestDispatcher dispatcher = request.getRequestDispatcher(request.getContextPath() + REGISTER_URL);
            dispatcher.forward(request, response);
        }

        User user = new User();
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(name);
        user.setPhoneNo(phone);
        user.setPassword(BCrypt.withDefaults().hashToString(BCRYPT_SALT, plainPassword.toCharArray()));

        if (new UserDaoImpl().insert(user) < 0) {
            request.setAttribute("errMsg", "Internal error! Please try again.");
        }

        response.sendRedirect(request.getContextPath() + LOGIN_URL);
    }

    private void retrieveCustomer(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String email = request.getParameter("email");
        String plainPassword = request.getParameter("password");
        // Assume email exist
        User customer = new UserDaoImpl().retrieve(email);
        if (customer == null || !BCrypt.verifyer().verify(plainPassword.toCharArray(), customer.getPassword()).verified) {
            request.setAttribute("errMsg", "Invalid email or password! Press <a href='<%= request.getContextPath() %>/Register'>here</a> to register an account!");
            response.sendRedirect(request.getContextPath() + LOGIN_URL);
            return;
        }
        request.getSession().setAttribute("isLoggedIn", true);
        request.getSession().setAttribute("user", customer);

        response.sendRedirect(request.getContextPath() + HOME_URL);
    }

    private void updateCustomer(HttpServletRequest request, HttpServletResponse response) throws Exception {
        User userInSession = (User) request.getSession().getAttribute("user");
        String email = request.getParameter("email");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String name = request.getParameter("username");
        String phone = request.getParameter("phoneNo");
        String plainOldPassword = request.getParameter("oldPass");
        String plainPassword = request.getParameter("newPass");
        String repeatPassword = request.getParameter("newPassConfirm");

        String authenticatedOldPassInHashedForm = userInSession.getPassword();

        // Check if user really want to change password
        if (!plainPassword.equals("") && !repeatPassword.equals("")) {
            // Check if old password same as authenticated password
            if (!repeatPassword.equals("plainNewPassword") || !BCrypt.verifyer().verify(plainOldPassword.toCharArray(), authenticatedOldPassInHashedForm).verified) {
                request.setAttribute("errMsg", "Wrong password!");
                response.sendRedirect(request.getContextPath() + PROFILE_URL + "/" + userInSession.getId());
                return;
            }
        }

        User user = new User();
        user.setId(userInSession.getId());
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(name);
        user.setPhoneNo(phone);

        if (!plainPassword.equals("") && !repeatPassword.equals("")) {
            user.setPassword(BCrypt.withDefaults().hashToString(BCRYPT_SALT, plainPassword.toCharArray()));
        }

        InputStream profileImage = null, coverImage = null;
        if (request.getPart("profile_pic") != null && request.getPart("profile_pic").getSize() > 0) {
            Part filePart = request.getPart("profile_pic");
            profileImage = filePart.getInputStream();
        }
        if (request.getPart("cover_photo") != null && request.getPart("cover_photo").getSize() > 0) {
            Part filePart = request.getPart("cover_photo");
            coverImage = filePart.getInputStream();
        }

        if (new UserDaoImpl().update(user, profileImage, coverImage) < 0) {
            request.setAttribute("errMsg", "Internal error! Please try again.");
            response.sendRedirect(request.getContextPath() + PROFILE_URL + "/" + userInSession.getId());
            return;
        }

        user = new UserDaoImpl().retrieve(email);
        request.getSession().setAttribute("user", user);
        response.sendRedirect(request.getContextPath() + PROFILE_URL + "/" + userInSession.getId());
    }

    private void logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.getSession().invalidate();
        response.sendRedirect(request.getContextPath() + LOGIN_URL);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
