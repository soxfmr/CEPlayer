package com.soxfmr.ecplayer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ecge.Director;
import com.ecge.scenario.ScenarioLoader;
import com.ecge.scenario.SceneInfo;


public class MainActivity extends Activity implements View.OnTouchListener, Director.ScenarioExecuter {	
	private RelativeLayout mainView;
	private ImageView imgView;
	
	private SoundPool mSoundPool = null;
	private Map<String, Integer> mSoundMap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mainView = (RelativeLayout) findViewById(R.id.mainview);
        imgView = (ImageView) findViewById(R.id.imageView1);
        mainView.setOnTouchListener(this);
        
        AssetManager assetMgr = getResources().getAssets();
        
        mSoundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        mSoundMap = new HashMap<String, Integer>();
        try {
			mSoundMap.put("se001.wav", mSoundPool.load(assetMgr.openFd("se001.wav"), 1));
			mSoundMap.put("se002.wav", mSoundPool.load(assetMgr.openFd("se002.wav"), 1));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        ScenarioLoader sceLoader = new ScenarioLoader(this);
        sceLoader.Load();
        
        Director.SceLoader = sceLoader;
        Director.SceExecuter = this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN) {
			if(!Director.SceLoader.ToTheEnd())
				Director.Next();
		}
		return false;
	}

	@Override
	public void Execute(SceneInfo si) {
		boolean jumpable = true;
		try{
			switch(si.Type) {
			case SceneInfo.TYPE_TEXT:
				break;
			case SceneInfo.TYPE_SOUND:
				mSoundPool.play(mSoundMap.get((String)si.Action), 1, 1, 0, 0, 1);
				break;
			case SceneInfo.TYPE_BACKGROUND_MUSIC:
				
				break;
			case SceneInfo.TYPE_BACKGROUND_IMAGE:
				imgView.setImageBitmap(BitmapFactory.decodeStream(getResources().getAssets().open((String) si.Action)));
				jumpable = false;
				break;
			default:;
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		if(jumpable) Director.Next();
	}
}
