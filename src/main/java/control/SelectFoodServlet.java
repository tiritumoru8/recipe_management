package control;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dto.Food;
import dto.Recipe;
import model.Operation;

/**
 * Servlet implementation class SelectFoodServlet
 */
@WebServlet("/select-food-servlet")
public class SelectFoodServlet extends HttpServlet {
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
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String url = "view/search.jsp";
		
		request.setCharacterEncoding("utf-8");
		HttpSession session = request.getSession();
		
		String recipeKana = request.getParameter("recipeKana");
		Recipe recipe = getRecipe(session,recipeKana);
		request.setAttribute("selectRecipe", recipe);
		
		List<List<Food>> refrigerator = (List<List<Food>>) session.getAttribute("refrigerator");
		Map<Integer,String> map = getSelectFood(refrigerator,request);
		
		if(map.size() != 0) {
			try {
				Operation op = new Operation();
				boolean result = op.useFood(session,map,refrigerator);
				if(result) {
					url = "view/recipe-show.jsp";
				}
			}catch(Exception e) {
				request.setAttribute("errorMsg", "データベースを操作できませんでした");
			}
		}else {
			request.setAttribute("errorMsg", "使用する材料の個数を入力してください");
			url = "view/select-food.jsp";
		}
		RequestDispatcher dispatcher = request.getRequestDispatcher(url);
		dispatcher.forward(request, response);
	}
	
	public Recipe getRecipe(HttpSession session,String recipeKana) {
		Recipe selectRecipe = null;
		@SuppressWarnings("unchecked")
		List<Recipe> recipeAll = (List<Recipe>) session.getAttribute("recipeAll");
		for(Recipe recipe : recipeAll) {
			if(recipe.getRecipeKana().equals(recipeKana)) {
				selectRecipe = recipe;
			}
		}
		return selectRecipe;
	}
	public Map<Integer,String> getSelectFood(List<List<Food>> refrigerator,HttpServletRequest request) {
		Map<Integer,String> map = new HashMap<>();
		for(List<Food> foodList : refrigerator) {
			for(Food food : foodList) {
				String quantityStr = request.getParameter(food.getFoodKana() + "Num");
				if(! quantityStr.equals("0")) {
					String foodIdStr = request.getParameter(String.valueOf(food.getFoodId()));
					map.put(Integer.parseInt(foodIdStr), quantityStr);
				}
			}
		}
		return map;
	}
}
