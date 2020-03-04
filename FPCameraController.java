
package finalproject;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.Sys;

public class FPCameraController {
    //3d vector to store the camera's position in
    private Vector3f position = null;
    private Vector3f lPosition = null;
    
    //the rotation around the Y axis of the camera
    private float yaw = 0.0f;
    
    //the rotation around the X axis of the camera
    private float pitch = 0.0f;
    private Vector3Float me;

    public FPCameraController(float x, float y, float z){
        //instantiate position Vector3f to the x y z parameters
        position = new Vector3f(x,y,z);
        lPosition = new Vector3f(x,y,z);
        lPosition.x = 0f;
        lPosition.y = 15f;
        lPosition.z = 0f;
    }
    
    //increment the camera's current yaw rotation
    public void yaw(float amount){
        //increment the yaw by the amount parameter
        yaw += amount;
    }
    
    //increment the camera's current yaw rotation
    public void pitch(float amount){
        //increment the pitch by the amount parameter
        pitch -= amount;
    }
    
    //moves the camera foward relative to its current rotation(yaw)
    public void walkForward(float distance){
        float xOffset = distance * (float)Math.sin(Math.toRadians(yaw));
        float zOffset = distance * (float)Math.cos(Math.toRadians(yaw));
        position.x -= xOffset;
        position.z += zOffset;
    }
    
    //moves the camera backward relative to its current rotation(yaw)
    public void walkBackwards(float distance){
        float xOffset = distance * (float)Math.sin(Math.toRadians(yaw));
        float zOffset = distance * (float)Math.cos(Math.toRadians(yaw));
        position.x += xOffset;
        position.z -= zOffset;
    }   

    //strafes the camera left relative to its current rotation(yaw)
    public void strafeLeft(float distance){
        float xOffset = distance * (float)Math.sin(Math.toRadians(yaw-90));
        float zOffset = distance * (float)Math.cos(Math.toRadians(yaw-90));
        position.x -= xOffset;
        position.z += zOffset;
    }
    
    //strafes the camera right relative to its current rotation(yaw)
    public void strafeRight(float distance){
        float xOffset = distance * (float)Math.sin(Math.toRadians(yaw+90));
        float zOffset = distance * (float)Math.cos(Math.toRadians(yaw+90));
        position.x -= xOffset;
        position.z += zOffset;
    }
    
    //moves the camera up relative to its current rotation (yaw)
    public void moveUp(float distance){
        position.y -= distance;
    }
    
    //moves the camera down relative to its current rotation (yaw)
    public void moveDown(float distance){
        position.y += distance;
    }
    
    //translate and rotate the matrix so that it looks through the camera
    //this does basicall what gluLookAt() does
    public void lookThrough(){
        //rotate the pitch around the X axis
        glRotatef(pitch, 1.0f, 0.0f, 0.0f);
        //rotate the yaw around the Y axis
        glRotatef(yaw, 0.0f, 1.0f, 0.0f);
        //translate to the position vector's location
        glTranslatef(position.x, position.y, position.z);
    }
    
    public void gameLoop(){
        FPCameraController camera = new FPCameraController(0,0,0);
        float dx = 0.0f;
        float dy = 0.0f;
        //length of frame
        float dt = 0.0f; 
        //when the last frame was
        float lastTime = 0.0f;
        long time = 0;
        float mouseSensitivity = 0.09f;
        float movementSpeed = 0.35f;
        //hide the mouse
        Mouse.setGrabbed(true);
        
        //keep looping till the display window is closed the ESC key is down
        while(!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
            time = Sys.getTime();
            lastTime = time;
            //control camera yaw from x movement from the mouse
            camera.yaw(dx * mouseSensitivity);
            //control camera pitch from y movement from the mouse
            camera.pitch(dy * mouseSensitivity);
            //distance in mouse movement from the last getDX() call
            dx = Mouse.getDX();
            //distance in mouse movement from the last getDY() call
            dy = Mouse.getDY();
            
            /*when passing in the distance to move
                we times the movementSpeed with dt this is a time sacle
                so if its a slow frame u move more than a fast frame
                so on a slow computer you move just as fast as on a fast computer
            */
            if (Keyboard.isKeyDown(Keyboard.KEY_W)){
                //move forward
                camera.walkForward(movementSpeed);
            }
            if(Keyboard.isKeyDown(Keyboard.KEY_S)){
                //move backwards
                camera.walkBackwards(movementSpeed);
            }
            if(Keyboard.isKeyDown(Keyboard.KEY_A)){
                //strafe left
                camera.strafeLeft(movementSpeed);
            }
            if(Keyboard.isKeyDown(Keyboard.KEY_D)){
                //strafe right
                camera.strafeRight(movementSpeed);
            }
            if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
                //move up
                camera.moveUp(movementSpeed);
            }
            if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
                //move down
                camera.moveDown(movementSpeed);
            }
            
           //set the modelview matrix back to the identity
           glLoadIdentity();
           //look through the camera before you draw anything
           camera.lookThrough();
           glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
           //you would draw your scene here
           render();
           //draw the buffer to the screen
           Display.update();
           Display.sync(60);
        }
        Display.destroy();
    }
    
    private void render(){
        try{
            //Render the cube
            glBegin(GL_QUADS);
            //Top side of cube
            glColor3f(1.0f,0.0f,0.0f);//red           
            glVertex3f( 1.0f, 1.0f,-1.0f);        
            glVertex3f(-1.0f, 1.0f,-1.0f);        
            glVertex3f(-1.0f, 1.0f, 1.0f);
            glVertex3f( 1.0f, 1.0f, 1.0f);  
            //Back side of cube
            glColor3f(0.0f,1.0f,0.0f);//blue       
            glVertex3f( 1.0f,-1.0f, 1.0f);
            glVertex3f(-1.0f,-1.0f, 1.0f);
            glVertex3f(-1.0f,-1.0f,-1.0f);
            glVertex3f( 1.0f,-1.0f,-1.0f);
            //Bottom side of cube
            glColor3f(0.0f,0.0f,1.0f);//green
            glVertex3f( 1.0f, 1.0f, 1.0f);
            glVertex3f(-1.0f, 1.0f, 1.0f);
            glVertex3f(-1.0f,-1.0f, 1.0f);
            glVertex3f( 1.0f,-1.0f, 1.0f);
            //Front side of cube
            glColor3f(1.0f,1.0f,0.0f);//yellow
            glVertex3f( 1.0f,-1.0f,-1.0f);
            glVertex3f(-1.0f,-1.0f,-1.0f);
            glVertex3f(-1.0f, 1.0f,-1.0f);
            glVertex3f( 1.0f, 1.0f,-1.0f);
            //Left side of cube
            glColor3f(0.0f,1.0f,1.0f);//teal
            glVertex3f(-1.0f, 1.0f, 1.0f);
            glVertex3f(-1.0f, 1.0f,-1.0f);
            glVertex3f(-1.0f,-1.0f,-1.0f);
            glVertex3f(-1.0f,-1.0f, 1.0f);
            //Right side of cube
            glColor3f(1.0f,0.0f,1.0f);//pink
            glVertex3f( 1.0f, 1.0f,-1.0f);
            glVertex3f( 1.0f, 1.0f, 1.0f);
            glVertex3f( 1.0f,-1.0f, 1.0f);
            glVertex3f( 1.0f,-1.0f,-1.0f);
            
            glEnd();
        }catch(Exception e){           
        }
    }
}
