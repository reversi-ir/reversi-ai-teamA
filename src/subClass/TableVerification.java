package subClass;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import jp.takedarts.reversi.Board;
import jp.takedarts.reversi.Piece;
import jp.takedarts.reversi.Position;

/**
 * モンテカルロ法の検証用ツール （出力結果(黒の駒数、白の駒数、処理時間)をcsvファイルへ書き出し）
 *
 * @author 1517643
 *
 */
public class TableVerification {

	public static void main(String[] args) {

		int count;
		int i = 0;
		int j = 0;
		int first = 0;
		MinMax minmax = new MinMax();

		// ファイル読み込み
		try {

			File file = new File(System.getProperty("user.dir") + "/" + "valueTable.csv");
			BufferedReader br = new BufferedReader(new FileReader(file));

			String str = br.readLine();
			if (str != null) {

				first = Integer.parseInt(str);

			} else {

				first = 0;
			}

			br.close();

		} catch (FileNotFoundException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}

		// 外側ループ：ランダム評価テーブルを何回か試す
		for (count = 0; count < 8; count++) {

			/////////// メインに書きたい：乱数を発生させて座標の各位置に評価値を入力するプログラム//////////////

			int[][] _VALUES = new int[8][8];

			for (i = 0; i < 8; i++) {
				for (j = 0; j < 8; j++) {

					if ((i == 0 && j == 0) || (i == 0 && j == 7) || (i == 7 && j == 0) || (i == 7 && j == 7)) {
						_VALUES[i][j] = 100;

					} else {

						_VALUES[i][j] = minmax.randomvalue();
					}
					// sysoutは結果確認用なので、実装するときに不要であれば抜かしてください。
					System.out.println("(" + i + j + ")" + _VALUES[i][j]);
				}

			}

			try {

				int result = 0;
				int test = 0;
				int a = 0;
				int b = 0;

				List<String> randomList = new ArrayList<>();

				// 出力先を作成する
				FileOutputStream fos = new FileOutputStream(System.getProperty("user.dir") + "/" + "result.csv", false);
				OutputStreamWriter osw = new OutputStreamWriter(fos, "SJIS");
				BufferedWriter fw = new BufferedWriter(osw);
				PrintWriter pw = new PrintWriter(new BufferedWriter(fw));

				FileOutputStream fos_board = new FileOutputStream(
						System.getProperty("user.dir") + "/" + "resultBoard.csv", false);
				OutputStreamWriter osw_board = new OutputStreamWriter(fos_board, "SJIS");
				BufferedWriter fw_board = new BufferedWriter(osw_board);
				PrintWriter pw_board = new PrintWriter(new BufferedWriter(fw_board));

				FileOutputStream fos_valueTable = new FileOutputStream(
						System.getProperty("user.dir") + "/" + "valueTable.csv", false);
				OutputStreamWriter osw_valueTable = new OutputStreamWriter(fos_valueTable, "SJIS");
				BufferedWriter fw_valueTable = new BufferedWriter(osw_valueTable);
				PrintWriter pw_valueTable = new PrintWriter(new BufferedWriter(fw_valueTable));

				FileOutputStream fos_randomValueTable = new FileOutputStream(
						System.getProperty("user.dir") + "/" + "random" + "/" + "valueTable.csv", false);
				OutputStreamWriter osw_randomValueTable = new OutputStreamWriter(fos_randomValueTable, "SJIS");
				BufferedWriter fw_randomValueTable = new BufferedWriter(osw_randomValueTable);
				PrintWriter pw_randomValueTable = new PrintWriter(new BufferedWriter(fw_randomValueTable));

				String stringRandomValues[] = new String[8];
				String randomStr;

				for (a = 0; a < 8; a++) {
					for (b = 0; b < 8; b++) {

						randomStr = String.valueOf(_VALUES[a][b]);

						stringRandomValues[b] = randomStr;

						if (b == 7) {
							randomList = Arrays.stream(stringRandomValues).map(line -> String.join(",", line))
									.collect(Collectors.toList());

							pw_randomValueTable.print(randomList);
							pw_valueTable.println();

						}
					}
				}

				// ファイルに書き出す

				pw_randomValueTable.close();

				Board testBoard = new Board();

				testBoard.putPiece(3, 3, Piece.WHITE);
				testBoard.putPiece(4, 4, Piece.WHITE);

				testBoard.putPiece(3, 4, Piece.BLACK);
				testBoard.putPiece(4, 3, Piece.BLACK);

				Board playBoard = new Board(testBoard.getBoard());

				// black ←ここを更新
				Main myProcessor = new Main();
				Piece piece = Piece.BLACK;

				// white ←ここを更新
				SampleMinMax opponentProcessor = new SampleMinMax();
				Piece opponentPiece = Piece.WHITE;

				// System.out.println("AI(BLACK)(自分)： " + myProcessor.getName());
				// System.out.println("AI(WHITE)(相手)： " + opponentProcessor.getName());
				// System.out.println("");

				// System.out.println(testBoard);
				// System.out.println("");

				pw.print("AI(BLACK)：");
				pw.print(",");
				pw.print(myProcessor.getName());
				pw.println();

				pw.print("AI(WHITE)：");
				pw.print(",");
				pw.print(opponentProcessor.getName());
				pw.println();

				pw.println();

				pw.print("No");
				pw.print(",");
				pw.print("黒の駒数");
				pw.print(",");
				pw.print("白の駒数");
				pw.print(",");
				pw.print("処理時間(ms)");
				pw.println();

				// pw_board.print(testBoard);
				// pw_board.println();

				for (test = 1; test <= 100; test++) { // test:対戦回数

					long to; // 処理時間を所持
					long time; // 実行時間を所持

					System.out.println(test + "回目");

					to = System.currentTimeMillis();

					while (playBoard.hasEnablePositions(piece) || playBoard.hasEnablePositions(opponentPiece)) {

						// 自分の手を置く
						if (playBoard.hasEnablePositions(piece)) {

							Position myPosition = myProcessor.nextPosition(playBoard, piece, 30000);

							playBoard.putPiece(myPosition, piece);

							//System.out.println(playBoard);
							//System.out.println("");

						} else if (!playBoard.hasEnablePositions(piece)) {

							System.out.println(piece + "：　パス");
							//System.out.println("");
						}

						if (playBoard.hasEnablePositions(opponentPiece)) {

							Position opponentPosition = opponentProcessor.nextPosition(playBoard, opponentPiece, 30000);
							playBoard.putPiece(opponentPosition, opponentPiece);
							//System.out.println("");

						} else if (!playBoard.hasEnablePositions(opponentPiece)) {

							System.out.println(opponentPiece + "：　パス");
							//System.out.println("");

						}

						//System.out.println(playBoard);
						//System.out.println("");

					}

					// System.out.println("黒の数： " + playBoard.countPiece(piece));
					// System.out.println("白の数： " + playBoard.countPiece(opponentPiece));

					time = System.currentTimeMillis() - to;

					pw.print(test);
					pw.print(",");
					pw.print(playBoard.countPiece(piece));
					pw.print(",");
					pw.print(playBoard.countPiece(opponentPiece));
					pw.print(",");
					pw.print(time);
					pw.println();

					pw_board.print(test);
					pw_board.println();
					pw_board.print(playBoard);
					pw_board.println();

					// try {

					// Files.write(Paths.get(System.getProperty("user.dir"), "out.csv"), list,
					// StandardOpenOption.CREATE);
					// } catch (IOException e) {
					// e.printStackTrace();
					// }

					playBoard = new Board(testBoard.getBoard());

					if ((playBoard.countPiece(piece) - playBoard.countPiece(opponentPiece)) > 0) {

						result += 1;

					}

				}
				// CSVから結果を読み込む

				// 現状1位と今回の結果を比較する
				if (first < result) {

					first = result;

					String stringValues[] = new String[8];
					String iStr;
					List<String> firstList = new ArrayList<>();

					pw_valueTable.print(first);
					pw_valueTable.println();

					for (a = 0; a < 8; a++) {
						for (b = 0; b < 8; b++) {

							iStr = String.valueOf(_VALUES[b]);

							stringValues[b] = iStr;

							if (b == 7) {
								firstList = Arrays.stream(stringValues).map(line -> String.join(",", line))
										.collect(Collectors.toList());

								pw_valueTable.print(firstList);
								pw_valueTable.println();
							}
						}
					}

				}

				// ファイルに書き出す
				pw.close();
				pw_board.close();
				pw_valueTable.close();

			} catch (IOException ex) {
				// 例外時処理
				ex.printStackTrace();

			} catch (ArrayIndexOutOfBoundsException e) {

				System.out.println("例外：" + e);
				System.out.println("引数を一つ入力してください。");

			}

		}
	}
}
