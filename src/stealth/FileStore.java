package stealth;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/*
 * Track the high scores.
 */

public class FileStore {
	public final static int TRACK_X_SCORES = 10;
		
	public final static String FILE_PATH = "src/stealth/resources/score.txt";

	/**
	 * Persists scores to a file
	 * 
	 * @param scores. An array representing the scores to save. 
	 */
	private static void saveHighScore(ArrayList<Integer> scores) {
		try {
			FileWriter file = new FileWriter(FILE_PATH); 
			for (int score: scores) {
				file.write(String.valueOf(score) + "\n");
			}
			file.close();
			
		} catch (IOException e) {
			System.out.println("An error occurred while saving the high scores to file");
		}
	}
	
	/**
	 * Read the score file
	 * @return an order list with the score
	 */
	public static ArrayList<Integer> getHighScores() {
		ArrayList<Integer> scores = new ArrayList<>();
		FileReader file;
		String line;
		try {
			file = new FileReader(FILE_PATH);
			BufferedReader bFile = new BufferedReader(file);
			while ((line = bFile.readLine()) != null) {
				scores.add(Integer.parseInt(line));
			}
			Collections.sort(scores, Collections.reverseOrder());
			bFile.close();
		} catch (IOException e) {
			// we don't care much
			System.out.printf("File %s doesn't exist\n", FILE_PATH);
		}
		return scores;
	}
	
	/*
	 * Add a score to the list of scores that are persisted in the file storage
	 * This method also checks for uniqueness making sure that scores are not repeated
	 */
	public static void addHighScore(int score) {
		ArrayList<Integer> currentScores = FileStore.getHighScores();
		currentScores.add(score);
		Set<Integer> scoreSet = new HashSet<>();
		scoreSet.addAll(currentScores);
		currentScores.clear();
		currentScores.addAll(scoreSet);
		FileStore.saveHighScore(currentScores);
	}
	
}
