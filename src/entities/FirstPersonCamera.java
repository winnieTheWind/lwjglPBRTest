package entities;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;

public class FirstPersonCamera {

	private Vector3f position = new Vector3f(0, 5f, 0);
	private float pitch = 0;
	private float yaw = 0;
	private float roll;

	private float sensitivity = 0.9f;
	private float viewChange = 1.1f;
	private int offset = 15;

	private FirstPersonCameraPlayer player;

	public FirstPersonCamera(FirstPersonCameraPlayer player) {
		this.player = player;
	}

	public void movement() {
		calculateCameraPosition();
		yaw = (float) (180 - player.getRotY());

		mouseMovements();
	}

	public void invertPitch() {
		pitch = -pitch;
	}


	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}

	public Vector3f getPosition() {
		return position;
	}

	private void mouseMovements() {
		Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);
		player.increaseRotation(0, -Mouse.getDX() * sensitivity * DisplayManager.getFrameTimeSeconds(), 0);
		pitch -= Mouse.getDY() * sensitivity * DisplayManager.getFrameTimeSeconds();
		Mouse.setGrabbed(true);
	}

	private void calculateCameraPosition() {
		position.x = player.getPosition().x;
		position.z = player.getPosition().z;
		position.y = player.getPosition().y + 10;
	}
}