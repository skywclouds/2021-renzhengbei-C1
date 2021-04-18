/**
 * ���ߣ����գ�����
 * ���ļ����������գ�����������ɵĶ���μӡ���֤������ѧ��ģ�������C1��Ĵ���
 * ʱ�䣺2021-4-8 to 2021-4-11
 * ���ڼ���ۣ����ӿ��մ��ˣ�
 * �ü�����ΪӦ�����������ˣ�
 * ���ǿ�������ܳ�����һ�̣�
 * �����ޱ�ϲ��
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
	/**�ϲ�֮��ͣ���������
	 * */
	private static int SIZE = 143;
	/**ͣ����ϲ��ķ�Χ
	 * */
	private static int DISTANCE = 500;
	/**ʱ��
	 * */
	private static ArrayList<String> timestamp = new ArrayList<String>();
	/**γ��
	 * */
	private static ArrayList<Double> latitude = new ArrayList<Double>();
	/**����
	 * */
	private static ArrayList<Double> longitude = new ArrayList<Double>();
	/**�����б�
	 * */
	private static ArrayList<ArrayList<String>> carList = 
			new ArrayList<ArrayList<String>>();
	/**ͣ����
	 * */
	private static ArrayList<ArrayList<Double>> loction = 
			new ArrayList<ArrayList<Double>>();
	
	public static void main(String[] args) 
	{
//		�����һ������ȡ�ļ������Ҷ����ݽ��д���
//		����readInf()����
//		�ڶ�������ͣ������Ϣд���ļ�
//		����writeData(loction)����
//		�����������������֮�䳵���ƶ����
//		����Indirection����
//		���Ĳ����������ƶ����д���ļ�
//		����writeCarchange(direction)����
//		���岽����������ɷ���ת�ƾ���
//		����getP(direction)����
//		��������������ɷ���ת�ƾ���д���ļ�
//		����writeP(p)����
//		���߲��������ʼ������
//		����getX()����
//		�ڰ˲�������ʼ������д���ļ�
//		����writeX(x)����		
//		�ھŲ����������յ���̬
//		����getEnd()����
//		��ʮ����ȷ�����ȷ���
//		����getchangeNumber()����
//		��ʮһ���������ȷ���д���ļ�
//		����writechangeNumber(changeNumber)����
//		�õ��ĸ���������
//		Matrixmultiplication(double[][], double[][])�����������˷�
//		getDistance(double, double, double, double)���ݾ�γ�ȼ�������֮�����
	}
	/**���㳵�����ȷ���
	 * */
	static double[][] getchangeNumber()
	{
		double[][] xend = readEnd();//��������̬��ȡ����
		int[][] end = new int[SIZE][1];//����̬�������е�ÿһ��ȡ��
		for(int i = 0;i < SIZE;i++) 
			end[i][0] = (int)(xend[i][0]);
		int[][] x = readX();//�ѳ�ʼ��������ȡ����
		double[][] data = readData();//��ͣ���������ȡ����
		double[] change = new double[SIZE];//��ʼ����̬�Ĳ�ֵ
		double[][] changeNumber = new double[SIZE][SIZE];
		//��¼�������ȵľ���i��j���ʾ��i����j����ȵĳ�����
		for(int i = 0;i < SIZE;i++) 
			for(int j = 0;j < SIZE;j++) 
				changeNumber[i][j] = 0;//����ʼֵΪ0
		for(int i = 0;i < SIZE;i++) 
			change[i] = x[i][0] - end[i][0];//�����ʼ����̬�Ĳ�ֵ
		for(int i = 0;i < SIZE;i++) 
			if(change[i] > 0) //�Ѳ�ֵ����0��ͣ��λ�ĳ�����
				while(change[i] > 0.1) 
				{
					//ֹͣ���ȵ���ֹ����
					int index = 0;
					for(int j = 0;j < SIZE;j++) 
						if(change[j] < 0) 
						{
							//�ҵ���һ����ֵС��0�ĵ�
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
								//�ҵ���������Ĳ�ֵС��0�ĵ�
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
					//������ȳ�����
				}
			
		
		return changeNumber;
	}
	/**��ȡ��ʼ������
	 * */
	static int[][] getX()
	{
		int n = SIZE;
		int[][] x = new int[n][1];//����������
		for(int i = 0;i < n;i++) 
			x[i][0] = 0;//���ó�ʼֵΪ0
		ArrayList<String> nowCar = 
				new ArrayList<String>();
		//�Ѿ�������ĳ����б�
		for(int i = 0;i < carList.size();i++) 
			for(int j = 0;j < carList.get(i).size();j++)
				if(nowCar.indexOf(carList.get(i).get(j))==-1) 
				{
					//��¼ÿ������һ�γ��ֵ�ͣ���㣬Ȼ�����Ӧ������ֵ��һ
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
	/**��ʼ��ͣ����֮������ת�������ľ���
	 * */
	static int[][] Indirection()
	{
		int n = SIZE;
		int[][] direction = new int[n][n];//��������
		for(int i = 0;i < n;i++) 
			for(int j = 0;j < n;j++) 
				direction[i][j] = 0;
		//��һ��ͣ���㵽��һ��ͣ����ת�Ƶĳ�������
		//����ÿһ������������һ�γ��ֵ�ͣ���㣬Ȼ�����Ӧ�����һ
		//��δ�������50����
		for(int i = 0;i < carList.size();i++) 
			for(int j = 0; j < carList.get(i).size();j++) 	
				for(int k = i+1;k < carList.size();k++) 
					for(int m = j+1;m < carList.get(k).size();m++) 					
						if(carList.get(i).get(j).equals(carList.get(k).get(m))) 
						{
							ArrayList<Double> temp = new ArrayList<Double>();
							temp.add(latitude.get(i));
							temp.add(longitude.get(i));
							//��50��֮�ڵĵ�
							int x = getIndex(temp);
							temp = new ArrayList<Double>();
							temp.add(latitude.get(k));
							temp.add(longitude.get(k));
							//��50��֮�ڵĵ�
							int y = getIndex(temp);
							direction[y][x]++;
						}
		return direction;
	}
	/**����������̬������
	 * */
	static double[][] getEnd(double[][] p,int[][] x)
	{
		double[][] dx = new double[x.length][1];
		for(int i = 0;i < dx.length;i++) 
			dx[i][0] = x[i][0];
		double[][] b = Matrixmultiplication(p,dx);
		for(int i = 0;i < 100;i++) 
			b = Matrixmultiplication(p, b);//���о���˷�
		for(int i = 0;i < b.length;i++) 
			b[i][0] = b[i][0] * (313.0/215.0);
		return b;
	}
	/**����ת�ƾ���
	 * */
	static double[][] getP(int[][] direction)
	{
		int n = direction.length;
		double[] sum = new double[n];
		//����ÿһ�еĺ͵�����
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
					//ÿһ����ڳ���ת�������Ը��еĺ�		
		return p;
	}
	/**����˷�
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
	/**���ݾ�γ�ȼ������
	 * */
	static double getDistance(double longitude1, 
			double latitude1, double longitude2, double latitude2) {
        // γ��
        double lat1 = Math.toRadians(latitude1);
        double lat2 = Math.toRadians(latitude2);
        // ����
        double lng1 = Math.toRadians(longitude1);
        double lng2 = Math.toRadians(longitude2);
        // γ��֮��
        double a = lat1 - lat2;
        // ����֮��
        double b = lng1 - lng2;
        // �����������Ĺ�ʽ
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(b / 2), 2)));
        // �����˵���뾶, ���ص�λ: ��
        s =  s * 6378.137 * 1000;
        return s;
    }
	/**�ѵ�������д���ļ���
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
	/**д������ת������
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
	/**��ȡ�ļ��ĺ���
	 * */
	static void readInf() 
	{
		File f = new File("C:\\Users\\ws_wa\\Documents\\Tencent Files\\2721162853\\FileRecv\\all1\\����������λ����.csv");
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
				//���whileѭ����ȡ����
				String[] inf = line.split(",");//�ָ��ַ���
				timestamp.add(inf[0]);//ʱ��
				latitude.add(Double.valueOf(inf[1]));//����
				longitude.add(Double.valueOf(inf[2]));//γ��
				ArrayList<Double> temp = new ArrayList<Double>();//ͣ�������ʱһά����
				temp.add(Double.valueOf(inf[1]));
				temp.add(Double.valueOf(inf[2]));
				//�����ǿգ���Ѱ�����޾���С��ָ������ĵ�
				//���У�������
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
					//���ޣ����룬���ַ���Ϊ�գ���һ���������ô��������
					loction.add(temp);//��û�и�ͣ���㣬�ͼ���
				//���´���ͳ��ÿ��ͣ����ĳ����б�
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
	/**��ȡ��ʼ������
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
	/**��ȡ������̬
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
	/**д��ͣ��������
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
	/**���ݾ�γ�Ȼ�ȡ�õ���ͣ�����б�����±�
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
	/**��ȡͣ��������
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
	/**��ȡ����ת�ƾ���
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
	/**д������Ʒ�������ת�ƾ���
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
	/**д����̬������
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
	/**д���ʼ������
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
