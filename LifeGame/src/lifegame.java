import java.sql.Time;
import java.util.Scanner;

public class lifegame {

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);

		// 可按需求更改界面长宽
		int frameWidth = 25;
		int frameHight = 25;
		int startNum;
		int[] startX;
		int[] startY;
		int gameMod;
		int cycle = 0;

		// 选择随机生成初始位置或者手动选择
		// System.out.println("随机选择初始位置按 1 \r手动选择初始位置按 2 \r退出游戏按 3");
		System.out.println("随机选择初始位置按 1");
		System.out.println("手动选择初始位置按 2");
		System.out.println("退出游戏按 3");
		gameMod = input.nextInt();

		if (gameMod == 1) {
			// 选择生成几个初始细胞
			System.out.println("请输入生成初始细胞的数量");
			startNum = input.nextInt();
			startX = new int[startNum];
			startY = new int[startNum];

			for (int i = 0; i < startNum; i++) {
				startX[i] = (int) (Math.random() * (frameWidth - 1) + 1);
				startY[i] = (int) (Math.random() * (frameHight - 1) + 1);
			}
		} else if (gameMod == 2) {
			System.out.println("请输入生成初始细胞的数量");
			startNum = input.nextInt();
			startX = new int[startNum];
			startY = new int[startNum];

			// 参考坐标
			printGame(frameWidth, frameHight, startNum, drawAxes(frameWidth, frameHight), cycle);

			for (int i = 1; i <= startNum; i++) {
				System.out.println("请输入第" + i + "个细胞的横坐标(0到" + frameWidth + "之间)");
				startX[i - 1] = input.nextInt();
				System.out.println("请输入第" + i + "个细胞的纵坐标(0到" + frameHight + "之间)");
				startY[i - 1] = input.nextInt();
			}
		} else {
			startX = new int[0];
			startY = new int[0];
			startNum = 0;
			System.out.println("游戏关闭");
			System.exit(0);
		}

		String[][] coordinate = buildCell(frameWidth, frameHight, startNum, startX, startY);
		printGame(frameWidth, frameHight, startNum, coordinate, cycle);

		// 定时刷新
		while (true) {
			long lastTime = System.currentTimeMillis();
			while (true) {
				long nowTime = System.currentTimeMillis();
				if (nowTime - lastTime >= 600) {
					cycle++;
					cellLogic(frameWidth, frameHight, coordinate);
					printGame(frameWidth, frameHight, startNum, coordinate, cycle);
					break;
				}
			}
		}

	}

	/*************************** 方法 ***************************/

	// 绘制游戏坐标系
	public static String[][] drawScence(int frameWidth, int frameHight) {
		String[][] coordinate = new String[frameWidth + 1][frameHight + 1];
		int x = 0;
		int y = 0;

		// 生成游戏界面
		for (y = 0; y <= frameHight; y++) {
			for (x = 0; x <= frameWidth; x++) {
				if (x == 0 && y == 0) {
					coordinate[x][y] = " ┌─";
				} else if (x == frameWidth && y == 0) {
					coordinate[x][y] = "─┐";
				} else if (x == 0 && y == frameHight) {
					coordinate[x][y] = " └─";
				} else if (x == frameWidth && y == frameHight) {
					coordinate[x][y] = "─┘";
				} else if (x > 0 && x < frameWidth && (y == 0 || y == frameHight)) {
					coordinate[x][y] = "──";
				} else if ((x == 0 || x == frameWidth) && y > 0 && y < frameHight) {
					coordinate[x][y] = " │ ";
				} else {
					coordinate[x][y] = "  ";
				}
			}
		}
		return coordinate;
	}

	// 生成参考坐标系
	public static String[][] drawAxes(int frameWidth, int frameHight) {
		String[][] coordinate = new String[frameWidth + 1][frameHight + 1];
		int x = 0;
		int y = 0;

		// 生成游戏界面
		for (y = 0; y <= frameHight; y++) {
			for (x = 0; x <= frameWidth; x++) {
				if (x == 0 && y == 0) {
					coordinate[x][y] = "┌\t";
				} else if (x == frameWidth && y == 0) {
					coordinate[x][y] = "┐\t";
				} else if (x == 0 && y == frameHight) {
					coordinate[x][y] = "└\t";
				} else if (x == frameWidth && y == frameHight) {
					coordinate[x][y] = "┘\t";
				} else if (x > 0 && x < frameWidth && (y == 0 || y == frameHight)) {
					coordinate[x][y] = x + "\t";
				} else if ((x == 0 || x == frameWidth) && y > 0 && y < frameHight) {
					coordinate[x][y] = y + "\t";
				} else {
					coordinate[x][y] = "+\t";
				}
			}
		}
		return coordinate;
	}

	// 细胞生成
	public static String[][] buildCell(int frameWidth, int frameHight, int startNum, int[] startX, int[] startY) {
		String[][] coordinate = drawScence(frameWidth, frameHight);
		for (int i = 1; i <= startNum; i++) {
			coordinate[startX[i - 1]][startY[i - 1]] = "o ";
		}
		return coordinate;
	}

	// 细胞逻辑
	public static void cellLogic(int frameWidth, int frameHight, String[][] coordinate) {
		String[][] saveChange = new String[frameWidth][frameHight];

		for (int y = 1; y < frameHight; y++) {
			for (int x = 1; x < frameWidth; x++) {

				int count;
				if (coordinate[x][y] == "o ") {
					count = -1;
				} else {
					count = 0;
				}

				for (int sideY = y - 1; sideY <= y + 1; sideY++) {
					for (int sideX = x - 1; sideX <= x + 1; sideX++) {
						if (coordinate[sideX][sideY] == "o ") {
							count++;
						}
					}
				}

				if (count < 2) {
					saveChange[x][y] = "  ";
				} else if (count == 2) {

				} else if (count == 3) {
					saveChange[x][y] = "o ";
				} else {
					saveChange[x][y] = "  ";
				}

			}
		}
		for (int y = 1; y < frameHight; y++) {
			for (int x = 1; x < frameWidth; x++) {
				if (saveChange[x][y] != null) {
					coordinate[x][y] = saveChange[x][y];
				}
			}
		}

	}

	// 打印画面
	public static void printGame(int frameWidth, int frameHight, int startNum, String[][] coordinate, int cycle) {
		System.out.println(" 第 " + cycle + " 个周期");
		for (int y = 0; y <= frameHight; y++) {
			for (int x = 0; x <= frameWidth; x++) {
				System.out.print(coordinate[x][y]);
				if ((x + 1) % (frameWidth + 1) == 0) {
					System.out.println();
				}
			}
		}
	}

}
