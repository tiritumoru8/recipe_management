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
 * Servlet implementation class SetRecipeServlet
 */
@WebServlet("/set-recipe-servlet")
public class SetRecipeServlet extends HttpServlet {
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
		request.setCharacterEncoding("utf-8");
		String indexAStr = request.getParameter("indexA");
		String indexBStr = request.getParameter("indexB");
		
		int indexA = Integer.parseInt(indexAStr);
		int indexB = Integer.parseInt(indexBStr);
		
		HttpSession session = request.getSession();
		@SuppressWarnings("unchecked")
		List<List<Recipe>> myRecipe = (List<List<Recipe>>) session.getAttribute("myRecipe");
		Recipe recipe = myRecipe.get(indexA).get(indexB);
		request.setAttribute("selectRecipe", recipe);
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("view/create-recipe.jsp");
		dispatcher.forward(request, response);
	}

}
