<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="dto.Recipe"%>
<%@ page import="java.util.List"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1" charset="UTF-8">
<title>マイレシピ表示画面</title>
<link rel="stylesheet" href="/MyCooking/css/doctor-reset.css">
<link rel="stylesheet" href="/MyCooking/css/share.css">
<link rel="stylesheet" href="/MyCooking/css/my-recipe.css">
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
			
<%
	List<List<Recipe>> myRecipeList = (List<List<Recipe>>) session.getAttribute("myRecipe");
	if(myRecipeList != null) {
%>
		<div class="contents">
			<p id="btn-set">
				<button onClick="deleteRecipe()">削除</button>
				<button onClick="editRecipe()">編集</button>
			</p>
<%
		for(int a = 0; a < myRecipeList.size(); a++) {
			List<Recipe> myRecipe = myRecipeList.get(a);
			String recipeGenre = myRecipe.get(0).getRecipeGenre();
%>
			<div class="content">
				<h2 class="genre">○<%=recipeGenre %></h2>	
				<div class="item">
			<%
				for(int b = 0; b < myRecipe.size(); b++) {
					Recipe recipe = myRecipe.get(b);
					String recipeName = recipe.getRecipeName();
					String recipeKana = recipe.getRecipeKana();
			%>
					<form action="/MyCooking/select-recipe-servlet" method="post">
						<input type="hidden" name="recipeKana" value="<%=recipeKana %>">
						<button><%=recipeName %></button>
					</form>
			<%
				}
			%>
				</div>	
			</div>
<%
		}
%>
		</div>
<%
	}else {
%>
			<p>登録されているレシピはありません</p><br><br>
			<button type="button" onClick="addFood()">追加</button>
<%
	}
%>
		</article>
	</div>
	<script>
		function addFood() {
			location.pathname = '/MyCooking/view/create-recipe.jsp';
		}
		function editRecipe() {
			location.pathname = '/MyCooking/view/edit-recipe.jsp';
		}
		function deleteRecipe() {
			location.pathname = '/MyCooking/view/delete-recipe.jsp';
		}
	</script>
</body>
</html>