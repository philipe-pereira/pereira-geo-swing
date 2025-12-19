package br.com.pereiraeng.geo.swing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;

import br.com.pereiraeng.geo.GeoCoordinate;
import br.com.pereiraeng.math.Angle;
import br.com.pereiraeng.swing.Grid;
import br.com.pereiraeng.swing.Leaf;
import br.com.pereiraeng.swing.LeafOM;
import br.com.pereiraeng.swing.interfaces.WL;

public class GeoGrid extends Grid {

	private int grids;

	public GeoGrid(int grids) {
		this.grids = grids;
	}

	public void drawGrid(Graphics2D g, Leaf chart, WL wl) {
		GeoMap<?> map = (GeoMap<?>) chart;

		// calcul du pas du quadrillage
		float stepLat = getGridStep(map.getDy());
		float stepLon = getGridStep(map.getDx());

		// premier parallel
		float parallel = map.getY0() - map.getY0() % stepLat;

		// premier meridian
		float meridian = map.getX0() - map.getX0() % stepLon;

		// coordonnées, en pixels, du premier parallel et du premier meridian
		Point firstGridPixel = LeafOM.getTranformedPoint(meridian, parallel, map);

		// pas, en pixels
		Point2D.Float stepPixel = LeafOM.getTranformedPointF(0f, 0f, stepLon, stepLat, map);

		g.setStroke(new BasicStroke());

		// dessiner les méridiens
		for (int i = 0; firstGridPixel.x + stepPixel.x * i < map.getSize().width; i++) {
			String s = GeoCoordinate.toStringLon(meridian);
			g.setColor(getColor(s));

			g.drawLine((int) (firstGridPixel.x + stepPixel.x * i), 0, (int) (firstGridPixel.x + stepPixel.x * i),
					map.getSize().height);

			g.drawString(s, firstGridPixel.x + stepPixel.x * i, 15);
			meridian += stepLon;
		}

		// dessiner les parallèle
		for (int i = 0; firstGridPixel.y - stepPixel.y * i < map.getSize().height; i++) {
			String s = GeoCoordinate.toStringLat(parallel);
			g.setColor(getColor(s));

			g.drawLine(0, (int) (firstGridPixel.y - stepPixel.y * i), map.getSize().width,
					(int) (firstGridPixel.y - stepPixel.y * i));

			g.drawString(s, 0, firstGridPixel.y - stepPixel.y * i);
			parallel -= stepLat;
		}
	}

	private Color getColor(String s) {
		String n = s.substring(0, s.length() - 1);
		if (n.endsWith("''"))
			return LeafOM.lightGray;
		else {
			char c = n.charAt(n.length() - 1);
			if (c == '\'')
				return LeafOM.gray;
			else if (c == Angle.DEGREE || c == '0')
				return LeafOM.black;
			else
				return Color.RED;
		}
	}

	/**
	 * Fonction calculant le pas du quadrillage en fonction de l'intervalle d'angle
	 * de la fenêtre
	 * 
	 * @param size l'intervalle d'angle de la fenêtre
	 * @return l'intervalle d'angle du pas
	 */
	private float getGridStep(float size) {
		if (Math.abs(Angle.getDegrees(size)) < grids) {
			int step = (60 * Math.abs(Angle.getDegrees(size)) + Angle.getMinutes(size));
			if (step < grids) {
				step = (int) (60 * step + Angle.getSeconds(size));
				if (step < grids) {
					return 1 / 3600f;
				}
				step /= grids;
				if (step > 10)
					step -= (step % 10);
				return step / 3600f;
			}
			step /= grids;
			if (step > 10)
				step -= (step % 10);
			return step / 60f;
		} else {
			return ((int) (Angle.getDegrees(size) / grids));
		}
	}
}
