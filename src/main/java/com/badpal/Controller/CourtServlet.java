/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.badpal.Controller;

import com.badpal.Dao.CourtDaoImpl;
import com.badpal.Model.Booking;
import com.badpal.Model.Court;
import java.io.IOException;
import java.util.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
 * @author SC
 */
@WebServlet("/CourtController")
public class CourtServlet extends HttpServlet {

    private final String COURT_URL = "/Court";
    private final String COURT_LIST_URL = "/Courts";

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
            case "GET_CITIES":
                getCities(request, response);
                break;
            case "RETRIEVE_ALL":
                retrieveAll(request, response);
                break;
            case "RETRIEVE":
                retrieve(request, response);
                break;
            default:
                System.err.println("Unsupported Operation! Command `" + router + "` does not exist!");

        }
    }

    private void retrieveAll(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException, ParseException {
        String location = request.getParameter("court_location");
        // Formatter
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfTime = new SimpleDateFormat("hh:mm");
        Date bookDate = sdfDate.parse(request.getParameter("book_date"));
        Date bookTime = sdfTime.parse(request.getParameter("book_time"));
        Date bookDuration = sdfTime.parse(request.getParameter("book_duration"));
        Time sqlBookTime = new Time(bookTime.getTime());
        Time sqlBookDuration = new Time(bookDuration.getTime());

        Booking booking = new Booking();
        booking.setDate(bookDate);
        booking.setTime(sqlBookTime);
        booking.setDuration(sqlBookDuration);
        List<Court> courtList = new CourtDaoImpl().retrieveAll(location, booking);

        HttpSession session = request.getSession(false);
        session.setAttribute("booking_details", booking);
        session.setAttribute("courts_list", courtList);
        response.sendRedirect(request.getContextPath() + COURT_LIST_URL);
    }

    private void retrieve(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        String courtId = request.getParameter("id");
        Court court = new CourtDaoImpl().retrieve(courtId);
        HttpSession session = request.getSession(false);
        session.setAttribute("court", court);
        response.sendRedirect(request.getContextPath() + COURT_URL + "/" + courtId);
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
            Logger.getLogger(CourtServlet.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(CourtServlet.class.getName()).log(Level.SEVERE, null, ex);
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

    private void getCities(HttpServletRequest request, HttpServletResponse response) throws SQLException, ParseException {
        List<String> cityList = new CourtDaoImpl().retrieveCities();
        request.setAttribute("cityList", cityList);
    }

}
