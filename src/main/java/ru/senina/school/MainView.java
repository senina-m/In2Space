package ru.senina.school;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.*;

public class MainView {

	static int numberOfFlights = 0;
	static int money = 0;
	static int time = 0;

	/** gas tank button */
	// кнопки
	static JButton[] gtb = new JButton[4];
	// была ли когда-то куплена
	static boolean[] gtbSet = new boolean[4];
	// выбрана ли сейчас
	static int gasTank = 0;

	/** motor button */
	static JButton[] mb = new JButton[4];
	// была ли когда-то куплена
	static boolean[] mbSet = new boolean[4];
	// выбрана ли сейчас
	static int motor = 0;

	/** nozzle button */
	static JButton[] nb = new JButton[4];
	// была ли когда-то куплена
	static boolean[] nbSet = new boolean[4];
	// выбрана ли сейчас
	static int nozzle = 0;

	static int gTank = gasTank * 5 + 10;

	public abstract static class SpaceObject {
		double x;
		double y;
		double height;
		double width;
		boolean visible = true;

		public SpaceObject(double x, double y, double width, double height) {
			this.x = x;
			this.y = y;
			this.height = height;
			this.width = width;
		}

		public boolean isIntersect(SpaceObject o) {
			return x - width / 2 < o.x + o.width / 2 && x + width / 2 > o.x - o.width / 2
					&& y - height / 2 < o.y + o.height / 2 && y + height / 2 > o.y - o.height / 2;
		}

		public abstract void draw(Graphics2D g, CoordConverter conv);
	}

	public static class CoordConverter {
		double modelWinWidth = 1000;
		double modelWinHeight = 3000;
		double modelWinTopX = -300;
		double modelWinTopY = 600;

		int displayWinWidth = 700;
		int displayWinHeight = 600;

		public int modelXToGraphicsX(double x) {
			return (int) (x - modelWinTopX);
		}

		public int modelYToGraphicsY(double y) {
			return (int) (modelWinTopY - y);
		}
	}

	public static class Star extends SpaceObject {

		public Star(double x, double y) {
			super(x, y, 0, 0);
		}

		public void draw(Graphics2D g, CoordConverter conv) {
			int displayX = conv.modelXToGraphicsX(x);
			int displayY = conv.modelYToGraphicsY(y);
			g.setColor(Color.WHITE);
			g.drawLine(displayX, displayY, displayX, displayY);
		}

	}

	public static class StartSpace extends SpaceObject {
		private final static double H = 500;
		private final static double W = 700;

		public StartSpace(double x, double y) {
			super(x, y, W, H);
		}

		public void draw(Graphics2D g, CoordConverter conv) {
			int xL = Math.abs(conv.modelXToGraphicsX(0) - conv.modelXToGraphicsX(W));
			int yL = Math.abs(conv.modelYToGraphicsY(0) - conv.modelYToGraphicsY(H));
			int displayX = conv.modelXToGraphicsX(x);
			int displayY = conv.modelYToGraphicsY(y);
			BufferedImage bufImg = null;
			try {
				bufImg = ImageIO
						.read(Objects.requireNonNull(this.getClass().getResource("/spaceShipImageStart.jpg")));
			} catch (IOException e) {
				e.printStackTrace();
			}
			g.drawImage(bufImg, displayX - xL / 2, displayY - yL / 2, xL, yL, null);
		}
	}

	public static class FlatWorld extends SpaceObject {
		private final static double H = 200;
		private final static double W = 500;

		public FlatWorld(double x, double y) {
			super(x, y, W, H);
		}

		public void draw(Graphics2D g, CoordConverter conv) {
			int xL = Math.abs(conv.modelXToGraphicsX(0) - conv.modelXToGraphicsX(W));
			int yL = Math.abs(conv.modelYToGraphicsY(0) - conv.modelYToGraphicsY(H));
			int displayX = conv.modelXToGraphicsX(x);
			int displayY = conv.modelYToGraphicsY(y);
			BufferedImage bufImg = null;
			try {
				bufImg = ImageIO.read(Objects.requireNonNull(this.getClass().getResource("/flatworldImage.jpg")));
			} catch (IOException e) {
				e.printStackTrace();
			}
			g.drawImage(bufImg, displayX - xL / 2, displayY - yL / 2, xL, yL, null);
		}
	}

	public static class Spaceship extends SpaceObject {
		private final static double W = 34;
		private final static double H = 50;

		public Spaceship(double x, double y) {
			super(x, y, W, H);
		}

		@Override
		public void draw(Graphics2D g, CoordConverter conv) {
			int xL = Math.abs(conv.modelXToGraphicsX(0) - conv.modelXToGraphicsX(W));
			int yL = Math.abs(conv.modelYToGraphicsY(0) - conv.modelYToGraphicsY(H));
			int displayX = conv.modelXToGraphicsX(x);
			int displayY = conv.modelYToGraphicsY(y);
			BufferedImage bufImg = null;
			try {
				String image = String.format("/spaceShipImage%d.png", time % 2 + 1);
				bufImg = ImageIO.read(Objects.requireNonNull(this.getClass().getResource(image)));
			} catch (IOException e) {
				e.printStackTrace();
			}
			g.drawImage(bufImg, displayX - xL / 2, displayY - yL / 2, xL, yL, null);
		}
	}

	public static class GasTank extends SpaceObject {
		private final static double L = 20;

		public GasTank(double x, double y) {
			super(x, y, L, L);
		}

		@Override
		public void draw(Graphics2D g, CoordConverter conv) {
			int xL = Math.abs(conv.modelXToGraphicsX(0) - conv.modelXToGraphicsX(L));
			int yL = Math.abs(conv.modelYToGraphicsY(0) - conv.modelYToGraphicsY(L));
			int displayX = conv.modelXToGraphicsX(x);
			int displayY = conv.modelYToGraphicsY(y);
			BufferedImage bufImg = null;
			try {
				bufImg = ImageIO.read(Objects.requireNonNull(this.getClass().getResource("/gasTankImage.png")));
			} catch (IOException e) {
				e.printStackTrace();
			}
			g.drawImage(bufImg, displayX - xL / 2, displayY - yL / 2, xL, yL, null);
		}

	}

	public static class Coin extends SpaceObject {
		private final static double L = 15;

		Coin(double x, double y) {
			super(x, y, L, L);
		}

		@Override
		public void draw(Graphics2D g, CoordConverter conv) {
			int xL = Math.abs(conv.modelXToGraphicsX(0) - conv.modelXToGraphicsX(L));
			int yL = Math.abs(conv.modelYToGraphicsY(0) - conv.modelYToGraphicsY(L));
			int displayX = conv.modelXToGraphicsX(x);
			int displayY = conv.modelYToGraphicsY(y);
			BufferedImage bufImg = null;
			try {
				bufImg = ImageIO.read(Objects.requireNonNull(this.getClass().getResource("/coinImage.png")));
			} catch (IOException e) {
				e.printStackTrace();
			}
			g.drawImage(bufImg, displayX - xL / 2, displayY - yL / 2, xL, yL, null);
		}

	}

	public static interface SpaceModelListener {
		public void gasTankBecomeEmpty();

		public void maxHeigthReached();
	}

	public static class SpaceModel {
		Spaceship ship = new Spaceship(0, 0);
		FlatWorld world = new FlatWorld(0, 2900);
		GasTank[] tanks = new GasTank[20];
		Coin[] coins = new Coin[20];
		Star[] stars = new Star[2000];
		StartSpace start = new StartSpace(0, 0);
		long time = 0;
		double shipSpeed = 4;

		ArrayList<MainView.SpaceModelListener> listeners = new ArrayList<>();

		public SpaceModel() {
			Random r = new Random();
			for (int i = 0; i < tanks.length; i++) {
				tanks[i] = new GasTank(r.nextDouble() * 1000 - 500, r.nextDouble() * 2900 + 100);
			}
			for (int i = 0; i < coins.length; i++) {
				coins[i] = new Coin(r.nextDouble() * 1000 - 500, r.nextDouble() * 2900 + 100);
			}
			for (int i = 0; i < stars.length; i++) {
				stars[i] = new Star(r.nextDouble() * 1000 - 500, r.nextDouble() * 2900 + 100);
			}
		}

		public void reset() {
			ship.x = 0;
			ship.y = 0;
			Random r = new Random();
			for (int i = 0; i < tanks.length; i++) {
				tanks[i] = new GasTank(r.nextDouble() * 1000 - 500, r.nextDouble() * 2900 + 100);
			}
			for (int i = 0; i < coins.length; i++) {
				coins[i] = new Coin(r.nextDouble() * 1000 - 500, r.nextDouble() * 2900 + 100);
			}
			for (int i = 0; i < stars.length; i++) {
				stars[i] = new Star(r.nextDouble() * 1000 - 500, r.nextDouble() * 2900 + 100);
			}
			start.x = 0;
			start.y = 0;
			world.x = 0;
			world.y = 2800;
			time = 0;
			shipSpeed = 4;

		}

		CoordConverter converter = new CoordConverter();

		public void addListener(MainView.SpaceModelListener l) {
			if (!listeners.contains(l)) {
				listeners.add(l);
			}
		}

		public void removeListener(MainView.SpaceModelListener l) {
			if (listeners.contains(l)) {
				listeners.remove(l);
			}
		}

		public void fireGasTankBecomeEmpty() {
			for (MainView.SpaceModelListener l : listeners) {
				l.gasTankBecomeEmpty();
			}
		}

		public void fireMaxHeigthReached() {
			for (MainView.SpaceModelListener l : listeners) {
				l.maxHeigthReached();
			}
		}

		public void nextStep() {
			time++;
			update();
		}

		private void update() {
			ship.y = ship.y + shipSpeed;
			if (ship.y > 3000) {
				fireMaxHeigthReached();
			}
			if (time % 10 == 0) {
				gTank -= 1;
				if (gTank < 0) {
					fireGasTankBecomeEmpty();
				}
			}

			converter.modelWinTopX = ship.x - converter.displayWinWidth / 2;
			converter.modelWinTopY = ship.y + converter.displayWinHeight / 2;

			for (int i = 0; i < coins.length; i++) {
				if (coins[i].visible && ship.isIntersect(coins[i])) {
					money += 10;
					coins[i].visible = false;
				}
			}

			for (int i = 0; i < tanks.length; i++) {
				if (tanks[i].visible && ship.isIntersect(tanks[i])) {
					gTank += (1 + gasTank) * 5;
					tanks[i].visible = false;
				}
			}
		}

		public void moveShipRight() {
			ship.x += nozzle + 1;
			if (time % 10 == 0) {
				gTank -= nozzle;
			}
		}

		public void moveShipLeft() {
			ship.x -= nozzle + 1;
			if (time % 10 == 0) {
				gTank -= nozzle;
			}
		}

		public void SpeedUp() {
			ship.y = ship.y + motor;
			if (time % 10 == 0) {
				gTank -= motor;
			}
		}

	}

	public static class FlyPanel extends JPanel {
		private static final long serialVersionUID = 1L;

		private SpaceModel model;

		public FlyPanel(SpaceModel model) {
			this.model = model;
			this.setFocusable(true);
			this.setRequestFocusEnabled(true);
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;

			StartSpace start = model.start;
			if (start.visible) {
				start.draw(g2d, model.converter);
			}

			FlatWorld world = model.world;
			if (world.visible) {
				world.draw(g2d, model.converter);
			}

			for (int i = 0; i < model.stars.length; i++) {
				Star star = model.stars[i];
				if (star.visible) {
					star.draw(g2d, model.converter);
				}
			}

			for (int i = 0; i < model.coins.length; i++) {
				Coin coin = model.coins[i];
				if (coin.visible) {
					coin.draw(g2d, model.converter);
				}
			}
			for (int i = 0; i < model.tanks.length; i++) {
				GasTank tank = model.tanks[i];
				if (tank.visible) {
					tank.draw(g2d, model.converter);
				}
			}

			Spaceship ship = model.ship;
			if (ship.visible) {
				ship.draw(g2d, model.converter);
			}

		}
	}

	public static void main(String[] args) {
		JFrame window = new JFrame("In2Space");
		window.setSize(700, 700);
		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		/** making panels */
		final SpaceModel model = new SpaceModel();
		final FlyPanel flightPanel = new FlyPanel(model);
		flightPanel.setBackground(Color.BLACK);

		final JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		JPanel shopPanel = new JPanel();
		JPanel rulePanel = new JPanel();
		JPanel viewPanel = new JPanel();
		JPanel lablesPanel = new JPanel();
		JPanel winPanel = new JPanel();

		String text = "<html><h1>How to play?</h1>" + "<font face=ТverdanaТ size = 2>"
				+ " <h3> <i>The goal of this game is to reach the universe end! </i> </h3> <br>"
				+ " But to do it you need to improve your spaceship! <br>"
				+ " Your spaceship has got 3 objects in it that can change your, <br>"
				+ " if you improve them: <br>"
				+ "  <br>" 
				+ " <h4><ins>* volume of gas tank</ins> </h4> <i>(increase the time of flight)</i> <br>"
				+ " If you buy it your gas tank become bigger fo 5 units <br>"
				+ " in the begining of your new flight. And it increase to such <br>"
				+ " quantity of gas every time you take gas tank in the space. <br>"
				+ " <h4><ins>* motor power</ins> </h4><i>(increase vertical velocity component</i> <br>"
				+ " <i>of spaceship)</i> <br>" 
				+ " It will accelerate spaceship in the the vertical direction as  <br>"
				+ " more as good is a model of your motor. To use it just press<br>"
				+ "  a button UP <br>"
				+ " <h4><ins>* level of nuzzels</ins></h4> <i>(increase horizontal velocity</i> <br>"
				+ " <i>component of spaceship)</i> <br>"
				+ " It will accelerate spaceship in the the horisontal direction as  <br>"
				+ " more as good is a model of your nuzzels. To use it just press <br>"
				+ "  a button LEFT or RIGHT. <br>"
				+ "  <br>"
				+ " &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
				+ "Good Luck and have a nice flight!</html>";

		JLabel htmlLabel = new JLabel();
		htmlLabel.setText(text);
		rulePanel.add(htmlLabel);

		winPanel.setLayout(new BoxLayout(winPanel, BoxLayout.Y_AXIS));
		window.add(winPanel);
		JLabel youWin = new JLabel("You Win!");
		youWin.setFont(new Font("Serif", Font.PLAIN, 50));
		JLabel youWinLable = new JLabel();
		youWinLable.setFont(new Font("Serif", Font.PLAIN, 30));
		winPanel.add(youWin);
		winPanel.add(youWinLable);

		/** making tabs */
		JTabbedPane tabs = new JTabbedPane();
		tabs.addTab("Main", viewPanel);
		tabs.addTab("Rules", rulePanel);
		window.add(tabs, BorderLayout.CENTER);

		JButton startButton = new JButton("start");
		startButton.setPreferredSize(new Dimension(200, 25));
		JLabel gt = new JLabel("  gas tanks");
		JLabel m = new JLabel("  motors");
		JLabel n = new JLabel("  nozzels");

		String coin = "rub";
		for (int i = 1; i < 4; i++) {
			gtb[i] = new JButton("buy (" + (i + 1) * 20 + " " + coin + ")");
			mb[i] = new JButton("buy (" + (i + 1) * 20 + " " + coin + ")");
			nb[i] = new JButton("buy (" + (i + 1) * 20 + " " + coin + ")");
			gtbSet[i] = false;
			mbSet[i] = false;
			nbSet[i] = false;
		}
		gtbSet[0] = true;
		mbSet[0] = true;
		nbSet[0] = true;

		shopPanel.add(gt);
		gtb[0] = new JButton("got");
		for (int i = 0; i < 4; i++) {
			shopPanel.add(gtb[i]);
		}

		shopPanel.add(m);
		mb[0] = new JButton("got");
		for (int i = 0; i < 4; i++) {
			shopPanel.add(mb[i]);
		}

		shopPanel.add(n);
		nb[0] = new JButton("got");
		for (int i = 0; i < 4; i++) {
			shopPanel.add(nb[i]);
		}
		/** Creating labels to the mainPanel */
		final JLabel moneyLable = new JLabel(Integer.toString(money));
		JLabel mL = new JLabel("Money");
		JLabel numberOfFlightsLable = new JLabel(Integer.toString(numberOfFlights));
		JLabel nOFL = new JLabel("               Number of flights");
		JLabel gasTankLable = new JLabel(Integer.toString(gTank) + "   ");
		JLabel gTL = new JLabel("               Gas tank");

		/** adding panels */
		viewPanel.add(lablesPanel);
		viewPanel.add(mainPanel);
		viewPanel.add(flightPanel);
		flightPanel.setVisible(false);
		viewPanel.add(winPanel);
		winPanel.setVisible(false);
		mainPanel.setVisible(true);
		lablesPanel.add(mL);
		lablesPanel.add(moneyLable);
		lablesPanel.add(nOFL);
		lablesPanel.add(numberOfFlightsLable);
		lablesPanel.add(gTL);
		lablesPanel.add(gasTankLable);
		mainPanel.add(shopPanel);
		lablesPanel.add(startButton);
		mainPanel.setPreferredSize(new Dimension(600, 250));
		flightPanel.setPreferredSize(new Dimension(700, 600));
		shopPanel.setLayout(new GridLayout(3, 4, 10, 10));
		shopPanel.setPreferredSize(new Dimension(700, 200));

		/** Creating timer */
		Timer timer = new Timer(100, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				model.nextStep();
				flightPanel.repaint();
				moneyLable.setText(Integer.toString(money));
				gasTankLable.setText(Integer.toString(gTank) + "   ");
				time++;
			}
		});

		model.addListener(new SpaceModelListener() {
			@Override
			public void maxHeigthReached() {
				flightPanel.setVisible(false);
				youWinLable.setText("Your score : " + numberOfFlights);
				winPanel.setVisible(true);
				lablesPanel.setVisible(false);
				timer.stop();

			}

			@Override
			public void gasTankBecomeEmpty() {
				flightPanel.setVisible(false);
				mainPanel.setVisible(true);
				timer.stop();
				numberOfFlights++;
				gTank = gasTank * 5 + 10;
				numberOfFlightsLable.setText(Integer.toString(numberOfFlights));
				model.reset();
			}
		});

		startButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				mainPanel.setVisible(false);
				flightPanel.setVisible(true);
				flightPanel.setFocusable(true);
				flightPanel.setRequestFocusEnabled(true);
				flightPanel.grabFocus();
				timer.start();

			}
		});

		flightPanel.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent event) {
			}

			@Override
			public void keyReleased(KeyEvent event) {
			}

			@Override
			public void keyPressed(KeyEvent event) {
				switch (event.getKeyCode()) {
				case KeyEvent.VK_LEFT:
					model.moveShipLeft();
					break;
				case KeyEvent.VK_RIGHT:
					model.moveShipRight();
					break;
				case KeyEvent.VK_UP:
					model.SpeedUp();
					break;
				}
			}
		});

		gtb[0].addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if ((money >= 0 * 100) || gtbSet[0]) {
					gtb[gasTank].setText("take");
					gtb[0].setText("got");
					gtbSet[0] = true;
					gasTank = 0;
					gTank = gasTank * 5 + 10;
					gasTankLable.setText(Integer.toString(gTank) + "   ");
				}
			}
		});
		mb[0].addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if ((money >= 0 * 100) || mbSet[0]) {
					if (mbSet[0] == false) {
						mb[motor].setText("take");
						mb[0].setText("got");
						mbSet[0] = true;
						motor = 0;
					}
				}
			}
		});
		nb[0].addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if ((money >= 0 * 100) || nbSet[0]) {
					if (nbSet[0] == false) {
						nb[nozzle].setText("take");
						nb[0].setText("got");
						nbSet[0] = true;
						nozzle = 0;
					}
				}
			}
		});
		gtb[1].addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if ((money >= 2 * 20) || gtbSet[1]) {
					if (gtbSet[1] == false) {
						money = money - 2 * 20;
					}
					gtb[gasTank].setText("take");
					gtb[1].setText("got");
					gtbSet[1] = true;
					gasTank = 1;
					gTank = gasTank * 5 + 10;
					gasTankLable.setText(Integer.toString(gTank) + "   ");
					moneyLable.setText(Integer.toString(money));
				}
			}
		});
		mb[1].addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if ((money >= 2 * 20) || mbSet[1]) {
					if (mbSet[1] == false) {
						money = money - 2 * 20;
					}
					mb[motor].setText("take");
					mb[1].setText("got");
					mbSet[1] = true;
					motor = 1;
					moneyLable.setText(Integer.toString(money));
				}
			}
		});
		nb[1].addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if ((money >= 2 * 20) || nbSet[1]) {
					if (nbSet[1] == false) {
						money = money - 2 * 20;
					}
					nb[nozzle].setText("take");
					nb[1].setText("got");
					nbSet[1] = true;
					nozzle = 1;
					moneyLable.setText(Integer.toString(money));
				}
			}
		});

		gtb[2].addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if ((money >= 3 * 20) || gtbSet[2]) {
					if (gtbSet[2] == false) {
						money = money - 3 * 20;
					}
					gtb[gasTank].setText("take");
					gtb[2].setText("got");
					gtbSet[2] = true;
					gasTank = 2;
					gTank = gasTank * 5 + 10;
					gasTankLable.setText(Integer.toString(gTank) + "   ");
					moneyLable.setText(Integer.toString(money));
				}
			}
		});
		mb[2].addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if ((money >= 3 * 20) || mbSet[2]) {
					if (mbSet[2] == false) {
						money = money - 3 * 20;
					}
					mb[motor].setText("take");
					mb[2].setText("got");
					mbSet[2] = true;
					motor = 2;
					moneyLable.setText(Integer.toString(money));
				}
			}
		});
		nb[2].addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if ((money >= 3 * 20) || nbSet[2]) {
					if (nbSet[2] == false) {
						money = money - 3 * 20;
					}
					nb[nozzle].setText("take");
					nb[2].setText("got");
					nbSet[2] = true;
					nozzle = 2;
					moneyLable.setText(Integer.toString(money));
				}
			}
		});
		gtb[3].addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if ((money >= 4 * 20) || gtbSet[3]) {
					if (gtbSet[3] == false) {
						money = money - 4 * 20;
					}
					gtb[gasTank].setText("take");
					gtb[3].setText("got");
					gtbSet[3] = true;
					gasTank = 3;
					gTank = gasTank * 5 + 10;
					gasTankLable.setText(Integer.toString(gTank) + "   ");
					moneyLable.setText(Integer.toString(money));
				}
			}
		});
		mb[3].addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if ((money >= 4 * 20) || mbSet[3]) {
					if (mbSet[3] == false) {
						money = money - 4 * 20;
					}
					mb[motor].setText("take");
					mb[3].setText("got");
					mbSet[3] = true;
					motor = 3;
					moneyLable.setText(Integer.toString(money));
				}
			}
		});
		nb[3].addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if ((money >= 4 * 20) || nbSet[3]) {
					if (nbSet[3] == false) {
						money = money - 4 * 20;
					}
					nb[nozzle].setText("take");
					nb[3].setText("got");
					nbSet[3] = true;
					nozzle = 3;
					moneyLable.setText(Integer.toString(money));
				}
			}
		});

		window.setVisible(true);
	}

}
