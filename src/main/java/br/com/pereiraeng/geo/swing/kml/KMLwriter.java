package br.com.pereiraeng.geo.swing.kml;

import java.awt.Color;
import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.tree.DefaultMutableTreeNode;

import br.com.pereiraeng.geo.objetos.Geo;
import br.com.pereiraeng.io.IOutils;

public class KMLwriter {

	private static final String HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n\t<Document>\n",
			FOOTER = "\t</Document>\n</kml>\n";

	/**
	 * Função que cria o arquivo no formato .kml com o conjunto de pontos
	 * geográficos
	 * 
	 * @param file arquivo a ser criado
	 * @param geos tabela que associa para cada nome identificador do local
	 *             geográfico suas coordenadas
	 * @param id   se <code>true</code>, cria-se o campo 'id' como atributo do
	 *             Placemark, que é numerado de acordo com a ordem dos elementos
	 */
	public static void writeKML(File file, Collection<? extends Geo> geos) {
		StringBuilder out = new StringBuilder(HEADER);

		for (Geo g : geos)
			out.append(g.getKML());

		out.append(FOOTER);
		IOutils.writeFile(file, out.toString());
	}

	/**
	 * 
	 * @param dest   pasta de destino do(s) arquivo(s) KML a serem criados
	 * @param root   raiz das pastas do arquivo KML
	 * @param geos   tabela de dispersão que associa para cada nó da raiz a lista de
	 *               objeto geográficos a serem escritos
	 * @param split
	 * @param styles tabela de dispersão com os estilos das linhas
	 */
	public static void writeKML(File dest, DefaultMutableTreeNode root, Map<DefaultMutableTreeNode, List<Geo>> geos,
			boolean split, Map<String, Object[][]> styles) {
		if (split) {
			// se for para gerar vários arquivos
			for (int i = 0; i < root.getChildCount(); i++) {
				StringBuilder out = new StringBuilder(HEADER);

				DefaultMutableTreeNode folder = (DefaultMutableTreeNode) root.getChildAt(i);
				out.append(writeKML(folder, geos, split));

				if (styles != null)
					out.append(getStyle(styles));

				out.append(FOOTER);
				IOutils.writeFile2(new File(dest.getAbsolutePath() + "/" + folder.getUserObject() + ".kml"), out.toString());
			}
		} else {
			// se for um arquivo único
			StringBuilder out = new StringBuilder(HEADER);

			for (int i = 0; i < root.getChildCount(); i++)
				out.append(writeKML((DefaultMutableTreeNode) root.getChildAt(i), geos, split));

			if (styles != null)
				out.append(getStyle(styles));

			out.append(FOOTER);
			IOutils.writeFile2(new File(dest.getAbsolutePath() + "/" + root.getUserObject() + ".kml"), out.toString());
		}
	}

	private static String writeKML(DefaultMutableTreeNode root, Map<DefaultMutableTreeNode, List<Geo>> geos,
			boolean split) {
		StringBuilder out = new StringBuilder("<Folder>\n<name>" + root.getUserObject() + "</name>\n");

		// placemarks deste nó
		List<? extends Geo> gs = geos.get(root);
		if (gs != null)
			for (Geo g : gs)
				out.append(g.getKML());

		// filhos deste nó
		for (int i = 0; i < root.getChildCount(); i++)
			out.append(writeKML((DefaultMutableTreeNode) root.getChildAt(i), geos, split));

		out.append("</Folder>\n");
		return out.toString();
	}

	private static String getStyle(Map<String, Object[][]> styles) {
		StringBuilder out = new StringBuilder();

		for (Entry<String, Object[][]> e : styles.entrySet()) {
			String styleName = e.getKey();
			Object[][] normalHighlight = e.getValue();

			out.append("<Style id='" + styleName + "N'>\n<LineStyle>\n");
			out.append("<color>" + getColorCode((Color) normalHighlight[0][0]) + "</color>\n");
			out.append("<width>" + ((Float) normalHighlight[0][1]) + "</width>\n");
			out.append("</LineStyle>\n</Style>\n");

			out.append("<Style id='" + styleName + "H'>\n<LineStyle>\n");
			out.append("<color>" + getColorCode((Color) normalHighlight[1][0]) + "</color>\n");
			out.append("<width>" + ((Float) normalHighlight[1][1]) + "</width>\n");
			out.append("</LineStyle>\n</Style>\n");

			out.append("<StyleMap id='" + styleName + "'>\n");
			out.append("<Pair>\n<key>normal</key>\n");
			out.append("<styleUrl>#" + styleName + "N</styleUrl>\n");
			out.append("</Pair>\n");
			out.append("<Pair>\n<key>highlight</key>\n");
			out.append("<styleUrl>#" + styleName + "H</styleUrl>\n");
			out.append("</Pair>\n");
			out.append("</StyleMap>\n");
		}

		return out.toString();
	}

	private static String getColorCode(Color color) {
		return String.format("ff%02x%02x%02x", color.getBlue(), color.getGreen(), color.getRed());
	}
}
