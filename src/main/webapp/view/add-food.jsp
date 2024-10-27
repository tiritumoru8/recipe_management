<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="dto.Food"%>
<%@ page import="java.util.List"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1" charset="UTF-8">
<title>マイ冷蔵庫に食材を追加</title>
<link rel="stylesheet" href="/MyCooking/css/doctor-reset.css">
<link rel="stylesheet" href="/MyCooking/css/share.css">
<link rel="stylesheet" href="/MyCooking/css/add-food.css">
</head>
<body>
	<%@ include file="header.jsp"%>
	<div class="container">
		<aside>
		<%@ include file="menu.jsp"%>
		</aside>
	
		<article>
		
			<h1>材料を選択</h1>
	<%
		List<Food> selectFood = (List<Food>) request.getAttribute("selectFood");
		if(selectFood == null) {
	%>
			<div id="content">
				<form id="food" action="/MyCooking/check-food-servlet" method="post">
					<label for="foodKana">追加する材料</label><br><br>
					<input id="foodKana" name="foodKana" type="text" placeholder="ひらがなで入力してください"
						pattern="^[ぁ-んー]+$"><br>
					<label for="quantity">個数</label><br><br>
					<input id="quantity" name="quantity" type="text" placeholder="数値を入力してください"
						pattern="[0-9 ０-９]"><br>
				</form>
			</div>
			<button form="food" type="submit">追加</button>
	<%
		}else {
	%>
			<div id="content">
				<form id="food" action="/MyCooking/add-food-servlet" method="post">
					<label for="foodKana">追加する材料</label><br><br>
					<select name="foodKana" id="select">
						<option disabled selected>--食材を選択してください--</option>
				<% 
					for(Food food : selectFood) {
				%>
						<option value="<%=food.getFoodKana() %>"><%=food.getFoodName() %></option>
				<%
					}
				%>
					</select><br>
					<label for="quantity">個数</label><br><br>
					<input id="quantity" name="quantity" type="text" placeholder="数値を入力してください"
						pattern="[0-9]"><br>
				</form>
			</div>
			<button form="food" type="submit">追加</button>
	<%
		}
	%>
		<%@ include file="message.jsp"%>
	<%
		if(errorMsg != null) {
			if(errorMsg.equals("該当する食材がありません")) {
	%>
		<button type="button" onClick="newFood()">食材を新規で登録</button>
	<%
			}
		}
	%>
		</article>
	</div>
	<script>
		function newFood() {
			location.pathname = '/MyCooking/view/new-food.jsp';
		}
	</script>
</body>
</html>