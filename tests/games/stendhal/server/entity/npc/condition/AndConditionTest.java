package games.stendhal.server.entity.npc.condition;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.SpeakerNPC.ChatCondition;
import games.stendhal.server.entity.player.Player;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import utilities.PlayerTestHelper;
import utilities.SpeakerNPCTestHelper;

public class AndConditionTest {
	AlwaysTrueCondition trueCondition;
	AlwaysTrueCondition falsecondition;

	@Before
	public void setUp() throws Exception {
		trueCondition = new AlwaysTrueCondition();
		falsecondition = new AlwaysTrueCondition() {

			@Override
			public boolean fire(Player player, String text, SpeakerNPC engine) {
				return false;
			}

			@Override
			public String toString() {
				return "false";
			}
		};
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void selftest() throws Exception {
		assertTrue("empty And is true", trueCondition.fire(
				PlayerTestHelper.createPlayer(), "testAndConditionText",
				SpeakerNPCTestHelper.createSpeakerNPC()));
		assertFalse("empty And is true", falsecondition.fire(
				PlayerTestHelper.createPlayer(), "testAndConditionText",
				SpeakerNPCTestHelper.createSpeakerNPC()));
	}

	@Test
	public void testConstructor() throws Throwable {
		new AndCondition();
	}

	@Test
	public void testEquals() throws Throwable {
		assertFalse(new AndCondition().equals(null));

		AndCondition obj = new AndCondition();
		assertTrue(obj.equals(obj));
		assertTrue(new AndCondition().equals(new AndCondition()));
		assertTrue(new AndCondition((ChatCondition)null).equals(new AndCondition((ChatCondition)null)));

		assertFalse(new AndCondition((ChatCondition)null).equals(new AndCondition()));
		assertFalse(new AndCondition().equals(new AndCondition((ChatCondition)null)));
		assertFalse(new AndCondition((ChatCondition)null).equals(new AndCondition(new ChatCondition(){

			@Override
			public boolean fire(Player player, String text, SpeakerNPC npc) {

				return false;
			}})));
		assertFalse(new AndCondition().equals(new Integer(100)));
		assertTrue(new AndCondition().equals(new AndCondition() {
		}));
	}

	@Test
	public void testFire() throws Throwable {


		assertTrue("empty And is true", new AndCondition().fire(
				PlayerTestHelper.createPlayer(), "testAndConditionText",
				SpeakerNPCTestHelper.createSpeakerNPC()));

		AndCondition and = new AndCondition(trueCondition);
		assertTrue("And with one Allwaystrue is true", and.fire(
				PlayerTestHelper.createPlayer(), "testAndConditionText",
				SpeakerNPCTestHelper.createSpeakerNPC()));

		and = new AndCondition(trueCondition, falsecondition);
		assertFalse("And with one true and on false is false", and.fire(
				PlayerTestHelper.createPlayer(), "testAndConditionText",
				SpeakerNPCTestHelper.createSpeakerNPC()));

		and = new AndCondition(falsecondition, trueCondition);
		assertFalse("And with one false and on true is false", and.fire(
				PlayerTestHelper.createPlayer(), "testAndConditionText",
				SpeakerNPCTestHelper.createSpeakerNPC()));

		and = new AndCondition(new AdminCondition());

		assertFalse("And with one false is false", and.fire(
				PlayerTestHelper.createPlayer(), "testAndConditionText",
				SpeakerNPCTestHelper.createSpeakerNPC()));
	}

	@Test
	public void testHashCode() throws Throwable {
		AndCondition obj = new AndCondition();
		assertEquals(obj.hashCode(),obj.hashCode());
		assertEquals(new AndCondition().hashCode(),new AndCondition().hashCode());
		assertEquals(new AndCondition((ChatCondition)null).hashCode(),new AndCondition((ChatCondition)null).hashCode());

	}

	@Test
	public void testToString() throws Throwable {
		assertEquals("[]", new AndCondition().toString());

		assertEquals("[true]", new AndCondition(trueCondition).toString());
		assertEquals("[true, false]", new AndCondition(trueCondition,
				falsecondition).toString());
		assertEquals("[false]", new AndCondition(falsecondition).toString());
	}
}
