<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="dto.Food"%>
<%@ page import="java.util.List"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1" charset="UTF-8">
<title>使用する野菜を選択</title>
<link rel="stylesheet" href="/MyCooking/css/doctor-reset.css">
<link rel="stylesheet" href="/MyCooking/css/share.css">
<link rel="stylesheet" href="/MyCooking/css/select-food.css">
</head>
<body>
	<%@ include file="header.jsp"%>
	<div id="contents">
<%
	List<List<Food>> refrigerator = (List<List<Food>>) session.getAttribute("refrigerator");
	String recipeKana = (String) request.getAttribute("recipeKana");
	if(refrigerator != null) {
%>
		<%@ include file="message.jsp"%>
		<h1>レシピで使用する材料と個数を入力してください</h1>
		<form action="/MyCooking/select-food-servlet" method="post">
	<%
		for(List<Food> foodList : refrigerator) {
			String foodGenre = foodList.get(0).getFoodGenre();
	%>
		<h2>○<%=foodGenre %></h2>
		<div id="content">
		<%
			for(Food food : foodList) {
				String foodNum = food.getFoodKana() + "Num";
				String imagePath = food.getImagePath();
				if(imagePath == null) {
					imagePath = "/image/food.png";
				}
		%>
			<div class="item">
				<input type="hidden" name="<%=food.getFoodId() %>" value="<%=food.getFoodId() %>">
				<p><%=food.getFoodName() %>　在庫数：<%=food.getQuantity() %></p>
				<img src="/MyCooking<%=imagePath %>">
				<input type="number" name="<%=foodNum %>" placeholder="個数" 
					value="0" min="0" max="<%=food.getQuantity() %>" step="1">
			</div>
	<%
			}
	%>
		</div>
	<%
		}
	%>
			<input type="hidden" name="recipeKana" value="<%=recipeKana %>">
			<p id="setbtn">
				<button type="button" onClick="back()">戻る</button>
				<button type="reset">リセット</button>
				<button type="submit">決定</button>
			</p>
		</form>
		<form action="recipe-show-servlet" method="post">
			<input type="hidden" name="recipeKana" value="<%= recipeKana%>">
			<p class="guide">＊材料を選択せずにレシピを表示する場合はこちら<br><br>
				<button type="submit">レシピ表示</button>
			</p>
			
		</form>
	
<%
	}else {
%>
		<h1>現在保管している食材がありません</h1>
		<p class="guide"><button type="button" onClick="back()">戻る</button><p><br><br>
		<form action="recipe-show-servlet" method="post">
			<input type="hidden" name="recipeKana" value="<%=recipeKana %>">
			<p class="guide">＊材料を選択せずにレシピを表示する場合はこちら<br><br>
				<button type="submit">レシピ表示</button>
			</p>
		</form>
		
<%
	}
%>
	</div>
	<script>
		function back() {
			location.pathname = '/MyCooking/view/search.jsp';
		}
	</script>
</body>
</html>