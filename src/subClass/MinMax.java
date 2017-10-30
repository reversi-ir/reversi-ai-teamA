package subClass;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import jp.takedarts.reversi.Board;
import jp.takedarts.reversi.Piece;
import jp.takedarts.reversi.Position;

public class MinMax {

	/**
	 * 手番が来たときに、次の手を決定するメソッド。
	 *
	 * @param board
	 *            盤面の状態
	 * @param piece
	 *            自分が打つ駒
	 * @param thinkingTime
	 *            思考時間
	 * @return 次の手を置く場所
	 */

	public Position minmax(Board board, Piece piece, long thinkingTime) {
		// 次に置ける場所の中で、もっとも評価の高い場所を探す
		int max = Integer.MIN_VALUE;
		int x = -1;
		int y = -1;

		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				// 置けるかどうかを確認し、置けないのなら何もしない
				if (!board.isEnablePosition(i, j, piece)) {
					continue;
				}

				// 同じ盤面を表すオブジェクトを作成し、自分の駒を置く
				Board next_board = new Board(board.getBoard());

				next_board.putPiece(i, j, piece);

				// 駒を置いた後の盤面に、さらに相手が駒を置いた場合の最小評価値を計算する
				int value = _getMinValue(next_board, piece);

				// 求めた盤面の最小評価値が最大となる駒の置き場所を保存する
				if (value > max) {
					max = value;
					x = i;
					y = j;
				}
			}
		}

		// 置く場所をPositionオブジェクトに変換して返す
		return new Position(x, y);
	}

	/**
	 * 盤面の評価値を計算する。
	 *
	 * @param board
	 *            盤面の状態
	 * @param piece
	 *            自分の駒
	 * @return 評価値
	 */
	private int _getValue(Board board, Piece piece) {
		int value = 0;
		int[][] _VALUES = new int[8][8];

		// 評価テーブルをセット
		_VALUES = setValue();

		// 評価値算出
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (board.getPiece(i, j) == piece) {
					value += _VALUES[i][j];
				} else if (board.getPiece(i, j) == Piece.opposite(piece)) {
					value -= _VALUES[i][j];
				}
			}
		}

		return value;
	}

	// 相手の駒を置いたときの最小評価値を返す。

	// @param board 盤面の状態
	// @param piece 自分の駒
	// @return 最小評価値
	//
	private int _getMinValue(Board board, Piece piece) {
		// 相手の駒を置ける場所に駒を置いてみて、その中の評価値の最小値を求める
		Piece enemy = Piece.opposite(piece);
		int min = Integer.MAX_VALUE;

		// 評価値算出

		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				// 駒を置けない場合は何もしない
				if (!board.isEnablePosition(i, j, enemy)) {
					continue;
				}

				// 盤面の状態をコピーして、相手の駒を置く
				Board next_board = new Board(board.getBoard());

				next_board.putPiece(i, j, enemy);

				// 駒を置いた後の盤面を評価し、最小の評価値を判定する
				int value = _getValue(next_board, piece);

				if (min > value) {
					min = value;
				}
			}
		}

		return min;
	}

	/**
	 * CSVファイルを読み込み評価テーブルをセットする。
	 *
	 * @return 評価テーブル
	 */
	public int[][] setValue() {

		int[][] _VALUES = new int[8][8];
		String dataStr;
		// 評価テーブルをセット
		try {

			File file = new File(System.getProperty("user.dir") + "/" + "random" + "/" +"valueTable.csv");
			BufferedReader br = new BufferedReader(new FileReader(file));

			String str = br.readLine();
			while (str != null) {

				for (int a = 0; a < 8; a++) {

					String[] data = str.split(",", 0);

					for (int b = 0; b < 8; b++) {

						dataStr = data[b];
						_VALUES[a][b] = Integer.parseInt(dataStr);

					}

				}
				str = br.readLine();

			}

			br.close();

		} catch (FileNotFoundException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}

		return _VALUES;
	}

	/////////////////////////////////// 分岐のプログラム///////////////////////////////
	public int kyokumen(Board board, Piece piece) {

		int kyokumenflag = 0;
		int countreult = 0;

		countreult = board.countPiece(piece) + board.countPiece(Piece.opposite(piece));

		// 駒数チェックのためのsysout
		System.out.println("現在駒数:" + countreult);


		if (countreult < 20) {
			kyokumenflag = 0;

		} else if ((countreult >= 21) && (countreult <= 40)) {

			kyokumenflag = 1;

		} else if (countreult >= 41) {

			kyokumenflag = 2;
		}

		return kyokumenflag;

	}

	/////////// ランダム関数を発生させるメソッドはこれです。////////////////

	// ランダム関数を発生させるメソッド
	// 11～60
	public int randomvalue() {

		// randomメソッドは実数を生成する変数を宣言
		int resultvalue;
		// randomメソッドで生成した実数を変数resultvalueに代入
		resultvalue = (int) (Math.floor(Math.random() * 50) + 11);

		return resultvalue;
	}

	// 1～10
	public int randomvalue2() {

		// randomメソッドは実数を生成する変数を宣言
		int resultvalue;
		// randomメソッドで生成した実数を変数resultvalueに代入
		resultvalue = (int) (Math.floor(Math.random() * 10) + 1);

		return resultvalue;
	}

	// 61～80
		public int randomvalue3() {

			// randomメソッドは実数を生成する変数を宣言
			int resultvalue;
			// randomメソッドで生成した実数を変数resultvalueに代入
			resultvalue = (int) (Math.floor(Math.random() * 20) + 61);

			return resultvalue;
		}


}
