/**
 * 作者：王赫，王凯
 * 本文件代码是王赫，王凯，马凯组成的队伍参加“认证杯”数学建模竞赛解答C1题的代码
 * 时间：2021-5-13 to 2021-5-16
 * */
package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Second {

	/**main函数
	 * */
	public static void main(String[] args) {
		
	}
	/**计算各个停车点之间的距离
	 * */
	public static double[][] getDistance()
	{
		double[][] distance = new double[143][143];
		double[][] data = Main.readData();
		for(int i = 0;i < 143;i++) {
			for(int j = 0;j < 143;j++){
				distance[i][j] = Main.getDistance(data[i][1], data[i][0],
						data[j][1], data[j][0]);
			}
		}
 		return distance;
	}
	/**获得分时借车情况
	 * */
	public static void getTimelyNumbers() {
		Main.readInf();
		ArrayList<String> times = new ArrayList<String>();
		//统计不同的时间点个数
		for(int i = 0;i < Main.timestamp.size();i++) {
			if(times.indexOf(Main.timestamp.get(i)) == -1) {
				times.add(Main.timestamp.get(i));
			}
		}
		ArrayList<Integer> split = new ArrayList<Integer>();
		//储存各个时间点的分割点
		for(int i = 0;i < times.size();i++) {
			split.add(Main.timestamp.indexOf(times.get(i)));
		}
		split.add(Main.timestamp.size()-1);
		int[] numbers = new int[times.size()-2];
		//统计分时借车情况，在对每一辆车在后一个时间点遍历，若找不到就加一
		for(int i = 0;i < numbers.length;i++) {
			for(int j = split.get(i);j < split.get(i+1);j++){
				for(int k = 0;k < Main.carList.get(j).size();k++) {
					String car = Main.carList.get(j).get(k);
					boolean isfind = false;
					for(int w = split.get(i+1);w <split.get(i+2);w++) {
						for(int x = 0;x < Main.carList.get(w).size();x++) {
							if(car.equals(Main.carList.get(w).get(x))) {
								isfind = true;
							}
						}
					}
					if(!isfind) {
						numbers[i]++;
					}
				}
			}
		}
		for(int i = 0;i < numbers.length;i++) {
			System.out.println(numbers[i]);
		}
	}
	/**把各个停车点之间的距离写入文件
	 * */
	public static void writeDistance() {
		File f2 = new File
				("C:\\Users\\ws_wa\\Documents\\"
						+ "Tencent Files\\2721162853\\FileRecv"
						+ "\\all1\\distance.csv");
		double[][] distance = getDistance();
		if(f2.exists()) 
		{
			f2.delete();
			try 
			{
				f2.createNewFile();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		else 
		{
			try 
			{
				f2.createNewFile();
			} catch (IOException e)
			{ 
				e.printStackTrace();
			}
		}
		BufferedWriter bw = null;
		try 
		{
			bw = new BufferedWriter(new FileWriter(f2.getPath()));
		} catch (IOException e1) 
		{ 
			e1.printStackTrace();
		}
		int n = 143;
		for(int i = 0;i < n;i++) 
		{
			for(int j = 0;j < n;j++) 
			{
				try 
				{
					bw.write(distance[i][j]+",");
					bw.flush();
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
			try 
			{
				bw.newLine();
				bw.flush();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}	
		}
	}
}
