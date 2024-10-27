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
 * Servlet implementation class DeleteAccountServlet
 */
@WebServlet("/delete-account-servlet")
public class DeleteAccountServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String url = "view/login.jsp";
		HttpSession session = request.getSession();
		try {
			Operation op = new Operation();
			boolean result = op.deleteAccount(session);
			if(result) {
				request.setAttribute("message", "アカウント情報を削除しました");
			}else {
				throw new Exception();
			}
		}catch(Exception e) {
			request.setAttribute("errorMsg", "アカウントを削除できませんでした");
			url = "error";
		}
		//転移先
		if(url.equals("error")) {
			RequestDispatcher dispatcher = request.getRequestDispatcher("view/my-account.jsp");
			dispatcher.forward(request, response);
		}
		response.sendRedirect(url);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
