<%@ page import="model.User" %>
<%@ page session="true" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Account Management</title>
    <link rel="stylesheet" href="css/subpages/account.css">
</head>
<body>
    <header>
        <div class="logo">
            <img src="assets/images/iotbay_logo.png" alt="IoTBay">
        </div>
        <a href="index.jsp">Return to Main Page</a>
    </header>
    <h2>Account Management</h2>
    <form action="account.jsp" method="post">
        <label for="email">Email:</label><br>
        <input type="email" name="email" id="email" value="<%= user.getEmail() %>" readonly><br>
        <label for="name">Name:</label><br>
        <input type="text" name="name" id="name" value="<%= user.getName() %>"><br>
        <label for="password">Password:</label><br>
        <input type="password" name="password" id="password" value="<%= user.getPassword() %>"><br>
        <label for="gender">Gender:</label><br>
        <input type="radio" name="gender" value="male" <%= "male".equals(user.getGender()) ? "checked" : "" %>> Male<br>
        <input type="radio" name="gender" value="female" <%= "female".equals(user.getGender()) ? "checked" : "" %>> Female<br>
        <input type="radio" name="gender" value="other" <%= "other".equals(user.getGender()) ? "checked" : "" %>> Other<br>
        <label for="adressLine">Address Line:</label><br>
        <input type="text" name="adressLine" id="adressLine" value="<%= user.getAdressLine() %>"><br>
        <label for="additionalAdressInfo">Additional Address Info:</label><br>
        <input type="text" name="additionalAdressInfo" id="additionalAdressInfo" value="<%= user.getAdditionalAdressInfo() %>"><br>
        <label for="postalCode">Postal Code:</label><br>
        <input type="number" name="postalCode" id="postalCode" value="<%= user.getPostalCode() %>"><br>
        <label for="stateCode">State Code:</label><br>
        <input type="text" name="stateCode" id="stateCode" value="<%= user.getStateCode() %>"><br>
        <input type="submit" value="Save Changes">
    </form>
    <%
        if ("POST".equalsIgnoreCase(request.getMethod())) {
            user.setName(request.getParameter("name"));
            user.setPassword(request.getParameter("password"));
            user.setGender(request.getParameter("gender"));
            user.setAdressLine(request.getParameter("adressLine"));
            user.setAdditionalAdressInfo(request.getParameter("additionalAdressInfo"));
            user.setPostalCode(request.getParameter("postalCode") != null ? Integer.parseInt(request.getParameter("postalCode")) : null);
            user.setStateCode(request.getParameter("stateCode"));
            session.setAttribute("user", user);
            out.println("<p>Changes saved successfully!</p>");
        }
    %>
</body>
</html>