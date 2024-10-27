<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1" charset="UTF-8">
<title>ログイン画面</title>
<link rel="stylesheet" href="/MyCooking/css/doctor-reset.css">
<link rel="stylesheet" href="/MyCooking/css/share.css">
<link rel="stylesheet" href="/MyCooking/css/login.css">
</head>
<body>
	<div id="login-container">
		<div class="login">
			<h1>ログイン</h1>
		<%@ include file="message.jsp"%>
			<form action="/MyCooking/login-servlet" method="post">
				<p>
					<label for="name">メールアドレス</label><br><br>
					<input id="name" type="email" name="email" placeholder="sample@sample.com" required>
				</p>
				<p>
					<label for="password">パスワード</label><br><br>
					<input id="password" type="password" name="password" placeholder="任意のパスワード" required>
				</p>
				<p><button type="submit">ログイン</button></p>
			</form>
			<br><br>
			<p><a href="/MyCooking/view/new-account.jsp">新規会員登録</a></p><br><br>
		</div>
	</div>
</body>
</html>