/**
 * 作者：王赫，王凯
 * 本文件代码是王赫，王凯，马凯组成的队伍参加“认证杯”数学建模竞赛解答C1题的代码
 * 时间：2021-4-8 to 2021-4-11
 * 这期间很累，脑子快烧穿了，
 * 好几次认为应该做不出来了，
 * 但是看到结果跑出来那一刻，
 * 心中无比喜悦
 * */

package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Main {
	/**合并之后停车点的数量
	 * */
	private static int SIZE = 143;
	/**停车点合并的范围
	 * */
	private static int DISTANCE = 500;
	/**时间
	 * */
	private static ArrayList<String> timestamp = new ArrayList<String>();
	/**纬度
	 * */
	private static ArrayList<Double> latitude = new ArrayList<Double>();
	/**经度
	 * */
	private static ArrayList<Double> longitude = new ArrayList<Double>();
	/**汽车列表
	 * */
	private static ArrayList<ArrayList<String>> carList = 
			new ArrayList<ArrayList<String>>();
	/**停车点
	 * */
	private static ArrayList<ArrayList<Double>> loction = 
			new ArrayList<ArrayList<Double>>();
	
	public static void main(String[] args) 
	{
//		解题第一步，读取文件，并且对数据进行处理
//		调用readInf()函数
//		第二步：将停车点信息写入文件
//		调用writeData(loction)函数
//		第三步：计算各个点之间车辆移动情况
//		调用Indirection函数
//		第四步：将车辆移动情况写入文件
//		调用writeCarchange(direction)函数
//		第五步：计算马尔可夫链转移矩阵
//		调用getP(direction)函数
//		第六步：将马尔可夫链转移矩阵写入文件
//		调用writeP(p)函数
//		第七步：计算初始列向量
//		调用getX()矩阵
//		第八步：将初始列向量写入文件
//		调用writeX(x)函数		
//		第九步：计算最终的稳态
//		调用getEnd()函数
//		第十步，确定调度方案
//		调用getchangeNumber()函数
//		第十一步：将调度方案写入文件
//		调用writechangeNumber(changeNumber)函数
//		用到的辅助函数：
//		Matrixmultiplication(double[][], double[][])用来计算矩阵乘法
//		getDistance(double, double, double, double)根据经纬度计算两点之间距离
	}
	/**计算车辆调度方案
	 * */
	static double[][] getchangeNumber()
	{
		double[][] xend = readEnd();//把最终稳态读取进来
		int[][] end = new int[SIZE][1];//把稳态列向量中的每一项取整
		for(int i = 0;i < SIZE;i++) 
			end[i][0] = (int)(xend[i][0]);
		int[][] x = readX();//把初始列向量读取进来
		double[][] data = readData();//把停车点坐标读取进来
		double[] change = new double[SIZE];//初始与稳态的差值
		double[][] changeNumber = new double[SIZE][SIZE];
		//记录车辆调度的矩阵，i，j项表示从i点向j点调度的车辆数
		for(int i = 0;i < SIZE;i++) 
			for(int j = 0;j < SIZE;j++) 
				changeNumber[i][j] = 0;//赋初始值为0
		for(int i = 0;i < SIZE;i++) 
			change[i] = x[i][0] - end[i][0];//计算初始与稳态的差值
		for(int i = 0;i < SIZE;i++) 
			if(change[i] > 0) //把差值大于0的停车位的车调走
				while(change[i] > 0.1) 
				{
					//停止调度的终止条件
					int index = 0;
					for(int j = 0;j < SIZE;j++) 
						if(change[j] < 0) 
						{
							//找到第一个差值小于0的点
							index = j;
							break;
						}
					for(int j = index+1;j < SIZE;j++) 
						if(change[j] < 0) 
						{
							if(getDistance(data[i][1], data[i][0], 
									data[index][1], data[index][0])
									>getDistance(data[i][1], data[i][0],
											data[j][1], data[j][0])) 
							{
								//找到距离最近的差值小于0的点
								index = j;
							}
						}
					
					if(change[i]+change[index]>0) 
					{
						changeNumber[i][index] -= change[index];
						change[i] = change[i]+change[index];
						change[index] = 0;
					}else 
					{
						changeNumber[i][index] += change[i];
						change[i] = 0;
						change[index] = change[i]+change[index];
					}
					//计算调度车辆数
				}
			
		
		return changeNumber;
	}
	/**获取初始列向量
	 * */
	static int[][] getX()
	{
		int n = SIZE;
		int[][] x = new int[n][1];//创建列向量
		for(int i = 0;i < n;i++) 
			x[i][0] = 0;//设置初始值为0
		ArrayList<String> nowCar = 
				new ArrayList<String>();
		//已经考察过的车的列表
		for(int i = 0;i < carList.size();i++) 
			for(int j = 0;j < carList.get(i).size();j++)
				if(nowCar.indexOf(carList.get(i).get(j))==-1) 
				{
					//记录每辆车第一次出现的停车点，然后给相应的想数值加一
					nowCar.add(carList.get(i).get(j));
					ArrayList<Double> temp = 
							new ArrayList<Double>();
					temp.add(latitude.get(i));
					temp.add(longitude.get(i));
					int index = getIndex(temp);
					x[index][0]++;
				}
		return x;
	}
	/**初始化停车点之间汽车转移数量的矩阵
	 * */
	static int[][] Indirection()
	{
		int n = SIZE;
		int[][] direction = new int[n][n];//创建矩阵
		for(int i = 0;i < n;i++) 
			for(int j = 0;j < n;j++) 
				direction[i][j] = 0;
		//从一个停车点到另一个停车点转移的车的数量
		//对于每一辆车，考察上一次出现的停车点，然后给对应的项加一
		//这段代码跑了50分钟
		for(int i = 0;i < carList.size();i++) 
			for(int j = 0; j < carList.get(i).size();j++) 	
				for(int k = i+1;k < carList.size();k++) 
					for(int m = j+1;m < carList.get(k).size();m++) 					
						if(carList.get(i).get(j).equals(carList.get(k).get(m))) 
						{
							ArrayList<Double> temp = new ArrayList<Double>();
							temp.add(latitude.get(i));
							temp.add(longitude.get(i));
							//找50米之内的点
							int x = getIndex(temp);
							temp = new ArrayList<Double>();
							temp.add(latitude.get(k));
							temp.add(longitude.get(k));
							//找50米之内的点
							int y = getIndex(temp);
							direction[y][x]++;
						}
		return direction;
	}
	/**计算最终稳态列向量
	 * */
	static double[][] getEnd(double[][] p,int[][] x)
	{
		double[][] dx = new double[x.length][1];
		for(int i = 0;i < dx.length;i++) 
			dx[i][0] = x[i][0];
		double[][] b = Matrixmultiplication(p,dx);
		for(int i = 0;i < 100;i++) 
			b = Matrixmultiplication(p, b);//进行矩阵乘法
		for(int i = 0;i < b.length;i++) 
			b[i][0] = b[i][0] * (313.0/215.0);
		return b;
	}
	/**计算转移矩阵
	 * */
	static double[][] getP(int[][] direction)
	{
		int n = direction.length;
		double[] sum = new double[n];
		//储存每一行的和的数组
		for(int i = 0;i < n;i++)
			sum[i]= 0;
		for(int i = 0;i < n;i++) 
			for(int j = 0; j < n;j++) 
				sum[i] += direction[i][j];		
		double[][] p = new double[n][n];
		for(int i = 0;i < n;i++) 
			for(int j = 0; j < n;j++) 
				p[i][j] = 0;	
		for(int i = 0;i < n;i++) 		
			for(int j = 0; j < n;j++) 			
				if(sum[i]!=0)
					p[i][j] = direction[i][j]/sum[i];
					//每一项等于车辆转移数除以该行的和		
		return p;
	}
	/**矩阵乘法
	 * */
	static double[][] Matrixmultiplication(double[][] a,double[][] b){
		int m = a.length;
		int n = b[0].length;
		double[][] result = new double[m][n];
		for(int i = 0;i < m;i++) 
			for(int j = 0;j < n;j++) 
				result[i][j] = 0;
		for(int i = 0;i < m;i++) 
			for(int j = 0;j < n;j++) 
				for(int k = 0;k < b.length;k++) 
					result[i][j] += a[i][k]*b[k][j];
		return result;
	}
	/**根据经纬度计算距离
	 * */
	static double getDistance(double longitude1, 
			double latitude1, double longitude2, double latitude2) {
        // 纬度
        double lat1 = Math.toRadians(latitude1);
        double lat2 = Math.toRadians(latitude2);
        // 经度
        double lng1 = Math.toRadians(longitude1);
        double lng2 = Math.toRadians(longitude2);
        // 纬度之差
        double a = lat1 - lat2;
        // 经度之差
        double b = lng1 - lng2;
        // 计算两点距离的公式
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(b / 2), 2)));
        // 弧长乘地球半径, 返回单位: 米
        s =  s * 6378.137 * 1000;
        return s;
    }
	/**把调度数据写入文件中
	 * */
	static void writechangeNumber(double[][] changeNumber) {
		File f2 = new File("C:\\Users\\ws_wa\\Documents\\Tencent Files\\2721162853\\FileRecv\\all1\\changeNumber.csv");
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
		int n = SIZE;
		for(int i = 0;i < n;i++) 
		{
			for(int j = 0;j < n;j++) 
			{
				try 
				{
					bw.write(changeNumber[i][j]+",");
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
	/**写入汽车转移数据
	 * */	
	static void writeCarchange(int[][] direction) {
		File f2 = new File("C:\\Users\\ws_wa\\Documents\\Tencent Files\\2721162853\\FileRecv\\all1\\car_change.csv");
		if(f2.exists()) 
		{
			f2.delete();
			try 
			{
				f2.createNewFile();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		else 
		{
			try 
			{
				f2.createNewFile();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		BufferedWriter bw = null;
		try 
		{
			bw = new BufferedWriter(new FileWriter(f2.getPath()));
		} 
		catch (IOException e1) 
		{
			e1.printStackTrace();
		}
		int n = direction.length;
		for(int i = 0;i < n;i++) 
		{
			for(int j = 0;j < n;j++) 
			{
				try 
				{
					bw.write(direction[i][j]+",");
					bw.flush();
				} 
				catch (IOException e) 
				{
					
					e.printStackTrace();
				}
			}
			try {
				bw.newLine();
				bw.flush();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			
		}
	}
	/**读取文件的函数
	 * */
	static void readInf() 
	{
		File f = new File("C:\\Users\\ws_wa\\Documents\\Tencent Files\\2721162853\\FileRecv\\all1\\共享汽车定位数据.csv");
		InputStreamReader reader = null;
		try 
		{
			reader = new InputStreamReader(new FileInputStream(f));
		} 
		catch (FileNotFoundException e1) 
		{
			e1.printStackTrace();
		}
		BufferedReader br = new BufferedReader(reader);
		String line = "";
		try 
		{
			line = br.readLine();
			line = br.readLine();
			while(line!=null) 
			{
				//这个while循环读取数据
				String[] inf = line.split(",");//分割字符串
				timestamp.add(inf[0]);//时间
				latitude.add(Double.valueOf(inf[1]));//经度
				longitude.add(Double.valueOf(inf[2]));//纬度
				ArrayList<Double> temp = new ArrayList<Double>();//停车点的临时一维数组
				temp.add(Double.valueOf(inf[1]));
				temp.add(Double.valueOf(inf[2]));
				//若不是空，则寻找有无距离小于指定距离的点
				//若有，不插入
				boolean have = false;
				for(int i = 0;i < loction.size();i++) 
					if(getDistance(loction.get(i).get(1),
							loction.get(i).get(0),
							temp.get(1),temp.get(0)) <= DISTANCE) 
					{
						have = true;						
						break;
					}
				if(!have) 
					//若无，插入，若字符串为空，则一定运行至该处，则插入
					loction.add(temp);//若没有该停车点，就加入
				//以下代码统计每个停车点的车的列表
				int carnumber = Integer.valueOf(inf[3]);
				ArrayList<String> cartemp = new ArrayList<String>();
				for(int i = 0;i < carnumber;i++) 
					if(carnumber == 1) 
						cartemp.add(inf[4+i].substring(1,inf[4+i].length()-1));
					else if(i == 0)
						cartemp.add(inf[4+i].substring(2));
					else if(i == carnumber-1) 
					{
						inf[4+i] = inf[4+i].trim();
						cartemp.add(inf[4+i].substring(0, inf[4+i].length()-2));
					}
					else 
						cartemp.add(inf[4+i].trim());
				carList.add(cartemp);
				line = br.readLine();
			}
		} 
		catch (IOException e) 
		{ 
			e.printStackTrace();
		}
		try 
		{
			br.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	/**读取初始列向量
	 * */
	static int[][] readX(){
		File f = new File("C:\\Users\\ws_wa\\Documents\\Tencent Files\\2721162853\\FileRecv\\all1\\x.txt");
		InputStreamReader reader = null;
		try 
		{
			reader = new InputStreamReader(new FileInputStream(f));
		} catch (FileNotFoundException e1) 
		{
			e1.printStackTrace();
		}
		int n = SIZE;
		int linec = 0;
		int[][] x = new int[n][1];
		BufferedReader br = new BufferedReader(reader);
		String line = "";
		try 
		{
			line = br.readLine();
			while(line!=null) 
			{
				x[linec][0] = Integer.valueOf(line);
				line = br.readLine();
				linec++;
			}
		} catch (IOException e1) 
		{ 
			e1.printStackTrace();
		}
		try 
		{
			br.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return x;
	}
	/**读取最终稳态
	 * */
	static double[][] readEnd()
	{
		File f = new File("C:\\Users\\ws_wa\\Documents\\Tencent Files\\2721162853\\FileRecv\\all1\\end.txt");
		InputStreamReader reader = null;
		try 
		{
			reader = new InputStreamReader(new FileInputStream(f));
		} 
		catch (FileNotFoundException e1) 
		{ 
			e1.printStackTrace();
		}
		int n = SIZE;
		int linec = 0;
		double[][] end = new double[n][1];
		BufferedReader br = new BufferedReader(reader);
		String line = "";
		try 
		{
			line = br.readLine();
			while(line!=null) 
			{				
				end[linec][0] = Double.valueOf(line);
				line = br.readLine();
				linec++;
			}
		} 
		catch (IOException e1) 
		{
			e1.printStackTrace();
		}
		try 
		{
			br.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return end;
	}
	/**写入停车点数据
	 * */
	static void writeData(ArrayList<ArrayList<Double>> loction) 
	{
		File f2 = new File("C:\\Users\\ws_wa\\Documents\\Tencent Files\\2721162853\\FileRecv\\all1\\data.csv");
		if(f2.exists()) 
		{
			f2.delete();
			try 
			{
				f2.createNewFile();
			} 
			catch (IOException e) 
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
		} 
		catch (IOException e1) 
		{
			e1.printStackTrace();
		}
		try 
		{
			bw.write("latitude,longitude");
			bw.flush();
			bw.newLine();
			bw.flush();
			for(int i = 0;i < loction.size();i++)
			{
				bw.write(loction.get(i).get(0)+","+loction.get(i).get(1));
				bw.flush();
				bw.newLine();
				bw.flush();
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	/**根据经纬度获取该点在停车点列表里的下标
	 * */
	static int getIndex(ArrayList<Double> temp) {
		for(int i = 0;i < loction.size();i++) 
			if(getDistance(loction.get(i).get(1),
					loction.get(i).get(0),
					temp.get(1),temp.get(0)) <= DISTANCE)
			{
				return i;
			}
		return -1;
	}
	/**读取停车点数据
	 * */
	static double[][] readData()
	{
		File f = new File("C:\\Users\\ws_wa\\Documents\\Tencent Files\\2721162853\\FileRecv\\all1\\data.csv");
		InputStreamReader reader = null;
		try 
		{
			reader = new InputStreamReader(new FileInputStream(f));
		} 
		catch (FileNotFoundException e1) 
		{
			e1.printStackTrace();
		}
		int n = SIZE;
		int linec = 0;
		double[][] data = new double[n][2];
		BufferedReader br = new BufferedReader(reader);
		String line = "";
		try 
		{
			line = br.readLine();
			line = br.readLine();
			while(line!=null) 
			{
				String[] str = line.split(",");
				data[linec][0] = Double.valueOf(str[0]);
				data[linec][1] = Double.valueOf(str[1]);
				line = br.readLine();
				linec++;
			}
		} 
		catch (IOException e1) 
		{ 
			e1.printStackTrace();
		}
		try 
		{
			br.close();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		return data;
	}
	/**读取车辆转移矩阵
	 * */
	static int[][] readDirection()
	{
		File f = new File("C:\\Users\\ws_wa\\Documents\\Tencent Files\\2721162853\\FileRecv\\all1\\car_change.csv");
		InputStreamReader reader = null;
		try 
		{
			reader = new InputStreamReader(new FileInputStream(f));
		} catch (FileNotFoundException e1) 
		{
			e1.printStackTrace();
		}
		int n = SIZE;
		int linec = 0;
		int[][] direction = new int[n][n];
		BufferedReader br = new BufferedReader(reader);
		String line = "";
		try 
		{
			line = br.readLine();
			while(line!=null)
			{
				String[] str = line.split(",");
				for(int i = 0;i < n;i++) 
					direction[linec][i] = Integer.valueOf(str[i]);
				line = br.readLine();
				linec++;
			}
		} 
		catch (IOException e1) 
		{ 
			e1.printStackTrace();
		}
		try 
		{
			br.close();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		return direction;
	}
	/**写入马尔科夫链概率转移矩阵
	 * */
	static void writeP(double[][] p) 
	{
		File f2 = new File("C:\\Users\\ws_wa\\Documents\\Tencent Files\\2721162853\\FileRecv\\all1\\p.csv");
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
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		BufferedWriter bw = null;
		try 
		{
			bw = new BufferedWriter(new FileWriter(f2.getPath()));
		} 
		catch (IOException e1) 
		{
			e1.printStackTrace();
		}
		int n = p.length;
		for(int i = 0;i < n;i++) 
		{
			for(int j = 0;j < n;j++)
			{
				try 
				{
					bw.write(p[i][j]+",");
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
	/**写入稳态列向量
	 * */
	static void writeEnd(double[][] end) 
	{
		File f2 = new File("C:\\Users\\ws_wa\\Documents\\Tencent Files\\2721162853\\FileRecv\\all1\\end.txt");
		if(f2.exists()) 
		{
			f2.delete();
			try 
			{
				f2.createNewFile();
			} 
			catch (IOException e)
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
		int n = end.length;
		for(int i = 0;i < n;i++)
		{
			String s = ""+ end[i][0];
			try
			{
				bw.write(s);
				bw.flush();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
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
	/**写入初始列向量
	 * */
	static void writeX(int[][] x) 
	{
		File f2 = new File("C:\\Users\\ws_wa\\Documents\\Tencent Files\\2721162853\\FileRecv\\all1\\x.txt");
		if(f2.exists()) 
		{
			f2.delete();
			try 
			{
				f2.createNewFile();
			} 
			catch (IOException e) 
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
		int n = x.length;
		for(int i = 0;i < n;i++) 
		{
			try 
			{
				String s = "" + x[i][0];
				bw.write(s);
				bw.flush();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}			
			try 
			{
				bw.newLine();
				bw.flush();
			} catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}
}
