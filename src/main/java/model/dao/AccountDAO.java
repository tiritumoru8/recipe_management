package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.http.HttpSession;

import dto.Account;

public class AccountDAO {
	private FoodDAO foodDao = new FoodDAO();
	private RecipeDAO recipeDao = new RecipeDAO();
	
	public Account findAccount(String email, String hashPass) throws Exception{
		Account account = new Account();
		//存在チェック用
		String sql = "SELECT p.user_id,u.user_name FROM password p JOIN user u ON p.user_id = u.user_id "
				+ "WHERE email = ? AND password = ?";
		try(Connection con = MyConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, email);
			ps.setString(2, hashPass);
			ResultSet res = ps.executeQuery();
			while(res.next()) {
				account.setUserId(res.getInt("user_id"));
				account.setUserName(res.getString("user_name"));
				account.setEmail(email);
				account.setPassword(hashPass);
			}
		}
		return account;
	}
	public Account getAddResource(Account account) throws Exception{
		int userId = account.getUserId();

		int countMyRecipe = recipeDao.countMyRecipe(userId);
		int[] foodQuantity = foodDao.countRefrigerator(userId);
		int favoriteCount = recipeDao.countFavoriteRecipe(userId);
		account.setMyRecipeCount(countMyRecipe);
		account.setKindOfFood(foodQuantity[0]);
		account.setFoodQuantity(foodQuantity[1]);
		account.setFavoriteCount(favoriteCount);
		
		return account;
	}
	/**
	 * 新規登録
	 * @param session ... セッションスコープ
	 * @param userId ... ユーザID
	 * @param userName ... ユーザ名
	 * @param email ... メールアドレス
	 * @param password ... パスワード
	 * @return count ... 処理件数
	 * @throws Exception
	 */
	public int newAccount(HttpSession session, int userId, String userName, String email,
			String password) throws Exception{
		int count = 0;
		
		String sql1 = "INSERT INTO user (user_name) VALUES (?)";
		String sql2 = "SELECT user_id FROM user WHERE user_name = ?";
		String sql3 = "INSERT INTO password (user_id,email, password) VALUES (?,?,?)";
		try(Connection con = MyConnection.getConnection()) {
			con.setAutoCommit(false);
			try(PreparedStatement ps1 = con.prepareStatement(sql1);
					PreparedStatement ps2 = con.prepareStatement(sql2);
					PreparedStatement ps3 = con.prepareStatement(sql3)) {
				ps1.setString(1, userName);
				count += ps1.executeUpdate();
				
				ps2.setString(1, userName);
				ResultSet res = ps2.executeQuery();
				while(res.next()) {
					userId = res.getInt("user_id");
				}
				
				ps3.setInt(1, userId);
				ps3.setString(2, email);
				ps3.setString(3, password);
				count += ps3.executeUpdate();
				con.commit();
			}catch(Exception e) {
				con.rollback();
				throw new Exception();
			}
		}
		//新規登録情報をセッションスコープへ保管
		Account account = new Account();
		account.setUserId(userId);
		account.setUserName(userName);
		account.setEmail(email);
		account.setPassword(password);
		session.setAttribute("account", account);
		
		return count;
	}
	/**
	 * 全て変更
	 * @param session ... セッションスコープ
	 * @param userId ... ユーザID
	 * @param userName ... ユーザ名
	 * @param email ... メールアドレス
	 * @param password ... パスワード
	 * @return count ... 処理件数
	 * @throws Exception
	 */
	public int insertAll(HttpSession session, int userId, String userName, String email,
			String password) throws Exception{
		int count = 0;
		String sql1 = "INSERT INTO user (user_name) VALUES (?) WHERE user_id = ?";
		String sql2 = "INSERT INTO password (email, password) VALUES (?,?) WHERE user_id = ?";
		
		try(Connection con = MyConnection.getConnection()) {
			con.setAutoCommit(false);
			try(PreparedStatement ps1 = con.prepareStatement(sql1);
					PreparedStatement ps2 = con.prepareStatement(sql2)) {
				ps1.setInt(2, userId);
				ps1.setString(1, userName);
				count += ps1.executeUpdate();
				
				ps2.setInt(3, userId);
				ps2.setString(1, email);
				ps2.setString(2, password);
				count += ps2.executeUpdate();
				
				con.commit();
			}catch(Exception e) {
				con.rollback();
			}
		}
		
		Account account = (Account) session.getAttribute("account");
		account.setUserName(userName);
		account.setEmail(email);
		account.setPassword(password);
		session.setAttribute("account", account);
		
		return count;
	}
	/**
	 * ユーザ名、メールアドレス変更
	 * @param session ... セッションスコープ
	 * @param userId ... ユーザID
	 * @param userName ... ユーザ名
	 * @param email ... メールアドレス
	 * @return count ... 処理件数
	 * @throws Exception
	 */
	public int insertNameEmail(HttpSession session,  int userId, String userName,
			String email) throws Exception{
		int count = 0;
		String sql1 = "INSERT INTO user (user_name) "
				+ "VALUES (?) WHERE user_id = ?";
		String sql2 = "INSERT INTO password (email) "
				+ "VALUES (?) WHERE user_id = ?";
		try(Connection con = MyConnection.getConnection()) {
			con.setAutoCommit(false);
			try(PreparedStatement ps1 = con.prepareStatement(sql1);
					PreparedStatement ps2 = con.prepareStatement(sql2)) {
				ps1.setInt(2, userId);
				ps1.setString(1, userName);
				count += ps1.executeUpdate();
				
				ps2.setInt(2, userId);
				ps2.setString(1, email);
				count += ps2.executeUpdate();
				con.commit();
			}catch(Exception e) {
				con.rollback();
			}
		}
		
		Account account = (Account) session.getAttribute("account");
		account.setUserName(userName);
		account.setEmail(email);
		session.setAttribute("account", account);
		
		return count;
	}
	/**
	 * ユーザ名、パスワード変更
	 * @param session ... セッションスコープ
	 * @param userId ... ユーザID
	 * @param userName ... ユーザ名
	 * @param password ... パスワード
	 * @return count ... 処理件数
	 * @throws Exception
	 */
	public int insertNamePass(HttpSession session, int userId, String userName,
			String password) throws Exception{
		int count = 0;
		String sql1 = "INSERT INTO user (user_name) "
				+ "VALUES (?) WHERE user_id = ?";
		String sql2 = "INSERT INTO password (password) "
				+ "VALUES (?) WHERE user_id = ?";
		try(Connection con = MyConnection.getConnection()) {
			con.setAutoCommit(false);
			try(PreparedStatement ps1 = con.prepareStatement(sql1);
					PreparedStatement ps2 = con.prepareStatement(sql2)) {
				ps1.setInt(2, userId);
				ps1.setString(1, userName);
				count += ps1.executeUpdate();
				
				ps2.setInt(2, userId);
				ps2.setString(1, password);
				count += ps2.executeUpdate();
				con.commit();
			}catch(Exception e) {
				con.rollback();
			}
		}
		
		Account account = (Account) session.getAttribute("account");
		account.setUserName(userName);
		account.setPassword(password);
		session.setAttribute("account", account);
		
		return count;
	}
	/**
	 * メールアドレス、パスワード変更
	 * @param session ... セッションスコープ
	 * @param userId ... ユーザID
	 * @param email ... メールアドレス
	 * @param password ... パスワード
	 * @return count ... 処理件数
	 * @throws Exception
	 */
	public int insertEmailPass(HttpSession session, int userId, String email,
			String password) throws Exception{
		int count = 0;
		String sql = "INSERT INTO password (email, password) VALUES (?,?) WHERE user_id = ?";
		try(Connection con = MyConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(3, userId);
			ps.setString(1, email);
			ps.setString(2, password);
			count += ps.executeUpdate();
		}
		
		Account account = (Account) session.getAttribute("account");
		account.setEmail(email);
		account.setPassword(password);
		session.setAttribute("account", account);
		
		return count;
	}
	/**
	 * ユーザ名変更
	 * @param session ... セッションスコープ
	 * @param userId ... ユーザID
	 * @param userName ... ユーザ名
	 * @return count ... 処理件数
	 * @throws Exception
	 */
	public int insertUserName(HttpSession session, int userId, String userName) throws Exception{
		int count = 0;
		String sql = "INSERT INTO user (user_name) "
				+ "VALUES (?) WHERE user_id = ?";
		try(Connection con = MyConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(2, userId);
			ps.setString(1, userName);
			count += ps.executeUpdate();
		}
		Account account = (Account) session.getAttribute("account");
		account.setUserName(userName);
		session.setAttribute("account", account);
		
		return count;
	}
	/**
	 * メールアドレス変更
	 * @param session ... セッションスコープ
	 * @param userId ... ユーザID
	 * @param email ... メールアドレス
	 * @return count ... 処理件数
	 * @throws Exception
	 */
	public int insertEmail(HttpSession session, int userId, String email) throws Exception{
		int count = 0;
		String sql = "INSERT INTO password (email) "
				+ "VALUES (?) WHERE user_id = ?";
		try(Connection con = MyConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(2, userId);
			ps.setString(1, email);
			count += ps.executeUpdate();
		}
		
		Account account = (Account) session.getAttribute("account");
		account.setEmail(email);
		session.setAttribute("account", account);
		
		return count;
	}
	/**
	 * パスワード変更
	 * @param userId ... ユーザID
	 * @param password ... パスワード
	 * @return count ... 処理件数
	 * @throws Exception
	 */
	public int insertPassword(HttpSession session, int userId, String password) throws Exception{
		int count = 0;
		String sql = "INSERT INTO password (password) "
				+ "VALUES (?) WHERE user_id = ?";
		try(Connection con = MyConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(2, userId);
			ps.setString(1, password);
			count += ps.executeUpdate();
		}
		
		Account account = (Account) session.getAttribute("account");
		account.setPassword(password);
		session.setAttribute("account", account);
		
		return count;
	}
	/**
	 * アカウント情報を一括削除
	 * @param userId ... ユーザID
	 * @return 処理結果
	 * @throws Exception
	 */
	public int delete(int userId) throws Exception {
		int count = 0;
		String sql1 = "DELETE FROM password WHERE user_id = ?";
		String sql2 = "DELETE FROM user WHERE user_id = ?";
		//マイレシピ情報を削除するsql文取得
		RecipeDAO recipeDao = new RecipeDAO();
		String[] sqlList = recipeDao.deleteByUserId();
		String sql3 = sqlList[0];
		String sql4 = sqlList[1];
		//マイ冷蔵庫情報を削除するsql文取得
		String sql5 = foodDao.deleteByUserId();
		
		//DBからアカウント削除(table name = user : password)
		try(Connection con = MyConnection.getConnection()) {
			try(PreparedStatement ps1 = con.prepareStatement(sql1);
				PreparedStatement ps2 = con.prepareStatement(sql2);
				PreparedStatement ps3 = con.prepareStatement(sql3);
				PreparedStatement ps4 = con.prepareStatement(sql4);
				PreparedStatement ps5 = con.prepareStatement(sql5)) {
				//user,passwordテーブル両方のアカウント情報を削除できたらコミット実行
				con.setAutoCommit(false);
				//ユーザ情報削除
				ps1.setInt(1,userId);
				count += ps1.executeUpdate();
				ps2.setInt(1, userId);
				count += ps2.executeUpdate();
				//マイレシピ情報削除
				ps3.setInt(1, userId);
				count += ps3.executeUpdate();
				ps4.setInt(1, userId);
				count += ps4.executeUpdate();
				//マイ冷蔵庫情報削除
				ps5.setInt(1, userId);
				count += ps5.executeUpdate();
				
				con.commit();
			}catch(Exception e) {
				con.rollback();
			}
		}
		return count;
	}
}
