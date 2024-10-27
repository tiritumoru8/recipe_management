<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="dto.Food"%>
<%@ page import="java.util.List"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1" charset="UTF-8">
<title>マイ冷蔵庫</title>
<link rel="stylesheet" href="/MyCooking/css/doctor-reset.css">
<link rel="stylesheet" href="/MyCooking/css/share.css">
<link rel="stylesheet" href="/MyCooking/css/my-refrigerator.css">
</head>
<body>
	<%@ include file="header.jsp"%>
	<div class="container">
		<aside>
		<%@ include file="menu.jsp"%>
		</aside>
		<article>
			<h1>マイ冷蔵庫</h1>
		<%@ include file="message.jsp"%>
<%
	session.removeAttribute("search");
	List<List<Food>> refrigeratorList = (List<List<Food>>) session.getAttribute("refrigerator");
	if(refrigeratorList != null && refrigeratorList.size() != 0) {
%>
			<button id="add-btn" type="button" onClick="addFood()">追加</button>
	<%
		for(int a = 0; a < refrigeratorList.size(); a++) {
			List<Food> refrigerator = refrigeratorList.get(a);
			String foodGenre = refrigerator.get(0).getFoodGenre();
	%>
			<h2 style="text-align:left;">○<%=foodGenre %></h2>
			
			<div id="items">
		<% 
			for(int b = 0; b < refrigerator.size(); b++) {
				Food food = refrigerator.get(b);
				String foodName = food.getFoodName();
				int foodId = food.getFoodId();
				String imagePath = food.getImagePath();
				if(imagePath == null) {
					imagePath = "/image/food.png";
				}
				int quantity = food.getQuantity();
				String unit = food.getUnit();
				int foodNameSize = 14;
				if(foodName.length() > 4) {
					foodNameSize = 12;
				}
				String id = "remove" + a + b;
		%>
				<div class="item">
					<div style="height:80%;">
						<form id="<%=id %>" action="/MyCooking/remove-food-servlet" method="post">
							<input type="hidden" value="<%=foodId %>" name="foodId">
							<input type="hidden" value="<%=a %>" name="idxA">
							<input type="hidden" value="<%=b %>" name="idxB">
							<img src="/MyCooking/<%=imagePath %>" alt="<%=foodName %>画像"><br><br>
							<span style="font-size:<%=foodNameSize %>px;"><%=foodName %></span><br>
						</form>
					</div><br>
					<div style="height:20%;">
						<p><%=quantity %> <%=unit %></p><br>
						<button type="submit" form="<%=id %>" style="width:100%;">削除</button>
					</div>
				</div>
		<% 
			}
		%>
			</div>
<%
		}
	}else {
%>
			<p>保存されている食材はありません</p><br><br>
			<button type="button" onClick="addFood()">追加</button>
<%
	}
%>
		</article>
	</div>
	<script>
		function addFood() {
			location.pathname = '/MyCooking/view/add-food.jsp';
		}
	</script>
</body>
</html>