package iotwechat.xlink.cloud.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipFile {

	public static final String ZIP_FILENAME = ""; // 需要解压缩的文件名
	public static final String ZIP_DIR = ""; // 需要压缩的文件夹
	public static final String UN_ZIP_DIR = ""; // 要解压的文件目录
	public static final int BUFFER = 1024; // 缓存大小

	public static void zipFile(String baseDir, String fileName)
			throws Exception {
		List fileList = getSubFiles(new File(baseDir));
		ZipOutputStream zos = new ZipOutputStream(
				new FileOutputStream(fileName));
		ZipEntry ze = null;
		byte[] buf = new byte[BUFFER];
		int readLen = 0;
		for (int i = 0; i < fileList.size(); i++) {
			File f = (File) fileList.get(i);
			ze = new ZipEntry(getAbsFileName(baseDir, f));
			ze.setSize(f.length());
			ze.setTime(f.lastModified());
			zos.putNextEntry(ze);
			InputStream is = new BufferedInputStream(new FileInputStream(f));
			while ((readLen = is.read(buf, 0, BUFFER)) != -1) {
				zos.write(buf, 0, readLen);
			}
			is.close();
		}
		zos.close();
	}

	private static String getAbsFileName(String baseDir, File realFileName) {
		File real = realFileName;
		File base = new File(baseDir);
		String ret = real.getName();
		while (true) {
			real = real.getParentFile();
			if (real == null)
				break;
			if (real.equals(base))
				break;
			else
				ret = real.getName() + "/" + ret;
		}
		return ret;
	}

	private static List getSubFiles(File baseDir) {
		List ret = new ArrayList();
		File[] tmp = baseDir.listFiles();
		for (int i = 0; i < tmp.length; i++) {
			if (tmp[i].isFile())
				ret.add(tmp[i]);
			if (tmp[i].isDirectory())
				ret.addAll(getSubFiles(tmp[i]));
		}
		return ret;
	}
	
	public static void downloadZip(File srcFile,String desFile) {
        try {
        // 以流的形式下载文件。
        InputStream fis = new BufferedInputStream(new FileInputStream(srcFile.getPath()));
        byte[] buffer = new byte[fis.available()];
        fis.read(buffer);
        fis.close();

        OutputStream toClient = new BufferedOutputStream(new FileOutputStream(desFile));
        toClient.write(buffer);
        toClient.flush();
        toClient.close();
        } catch (Exception ex) {
        ex.printStackTrace();
        }finally{
             try {
                    File f = new File(srcFile.getPath());
                    f.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }
	

	public static void main(String[] args) throws Exception {
		//ZipFile.zipFile("d://test/", "d://test.zip");
		downloadZip(new File ("d://test.zip"),"d://test/test.zip");
	}
}