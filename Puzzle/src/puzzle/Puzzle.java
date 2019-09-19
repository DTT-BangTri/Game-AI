package puzzle;

/**
 * 
 * @author Tri Bằng - VUWIT14
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class Puzzle extends JPanel {
	private static final long serialVersionUID = 1L;
	private Image[] imageArray;
	private Image movingImage;
	private int numOfTiles, dimension, imageWidth, imageHeight, panelWidth, panelHeight, r0, r1,
			c0, c1, x, y, movingCoord, sleepMs;
	private boolean animationDone = true;
	private byte[] state;

	public Puzzle(final int numOfTiles) {
		super();
		setNumOfTiles(numOfTiles);
		setSize(this.panelWidth, this.panelHeight);
		setDoubleBuffered(true);
	}

	public void setNumOfTiles(final int numOfTiles) {
		this.numOfTiles = numOfTiles;
		this.dimension = (int) Math.sqrt(numOfTiles);
		this.imageArray = new Image[numOfTiles];
		final String file = "/images/" + (numOfTiles - 1) + "-puzzle/shrksign_";
		this.imageArray[0] = null;
		for (int i = 1; i < numOfTiles; ++i) {
			final StringBuilder builder = new StringBuilder(file);
			if (i <= 9) {
				builder.append("0");
			}
			builder.append(Integer.toString(i));
			builder.append(".gif");
			this.imageArray[i] = ImagePanel.getImage(builder.toString());
		}
		this.imageWidth = this.imageArray[1].getWidth(null);
		this.imageHeight = this.imageArray[1].getHeight(null);
		this.panelWidth = this.imageWidth * this.dimension;
		this.panelHeight = this.imageHeight * this.dimension;
		this.state = new byte[numOfTiles];
		this.sleepMs = 500 / this.imageWidth;
		this.animationDone = true;
		resetOrder();
	}

	public void resetOrder() {
		for (int i = 0; i < this.numOfTiles - 1; ++i) {
			this.state[i] = (byte) (i + 1);
		}
		this.state[this.numOfTiles - 1] = 0;
		repaint();
	}

	public void setOrder(final byte[] state) {
		System.arraycopy(state, 0, this.state, 0, state.length);
		repaint();
	}

	public void stopAnimation() {
		this.animationDone = true;
	}

	public void animatePuzzleTo(final byte[] newState) {
		int newPosOfTile = 0, currentPosOfTile = 0;
		for (int i = 0; i < this.numOfTiles; ++i) {
			if (this.state[i] == 0) {
				newPosOfTile = i;
			}
			if (newState[i] == 0) {
				currentPosOfTile = i;
			}
		}
		this.r0 = newPosOfTile / this.dimension;
		this.c0 = newPosOfTile % this.dimension;
		this.r1 = currentPosOfTile / this.dimension;
		this.c1 = currentPosOfTile % this.dimension;
		this.movingImage = this.imageArray[this.state[currentPosOfTile]];
		System.arraycopy(newState, 0, this.state, 0, newState.length);
		if (this.r0 == this.r1) {
			this.movingCoord = this.c1 * this.imageWidth;
		} else {
			this.movingCoord = this.r1 * this.imageHeight;
		}
		final Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				Puzzle.this.x = Puzzle.this.c0 * Puzzle.this.imageWidth;
				Puzzle.this.y = Puzzle.this.r0 * Puzzle.this.imageHeight;
				Puzzle.this.animationDone = false;
				while (!Puzzle.this.animationDone) {
					if (Puzzle.this.r0 == Puzzle.this.r1) {
						if (Puzzle.this.c1 < Puzzle.this.c0) {
							if (Puzzle.this.movingCoord <= Puzzle.this.x) {
								++Puzzle.this.movingCoord;
							} else {
								Puzzle.this.animationDone = true;
							}
						} else {
							if (Puzzle.this.movingCoord >= Puzzle.this.x) {
								--Puzzle.this.movingCoord;
							} else {
								Puzzle.this.animationDone = true;
							}
						}
					} else {
						if (Puzzle.this.r1 < Puzzle.this.r0) {
							if (Puzzle.this.movingCoord < Puzzle.this.y) {
								++Puzzle.this.movingCoord;
							} else {
								Puzzle.this.animationDone = true;
							}
						} else {
							if (Puzzle.this.movingCoord >= Puzzle.this.y) {
								--Puzzle.this.movingCoord;
							} else {
								Puzzle.this.animationDone = true;
							}
						}
					}
					repaint();
					try {
						Thread.sleep(Puzzle.this.sleepMs);
					} catch (final InterruptedException ie) {
					}
				}
			}
		});
		t.start();
		try {
			t.join();
		} catch (final InterruptedException ie) {
		}
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(this.panelWidth, this.panelHeight);
	}

	@Override
	protected void paintComponent(final Graphics g) {
		int pos = 0;
		for (int row = 0; row < this.dimension; ++row) {
			for (int col = 0; col < this.dimension; ++col) {
				if (this.state[pos] != 0) {
					g.setColor(Color.blue);
					g.fill3DRect(col * this.imageWidth, row * this.imageHeight, this.imageWidth,
							this.imageHeight, true);
					final Image image = this.imageArray[this.state[pos]];
					g.drawImage(image, col * this.imageWidth, row * this.imageHeight,
							this.imageWidth, this.imageHeight, null);
				} else {
					g.setColor(Color.black);
					g.fillRect(col * this.imageWidth, row * this.imageHeight, this.imageWidth,
							this.imageHeight);
				}
				++pos;
			}
		}
		if (!this.animationDone) {
			paintMovingTileRegion(g);
		}
	}

	private void paintMovingTileRegion(final Graphics g) {
		g.setColor(Color.black);
		g.fillRect(this.x, this.y, this.imageWidth, this.imageHeight);
		g.fillRect(this.c1 * this.imageHeight, this.r1 * this.imageWidth, this.imageWidth,
				this.imageHeight);
		g.setColor(Color.blue);
		if (this.r0 == this.r1) {
			g.fill3DRect(this.movingCoord, this.y, this.imageWidth, this.imageHeight, true);
			g.drawImage(this.movingImage, this.movingCoord, this.y, this.imageWidth,
					this.imageHeight, null);
		} else {
			g.fill3DRect(this.x, this.movingCoord, this.imageWidth, this.imageHeight, true);
			g.drawImage(this.movingImage, this.x, this.movingCoord, this.imageWidth,
					this.imageHeight, null);
		}
	}
}
