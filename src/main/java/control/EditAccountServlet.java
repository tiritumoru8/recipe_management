package control;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dto.Account;
import model.Operation;
import model.PasswordHasher;

/**
 * Servlet implementation class EditAccountServlet
 */
@WebServlet("/edit-account-servlet")
public class EditAccountServlet extends HttpServlet {
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
		String url = "/view/my-account.jsp";	
		HttpSession session = request.getSession();
		//パラメータ取得
		request.setCharacterEncoding("utf-8");
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String again = request.getParameter("again");
		//全項目入力なし
		if(name == null && email == null && password == null && again == null) {
			request.setAttribute("errorMsg", "入力されていません");
			url = "/view/new-account.jsp";
		//パスワード入力の値を比較（null比較含む）
		} else if(Objects.equals(password, again)) {
			try {
				String hashPass = PasswordHasher.hashPassword(password);
				Account account = new Account(name, email, hashPass);
				Operation op = new Operation();
				int count = op.reqistration(session, account);
				//リソース情報をセッションに格納
				op.setResource(session);
				op.setFavoriteRecipe(session);
				if(count > 0) {
					request.setAttribute("message", "登録しました");
				}else {
					throw new Exception();
					
				}
			}catch(Exception e) {
				request.setAttribute("errorMsg", "登録できませんでした");
				url = "/view/new-account.jsp";
			}
		}else {
			request.setAttribute("errorMsg", "パスワードが一致しません。再度入力してください");
			url = "/view/new-account.jsp";
		}
		//転送
		RequestDispatcher dispatcher = request.getRequestDispatcher(url);
		dispatcher.forward(request, response);
	}
}
