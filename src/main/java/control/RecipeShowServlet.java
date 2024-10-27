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

import dto.Recipe;

/**
 * Servlet implementation class RecipeShowServlet
 */
@WebServlet("/recipe-show-servlet")
public class RecipeShowServlet extends HttpServlet {
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
		String url = "view/recipe-show.jsp";
		
		request.setCharacterEncoding("utf-8");
		HttpSession session = request.getSession();
		String recipeKana = request.getParameter("recipeKana");
		Recipe recipe = getRecipe(session,recipeKana);
		
		request.setAttribute("selectRecipe", recipe);
		
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
}
