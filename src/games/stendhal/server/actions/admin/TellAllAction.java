package games.stendhal.server.actions.admin;

import static games.stendhal.common.constants.Actions.TELLALL;
import static games.stendhal.common.constants.Actions.TEXT;
import games.stendhal.server.actions.CommandCenter;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.entity.player.Player;
import marauroa.common.game.RPAction;

public class TellAllAction extends AdministrationAction {


	public static void register() {
		CommandCenter.register(TELLALL, new TellAllAction(), 200);

	}

	@Override
	public void perform(final Player player, final RPAction action) {
		if (action.has(TEXT)) {
			final String message = "Administrator SHOUTS: " + action.get(TEXT);
			SingletonRepository.getRuleProcessor().addGameEvent(player.getName(),
					TELLALL, action.get(TEXT));

			SingletonRepository.getRuleProcessor().tellAllPlayers(message);
		}
	}

}
