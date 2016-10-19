package fr.feasil.utils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;


public class Utilitaire 
{
	
	
	/**
	 * 	Transforme un code Hexa en couleur
	 */
	public static Color getColor(String colorHexa)
	{
		if ( colorHexa == null || colorHexa.length() == 0 )
			return null;
		try{
			return Color.decode(colorHexa);
		} catch (NumberFormatException e){
			return null;
		}
	}
	
	/**
	 * Transforme un nom d'image en ImageIcon 
	 */
	public static ImageIcon getImageIcon(String img)
	{
		return new ImageIcon(Utilitaire.class.getResource("/fr/feasil/images/" + img));
	}
	
	
	public static BufferedImage getBufferedImage(String img) throws IOException
	{
		return ImageIO.read(Utilitaire.class.getResource("/fr/feasil/images/" + img));
	}
	
	
	
	// Fichiers
	/**
	 * Copie le contenu d'un fichier vers un autre.
	 */
	public static void copyFile(File srFile, File dtFile) throws IOException
	{
		InputStream in = new FileInputStream(srFile);
		copyFile(in, dtFile);
	}
	
	public static void copyFile(InputStream srFile, File dtFile) throws IOException
	{
		InputStream in = srFile;
		if ( !dtFile.exists() )
			dtFile.createNewFile();
		OutputStream out = new FileOutputStream(dtFile);
		
		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0){
			out.write(buf, 0, len);
		}
		in.close();
		out.close();
	}
	
	
	
	public static byte[] readBinFile(String filename) throws IOException
	{
		File f = new File(filename);
		long filesize = f.length();
		byte data[] =  new byte[(int)filesize];
		DataInputStream in = new DataInputStream(new FileInputStream(f));
		in.readFully(data);
		in.close();
		return data;
	}
	public static void writeBinFile(String filename, byte[] data) throws IOException
	{
		File f = new File(filename);
		DataOutputStream out = new DataOutputStream(new FileOutputStream(f));
		out.write(data);
		out.close();
	}
	
	public static String readTextFile(String filename) throws IOException
	{
		File f = new File(filename);
		long filesize = f.length();
		byte data[] =  new byte[(int)filesize];
		FileInputStream in = new FileInputStream(f);
		in.read(data);
		in.close();
		return new String(data, "UTF8");
	}
	public static void writeTextFile(String filename, String data) throws IOException
	{
		File f = new File(filename);
		writeTextFile(f, data);
	}
	public static void writeTextFile(File file, String data) throws IOException
	{
		FileOutputStream out = new FileOutputStream(file);
		out.write(data.getBytes());
		out.close();
	}
	
	
	
}
