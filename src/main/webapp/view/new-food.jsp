<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1" charset="UTF-8">
<title>食材の新規登録画面</title>
<link rel="stylesheet" href="/MyCooking/css/doctor-reset.css">
<link rel="stylesheet" href="/MyCooking/css/share.css">
<link rel="stylesheet" href="/MyCooking/css/new-food.css">
</head>
<body>
<%
	List<String> foodGenre = (List<String>) session.getAttribute("foodGenre");
	List<String> unitList = (List<String>) session.getAttribute("unit");
	if(foodGenre != null && unitList != null) {
%>
<%@ include file="header.jsp"%>
	<div class="container">
		<aside>
		<%@ include file="menu.jsp"%>
		</aside>
		
		<article>
			<h1>食材を登録</h1>
			<div id="content">
				<form id="newFood" action="/MyCooking/new-food-servlet" method="post">
					<input type="text" name="foodName" placeholder="食材の名前を入力" required><br>
					<input type="text" name="foodKana" placeholder="食材名のふりがなを入力（ひらがな）"
						pattern="^[ぁ-んー]+$" required><br>
					<select name="foodGenre" required>
						<option selected disabled>--食材の分類名を選択--</option>
			<%
				for(String genre : foodGenre) {
			%>
						<option value="<%=genre %>"><%=genre %></option>
			<%
				}
			%>
					</select><br>
					<select name="unit" required>
						<option selected disabled>--食材の単位を選択--</option>
			<%
				for(String unit : unitList) {
			%>
						<option value="<%=unit %>"><%=unit %></option>
			<%
				}
			%>
					</select><br>
				</form>
			</div>
			<p id="btn-set">
				<button type="reset" form="newFood">リセット</button>
				<button type="submit" form="newFood">登録</button>
			</p>
		</article>
	</div>
<%
	}else {
		request.setAttribute("errorMsg", "セッションが切れました。再ログインをお願いします");
		RequestDispatcher dispatcher = request.getRequestDispatcher("MyCooking/view/login.jsp");
		dispatcher.forward(request, response);
	}
%>
</body>
</html>