package control;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dto.Recipe;
import model.Operation;

/**
 * Servlet implementation class FavoriteRecipeServlet
 */
@WebServlet("/favorite-recipe-servlet")
public class FavoriteRecipeServlet extends HttpServlet {
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
		String recipeIdStr = request.getParameter("recipeId");
		String favoriteStr = request.getParameter("favorite");
		HttpSession session = request.getSession();
		
		try {
			Operation op = new Operation();
			Recipe recipe = op.favorite(session,Integer.parseInt(recipeIdStr),Integer.parseInt(favoriteStr));
			if(recipe != null) {
				request.setAttribute("selectRecipe", recipe);
			}else {
				request.setAttribute("errorMsg","処理途中でエラーが発生しました");
				url = "view/search.jsp";
			}
		}catch(Exception e) {
			request.setAttribute("errorMsg", "お気に入り登録ができませんでした");
		}
		
		RequestDispatcher dispatcher = request.getRequestDispatcher(url);
		dispatcher.forward(request, response);
	}

}
