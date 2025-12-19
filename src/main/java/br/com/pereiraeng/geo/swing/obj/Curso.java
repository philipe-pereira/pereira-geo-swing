package br.com.pereiraeng.geo.swing.obj;

import java.awt.Color;

import br.com.pereiraeng.geo.swing.objetos.GeoStringD;

/**
 * Objeto geográfico que representa um curso hidrográfico de um {@link Rio}
 * 
 * @author Philipe PEREIRA
 *
 */
public class Curso extends GeoStringD {
	private static final long serialVersionUID = 1L;

	private String nome;

	private String corpo;

	@Override
	public boolean isLoop() {
		return false;
	}

	@Override
	protected Color getColor() {
		return Color.BLUE;
	}

	@Override
	public String toString() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	public void setCorpo(String data) {
		this.corpo = data;
	}

	public String getCorpo() {
		return corpo;
	}
}