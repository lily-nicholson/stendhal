package games.stendhal.server.maps.ados;

import games.stendhal.common.Direction;
import games.stendhal.server.StendhalRPWorld;
import games.stendhal.server.StendhalRPZone;
import games.stendhal.server.entity.PersonalChest;
import games.stendhal.server.entity.npc.NPCList;
import games.stendhal.server.entity.npc.ProducerBehaviour;
import games.stendhal.server.entity.npc.SellerBehaviour;
import games.stendhal.server.entity.npc.ShopList;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.portal.Portal;
import games.stendhal.server.pathfinder.Path;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import marauroa.common.game.IRPZone;

/**
 * Builds the inside of buildings in Ados City
 *
 * @author hendrik
 */
public class AdosCityInside {
	private NPCList npcs = NPCList.get();
	private ShopList shops = ShopList.get();

	/**
	 * build the city insides
	 */
	public void build() {
		StendhalRPWorld world = StendhalRPWorld.get();
		buildBank((StendhalRPZone) world.getRPZone(new IRPZone.ID("int_ados_bank")));
		buildBakery((StendhalRPZone) world.getRPZone(new IRPZone.ID("int_ados_bakery")));
		buildSemosTavern((StendhalRPZone) world.getRPZone(new IRPZone.ID("int_ados_tavern_0")));
		buildTempel((StendhalRPZone) world.getRPZone(new IRPZone.ID("int_ados_temple")));
	}

	private void buildBank(StendhalRPZone zone) {

		// portal from bank to city
		Portal portal = new Portal();
		zone.assignRPObjectID(portal);
		portal.setX(22);
		portal.setY(17);
		portal.setNumber(0);
		portal.setDestination("0_ados_city", 6);
		zone.addPortal(portal);

		// personal chest
		PersonalChest chest = new PersonalChest();
		zone.assignRPObjectID(chest);
		chest.set(3, 12);
		zone.add(chest);

		chest = new PersonalChest();
		zone.assignRPObjectID(chest);
		chest.set(5, 12);
		zone.add(chest);
		
		chest = new PersonalChest();
		zone.assignRPObjectID(chest);
		chest.set(10, 12);
		zone.add(chest);
	}

	private void buildSemosTavern(StendhalRPZone zone) {
		Portal portal = new Portal();
		zone.assignRPObjectID(portal);
		portal.setX(12);
		portal.setY(17);
		portal.setNumber(0);
		portal.setDestination("0_ados_city", 0);
		zone.addPortal(portal);
		
		portal = new Portal();
		zone.assignRPObjectID(portal);
		portal.setX(27);
		portal.setY(17);
		portal.setNumber(1);
		portal.setDestination("0_ados_city", 1);
		zone.addPortal(portal);

		
		SpeakerNPC tavernMaid = new SpeakerNPC("Cor�lia") {
			@Override
			protected void createPath() {
				List<Path.Node> nodes = new LinkedList<Path.Node>();
				nodes.add(new Path.Node(17, 12));
				nodes.add(new Path.Node(17, 13));
				nodes.add(new Path.Node(16, 8));
				nodes.add(new Path.Node(13, 8));
				nodes.add(new Path.Node(13, 6));
				nodes.add(new Path.Node(13, 10));
				nodes.add(new Path.Node(25, 10));
				nodes.add(new Path.Node(25, 13));
				nodes.add(new Path.Node(25, 10));
				nodes.add(new Path.Node(17, 10));
				setPath(nodes, true);
			}

			@Override
			protected void createDialog() {
				addGreeting();
				addJob("I am the bar maid for this fair tavern. We sell both imported and local beers, and fine food.");
				addHelp("This tavern is a great place to take a break and meet new people! Just ask if you want me to #offer you a drink.");
				addSeller(new SellerBehaviour(shops.get("food&drinks")));
				addGoodbye();
			}
		};
		npcs.add(tavernMaid);
		zone.assignRPObjectID(tavernMaid);
		tavernMaid.put("class", "tavernbarmaidnpc");
		tavernMaid.set(17, 12);
		tavernMaid.initHP(100);
		zone.addNPC(tavernMaid);

	}

	private void buildBakery(StendhalRPZone zone) {
		Portal portal = new Portal();
		zone.assignRPObjectID(portal);
		portal.setX(26);
		portal.setY(14);
		portal.setNumber(0);
		portal.setDestination("0_ados_city", 10);
		zone.addPortal(portal);

		SpeakerNPC baker = new SpeakerNPC("Arlindo") {
			@Override
			protected void createPath() {
				List<Path.Node> nodes = new LinkedList<Path.Node>();
				// to the well
				nodes.add(new Path.Node(15, 2));
				// to a barrel
				nodes.add(new Path.Node(15, 7));
				// to the baguette on the table
				nodes.add(new Path.Node(13, 7));
				// around the table
				nodes.add(new Path.Node(13, 9));
				nodes.add(new Path.Node(10, 9));
				// to the sink
				nodes.add(new Path.Node(10, 11));
				// to the pizza/cake/whatever
				nodes.add(new Path.Node(7, 11));
				nodes.add(new Path.Node(7, 9));
				// to the pot
				nodes.add(new Path.Node(3, 9));
				// towards the oven
				nodes.add(new Path.Node(3, 3));
				nodes.add(new Path.Node(5, 3));
				// to the oven
				nodes.add(new Path.Node(5, 2));
				// one step back
				nodes.add(new Path.Node(5, 3));
				// towards the well
				nodes.add(new Path.Node(15, 3));
				
				setPath(nodes, true);
			}

			@Override
			protected void createDialog() {
				// addGreeting("Hi, most of the people are out of town at the moment.");
				addJob("I'm the local baker. Although we get most of our supplies from Semos City, there is still a lot of work to do.");
				addReply(Arrays.asList("flour", "meat", "carrot", "mushroom", "button_mushroom"), "Ados is short on supplies. We get most of our food from Semos City which is west of here.");
				addHelp("My wife is searching for that lost boy, too. So we cannot sell you anthing at the moment.");
				addGoodbye();

				// Arlindo makes pies if you bring him flour, meat, carrot and a mushroom
				Map<String, Integer> requiredResources = new HashMap<String, Integer>();
				requiredResources.put("flour", new Integer(2));
				requiredResources.put("meat", new Integer(2));
				requiredResources.put("carrot", new Integer(1));
				requiredResources.put("button_mushroom", new Integer(1));

				ProducerBehaviour behaviour = new ProducerBehaviour(
						"arlindo_make_pie", "make", "pie", requiredResources, 7 * 60);

				addProducer(behaviour,
						"Hi! I bet you've heard about my famous pie and want me to #make one for you, am I right?");
			}
		};
		npcs.add(baker);
		zone.assignRPObjectID(baker);
		baker.put("class", "chefnpc");
		baker.setDirection(Direction.DOWN);
		baker.set(15, 2);
		baker.initHP(100);
		zone.addNPC(baker);
	}

	private void buildTempel(StendhalRPZone zone) {
		Portal portal = new Portal();
		zone.assignRPObjectID(portal);
		portal.setX(10);
		portal.setY(23);
		portal.setNumber(0);
		portal.setDestination("0_ados_city", 1);
		zone.addPortal(portal);
		portal = new Portal();
		zone.assignRPObjectID(portal);
		portal.setX(11);
		portal.setY(23);
		portal.setNumber(1);
		portal.setDestination("0_ados_city", 1);
		zone.addPortal(portal);
		portal = new Portal();
		zone.assignRPObjectID(portal);
		portal.setX(12);
		portal.setY(23);
		portal.setNumber(2);
		portal.setDestination("0_ados_city", 1);
		zone.addPortal(portal);
		portal = new Portal();
		zone.assignRPObjectID(portal);
		portal.setX(13);
		portal.setY(23);
		portal.setNumber(3);
		portal.setDestination("0_ados_city", 1);
		zone.addPortal(portal);
	}

}
