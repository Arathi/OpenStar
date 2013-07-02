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
	protected boolean runningFlag;
	
	//Getters
	public int getStar(int x, int y){
		return stars[x][y];
	}
	public boolean isPoped(int x, int y){
		return popedFlags[x][y];
	}
	public int getScore(){
		return score;
	}
	public boolean isGameEnd(){
		return !runningFlag;
	}
	
	/**
	 * 构造器
	 * 调用init()
	 */
	public PopstarCore(){
		init();
	}
	
	/**
	 * 初始化
	 */
	public void init(){
		stars=new int[Width][Height];
		popedFlags=new boolean[Width][Height];
		combo=0;
		score=0;
		runningFlag=false;
	}
	
	/**
	 * 填充stars(随机)
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
	 * 填充stars(指定)
	 * @param starList 指定的stars序列
	 * @param isReversed 决定y坐标是否上下颠倒
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
	 * 填充stars(颠倒指定)
	 * @param starList 指定的stars序列
	 */
	public void fillStars(int[] starList){
		fillStars(starList, true);
	}
	
	/**
	 * 重置popedFlags
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
		runningFlag=true;
	}
	
	/**
	 * 检测当前坐标是否合法
	 * @param x 要检测的点的x坐标
	 * @param y 要检测的点的y坐标
	 * @return 坐标是否在有效的区域内
	 */
	public boolean check(int x, int y){
		if (x<0 || x>=Width || y<0 || y>=Height)
			return false;
		return true;
	}
	
	/**
	 * 触碰第一阶段
	 * @param x 要pop的点的x坐标
	 * @param y 要pop的点的y坐标
	 * @return 该点及其周围连续的相同颜色的方块数量
	 */
	public int pop(int x, int y){
		int type, counter;
		if (!check(x,y)) return 0;
		type=stars[x][y];
		if (type==0) return 0;
		counter=pop(x, y, type);
		if (counter==1){
			counter=0;
			popedFlags[x][y]=false;
			//resetPopedFlags();
		}
		return counter;
	}
	
	/**
	 * 触碰第二阶段
	 * @param x 当前正在检测的点的x坐标
	 * @param y 当前正在检测的点的y坐标
	 * @param type 指定颜色类型
	 * @return 该点及其周围连续的相同颜色的方块数量
	 */
	public int pop(int x, int y, int type){
		if (!check(x,y)) return combo; //越界
		if (popedFlags[x][y]) return combo; //该块已经检测过
		if (stars[x][y]!=type) return combo; //检测到不同颜色的块 
		combo++;
		popedFlags[x][y]=true;
		//向四周扩展
		pop(x,y+1,type); //上
		pop(x,y-1,type); //下
		pop(x-1,y,type); //左
		pop(x+1,y,type); //右
		return combo;
	}
	
	/**
	 * 清除已经被pop的块
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
	 * 方块下落
	 * 如果一个方块的下方没有方块，则该方块向下移动
	 */
	public void blocksFall(){
		int x;
		int spaceStart, spaceEnd, pointer, delta;
		for (x=0; x<Width; x++){
			for (spaceStart = 0; spaceStart < Height; spaceStart++) {
				// 查找下一个0
				if (stars[x][spaceStart] != 0)
					continue;
				// 查找下一个非0
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
	 * 方块左移
	 * 如果某一列的左边一列整列没有方块，则该列整列往左移动
	 */
	public void blocksMoveLeft(){
		int x, y, start, end, delta, pointer;
		int[] colNotZeroAmount=new int[Width];
		//统计每列非0数量
		for (x=0; x<Width; x++){
			int amount=0;
			for (y=0; y<Height; y++){
				if (stars[x][y]!=0) amount++;
			}
			colNotZeroAmount[x]=amount;
			//System.out.print(colNotZeroAmount[x]+" ");
		}
		//System.out.println();
		//再次扫描
		for (start=0; start<Width; start++){
			//查找第一个0值
			if (colNotZeroAmount[start]!=0) continue;
			//查找之后第一个非0值
			for (end=start+1; end<Width; end++){
				if (colNotZeroAmount[end]!=0) break;
			}
			if (end>=Width) continue;
			delta=end-start;
			//System.out.println("START="+start+",END="+end);
			//开始左移
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
		int blocks=pop(x,y);
		System.out.println("连续方块数量: "+blocks);
		//
		if (blocks>1){
			clearPopedBlocks();
			resetPopedFlags();
			blocksFall();
			blocksMoveLeft();
		}
		combo=0;
		resetPopedFlags();
		//System.out.println(toString());
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
	 * 当前序列转化为哈希表的键
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
	
	public int scan(){
		int x,y,counter=0;
		for (x=0; x<Width; x++){
			for (y=0; y<Height; y++){
				combo=0;
				if (pop(x, y)>1) {
					counter++;
					//System.out.println("CASE "+counter+": ("+x+","+y+")"+" Combo "+combo);					
				}
			}
		}
		if (counter>0){
			System.out.println("当前情况有"+counter+"处可被点击");
		}
		else{
			runningFlag=false;
			for (x=0; x<Width; x++){
				for (y=0; y<Height; y++){
					if (stars[x][y]!=0) counter++;
				}
			}
			System.out.println("游戏结束，剩余方块数量："+counter);
		}
		resetPopedFlags();
		combo=0;
		return counter;
	}

}
