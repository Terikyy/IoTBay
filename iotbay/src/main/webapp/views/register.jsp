<html>
<body>
<h2>Registration</h2>
<form action="index.jsp" method="post">
    <label for="email">Email:</label><br>
    <input type="email" name="email" id="email"><br>
    <label for="name">Name:</label><br>
    <input type="text" name="name" id="name"><br>
    <label for="password">Password:</label><br>
    <input type="password" name="password" id="password"><br>
    <input type="radio" name="gender" value="male"><label for="male">Male</label><br>
    <input type="radio" name="gender" value="female"><label for="male">Female</label><br>
    <input type="radio" name="gender" value="other"><label for="male">Other</label><br>
    <label for="favcol">Favourite Color:</label><br>
    <input type="color" name="favcol" id="favcol"><br>
    <label for="tos">Agree to TOS:</label><input type="checkbox" name="tos" id="tos"><br>
    <input type="submit" value="Submit">
</form>
</body>
</html>