package control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dto.Food;

/**
 * Servlet implementation class CheckFoodServlet
 */
@WebServlet("/check-food-servlet")
public class CheckFoodServlet extends HttpServlet {
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
		String url = "add-food-servlet";
		request.setCharacterEncoding("utf-8");
		String foodKana = request.getParameter("foodKana");
		String quantityStr = request.getParameter("quantity");
		List<Integer> foodIdList = new ArrayList<>();
		
		HttpSession session = request.getSession();
		@SuppressWarnings("unchecked")
		List<Food> foodList = (List<Food>) session.getAttribute("food");
		List<Food> selectFood = new ArrayList<>();
		//入力した食材がDBに存在するか確認
		boolean exist = false;
		for(Food food : foodList) {
			if(food.getFoodKana().contains(foodKana)) {
				exist = true;
				int foodId = food.getFoodId();
				foodIdList.add(foodId);
				selectFood.add(food);
			}
		}
		if(exist) {
			if(foodIdList.size() == 1) {
				request.setAttribute("foodId", foodIdList.get(0));
				request.setAttribute("foodKana", foodKana);
				request.setAttribute("quantityStr", quantityStr);
			}else {
				request.setAttribute("selectFood", selectFood);
				request.setAttribute("message", "該当の食材が複数見つかりました。登録する食材を選択し、入力してください");
				url = "view/add-food.jsp";
			}
		}else {
			request.setAttribute("errorMsg", "該当する食材がありません");
			url="view/add-food.jsp";
		}
		
		RequestDispatcher dispatcher = request.getRequestDispatcher(url);
		dispatcher.forward(request, response);
	}

}
