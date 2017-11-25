package entities;

import models.TexturedModel;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;
import terrains.Terrain;

public class FirstPersonCameraPlayer extends Entity {

    private static final float RUN_SPEED = 100;
    private static final float TURN_SPEED = 160;
    private static final float GRAVITY = -50;
    private static final float JUMP_POWER = 30f;

    private static final float TERRAIN_HEIGHT = 0;

    private float currentSpeed = 0;
    private float currentSpeed1 = 0;
    private float upwardsSpeed = 0;

    private boolean isInAir = false;

    public FirstPersonCameraPlayer(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        super(model, position, rotX, rotY, rotZ, scale);
    }

    float dx;
    float dz;

    float dx1;
    float dz1;
    public void move(Terrain terrain) {
        checkInputs();
        float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
        float distance1 = currentSpeed1 * DisplayManager.getFrameTimeSeconds();

        dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
        dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
        dx1 = (float) (distance1 * Math.cos(-Math.toRadians(super.getRotY())));
        dz1 = (float) (distance1 * Math.sin(-Math.toRadians(super.getRotY())));
        upwardsSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
        super.increasePosition(dx,upwardsSpeed * DisplayManager.getFrameTimeSeconds(),dz);
        super.increasePosition(dx1,upwardsSpeed * DisplayManager.getFrameTimeSeconds(),dz1);
        float terrainHeight = terrain.getHeightOfTerrain(super.getPosition().x, super.getPosition().z);

        if(super.getPosition().y < terrainHeight) {
            upwardsSpeed = 0f;
            isInAir = false;
            super.getPosition().y = terrainHeight;
        }


    }

    private void jump() {
        if(!isInAir) {
            this.upwardsSpeed = JUMP_POWER;
            isInAir = true;
        }
    }

    private void checkInputs() {
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            this.currentSpeed = RUN_SPEED;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            this.currentSpeed = -RUN_SPEED;
        } else {
            this.currentSpeed = 0;
        }


        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            this.currentSpeed1 = RUN_SPEED;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            this.currentSpeed1 = -RUN_SPEED;
        } else {
            this.currentSpeed1 = 0;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            jump();
        }
    }
}
