<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="dto.Recipe"%>
<%@ page import="java.util.List"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1" charset="UTF-8">
<title>レシピ新規登録画面</title>
<link rel="stylesheet" href="/MyCooking/css/doctor-reset.css">
<link rel="stylesheet" href="/MyCooking/css/share.css">
<link rel="stylesheet" href="/MyCooking/css/create-recipe.css">
</head>
<body>
<%
	session.removeAttribute("search");
	Recipe selectRecipe = (Recipe) request.getAttribute("selectRecipe");
	List<String> cookingGenre = (List<String>) session.getAttribute("cookingGenre");
	List<Recipe> recipeAll = (List<Recipe>) session.getAttribute("recipeAll");
	if(cookingGenre != null) {
%>
	<%@ include file="header.jsp"%>
	<div class="container">
		<aside>
		<%@ include file="menu.jsp"%>
		</aside>
		<article>
			<h1>マイレシピ作成</h1>
			<div id="content">
				
		<%
			if(selectRecipe != null) {
		%>
				<form action="/MyCooking/edit-recipe-servlet" method="post" enctype="multipart/form-data">
					<input type="hidden" value="<%=selectRecipe.getRecipeId() %>" name="recipeId">
					<input type="text" name="recipeName" value="<%=selectRecipe.getRecipeName() %>" required><br>
					<input type="text" name="recipeKana" value="<%=selectRecipe.getRecipeKana() %>"
						pattern="^[ぁ-んー]+$" required><br>
					<select name="cookingGenre" required>
						<option disabled>--料理の分類名を選択--</option>
			<% 
				for(String genre : cookingGenre) {
					if(genre.equals(selectRecipe.getRecipeGenre())) {
			%>
						<option value="<%=genre %>" selected><%=genre %></option>
			<% 
					}else {
			%>
						<option value="<%=genre %>"><%=genre %></option>
			<%
					}
				}
			%>
					</select><br>
					<input type="file" name="imageFile">
					<textarea name="foodList" required><%=selectRecipe.getFoodList() %></textarea><br>
					<textarea name="process" required><%=selectRecipe.getProcess() %></textarea><br>
					<p id="btn-set">
						<button type="reset">リセット</button>
						<button type="submit">登録</button>
					</p>
				</form>
		<%
			}else {
				Recipe recipe = recipeAll.get(0);
		%>	
				<form action="/MyCooking/create-recipe-servlet" method="post" enctype="multipart/form-data">
					<input type="text" name="recipeName" placeholder="料理名入力" required><br>
					<input type="text" name="recipeKana" placeholder="料理名のふりがなを入力（ひらがな）"
						pattern="^[ぁ-んー]+$" required><br>
					<select name="cookingGenre" required>
						<option selected disabled>--料理の分類名を選択--</option>
			<% 
				for(String genre : cookingGenre) {
			%>
						<option value="<%=genre %>"><%=genre %></option>
			<%
				}
			%>
					</select><br>
					<input type="file" name="imageFile">
					<textarea name="foodList" placeholder="(例)<%=recipe.getFoodList() %>" required></textarea><br>
					<textarea name="process" placeholder="(例)<%=recipe.getProcess() %>" required></textarea><br>
					<p id="btn-set">
						<button type="reset">リセット</button>
						<button type="submit">登録</button>
					</p>
				</form>
		<%
			}
		%>
			</div>
		</article>
	</div>
<%
	}else {
		request.setAttribute("errorMsg", "セッションが切れました。再ログインをお願いします");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/MyCooking/view/login.jsp");
		dispatcher.forward(request, response);
	}
%>
</body>
</html>