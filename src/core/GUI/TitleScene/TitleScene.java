package core.GUI.TitleScene;

import core.GUI.Listener;
import core.GUI.Scene;

/**
 * {@link Scene} that holds the objects for the title screen
 * @author Sam Maynard
 *
 */
public class TitleScene extends Scene implements Listener {

	/**
	 * constructor
	 */
	public TitleScene(int width, int height, Listener listener) {
		super(listener);
		
		Title title = new Title(0, 0, width, height, this);
		graphicalObjects.add(title);
		
		PlayButton playButton = new PlayButton(100, 280, 150, 50, this);
		graphicalObjects.add(playButton);
		
		//TutorialButton tutorialButton = new TutorialButton(140, 225, 150, 35, this);
		//graphicalObjects.add(tutorialButton);
	}

	@Override
	public void hear(String message, Object sender) {
		switch(message){
		case "Play": listener.hear("Play", this); break;
		case "Tutorial": listener.hear("HelpScene", this); break;
		}
	}
	
}
