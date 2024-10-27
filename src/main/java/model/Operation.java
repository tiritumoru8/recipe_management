package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import dto.Account;
import dto.Food;
import dto.Recipe;
import model.dao.AccountDAO;
import model.dao.FoodDAO;
import model.dao.RecipeDAO;

public class Operation {
	private AccountDAO accountDao = new AccountDAO();
	private RecipeDAO recipeDao = new RecipeDAO();
	private FoodDAO foodDao = new FoodDAO();
	
	public boolean login(HttpSession session, String email, String password) throws Exception{
		boolean result = false;
		String hashPass = PasswordHasher.hashPassword(password);
		
		Account account = accountDao.findAccount(email, hashPass);
		if(account.getUserId() != 0) {
			Account addAccount = accountDao.getAddResource(account);
			session.setAttribute("account", addAccount);
			result = true;
		}
		
		return result;
	}
	/**
	 * セッションに格納するリソース情報の取得処理
	 * @param session ... セッションスコープ
	 * @throws Exception
	 */
	public void setResource(HttpSession session) throws Exception{
		Account account = (Account) session.getAttribute("account");
		int userId = account.getUserId();
		
		//レシピ情報取得し、セッションスコープに格納
		setAllRecipe(session,userId);
		
		//料理ジャンルリストを取得し、セッションスコープに格納
		List<String> cookingGenre = recipeDao.selectCookingGenre();
		if(cookingGenre.size() != 0) {
			session.setAttribute("cookingGenre", cookingGenre);
		}else {
			throw new Exception();
		}
		
		//全食材情報を取得し、セッションスコープに格納
		List<Food> foodList = foodDao.selectFoodList();
		if(foodList.size() != 0) {
			session.setAttribute("food", foodList);
		}else {
			throw new Exception();
		}
		
		//食材ジャンルリストを取得し、セッションスコープに格納
		List<String> foodGenre = foodDao.selectFoodGenre();
		if(foodGenre.size() != 0) {
			session.setAttribute("foodGenre", foodGenre);
		}else {
			throw new Exception();
		}
		
		//食材単位リストを取得し、セッションスコープに格納
		List<String> unit = foodDao.selectUnit();
		if(unit.size() != 0) {
			session.setAttribute("unit", unit);
		}else {
			throw new Exception();
		}
		
		//マイレシピをジャンル別に取得し、セッションスコープに格納
		//マイレシピを登録していない場合はnull
		List<List<Recipe>> myRecipe = recipeDao.selectMyRecipe(cookingGenre, userId);
		if(myRecipe.size() != 0) {
			session.setAttribute("myRecipe", myRecipe);
		}
		
		//マイ冷蔵庫をジャンル別に取得し、セッションスコープに格納
		//マイ冷蔵庫を登録していない場合はnull
		List<List<Food>> refrigerator = foodDao.selectByUserId(foodGenre, userId);
		if(refrigerator.size() != 0) {
			session.setAttribute("refrigerator", refrigerator);
		}
	}
	
	public void setAllRecipe(HttpSession session,int userId) throws Exception {
		//ジャンル別にレシピリストを取得
		List<List<Recipe>> recipeGenreList = recipeDao.selectCookingList(userId);
		//ジャンル別のレシピリストをひとつに統合
		List<Recipe> recipeList = new ArrayList<>();
		for(List<Recipe> recipe : recipeGenreList) {
			for(int i = 0; i < recipe.size(); i++) {
				recipeList.add(recipe.get(i));
			}
		}
		if(recipeGenreList.size() != 0 && recipeList.size() != 0) {
			session.setAttribute("recipe", recipeGenreList);
			session.setAttribute("recipeAll", recipeList);
		}else {
			throw new Exception();
		}
	}
/**
 * お気に入りレシピ取得処理
 * @param session ... セッションスコープ
 * @throws Exception
 */
	public void setFavoriteRecipe(HttpSession session) throws Exception{
		@SuppressWarnings("unchecked")
		List<Recipe> recipeList = (List<Recipe>) session.getAttribute("recipeAll");
		Account account = (Account) session.getAttribute("account");
		int userId = account.getUserId();
		List<Recipe> favoriteRecipe = recipeDao.selectFavoriteRecipe(recipeList,userId);
		if(favoriteRecipe.size() != 0) {
			session.setAttribute("favoriteRecipe", favoriteRecipe);
		}
	}
	
	public void search(HttpSession session,String keyword) throws Exception {
		List<Recipe> searchRecipe = recipeDao.searchRecipe(keyword);
		if(searchRecipe.size() != 0) {
			session.setAttribute("search", searchRecipe);
		}else {
			session.removeAttribute("search");
			throw new Exception();
		}
	}
	
	public Recipe recipeShow(HttpSession session,List<Recipe> recipeList, int index) {
		Recipe recipe = null;
		recipe = recipeList.get(index);
		return recipe;
	}
	public int addFood(HttpSession session,int foodId, String foodKana, int quantity) throws Exception{
		@SuppressWarnings("unchecked")
		List<List<Food>> refrigeratorList = (List<List<Food>>) session.getAttribute("refrigerator");
		Account account = (Account) session.getAttribute("account");
		int userId = account.getUserId();
		int count = 0;
		//マイ冷蔵庫に食材が登録されている場合、マイ冷蔵庫に該当の食材が存在するか確認
		boolean exist = false;
		int indexA = 0;
		int indexB = 0;
		if(refrigeratorList != null) {
			for(int a = 0; a < refrigeratorList.size(); a++) {
				List<Food> refrigerator = refrigeratorList.get(a);
				for(int b = 0; b < refrigerator.size(); b++) {
					Food food = refrigerator.get(b);
					if(food.getFoodKana().equals(foodKana)) {
						exist = true;
						indexA = a;
						indexB = b;
					}
				}
			}
		}
		
		//マイ冷蔵庫に存在する場合、個数追加処理を行う
		if(exist) {
			count += foodDao.addFood(userId, foodId, quantity);
			//処理が成功した場合、保管している食材の個数のみ変更
			if(count != 0) {
				Food food = refrigeratorList.get(indexA).get(indexB);
				food.addQuantity(quantity);
				refrigeratorList.get(indexA).set(indexB, food);
				session.setAttribute("refrigerator", refrigeratorList);
				
				int[] foodQuantity = foodDao.countRefrigerator(userId);
				account.setKindOfFood(foodQuantity[0]);
				account.setFavoriteCount(foodQuantity[1]);
				session.setAttribute("account", account);
			}
		}else {
		//マイ冷蔵庫に存在しない場合、食材をマイ冷蔵庫に新規で登録する
			count+= foodDao.addNewFood(userId, foodId, quantity);
			if(count != 0) {
				@SuppressWarnings("unchecked")
				List<String> foodGenre = (List<String>) session.getAttribute("foodGenre");
				List<List<Food>> refrigerator = foodDao.selectByUserId(foodGenre, userId);
				if(refrigerator.size() != 0) {
					session.setAttribute("refrigerator", refrigerator);
					
					int[] foodQuantity = foodDao.countRefrigerator(userId);
					account.setKindOfFood(foodQuantity[0]);
					account.setFoodQuantity(foodQuantity[1]);
					session.setAttribute("account", account);
				}else {
					throw new Exception();
				}
			}
		}
		return count;
	}
	public boolean removeFood(HttpSession session, int foodId, int idxA, int idxB) throws Exception {
		boolean result = false;
		int count = 0;
		Account account = (Account) session.getAttribute("account");
		@SuppressWarnings("unchecked")
		List<List<Food>> refrigeratorList = (List<List<Food>>) session.getAttribute("refrigerator");
		
		if(account != null && refrigeratorList != null) {
			int userId = account.getUserId();
			count = foodDao.removeFoodByUserId(userId, foodId);
			if(count > 0) {
				//既存のマイ冷蔵庫情報から特定の食材を取り除き、セッションスコープに上書きで格納
				List<Food> refrigerator = refrigeratorList.get(idxA);
				refrigerator.remove(idxB);
				if(refrigerator.size() == 0) {
					refrigeratorList.remove(idxA);
				}
				session.setAttribute("refrigerator", refrigeratorList);
				
				int[] foodQuantity = foodDao.countRefrigerator(userId);
				account.setKindOfFood(foodQuantity[0]);
				account.setFoodQuantity(foodQuantity[1]);
				session.setAttribute("account", account);
				
				result = true;
			}
		}
		
		return result;
	}
	public int newFood(HttpSession session,String foodName, String foodKana, String foodGenre, String unit) throws Exception{
		int count = 0;
		count += foodDao.newFood(foodName,foodKana,foodGenre,unit);
		if(count != 0) {
			//全食材情報を取得し、セッションスコープに格納
			List<Food> foodList = foodDao.selectFoodList();
			if(foodList.size() != 0) {
				session.setAttribute("food", foodList);
			}else {
				count = 0;
			}
		}
		return count;
	}
	
	public boolean useFood(HttpSession session,Map<Integer,String> map,
			List<List<Food>> refrigerator) throws Exception {
		@SuppressWarnings("unchecked")
		List<String> foodGenre = (List<String>) session.getAttribute("foodGenre");
		Account account = (Account) session.getAttribute("account");
		int userId = account.getUserId();
		boolean result = false;
		List<List<Food>> newRefrigerator = foodDao.useFood(map, refrigerator, userId,foodGenre);
		if(newRefrigerator != null) {
			session.setAttribute("refrigerator", newRefrigerator);
			
			int[] foodQuantity = foodDao.countRefrigerator(userId);
			account.setKindOfFood(foodQuantity[0]);
			account.setFoodQuantity(foodQuantity[1]);
			session.setAttribute("account", account);
			result = true;
		}
		return result;
	}
	
	public Recipe favorite(HttpSession session, int recipeId, int favorite) throws Exception{
		int count = 0;
		Recipe selectRecipe = null;
		Account account = (Account) session.getAttribute("account");
		@SuppressWarnings("unchecked")
		List<Recipe> favoriteList = (List<Recipe>) session.getAttribute("favoriteRecipe");
		int userId = account.getUserId();
		
		if(favorite == 0) {
			selectRecipe = recipeDao.insertFavoriteRecipe(recipeId, userId);
			if(selectRecipe != null && favoriteList != null) {
				favoriteList.add(selectRecipe);
				session.setAttribute("favoriteRecipe", favoriteList);
			}else if(selectRecipe != null && favoriteList == null) {
				List<Recipe> newFavoriteRecipe = new ArrayList<>();
				newFavoriteRecipe.add(selectRecipe);
				session.setAttribute("favoriteRecipe", newFavoriteRecipe);
			}else {
				throw new Exception();
			}
		}else {
			count += recipeDao.deleteFavoriteRecipe(recipeId, userId);
			if(count > 0) {
				for(int i = 0; i < favoriteList.size(); i++) {
					Recipe recipe = favoriteList.get(i);
					if(recipe.getRecipeId() == recipeId) {
						selectRecipe = recipe;
						favoriteList.remove(i);
					}
				}
				if(favoriteList.size() != 0) {
					session.setAttribute("favoriteRecipe", favoriteList);
				}else {
					session.removeAttribute("favoriteRecipe");
				}
			}else {
				throw new Exception();
			}
		}
		int favoriteCount = recipeDao.countFavoriteRecipe(userId);
		
		account.setFavoriteCount(favoriteCount);
		session.setAttribute("account", account);
		return selectRecipe;
	}
	
	public boolean editMyRecipe(HttpSession session, Recipe recipe) throws Exception{
		boolean result = false;
		@SuppressWarnings("unchecked")
		List<String> cookingGenre = (List<String>) session.getAttribute("cookingGenre");
		Account account = (Account) session.getAttribute("account");
		int userId = account.getUserId();
		
		List<List<Recipe>> myRecipe = new ArrayList<>();
		String imagePath = recipe.getImagePath();
		if(imagePath != null) {
			myRecipe = recipeDao.editByRecipeId(recipe,cookingGenre,userId);
		}else {
			myRecipe = recipeDao.editRecipeWithoutImage(recipe,cookingGenre,userId);
		}
				
		if(myRecipe.size() != 0) {
			session.setAttribute("myRecipe", myRecipe);
			setAllRecipe(session,account.getUserId());
			
			int countMyRecipe = recipeDao.countMyRecipe(userId);
			int countFavoriteRecipe = recipeDao.countFavoriteRecipe(userId);
			account.setMyRecipeCount(countMyRecipe);
			account.setFavoriteCount(countFavoriteRecipe);
			session.setAttribute("account", account);
			
			result = true;
		}
		return result;
	}
	public boolean deleteMyRecipe(HttpSession session, int indexA, int indexB) throws Exception {
		boolean result = false;
		@SuppressWarnings("unchecked")
		List<List<Recipe>> myRecipeList = (List<List<Recipe>>) session.getAttribute("myRecipe");
		List<Recipe> myRecipe = myRecipeList.get(indexA);
		Recipe recipe = myRecipe.get(indexB);
		Account account = (Account) session.getAttribute("account");
		int userId = account.getUserId();
		
		int count = recipeDao.deleteMyRecipe(recipe);
		if(count != 0) {
			myRecipe.remove(indexB);
			if(myRecipe.size() != 0) {
				myRecipeList.set(indexA, myRecipe);
			}else {
				myRecipeList.remove(indexA);
			}
			
			if(myRecipeList.size() != 0) {
				session.setAttribute("myRecipe", myRecipeList);
			}else {
				session.removeAttribute("myRecipe");
			}
			
			setAllRecipe(session, userId);
			
			int countMyRecipe = recipeDao.countMyRecipe(userId);
			int countFavoriteRecipe = recipeDao.countFavoriteRecipe(userId);
			account.setMyRecipeCount(countMyRecipe);
			account.setFavoriteCount(countFavoriteRecipe);
			session.setAttribute("account", account);
			
			result = true;
		}
				
		return result;
	}
	
	public boolean newMyRecipe(HttpSession session,Recipe recipe) throws Exception {
		boolean result = false;
		@SuppressWarnings("unchecked")
		List<String> cookingGenre = (List<String>) session.getAttribute("cookingGenre");
		Account account = (Account) session.getAttribute("account");
		int userId = account.getUserId();
		String imagePath = recipe.getImagePath();
		List<List<Recipe>> myRecipe = new ArrayList<>();
		if(imagePath != null) {
			myRecipe = recipeDao.insertMyRecipe(recipe,cookingGenre,userId);
		}else {
			myRecipe = recipeDao.insertRecipeWithoutImage(recipe,cookingGenre,userId);
		}
		
		if(myRecipe.size() != 0) {
			session.setAttribute("myRecipe", myRecipe);
			//全レシピリストを再取得し、セッションスコープへ
			setAllRecipe(session,account.getUserId());
			
			int countMyRecipe = recipeDao.countMyRecipe(userId);
			int countFavoriteRecipe = recipeDao.countFavoriteRecipe(userId);
			account.setMyRecipeCount(countMyRecipe);
			account.setFavoriteCount(countFavoriteRecipe);
			session.setAttribute("account", account);
			
			result = true;
		}
		return result;
	}
	
	
/**
 * アカウント編集処理
 * @param session ... セッションスコープ
 * @param account ... 新規アカウント情報
 * @return count ... 処理件数
 * @throws Exception
 */
	public int reqistration(HttpSession session, Account account) throws Exception{
		Account oldAccount = (Account) session.getAttribute("account");
		int userId = 0;
		//新規以外
		if(oldAccount != null) {
			userId = oldAccount.getUserId();
		}
		String userName = account.getUserName();
		String email = account.getEmail();
		String password = account.getPassword();
		
		int count = 0;
		
		//新規登録
		if(userId == 0 && userName != null && email != null && password != null) {
			count += accountDao.newAccount(session, userId, userName, email, password);
		}else if(userId != 0 && userName != null && email != null && password != null) {
			count += accountDao.insertAll(session, userId, userName, email, password);
		}else if(userId != 0 && userName != null && email != null) {
			count += accountDao.insertNameEmail(session, userId, userName, email);
		}else if(userId != 0 && userName != null && password != null) {
			count += accountDao.insertNamePass(session, userId, userName, password);
		}else if(userId != 0 && email != null && password != null) {
			count += accountDao.insertEmailPass(session, userId, email, password);
		}else if(userId != 0 && userName != null) {
			count += accountDao.insertUserName(session, userId, userName);
		}else if(userId != 0 && email != null) {
			count += accountDao.insertEmail(session, userId, email);
		}else if(userId != 0 && password != null) {
			count += accountDao.insertPassword(session, userId, password);
		}else {
			throw new Exception();
		}
		return count;
	}
	
	public boolean deleteAccount(HttpSession session) throws Exception {
		boolean result = false;
		int count = 0;
		Account account = (Account) session.getAttribute("account");
		//セッション切れ確認
		if(account != null) {
			//ユーザ情報削除
			int userId = account.getUserId();
			count += accountDao.delete(userId);
		}else {
			throw new Exception();
		}
		if(count > 0) {
			result = true;
			session.invalidate();
		}
		return result;
	}
	
	
}
