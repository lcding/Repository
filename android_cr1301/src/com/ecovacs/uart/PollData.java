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
	

	//�����ļ�
	public static void save(String msg,String filename){//����Ҫ������ַ�����Ҫ������ļ���

		boolean append;
		try{
			if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){  //���SD����׼����
				msg = msg+"\n";//����
				filename =filename+".txt";   //���ļ���ĩβ����.txt									
				File sdCardDir = Environment.getExternalStorageDirectory();  //�õ�SD����Ŀ¼
				File BuildDir = new File(sdCardDir, "/data");   //��dataĿ¼���粻����������
				if(BuildDir.exists()==false)BuildDir.mkdirs();
				if(BuildDir.exists()){
					append = true;
				}else{
					append = false;
				}
				File saveFile =new File(BuildDir, filename);  //�½��ļ���������Ѵ������½��ĵ�
				//FileOutputStream stream = new FileOutputStream(saveFile);  //���ļ�������				
				FileOutputStream stream = new FileOutputStream(saveFile,append);  //���ļ�������,ͨ�������ж��Ƿ�����ļ�����д
				stream.write(msg.getBytes());
				stream.close();					
			}else{}

		}catch(IOException e){
			return;
		}
	}

	//ɾ���ļ�
	public static void deleteAllFile(String filename){
		try{
			if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){  //���SD����׼����

				filename =filename+".txt";   //���ļ���ĩβ����.txt									
				File sdCardDir = Environment.getExternalStorageDirectory();  //�õ�SD����Ŀ¼
				File BuildDir = new File(sdCardDir, "/data");   //��dataĿ¼���粻����������
				if(BuildDir.exists()==false)BuildDir.mkdirs();			
				File saveFile =new File(BuildDir, filename);  //�½��ļ���������Ѵ������½��ĵ�
				FileOutputStream stream = new FileOutputStream(saveFile);  //���ļ�������
				//stream.write(null);
				stream.close();					
			}else{}
		}catch(IOException e){
			return;
		}
	}
	//���ļ�,�õ���Ҫ��ѯ�Ķ̵�ַ
	public static Vector<String> read(String filename){//����·���ַ���
		File sdCardDir = Environment.getExternalStorageDirectory();  //�õ�SD����Ŀ¼

		Vector<String> short_address = new Vector<String>();
		String path = sdCardDir.getAbsolutePath();

		String lineTxt = null;
		String filePath =path + "/data/" + filename;
		try { 
			String encoding="GBK"; //����
			File file=new File(filePath); 
			if(file.isFile() && file.exists()){ //�ж��ļ��Ƿ���� 
				InputStreamReader read = new InputStreamReader( 
						new FileInputStream(file),encoding);//���ǵ������ʽ 
				BufferedReader bufferedReader = new BufferedReader(read); 
				 
				while((lineTxt = bufferedReader.readLine()) != null){ //bufferedReader.readLine()��һ��
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
