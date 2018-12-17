package com.utry.openticket.util;

import java.io.File;
import java.util.List;

public class FileUtil {

	/**
	 * 批量删除文件
	 * @param paths
	 */
	public static void delFileByPath(String[] paths){
		for(int i=0;i<paths.length;i++){
			File file=new File(paths[i]);
			if(file.exists()){
				file.delete();
			}
		}
	}
	/**
	 * 批量删除文件
	 * @param paths
	 */
	public static void delFileByPath(List<String> list){
		for(int i=0;i<list.size();i++){
			File file=new File(list.get(i));
			if(file.exists()){
				file.delete();
			}
		}
	}
}
