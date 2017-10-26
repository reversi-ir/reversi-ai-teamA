package subClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jp.takedarts.reversi.Board;
import jp.takedarts.reversi.Piece;
import jp.takedarts.reversi.Position;

public class KaihoudoRiron {

	public Position HattenKaihoudoLogic(Board board, Piece piece) {

		// 次に置ける場所を探す
		int kaihoudo = 100;
		int x = -1;
		int y = -1;

		// 評価値が最小となる座標を格納するリスト
		List<Position> positionList = new ArrayList<>();

		// 置ける場所の単純検索
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {

				if (board.isEnablePosition(i, j, piece)) {

					// 置けるかどうかを確認し、置けないのなら何もしない
					if (!board.isEnablePosition(i, j, piece)) {
						continue;
					}

					// ただし四隅は取らない
					if (i == 0 && j == 0 || i == 7 && j == 0 || i == 0 && j == 7 || i == 7 && j == 7) {

						continue;

					} else {

						// 同じ盤面を表すオブジェクトを生成し、自分の駒を置く
						Board nextBoard = new Board(board.getBoard());
						nextBoard.putPiece(i, j, piece);

						// 駒を置いた後の盤面で、暫定的な開放度を計算する
						int value = calculateValue(board, nextBoard, piece);

						// 相手の番を考え、一番小さい開放度と座標を取得する
						// 相手が置けない場合、相手の開放度を0とする

						Position oppositePosition = returnPlace(nextBoard, Piece.opposite(piece));
						int oppositeKaihoudo = returnMinKaihoudo(nextBoard, Piece.opposite(piece));

						// 相手の駒を置く

						Board afterNextBoard = new Board(nextBoard.getBoard());
						afterNextBoard.putPiece(oppositePosition.getX(), oppositePosition.getY(),
								Piece.opposite(piece));

						// 次の自分の番を考え、一番小さい開放度を取得する
						// 自分が置けない場合、自分の次の手の開放度を0とする

						int afterNextKaihoudo = returnMinKaihoudo(afterNextBoard, piece);
						Position afterNextPosition = returnPlace(afterNextBoard, piece);


//						// 2回読む
//						// 盤の生成
//						Board board3 = new Board(afterNextBoard.getBoard());
//						board3.putPiece(afterNextPosition.getX(), afterNextPosition.getY(), piece);
//
//						// その次の相手の番
//						int kaihoudo3 = returnMinKaihoudo(board3, Piece.opposite(piece));
//						Position position3 = returnPlace(board3, Piece.opposite(piece));
//
//						// 盤の生成
//						Board board4 = new Board(board3.getBoard());
//						board4.putPiece(position3.getX(), position3.getY(), Piece.opposite(piece));
//
//						// その次の自分の番
//						int kaihoudo4 = returnMinKaihoudo(board4, piece);
//						Position position4 = returnPlace(board4, piece);
//
//
//						// 3回読む
//						// 盤の生成
//						Board board5 = new Board(board4.getBoard());
//						board5.putPiece(position4.getX(), position4.getY(), piece);
//
//						// その次の相手の番
//						int kaihoudo5 = returnMinKaihoudo(board5, Piece.opposite(piece));
//						Position position5 = returnPlace(board5, Piece.opposite(piece));
//
//						// 盤の生成
//						Board board6 = new Board(board5.getBoard());
//						board6.putPiece(position5.getX(), position5.getY(), Piece.opposite(piece));
//
//						// その次の自分の番
//						int kaihoudo6 = returnMinKaihoudo(board6, piece);
//						Position position6 = returnPlace(board6, piece);
//
//						// 4回読む
//						// 盤の生成
//						Board board7 = new Board(board6.getBoard());
//						board7.putPiece(position6.getX(), position6.getY(), piece);
//
//						// その次の相手の番
//						int kaihoudo7 = returnMinKaihoudo(board7, Piece.opposite(piece));
//						Position position7 = returnPlace(board7, Piece.opposite(piece));
//
//						// 盤の生成
//						Board board8 = new Board(board7.getBoard());
//						board8.putPiece(position7.getX(), position7.getY(), Piece.opposite(piece));
//
//						// その次の自分の番
//						int kaihoudo8 = returnMinKaihoudo(board8, piece);
//						Position position8 = returnPlace(board8, piece);


						// 発展開放度 ＝ 自分の開放度 - 相手の開放度 + 次の自分の開放度
						int bestKaihoudo = value - oppositeKaihoudo + afterNextKaihoudo;
								- kaihoudo3 + kaihoudo4 - kaihoudo5 + kaihoudo6 - kaihoudo7 + kaihoudo8;

						// 真に小さい開放度が見つかった場合
						if (bestKaihoudo < kaihoudo) {

							kaihoudo = bestKaihoudo;

							positionList.clear();
							positionList.add(new Position(i, j));

						} else if (bestKaihoudo == kaihoudo) {
							// 等しい開放度が見つかった場合

							positionList.add(new Position(i, j));
						}

					}
				}
			}
		}

		// 座標をシャッフルする
		Collections.shuffle(positionList);

		if (!(positionList.size() == 0)) {

			// 座標を1個取り出す
			x = positionList.get(0).getX();
			y = positionList.get(0).getY();

		}

		// 置く場所をPositionオブジェクトに変換して返す
		return new Position(x, y);
	}

	/**
	 * 自分の駒を置く場所を決めるメソッド
	 *
	 * @param board
	 * @param piece
	 * @return
	 */

	public Position KaihoudoLogic(Board board, Piece piece) {

		// 開放度が最小となる座標の候補を求める
		Position position = returnPlace(board, piece);

		// 置く場所をPositionオブジェクトに変換して返す
		return position;

	}

	/**
	 * 最小の開放度を返す
	 *
	 * @param board
	 * @param piece
	 * @return
	 */

	public int returnMinKaihoudo(Board board, Piece piece) {

		// 次に置ける場所を探す
		int kaihoudo = 0;

		// 評価値が最小となる座標を格納するリスト
		List<Position> positionList = new ArrayList<>();

		// 置ける場所の単純検索
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {

				if (board.isEnablePosition(i, j, piece)) {

					// 置けるかどうかを確認し、置けないのなら何もしない
					if (!board.isEnablePosition(i, j, piece)) {
						continue;
					}

					// ただし四隅は取らない
					if (i == 0 && j == 0) {
						continue;
					} else if (i == 7 && j == 0) {
						continue;
					} else if (i == 0 && j == 7) {
						continue;
					} else if (i == 7 && j == 7) {
						continue;
					} else {

						// 同じ盤面を表すオブジェクトを生成し、自分の駒を置く
						Board nextBoard = new Board(board.getBoard());

						nextBoard.putPiece(i, j, piece);

						// 駒を置いた後の盤面で、暫定的な開放度を計算する
						int value = calculateValue(board, nextBoard, piece);

						// 真に小さい開放度が見つかった場合
						if (value < kaihoudo) {

							kaihoudo = value;

							positionList.clear();
							positionList.add(new Position(i, j));

						} else if (value == kaihoudo) {
							// 等しい開放度が見つかった場合

							positionList.add(new Position(i, j));
						}

					}
				}
			}

			// 置けない場合は開放度を0と定義する
			if (!board.hasEnablePositions(piece)) {

				kaihoudo = 0;

			}
		}

		return kaihoudo;

	}

	/**
	 * 開放度が最小となる場所を返す
	 *
	 * @param board
	 * @param piece
	 * @return
	 */
	public Position returnPlace(Board board, Piece piece) {

		// 次に置ける場所を探す
		int kaihoudo = 100;
		int x = -1;
		int y = -1;

		// 評価値が最小となる座標を格納するリスト
		List<Position> positionList = new ArrayList<>();

		// 置ける場所の単純検索
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {

				if (board.isEnablePosition(i, j, piece)) {

					// 置けるかどうかを確認し、置けないのなら何もしない
					if (!board.isEnablePosition(i, j, piece)) {
						continue;
					}

					// ただし四隅は取らない
					if (i == 0 && j == 0) {
						continue;
					} else if (i == 7 && j == 0) {
						continue;
					} else if (i == 0 && j == 7) {
						continue;
					} else if (i == 7 && j == 7) {
						continue;
					} else {

						// 同じ盤面を表すオブジェクトを生成し、自分の駒を置く
						Board nextBoard = new Board(board.getBoard());

						nextBoard.putPiece(i, j, piece);

						// 駒を置いた後の盤面で、暫定的な開放度を計算する
						int value = calculateValue(board, nextBoard, piece);

						// 真に小さい開放度が見つかった場合
						if (value < kaihoudo) {

							kaihoudo = value;

							positionList.clear();
							positionList.add(new Position(i, j));

						} else if (value == kaihoudo) {
							// 等しい開放度が見つかった場合

							positionList.add(new Position(i, j));
						}

					}
				}
			}
		}

		// 座標をシャッフルする
		Collections.shuffle(positionList);

		if (!(positionList.size() == 0)) {

			// 座標を1個取り出す
			x = positionList.get(0).getX();
			y = positionList.get(0).getY();

		}

		// 置く場所をPositionオブジェクトに変換して返す
		return new Position(x, y);

	}

	/**
	 * 開放度を計算する
	 *
	 * @param board
	 * @param nextBoard
	 * @param piece
	 * @return
	 */
	public int calculateValue(Board board, Board nextBoard, Piece piece) {

		int value = 0;

		// こまが変わる座標を入れたリストの作成
		List<Position> positionList = new ArrayList<>();

		// 変化する座標を記録する
		positionList = checkReturnPlace(board, nextBoard, piece);

		// ひっくり返せる駒の座標の周囲にいくつ空きますがあるかを計算する

		for (Position pos : positionList) {

			value = value + countEnablePosition(nextBoard, piece, pos);

		}

		// 和を開放度とする
		return value;
	}

	/**
	 * 駒を置く前と後で比較して、変化のある座標を返す
	 *
	 * @param board
	 * @param nextBoard
	 * @param piece
	 * @return
	 */
	private List<Position> checkReturnPlace(Board board, Board nextBoard, Piece piece) {

		// 初めのボードと自分が置いた後のボードを比較し、
		// 相手の色が自分の色になっている部分の座標を取り出す

		// 座標を入れるリスト
		List<Position> list = new ArrayList<>();

		// 自分の駒と相手の駒が異なる部分の座標を記録する
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {

				// 相手の駒が自分の駒になった場合を考える
				if (board.getPiece(i, j) == Piece.opposite(piece)
						&& nextBoard.getPiece(i, j) != Piece.opposite(piece)) {

					// 場所を取得する
					Position position = new Position(i, j);

					list.add(position);
				}
			}

		}

		return list;
	}

	/**
	 * ある座標の周囲の空きマス数を数える
	 *
	 * @param board
	 * @param piece
	 * @param position
	 * @return
	 */
	private int countEnablePosition(Board board, Piece piece, Position position) {

		// 空きます数
		int count = 0;

		// 座標を取得する
		int x = position.getX();
		int y = position.getY();

		for (int i = x - 1; i < x + 2; i++) {
			for (int j = y - 1; j < y + 2; j++) {

				// オセロ版の範囲外は考えない
				if (i > -1 && i < 8 && j > -1 && j < 8) {

					// 空きますだったらカウント+1
					if (board.getPiece(i, j) != Piece.BLACK && board.getPiece(i, j) != Piece.WHITE)

						count = count + 1;

				}
			}
		}
		return count;
	}

}

