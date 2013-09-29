package org.beh.openstar;

import java.util.Random;

public class PopstarEngine {
	public static final int Width = 10;
	public static final int Height = 10;
	public static final int BlockColors = 5;

	int[][] stars;
	boolean[][] selectedFlags; // 选取标记
	boolean[][] scanedFlags; // 扫描标记
	boolean[][] fallFlags; // 下落标记
	boolean[][] leftFlags; // 左移标记
	boolean selectLock; // 选择锁
	int counter; // 计数器

	/**
	 * 构造器（无参）
	 */
	public PopstarEngine() {
		init();
	}

	public void init() {
		stars = new int[Width][Height];
		selectedFlags = new boolean[Width][Height];
		scanedFlags = new boolean[Width][Height];
		fallFlags = new boolean[Width][Height];
		leftFlags = new boolean[Width][Height];
		counter=0;
	}

	/**
	 * 重置选择标记
	 */
	void resetSelectedFlags() {
		int x, y;
		for (y = 0; y < Height; y++) {
			for (x = 0; x < Width; x++) {
				selectedFlags[x][y] = false;
			}
		}
	}

	/**
	 * 重置扫描标记
	 */
	void resetScanedFlags() {
		int x, y;
		for (y = 0; y < Height; y++) {
			for (x = 0; x < Width; x++) {
				scanedFlags[x][y] = false;
			}
		}
	}

	/**
	 * 深度优先搜素连续方块
	 * 
	 * @param x
	 *            开始点x坐标
	 * @param y
	 *            开始点y坐标
	 * @param c
	 *            颜色代码
	 */
	public void dfs(int x, int y, int c) {
		if (x < 0 || x >= Width || y < 0 || y >= Height)
			return; // 越界情况
		if (selectedFlags[x][y] == true)
			return; // 已被选取
		if (stars[x][y] == 0)
			return;
		// 首次选取
		if (c == 0) {
			c = stars[x][y];
		}
		if (stars[x][y] != c)
			return;
		selectedFlags[x][y] = true;
		counter++;
		dfs(x, y - 1, c);
		dfs(x - 1, y, c);
		dfs(x, y + 1, c);
		dfs(x + 1, y, c);
	}

	/**
	 * 随机填充
	 */
	public void fillBlocks() {
		Random random = new Random();
		int x, y;
		for (y = 0; y < Height; y++) {
			for (x = 0; x < Width; x++) {
				stars[x][y] = random.nextInt(BlockColors) + 1;
			}
		}
	}

	/**
	 * 指定填充
	 * 
	 * @param key
	 *            填充内容
	 */
	public void fillBlocks(String key) {
		int x, y, index, color;
		for (y = 0; y < Height; y++) {
			for (x = 0; x < Width; x++) {
				index = x + y * Width;
				color = key.charAt(index) - '0';
				stars[x][y] = color;
			}
		}

	}

	/**
	 * 打印方块
	 */
	public void printBlocks() {
		int x, y;
		System.out.println("   0 1 2 3 4 5 6 7 8 9");
		System.out.println(" S---------------------*");
		for (y = 0; y < Height; y++) {
			System.out.print(y + "| ");
			for (x = 0; x < Width; x++) {
				System.out.print(stars[x][y] + " ");
			}
			System.out.println("|");
		}
		System.out.println(" *---------------------*");
		//System.out.println("======================");
	}

	/**
	 * 方块转化为键
	 * 
	 * @return
	 */
	public String toKey() {
		String key = "";
		int x, y;
		for (y = 0; y < Height; y++) {
			for (x = 0; x < Width; x++) {
				key += stars[x][y];
			}
		}
		return key;
	}

	/**
	 * 打印已选取标记(不建议使用)
	 */
	@Deprecated
	public void printSelected() {
		int x, y;
		for (y = 0; y < Height; y++) {
			System.out.print(y + "|");
			for (x = 0; x < Width; x++) {
				int selected = selectedFlags[x][y] ? 1 : 0;
				System.out.print(selected + " ");
			}
			System.out.println();
		}
	}

	/**
	 * 清除选取方块
	 */
	public void clearSelectedBlocks() {
		int x, y;
		for (y = 0; y < Height; y++) {
			for (x = 0; x < Width; x++) {
				if (selectedFlags[x][y])
					stars[x][y] = 0;
			}
		}
	}

	/**
	 * 设置移动方块标记
	 * 
	 * @return 是否发生了移动
	 */
	public boolean setMoveFlagForBlocks() {
		int x, y;
		boolean movedFlag=false;
		boolean leftMoveFlag = false; //表明本轮剩下的列都需要左移
		for (x = 0; x < Width; x++) {
			int row_counter = 0;
			boolean fallMoveFlag = false; //表明本列剩下的方块都要下移 
			for (y=Height-1; y>=0; y--){
				if (stars[x][y] == 0) {
					row_counter++;
					continue;
				}
				//if (y == Height - 1) continue;
				if (fallMoveFlag){
					fallFlags[x][y] = true;
				}
				else if (y+1<Height && stars[x][y+1] == 0) {
					fallFlags[x][y] = true;
					fallMoveFlag=true;
					movedFlag=true;
				}
				if (leftMoveFlag) {
					leftFlags[x][y] = true;
				}
			}
			if (row_counter == Height) {
				leftMoveFlag = true;
				if (x!=Width-1)
					movedFlag=true;
			} 
			//else {
				//leftMoveFlag = false;
			//}
		}
		return movedFlag;
	}
	
	public void moveBlocks(){
		int x, y;
		//清空移动标记
		for (x = 0; x < Width; x++) {
			for (y = Height-1; y >= 0; y--) {
				int nx=x,ny=y;
				int aspect=0;
				if (fallFlags[x][y])
					aspect |= 0x01;
				if (leftFlags[x][y])
					aspect |= 0x02;
//				if (fallFlags[nx][ny] && ny+1<Height){
//					stars[nx][ny+1]=stars[nx][ny];
//					stars[nx][ny]=0;
//					ny=ny+1;
//				}
//				if (leftFlags[nx][ny] && nx-1>=0 ){
//					stars[nx-1][ny]=stars[nx][ny];
//					stars[nx][ny]=0;
//					nx=nx-1;
//				}
				switch (aspect){
				case 1:
					stars[x][y+1]=stars[x][y];
					stars[x][y]=0;
					break;
				case 2:
					stars[x-1][y]=stars[x][y];
					stars[x][y]=0;
					break;
				case 3:
					stars[x-1][y+1]=stars[x][y];
					stars[x][y]=0;
					break;
				default:
				}
				fallFlags[x][y] = false;
				leftFlags[x][y] = false;
			}
		}
	}

	/**
	 * 打印移动方向标记<br/> 
	 * 0(00000000): 不移动<br/> 
	 * 1(00000001): 下落 <br/>
	 * 2(00000010): 左移 <br/>
	 * 3(00000011): 左下移动<br/>
	 */
	public void printMoveFlags() {
		int x, y;
		System.out.println("   0 1 2 3 4 5 6 7 8 9");
		System.out.println(" M--------------------");
		for (y = 0; y < Height; y++) {
			System.out.print(y + "| ");
			for (x = 0; x < Width; x++) {
				int aspect = 0;
				if (fallFlags[x][y])
					aspect |= 0x01;
				if (leftFlags[x][y])
					aspect |= 0x02;
				System.out.print(aspect + " ");
			}
			System.out.println();
		}
	}

	/**
	 * 选取单个方块
	 * 
	 * @param x
	 *            要选取的方块的x坐标
	 * @param y
	 *            要选取的方块的y坐标
	 */
	void selectBlock(int x, int y) {
		if (selectLock)
			selectedFlags[x][y] = true;
	}

	public static void main(String[] args) {
		PopstarEngine pse = new PopstarEngine();
		String key = "3531522522455225242251333322413531512331245435233252531522222544512522535212242321222424453544242145";
		pse.fillBlocks(key);
		pse.printBlocks();
		pse.dfs(9, 4, 0);
		pse.clearSelectedBlocks();
		pse.printBlocks();
		while (pse.setMoveFlagForBlocks()){
			pse.moveBlocks();
			pse.printBlocks();
		}
		//;
		
		//pse.printBlocks();
		
//		System.out.println("=====================");
//		pse.setMoveFlagForBlocks();
//		pse.printMoveFlags();
		// pse.printSelected();
		// System.out.println(pse.counter);
	}

	public int getBlock(int x, int y) {
		return stars[x][y];
	}
	public boolean getFallFlag(int x, int y){
		return fallFlags[x][y];
	}
	public boolean getLeftFlag(int x, int y){
		return leftFlags[x][y];
	}

}
