/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.badpal.Controller;

import com.badpal.Dao.BookingDaoImpl;
import com.badpal.Model.Booking;
import com.badpal.Model.BookingDetails;
import com.badpal.Model.Court;
import com.badpal.Model.User;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author garyl
 */
@WebServlet(name = "BookingServlet", urlPatterns = {"/BookingController"})
public class BookingServlet extends HttpServlet {

    private final String COURT_LIST_URL = "/Courts";
    private final String PROFILE_URL = "/User";

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        String router = request.getParameter("command");
        switch (router.toUpperCase()) {
            case "INSERT":
                insertBooking(request, response);
                break;
            case "RETRIEVE":
                retrieveBooking(request, response);
                break;
            case "DELETE":
                deleteBooking(request, response);
                break;
            default:
                System.err.println("Unsupported Operation! Command `" + router + "` does not exist!");
        }
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
        } catch (SQLException ex) {
            Logger.getLogger(BookingServlet.class.getName()).log(Level.SEVERE, null, ex);
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
        } catch (SQLException ex) {
            Logger.getLogger(BookingServlet.class.getName()).log(Level.SEVERE, null, ex);
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

    private void insertBooking(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        String courtId = request.getParameter("id");
        int price = Integer.parseInt(request.getParameter("price"));
        if (new BookingDaoImpl().insert(
                courtId,
                (User) request.getSession().getAttribute("user"),
                (Booking) request.getSession().getAttribute("booking_details"),
                price
        ) < 0) {
            request.setAttribute("errMsg", "Internal error! Please try again.");
        }
        response.sendRedirect(request.getContextPath() + COURT_LIST_URL);
    }

    private void retrieveBooking(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        User user = (User) request.getSession().getAttribute("user");
        List<BookingDetails> booking_history = new BookingDaoImpl().retrieve(user.getId());
        request.setAttribute("booking_history", booking_history);
    }

    private void deleteBooking(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        String bookingId = request.getParameter("id");
        if (new BookingDaoImpl().delete(bookingId) < 0) {
            request.setAttribute("errMsg", "Internal error! Please try again.");
            response.sendRedirect(request.getContextPath() + PROFILE_URL + "/" + user.getId());
            return;
        }
        response.sendRedirect(request.getContextPath() + PROFILE_URL + "/" + user.getId());
    }

}
