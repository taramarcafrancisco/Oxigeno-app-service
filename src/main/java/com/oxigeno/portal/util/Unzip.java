package com.oxigeno.portal.util;
import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.zip.*;

public class Unzip {
	public static final void copyInputStream(InputStream in, OutputStream out)throws IOException
	{
		byte[] buffer = new byte[1024];
		int len;
		while((len = in.read(buffer)) >= 0)
			out.write(buffer, 0, len);
		in.close();
		out.close();
	}

	public static List<File> xtract (String sZip, String sToFolder, String filter) throws IOException {
		Pattern pattern = null;
		if (filter != null)
			pattern = Pattern.compile(filter, Pattern.CASE_INSENSITIVE);
		return xtract (sZip, sToFolder, pattern); 
	}
	public static List<File> xtract (String sZip, String sToFolder, Pattern pattern ) throws IOException {

		Enumeration entries;
		List<File> v = new ArrayList<> ();
		ZipFile zipFile;
		zipFile = new ZipFile(sZip);
		entries = zipFile.entries();
		while(entries.hasMoreElements()) {
			ZipEntry entry = (ZipEntry)entries.nextElement();
			if(entry.isDirectory()) {
				// Assume directories are stored parents first then children.
				//System.err.println("Extracting directory: " + entry.getName());
				// This is not robust, just for demonstration purposes.
				//(new File(entry.getName())).mkdir();
				continue;
			}
			// filtra archivo no permitidos
			if (pattern != null && !pattern.matcher (entry.getName()).matches())
				continue;
			//System.err.println("Extracting file: " + entry.getName());
			String s;
			copyInputStream(zipFile.getInputStream(entry),
					new BufferedOutputStream(
							new FileOutputStream(s = sToFolder + File.separator + entry.getName())
						)
				);
			v.add(new File (s));
		}
		zipFile.close();
		return v;
	}
}
