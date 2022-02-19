//import java.awt.event.ComponentEvent;
//import java.awt.event.ComponentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.lang.Math;
import java.util.Timer;
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileReader;
//import java.io.FileWriter;
//import java.io.IOException;

import javax.swing.JFrame;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

public class MainC {
	enum States {
		DETECT_BALL,
		GO_TO_BALL,
		PICK_UP_BALL,
		FACE_HUB,
		GO_TO_HUB,
		DROP_BALL;
	}
	//Camera cam = new Camera();
	static byte[] keys = {0,0,0,0,0,0,0}; // WSADLR
	static boolean timing = false;
	static boolean reset = false;
	static long time;
	static int highScore = 0;
	static GUI gui;
	static SwerveGui swerve;
    static Component stickX1 = null;
    static Component stickY1 = null;
    static Component stickX2 = null;
    static Component stickY2 = null;
    static Component startBtn = null;
	static float [] joys = new float[4];
	static boolean ballFound = false;
	static boolean reachBall = false;


	static int cntr;
	static Camera cam;
	static States state;
	
	public static void main (String args[]){
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
		//gui.setResizable(false);
		gui.setTitle("Pwnage Swerve Game");
		gui.setLocationRelativeTo(null);
		gui.setVisible(true);
		gui.requestFocusInWindow();
        /*
		gui.addComponentListener(new ComponentListener() {
			@Override
			
			public void componentResized(ComponentEvent e) {
				gui.changeWindow();
			}
			
			@Override public void componentHidden(ComponentEvent arg0) {}
			@Override public void componentMoved(ComponentEvent arg0) {}
			@Override public void componentShown(ComponentEvent arg0) {}
		});
		*/
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
        /*
        if ((new File("highscore.hs")).exists() == false){
        	try {
        	    BufferedWriter out = new BufferedWriter(new FileWriter("highscore.hs"));
        	    out.write("0");
        	    out.close();
        	} catch (IOException e) {}
        }
        try {
		    BufferedReader in = new BufferedReader(new FileReader("highscore.hs"));
		    String str = in.readLine();
		    highScore = Integer.parseInt(str);
		    gui.highScore = highScore;
		    in.close();
		} catch (IOException e) {}
        
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
            	try {
            	    BufferedWriter out = new BufferedWriter(new FileWriter("highscore.hs"));
            	    out.write(""+gui.highScore);
            	    out.close();
            	} catch (IOException e) {}
            }
        }));
        */
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
	
	
	public static void randomwalk() {
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
		//Find Ball
		/*
		if (cntr < 1000 && Math.abs(rotationAngle) > 0.1 && ballFound == false)  {
			//joys[0] = (float) (0); // Vertical Motion
			//joys[1] = (float) (0); // Horizontal Motion
			//joys[2] = 0; // Purpose unknown
			joys[3] = (float) -.00001; // Rotation Speed
			if (rotationAngle + 92 < Math.PI/2) {
				joys[3] = (float) -.5; // Rotation Speed
			}
			else {
				joys[3] = (float) 0.5;
			}
			gui.Drive(joys);
			cntr++;
		}
		
		else if (reachBall == false) {
			joys[3] = (float) 0;
			ballFound = true;
			double[] vector = cam.ballVector();
			joys[0] = (float) (vector[1]/vector[2]);
			joys[1] = (float) (vector[0]/vector[2]);
			gui.Drive(joys);
		
		}
		if (swerve.getTrueRobotX() == swerve.ballX && (swerve.getTrueRobotY() == swerve.ballY)) {
			System.out.println ("Crash into small wall that can crawl on a call in a waterfall");

			reachBall = true;
			joys[0] = (float) (0);
			joys[1] = (float) (0);
		}	
		*/
		// Crash into small wall that can crawl on a call in a waterfall (DON'T DELETE VERY IMPORTANT)
	}


	static void joysticks(){
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
	
	static void timer(boolean startBtn){ 
		if(startBtn)
		{
			gui.resetPoints();
			reset = false;
	    	timing = true;
	    	time = System.currentTimeMillis();
		}
		if(timing){
			//gui.printTime(System.currentTimeMillis()-time);
			if(System.currentTimeMillis()-time > 20000){
				timing = false;
				int points = gui.getPoints();
				if(points > highScore){
					highScore = points;
					//gui.setHighScore(points);
				}
			}
		}
	}
	public static void dropBall() {
		joys[0] = (float) 0;
		joys[1] = (float) 0;
		joys[2] = (float) 0;
		joys[3] = (float) 0;
	}
	public static void goToHub() {
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
	public static void faceHub() {
		double turnAngle = cam.hubAngle();
		if (Math.abs(turnAngle*3) > 0.1)  {
			//joys[0] = (float) (0); // Vertical Motion
			//joys[1] = (float) (0); // Horizontal Motion
			//joys[2] = 0; // Purpose unknown
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
	public static void pickUpBall() {
		joys[0] = (float) 0;
		joys[1] = (float) 0;
		joys[2] = (float) 0;
		joys[3] = (float) 0;
		swerve.ballX -= 1;
		swerve.ballY -= 1;
		state = States.FACE_HUB;

	}
	public static void detectBall(){
		double rotationAngle = cam.angle();
		if (Math.abs(rotationAngle*3) > 0.1)  {
			//joys[0] = (float) (0); // Vertical Motion
			//joys[1] = (float) (0); // Horizontal Motion
			//joys[2] = 0; // Purpose unknown
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
	public static void goToBall() {
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