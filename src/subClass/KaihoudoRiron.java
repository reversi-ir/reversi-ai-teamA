package subClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jp.takedarts.reversi.Board;
import jp.takedarts.reversi.Piece;
import jp.takedarts.reversi.Position;

public class KaihoudoRiron {

	public Position KaihoudoLogic(Board board, Piece piece) {

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

						// 駒を置いた後の盤面で、開放度を計算する
						int value = calculateValue(board, nextBoard, piece);

						// 開放度が最小となるものを保存する（複数ある場合はランダム抽出）
						// ここで相手の手を読む要素を取り入れたいが、まずはランダムでやってみる

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
