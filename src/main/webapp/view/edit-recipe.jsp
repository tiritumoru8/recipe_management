<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="dto.Recipe"%>
<%@ page import="java.util.List"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1" charset="UTF-8">
<title>マイレシピ編集画面</title>
<link rel="stylesheet" href="/MyCooking/css/doctor-reset.css">
<link rel="stylesheet" href="/MyCooking/css/share.css">
<link rel="stylesheet" href="/MyCooking/css/edit-recipe.css">
</head>
<body>
<%@ include file="header.jsp"%>
	<div class="container">
		<aside>
		<%@ include file="menu.jsp"%>
		</aside>
		<article>
			<h1>マイレシピ</h1>
		<%@ include file="message.jsp"%>
			<p id="btn-set">
				<button onClick="back()">戻る</button>
			</p>
<%
	List<List<Recipe>> myRecipeList = (List<List<Recipe>>) session.getAttribute("myRecipe");
	if(myRecipeList != null) {
		for(int a = 0; a < myRecipeList.size(); a++) {
			List<Recipe> myRecipe = myRecipeList.get(a);
			String recipeGenre = myRecipe.get(0).getRecipeGenre();
%>
			<div class="content">
				<h2>○<%=recipeGenre %></h2>	
				<div class="items">
			<%
				for(int b = 0; b < myRecipe.size(); b++) {
					Recipe recipe = myRecipe.get(b);
					String recipeName = recipe.getRecipeName();
					String recipeKana = recipe.getRecipeKana();
			%>
					<div class="item">
						<form action="/MyCooking/set-recipe-servlet" method="post">
							<input type="hidden" name="indexA" value="<%=a %>">
							<input type="hidden" name="indexB" value="<%=b %>">
							<p><%=recipeName %></p>
							<button>編集</button>
						</form>
					</div>
			<%
				}
			%>
				</div>	
			</div>
<%
		}
	}else {
		request.setAttribute("errorMsg", "セッションが切れました。再ログインをお願いします");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/MyCooking/view/login.jsp");
		dispatcher.forward(request, response);
	}
%>
		</article>
	</div>
	<script>
		function back() {
			location.pathname = 'MyCooking/view/my-recipe.jsp';
		}
	</script>
</body>
</html>