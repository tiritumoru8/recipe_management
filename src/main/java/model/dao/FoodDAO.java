package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dto.Food;

public class FoodDAO {
	/**
	 * 全食材情報を取得
	 * @return foodList ... 全食材情報
	 * @throws Exception
	 */
	public List<Food> selectFoodList() throws Exception {
		List<Food> foodList = new ArrayList<>();
		
		String sql = "SELECT food_id,food_name,food_kana,fg.food_genre_name,u.unit,food_image "
				+ "FROM food f "
				+ "LEFT JOIN food_genre fg ON f.food_genre_id = fg.food_genre_id "
				+ "LEFT JOIN unit u ON f.unit_id = u.unit_id";
		
		try(Connection con = MyConnection.getConnection();
				Statement state = con.createStatement();
				ResultSet res = state.executeQuery(sql)) {
			while(res.next()) {
				int foodId = res.getInt("food_id");
				String foodName = res.getString("food_name");
				String foodKana = res.getString("food_kana");
				String foodGenre = res.getString("food_genre_name");
				String unit = res.getString("unit");
				String imagePath = res.getString("food_image");
				Food food = new Food(foodId,foodName,foodKana,foodGenre,unit,imagePath);
				
				foodList.add(food);
			}
		}
		return foodList;
	}
	public List<String> selectFoodGenre() throws Exception {
		List<String> foodList = new ArrayList<>();
		
		String sql = "SELECT food_genre_name FROM food_genre";
		try(Connection con = MyConnection.getConnection();
				Statement state = con.createStatement();
				ResultSet res = state.executeQuery(sql)) {
			while(res.next()) {
				String foodGenre = res.getString("food_genre_name");
				foodList.add(foodGenre);
			}
		}
		return foodList;
	}
	public List<String> selectUnit() throws Exception {
		List<String> unitList = new ArrayList<>();
		
		String sql = "SELECT unit FROM unit";
		try(Connection con = MyConnection.getConnection();
				Statement state = con.createStatement();
				ResultSet res = state.executeQuery(sql)) {
			while(res.next()) {
				String unit = res.getString("unit");
				unitList.add(unit);
			}
		}
		return unitList;
	}
	
	public List<List<Food>> selectByUserId(List<String> foodGenreList, int userId) throws Exception{
		//食材ジャンルリストのfood_genre_idを取得
		String sql1= "SELECT food_genre_id FROM food_genre WHERE food_genre_name = ?";
		//ジャンル別に食材リストを取得するsql
		String sql2 = "SELECT f.food_id,food_name,food_kana,fg.food_genre_name,"
				+ "u.unit,food_image ,quantity "
				+ "FROM (SELECT * FROM food WHERE food_genre_id = ?) f "
				+ "LEFT JOIN food_genre fg ON f.food_genre_id = fg.food_genre_id "
				+ "LEFT JOIN unit u ON f.unit_id = u.unit_id "
				+ "LEFT JOIN refrigerator r ON r.food_id  = f.food_id "
				+ "WHERE user_id = ?";
				
		List<Integer> foodGenreIdList = new ArrayList<>();
		List<List<Food>> refrigerator = new ArrayList<>();
		
		for(String foodGenre : foodGenreList) {
			try(Connection con = MyConnection.getConnection();
					PreparedStatement ps = con.prepareStatement(sql1)) {
				ps.setString(1, foodGenre);
				ResultSet res = ps.executeQuery();
				if(res.next()) {
					int foodGenreId = res.getInt("food_genre_id");
					foodGenreIdList.add(foodGenreId);
				}
			}
		}
		if(foodGenreList.size() != 0) {
			for(int foodGenre : foodGenreIdList) {
				try(Connection con = MyConnection.getConnection();
					PreparedStatement ps = con.prepareStatement(sql2)) {
					ps.setInt(1, foodGenre);
					ps.setInt(2, userId);
					List<Food> foodList = new ArrayList<>();
					ResultSet res = ps.executeQuery();
					while(res.next()) {
						int foodId = res.getInt("food_id");
						String foodName = res.getString("food_name");
						String foodKana = res.getString("food_kana");
						String foodGenreName = res.getString("food_genre_name");
						String unit = res.getString("unit");
						String imagePath = res.getString("food_image");
						int quantity = res.getInt("quantity");
						Food food = new Food(foodId,foodName,foodKana,foodGenreName,unit,
								imagePath,quantity);
						foodList.add(food);
					}
					if(foodList.size() != 0) {
						refrigerator.add(foodList);
					}
				}
			}
		}else {
			throw new Exception();
		}
		return refrigerator;
	}
	/**
	 * マイ冷蔵庫に食材を追加処理
	 * @param userId ... ユーザID
	 * @param foodId ... 食材ID
	 * @param quantity ... 個数
	 * @return count ... 処理件数
	 * @throws Exception
	 */
	public int addFood(int userId, int foodId,int quantity) throws Exception{
		String sql = "UPDATE refrigerator SET quantity = quantity + ? "
				+ "WHERE user_id = ? AND food_id = ?";
		int count = 0;
		try(Connection con = MyConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, quantity);
			ps.setInt(2, userId);
			ps.setInt(3, foodId);
			count += ps.executeUpdate();
		}
		return count;
	}
	/**
	 * マイ冷蔵庫に新規食材を追加
	 * @param userId ... ユーザID
	 * @param foodId ... 食材Id
	 * @param quantity ... 個数
	 * @return count ... 処理件数
	 * @throws Exception
	 */
	public int addNewFood(int userId, int foodId, int quantity) throws Exception {
		String sql = "INSERT INTO refrigerator (user_id,food_id,quantity) VALUES (?,?,?)";
		int count = 0;
		try(Connection con = MyConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, userId);
			ps.setInt(2, foodId);
			ps.setInt(3, quantity);
			count += ps.executeUpdate();
		}
		return count;
	}
	public int[] countRefrigerator(int userId) throws Exception {
		String sql = "SELECT COUNT(food_id) AS food, SUM(quantity) AS quantity FROM refrigerator "
				+ "WHERE user_id = ?";
		int[] foodQuantity = new int[2];
		try(Connection con = MyConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, userId);
			ResultSet res2 = ps.executeQuery();
			if(res2.next()) {
				foodQuantity[0] = res2.getInt("food");
				foodQuantity[1] = res2.getInt("quantity");
			}
		}
		return foodQuantity;
	}
	
	public int newFood(String foodName, String foodKana, String foodGenre, String unit) throws Exception {
		int count = 0;
		
		String sql1 = "SELECT food_genre_id FROM food_genre WHERE food_genre_name = ?";
		String sql2 = "SELECT unit_id FROM unit WHERE unit = ?";
		String sql3 = "INSERT INTO food (food_name,food_kana,food_genre_id,unit_id) "
				+ "VALUES (?,?,?,?)";
		int foodGenreId = 0;
		int unitId = 0;
		
		try(Connection con = MyConnection.getConnection();
				PreparedStatement ps1 = con.prepareStatement(sql1);
				PreparedStatement ps2 = con.prepareStatement(sql2)) {
			ps1.setString(1,foodGenre);
			ResultSet res1 = ps1.executeQuery();
			if(res1.next()) {
				foodGenreId = res1.getInt("food_genre_id");
			}
			
			ps2.setString(1, unit);
			ResultSet res2 = ps2.executeQuery();
			if(res2.next()) {
				unitId = res2.getInt("unit_id");
			}
		}
		//食材ジャンルIdと単位IDが取得できた場合のみ、食材を登録
		if(foodGenreId != 0 && unitId != 0) {
			try(Connection con = MyConnection.getConnection();
					PreparedStatement ps = con.prepareStatement(sql3)) {
				ps.setString(1, foodName);
				ps.setString(2, foodKana);
				ps.setInt(3, foodGenreId);
				ps.setInt(4, unitId);
				count += ps.executeUpdate();
			}
		}
		
		return count;
	}
	public List<List<Food>> useFood(Map<Integer,String> map,List<List<Food>> refrigerator,
			int userId,List<String> foodGenre) throws Exception {
		int count = 0;
		String sql1 = "UPDATE refrigerator SET quantity = quantity - ? "
				+ "WHERE user_id = ? AND food_id = ?";
		String sql2 = "DELETE FROM refrigerator WHERE user_id = ? AND food_id = ?";
		
		for(List<Food> foodList : refrigerator) {
			for(Food food : foodList) {
				String quantityStr = map.get(food.getFoodId());
				if(quantityStr != null) {
					int quantity = Integer.parseInt(quantityStr);
					if(quantity == food.getQuantity()) {
						try(Connection con = MyConnection.getConnection();
								PreparedStatement ps = con.prepareStatement(sql2)) {
							ps.setInt(1, userId);
							ps.setInt(2, food.getFoodId());
							count += ps.executeUpdate();
						}
					}else {
						try(Connection con = MyConnection.getConnection();
								PreparedStatement ps = con.prepareStatement(sql1)) {
							ps.setInt(1, Integer.parseInt(quantityStr));
							ps.setInt(2, userId);
							ps.setInt(3, food.getFoodId());
							count += ps.executeUpdate();
						}
					}
				}
			}
		}
		List<List<Food>> refrigeratorList = new ArrayList<>();
		if(count > 0) {
			refrigeratorList = selectByUserId(foodGenre,userId);
		}else {
			refrigeratorList = null;
		}
		return refrigeratorList;
	}
	
	public int removeFoodByUserId(int userId, int foodId) throws Exception{
		int count = 0;
		String sql = "DELETE FROM refrigerator WHERE user_id = ? AND food_id = ?";
		try(Connection con = MyConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1,userId);
			ps.setInt(2, foodId);
			count += ps.executeUpdate();
		}
		return count;
	}
	
	public String deleteByUserId() {
		String sql = "DELETE FROM refrigerator WHERE user_id = ?";
		return sql;
	}
}
