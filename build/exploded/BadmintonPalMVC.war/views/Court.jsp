<%--
    Document   : Court
    Created on : Jun 13, 2022, 4:24:29 PM
    Author     : garyl
--%>

<%@page contentType="text/html" pageEncoding="utf-8"%>
<%@include file="./../../partials/_default.jsp"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@page import="com.badpal.Model.Court"%>
<%@page import="java.sql.Blob"%>
<%@page import="javax.servlet.jsp.PageContext"%>

<c:set var="latitude" value="${sessionScope.court.getLatitude()}"/>
<c:set var="longitude" value="${sessionScope.court.getLongitude()}"/>

<fmt:setLocale value="en_MY"/>
<%
    Court court = (Court) request.getSession().getAttribute("court");
    System.out.print(court.getName());
%>
<main>
    <section class="flex flex-col justify-center items-center h-screen bg-base-200">

        <div class="flex justify-center w-[80%] h-[80%] swiper mySwiper">
            <div class="swiper-wrapper">
                <%
                    for(int i = 0; i< court.getImages().getImages().size(); i++){
                        Blob currentImg = court.getImages().getImages().get(i);
                        byte[] content = currentImg.getBytes(1, (int) currentImg.length());
                        String base64Encoded = new String(Base64.encodeBase64(content), "UTF-8");
                %>
                <div class="swiper-slide rounded-lg">
                    <img src="data:image/png;base64,<%=base64Encoded%>" alt="<%= court.getName() %>" class="w-full rounded-xl" />
                </div>
                <%
                    }
                %>
            </div>
            <div class="swiper-button-next"></div>
            <div class="swiper-button-prev"></div>
            <div class="swiper-pagination"></div>
        </div>

    </section>

    <section class=" stats shadow flex flex-col justify-center items-center h-screen bg-base-200">
        <div class="w-[80%] bg-base-100 rounded-xl p-10">

            <div class="stat">
                <div class="stat-title">Name</div>
                <div class="stat-value text-primary"><c:out value="<%= court.getName() %>"/></div>
            </div>

            <div class="stat">
                <div class="stat-figure text-primary">
                    <i class="ph-clock-clockwise ph-2x"></i>
                </div>
                <div class="stat-title">Opening Hours: Pricing</div>
                <c:forEach var="__availability__" items="<%= court.getAvailability()%>" varStatus="loop">
                    <div class="stat-value text-primary"><c:out value="${__availability__.getAvailableDay()}: ${__availability__.getOpeningHour()} - ${__availability__.getClosingHour()}"/>: <fmt:formatNumber value="${__availability__.getPricePerHour()}" type="currency"/></div>
                </c:forEach>
            </div>

    </section>

    <section class="flex justify-center items-center h-screen bg-base-200">
        <div class="w-[80%] h-[80%] text-center">
            <h1 class="text-5xl font-bold">Location</h1>
            <div class="mt-6 w-full h-full flex justify-center items-center">
                <div id="map" class="rounded-lg mapboxgl-map" style="width: 100vw;height:70vh;"></div>
            </div>
        </div>

        <script>
            const ACCESS_TOKEN = 'pk.eyJ1Ijoia29ob2dhIiwiYSI6ImNsNGNldGtiNDF2OHEzZHIzaThld3ZuZDMifQ.eOcNdv6tvk1Qy-Z4D0BwNA';
            mapboxgl.accessToken = ACCESS_TOKEN;
            var map = new mapboxgl.Map({
                container: 'map',
                style: 'mapbox://styles/mapbox/satellite-streets-v11',
                center: [<%= pageContext.getAttribute("latitude")%>, <%= pageContext.getAttribute("longitude")%>],
                zoom: 15
            });

            const nav = new mapboxgl.NavigationControl();
            map.addControl(nav);

            const marker = new mapboxgl.Marker()
                    .setLngLat([<%= pageContext.getAttribute("latitude")%>, <%= pageContext.getAttribute("longitude")%>])
                    .addTo(map);


            <%-- Initialize Swiper --%>
            var swiper = new Swiper(".mySwiper", {
                spaceBetween: 30,
                centeredSlides: true,
                autoplay: {
                    delay: 2500,
                    disableOnInteraction: false
                },
                pagination: {
                    el: ".swiper-pagination",
                    clickable: true
                },
                navigation: {
                    nextEl: ".swiper-button-next",
                    prevEl: ".swiper-button-prev"
                }
            });
        </script>
    </section>
</main>

