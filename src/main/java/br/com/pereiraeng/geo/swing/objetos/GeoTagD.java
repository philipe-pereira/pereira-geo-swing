package br.com.pereiraeng.geo.swing.objetos;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import br.com.pereiraeng.geo.objetos.GeoTag;
import br.com.pereiraeng.swing.LeafOM;
import br.com.pereiraeng.swing.interfaces.WL;

public class GeoTagD extends GeoTag implements GeoD {
	private static final long serialVersionUID = -106203389201994390L;

	public GeoTagD(double longitude, double latitude, int xTag, int yTag, String tag) {
		super(longitude, latitude, xTag, yTag, tag);
	}

	private Color color;

	public void setColor(Color color) {
		this.color = color;
	}

	public Color getColor() {
		return color;
	}

	protected transient WL wl;

	@Override
	public void setWL(WL wl) {
		this.wl = wl;
	}

	@Override
	public boolean isDrawable() {
		return true;
	}

	@Override
	public void setDrawable(boolean drawable) {
	}

	@Override
	public boolean wasDrawn() {
		return true;
	}

	@Override
	public void drawObject(Graphics2D g) {
		Point p = LeafOM.getTranformedPoint(x, y, wl);
		drawGeo(g, p.x, p.y);
	}

	@Override
	public void drawGeo(Graphics2D g, int x, int y) {
		Color c = null;
		if (color != null) {
			c = g.getColor();
			g.setColor(color);
		}

		g.drawLine(x, y, xTag, yTag);
		g.drawString(getDescription(), xTag, yTag);

		if (c != null)
			g.setColor(c);
	}
}
