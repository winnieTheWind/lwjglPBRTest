package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import entities.*;
import guis.GuiRenderer;
import guis.GuiTexture;
import models.RawModel;
import models.TexturedModel;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

import static org.lwjgl.input.Keyboard.isKeyDown;

//INPUT 10/11/2017
////////////////////////////
//MOVEMENT
//W to move camera forward
//S to move camera backward
//A to move camera left
//D to move camera right
////////////////////////////
//LIGHTING
//P to increase sunlight
//O to decrease sunlight
////////////////////////////

public class MainGameLoop {
	private static FirstPersonCamera firstPersonCamera;
	private static FirstPersonCameraPlayer firstPersonCameraPlayer;
	private static ThirdPersonCamera thirdPersonCamera;
	private static ThirdPersonCameraPlayer thirdPersonCameraPlayer;
	private static FieldCamera fieldCamera;
	private static Entity thirdPersonPlayerEntity;
	private static Light light;
	static float brightnessX, brightnessY, brightnessZ;

	//Trees Vector Position ex 1
	private static float xPos, zPos, yPos;
	private static Vector3f v = new Vector3f(xPos,0,zPos);

	public static void main(String[] args) {

		DisplayManager.createDisplay();
		Loader loader = new Loader();

		//Default light brightness
		brightnessX = 1;
		brightnessY = 1;
		brightnessZ = 1;

		//Terrains
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grass"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("grass"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grass"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("grass"));

		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));

		TerrainTexture backgroundTexture_1 = new TerrainTexture(loader.loadTexture("grass"));
		TerrainTexture rTexture_1 = new TerrainTexture(loader.loadTexture("grass"));
		TerrainTexture gTexture_1 = new TerrainTexture(loader.loadTexture("grass"));
		TerrainTexture bTexture_1 = new TerrainTexture(loader.loadTexture("grass"));

		TerrainTexturePack texturePack_1 = new TerrainTexturePack(backgroundTexture_1, rTexture_1, gTexture_1, bTexture_1);
		TerrainTexture blendMap_1 = new TerrainTexture(loader.loadTexture("blendMap2"));

		Terrain terrain = new Terrain(-1,-1,loader,texturePack, blendMap,"heightmaptest");
		Terrain terrain2 = new Terrain(0,-1,loader,texturePack_1, blendMap,"heightmaptest");
		Terrain terrain3 = new Terrain(0,0,loader,texturePack_1, blendMap_1,"heightmaptest");
		Terrain terrain4 = new Terrain(-1,0,loader,texturePack_1, blendMap,"heightmaptest");

		//Player Model
		RawModel playerModel = OBJLoader.loadObjModel("bunny", loader);
		TexturedModel playerTexturedModel = new TexturedModel(playerModel,new ModelTexture(loader.loadTexture("green")));
		playerTexturedModel.getTexture().setReflectivity(0.1F);
		playerTexturedModel.getTexture().setShineDamper(0.1F);

		//Camera Models
		RawModel cameraModel = OBJLoader.loadObjModel("cameraModel", loader);
		TexturedModel cameraTexturedModel = new TexturedModel(cameraModel,new ModelTexture(loader.loadTexture("green")));

		//Tree Models
		RawModel treeWoodModel = OBJLoader.loadObjModel("TreeWood", loader);
		ModelTexture treeTexture = new ModelTexture(loader.loadTexture("bark_0021"));
		TexturedModel treeWoodStaticModel = new TexturedModel(treeWoodModel,treeTexture);
		treeTexture.setNumberOfRows(2);

		RawModel treeLeavesModel = OBJLoader.loadObjModel("TreeLeaves", loader);
		TexturedModel treeLeavesStaticModel = new TexturedModel(treeLeavesModel,new ModelTexture(loader.loadTexture("DB2X2_L01")));

		//Fern Model
		RawModel fernModel = OBJLoader.loadObjModel("fern2", loader);
		ModelTexture fernTextureAtlas = new ModelTexture(loader.loadTexture("fern"));
		TexturedModel fernStaticModel = new TexturedModel(fernModel,fernTextureAtlas);
		fernTextureAtlas.setHasTransparency(true);
		fernTextureAtlas.setUseFakeLighting(true);
		fernTextureAtlas.setNumberOfRows(2);

		//Dragon Model
		RawModel dragonModel = OBJLoader.loadObjModel("dragon", loader);
		TexturedModel dragonStaticModel = new TexturedModel(dragonModel,new ModelTexture(loader.loadTexture("green")));

		//Symbol Models

		RawModel symbolModel1 = OBJLoader.loadObjModel("symbol2", loader);
		TexturedModel symbolStaticModel1 = new TexturedModel(symbolModel1,new ModelTexture(loader.loadTexture("symbol2")));
		symbolStaticModel1.getTexture().setHasTransparency(true);
		symbolStaticModel1.getTexture().setUseFakeLighting(true);

		RawModel symbolModel2 = OBJLoader.loadObjModel("symbol", loader);
		TexturedModel symbolStaticModel2 = new TexturedModel(symbolModel2,new ModelTexture(loader.loadTexture("symbol")));
		symbolStaticModel2.getTexture().setHasTransparency(true);
		symbolStaticModel2.getTexture().setUseFakeLighting(true);

		//Trees Vector Position ex 2
		List<Entity> entities = new ArrayList<>();
		Random random = new Random();
		//Tree Loop
		for(int i=0;i<300;i++){
			xPos = 80 + random.nextFloat() * -155;
			zPos = 700 + random.nextFloat() * -1500;
			yPos = terrain.getHeightOfTerrain(xPos,zPos);

			float size = random.nextFloat()*100;
			entities.add(new Entity(treeWoodStaticModel, random.nextInt(4),
					new Vector3f(xPos*10,yPos-5f,zPos),0,0,0,size));
			entities.add(new Entity(treeLeavesStaticModel,
					new Vector3f(xPos*10,yPos,zPos),0,random.nextFloat() * 360,0,size));

		}
		for(int i=0;i<100;i++){
			xPos = random.nextFloat()*800 - 400;
			zPos = random.nextFloat() * -600;
			yPos = terrain.getHeightOfTerrain(xPos,zPos);
			entities.add(new Entity(fernStaticModel, random.nextInt(4),
					new Vector3f(xPos,-1f+yPos,zPos),0,0,0,1.5f));
		}

		//MasterRenderer
		MasterRenderer renderer = new MasterRenderer(loader);

		//Props
		Entity treeWoodEntity = new Entity(treeWoodStaticModel, new Vector3f(24,0,-100),0,0,0,2);
		Entity treeLeaveEntity = new Entity(treeLeavesStaticModel, new Vector3f(24,0,-100),0,0,0,2);

		//Props Symbols
		Entity symbolEntity1 = new Entity(symbolStaticModel1, new Vector3f(0,3,-100),0,0,0,2);
		Entity symbolEntity2 = new Entity(symbolStaticModel2, new Vector3f(15,1,-100),0,0,0,2);

		//Initialize first and third Person Player instances
		firstPersonCameraPlayer = new FirstPersonCameraPlayer(cameraTexturedModel, new Vector3f(-500, 0, -500), 0,0,0,2);
		thirdPersonCameraPlayer = new ThirdPersonCameraPlayer(playerTexturedModel, new Vector3f(-500, 0, -500), 0,0,0,2);

		firstPersonCamera = new FirstPersonCamera(firstPersonCameraPlayer);
		thirdPersonCamera = new ThirdPersonCamera(thirdPersonCameraPlayer);

		//Initialize field camera - not in use
		fieldCamera = new FieldCamera();

		List<GuiTexture> guis = new ArrayList<GuiTexture>();
		GuiTexture gui = new GuiTexture(loader.loadTexture("symbol2"), new Vector2f(0.75f, -0.7f), new Vector2f(0.25f, 0.25f));
		GuiTexture gui2 = new GuiTexture(loader.loadTexture("texttest1"), new Vector2f(0.80f, 0.85f), new Vector2f(0.25f, 0.55f));
		guis.add(gui);
		guis.add(gui2);

		GuiRenderer guiRenderer = new GuiRenderer(loader);

		while(!Display.isCloseRequested()) {
			//let there be light
			List<Light> lights = new ArrayList<Light>();
			lights.add(new Light(new Vector3f(0,1000,-7000),new Vector3f(brightnessX,brightnessY,brightnessZ)));
			lights.add(new Light(new Vector3f(-500,10,-500),new Vector3f(2,0,0), new Vector3f(1,0.01f,0.002f)));
			lights.add(new Light(new Vector3f(370,17,-300),new Vector3f(0,2,2), new Vector3f(1,0.01f,0.002f)));
			lights.add(new Light(new Vector3f(293,7,-305), new Vector3f(2,2,0), new Vector3f(1,0.01f,0.002f)));

			//hold q to trigger third person camera on thirdPersonPlayer
			if (isKeyDown(Keyboard.KEY_Q)) {
				thirdPersonCamera.move();
				thirdPersonCameraPlayer.move(terrain);
				renderer.render(lights, thirdPersonCamera);
				renderer.processEntity(firstPersonCameraPlayer);
				renderer.processEntity(thirdPersonCameraPlayer);

			} else {
				firstPersonCamera.movement();
				firstPersonCameraPlayer.move(terrain);
				renderer.render(lights, firstPersonCamera);
				renderer.processEntity(thirdPersonCameraPlayer);
			}
			guiRenderer.render(guis);
			// change sun brightness
			if (isKeyDown(Keyboard.KEY_P)) {
				brightnessX = brightnessX + 0.01f;
				brightnessY = brightnessY + 0.01f;
				brightnessZ = brightnessZ + 0.01f;
			}

			if (isKeyDown(Keyboard.KEY_O)) {
				brightnessX = brightnessX - 0.01f;
				brightnessY = brightnessY - 0.01f;
				brightnessZ = brightnessZ - 0.01f;
			}
			//auxilary movement
			symbolEntity1.increaseRotation(0, 1, 0);
			//render everything
			renderer.processTerrain(terrain);
			renderer.processTerrain(terrain2);
			renderer.processTerrain(terrain3);
			renderer.processTerrain(terrain4);

			for (Entity entity : entities) {
				renderer.processEntity(entity);
			}

			renderer.processEntity(symbolEntity1);
			renderer.processEntity(symbolEntity2);
			renderer.processEntity(treeWoodEntity);
			renderer.processEntity(treeLeaveEntity);



				DisplayManager.updateDisplay();
				}
				guiRenderer.cleanUp();
				renderer.cleanUp();
				loader.cleanUp();
				DisplayManager.closeDisplay();

			}

}
