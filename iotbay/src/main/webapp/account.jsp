<%@ page import="model.users.*" %>
<%@ page session="true" %>
<%
    // Redirect to login page if user is not logged in
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
    <div class="container">
        <div class="main-container">
            <div class="centered-container">
                <form action="account.jsp" method="post">
                    <h2>Account Management</h2>
                    <p>Personal Information:</p>
                    <label for="name">Name:</label><br>
                    <input type="text" name="name" id="name" value="<%= user.getName() %>"><br>
                    <label for="email">Email:</label><br>
                    <input type="email" name="email" id="email" value="<%= user.getEmail() %>" readonly><br>
                    <label for="password">Password:</label><br>
                    <input type="password" name="password" id="password" value="<%= user.getPassword() %>"><br>
                    <p>Address Information:</p>
                    <!-- TODO: Add adress information fields with Adress.java class-->
                    <label for="adressLine">Address Line:</label><br>
                    <input type="text" name="adressLine" id="adressLine" value=""><br>
                    <label for="additionalAdressInfo">Additional Address Info:</label><br>
                    <input type="text" name="additionalAdressInfo" id="additionalAdressInfo" value=""><br>
                    <label for="postalCode">Postal Code:</label><br>
                    <input type="number" name="postalCode" id="postalCode" value=""><br>
                    <label for="stateCode">State Code:</label><br>
                    <input type="text" name="stateCode" id="stateCode" value=""><br>
                    <input type="submit" value="Save Changes">
                </form>
                <%
                    if ("POST".equalsIgnoreCase(request.getMethod())) {
                        user.setName(request.getParameter("name"));
                        user.setEmail(request.getParameter("email"));
                        user.setPassword(request.getParameter("password"));
                        // TODO: add adress saving
                        session.setAttribute("user", user);
                        out.println("<p>Changes saved successfully!</p>");
                    }
                %>
                <a href="logout.jsp">Logout</a>
            </div>
        </div>
    </div>
</body>
</html> 



