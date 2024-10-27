package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dto.Recipe;

public class RecipeDAO {
	
	public List<List<Recipe>> selectCookingList(int userId) throws Exception{
		
		List<List<Recipe>> recipeGenreList = new ArrayList<>();
		
		String sql1 = "SELECT cooking_genre_id FROM cooking_genre";
		String sql2 = "SELECT recipe_id, recipe_name, recipe_kana, image_file,"
				+ "food_list, process,c.cooking_genre_name "
				+ "FROM (SELECT * FROM recipe WHERE cooking_genre_id = ?) r "
				+ "LEFT JOIN cooking_genre c ON r.cooking_genre_id = c.cooking_genre_id "
				+ "WHERE user_id IS NULL OR user_id = ?";
		
		try(Connection con = MyConnection.getConnection();
				Statement state = con.createStatement();
				ResultSet res1 = state.executeQuery(sql1);
				PreparedStatement ps = con.prepareStatement(sql2)) {
			List<Integer> genreIdList = new ArrayList<>();
			while(res1.next()) {
				 genreIdList.add(res1.getInt("cooking_genre_id"));
			}
			
			for(int i = 0; i < genreIdList.size(); i++) {
				int genreId = genreIdList.get(i);
				ps.setInt(1, genreId);
				ps.setInt(2, userId);
				List<Recipe> recipeList = new ArrayList<>();
				ResultSet res2 = ps.executeQuery();
				while(res2.next()) {
					int recipeId = res2.getInt("recipe_id");
					String recipeName = res2.getString("recipe_name");
					String recipeKana = res2.getString("recipe_kana");
					String recipeGenre = res2.getString("cooking_genre_name");
					String imagePath = res2.getString("image_file");
					String foodList = res2.getString("food_list");
					String process = res2.getString("process");
					Recipe recipe = new Recipe(recipeId,recipeName, recipeKana, recipeGenre,
							imagePath, foodList, process);
					recipeList.add(recipe);
				}
				if(recipeList.size() != 0) {
					recipeGenreList.add(recipeList);
				}
				
			}
		}
		return recipeGenreList;
	}
	
	public List<String> selectCookingGenre() throws Exception {
		List<String> cookingGenre = new ArrayList<>();
		
		String sql = "SELECT cooking_genre_name FROM cooking_genre";
		try(Connection con = MyConnection.getConnection();
				Statement state = con.createStatement();
				ResultSet res = state.executeQuery(sql)) {
			while(res.next()) {
				cookingGenre.add(res.getString("cooking_genre_name"));
			}
		}
		return cookingGenre;
	}
	
	public List<Recipe> selectFavoriteRecipe(List<Recipe> recipeList,int userId) throws Exception {
		List<Recipe> favoriteRecipe = new ArrayList<>();
		
		String sql = "SELECT recipe_id FROM favorite WHERE user_id = ?";
		try(Connection con = MyConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, userId);
			ResultSet res = ps.executeQuery();
			while(res.next()) {
				int i = 0;
				while(i < recipeList.size()) {
					Recipe recipe = recipeList.get(i);
					if(res.getInt("recipe_id") == recipe.getRecipeId()) {
						favoriteRecipe.add(recipe);
					}
					i++;
				}
			}
		}
		return favoriteRecipe;
	}
	
	public Recipe insertFavoriteRecipe(int recipeId, int userId) throws Exception {
		Recipe recipe = null;
		int count = 0;
		
		String sql1 = "INSERT INTO favorite (recipe_id,user_id,favorite) "
				+ "VALUES(?,?,true)";
		
		String sql2 = "SELECT recipe_name,recipe_kana,image_file,process,food_list,"
				+ "cooking_genre_name FROM recipe r "
				+ "LEFT JOIN cooking_genre cg ON r.cooking_genre_id = cg.cooking_genre_id "
				+ "WHERE recipe_id = ?";
		
		try(Connection con = MyConnection.getConnection()) {
			try(PreparedStatement ps1 = con.prepareStatement(sql1);
					PreparedStatement ps2 = con.prepareStatement(sql2)) {
				con.setAutoCommit(false);
				
				ps1.setInt(1, recipeId);
				ps1.setInt(2, userId);
				count += ps1.executeUpdate();
				if(count > 0) {
					ps2.setInt(1, recipeId);
					ResultSet res = ps2.executeQuery();
					while(res.next()) {
						String recipeName = res.getString("recipe_name");
						String recipeKana = res.getString("recipe_kana");
						String recipeGenre = res.getString("cooking_genre_name");
						String imagePath = res.getString("image_file");
						String foodList = res.getString("food_list");
						String process = res.getString("process");
						if(imagePath != null) {
							recipe = new Recipe(recipeId,recipeName,recipeKana,recipeGenre,imagePath,
									foodList,process);
						}else {
							recipe = new Recipe(recipeId,recipeName,recipeKana,recipeGenre,
									foodList,process);
						}
					}
				}else {
					throw new Exception();
				}
				con.commit();
			}catch(Exception e) {
				con.rollback();
			}
		}
		
		return recipe;
	}
	
	public int deleteFavoriteRecipe(int recipeId, int userId) throws Exception {
		int count = 0;
		String sql = "DELETE FROM favorite WHERE recipe_id = ? AND user_id = ?";
		
		try(Connection con = MyConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, recipeId);
			ps.setInt(2, userId);
			count += ps.executeUpdate();
		}
		
		return count;
	}
	
	public int countFavoriteRecipe(int userId) throws Exception {
		String sql = "SELECT COUNT(recipe_id) AS favorite FROM favorite WHERE user_id = ?";
		int favoriteCount = 0;
		
		try(Connection con = MyConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, userId);
			ResultSet res = ps.executeQuery();
			if(res.next()) {
				favoriteCount = res.getInt("favorite");
			}
		}
		
		return favoriteCount;
	}
	
	public List<List<Recipe>> selectMyRecipe(List<String> cookingGenreList, int userId) throws Exception{
		//料理ジャンルリストのcooking_genre_idを取得
		String sql1= "SELECT cooking_genre_id FROM cooking_genre WHERE cooking_genre_name = ?";
		//ジャンル別にマイレシピを取得するsql
		String sql2 = "SELECT recipe_id, recipe_name, recipe_kana, image_file,food_list,"
				+ "process,c.cooking_genre_name "
				+ "FROM (SELECT * FROM recipe WHERE cooking_genre_id = ?) r "
				+ "LEFT JOIN cooking_genre c ON r.cooking_genre_id = c.cooking_genre_id "
				+ "WHERE user_id = ?";
		
		List<Integer> cookingGenreIdList = new ArrayList<>();
		List<List<Recipe>> myRecipe = new ArrayList<>();
		
		for(String cookingGenre : cookingGenreList) {
			try(Connection con = MyConnection.getConnection();
					PreparedStatement ps1 = con.prepareStatement(sql1)) {
				ps1.setString(1, cookingGenre);
				ResultSet res = ps1.executeQuery();
				if(res.next()) {
					int cookingGenreId = res.getInt("cooking_genre_id");
					cookingGenreIdList.add(cookingGenreId);
				}
			}
		}
		if(cookingGenreList.size() != 0) {
			for(int cookingGenre : cookingGenreIdList) {
				try(Connection con = MyConnection.getConnection();
						PreparedStatement ps2 = con.prepareStatement(sql2)) {
					ps2.setInt(1, cookingGenre);
					ps2.setInt(2, userId);
					List<Recipe> recipeList = new ArrayList<>();
					ResultSet res = ps2.executeQuery();
					while(res.next()) {
						int recipeId = res.getInt("recipe_id");
						String recipeName = res.getString("recipe_name");
						String recipeKana = res.getString("recipe_kana");
						String recipeGenre = res.getString("cooking_genre_name");
						String imagePath = res.getString("image_file");
						String foodList = res.getString("food_list");
						String process = res.getString("process");
						Recipe recipe = new Recipe(recipeId,recipeName, recipeKana, recipeGenre,
								imagePath, foodList, process);
						recipeList.add(recipe);
					}
					if(recipeList.size() != 0) {
						myRecipe.add(recipeList);
					}
				}
			}
			return myRecipe;
		}else {
			throw new Exception();
		}
	}
	public List<List<Recipe>> insertMyRecipe(Recipe recipe,List<String>cookingGenre, int userId) throws Exception {
		int count = 0;
		
		String recipeName = recipe.getRecipeName();
		String recipeKana = recipe.getRecipeKana();
		String recipeGenre = recipe.getRecipeGenre();
		String imagePath = recipe.getImagePath();
		String process = recipe.getProcess();
		String foodList = recipe.getFoodList();
		
		String sql1 = "SELECT cooking_genre_id FROM cooking_genre WHERE cooking_genre_name = ?";
		String sql2 = "INSERT INTO recipe (recipe_name,recipe_kana,user_id,image_file,process,"
				+ "food_list,cooking_genre_id) VALUES(?,?,?,?,?,?,?)";
		
		int recipeGenreId = 0;
		
		try(Connection con = MyConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql1)) {
			ps.setString(1, recipeGenre);
			ResultSet res = ps.executeQuery();
			if(res.next()) {
				recipeGenreId = res.getInt("cooking_genre_id");
			}
		}
		if(recipeGenreId != 0) {
			try(Connection con = MyConnection.getConnection();
					PreparedStatement ps = con.prepareStatement(sql2)) {
				ps.setString(1, recipeName);
				ps.setString(2, recipeKana);
				ps.setInt(3, userId);
				ps.setString(4, imagePath);
				ps.setString(5, process);
				ps.setString(6, foodList);
				ps.setInt(7, recipeGenreId);
				count += ps.executeUpdate();
				if(count == 0) {
					throw new Exception();
				}
			}
		}else {
			throw new Exception();
		}
		
		List<List<Recipe>> myRecipe = selectMyRecipe(cookingGenre,userId);
		return myRecipe;
	}
	public List<List<Recipe>> insertRecipeWithoutImage(Recipe recipe, List<String> cookingGenre,
			int userId) throws Exception {
		int count = 0;
				
		String recipeName = recipe.getRecipeName();
		String recipeKana = recipe.getRecipeKana();
		String recipeGenre = recipe.getRecipeGenre();
		String process = recipe.getProcess();
		String foodList = recipe.getFoodList();
				
		String sql1 = "SELECT cooking_genre_id FROM cooking_genre WHERE cooking_genre_name = ?";
		String sql2 = "INSERT INTO recipe (recipe_name,recipe_kana,user_id,process,"
						+ "food_list,cooking_genre_id) VALUES(?,?,?,?,?,?)";
				
		int recipeGenreId = 0;
				
		try(Connection con = MyConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql1)) {
			ps.setString(1, recipeGenre);
			ResultSet res = ps.executeQuery();
			if(res.next()) {
				recipeGenreId = res.getInt("cooking_genre_id");
			}
		}
		
		if(recipeGenreId != 0) {
			try(Connection con = MyConnection.getConnection();
					PreparedStatement ps = con.prepareStatement(sql2)) {
				ps.setString(1, recipeName);
				ps.setString(2, recipeKana);
				ps.setInt(3, userId);
				ps.setString(4, process);
				ps.setString(5, foodList);
				ps.setInt(6, recipeGenreId);
				count += ps.executeUpdate();
				if(count == 0) {
					throw new Exception();
				}
			}
		}else {
			throw new Exception();
		}
				
		List<List<Recipe>> myRecipe = selectMyRecipe(cookingGenre,userId);
		return myRecipe;
	}
	
	public int countMyRecipe(int userId) throws Exception {
		String sql = "SELECT COUNT(recipe_id) AS recipe FROM recipe WHERE user_id = ?";
		int countMyRecipe = 0;
		try(Connection con = MyConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, userId);
			ResultSet res1 = ps.executeQuery();
			if(res1.next()) {
				countMyRecipe = res1.getInt("recipe");
			}
		}
		return countMyRecipe;
	}
	
	public List<Recipe> searchRecipe(String keyword) throws Exception {
		List<Recipe> searchRecipe = new ArrayList<>();
		
		String sql = "SELECT recipe_id,recipe_name,recipe_kana,cooking_genre_name,"
				+ "image_file,food_list,process FROM recipe r "
				+ "LEFT JOIN cooking_genre cg ON r.cooking_genre_id = cg.cooking_genre_id "
				+ "WHERE recipe_kana LIKE ?";
		try(Connection con = MyConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, "%" + keyword + "%");
			ResultSet res = ps.executeQuery();
			while(res.next()) {
				int recipeId = res.getInt("recipe_id");
				String recipeName = res.getString("recipe_name");
				String recipeKana = res.getString("recipe_kana");
				String recipeGenre = res.getString("cooking_genre_name");
				String imagePath = res.getString("image_file");
				String foodList = res.getString("food_list");
				String process = res.getString("process");
				Recipe recipe = new Recipe(recipeId,recipeName,recipeKana,recipeGenre,
						imagePath,foodList,process);
				searchRecipe.add(recipe);
			}
		}
		return searchRecipe;
	}
	public List<List<Recipe>> editByRecipeId(Recipe recipe, List<String> cookingGenre,int userId) throws Exception {
		int count = 0;
		
		int recipeId = recipe.getRecipeId();
		String recipeName = recipe.getRecipeName();
		String recipeKana = recipe.getRecipeKana();
		String recipeGenre = recipe.getRecipeGenre();
		String imagePath = recipe.getImagePath();
		String foodList = recipe.getFoodList();
		String process = recipe.getProcess();
		
		String sql1 = "SELECT cooking_genre_id FROM cooking_genre WHERE cooking_genre_name = ?";
		String sql2 = "UPDATE recipe SET recipe_name = ?,recipe_kana = ?,image_file = ?,"
				+ "process = ?,food_list = ?,cooking_genre_id = ? WHERE recipe_id = ?";
		
		int recipeGenreId = 0;
		
		try(Connection con = MyConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql1)) {
			ps.setString(1, recipeGenre);
			ResultSet res = ps.executeQuery();
			if(res.next()) {
				recipeGenreId = res.getInt("cooking_genre_id");
			}
		}
		if(recipeGenreId != 0) {
			try(Connection con = MyConnection.getConnection();
					PreparedStatement ps = con.prepareStatement(sql2)) {
				ps.setString(1, recipeName);
				ps.setString(2, recipeKana);
				ps.setString(3, imagePath);
				ps.setString(4,process);
				ps.setString(5, foodList);
				ps.setInt(6, recipeGenreId);
				ps.setInt(7, recipeId);
				
				count += ps.executeUpdate();
				if(count == 0) {
					throw new Exception();
				}
			}
		}else {
			throw new Exception();
		}
		
		List<List<Recipe>> myRecipe = selectMyRecipe(cookingGenre,userId);
		return myRecipe;
	}
	
	public List<List<Recipe>> editRecipeWithoutImage(Recipe recipe,List<String> cookingGenre, int userId) throws Exception {
		int count = 0;
		
		int recipeId = recipe.getRecipeId();
		String recipeName = recipe.getRecipeName();
		String recipeKana = recipe.getRecipeKana();
		String recipeGenre = recipe.getRecipeGenre();
		String foodList = recipe.getFoodList();
		String process = recipe.getProcess();
		
		String sql1 = "SELECT cooking_genre_id FROM cooking_genre WHERE cooking_genre_name = ?";
		String sql2 = "UPDATE recipe SET recipe_name = ?,recipe_kana = ?,"
				+ "process = ?,food_list = ?,cooking_genre_id = ? WHERE recipe_id = ?";
		
		int recipeGenreId = 0;
		
		try(Connection con = MyConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql1)) {
			ps.setString(1, recipeGenre);
			ResultSet res = ps.executeQuery();
			if(res.next()) {
				recipeGenreId = res.getInt("cooking_genre_id");
			}
		}
		if(recipeGenreId != 0) {
			try(Connection con = MyConnection.getConnection();
					PreparedStatement ps = con.prepareStatement(sql2)) {
				ps.setString(1, recipeName);
				ps.setString(2, recipeKana);
				ps.setString(3,process);
				ps.setString(4, foodList);
				ps.setInt(5, recipeGenreId);
				ps.setInt(6, recipeId);
				
				count += ps.executeUpdate();
				if(count == 0) {
					throw new Exception();
				}
			}
		}else {
			throw new Exception();
		}
		
		List<List<Recipe>> myRecipe = selectMyRecipe(cookingGenre,userId);
		return myRecipe;
	}
	
	
	
	
	public int deleteMyRecipe(Recipe recipe) throws Exception{
		int count = 0;
		int recipeId = recipe.getRecipeId();
		String sql = "DELETE FROM recipe WHERE recipe_id = ?";
		
		if(recipeId != 0) {
			try(Connection con = MyConnection.getConnection();
					PreparedStatement ps = con.prepareStatement(sql)) {
				ps.setInt(1, recipeId);
				count = ps.executeUpdate();
			}
		}
		
		return count;
	}
	
	
	public String[] deleteByUserId() {
		String sql1 = "DELETE FROM recipe WHERE user_id = ?";
		String sql2 = "DELETE FROM favorite WHERE user_id = ?";
		String[] sqlList = {sql1, sql2};
		return sqlList;
	}
}
