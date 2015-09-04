package app.android.gotcha.music.player;

import java.io.File;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import app.android.gotcha.music.player.constants.GotchaMusicPlayerConstants;
import app.android.gotcha.music.player.constants.GotchaMusicPlayerConstants.CONFIG_VALUE_FROM_DATABASE;
import app.android.gotcha.music.player.dao.GotchaMusicPlayerDAO;
import app.android.gotcha.music.player.util.SongChooser;

public class GotchaMusicPlayerActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gotcha_music_player);
		final GotchaMusicPlayerDAO dao = new GotchaMusicPlayerDAO(
				getApplicationContext());
		Map<CONFIG_VALUE_FROM_DATABASE, String> configMap = dao.getAllconfigurationValues();
		if(!configMap.isEmpty()) {
			((EditText) findViewById(R.id.editText1)).setText(configMap.get(CONFIG_VALUE_FROM_DATABASE.DEFAULT_PATH));
			((CheckBox) findViewById(R.id.checkBox1)).setChecked(GotchaMusicPlayerConstants.CONSTANT_VALUE_YES.equals(configMap.get(CONFIG_VALUE_FROM_DATABASE.IS_REPLY_ENABLED)));
		}
		Button activateButton = (Button) findViewById(R.id.activateButton);
		activateButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String defaultPath = ((EditText) findViewById(R.id.editText1))
						.getText().toString();
				
				if (defaultPath.trim().length() > 0 ) {
					if(SongChooser.getSongsCount(new File(defaultPath), GotchaMusicPlayerConstants.DEFAULT_MAX_DIRECTORY_LEVEL) > GotchaMusicPlayerConstants.DEFAULT_MIN_SONGS_REQUIRED) { 
						dao.setConfigurationValue(
								CONFIG_VALUE_FROM_DATABASE.DEFAULT_PATH,
								defaultPath);
						String isReplyEnabled = ((CheckBox) findViewById(R.id.checkBox1))
								.isChecked() ? GotchaMusicPlayerConstants.CONSTANT_VALUE_YES: GotchaMusicPlayerConstants.CONSTANT_VALUE_NO;
						dao.setConfigurationValue(
								CONFIG_VALUE_FROM_DATABASE.IS_REPLY_ENABLED,
								isReplyEnabled);
					} else {
						Toast.makeText(getBaseContext(), "No songs in the directory specified!!!",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(getBaseContext(), "Fields Empty!!!",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.gotcha_music_player, menu);
		return true;
	}

}
