package control;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dto.Account;
import dto.Food;
import model.Operation;

/**
 * Servlet implementation class AddFoodServlet
 */
@WebServlet("/add-food-servlet")
public class AddFoodServlet extends HttpServlet {
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
		String foodKana = request.getParameter("foodKana");
		String quantityStr = request.getParameter("quantity");
		int foodId = 0;
		
		HttpSession session = request.getSession();
		Account account = (Account) session.getAttribute("account");
		if(account != null) {
			@SuppressWarnings("unchecked")
			List<Food> foodList = (List<Food>) session.getAttribute("food");
			//入力した食材がDBに存在するか確認
			boolean exist = false;
			for(Food food : foodList) {
				if(food.getFoodKana().equals(foodKana)) {
					exist = true;
					foodId = food.getFoodId();
				}
			}
			
			if(exist) {
				try {
					int quantity = Integer.parseInt(quantityStr);
					Operation op = new Operation();
					int count = op.addFood(session,foodId, foodKana, quantity);
					if(count != 0) {
						request.setAttribute("message", "登録しました");
					}else {
						throw new Exception();
					}
				}catch(Exception e) {
					request.setAttribute("errorMsg", "登録できませんでした");
					url = "view/add-food.jsp";
				}
			}else {
				request.setAttribute("errorMsg", "該当する食材がありません");
				url="view/add-food.jsp";
			}
		}else {
			request.setAttribute("errorMsg", "セッションが切れました。再ログインをお願いします");
			url = "view/login.jsp";
		}
		
		RequestDispatcher dispatcher = request.getRequestDispatcher(url);
		dispatcher.forward(request, response);
	}

}
