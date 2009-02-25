package games.stendhal.server.actions.pet;

import static games.stendhal.common.constants.Actions.FORSAKE;
import static games.stendhal.common.constants.Actions.PET;
import static games.stendhal.common.constants.Actions.SHEEP;
import static games.stendhal.common.constants.Actions.SPECIES;
import games.stendhal.server.actions.ActionListener;
import games.stendhal.server.actions.CommandCenter;
import games.stendhal.server.entity.creature.Pet;
import games.stendhal.server.entity.creature.Sheep;
import games.stendhal.server.entity.player.Player;
import marauroa.common.game.RPAction;

import org.apache.log4j.Logger;

public class ForsakeAction implements ActionListener {
	
	private static final String DB_ID = "#db_id";
	private static final Logger logger = Logger.getLogger(ForsakeAction.class);
	public static void register() {
		CommandCenter.register(FORSAKE, new ForsakeAction());
	}
	
	public void onAction(final Player player, final RPAction action) {
		
				if (action.has(SPECIES)) {
				final String species = action.get(SPECIES);

				if (species.equals(SHEEP)) {
					final Sheep sheep = player.getSheep();

					if (sheep != null) {
						player.removeSheep(sheep);

						// HACK: Avoid a problem on database
						if (sheep.has(DB_ID)) {
							sheep.remove(DB_ID);
						}
					} else {
						logger.error("sheep not found in disown action: " + action.toString());
					}
				} else if (species.equals(PET)) {
					final Pet pet = player.getPet();

					if (pet != null) {
						player.removePet(pet);

						// HACK: Avoid a problem on database
						if (pet.has(DB_ID)) {
							pet.remove(DB_ID);
						}
					} else {
						logger.error("pet not found in disown action: " + action.toString());
					}
				}
			}
		}

	

}
