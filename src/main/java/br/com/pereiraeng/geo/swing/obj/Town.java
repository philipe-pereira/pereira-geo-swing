package br.com.pereiraeng.geo.swing.obj;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import br.com.pereiraeng.geo.GeoCoordinate;
import br.com.pereiraeng.geo.swing.objetos.GeoMarkD;

/**
 * Objeto que representa a sede de uma cidade a ser representada sobre um mapa
 * 
 * @author Philipe PEREIRA
 *
 */
public class Town extends GeoMarkD {
	private static final long serialVersionUID = 1L;

	public Town() {
		super(0f, 0f);
	}

	public Town(GeoCoordinate coordinate) {
		super(coordinate.x, coordinate.y);
	}

	// ------------------------------- DRAWER -------------------------------

	@Override
	public void drawGeo(Graphics2D g, int x, int y) {
		g.setColor(Color.RED);
		g.drawOval(x - 2, y - 2, 4, 4);
		if (name != null) {
			Font f = g.getFont();
			g.setFont(new Font(Font.DIALOG, Font.PLAIN, 10));
			g.drawString(name, x, y);
			g.setFont(f);
		}
	}
}
