package games.stendhal.server.maps.semos.tavern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import games.stendhal.server.core.engine.StendhalRPWorld;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.npc.NPCList;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.fsm.Engine;
import games.stendhal.server.entity.player.Player;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import utilities.PlayerTestHelper;
import utilities.QuestHelper;

/**
 * Test buying with fractional amounts.
 *
 * @author Martin Fuchs
 */
public class BarMaidNPCTest {

	private static final String ZONE_NAME = "int_semos_tavern_0";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		QuestHelper.setUpBeforeClass();

		StendhalRPZone zone = new StendhalRPZone(ZONE_NAME);
		StendhalRPWorld world = StendhalRPWorld.get();
		world.addRPZone(zone);

		new BarMaidNPC().configureZone(zone, null);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
		PlayerTestHelper.resetNPC("Margaret");
	}

	@Test
	public void testHiAndBye() {
		Player player = PlayerTestHelper.createPlayer("player");

		SpeakerNPC npc = NPCList.get().get("Margaret");
		assertNotNull(npc);
		Engine en = npc.getEngine();

		assertTrue(en.step(player, "hello"));
		assertEquals("Greetings! How may I help you?", npc.get("text"));

		assertTrue(en.step(player, "bye"));
		assertEquals("Bye.", npc.get("text"));
	}

	@Test
	public void testBuyHam() {
		Player player = PlayerTestHelper.createPlayer("player");

		SpeakerNPC npc = NPCList.get().get("Margaret");
		assertNotNull(npc);
		Engine en = npc.getEngine();

		assertTrue(en.step(player, "hi"));
		assertEquals("Greetings! How may I help you?", npc.get("text"));

		assertTrue(en.step(player, "job"));
		assertEquals("I am the bar maid for this fair tavern. We sell both imported and local beers, and fine food.", npc.get("text"));

		assertTrue(en.step(player, "offer"));
		assertEquals("I sell beer, wine, flask, cheese, apple, carrot, meat, and ham.", npc.get("text"));

		// Currently there are no "quest" responses for Margaret.
		assertFalse(en.step(player, "quest"));

		assertTrue(en.step(player, "buy"));
		assertEquals("Please tell me what you want to buy.", npc.get("text"));

		assertTrue(en.step(player, "buy dog"));
		assertEquals("Sorry, I don't sell dogs.", npc.get("text"));

		assertTrue(en.step(player, "buy house"));
		assertEquals("Sorry, I don't sell houses.", npc.get("text"));

		assertTrue(en.step(player, "buy someunknownthing"));
		assertEquals("Sorry, I don't sell someunknownthings.", npc.get("text"));

		assertTrue(en.step(player, "buy ham"));
		assertEquals("1 piece of ham will cost 80. Do you want to buy it?", npc.get("text"));

		assertTrue(en.step(player, "no"));
		assertEquals("Ok, how else may I help you?", npc.get("text"));

		assertTrue(en.step(player, "buy ham"));
		assertEquals("1 piece of ham will cost 80. Do you want to buy it?", npc.get("text"));

		assertTrue(en.step(player, "yes"));
		assertEquals("Sorry, you don't have enough money!", npc.get("text"));

		// equip with enough money
		assertTrue(PlayerTestHelper.equipWithMoney(player, 1000));

		assertFalse(player.isEquipped("ham"));
		assertTrue(en.step(player, "buy 5 hams"));
		assertEquals("5 pieces of ham will cost 400. Do you want to buy them?", npc.get("text"));

		assertTrue(en.step(player, "yes"));
		assertEquals("Congratulations! Here are your pieces of ham!", npc.get("text"));
		assertTrue(player.isEquipped("ham", 5));

		assertTrue(en.step(player, "buy ham"));
		assertEquals("1 piece of ham will cost 80. Do you want to buy it?", npc.get("text"));

		assertTrue(en.step(player, "yes"));
		assertEquals("Congratulations! Here is your piece of ham!", npc.get("text"));

		assertTrue(en.step(player, "buy .75 ham"));
		assertEquals("1 piece of ham will cost 80. Do you want to buy it?", npc.get("text"));

		assertTrue(en.step(player, "yes"));
		assertEquals("Congratulations! Here is your piece of ham!", npc.get("text"));

		assertTrue(en.step(player, "buy 3.5 ham"));
		assertEquals("4 pieces of ham will cost 320. Do you want to buy them?", npc.get("text"));

		assertTrue(en.step(player, "yes"));
		assertEquals("Congratulations! Here are your pieces of ham!", npc.get("text"));

		assertTrue(en.step(player, "buy 10000 ham"));
		assertEquals("1 piece of ham will cost 80. Do you want to buy it?", npc.get("text"));
	}

	@Test
	public void testSellHam() {
		Player player = PlayerTestHelper.createPlayer("player");

		SpeakerNPC npc = NPCList.get().get("Margaret");
		assertNotNull(npc);
		Engine en = npc.getEngine();

		assertTrue(en.step(player, "hi Margaret"));
		assertEquals("Greetings! How may I help you?", npc.get("text"));

		// Currently there are no response to sell sentences for Margaret.
		assertFalse(en.step(player, "sell"));
	}
}
