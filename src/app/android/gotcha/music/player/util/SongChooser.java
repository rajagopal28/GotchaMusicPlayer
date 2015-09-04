package app.android.gotcha.music.player.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;
import app.android.gotcha.music.player.constants.GotchaMusicPlayerConstants;
import app.android.gotcha.music.player.constants.GotchaMusicPlayerConstants.CONFIG_VALUE_FROM_DATABASE;
import app.android.gotcha.music.player.constants.GotchaMusicPlayerConstants.SUPORTED_MUSIC_FORMAT;
import app.android.gotcha.music.player.dao.GotchaMusicPlayerDAO;

public class SongChooser {
	public static int getSongsCount(File current, int level) {
		int songsCount = 0;
		Log.i("SongChooser", "Count @ " + current.getAbsolutePath() + " level = " + level);
		if (level > 0) {
			File[] fileList = current.listFiles();
			try {
				for (File iter : fileList) {
					if (iter.isDirectory() && iter.canRead()) {
						songsCount += getSongsCount(iter, level - 1);
					} else if (iter.isFile()) {
						if (isSongFile(iter)) {
							songsCount++;
						}
					}
				}
				return songsCount;
			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}
		return 0;
	}

	private static boolean isSongFile(File myFile) {
		String name = myFile.getName();
		String extension = name.substring(name.lastIndexOf(".") + 1);
		boolean isValidExtension = false;
		try {
			SUPORTED_MUSIC_FORMAT.valueOf(extension);
			isValidExtension = true;
		} catch(IllegalArgumentException ex) {
			isValidExtension = false;
		}
		return  isValidExtension;
	}

	public static String getSongUrl(File current, String pattern, int level) {
		if (level >= 0) {
			File[] fileList = current.listFiles();
			try {
				for (File iter : fileList) {
					if (iter.isDirectory()) {
						String temp = getSongUrl(iter, pattern, level - 1);
						if (temp != null) {
							return temp;
						}
					} else if (iter.isFile()) {
						if (isSongFile(iter)) {
							if (iter.getName().toLowerCase()
									.contains(pattern.toLowerCase())) {
								return iter.getAbsolutePath();
							}
						}
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}
		return null;
	}

	public static void playSong(String songPath, Context context) {
		MediaPlayer filePlayer = MediaPlayer.create(context,
				Uri.parse("file://" + songPath));
		try {
			// MediaPlayer filePlayer = MediaPlayer.create(this
			// ,Uri.parse("file:///system/media/audio/ui/camera_click.ogg"));
			// MediaPlayer filePlayer = MediaPlayer.create(this ,R.raw.second);
			filePlayer.start();
		} catch (IllegalStateException e) {			
			e.printStackTrace();
			Toast.makeText(context, "Song play exception" + e.toString(),
					Toast.LENGTH_LONG).show();
		} catch (Exception ex) {
			ex.printStackTrace();
			Toast.makeText(context, "Song play exception" + ex.toString(),
					Toast.LENGTH_LONG).show();
		}
	}

	public static String getSong(String initPath, String pattern) {
		if (initPath.length() > 0 && getSongsCount(new File(initPath), 4) > 0) {
			String path = getSongUrl(new File(initPath), pattern, 4);
			if (path != null) {
				return path;
			}
		}
		return null;
	}

	public static String interpretContent(Context context, String content) {
		content = content.toLowerCase();
		if (content.contains("GotchaPlay".toLowerCase())) {
			Log.i("SongChooser", "interpretContent : " + content);
			content = content.substring(content.indexOf("GotchaPlay".toLowerCase()));
			String[] contentArray = content.split(" ");
			// this message belong to this application
			if (contentArray[1].equals("Random".toLowerCase())) {
				// requester asks for a random song
				// return "Random Song";
				return getRandomSong(context);
			} else if (contentArray[1].equals(
					"PathRandom".toLowerCase())) {
				String path = contentArray[2];
				if (path.length() > 0) {
					// return "Random Song From Path";
					return getRandomSongFrom(path);
				} else {
					return null;
				}
			} else if (contentArray[1].equals(
					"Name".toLowerCase())) {
				String name = contentArray[2];
				if (name.length() > 0) {
					// return "Song From Name:"+name;
					GotchaMusicPlayerDAO dao = new GotchaMusicPlayerDAO(
							context);
					String path = dao
							.getConfigurationValue(CONFIG_VALUE_FROM_DATABASE.DEFAULT_PATH);

					return getSong(path, name);
				} else {
					return null;
				}
			} else if (contentArray[1].equals(
					"Path".toLowerCase())) {
				String myFile = contentArray[2];
				if (myFile.startsWith("/")) {
					if (new File(myFile).isFile()
							&& isSongFile(new File(myFile))) {
						// return "Full Song Path:"+myFile;
						return myFile;
					} else {
						return null;
					}

				}
			} else {
				return null;
			}
		} else {
			return GotchaMusicPlayerConstants.GOTCHA_REQUEST_NASREQ;
		}
		return null;
	}

	public static String getRandomSongFrom(String path) {
		File directory = new File(path);
		int count = 0;
		List<File> mySongFiles = new ArrayList<File>();
		if (directory.isDirectory()) {
			File[] allFiles = directory.listFiles();
			try {
				for (File iter : allFiles) {
					if (iter.isFile() && isSongFile(iter)) {
						mySongFiles.add(iter);
						count++;
					}
				}
				if (!mySongFiles.isEmpty()) {
					return mySongFiles.get(
							(int) (Math.floor(Math.random() * count)))
							.getAbsolutePath();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}
		return null;
	}

	public static void getRandomSongList(File current, List<String> myList,
			int maxLevel) {
		if (maxLevel > 0) {
			File[] allFiles = current.listFiles();
			List<File> filesList = new ArrayList<File>();
			int count = 0;
			try {
				for (File iter : allFiles) {
					if (iter.isDirectory()) {
						getRandomSongList(iter, myList, maxLevel - 1);
					}
					if (iter.isFile() && isSongFile(iter)) {
						filesList.add(iter);
						count++;
					}
				}
				myList.add(filesList.get(
						(int) Math.floor(Math.random() * count))
						.getAbsolutePath());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public static String getRandomSong(Context context) {
		List<String> randomList = new ArrayList<String>();
		GotchaMusicPlayerDAO dao = new GotchaMusicPlayerDAO(context);
		String path = dao
				.getConfigurationValue(CONFIG_VALUE_FROM_DATABASE.DEFAULT_PATH);
		getRandomSongList(new File(path), randomList,
				(int) Math.floor(Math.random() * 3 + 2));
		while (randomList.size() <= 0) {
			getRandomSongList(new File(path), randomList,
					(int) Math.floor(Math.random() * 3 + 2));
		}

		return randomList.get((int) Math.floor(Math.random()
				* randomList.size()));
		// return getSongUrl(new
		// File("/"),String.valueOf((char)(Math.floor(Math.random()*26))+1),(int)(Math.floor(Math.random()*4)+1));
	}

	public static void sendSMS(Context context, String to, String content) {
		PendingIntent pi = PendingIntent.getActivity(context, 0, new Intent(context,
				SongChooser.class), 0);
		SmsManager sms = SmsManager.getDefault();
		Log.i("SongChooser","Sending Reply to :" + to + " content : " + content);
		sms.sendTextMessage(to, null, content, pi, null);
	}
}
