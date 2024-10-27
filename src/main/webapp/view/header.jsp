<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="/MyCooking/css/doctor-reset.css">
<link rel="stylesheet" href="/MyCooking/css/share.css">
</head>
<body>
	 <header class="header">
		<div id="header_inner">
			<div id="icon">
				<p>Cooking</p>
			</div>
			<nav class="header_nav nav" id="js-nav">
				<ul class="nav_items nav-items">
					<li class="nav-items_item"><a href="/MyCooking/view/search.jsp">検索フォーム</a></li>
					<li class="nav-items_item"><a href="/MyCooking/view/recipe-list.jsp">全レシピ一覧</a></li>
					<li class="nav-items_item"><a href="/MyCooking/view/my-refrigerator.jsp">マイ冷蔵庫</a></li>
					<li class="nav-items_item"><a href="/MyCooking/view/my-recipe.jsp">マイレシピ</a></li>
					<li class="nav-items_item"><a href="/MyCooking/view/create-recipe.jsp">レシピ追加</a></li>
					<li class="nav-items_item"><a href="/MyCooking/view/my-account.jsp">マイアカウント</a></li>
					<li class="nav-items_item"><a href="/MyCooking/logout-servlet">ログアウト</a></li>
				</ul>
			</nav>
			<div class="header_hamburger hamburger" id="js-hamburger">
	        	<span></span>
	        	<span></span>
	 		    <span></span>
		    </div>
		</div>
	</header>
	<script>
		const ham = document.querySelector('#js-hamburger');
		const nav = document.querySelector('#js-nav');
	
		ham.addEventListener('click', function () {
			ham.classList.toggle('active');
			nav.classList.toggle('active');
		});
	</script>
</body>
</html>