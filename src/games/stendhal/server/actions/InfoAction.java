package games.stendhal.server.actions;

import games.stendhal.server.entity.player.Player;

import java.text.SimpleDateFormat;
import java.util.Date;

import marauroa.common.game.RPAction;

public class InfoAction implements ActionListener {

	private static final String DATE_FORMAT_NOW = "dd-MMMM-yyyy HH:mm:ss";

	public void onAction(final Player player, final RPAction action) {
		player.sendPrivateText("The server time is " + getGametime());

	}

	private String getGametime() {
		 final SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		    return sdf.format(new Date(System.currentTimeMillis()));
		
	}

}
