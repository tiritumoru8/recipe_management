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
 * Servlet implementation class DeleteRecipeServlet
 */
@WebServlet("/delete-recipe-servlet")
public class DeleteRecipeServlet extends HttpServlet {
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
		String idxAStr = request.getParameter("indexA");
		String idxBStr = request.getParameter("indexB");
		String url = "view/my-recipe.jsp";
		
		HttpSession session = request.getSession();
		
		try {
			int indexA = Integer.parseInt(idxAStr);
			int indexB = Integer.parseInt(idxBStr);
			Operation op = new Operation();
			boolean result = op.deleteMyRecipe(session, indexA, indexB);
			
			if(result) {
				request.setAttribute("message", "レシピを削除しました");
			}else {
				request.setAttribute("errorMsg", "データベース上の処理に失敗しました");
			}
		}catch(NumberFormatException e) {
			request.setAttribute("errorMsg", "インデックスを整数に変換できませんでした");
		}catch(Exception e) {
			request.setAttribute("errorMsg", "削除できませんでした");
		}
		
		RequestDispatcher dispatcher = request.getRequestDispatcher(url);
		dispatcher.forward(request, response);
	}

}
