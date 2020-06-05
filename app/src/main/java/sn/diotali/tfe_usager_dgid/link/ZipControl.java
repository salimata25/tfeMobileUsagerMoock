package sn.diotali.tfe_usager_dgid.link;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by lixc on 2017/6/26.
 */

public class ZipControl {

    private static boolean isCreateSrcDir = false;//是否创建源目录
    private static final String TAG = "ZipControl";

    private ZipControl() {

    }

    /**
     * [对指定路径下文件的压缩处理]<BR>
     * [功能详细描述]
     *
     * @param src     径地址
     * @param archive 指定到压缩文件夹的路径
     * @throws IOException IO输入异常
     */
    public static void writeByApacheZipOutputStream(String[] src, String archive)
            throws IOException {
        Log.e(TAG, "writeByApacheZipOutputStream");
        //----压缩文件：
        FileOutputStream fileOutputStream = null;
        CheckedOutputStream checkedOutputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(archive);
            //使用指定校验和创建输出流
            checkedOutputStream = new CheckedOutputStream(fileOutputStream, new CRC32());
            ZipOutputStream zipOutputStream = new ZipOutputStream(checkedOutputStream);
            bufferedOutputStream = new BufferedOutputStream(zipOutputStream);
            //启用压缩
            zipOutputStream.setMethod(ZipOutputStream.DEFLATED);
            //压缩级别为最强压缩，但时间要花得多一点
            zipOutputStream.setLevel(Deflater.BEST_COMPRESSION);
            // 如果为单个文件的压缩在这里修改
            for (int i = 0; i < src.length; i++) {
                Log.e(TAG, "src[" + i + "] is " + src[i]);
                File srcFile = new File(src[i]);
                if (!srcFile.exists()
                        || (srcFile.isDirectory() && srcFile.list().length == 0)) {
                    Log.e(TAG, "!srcFile.exists()");
                    throw new FileNotFoundException(
                            "File must exist and ZIP file must have at least one entry.");
                }
                String strSrcString = src[i];
                //获取压缩源所在父目录
                strSrcString = strSrcString.replaceAll("////", "/");
                String prefixDir;
                if (srcFile.isFile()) {
                    prefixDir = strSrcString.substring(0, strSrcString.lastIndexOf('/') + 1);
                } else {
                    prefixDir = (strSrcString.replaceAll("/$", "") + "/");
                }
                //如果不是根目录
                if (prefixDir.indexOf('/') != (prefixDir.length() - 1) && isCreateSrcDir) {
                    prefixDir = prefixDir.replaceAll("[^/]+/$", "");
                }
                //开始压缩
                writeRecursive(zipOutputStream, bufferedOutputStream, srcFile, prefixDir);
            }
        } finally {
            if (bufferedOutputStream != null) {
                bufferedOutputStream.close();
            }
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
            // 注：校验和要在流关闭后才准备，一定要放在流被关闭后使用
            if (checkedOutputStream != null) {
                Log.e(TAG, "Checksum: " + checkedOutputStream.getChecksum().getValue());
            }

        }
    }

    /**
     * [递归压缩
     * <p>
     * 使用 org.apache.tools.zip.ZipOutputStream 类进行压缩，它的好处就是支持中文路径， 而Java类库中的
     * java.util.zip.ZipOutputStream 压缩中文文件名时压缩包会出现乱码。 使用 apache 中的这个类与 java
     * 类库中的用法是一新的，只是能设置编码方式了。]<BR>
     * [功能详细描述]
     */
    private static void writeRecursive(ZipOutputStream zipOutputStream, BufferedOutputStream bufferedOutputStream, File srcFile, String prefixDir)
            throws IOException {
        Log.e(TAG, "writeRecursive");
        ZipEntry zipEntry;
        String filePath = srcFile.getAbsolutePath().replaceAll("////", "/")
                .replaceAll("//", "/");
        if (srcFile.isDirectory()) {
            filePath = filePath.replaceAll("/$", "") + '/';
        }
        String entryName = filePath.replace(prefixDir, "").replaceAll("/$", "");
        if (srcFile.isDirectory()) {
            if (!"".equals(entryName)) {
                Log.e(TAG, "正在创建目录 - " + srcFile.getAbsolutePath()
                        + " entryName=" + entryName);
                //如果是目录，则需要在写目录后面加上 /
                zipEntry = new ZipEntry(entryName + "/");
                zipOutputStream.putNextEntry(zipEntry);
            }
            File[] srcFiles = srcFile.listFiles();
            for (File file : srcFiles) {
                writeRecursive(zipOutputStream, bufferedOutputStream, file, prefixDir);
            }
        } else {
            Log.e(TAG, "正在写文件 - " + srcFile.getAbsolutePath()
                    + " entryName=" + entryName);
            FileInputStream fileInputStream = null;
            BufferedInputStream bufferedInputStream = null;
            try {
                fileInputStream = new FileInputStream(srcFile);
                bufferedInputStream = new BufferedInputStream(fileInputStream);
                //开始写入新的ZIP文件条目并将流定位到条目数据的开始处
                zipEntry = new ZipEntry(entryName);
                zipOutputStream.putNextEntry(zipEntry);
                byte[] buffer = new byte[1024];
                int readCount = bufferedInputStream.read(buffer);
                while (readCount != -1) {
                    bufferedOutputStream.write(buffer, 0, readCount);
                    readCount = bufferedInputStream.read(buffer);
                }
                //注，在使用缓冲流写压缩文件时，一个条件完后一定要刷新一把，不
                //然可能有的内容就会存入到后面条目中去了
                bufferedOutputStream.flush();
            } finally {
                //文件读完后关闭
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
            }
        }
    }
}
