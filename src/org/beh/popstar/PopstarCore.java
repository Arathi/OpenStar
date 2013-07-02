package org.beh.popstar;

import java.util.Random;

public class PopstarCore {
	public static final int Width=10;
	public static final int Height=10;
	public static final int Star_Type_Max=5;
	public static final int Pop_Min_Limit=2;
	
	protected int[][] stars;
	protected boolean[][] popedFlags;
	protected int combo;
	protected int score;
	
	//Getters
	public int getStar(int x, int y){
		return stars[x][y];
	}
	private boolean isPoped(int x, int y){
		return popedFlags[x][y];
	}
	public int getScore(){
		return score;
	}
	
	
	/**
	 * ������
	 * ����init()
	 */
	public PopstarCore(){
		init();
	}
	
	/**
	 * ��ʼ��
	 */
	public void init(){
		stars=new int[Width][Height];
		popedFlags=new boolean[Width][Height];
		combo=0;
		score=0;
	}
	
	/**
	 * ���stars(���)
	 */
	public void fillStars(){
		Random random = new Random();
		int x, y;
		for (y = 0; y < Height; y++) {
			for (x = 0; x < Width; x++) {
				int type = random.nextInt(Star_Type_Max) + 1;
				stars[x][y] = type;
			}
		}
	}
	
	/**
	 * ���stars(ָ��)
	 * @param starList ָ����stars����
	 * @param isReversed ����y�����Ƿ����µߵ�
	 */
	public void fillStars(int[] starList, boolean isReversed){
		int x, y, index, type;
		for (y = 0; y < Height; y++) {
			for (x = 0; x < Width; x++) {
				if (isReversed)
					index = x + (Height-y-1)*Width;
				else
					index = x + y * Width;
				type = starList[index];
				stars[x][y] = type;
			}
		}
	}
	
	/**
	 * ���stars(�ߵ�ָ��)
	 * @param starList ָ����stars����
	 */
	public void fillStars(int[] starList){
		fillStars(starList, true);
	}
	
	/**
	 * ����popedFlags
	 */
	public void resetPopedFlags(){
		if (popedFlags==null) return;
		int x, y;
		for (y = 0; y < Height; y++) {
			for (x = 0; x < Width; x++) {
				popedFlags[x][y] = false;
			}
		}
	}
	
	public void begin() {
		init();
//		fillStars(new int[]{
//				 1,5,2,2,3,1,4,4,4,1,
//				 2,5,4,1,1,1,3,2,3,4,
//				 2,5,4,4,2,1,3,4,1,2,
//				 1,1,3,4,3,1,1,1,1,2,
//				 3,1,5,3,4,1,1,5,5,3,
//				 4,5,2,5,3,1,2,5,1,3,
//				 3,1,1,5,1,1,5,2,2,4,
//				 2,4,1,5,3,1,2,3,3,4,
//				 1,2,3,1,1,1,4,2,4,4,
//				 4,1,1,1,5,1,5,3,3,4});
		fillStars();
		resetPopedFlags();
		System.out.println(toString());
	}
	
	/**
	 * ��⵱ǰ�����Ƿ�Ϸ�
	 * @param x Ҫ���ĵ��x����
	 * @param y Ҫ���ĵ��y����
	 * @return �����Ƿ�����Ч��������
	 */
	public boolean check(int x, int y){
		if (x<0 || x>=Width || y<0 || y>=Height)
			return false;
		return true;
	}
	
	/**
	 * ������һ�׶�
	 * @param x Ҫpop�ĵ��x����
	 * @param y Ҫpop�ĵ��y����
	 * @return �õ㼰����Χ��������ͬ��ɫ�ķ�������
	 */
	public int pop(int x, int y){
		int type, counter;
		if (!check(x,y)) return 0;
		type=stars[x][y];
		counter=pop(x, y, type);
		if (counter==1){
			counter=0;
			popedFlags[x][y]=false;
			//resetPopedFlags();
		}
		return counter;
	}
	
	/**
	 * �����ڶ��׶�
	 * @param x ��ǰ���ڼ��ĵ��x����
	 * @param y ��ǰ���ڼ��ĵ��y����
	 * @param type ָ����ɫ����
	 * @return �õ㼰����Χ��������ͬ��ɫ�ķ�������
	 */
	public int pop(int x, int y, int type){
		if (!check(x,y)) return combo; //Խ��
		if (popedFlags[x][y]) return combo; //�ÿ��Ѿ�����
		if (stars[x][y]!=type) return combo; //��⵽��ͬ��ɫ�Ŀ� 
		combo++;
		popedFlags[x][y]=true;
		//��������չ
		pop(x,y+1,type); //��
		pop(x,y-1,type); //��
		pop(x-1,y,type); //��
		pop(x+1,y,type); //��
		return combo;
	}
	
	/**
	 * ����Ѿ���pop�Ŀ�
	 */
	public void clearPopedBlocks(){
		int x, y;
		for (y = 0; y < Height; y++) {
			for (x = 0; x < Width; x++) {
				if (popedFlags[x][y])
					stars[x][y]=0;
			}
		}
	}
	
	/**
	 * ��������
	 * ���һ��������·�û�з��飬��÷��������ƶ�
	 */
	public void blocksFall(){
		int x;
		int spaceStart, spaceEnd, pointer, delta;
		for (x=0; x<Width; x++){
			for (spaceStart = 0; spaceStart < Height; spaceStart++) {
				// ������һ��0
				if (stars[x][spaceStart] != 0)
					continue;
				// ������һ����0
				for (spaceEnd = spaceStart + 1; spaceEnd < Height; spaceEnd++) {
					if (stars[x][spaceEnd] != 0)
						break;
				}
				if (spaceEnd>=Height) break;
				delta = spaceEnd - spaceStart;
				//System.out.println("X="+x+",START="+spaceStart+",END="+spaceEnd+",DELTA="+delta);
				for (pointer = spaceStart; pointer < Height; pointer++) {
					if (pointer + delta < Height){
						if (stars[x][pointer+delta]==0) break;
						stars[x][pointer] = stars[x][pointer+delta];
						stars[x][pointer+delta] = 0;
					}
					else {
						stars[x][pointer] = 0;
					}
				}
			}
		}
	}
	
	/**
	 * ��������
	 * ���ĳһ�е����һ������û�з��飬��������������ƶ�
	 */
	public void blocksMoveLeft(){
		int x, y, start, end, delta, pointer;
		int[] colNotZeroAmount=new int[Width];
		//ͳ��ÿ�з�0����
		for (x=0; x<Width; x++){
			int amount=0;
			for (y=0; y<Height; y++){
				if (stars[x][y]!=0) amount++;
			}
			colNotZeroAmount[x]=amount;
			//System.out.print(colNotZeroAmount[x]+" ");
		}
		//System.out.println();
		//�ٴ�ɨ��
		for (start=0; start<Width; start++){
			//���ҵ�һ��0ֵ
			if (colNotZeroAmount[start]!=0) continue;
			//����֮���һ����0ֵ
			for (end=start+1; end<Width; end++){
				if (colNotZeroAmount[end]!=0) break;
			}
			if (end>=Width) continue;
			delta=end-start;
			//System.out.println("START="+start+",END="+end);
			//��ʼ����
			for (pointer=start; pointer<Width; pointer++){
				if (pointer + delta < Width){
					if (colNotZeroAmount[pointer+delta]==0) break;
					for (y=0; y<Height; y++){
						stars[pointer][y]=stars[pointer+delta][y];
						stars[pointer+delta][y]=0;
					}
					colNotZeroAmount[pointer]=colNotZeroAmount[pointer+delta];
				}
				else {
					colNotZeroAmount[pointer]=0;
				}
			}
		}
	}
	
	public int touch(int x, int y){
		//y=Height-y-1;
		System.out.println("������������: "+pop(x,y));
		//
		clearPopedBlocks();
		resetPopedFlags();
		blocksFall();
		blocksMoveLeft();
		System.out.println(toString());
		return 0;
	}
	
	public static void main(String[] args){
		PopstarCore pc=new PopstarCore();
		pc.begin();
		System.out.println(pc.toKey());
		//pc.touch(4, 9);
		pc.scan();
	}
	
	@Override
	public String toString(){
		String starsMatrix="";
		int x, y;
		starsMatrix += "   0 1 2 3 4 5 6 7 8 9\n";
		for (y = Height-1; y >= 0; y--) {
			starsMatrix += y;
			if (y==Height-1) starsMatrix+=" {"; 
			else starsMatrix+="  ";
			for (x = 0; x < Width; x++) {
				starsMatrix += stars[x][y];
				if (y==0 && x==Width-1) starsMatrix += "}";
				else starsMatrix += ",";
			}
			if (y!=0) starsMatrix += "\n";
		}
		return starsMatrix;
	}
	
	/**
	 * ��ǰ����ת��Ϊ��ϣ���ļ�
	 */
	public String toKey(){
		String key="";
		int x,y;
		for (y = 0; y < Height; y++) {
			for (x = 0; x < Width; x++) {
				key+=stars[x][y];
			}
		}
		return key;
	}
	
	public void scan(){
		int x,y,counter=0;
		for (x=0; x<Width; x++){
			for (y=0; y<Height; y++){
				combo=0;
				if (pop(x, y)>1) {
					counter++;
					System.out.println("CASE "+counter+": ("+x+","+y+")"+" Combo "+combo);					
				}
			}
		}
	}

}