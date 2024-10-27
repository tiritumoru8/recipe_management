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
 * Servlet implementation class NewFoodServlet
 */
@WebServlet("/new-food-servlet")
public class NewFoodServlet extends HttpServlet {
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
		String url = "view/add-food.jsp";
		
		request.setCharacterEncoding("utf-8");
		String foodName = request.getParameter("foodName");
		String foodKana = request.getParameter("foodKana");
		String foodGenre = request.getParameter("foodGenre");
		String unit = request.getParameter("unit");
		HttpSession session = request.getSession();
		
		try {
			Operation op = new Operation();
			int count = op.newFood(session,foodName, foodKana, foodGenre, unit);
			if(count != 0) {
				request.setAttribute("message", "食材を登録しました");
			}else {
				request.setAttribute("errorMsg", "データベースにアクセスできませんでした。再ログインをお願いします");
				url = "view/login.jsp";
			}
		}catch(Exception e) {
			request.setAttribute("errorMsg", "登録できませんでした");
		}
		
		RequestDispatcher dispatcher = request.getRequestDispatcher(url);
		dispatcher.forward(request, response);
	}

}
