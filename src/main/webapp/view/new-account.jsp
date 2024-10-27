<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="dto.Account"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1" charset="UTF-8">
<title>アカウント登録画面</title>
<link rel="stylesheet" type="text/css" href="/MyCooking/css/doctor-reset.css">
<link rel="stylesheet" type="text/css" href="/MyCooking/css/share.css">
<link rel="stylesheet" type="text/css" href="/MyCooking/css/new-account.css"
</head>
<body>
	<%@ include file="header.jsp"%>
<%
	Account account = (Account) session.getAttribute("account");
	//アカウント新規登録
	if(account == null) {
%>
	<div class="container">
		<div class="contents">
			<h1>アカウント登録</h1>
			<%@ include file="message.jsp"%>
			<form action="/MyCooking/edit-account-servlet" method="post">
				<label for="name">お名前</label><br>
				<input type="text" id="name" name="name" required><br>
				
				<label for="email">メールアドレス</label><br>
				<input type="email" id="email" name="email" required><br>
					
				<label for="password">パスワード</label><br>
				<input type="password" id="password" name="password" required><br>
				<input type="password" name="again" placeholder="パスワード再入力" required><br>
				<p class="btnset">
					<button type="reset">リセット</button>
					<button type="submit">登録</button>
				</p>
			</form>
		</div>
	</div>
<% 
	//アカウント編集
	}else if(account != null) {
		String userName = account.getUserName();
		String email = account.getEmail();
%>
	<div class="container">
		<aside>
			<%@ include file="menu.jsp"%>
		</aside>
		<article>
			<div class="contents">
			<h1>アカウント登録</h1>
			<%@ include file="message.jsp"%>
			<form action="/MyCooking/edit-account-servlet" method="post">
				<label for="name">お名前</label><br>
				<input type="text" id="name" name="name" placeholder="<%=userName %>"><br>
				
				<label for="email">メールアドレス</label><br>
				<input type="email" id="email" name="email" placeholder="<%=email %>"><br>
					
				<label for="password">パスワード</label><br>
				<input type="password" id="password" name="password"><br>
				<input type="password" name="again" placeholder="パスワード再入力"><br>
				<p class="btnset">
					<button type="reset">リセット</button>
					<button type="submit">登録</button>
				</p>
			</form>
		</div>
		</article>
		
	</div>
<% 
	}else {
		request.setAttribute("errorMsg", "エラーが発生しました。再ログインお願いします");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/MyCooking/view/login.jsp");
		dispatcher.forward(request, response);
	}
%>
</body>
</html>