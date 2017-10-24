package subClass;

import java.util.ArrayList;

public class EvaluatePositionClass {

	public ArrayList<String> evaluatePosition(ArrayList<String> kouhoPositionList) {

		ArrayList<String> evaluatePositionList = new ArrayList<>();

		int[][] _VALUES = new int[][] {


		/**
		 * 評価テーブル。
		 */
//		private static int[][] _VALUES = new int[][]{

				{
						9, 1, 1, 1, 1, 1, 1, 9
				},
				{
						1, 0, 1, 1, 1, 1, 0, 1
				},
				{
						1, 1, 1, 1, 1, 1, 1, 1
				},
				{
						1, 1, 1, 4, 4, 1, 1, 1
				},
				{
						1, 1, 1, 4, 4, 1, 1, 1
				},
				{
						1, 1, 1, 1, 1, 1, 1, 1
				},
				{
						1, 0, 1, 1, 1, 1, 0, 1
				},
				{
						9, 1, 1, 1, 1, 1, 1, 9
				}
		};

		for (String zahyo : kouhoPositionList) {

			int first = 0;
			int second = 0;
			int evaluateNumber = 0;
			ArrayList<String> evaluatePosition = new ArrayList<>();

			first = Integer.parseInt(zahyo.substring(0, 0));
			second = Integer.parseInt(zahyo.substring(1, 1));

			evaluateNumber = _VALUES[first][second];

			evaluatePosition.add(String.valueOf(evaluateNumber));

		}

		return evaluatePositionList;

	}

}
