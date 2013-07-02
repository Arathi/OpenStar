package org.beh.popstar.disabled;

import java.util.Random;

/* 计分规则
 * 消灭得分=块*块*5
 * 结束得分=2000-块*块*20
 * */

public class Popstar {
	public static final int Width = 10;
	public static final int Height = 10;
	public static final int MaxTypes = 5;
	public static final int MinCombo = 2;

	private int[][] stars;
	private boolean[][] starPoped;

	public Popstar() {
		stars = new int[Width][Height];
		starPoped = new boolean[Width][Height];
	}

	public void fillStars() {
		Random random = new Random();
		int x, y;
		for (y = 0; y < Height; y++) {
			for (x = 0; x < Width; x++) {
				int type = random.nextInt(MaxTypes) + 1;
				stars[x][y] = type;
			}
		}
	}

	public void fillStars(int[] starlist) {
		int x, y;
		for (y = 0; y < Height; y++) {
			for (x = 0; x < Width; x++) {
				int index = x + y * Width;
				int type = starlist[index];
				stars[x][y] = type;
			}
		}
	}

	public void resetPoped() {
		int tx, ty;
		for (ty = 0; ty < Height; ty++) {
			for (tx = 0; tx < Width; tx++) {
				starPoped[tx][ty] = false;
			}
		}
		//type = stars[x][y];
	}
	
	public boolean checkCoordinate(int x, int y){
		if (x < 0 || x >= Width || y < 0 || y >= Height)
			return false;
		return true;
	}

	public int pop(int x, int y) {
		checkCoordinate(x, y);
		int type = stars[x][y];
		return pop(x, y, type, 0);
	}

	protected int pop(int x, int y, int type, int counter) {
		if (!checkCoordinate(x, y))
			return counter;
		if (starPoped[x][y] == true)
			return counter;
		// 首次点击，清空starPoped，设置本次要搜索的type
		
		// 如果当前单元格star的颜色与type相同
		if (stars[x][y] == type) {
			// stars[x][y]=0;
			starPoped[x][y] = true;
			counter += 1;
			// 向4个方向扩展
			counter = pop(x - 1, y, type, counter);
			counter = pop(x, y - 1, type, counter);
			counter = pop(x + 1, y, type, counter);
			counter = pop(x, y + 1, type, counter);
		}
		return counter;
	}

	public void clearSpace() {
		int x, y;
		for (y = 0; y < Height; y++) {
			for (x = 0; x < Width; x++) {
				if (starPoped[x][y] == true) {
					// starPoped[x][y]=false;
					stars[x][y] = 0;
				}
			}
		}
		fall();
	}

	private void fall() {
		int x, y;
		int[] spaceAmounts = new int[Width];
		for (x = 0; x < Width; x++) {
			int spaceAmount = 0, topZero;
			int spaceStart, spaceEnd, pointer, delta;
			boolean changed;

			for (spaceStart = Height - 1; spaceStart >= 0; spaceStart--) {
				// 查找下一个0
				if (stars[x][spaceStart] != 0)
					continue;
				// 查找下一个非0
				for (spaceEnd = spaceStart - 1; spaceEnd >= 0; spaceEnd--) {
					if (stars[x][spaceEnd] != 0)
						break;
				}
				delta = spaceStart - spaceEnd;
				// System.out.println("X="+x+", Start At "+spaceStart+", End At "+spaceEnd);
				for (pointer = spaceEnd; pointer >= 0; pointer--) {
					// System.out.println("X="+x+", pointer: "+pointer+", pointer-delta: "+(pointer-delta)
					// );
					stars[x][pointer + delta] = stars[x][pointer];
					stars[x][pointer] = 0;
				}
			}

			for (y = Height - 1; y > 0; y--) {
				if (stars[x][y] == 0)
					spaceAmount++;
			}
			spaceAmounts[x] = spaceAmount;
		}
	}

	public void printStars() {
		int x, y;
		System.out.println("  0 1 2 3 4 5 6 7 8 9 ");
		for (y = 0; y < Height; y++) {
			System.out.print(y + " ");
			for (x = 0; x < Width; x++) {
				System.out.print(stars[x][y] + ",");
			}
			System.out.println();
		}
	}

	public static void main(String[] args) {
		Popstar popstar = new Popstar();
		popstar.fillStars(new int[] {
				1, 2, 1, 4, 4, 3, 5, 1, 2, 1, 
				4, 5, 1, 1,	4, 4, 3, 3, 4, 1, 
				5, 5, 3, 2, 5, 2, 5, 4, 3, 5, 
				1, 2, 1, 1, 3, 4, 4, 2, 1, 4, 
				4, 5, 4, 2, 2, 1, 5, 4, 3, 4, 
				2, 4, 3, 2, 4, 2, 1, 3, 3, 1, 
				2, 2, 2, 2, 1, 1, 1, 4, 2, 4, 
				5, 5, 3, 3, 1, 1, 5, 2, 2, 4, 
				1, 5, 1, 1, 3, 5, 2, 2, 1, 2, 
				5, 2, 3, 1, 4, 3, 2, 5, 2, 1, });
		popstar.printStars();
		int popedAmount = popstar.pop(4, 1);
		// System.out.println( );
		if (popedAmount > 3)
			popstar.clearSpace();
		popstar.printStars();
	}

}
