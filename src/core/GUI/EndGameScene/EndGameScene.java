package core.GUI.EndGameScene;

import core.GUI.Listener;
import core.GUI.Scene;


public class EndGameScene extends Scene implements Listener {

	public EndGameScene(Listener listener) {
		super(listener);
		
		EndGameMessage endGameMessage = new EndGameMessage(280, 200);
		graphicalObjects.add(endGameMessage);
		
		//RestartButton restartButton = new RestartButton(360, 330, 130, 50, this);
		//graphicalObjects.add(restartButton);
	}

	@Override
	public void hear(String message, Object sender) {
		switch(message){
		case "Restart":
			listener.hear("Restart", this);
			break;
		}
	}
	
}
