package core.GUI;


public interface Listener {
	
	/**
	 * Method to allow graphical objects to communicate with each other
	 * 
	 * @param message Message to deliver to the object
	 * @param sender the object that sent the message
	 */
	public void hear(String message, Object sender);
	
}
