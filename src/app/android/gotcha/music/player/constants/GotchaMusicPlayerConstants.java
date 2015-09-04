package app.android.gotcha.music.player.constants;

public class GotchaMusicPlayerConstants {

	public static enum CONFIG_VALUE_FROM_DATABASE {
		DEFAULT_PATH,
		IS_REPLY_ENABLED
	};
	public static final int DEFAULT_MAX_DIRECTORY_LEVEL = 4;
	public static final int DEFAULT_MIN_SONGS_REQUIRED = 5;
	public static String CONSTANT_VALUE_YES = "yes" ;
	public static String CONSTANT_VALUE_NO = "no";
	public static enum SUPORTED_MUSIC_FORMAT{
		mp3,
		mid,
		aac,
		ogg,
		wav
	};
	
	public static String SONG_PLAY_SUCCESS_MSG_BODY = "Hello Requester.. Your request is processed and your song is played \n SONG PATH: {0}";
	public static String SONG_PLAY_FAILED_MSG_BODY = "Sorry... Your Request Failed... Try After Some Time";
	public static String SONG_PLAY_MSG_ENCLOSING_BODY = "GotchaReply: \n {0} \n***Good Luck***";
	public static String TOAST_STRING_RECIEVED_REQUEST = "SMS from : {0} \n {1}";
	
	public static String GOTCHA_REQUEST_NASREQ = "NaSReq";
}

