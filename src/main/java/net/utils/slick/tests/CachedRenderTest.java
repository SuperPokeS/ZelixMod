package net.utils.slick.tests;

import net.utils.slick.AppGameContainer;
import net.utils.slick.BasicGame;
import net.utils.slick.CachedRender;
import net.utils.slick.Color;
import net.utils.slick.GameContainer;
import net.utils.slick.Graphics;
import net.utils.slick.Input;
import net.utils.slick.SlickException;

/**
 * A simple test to show performance gains from cache operations in situtations where
 * rendering is static and heavy
 * 
 * @author kevin
 */
public class CachedRenderTest extends BasicGame {
	/** The set of operations to be cached */
	private Runnable operations;
	/** The cached version of the operations */
	private CachedRender cached;
	/** True if we're drawing the cached version */
	private boolean drawCached;
	
	/**
	 * Create a new simple test for cached rendering (aka display lists)
	 */
	public CachedRenderTest() {
		super("Cached Render Test");
	}
	
	/**
	 * @see net.utils.slick.BasicGame#init(net.utils.slick.GameContainer)
	 */
	public void init(final GameContainer container) throws SlickException {
		operations = new Runnable() {
			public void run() {
				for (int i=0;i<100;i++) {
					int c = i+100;
					container.getGraphics().setColor(new Color(c,c,c,c));
					container.getGraphics().drawOval((i*5)+50,(i*3)+50,100,100);
				}
			}
		};
		
		cached = new CachedRender(operations);
	}

	/**
	 * @see net.utils.slick.BasicGame#update(net.utils.slick.GameContainer, int)
	 */
	public void update(GameContainer container, int delta) throws SlickException {
		if (container.getInput().isKeyPressed(Input.KEY_SPACE)) {
			drawCached = !drawCached;
		}
	}

	/**
	 * @see net.utils.slick.Game#render(net.utils.slick.GameContainer, net.utils.slick.Graphics)
	 */
	public void render(GameContainer container, Graphics g) throws SlickException {
		g.setColor(Color.white);
		g.drawString("Press space to toggle caching", 10, 130);
		if (drawCached) {
			g.drawString("Drawing from cache", 10, 100);
			cached.render();
		} else {
			g.drawString("Drawing direct", 10, 100);
			operations.run();
		}
	}

	/**
	 * Entry point to our test
	 * 
	 * @param argv The arguments to pass into the test
	 */
	public static void main(String[] argv) {
		try {
			AppGameContainer container = new AppGameContainer(new CachedRenderTest());
			container.setDisplayMode(800,600,false);
			container.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
}
