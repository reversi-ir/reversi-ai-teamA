package subClass;

import jp.takedarts.reversi.Board;
import jp.takedarts.reversi.Piece;
import jp.takedarts.reversi.Position;
import jp.takedarts.reversi.Processor;

/**
 * Reversi人工知能のサンプルプログラム。
 *
 * @author Atushi TAKEDA
 */
public class Main
		extends Processor {
	/**
	 * 手番が来たときに、次の手を決定するメソッド。<br>
	 *
	 * @param board 盤面の状態
	 * @param piece 自分が打つ駒
	 * @param thinkingTime 思考時間
	 * @return 次の手を置く場所
	 */
	@Override
	public Position nextPosition(Board board, Piece piece, long thinkingTime) {

		int kyokumenFlag = 0;
		int cornerFlag = 0;
		int value = 0;
		int x = -1;
		int y = -1;

		MinMax minmax = new MinMax();

		kyokumenFlag = minmax.kyokumen(board, piece);

		int[][] _VALUES = new int[][] {

				/**
				 * 評価テーブル。
				 */

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

		// 4すみに置けるか判断
		value = 9;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (board.isEnablePosition(i, j, piece) && _VALUES[i][j] == value) {

					x = i;
					y = j;
					cornerFlag = 1;

					//ループを抜ける
					break;
				}
			}

			//break;

		}

		KaihoudoRiron kaihoudoRiron = new KaihoudoRiron();
		Position position = null;

		//4すみに置けなかったとき場合わけ
		if (cornerFlag != 1) {

			switch (kyokumenFlag) {

			//序盤
			case 0:

				position = minmax.minmax(board, piece, thinkingTime);
				break;

			//中盤
			case 1:

				position = kaihoudoRiron.KaihoudoLogic(board, piece);

				break;

			//終盤
			case 2:

				position = minmax.minmax(board, piece, thinkingTime);
				break;
			}

			x = position.getX();
			y = position.getY();

		}

		// 置く場所をログに出力
		log(String.format("next -> (%d, %d)", x, y));

		// 置く場所をPositionオブジェクトに変換して返す
		return new Position(x, y);
	}

	/**
	 * この人工知能の名前を返す。
	 *
	 * @return 人工知能の名前
	 */
	@Override
	public String getName() {
		return "サンプル";
	}

}
