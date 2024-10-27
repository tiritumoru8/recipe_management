package dto;

public class Food {
	private int foodId = 0;
	private String foodName = null;
	private String foodKana = null;
	private String foodGenre = null;
	private String unit = null;
	private String imagePath = null;
	private int quantity = 0;
	
	public Food() { }
	//全食材リスト
	public Food(int foodId, String foodName, String foodKana, String foodGenre, String unit,
			String imagePath) {
		this.foodId = foodId;
		this.foodName = foodName;
		this.foodKana = foodKana;
		this.foodGenre = foodGenre;
		this.unit = unit;
		this.imagePath = imagePath;
	}
	//マイ冷蔵庫の材料リスト用
	public Food(int foodId, String foodName, String foodKana, String foodGenre,
			String unit, String imagePath, int quantity) {
		this.foodId = foodId;
		this.foodName = foodName;
		this.foodKana = foodKana;
		this.foodGenre = foodGenre;
		this.unit = unit;
		this.imagePath = imagePath;
		this.quantity = quantity;
	}
	
	public int getFoodId() {
		return foodId;
	}
	public String getFoodName() {
		return foodName;
	}
	public String getFoodKana() {
		return foodKana;
	}
	public String getFoodGenre() {
		return foodGenre;
	}
	public String getUnit() {
		return unit;
	}
	public String getImagePath() {
		return imagePath;
	}
	public int getQuantity() {
		return quantity;
	}
	
	public void addQuantity(int add) {
		this.quantity += add;
	}
}
