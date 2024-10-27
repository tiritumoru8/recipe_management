<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="dto.Recipe"%>
<%@ page import="java.util.List"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1" charset="UTF-8">
<title>レシピ表示画面</title>
<link rel="stylesheet" href="/MyCooking/css/doctor-reset.css">
<link rel="stylesheet" href="/MyCooking/css/share.css">
<link rel="stylesheet" href="/MyCooking/css/recipe-show.css">
</head>
<body>
	<%@ include file="header.jsp"%>
	<div class="container">
		<aside>
		<%@ include file="menu.jsp"%>
		</aside>
<% 
	Recipe recipe = (Recipe) request.getAttribute("selectRecipe");
	if(recipe != null) {
		int recipeId = recipe.getRecipeId();
		String recipeName = recipe.getRecipeName();
		String recipeGenre = recipe.getRecipeGenre();
		String imagePath = recipe.getImagePath();
		if(imagePath == null) {
			imagePath = "/image/cooking.png";
		}
		String foodList = recipe.getFoodList();
		String process = recipe.getProcess();
		
		int favoriteBtn = 0;
		String fBImage = "/MyCooking/image/hosi_off.png";
		String toolTipMessage = "お気に入り登録をする";
		
		List<Recipe> favoriteList = (List<Recipe>) session.getAttribute("favoriteRecipe");
		if(favoriteList != null) {
			for(int i = 0; i < favoriteList.size(); i++) {
				Recipe favorite = favoriteList.get(i);
				if(favorite.getRecipeId() == recipeId) {
					favoriteBtn = 1;
					fBImage = "/MyCooking/image/hosi_on.png";
					toolTipMessage = "お気に入り登録を外す";
				}
			}
		}
%>
		<article>
			<div class="contents">
			<%@ include file="message.jsp"%>
				<p style="text-align:end"><%=recipeGenre %></p><br>
				<form action="/MyCooking/favorite-recipe-servlet" method="post">
					<input type="hidden" name="recipeId" value="<%=recipeId %>">
					<input type="hidden" name="favorite" value="<%=favoriteBtn %>">
					<p id="btnContainer"><button id="hosiBtn" type="submit"><img id="hosi" src="<%=fBImage %>"></button></p>
				</form>
				<div id="tooltip">
					<p id="tooltip_message"><%=toolTipMessage %></p>
				</div>
				<h2><%=recipeName %></h2>
				<div id="image">
					<img alt="<%=recipeName %>画像" src="/MyCooking<%=imagePath %>">
				</div>
				<p class="process">●材料<br><%=foodList %></p>
				<p class="process">●手順<br><%=process %></p>
			</div>
		</article>
	</div>
<%
	}else {
		request.setAttribute("errorMsg", "レシピを表示できませんでした");
		RequestDispatcher dispatcher = request.getRequestDispatcher("search.jsp");
		dispatcher.forward(request,response);
	}
%>
	<script>
		const container = document.getElementById("btnContainer");
		const tooltip = document.getElementById("tooltip");
		
		container.addEventListener("mouseover", () => {
			tooltip.style.display = 'block';
		},false);
		container.addEventListener("mouseout",() => {
	  		tooltip.style.display = 'none';
	  	},false);
	</script>
</body>
</html>