<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="dto.Account"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1" charset="UTF-8">
<title>マイアカウント</title>
<link rel="stylesheet" href="/MyCooking/css/doctor-reset.css">
<link rel="stylesheet" href="/MyCooking/css/share.css">
<link rel="stylesheet" href="/MyCooking/css/my-account.css">
</head>
<body>
<% 
	session.removeAttribute("search");
	Account account = (Account) session.getAttribute("account");
	if(account != null && account.getUserId() != 0) {
%>
	<%@ include file="header.jsp"%>
	<div class="container">
		<aside>
		<%@ include file="menu.jsp"%>
		</aside>
	
		<article>
			
			<div id="contents">
				<h1>マイアカウント</h1>
				<br>
				<%@ include file="message.jsp"%>
				<br>
				<div id="btn">
					<button type="button" onClick="accountEdit()">編集</button>
					<button type="button" onClick="accountDelete()">削除</button>
				</div>
				<div id="child">
					<h2>お名前</h2>
					<p><%=account.getUserName() %></p>
					<h2>メールアドレス</h2>
					<p><%=account.getEmail() %></p>
					<h2>お気に入りレシピ</h2>
					<p><%=account.getFavoriteCount() %>つ</p>
					<h2>マイ冷蔵庫</h2>
					<p>[食材の種類] <%=account.getKindOfFood() %>種類 ／ [食材の合計] <%=account.getFoodQuantity() %>個</p>
					<h2>マイレシピ</h2>
					<p style="margin-bottom:60px"><%=account.getMyRecipeCount() %>つ</p>
					
				</div>
			</div>
			<dialog id="conf" style="min_width:300px; height:200px; text-align:center;background-color:white;">
				<h2>アカウント情報を削除しますか？</h2>
				<br><br><br>
				<p>
					<button type="button" onClick="clickOk()" style="margin-right:50px">はい</button>
					<button type="button" onClick="clickNo()">キャンセル</button>
				</p>
			</dialog>
		</article>
	</div>
<% 
	}else {
		request.setAttribute("errorMsg", "セッションが切れました。再ログインをお願いします");
		String contextPath = request.getContextPath();
		RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
		dispatcher.forward(request, response);
	}
%>
	<script>
		var conf = document.getElementById('conf');

		function accountEdit(){
			location.pathname = '/MyCooking/view/new-account.jsp';
		}
		function accountDelete() {
			conf.showModal();
		}
		function clickOk() {
			location.pathname = '/MyCooking/delete-account-servlet';
		}
		function clickNo() {
			conf.close();
		}
	</script>
</body>
</html>