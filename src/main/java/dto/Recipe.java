package dto;

public class Recipe {
	private int recipeId;
	private String recipeName;
	private String recipeKana;
	private String recipeGenre;
	private String imagePath;
	private String foodList;
	private String process;
	
	public Recipe() { }
	public Recipe(String recipeName, String recipeKana, String recipeGenre,String imagePath,
			String foodList, String process) {
		this.recipeName = recipeName;
		this.recipeKana = recipeKana;
		this.recipeGenre = recipeGenre;
		this.imagePath = imagePath;
		this.foodList = foodList;
		this.process = process;
	}
	public Recipe(int recipeId, String recipeName, String recipeKana, String recipeGenre,
			String foodList, String process) {
		this.recipeId = recipeId;
		this.recipeName = recipeName;
		this.recipeKana = recipeKana;
		this.recipeGenre = recipeGenre;
		this.foodList = foodList;
		this.process = process;
	}
	public Recipe(int recipeId, String recipeName, String recipeKana,String recipeGenre,
			String imagePath, String foodList, String process) {
		this.recipeId = recipeId;
		this.recipeName = recipeName;
		this.recipeKana = recipeKana;
		this.recipeGenre = recipeGenre;
		this.imagePath = imagePath;
		this.foodList = foodList;
		this.process = process;
	}
	
	public int getRecipeId() {
		return recipeId;
	}
	public String getRecipeName() {
		return recipeName;
	}
	public String getRecipeKana() {
		return recipeKana;
	}
	public String getRecipeGenre() {
		return recipeGenre;
	}
	public String getImagePath() {
		return imagePath;
	}
	public String getFoodList() {
		return foodList;
	}
	public String getProcess() {
		return process;
	}
}
