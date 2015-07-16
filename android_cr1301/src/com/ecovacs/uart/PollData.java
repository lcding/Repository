package com.ecovacs.uart;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

import android.os.Environment;

public class PollData {
	

	//保存文件
	public static void save(String msg,String filename){//传入要保存的字符串，要保存的文件名

		boolean append;
		try{
			if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){  //如果SD卡已准备好
				msg = msg+"\n";//换行
				filename =filename+".txt";   //在文件名末尾加上.txt									
				File sdCardDir = Environment.getExternalStorageDirectory();  //得到SD卡根目录
				File BuildDir = new File(sdCardDir, "/data");   //打开data目录，如不存在则生成
				if(BuildDir.exists()==false)BuildDir.mkdirs();
				if(BuildDir.exists()){
					append = true;
				}else{
					append = false;
				}
				File saveFile =new File(BuildDir, filename);  //新建文件句柄，如已存在仍新建文档
				//FileOutputStream stream = new FileOutputStream(saveFile);  //打开文件输入流				
				FileOutputStream stream = new FileOutputStream(saveFile,append);  //打开文件输入流,通过后面判断是否接着文件内容写
				stream.write(msg.getBytes());
				stream.close();					
			}else{}

		}catch(IOException e){
			return;
		}
	}

	//删除文件
	public static void deleteAllFile(String filename){
		try{
			if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){  //如果SD卡已准备好

				filename =filename+".txt";   //在文件名末尾加上.txt									
				File sdCardDir = Environment.getExternalStorageDirectory();  //得到SD卡根目录
				File BuildDir = new File(sdCardDir, "/data");   //打开data目录，如不存在则生成
				if(BuildDir.exists()==false)BuildDir.mkdirs();			
				File saveFile =new File(BuildDir, filename);  //新建文件句柄，如已存在仍新建文档
				FileOutputStream stream = new FileOutputStream(saveFile);  //打开文件输入流
				//stream.write(null);
				stream.close();					
			}else{}
		}catch(IOException e){
			return;
		}
	}
	//读文件,得到需要轮询的短地址
	public static Vector<String> read(String filename){//传入路径字符串
		File sdCardDir = Environment.getExternalStorageDirectory();  //得到SD卡根目录

		Vector<String> short_address = new Vector<String>();
		String path = sdCardDir.getAbsolutePath();

		String lineTxt = null;
		String filePath =path + "/data/" + filename;
		try { 
			String encoding="GBK"; //中文
			File file=new File(filePath); 
			if(file.isFile() && file.exists()){ //判断文件是否存在 
				InputStreamReader read = new InputStreamReader( 
						new FileInputStream(file),encoding);//考虑到编码格式 
				BufferedReader bufferedReader = new BufferedReader(read); 
				 
				while((lineTxt = bufferedReader.readLine()) != null){ //bufferedReader.readLine()读一行
					short_address.add(lineTxt);
				} 
				read.close(); 
			}else{ 
				System.out.println("***************can not find the file"); 
			} 
		} catch (Exception e) { 
			System.out.println("******************error is occur when read the file"); 
			e.printStackTrace(); 
		} 
		
		//if(short_address.isEmpty()) short_address.add("1111");
		return short_address;
	}
}
