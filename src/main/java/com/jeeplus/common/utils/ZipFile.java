package com.jeeplus.common.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jeeplus.modules.device.entity.WashDevice;

public class ZipFile {
	
	public static void exportZip(List<WashDevice> wdList, HttpServletRequest req, HttpServletResponse resp){
		try {
			/**这个集合就是你想要打包的所有文件，
			 * 这里假设已经准备好了所要打包的文件*/
			String strDirPath = req.getSession().getServletContext().getRealPath("/");
			//String path = strDirPath+CommonValue.ErrorFilePath + File.separator +"noPicCardNumber.txt";
			List<File> files = new ArrayList<File>();
			if(wdList != null){
				int i=1;
				for(WashDevice washDevice:wdList){
					
					File descFile = new File(strDirPath + File.separator + "qrcode" + File.separator + washDevice.getMac() + ".png");
					if(descFile.exists()){
						files.add(descFile);
					}
					i++;
				}
			}
 
			/**创建一个临时压缩文件，
			 * 我们会把文件流全部注入到这个文件中
			 * 这里的文件你可以自定义是.rar还是.zip*/
			String realPath = strDirPath + File.separator + "zip" + File.separator;
			FileUtils.createDirectory(realPath);
			String zipPath = realPath + "zip-"+String.valueOf(System.currentTimeMillis()).substring(4, 13)+".rar";
			File file = new File(zipPath);
			if (!file.exists()){   
			    file.createNewFile();   
			}
			resp.reset();
			//response.getWriter()
			//创建文件输出流
			FileOutputStream fous = new FileOutputStream(file);   
			/**打包的方法我们会用到ZipOutputStream这样一个输出流,
			 * 所以这里我们把输出流转换一下*/
			ZipOutputStream zipOut = new ZipOutputStream(fous);
			/**这个方法接受的就是一个所要打包文件的集合，
			 * 还有一个ZipOutputStream*/
            zipFile(files, zipOut);
			zipOut.close();
			fous.close();
			downloadZip(file,resp);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

  /**
     * 把接受的全部文件打成压缩包 
     * @param List<File>;  
     * @param org.apache.tools.zip.ZipOutputStream  
     */
    public static void zipFile
            (List files,ZipOutputStream outputStream) {
        int size = files.size();
        for(int i = 0; i < size; i++) {
            File file = (File) files.get(i);
            zipFile(file, outputStream);
        }
    }

    public static HttpServletResponse downloadZip(File file,HttpServletResponse response) {
        try {
        // 以流的形式下载文件。
        InputStream fis = new BufferedInputStream(new FileInputStream(file.getPath()));
        byte[] buffer = new byte[fis.available()];
        fis.read(buffer);
        fis.close();
        // 清空response
        response.reset();

        OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
        response.setContentType("application/octet-stream");

    	//如果输出的是中文名的文件，在此处就要用URLEncoder.encode方法进行处理
        response.setHeader("Content-Disposition", "attachment;filename=" +URLEncoder.encode(file.getName(), "UTF-8"));
        toClient.write(buffer);
        toClient.flush();
        toClient.close();
        } catch (IOException ex) {
        ex.printStackTrace();
        }finally{
             try {
                    File f = new File(file.getPath());
                    f.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
        return response;
    }

/**  
     * 根据输入的文件与输出流对文件进行打包
     * @param File
     * @param org.apache.tools.zip.ZipOutputStream
     */
    public static void zipFile(File inputFile,
            ZipOutputStream ouputStream) {
        try {
            if(inputFile.exists()) {
                /**如果是目录的话这里是不采取操作的，
                 * 至于目录的打包正在研究中*/
                if (inputFile.isFile()) {
                    FileInputStream IN = new FileInputStream(inputFile);
                    BufferedInputStream bins = new BufferedInputStream(IN, 512);
                    //org.apache.tools.zip.ZipEntry
                    ZipEntry entry = new ZipEntry(inputFile.getName());
                    ouputStream.putNextEntry(entry);
                    // 向压缩文件中输出数据   
                    int nNumber;
                    byte[] buffer = new byte[512];
                    while ((nNumber = bins.read(buffer)) != -1) {
                        ouputStream.write(buffer, 0, nNumber);
                    }
                    // 关闭创建的流对象   
                    bins.close();
                    IN.close();
                } else {
                    try {
                        File[] files = inputFile.listFiles();
                        for (int i = 0; i < files.length; i++) {
                            zipFile(files[i], ouputStream);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
