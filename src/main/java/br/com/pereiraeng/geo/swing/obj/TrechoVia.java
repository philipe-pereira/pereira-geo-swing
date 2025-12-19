package br.com.pereiraeng.geo.swing.obj;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

import br.com.pereiraeng.geo.swing.objetos.GeoStringD;

/**
 * Objeto geográfico que representa um trecho de uma {@link Via}
 * 
 * @author Philipe PEREIRA
 *
 */
public class TrechoVia extends GeoStringD {
	private static final long serialVersionUID = 1L;

	private String nome;

	private String sigla;

	@Override
	public boolean isLoop() {
		return false;
	}

	@Override
	protected Color getColor() {
		return Color.LIGHT_GRAY;
	}

	@Override
	public String toString() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public String getSigla() {
		return sigla;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	private static final Stroke STROKE = new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1f,
			new float[] { 5f, 5f }, 0f);

	@Override
	public void drawObject(Graphics2D g) {
		BasicStroke bs = (BasicStroke) g.getStroke();
		g.setStroke(STROKE);
		super.drawObject(g);
		g.setStroke(bs);
	}
}
