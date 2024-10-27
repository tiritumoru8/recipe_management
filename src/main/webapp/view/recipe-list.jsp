<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="dto.Recipe"%>
<%@ page import="java.util.List"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1" charset="UTF-8">
<title>全レシピ表示画面</title>
<link rel="stylesheet" href="/MyCooking/css/doctor-reset.css">
<link rel="stylesheet" href="/MyCooking/css/share.css">
<link rel="stylesheet" href="/MyCooking/css/recipe-list.css">
</head>
<body>
<% 
	session.removeAttribute("search");
	List<List<Recipe>> recipeAll = (List<List<Recipe>>) session.getAttribute("recipe");
	
	if(recipeAll != null) {
%>
	<%@ include file="header.jsp"%>
	<div class="container">
		<aside>
		<%@ include file="menu.jsp"%>
		</aside>
	
		<article>
			<h1>全レシピ一覧</h1>
<% 
		for(int a = 0; a < recipeAll.size(); a++) {
			List<Recipe> recipeList = recipeAll.get(a);
			String recipeGenre = recipeList.get(0).getRecipeGenre();
%>
			<div class="content">
				<h2>○<%=recipeGenre %></h2>	
				<div class="recipelist">
		<%
			for(int b = 0; b < recipeList.size(); b++) {
				Recipe recipe = recipeList.get(b);
				String recipeName = recipe.getRecipeName();
				String recipeKana = recipe.getRecipeKana();
		%>
				<form action="/MyCooking/select-recipe-servlet" method="post">
					<input type="hidden" name="recipeKana" value="<%=recipeKana %>">
					<button type="submit"><%=recipeName %></button>
				</form>			
		<%
			}
		%>
				</div>	
			</div>
<%
		}
%>
		</article>
	</div>
<%
	}else {
		request.setAttribute("errorMsg", "セッションが切れました。再ログインをお願いします");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/MyCooking/view/login.jsp");
		dispatcher.forward(request,response);
	}
%>
</body>
</html>