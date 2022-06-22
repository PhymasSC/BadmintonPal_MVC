<%--
    Document   : Profile
    Created on : 28 May 2022, 01:08:13
    Author     : SC
--%>
<%@page import="com.badpal.Model.BookingDetails"%>
<%@page import="com.badpal.Model.User"%>
<%@page import="org.apache.commons.codec.binary.Base64"%>
<%
    String pathName = request.getRequestURI();
    String userIdFromPath = pathName.substring(pathName.lastIndexOf('/') + 1);
%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@include file="./../../partials/_default.jsp"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<fmt:setLocale value="en_MY"/>
<%   String profilePic = "", coverImage = "";
    if (user.getProfilePic() != null) {
        byte[] content = user.getProfilePic().getBytes(1, (int) user.getProfilePic().length());
        profilePic = new String(Base64.encodeBase64(content), "UTF-8");
    }
    if (user.getCoverPic() != null) {
        byte[] content = user.getCoverPic().getBytes(1, (int) user.getCoverPic().length());
        coverImage = new String(Base64.encodeBase64(content), "UTF-8");
    }
%>

<c:choose>
    <c:when test="${!sessionScope.isLoggedIn}">
        <c:redirect url="${request.getContextPath()}/Login"/>
    </c:when>
    <c:when test="<%= !user.getId().equals(userIdFromPath)%>">
        <% response.sendError(403);%>
    </c:when>
</c:choose>

<jsp:include page="/BookingController?command=RETRIEVE"/>

<head>
    <script src="<%=request.getContextPath()%>/utility/PhoneFormatter.js" defer></script>
</head>

<body>
    <form action="<%=request.getContextPath()%>/Authentication?command=UPDATE" method="POST" enctype="multipart/form-data" x-data="{ mode: 'viewer'}">
        <div class="mt-4 flex flex-col justify-center items-center">

            <div class="indicator w-[80%]">
                <div class="indicator-item indicator-bottom" x-show="mode === 'editor'">
                    <input type="file" id="cover_photo" name="cover_photo" accept="image/*" hidden>
                    <div class="btn btn-circle btn-primary cursor-pointer" onclick="document.querySelector('#cover_photo').click()">
                        <i class="ph-pencil-simple"></i>
                    </div>
                </div>

                <%
                    if (coverImage != null) {
                %>
                <div class="flex justify-around items-center bg-[url(data:image/png;base64,<%= coverImage%>)] gradient-to-r from-indigo-500 via-purple-500 to-pink-500 bg-center bg-cover bg-no-repeat rounded-md artboard artboard-horizontal h-[50vh]">
                    <%
                    } else {
                    %>
                    <div class="flex justify-around items-center bg-gradient-to-r from-indigo-500 via-purple-500 to-pink-500 bg-center bg-cover bg-no-repeat rounded-md artboard artboard-horizontal h-[50vh]">

                        <%
                            }
                        %>

                        <div class="flex flex-col justify-start items-center h-[80%]">
                            <div class="mt-12 flex flex-col justify-center items-center w-full relative top-[12rem]">
                                <div class="indicator">
                                    <div class="indicator-item indicator-bottom" x-show="mode === 'editor'">
                                        <input type="file" id="profile_pic" name="profile_pic" accept="image/*" hidden>
                                        <div class="btn btn-circle btn-primary cursor-pointer" onclick="document.querySelector('#profile_pic').click()">
                                            <i class="ph-pencil-simple"></i>
                                        </div>
                                    </div>
                                    <c:choose>
                                        <c:when test="<%= profilePic != null%>">
                                            <img class="w-36 h-36 avatar rounded-full" src="data:image/png;base64,<%= profilePic%>"/>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="avatar rounded-full bg-neutral-focus text-neutral-content w-36">
                                                <span class="text-3xl"><%= user.getFirstName()%></span>
                                            </div>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="flex justify-center w-[80%] mt-[8rem]">
                    <div x-data="{ activeTab: 0 }" id="tab_wrapper" class="flex flex-col w-full">
                        <!-- The tabs navigation -->
                        <div class="tabs">
                            <label
                                @click="activeTab = 0"
                                class="text-lg w-1/2 tab tab-bordered"
                                :class="{ 'tab-active': activeTab === 0 }"
                                >Personal information</label>
                            <label
                                @click="activeTab = 1"
                                class="text-lg w-1/2 tab tab-bordered"
                                :class="{ 'tab-active': activeTab === 1 }"
                                >Bookings History</label>

                        </div>

                        <!-- The tabs content -->
                        <div class="stats stats-vertical shadow" >
                            <div class="stats stats-vertical shadow" x-show.transition.in.opacity.duration.600="activeTab === 0">
                                <div class="stat">
                                    <div class="stat-title">Account id</div>
                                    <!-- Viewer mode -->
                                    <div class="stat-value" x-show="mode === 'viewer'"><%= user.getId()%></div>
                                    <!-- Editor mode -->
                                    <div class="stat-value" x-show="mode === 'editor'">
                                        <input type="text" name="userId" placeholder="<%= user.getId()%>" value="<%= user.getId()%>" class="text-[2.25rem] input input-ghost w-full" disabled required/>
                                    </div>
                                </div>

                                <div class="stat">
                                    <div class="stat-title">First name</div>
                                    <!-- Viewer mode -->
                                    <div class="stat-value" x-show="mode === 'viewer'"><%= user.getFirstName()%></div>
                                    <!-- Editor mode -->
                                    <div class="stat-value" x-show="mode === 'editor'">
                                        <input type="text" name="firstName" placeholder="First name" value="<%= user.getFirstName()%>" class="text-[2.25rem] input input-ghost w-full" required/>
                                    </div>

                                </div>

                                <div class="stat">
                                    <div class="stat-title">Last name</div>
                                    <!-- Viewer mode -->
                                    <div class="stat-value" x-show="mode === 'viewer'"><%= user.getLastName()%></div>
                                    <!-- Editor mode -->
                                    <div class="stat-value" x-show="mode === 'editor'">
                                        <input type="text" name="lastName" placeholder="Last name" value="<%= user.getLastName()%>" class="text-[2.25rem] input input-ghost w-full" required/>
                                    </div>
                                </div>

                                <div class="stat">
                                    <div class="stat-title">Username</div>
                                    <!-- Viewer mode -->
                                    <div class="stat-value" x-show="mode === 'viewer'"><%= user.getUsername()%></div>
                                    <!-- Editor mode -->
                                    <div class="stat-value" x-show="mode === 'editor'">
                                        <input type="text" name="username" placeholder="Username" value="<%= user.getUsername()%>" class="text-[2.25rem] input input-ghost w-full" required/>
                                    </div>
                                </div>

                                <div class="stat">
                                    <div class="stat-title">Email address</div>
                                    <!-- Viewer mode -->
                                    <div class="stat-value" x-show="mode === 'viewer'"><%= user.getEmail()%></div>
                                    <!-- Editor mode -->
                                    <div class="stat-value" x-show="mode === 'editor'">
                                        <input type="email" name="email" placeholder="Email address" value="<%= user.getEmail()%>" class="text-[2.25rem] input input-ghost w-full" required/>
                                    </div>
                                </div>

                                <div class="stat">
                                    <div class="stat-title">Phone number</div>
                                    <!-- Viewer mode -->
                                    <div class="stat-value" x-show="mode === 'viewer'"><%= user.getPhoneNo()%></div>
                                    <!-- Editor mode -->
                                    <div class="stat-value" x-show="mode === 'editor'">
                                        <input type="tel" id="phone" name="phoneNo" placeholder="Phone number" value="<%= user.getPhoneNo()%>" class="text-[2.25rem] input input-ghost w-full" pattern="[0-9]{3}-[0-9]{3}-[0-9]{4}" required/>
                                    </div>
                                </div>

                                <%-- Only available in editor mode --%>
                                <div class="stat "x-show="mode === 'editor'">
                                    <div class="stat-title">Current password</div>
                                    <div class="stat-value">
                                        <input type="password" name="oldPass" placeholder="•••••••••" class="text-[2.25rem] input input-ghost w-full" required/>
                                    </div>
                                </div>

                                <div class="stat" x-show="mode === 'editor'">
                                    <div class="stat-title">New password (Leave blank if not changing)</div>
                                    <div class="stat-value">
                                        <input type="password" name="newPass" placeholder="•••••••••" class="text-[2.25rem] input input-ghost w-full" />
                                    </div>
                                </div>

                                <div class="stat" x-show="mode === 'editor'">
                                    <div class="stat-title">New password confirmation (Leave blank if not changing)</div>
                                    <div class="stat-value">
                                        <input type="password" name="newPassConfirm" placeholder="•••••••••" class="text-[2.25rem] input input-ghost w-full" />
                                    </div>
                                </div>
                                <%-- --------------------- --%>

                                <div class="btn flex items-center gap-2 mt-6" @click=" mode = 'editor'"  x-show="mode === 'viewer'">
                                    <i class="ph-pencil-simple"></i>
                                    Edit
                                </div>
                                <div class="flex justify-around w-full gap-6 mt-6">
                                    <button class="btn flex items-center gap-2 w-[45%]" x-show="mode === 'editor'">
                                        <i class="ph-pencil-simple"></i>
                                        Update
                                    </button>
                                    <div class="btn flex items-center gap-2 w-[45%]" @click=" mode = 'viewer'"  x-show="mode === 'editor'">
                                        <i class="ph-x"></i>
                                        Cancel
                                    </div>
                                </div>
                                </form>
                            </div>
                            <div class="stats stats-vertical shadow" x-show.transition.in.opacity.duration.600="activeTab === 1">

                                <c:choose>
                                    <c:when test="${booking_history.size() == 0}">
                                        <p>No booking history</p>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach var="__history_details__" items="${booking_history}">
                                            <%
                                                BookingDetails _current_booking_details_ = (BookingDetails) pageContext.getAttribute("__history_details__");
                                                byte[] content = _current_booking_details_.getCourt().getImages().getImages().get(0).getBytes(1, (int) _current_booking_details_.getCourt().getImages().getImages().get(0).length());
                                                String base64Encoded = new String(Base64.encodeBase64(content), "UTF-8");
                                            %>
                                            <div class="card lg:card-side bg-base-100 shadow-xl mt-6 h-80">
                                                <figure><img class="h-full" src="data:image/png;base64,<%= base64Encoded%>"/></figure>
                                                <div class="card-body">
                                                    <h2 class="card-title"><c:out value="${__history_details__.getCourt().getName()}"/></h2>
                                                    <p class="card-desc"><c:out value="${__history_details__.getCourt().getState()} - ${__history_details__.getCourt().getCity()}"/></p>
                                                    <p>Total price: <fmt:formatNumber type="currency" value="${__history_details__.getBooking().getPriceInCents()}"/></p>
                                                    <p>Date: <c:out value="${__history_details__.getBooking().getDate()}"/></p>
                                                    <p>Time: <c:out value="${__history_details__.getBooking().getTime()}"/></p>
                                                    <p>Duration: <c:out value="${__history_details__.getBooking().getDuration()}"/></p>
                                                    <div class="card-actions justify-end">
                                                        <a href="./../BookingController?command=DELETE&id=${__history_details__.getBooking().getId()}" class="btn btn-primary">Cancel</a>
                                                    </div>
                                                </div>
                                            </div>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            </body>
