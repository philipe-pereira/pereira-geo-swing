package br.com.pereiraeng.geo.swing.objetos;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

import br.com.pereiraeng.geo.GeoCoordinate;
import br.com.pereiraeng.geo.objetos.GeoString;
import br.com.pereiraeng.swing.LeafOM;
import br.com.pereiraeng.swing.interfaces.WL;

public abstract class GeoStringD extends GeoString implements GeoD {
	private static final long serialVersionUID = 1333142314567835489L;

	// ------------------------------- DRAWER -------------------------------

	protected transient WL wl;

	@Override
	public void setWL(WL wl) {
		this.wl = wl;
	}

	@Override
	public void drawObject(Graphics2D g) {
		drawGeo(g, -1, -1);
	}

	@Override
	public void drawGeo(Graphics2D g, int x, int y) {
		drawString(g, this, wl, getColor(), isLoop(), toString());
	}

	protected abstract Color getColor();

	@Override
	public boolean isDrawable() {
		return true;
	}

	@Override
	public boolean wasDrawn() {
		return true;
	}

	@Override
	public void setDrawable(boolean drawable) {
	}

	protected static void drawString(Graphics2D g, List<GeoCoordinate> cs, WL wl, Color color, boolean loop,
			String label) {
		g.setColor(color);
		int[][] xy = LeafOM.getPoints(cs, wl);
		if (loop)
			g.drawPolygon(xy[0], xy[1], xy[0].length);
		else
			g.drawPolyline(xy[0], xy[1], xy[0].length);
		if (label != null) {
			int x, y, mid = xy[0].length / 2;
			if (xy[0].length % 2 == 0) {
				x = (xy[0][mid - 1] + xy[0][mid]) / 2;
				y = (xy[1][mid - 1] + xy[1][mid]) / 2;
			} else {
				x = xy[0][mid];
				y = xy[1][mid];
			}
			g.drawString(label, x, y);
		}

		for (GeoCoordinate gc : cs) {
			if (gc instanceof GeoPointD) {
				GeoPointD geoPoint = (GeoPointD) gc;
				geoPoint.setWL(wl);
				geoPoint.drawObject(g);
			}
		}
	}
}
