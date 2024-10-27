package dto;

public class Account {
	private int userId = 0;
	private String userName;
	private String email;
	private String password;
	private int favoriteCount = 0;
	private int myRecipeCount = 0;
	private int kindOfFood = 0;
	private int foodQuantity = 0;
	
	//空のコントラクタ
	public Account() { }
	
	//name,email,passwordフィールド設定
	public Account(String userName, String email, String password) {
		this.userName = userName;
		this.email = email;
		this.password = password;
	}
	
	//getter
	public int getUserId() {
		return userId;
	}
	public String getUserName() {
		return userName;
	}
	public String getEmail() {
		return email;
	}
	public String getPassword() {
		return password;
	}
	public int getFavoriteCount() {
		return favoriteCount;
	}
	public int getMyRecipeCount() {
		return myRecipeCount;
	}
	public int getKindOfFood() {
		return kindOfFood;
	}
	public int getFoodQuantity() {
		return foodQuantity;
	}
	//setter
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setFavoriteCount(int favoriteCount) {
		this.favoriteCount = favoriteCount;
	}
	public void setMyRecipeCount(int myRecipeCount) {
		this.myRecipeCount = myRecipeCount;
	}
	public void setKindOfFood(int kindOfFood) {
		this.kindOfFood = kindOfFood;
	}
	public void setFoodQuantity(int foodQuantity) {
		this.foodQuantity = foodQuantity;
	}
}
