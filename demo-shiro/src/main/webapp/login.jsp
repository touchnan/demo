<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<body>

	<form method="POST">
		<input name="username" /> <input name="password" /> <input
			type="checkbox" name="rememberMe" /> <input type="submit">
			<c:forEach var="a" items="1 2 5 4">
			
			 <c:out value="${a}"/>
			</c:forEach>
	</form>
</body>
</html>