/**
 * ���ߣ����գ�����
 * ���ļ����������գ�����������ɵĶ���μӡ���֤������ѧ��ģ�������C1��Ĵ���
 * ʱ�䣺2021-5-13 to 2021-5-16
 * */
package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Second {

	/**main����
	 * */
	public static void main(String[] args) {
		
	}
	/**�������ͣ����֮��ľ���
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
	/**��÷�ʱ�賵���
	 * */
	public static void getTimelyNumbers() {
		Main.readInf();
		ArrayList<String> times = new ArrayList<String>();
		//ͳ�Ʋ�ͬ��ʱ������
		for(int i = 0;i < Main.timestamp.size();i++) {
			if(times.indexOf(Main.timestamp.get(i)) == -1) {
				times.add(Main.timestamp.get(i));
			}
		}
		ArrayList<Integer> split = new ArrayList<Integer>();
		//�������ʱ���ķָ��
		for(int i = 0;i < times.size();i++) {
			split.add(Main.timestamp.indexOf(times.get(i)));
		}
		split.add(Main.timestamp.size()-1);
		int[] numbers = new int[times.size()-2];
		//ͳ�Ʒ�ʱ�賵������ڶ�ÿһ�����ں�һ��ʱ�����������Ҳ����ͼ�һ
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
	/**�Ѹ���ͣ����֮��ľ���д���ļ�
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
