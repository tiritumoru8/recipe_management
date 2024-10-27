package control;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Operation;

/**
 * Servlet implementation class loginServlet
 */
@WebServlet("/login-servlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String url = "view/search.jsp";
		
		request.setCharacterEncoding("utf-8");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		HttpSession session = request.getSession();
		
		boolean result = false;
		Operation op = new Operation();
		try {
			result = op.login(session, email, password);
		}catch(Exception e) {
			request.setAttribute("errorMsg", "ログインに失敗しました");
			url = "login";
		}
		if(result) {
			try {
				op.setResource(session);
				op.setFavoriteRecipe(session);
			}catch(Exception e) {
				request.setAttribute("errorMsg", "リソース情報取得時にエラーが発生しました");
				url = "login";
			}
		}else {
			request.setAttribute("errorMsg", "メールアドレス または パスワードに誤りがあります");
			url = "login";
		}
		//
		if(url.equals("login")) {
			RequestDispatcher dispatcher = request.getRequestDispatcher("view/login.jsp");
			dispatcher.forward(request, response);
		}else {
			response.sendRedirect(url);
		}
	}

}
