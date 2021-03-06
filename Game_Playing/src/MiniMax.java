import java.io.PrintWriter;
import java.util.Collection;

public class MiniMax {
	private static Score nextStateMiniMax(Pit pit, Board board, int player,
			int depth, PrintWriter writer, boolean test) throws Exception {
		Score finalScore = null;

		if (board.gameOver(board, player) != 0) {

			finalScore = new Score(board, calcEval(0, board), depth, pit, false);
			GameUtils
					.doGameOver(finalScore.move, board.gameOver(board, player));
			finalScore.temp = true;
		} else {
			player = pit.player;
			boolean repChance = false;
			Board x = (Board) board.clone();
			int noOfManc = pit.numberSeeds;
			if (player == 1) {
				x.lowPits.get(pit.position).numberSeeds = 0;
			} else {
				x.highPits.get(pit.position).numberSeeds = 0;

			}

			Pit p = null;
			int p2 = 0;
			if (player == 2) {
				p = x.highMancala;
			} else {
				p = x.highPits.get(0);
				p2 = 1;
			}
			MancalaList node1 = new MancalaList(p);

			for (int i = p2; i < x.highPits.size(); i++) {
				node1.InsertNext(x.highPits.get(i));

			}
			if (player == 1) {
				node1.InsertNext(x.lowMancala);
			}
			for (int i = x.lowPits.size() - 1; i >= 0; i--) {
				node1.InsertNext(x.lowPits.get(i));

			}

			boolean findCheck = false;
			int oppPosition = 0;
			// if (yourPlayer == 1) {
			boolean startRemoving = false;

			int pos = 0;
			while (noOfManc > 0) {
				if (!findCheck && equalPit(node1.data, pit)) {

					findCheck = true;
					startRemoving = true;
					node1 = node1.next;

					continue;
				}
				if (startRemoving) {
					noOfManc--;
					if (node1.data.position != -1) {
						if (node1.data.player == 1) {

							x.lowPits.get(node1.data.position).numberSeeds++;
						} else if (node1.data.player == 2) {
							x.highPits.get(node1.data.position).numberSeeds++;

						}
					} else {
						if (node1.data.player == 1) {
							x.lowMancala.numberSeeds++;
						} else if (node1.data.player == 2) {
							x.highMancala.numberSeeds++;

						}
					}

				}
				oppPosition = node1.data.player;
				pos = node1.data.position;
				node1 = node1.next;

			}

			if (player == oppPosition && pos == -1) {

				repChance = true;
				finalScore = new Score(x, calcEval(0, x), depth, pit, repChance);
				if (x.gameOver(board, player) != 0) {
					finalScore.temp = true;
				}
				// }
				return finalScore;
			} else if (player == oppPosition && pos < x.lowPits.size()) {
				if (player == 1) {

					if (x.lowPits.get(pos).numberSeeds == 1) {
						x.lowMancala.numberSeeds = x.lowMancala.numberSeeds
								+ x.highPits.get(pos).numberSeeds + 1;
						x.highPits.get(pos).numberSeeds = 0;
						x.lowPits.get(pos).numberSeeds = 0;
					}
				} else if (player == 2) {
					if (x.highPits.get(pos).numberSeeds == 1) {
						x.highMancala.numberSeeds = x.highMancala.numberSeeds
								+ x.lowPits.get(pos).numberSeeds + 1;
						x.highPits.get(pos).numberSeeds = 0;
						x.lowPits.get(pos).numberSeeds = 0;
					}

				}
			}

			finalScore = new Score(x, calcEval(0, x), depth, pit, repChance);
			if (x.gameOver(board, player) != 0) {
				finalScore.temp = true;
			}
		}
		return finalScore;
	}

	private static Score recMiniMax(Board game, Pit pit, int yourPlayer,
			int depth, int player, boolean repChance, PrintWriter writer,
			int maxMin, int cutOFF) throws Exception {
		// TODO Auto-generated method stub
		if (pit == null) {
			writer.println("Node,Depth,Value");
		}
		if (null != pit && (game.gameOver(game, player) != 0) || depth == 0) {
			Score s = new Score(game, 0, 0, pit, false);
			int check = game.gameOver(game, player);
			if (check != 0) {
				s.temp = true;
				if (check == 1) {
					s.move = GameUtils.doGameOver(s.move, check);
				}

				else if (check == 2) {
					s.move = GameUtils.doGameOver(s.move, check);

				}

			}

			int eval = calcEval(yourPlayer, game);
			int xp = depth;
			int opp2Player = yourPlayer == 1 ? 2 : 1;

			if (player != opp2Player && pit.player == yourPlayer&&depth!=0) {
				xp = depth - 1;
			} else {
				if (player == opp2Player && pit.player == opp2Player&&depth!=0) {
					
					xp = depth -1;

				}
			}
			if (pit.player == 1)
				writer.println("B" + (pit.position + 2) + ","
						+ printDepth(xp, cutOFF) + ","
						+ GameUtils.printVal(eval) );
			else
				writer.println("A" + (pit.position + 2) + ","
						+ printDepth(xp, cutOFF) + ","
						+ GameUtils.printVal(eval) );

			s.eval = eval;
			return s;
		}

		int bestScore = 0;
		Score bestSc = null;
		bestSc = new Score(game, bestScore, depth, pit, repChance);

		if (player == yourPlayer) {
			Collection<Pit> pits = (yourPlayer == 1 ? game.lowPits.values()
					: game.highPits.values());
			if (maxMin == 1) {
				bestScore = Integer.MIN_VALUE;
			} else {
				bestScore = Integer.MAX_VALUE;

			}
			if (pit == null) {
				writer.println("root," + printDepth(depth, cutOFF) + ","
						+ GameUtils.printVal(bestScore));
			} else {
				int xp = depth;
				int oppPlayer = yourPlayer == 1 ? 2 : 1;

				if (pit.player != oppPlayer) {
					xp = depth - 1;
				}
				if (pit.player == 1)
					writer.println("B" + (pit.position + 2) + ","
							+ printDepth(xp, cutOFF) + ","
							+ GameUtils.printVal(bestScore));
				else
					writer.println("A" + (pit.position + 2) + ","
							+ printDepth(xp, cutOFF) + ","
							+ GameUtils.printVal(bestScore));

			}
			bestSc = new Score(game, bestScore, depth, pit, false);

			for (Pit p : pits) {
				Pit pit2 = (Pit) p.clone();

				if (p.numberSeeds > 0) {
					Board b = (Board) game.clone();
					Score xboard = nextStateMiniMax(pit2, b, player, depth - 1,
							writer, false);

					int oppPlayer = yourPlayer == 1 ? 2 : 1;

					if (xboard.repChance) {
						Score score = recMiniMax(xboard.move, pit2, yourPlayer,
								depth, yourPlayer, true, writer, 1, cutOFF);
						Score xboard2 = score;

						if (maxMin == 1) {
							int x = score.eval;
							if (x > bestScore) {
								bestScore = x;
								bestSc = (Score) new Score(xboard2.move, x,
										depth, pit2, false);
								bestSc.temp = xboard2.temp;
							}
							if (pit == null) {
								writer.println("root,"
										+ printDepth(depth, cutOFF) + ","
										+ GameUtils.printVal(bestScore));
							} else {

								int xp = depth;
								int opp2Player = yourPlayer == 1 ? 2 : 1;

								if (pit.player != opp2Player) {
									xp = depth - 1;
								}

								if (pit.player == 1)
									writer.println("B" + (pit.position + 2)
											+ "," + printDepth(xp, cutOFF)
											+ ","
											+ GameUtils.printVal(bestScore));
								else
									writer.println("A" + (pit.position + 2)
											+ "," + printDepth(xp, cutOFF)
											+ ","
											+ GameUtils.printVal(bestScore));

							}
						} else {

							int x = score.eval;
							if (x < bestScore) {
								bestScore = x;
								bestSc = (Score) new Score(xboard2.move, x,
										depth, pit2, false);
								bestSc.temp = xboard2.temp;

							}
							if (pit == null) {
								writer.println("root,"
										+ printDepth(depth, cutOFF) + ","
										+ GameUtils.printVal(bestScore));
							} else {
								int xp = depth;
								int opp2Player = yourPlayer == 1 ? 2 : 1;

								if (pit.player != opp2Player) {
									xp = depth - 1;
								}
								if (pit.player == 1)
									writer.println("B" + (pit.position + 2)
											+ "," + printDepth(xp, cutOFF)
											+ ","
											+ GameUtils.printVal(bestScore));
								else
									writer.println("A" + (pit.position + 2)
											+ "," + printDepth(xp, cutOFF)
											+ ","
											+ GameUtils.printVal(bestScore));

							}

						}
					} else {
						Score score = recMiniMax(xboard.move, pit2, yourPlayer,
								depth - 1, oppPlayer, true, writer, -1, cutOFF);
						int x = score.eval;
						if (maxMin == 1) {
							if (x > bestScore) {
								bestScore = x;
								bestSc = (Score) new Score(xboard.move, x,
										depth, pit2, false);
								bestSc.temp = score.temp;

							}
							if (pit == null) {
								writer.println("root,"
										+ printDepth(depth, cutOFF) + ","
										+ GameUtils.printVal(bestScore));
							} else {
								int xp = depth;
								int opp2Player = yourPlayer == 1 ? 2 : 1;

								if (player == opp2Player
										&& pit.player == opp2Player) {
									xp = depth + 1;
								} else if (player != opp2Player
										&& pit.player != opp2Player) {
									xp = depth - 1;
								}
								if (pit.player == 1)
									writer.println("B" + (pit.position + 2)
											+ "," + printDepth(xp, cutOFF)
											+ ","
											+ GameUtils.printVal(bestScore));
								else
									writer.println("A" + (pit.position + 2)
											+ "," + printDepth(xp, cutOFF)
											+ ","
											+ GameUtils.printVal(bestScore));

							}
						}

						else {

							if (x < bestScore) {
								bestScore = x;
								bestSc = (Score) new Score(xboard.move, x,
										depth, pit2, false);
								bestSc.temp = score.temp;

							}
							if (pit == null) {
								writer.println("root,"
										+ printDepth(depth, cutOFF) + ","
										+ GameUtils.printVal(bestScore));
							} else {
								int xp = depth;
								int opp2Player = yourPlayer == 1 ? 2 : 1;

								if (player == opp2Player
										&& pit.player == opp2Player) {
									xp = depth + 1;
								} else if (player != opp2Player
										&& pit.player != opp2Player) {
									xp = depth - 1;
								}
								if (pit.player == 1)
									writer.println("B" + (pit.position + 2)
											+ "," + printDepth(xp, cutOFF)
											+ ","
											+ GameUtils.printVal(bestScore));
								else
									writer.println("A" + (pit.position + 2)
											+ "," + printDepth(xp, cutOFF)
											+ ","
											+ GameUtils.printVal(bestScore));

							}

						}
					}

				}

				// maximise
			}

		} else {
			Collection<Pit> pits = (player == 1 ? game.lowPits.values()
					: game.highPits.values());

			if (maxMin == 1) {
				bestScore = Integer.MIN_VALUE;
			} else {
				bestScore = Integer.MAX_VALUE;

			}
			if (pit == null) {
				writer.println("root," + printDepth(depth, cutOFF) + ","
						+ GameUtils.printVal(bestScore));
			} else {
				int xp = depth;
				int oppPlayer = yourPlayer == 1 ? 2 : 1;

				if (player == oppPlayer && pit.player == oppPlayer) {
					xp = depth - 1;
				}
				if (pit.player == 1)
					writer.println("B" + (pit.position + 2) + ","
							+ printDepth(xp, cutOFF) + ","
							+ GameUtils.printVal(bestScore));
				else
					writer.println("A" + (pit.position + 2) + ","
							+ printDepth(xp, cutOFF) + ","
							+ GameUtils.printVal(bestScore));

			}
			for (Pit p : pits) {
				Pit pit2 = (Pit) p.clone();
				if (p.numberSeeds > 0) {
					Board b = (Board) game.clone();

					Score xboard = nextStateMiniMax(pit2, b, player, depth - 1,
							writer, false);

					if (xboard.repChance) {

						Score score = recMiniMax(xboard.move, pit2, yourPlayer,
								depth, player, true, writer, -1, cutOFF);
						Score xboard2 = score;

						if (maxMin == 1) {
							int x = score.eval;
							if (x > bestScore) {
								bestScore = x;
								bestSc = (Score) new Score(xboard2.move, x,
										depth, pit2, false);
								bestSc.temp = xboard2.temp;

							}
							if (pit == null) {
								writer.println("root,"
										+ printDepth(depth, cutOFF) + ","
										+ GameUtils.printVal(bestScore));
							} else {
								int xp = depth;
								int opp2Player = yourPlayer == 1 ? 2 : 1;

								if (player == opp2Player
										&& pit.player == opp2Player) {
									xp = depth - 1;
								}
								if (pit.player == 1)
									writer.println("B" + (pit.position + 2)
											+ "," + printDepth(xp, cutOFF)
											+ ","
											+ GameUtils.printVal(bestScore));
								else
									writer.println("A" + (pit.position + 2)
											+ "," + printDepth(xp, cutOFF)
											+ ","
											+ GameUtils.printVal(bestScore));

							}
						} else {

							int x = score.eval;
							if (x < bestScore) {
								bestScore = x;
								bestSc = (Score) new Score(xboard2.move, x,
										depth, pit2, false);
								bestSc.temp = xboard2.temp;
							}
							if (pit == null) {
								writer.println("root,"
										+ printDepth(depth, cutOFF) + ","
										+ bestScore);
							} else {
								int xp = depth;
								int opp2Player = yourPlayer == 1 ? 2 : 1;

								if (player == opp2Player
										&& pit.player == opp2Player) {
									xp = depth - 1;
								}
								if (pit.player == 1)
									writer.println("B" + (pit.position + 2)
											+ "," + printDepth(xp, cutOFF)
											+ ","
											+ GameUtils.printVal(bestScore));
								else
									writer.println("A" + (pit.position + 2)
											+ "," + printDepth(xp, cutOFF)
											+ ","
											+ GameUtils.printVal(bestScore));

							}

						}
					} else {

						Score score = recMiniMax(xboard.move, pit2, yourPlayer,
								depth - 1, yourPlayer, true, writer, 1, cutOFF);
						if (maxMin == 1) {
							int x = score.eval;
							if (x > bestScore) {
								bestScore = x;
								bestSc = (Score) new Score(score.move, x,
										depth, pit2, false);
								bestSc.temp = score.temp;

							}
							if (pit == null) {
								writer.println("root,"
										+ printDepth(depth, cutOFF) + ","
										+ GameUtils.printVal(bestScore));
							} else {
								int xp = depth;
								int opp2Player = yourPlayer == 1 ? 2 : 1;

								if (player == opp2Player
										&& pit.player == opp2Player) {
									xp = depth - 1;
								} else {
									// xp = depth + 1;
								}
								if (pit.player == 1)
									writer.println("B" + (pit.position + 2)
											+ "," + printDepth(xp, cutOFF)
											+ ","
											+ GameUtils.printVal(bestScore));
								else
									writer.println("A" + (pit.position + 2)
											+ "," + printDepth(xp, cutOFF)
											+ ","
											+ GameUtils.printVal(bestScore));

							}
						} else {

							int x = score.eval;
							if (x < bestScore) {
								bestScore = x;
								bestSc = (Score) new Score(score.move, x,
										depth, pit2, false);
								bestSc.temp = score.temp;

							}
							if (pit == null) {
								writer.println("root,"
										+ printDepth(depth, cutOFF) + ","
										+ GameUtils.printVal(bestScore));
							} else {
								int xp = depth;
								int opp2Player = yourPlayer == 1 ? 2 : 1;

								if (player == opp2Player
										&& pit.player == opp2Player) {
									xp = depth - 1;
								} else {
									// xp = depth + 1;
								}
								if (pit.player == 1)
									writer.println("B" + (pit.position + 2)
											+ "," + printDepth(xp, cutOFF)
											+ ","
											+ GameUtils.printVal(bestScore));
								else
									writer.println("A" + (pit.position + 2)
											+ "," + printDepth(xp, cutOFF)
											+ ","
											+ GameUtils.printVal(bestScore));

							}

						}

					}
				}
			}
			// minimise

		}

		return bestSc;
	}

	public static int printDepth(int depth, int cutOff) {

		return cutOff - depth;
	}

	public static boolean equalPit(Pit data, Pit pit) {
		if (data.player == pit.player && data.position == pit.position)
			// TODO Auto-generated method stub
			return true;
		return false;
	}

	// static int calcEval2(Board board) {
	// int eval = 0, sumLowPits = board.lowMancala.numberSeeds, sumHighPits =
	// board.highMancala.numberSeeds;
	// eval = sumLowPits - sumHighPits;
	// return eval;

	// }

	public static Board miniMax(Game game) throws Exception {
		// TODO Auto-generated method stub
		// call recursive minimax
		PrintWriter writer = null;
		writer = new PrintWriter("traverse_log.txt", "UTF-8");

		Score finalMove = recMiniMax(game.board, null, game.yourPlayer,
				game.depth, game.yourPlayer, true, writer, 1, game.depth);
		System.out.println(finalMove.p.player + "--"
				+ (finalMove.p.position + 2));
		writer.close();
		// finalMove.move = nextState(finalMove.p, finalMove.move,
		// game.yourPlayer);
		return finalMove.move;
	}

	static int calcEval(int player, Board board) {
		int eval = 0, sumLowPits = board.lowMancala.numberSeeds, sumHighPits = board.highMancala.numberSeeds;

		eval = sumLowPits - sumHighPits;
		if (player == 1) {
			return eval;
		} else
			return -eval;

	}
}
