package app.android.gotcha.music.player.reciever;

import java.text.MessageFormat;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;
import app.android.gotcha.music.player.constants.GotchaMusicPlayerConstants;
import app.android.gotcha.music.player.constants.GotchaMusicPlayerConstants.CONFIG_VALUE_FROM_DATABASE;
import app.android.gotcha.music.player.dao.GotchaMusicPlayerDAO;
import app.android.gotcha.music.player.util.SongChooser;

public class SmsReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			GotchaMusicPlayerDAO dao = new GotchaMusicPlayerDAO(context);
			Map<CONFIG_VALUE_FROM_DATABASE, String> allConfigurations = dao
					.getAllconfigurationValues();
			if (allConfigurations.isEmpty()) {
				return;
			}
			Bundle bundle = intent.getExtras();
			SmsMessage[] msgs = null;
			String str = "";
			if (bundle != null) {
				// ---retrieve the SMS message received---
				Object[] pdus = (Object[]) bundle.get("pdus");
				msgs = new SmsMessage[pdus.length];
				String from = null, body = null;
				for (int i = 0; i < msgs.length; i++) {
					msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
					from = msgs[i].getOriginatingAddress();					
					body = msgs[i].getMessageBody();
					str  = MessageFormat.format(GotchaMusicPlayerConstants.TOAST_STRING_RECIEVED_REQUEST, new Object[] {from, body});
					String mySongPath = SongChooser.interpretContent(context, body);
					Log.i("SmsReceiver", "Processed song Path:" + mySongPath);
					if (mySongPath != null && !GotchaMusicPlayerConstants.GOTCHA_REQUEST_NASREQ.equals(mySongPath)) {
						SongChooser.playSong(mySongPath, context);
						body = MessageFormat.format(GotchaMusicPlayerConstants.SONG_PLAY_SUCCESS_MSG_BODY, new Object[]{mySongPath});
					} else if (!GotchaMusicPlayerConstants.GOTCHA_REQUEST_NASREQ.equals(mySongPath)) {
						body = GotchaMusicPlayerConstants.SONG_PLAY_FAILED_MSG_BODY;
					} else {
						body = null;
					}
				}
				if (from != null && body != null) {
					try {
						String isReplyEnabled = dao
								.getConfigurationValue(CONFIG_VALUE_FROM_DATABASE.IS_REPLY_ENABLED);
						if (GotchaMusicPlayerConstants.CONSTANT_VALUE_YES.equalsIgnoreCase(isReplyEnabled)) {
							SongChooser.sendSMS(context, from, MessageFormat.format(GotchaMusicPlayerConstants.SONG_PLAY_MSG_ENCLOSING_BODY, new Object[]{body}));
							Toast.makeText(context, str, Toast.LENGTH_SHORT)
									.show();
						}

					} catch (Exception ex) {
						ex.printStackTrace();
						Toast.makeText(context,
								"Cannot Reply Back  " + ex.toString(),
								Toast.LENGTH_SHORT).show();
					}
				}

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

}