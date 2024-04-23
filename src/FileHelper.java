import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class FileHelper {
	public static void unzipFile(String zipFilePath, File newDestinationFolder, String[] filesToExtract)
			throws IOException {
		byte[] buffer = new byte[1024];

		ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFilePath));

		ZipEntry zipEntry = getNext(zis);
		

		while (zipEntry != null) {
			if (!isInFolder(zipEntry.getName(), filesToExtract)) {
				zipEntry = getNext(zis);
				continue;
			}
			
			File newFile = createNewFile(newDestinationFolder, zipEntry);
			
			if (zipEntry.isDirectory()) {
				if (!newFile.isDirectory() && !newFile.mkdirs()) {
					zis.close();
					throw new IOException("Failed to create directory " + newFile);
				}
			} else {
				// fix for Windows-created archives
				File parent = newFile.getParentFile();
				if (!parent.isDirectory() && !parent.mkdirs()) {
					zis.close();
					throw new IOException("Failed to create directory " + parent);
				}

				// write file content
				FileOutputStream fos = new FileOutputStream(newFile);
				int len;
				while ((len = zis.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}
				fos.close();
			}

			zipEntry = getNext(zis);
				
		}

		zis.closeEntry();
		zis.close();
	}
	
	private static ZipEntry getNext(ZipInputStream zis) {
		ZipEntry zipEntry;
		while(true) {
			try {
				zipEntry = zis.getNextEntry();
				break;
			}
			catch(Exception e) {
				continue;
			}
		}
		return zipEntry;
	}

	private static File createNewFile(File destinationDir, ZipEntry zipEntry) throws IOException {
		File destFile = new File(destinationDir, zipEntry.getName());

		String destDirPath = destinationDir.getCanonicalPath();
		String destFilePath = destFile.getCanonicalPath();

		if (!destFilePath.startsWith(destDirPath + File.separator)) {
			throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
		}

		return destFile;
	}

	public static String zipFile(String sourceFile, String outputName) throws IOException {
		FileOutputStream fos = new FileOutputStream(outputName + ".zip");
		ZipOutputStream zipOut = new ZipOutputStream(fos);

		File fileToZip = new File(sourceFile);
		FileInputStream fis = new FileInputStream(fileToZip);
		ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
		zipOut.putNextEntry(zipEntry);

		byte[] bytes = new byte[1024];
		int length;
		while ((length = fis.read(bytes)) >= 0) {
			zipOut.write(bytes, 0, length);
		}

		zipOut.close();
		fis.close();
		fos.close();

		return null;
	}

	private static boolean isInFolder(String element, String[] array) {
		for (String data : array) {
			if(data.endsWith(element)) {
				return true;
			}
		}
		return false;
	}

}
