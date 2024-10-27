<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="dto.Recipe"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Collections"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1" charset="UTF-8">
<title>レシピ検索画面</title>
<link rel="stylesheet" href="/MyCooking/css/doctor-reset.css">
<link rel="stylesheet" href="/MyCooking/css/share.css">
<link rel="stylesheet" href="/MyCooking/css/search.css">
</head>
<body>
<% 
	List<Recipe> recipeList = (List<Recipe>) session.getAttribute("recipeAll");
	List<Recipe> favoriteList = (List<Recipe>) session.getAttribute("favoriteRecipe");
	List<Recipe> searchRecipe = (List<Recipe>) session.getAttribute("search");
	if(recipeList != null) {
%>
	<%@ include file="header.jsp"%>
	<div class="container">
		<aside>
		<%@ include file="menu.jsp"%>
		</aside>
	
		<article>
			<div id="search">
				<!--ひらがな入力のみ-->
				<form action="/MyCooking/search-servlet" method="post">
					<input id="search_input" type="text" name="keyword" pattern="^[ぁ-んー]+$"
					placeholder="おこのみやき（一部も可　例：やき）">
					<button type="submit">
						<p><img src="/MyCooking/image/search.png">
							検索
						</p>
					</button>
					<div id="tooltip">
						<p id="tooltip_message">検索するレシピはひらがなで入力</p>
					</div>
				</form>
			</div>
			
		<%@ include file="message.jsp"%>
<%
		if(errorMsg != null && errorMsg.equals("検索できませんでした")) {
%>
			<p><button type="button" onClick="addRecipe()">レシピを追加</button></p>
<%
		}
		if(searchRecipe != null) {
%>
			<br><br>
			<h2>検索したレシピ</h2>
			<div class="contents">
	<%
			for(int i = 0; i < searchRecipe.size(); i++) {
				Recipe recipe = searchRecipe.get(i);
				String recipeName = recipe.getRecipeName();
				String recipeKana = recipe.getRecipeKana();
				String recipeImage = recipe.getImagePath();
				if(recipeImage == null) {
					recipeImage = "/image/cooking.png";
				}
	%>
	 		
		 		<div class="item">
					<form action="/MyCooking/select-recipe-servlet" method="post">
						<input type="hidden" name="recipeKana" value="<%=recipeKana %>">
						<button class="image-button" type="submit">
							<img alt="<%=recipeName %>画像" src="/MyCooking<%=recipeImage%>"><br>
					<% 
						if(recipeName.length() < 13) {
					%>
							<span><%=recipeName %></span>		
					<% 
						}else {
					%>
							<span style="font-size:14px"><%=recipeName %></span>
					<%
						}
					%>
						</button>
					</form>
				</div>
	<%
			}
	%>
			</div>
<%
		}
		if(favoriteList != null) {
%>	
			<h2>お気に入りレシピ</h2>
			<div class="contents">
	<%
			for(int i = 0; i < favoriteList.size(); i++) {
				Recipe favoriteRecipe = favoriteList.get(i);
				String favoriteName = favoriteRecipe.getRecipeName();
				String favoriteKana = favoriteRecipe.getRecipeKana();
				String favoriteRecipeImage = favoriteRecipe.getImagePath();
				if(favoriteRecipeImage == null) {
					favoriteRecipeImage = "/image/cooking.png";
				}
	%>
	 		
		 		<div class="item">
					<form action="/MyCooking/select-recipe-servlet" method="post">
						<input type="hidden" name="recipeKana" value="<%=favoriteKana %>">
						<button class="image-button" type="submit">
							<img alt="<%=favoriteName %>画像" src="/MyCooking<%=favoriteRecipeImage%>"><br>
					<% 
						if(favoriteName.length() < 13) {
					%>
							<span><%=favoriteName %></span>		
					<% 
						}else {
					%>
							<span style="font-size:14px"><%=favoriteName %></span>
					<%
						}
					%>
						</button>
					</form>
				</div>
	<%	
			}
	%>
			</div>
<% 
		}
%>
			<br><br>
			<h2>おすすめのレシピ</h2>
			<div class="contents">
	<%
			//レシピをランダム表示
			ArrayList<Integer> list = new ArrayList<>();
			for(int i = 0; i < recipeList.size(); i++) {
				list.add(i);
			}
			Collections.shuffle(list);
			//0から9番目に格納してあるrecipeListのインデックス番号を取得
			//取得したインデックス番号のrecipeをrecipeListから取得
			for(int i = 0; i < 10; i++) {
				int random = list.get(i);
				Recipe recipe = recipeList.get(random);
				String recipeName = recipe.getRecipeName();
				String recipeKana = recipe.getRecipeKana();
				String recipeImage = recipe.getImagePath();
				if(recipeImage == null) {
					recipeImage = "/image/cooking.png";
				}
	%>
				<div class="item">
					<form action="/MyCooking/select-recipe-servlet" method="post">
						<input type="hidden" name="recipeKana" value="<%=recipeKana %>">
						<button class="image-button" type="submit">
							<img alt="<%=recipeName %>画像" src="/MyCooking<%=recipeImage %>"><br>
					<% 
						if(recipeName.length() < 13) {
					%>
							<span><%=recipeName %></span>		
					<% 
						}else {
					%>
							<span style="font-size:14px"><%=recipeName %></span>
					<%
						}
					%>
						</button>
					</form>
				</div>
		<%
			}
		%>
			</div>
		</article>
	</div>
<% 
	}else {
		request.setAttribute("errorMsg", "リソース情報を取得できませんでした");
		RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
		dispatcher.forward(request, response);
	}
%>
	<script>
		const search_input = document.getElementById("search_input");
		const tooltip = document.getElementById("tooltip");
		
		search_input.addEventListener("mouseover", () => {
			tooltip.style.display = 'block';
		},false);
  		search_input.addEventListener("mouseout",() => {
  	  		tooltip.style.display = 'none';
  	  	},false);
  		
		function addRecipe() {
			location.pathname = '/MyCooking/view/create-recipe.jsp';
		}
	</script>
</body>
</html>