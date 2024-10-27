package control;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import dto.Recipe;
import model.Operation;

/**
 * Servlet implementation class EditRecipeServlet
 */
@WebServlet("/edit-recipe-servlet")
@MultipartConfig
public class EditRecipeServlet extends HttpServlet {
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
		String url = "view/my-recipe.jsp";
		
		request.setCharacterEncoding("utf-8");
		String recipeIdStr = request.getParameter("recipeId");
		String recipeName = request.getParameter("recipeName");
		String recipeKana = request.getParameter("recipeKana");
		String cookingGenre = request.getParameter("cookingGenre");
		Part fpart = request.getPart("imageFile");
		String foodList = request.getParameter("foodList");
		String process = request.getParameter("process");
		HttpSession session = request.getSession();
		
		try {
			int recipeId = Integer.parseInt(recipeIdStr);
			String fPath = null;
			if(fpart.getSize() != 0) {
				fPath = fileUpload(fpart);
			}
			Operation op = new Operation();
			boolean result = op.editMyRecipe(session, new Recipe(recipeId,recipeName,recipeKana,
					cookingGenre,fPath,foodList,process));
			if(result) {
				request.setAttribute("message", "レシピを編集しました");
			}else {
				request.setAttribute("errorMsg", "データベースの読み込みができませんでした。再ログインをお願いします");
				url = "view/login.jsp";
			}
		}catch(Exception e) {
			request.setAttribute("errorMsg", "レシピを編集できませんでした");
		}
		
		RequestDispatcher dispatcher = request.getRequestDispatcher(url);
		dispatcher.forward(request, response);
	}
	
	private String fileUpload(Part part) throws IOException{
		String fPath = "/image/" + part.getSubmittedFileName();
		part.write(getServletContext().getRealPath("image/" + part.getSubmittedFileName()));
		return fPath;
	}
}
