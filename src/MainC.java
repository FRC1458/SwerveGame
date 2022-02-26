import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.lang.Math;

import javax.swing.JFrame;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
// State machine
public class MainC {
	enum States {
		DETECT_BALL,
		GO_TO_BALL,
		PICK_UP_BALL,
		FACE_HUB,
		GO_TO_HUB,
		DROP_BALL;
	}
	byte[] keys = {0,0,0,0,0,0,0}; // WSADLR
	boolean timing = false;
	boolean reset = false;
	long time;
	int highScore = 0;
	GUI gui;
	SwerveGui swerve;
    Component stickX1 = null;
	Component stickY1 = null;
    Component stickX2 = null;
    Component stickY2 = null;
    Component startBtn = null;
	float [] joys = new float[4];
	boolean ballFound = false;
	boolean reachBall = false;


	int cntr;
	Camera cam;
	States state;
	
	public static void main (String args[]){
		MainC m = new MainC(args);
	}

	public MainC(String args[]){

		cntr = 0;
		Controller[] ca = ControllerEnvironment.getDefaultEnvironment().getControllers();
		Controller xbox = null;
		Controller stick1 = null;
		Controller stick2 = null;
		Controller.Type type = null;
		state = States.DETECT_BALL;
        for(int i =0;i<ca.length;i++){
        	if(ca[i].getType() == Controller.Type.GAMEPAD){
        		xbox = ca[i];
        		stickX1 = xbox.getComponent(Component.Identifier.Axis.X);
        		stickY1 = xbox.getComponent(Component.Identifier.Axis.Y);
        		stickX2 = xbox.getComponent(Component.Identifier.Axis.RX);
        		stickY2 = xbox.getComponent(Component.Identifier.Axis.RY);
        		type = Controller.Type.GAMEPAD;
        		startBtn = xbox.getComponent(Component.Identifier.Button.A);
            	break;
        	} else if (ca[i].getType() == Controller.Type.STICK) {
        		if(stick1 == null){
        			stick1 = ca[i];
        		} else {
            		stick2 = ca[i];
            		stickX1 = stick1.getComponent(Component.Identifier.Axis.X);
            		stickY1 = stick1.getComponent(Component.Identifier.Axis.Y);
            		stickX2 = stick2.getComponent(Component.Identifier.Axis.X);
            		stickY2 = stick2.getComponent(Component.Identifier.Axis.Y);
            		type = Controller.Type.STICK;
            		startBtn = stick2.getComponent(Component.Identifier.Button._1);
            		break;
        		}
        	}
        }
        
		gui = new GUI(type);
		swerve = new SwerveGui();
		cam = new Camera(gui.SwerveGui());
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gui.pack();
		gui.setTitle("Pwnage Swerve Game");
		gui.setLocationRelativeTo(null);
		gui.setVisible(true);
		gui.requestFocusInWindow();
        gui.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e) {
			    switch (e.getKeyCode()){
		        case KeyEvent.VK_W:		keys[0] = 1;	break;
		        case KeyEvent.VK_S:		keys[1] = 1;	break;
		        case KeyEvent.VK_A:		keys[2] = 1;	break;
		        case KeyEvent.VK_D:		keys[3] = 1;	break;
		        case KeyEvent.VK_LEFT:	keys[4] = 1;	break;
		        case KeyEvent.VK_RIGHT:	keys[5] = 1;	break;
				case KeyEvent.VK_SPACE:	reset = true;	break;
				case KeyEvent.VK_ENTER: keys[6] = 1; 	break;
			    }
		        e.consume();
		    }
			public void keyReleased(KeyEvent e) {
			    switch (e.getKeyCode()){
		        case KeyEvent.VK_W:		keys[0] = 0;	break;
		        case KeyEvent.VK_S:		keys[1] = 0;	break;
		        case KeyEvent.VK_A:		keys[2] = 0;	break;
		        case KeyEvent.VK_D:		keys[3] = 0;	break;
		        case KeyEvent.VK_LEFT:	keys[4] = 0;	break;
				case KeyEvent.VK_RIGHT:	keys[5] = 0;	break;
				case KeyEvent.VK_ENTER: keys[6] = 0; 	break;
			    }
		        e.consume();
			}
		});
        long s = System.currentTimeMillis();
        while(true){
        	if(System.currentTimeMillis()-s > 10){
        		type = gui.type;
        		if(type == Controller.Type.GAMEPAD){
        			if(xbox.poll()){
	            		joysticks();
            		}else{
            			type = null;
            			gui.type = null;
            		}
        		}else if(type == Controller.Type.STICK || type == Controller.Type.GAMEPAD){
        			if(stick1.poll() && stick2.poll()){
	            		joysticks();
            		}else{
            			type = null;
            			gui.type = null;
            		}
        		}else{
					//if(keys[6]) {
					//	GUI.my.setBall();
					//}

					/*
					double keybaardSpeed = 0.8;
        			joys[0] = (float) (keyboardSpeed*(keys[1] - keys[0]));
        			joys[1] = (float) (keyboardSpeed*(keys[3] - keys[2]));
        			joys[2] = 0;
        			joys[3] = (float) (keyboardSpeed*(keys[5] - keys[4]));
					gui.Drive(joys);
					*/
					randomwalk();

        			timer(reset);
        		}
	            s = System.currentTimeMillis();
        	}
        }
	}
	
	// State machine
	public void randomwalk() {
		double rotationAngle = cam.angle();
		switch (state) {
			case DETECT_BALL:
				detectBall();
				break;
			case GO_TO_BALL:
				goToBall();
				break;
			case PICK_UP_BALL:
				pickUpBall();
				break;
			case FACE_HUB:
				faceHub();
				break;
			case GO_TO_HUB:
				goToHub();
				break;
			case DROP_BALL:
				dropBall();
				break;		
			}
		
		// Crash into small wall that can crawl on a call in a waterfall (DON'T DELETE VERY IMPORTANT!!!!!!!!!!!!!!)
	}

	void joysticks(){
		float [] prevJoys = new float[4];
		double k = .8;
		double deadband = .1;
		timer(startBtn.getPollData()==1);
		joys[0] = (Math.abs(stickX1.getPollData()) > deadband)? stickX1.getPollData() : 0;
		joys[1] = (Math.abs(stickY1.getPollData()) > deadband)? stickY1.getPollData() : 0;
		joys[2] = (Math.abs(stickX2.getPollData()) > deadband)? stickX2.getPollData() : 0;
		joys[3] = (Math.abs(stickY2.getPollData()) > deadband)? stickY2.getPollData() : 0;
		for(int i = 0; i<4; i++){
			prevJoys[i] = joys[i];
			joys[i] = (float)(k*joys[i]+(1-k)*prevJoys[i]);
		}
		gui.Drive(joys);
	}
	
	void timer(boolean startBtn){ 
		if(startBtn)
		{
			gui.resetPoints();
			reset = false;
	    	timing = true;
	    	time = System.currentTimeMillis();
		}
		if(timing){
			if(System.currentTimeMillis()-time > 20000){
				timing = false;
				int points = gui.getPoints();
				if(points > highScore){
					highScore = points;
				}
			}
		}
	}
	public void dropBall() {
		joys[0] = (float) 0;
		joys[1] = (float) 0;
		joys[2] = (float) 0;
		joys[3] = (float) 0;
	}
	// Makes robot drive to hub
	public void goToHub() {
		joys[0] = (float) 0;
		joys[1] = (float) 0;
		joys[2] = (float) 0;
		joys[3] = (float) 0;
		double hubVectorMag = cam.hubVector()[2];
		joys[3] = (float) 0;
		double[] vectorOfHub = cam.hubVector();
		joys[0] = (float) (vectorOfHub[1]/vectorOfHub[2]);
		joys[1] = (float) (vectorOfHub[0]/vectorOfHub[2]);
		gui.Drive(joys);
		if (hubVectorMag < 205) {
			state = States.DROP_BALL;
		}
	}
	// Makes robot turn to hub
	public void faceHub() {
		double turnAngle = cam.hubAngle();
		if (Math.abs(turnAngle*3) > 0.1)  {

			joys[3] = (float) -.5; // Rotation Speed
			if (turnAngle + 2*Math.PI < Math.PI/2) {
				joys[3] = (float) -.5; // Rotation Speed
			}
			else {
				joys[3] = (float) 0.5;
			}
			gui.Drive(joys);
		}
		else {
			state = States.GO_TO_HUB;
		}	
	}
	public void pickUpBall() {
		joys[0] = (float) 0;
		joys[1] = (float) 0;
		joys[2] = (float) 0;
		joys[3] = (float) 0;
		swerve.ballX -= 1;
		swerve.ballY -= 1;
		state = States.FACE_HUB;

	}
	// Makes robot turn to ball
	public void detectBall(){
		double rotationAngle = cam.angle();
		if (Math.abs(rotationAngle*3) > 0.1)  {

			joys[3] = (float) -.5; // Rotation Speed
			if (rotationAngle + 92 < Math.PI/2) {
				joys[3] = (float) -.5; // Rotation Speed
			}
			else {
				joys[3] = (float) 0.5;
			}
			gui.Drive(joys);
		}
		else {
			state = States.GO_TO_BALL;
		}	
	}
	// Makes robot drive to ball
	public void goToBall() {
		double ballVectorMag = cam.ballVector()[2];
		joys[3] = (float) 0;
		ballFound = true;
		double[] vector = cam.ballVector();
		joys[0] = (float) (vector[1]/vector[2]);
		joys[1] = (float) (vector[0]/vector[2]);
		gui.Drive(joys);
		if (ballVectorMag < 10) {
			state = States.PICK_UP_BALL;
		}
	}
}