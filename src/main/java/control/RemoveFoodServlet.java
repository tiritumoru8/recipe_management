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
 * Servlet implementation class RemoveFoodServlet
 */
@WebServlet("/remove-food-servlet")
public class RemoveFoodServlet extends HttpServlet {
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
		String url = "view/my-refrigerator.jsp";
		request.setCharacterEncoding("utf-8");
		String foodIdStr = request.getParameter("foodId");
		String idxAStr = request.getParameter("idxA");
		String idxBStr = request.getParameter("idxB");
		HttpSession session = request.getSession();
		
		if(foodIdStr != null) {
			try {
				int foodId = Integer.parseInt(foodIdStr);
				int idxA = Integer.parseInt(idxAStr);
				int idxB = Integer.parseInt(idxBStr);
				Operation op = new Operation();
				boolean result = op.removeFood(session, foodId,idxA,idxB);
				if(result) {
					request.setAttribute("message", "削除しました");
				}else {
					throw new Exception();
				}
			}catch(Exception e) {
				request.setAttribute("errorMsg", "削除できませんでした");
			}
		}else {
			request.setAttribute("errorMsg", "削除できませんでした");
		}
		
		RequestDispatcher dispatcher = request.getRequestDispatcher(url);
		dispatcher.forward(request, response);
	}

}
