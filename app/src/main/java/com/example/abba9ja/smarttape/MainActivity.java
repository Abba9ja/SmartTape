package com.example.abba9ja.smarttape;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;


public class MainActivity extends AppCompatActivity  implements SensorEventListener {
    private CameraPreview mPreview;
    private SensorManager sensorManager = null;
    private Camera mCamera;
    private int orientation;
    private ExifInterface exif;
    private int deviceHeight, deviceWidth;
    private String fileName;
    private int degrees = -1;

    public final double CONSTANT_VALUE = 0.1;
    private Button settingsBtn;

    private float[] gravity = new float[3];
    private float[] lineat_aceleretion = new float[3];
    private float cmToInch;

    public float mm, mmCount, mmToCm;

    public float getMm() {
        return mm;
    }

    public void setMm(float mm) {
        this.mm = mm;
    }

    public float getMmCount() {
        return mmCount;
    }

    public void setMmCount(float mmCount) {
        this.mmCount += mmCount;
    }

    public float getMmToCm() {
        return mmToCm;
    }

    public void setMmToCm(float mmToCm) {
        this.mmToCm += mmToCm;
    }

    public void resetMmToCm() {
        this.mmToCm = 0;
    }

    public void setCmCount(float cmCount) {
        this.cmCount += cmCount;
    }

    public float getCmCount() {
        return cmCount;
    }

    public void resetCmtoInch(){
        this.cmToInch = 0;
    }

    public float cmCount;

    public void setCmToInch(float cmToInch) {
        this.cmToInch += cmToInch;
    }

    public float getCmToInch() {
        return cmToInch;
    }


    public void setInchCounth(float inchCounth) {
        this.inchCounth += inchCounth;
    }

    public float getInchCounth() {
        return inchCounth;
    }

    public float inchCounth;

    public float getInchToft() {
        return inchToft;
    }

    public void setInchToft(float inchToft) {
        this.inchToft += inchToft;
    }

    public void resetInchToft(){
        this.inchToft = 0;
    }

    public float inchToft;

    public float getFtCount() {
        return ftCount;
    }

    public void setFtCount(float ftCount) {
        this.ftCount += ftCount;
    }

    public float ftCount;

    public void setMCount(float mCount) {
        this.mCount += mCount;
    }

    public float getMCount() {
        return mCount;
    }

    public float mCount;

    public float getCmTom() {
        return cmTom;
    }

    public void setCmTom(float cmTom) {
        this.cmTom = cmTom;
    }

    public void resetcmTom(){
        this.cmTom = 0;
    }

    public float cmTom;



    @Override
    public void finishAfterTransition() {
        super.finishAfterTransition();
    }

    public String xstring, ystring;

    public TextView tvmm, tvcm, tvft, tvinch, tvm, f1red, f1green, f2red, f2green, tvbtnUpdate;
    public Button btnstart, btnPause, btnReset, btnScreenShot, btnStop;
    public ImageView leftDown, rightUp;

    public boolean START = false;
    public boolean STOP= false;
    public boolean RESET= false;
    public boolean PAUSE= false;

    Toast tmsg;
    String stringContent;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settingsBtn = (Button) findViewById(R.id.btnsettings);
        btnstart = (Button) findViewById(R.id.btnstart);
        btnPause = (Button) findViewById(R.id.btnPause);
        btnReset = (Button) findViewById(R.id.btnReset);
        btnScreenShot = (Button) findViewById(R.id.btnScreenShot);
        btnStop = (Button) findViewById(R.id.btnStop);

        tvmm = (TextView) findViewById(R.id.tvmm);
        tvcm = (TextView) findViewById(R.id.tvcm);
        tvinch = (TextView) findViewById(R.id.tvinch);
        tvft = (TextView) findViewById(R.id.tvft);
        tvm = (TextView) findViewById(R.id.tvm);
        f1red = (TextView) findViewById(R.id.f1red);
        f1green = (TextView) findViewById(R.id.f1green);
        f2red = (TextView) findViewById(R.id.f2red);
        f2green = (TextView) findViewById(R.id.f2green);
        tvbtnUpdate = (TextView) findViewById(R.id.tvbtnUpdate);

        leftDown = (ImageView) findViewById(R.id.leftDown);
        rightUp = (ImageView) findViewById(R.id.rightUp);


        // Getting the sensor service.
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null){
            Toast.makeText(this, "TYPE_ACCELEROMETER DEY OO", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this, "TYPE_ACCELEROMETER NO DEY OO", Toast.LENGTH_LONG).show();
            // Sorry, there are no accelerometers on your device.
            // You can't play this game.
        }

        // Selecting the resolution of the Android device so we can create a
        // proportional preview
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        deviceHeight = display.getHeight();
        deviceWidth = display.getWidth();

        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = MainActivity.this;
                Class destinationClass = Settings.class;
                Intent intenttoSettings = new Intent(context, destinationClass);
                startActivity(intenttoSettings);
            }
        });


        btnstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (STOP){
                    resetAll();
                }
                START = true;
                PAUSE = false;
                STOP = false;
                RESET= false;
                tvbtnUpdate.setText("Staring measurement");
                buttonChecker("start");
            }
        });

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PAUSE = true;
                START = false;
                STOP = false;
                RESET = false;
                tvbtnUpdate.setText("Measuring paused");
                buttonChecker("pause");
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RESET = true;
                START = false;
                PAUSE = false;
                STOP = false;
                resetAll();
                tvbtnUpdate.setText("Reset, click start to begin");
                buttonChecker("reset");
            }
        });

        btnScreenShot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeScreenshot();
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                STOP = true;
                RESET = true;
                START = false;
                PAUSE = false;
                tvbtnUpdate.setText("Stoped, click start to begin");
                buttonChecker("stop");
            }
        });


    }

    private void takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/PICTURES/Screenshots/" + now + ".jpg";

            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);
            File imageFile = new File(mPath);
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();


            MediaScannerConnection.scanFile(this,
                    new String[]{imageFile.toString()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(String path, Uri uri) {
                            Log.i("ExternalStorage", "Scanned " + path + ":");
                            Log.i("ExternalStorage", "-> uri=" + uri);
                        }
                    });

            openScreenshot(imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or OOM
            e.printStackTrace();
        }
    }

    private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }


    private void resetAll() {
        float resetval = 0;
        String resetvalString = String.format(" %.1f", resetval);
        tvcm.setText(resetvalString);
        tvinch.setText(resetvalString);
        tvft.setText(resetvalString);
        tvm.setText(resetvalString);
        setMmCount(0);
        setMCount(0);
        setFtCount(0);
        setInchCounth(0);
        setCmCount(0);
        resetMmToCm();
        resetcmTom();
        resetInchToft();
        resetcmTom();
        resetCmtoInch();
    }


    protected Toast buttonChecker(String checker){
        if(checker == "start"){
            tmsg = Toast.makeText(this, "Starting Measuring", Toast.LENGTH_SHORT);
            tmsg.show();
        }else if(checker == "stop"){
            tmsg = Toast.makeText(this, "Measuring Stop", Toast.LENGTH_SHORT);
            tmsg.show();
        }else if(checker == "reset"){
            tmsg = Toast.makeText(this, "Measuring Reset", Toast.LENGTH_SHORT);
            tmsg.show();
        }else if(checker == "screenshot"){
            tmsg = Toast.makeText(this, "Screen Shot saved", Toast.LENGTH_SHORT);
            tmsg.show();
        }else if(checker == "pause"){
            tmsg = Toast.makeText(this, "Measuring pause", Toast.LENGTH_SHORT);
            tmsg.show();
        }
        return tmsg;

    }

    private void createCamera() {
        // Create an instance of Camera
        mCamera = getCameraInstance();

        // Setting the right parameters in the camera
        Camera.Parameters params = mCamera.getParameters();
        params.setPictureSize(1600, 1200);
        params.setPictureFormat(PixelFormat.JPEG);
        params.setJpegQuality(85);
        mCamera.setParameters(params);
        mCamera.setDisplayOrientation(90);

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);


        // Calculating the width of the preview so it is proportional.
        float widthcal = (float) (deviceWidth);
        int width = Math.round(widthcal);

        // Resizing the LinearLayout so we can make a proportional preview. This
        // approach is not 100% perfect because on devices with a really small
        // screen the the image will still be distorted - there is place for
        // improvment.
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(deviceWidth,deviceHeight);
        preview.setLayoutParams(layoutParams);
        // Adding the camera preview after the FrameLayout and before the button
        // as a separated element.
        preview.addView(mPreview, 0);
    }
    @Override
    protected void onResume() {
        super.onResume();

        if (!checkCameraHardware(this)) {
            Toast.makeText(this, "No CAMERA 2", Toast.LENGTH_LONG).show();
        } else if (!checkSDCard()) {
            Toast.makeText(this, "No sdcard 2", Toast.LENGTH_LONG).show();
        }

        // Creating the camera
        createCamera();

        // Register this class as a listener for the accelerometer sensor
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // release the camera immediately on pause event
        releaseCamera();

        // removing the inserted view - so when we come back to the app we
        // won't have the views on top of each other.
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.removeViewAt(0);
    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.release(); // release the camera for other applications
            mCamera = null;
        }
    }

    /** Check if this device has a camera */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    private boolean checkSDCard() {
        boolean state = false;

        String sd = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(sd)) {
            state = true;
        }

        return state;
    }

    /**
     * A safe way to get an instance of the Camera object.
     */
    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            // attempt to get a Camera instance
            c = Camera.open();
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }

        // returns null if camera is unavailable
        return c;
    }

    /**
     * Putting in place a listener so we can get the sensor data only when
     * something changes.
     */
    public void onSensorChanged(SensorEvent event) {
        synchronized (this) {

            final float alpha = 0.8f;

            gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
            gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
            gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];


            lineat_aceleretion[0] = event.values[0] - gravity[0];
            lineat_aceleretion[1] = event.values[1] - gravity[1];
            lineat_aceleretion[2] = event.values[1] - gravity[2];

            float xaxis = event.values[0];
            float yaxis = event.values[1];
            float zaxis = event.values[2];


            if (zaxis >= 7 || zaxis >= 3 || zaxis >= -2.5) {
                f2green.setVisibility(View.VISIBLE);
                f2red.setVisibility(View.INVISIBLE);
            } else {
                f2green.setVisibility(View.INVISIBLE);
                f2red.setVisibility(View.VISIBLE);
            }

            if (yaxis >= 3 || yaxis >= 7 ||  xaxis >= 7.5) {
                f1red.setVisibility(View.INVISIBLE);
                f1green.setVisibility(View.VISIBLE);
            } else {
                f1red.setVisibility(View.VISIBLE);
                f1green.setVisibility(View.INVISIBLE);
            }


            if (START) {
                if (zaxis >= 7 && yaxis >= 3) {
                    if (xaxis > 1.2) {
                        leftordown();
                        setMmCount((float) 0.01);
                        setMmToCm((float) 0.01);
                        String mm = String.format(" %.1f", getMmCount());
                        tvmm.setText(mm);

                        if (getMmToCm() >= 0.2){
                            resetMmToCm();
                            setCmCount((float) CONSTANT_VALUE);
                            setCmToInch((float) CONSTANT_VALUE);
                            String cm = String.format(" %.1f", getCmCount());
                            tvcm.setText(cm);
                            if (getCmToInch() >= 1.2) {
                                resetCmtoInch();
                                setInchCounth((float) 0.5);
                                setInchToft((float) 0.5);
                                String inch = String.format(" %.1f", getInchCounth());
                                tvinch.setText(inch);
                            }
                            if (getInchToft() >= 3.2) {
                                resetInchToft();
                                setFtCount((float) 0.5);
                                setCmTom((float) 0.5);
                                String ft = String.format(" %.1f", getFtCount());
                                tvft.setText(ft);

                                if (getCmTom() >= 0.5) {
                                    resetcmTom();
                                    setMCount((float) 0.2);
                                    String m = String.format(" %.1f", getMCount());
                                    tvm.setText(m);
                                }

                            }

                        }


                    }
                }


                if (zaxis >= 7 && yaxis >= 3) {
                    if (xaxis <= -1.5) {
                        rightorUp();
                        setMmCount((float) 0.01);
                        setMmToCm((float) 0.01);
                        String mm = String.format(" %.1f", getMmCount());
                        tvmm.setText(mm);

                        if (getMmToCm() >= 0.4){
                            resetMmToCm();
                            setCmCount((float) CONSTANT_VALUE);
                            setCmToInch((float) CONSTANT_VALUE);
                            String cm = String.format(" %.1f", getCmCount());
                            tvcm.setText(cm);
                            if (getCmToInch() >= 1.2) {
                                resetCmtoInch();
                                setInchCounth((float) 0.5);
                                setInchToft((float) 0.5);
                                String inch = String.format(" %.1f", getInchCounth());
                                tvinch.setText(inch);
                            }
                            if (getInchToft() >= 3.2) {
                                resetInchToft();
                                setFtCount((float) 0.5);
                                setCmTom((float) 0.5);
                                String ft = String.format(" %.1f", getFtCount());
                                tvft.setText(ft);

                                if (getCmTom() >= 0.5) {
                                    resetcmTom();
                                    setMCount((float) 0.2);
                                    String m = String.format(" %.1f", getMCount());
                                    tvm.setText(m);
                                }

                            }

                        }

                    }
                }


                if (zaxis >= 3 && yaxis >= 7) {
                    if (xaxis >= 1) {
                        leftordown();
                        setMmCount((float) 0.01);
                        setMmToCm((float) 0.01);
                        String mm = String.format(" %.1f", getMmCount());
                        tvmm.setText(mm);

                        if (getMmToCm() >= 0.4){
                            resetMmToCm();
                            setCmCount((float) CONSTANT_VALUE);
                            setCmToInch((float) CONSTANT_VALUE);
                            String cm = String.format(" %.1f", getCmCount());
                            tvcm.setText(cm);
                            if (getCmToInch() >= 1.2) {
                                resetCmtoInch();
                                setInchCounth((float) 0.5);
                                setInchToft((float) 0.5);
                                String inch = String.format(" %.1f", getInchCounth());
                                tvinch.setText(inch);
                            }
                            if (getInchToft() >= 3.2) {
                                resetInchToft();
                                setFtCount((float) 0.5);
                                setCmTom((float) 0.5);
                                String ft = String.format(" %.1f", getFtCount());
                                tvft.setText(ft);

                                if (getCmTom() >= 0.5) {
                                    resetcmTom();
                                    setMCount((float) 0.2);
                                    String m = String.format(" %.1f", getMCount());
                                    tvm.setText(m);
                                }

                            }

                        }

                    }
                }


                if (zaxis >= 3 && yaxis >= 7) {
                    if (xaxis <= -1) {
                        if (cmCount > 0) {
                            rightorUp();
                            setMmCount((float) 0.01);
                            setMmToCm((float) 0.01);
                            String mm = String.format(" %.1f", getMmCount());
                            tvmm.setText(mm);

                            if (getMmToCm() >= 0.4){
                                resetMmToCm();
                                setCmCount((float) CONSTANT_VALUE);
                                setCmToInch((float) CONSTANT_VALUE);
                                String cm = String.format(" %.1f", getCmCount());
                                tvcm.setText(cm);
                                if (getCmToInch() >= 1.2) {
                                    resetCmtoInch();
                                    setInchCounth((float) 0.5);
                                    setInchToft((float) 0.5);
                                    String inch = String.format(" %.1f", getInchCounth());
                                    tvinch.setText(inch);
                                }
                                if (getInchToft() >= 3.2) {
                                    resetInchToft();
                                    setFtCount((float) 0.5);
                                    setCmTom((float) 0.5);
                                    String ft = String.format(" %.1f", getFtCount());
                                    tvft.setText(ft);

                                    if (getCmTom() >= 0.5) {
                                        resetcmTom();
                                        setMCount((float) 0.2);
                                        String m = String.format(" %.1f", getMCount());
                                        tvm.setText(m);
                                    }

                                }

                            }

                        }
                    }

                }



                //on landscape

                if(zaxis >= -2.5 && xaxis >= 7.5){
                    if (yaxis <= -1.7){
                        leftordown();
                        setMmCount((float) 0.01);
                        setMmToCm((float) 0.01);
                        String mm = String.format(" %.1f", getMmCount());
                        tvmm.setText(mm);

                        if (getMmToCm() >= 0.4){
                            resetMmToCm();
                            setCmCount((float) CONSTANT_VALUE);
                            setCmToInch((float) CONSTANT_VALUE);
                            String cm = String.format(" %.1f", getCmCount());
                            tvcm.setText(cm);
                            if (getCmToInch() >= 1.2) {
                                resetCmtoInch();
                                setInchCounth((float) 0.5);
                                setInchToft((float) 0.5);
                                String inch = String.format(" %.1f", getInchCounth());
                                tvinch.setText(inch);
                            }
                            if (getInchToft() >= 3.2) {
                                resetInchToft();
                                setFtCount((float) 0.5);
                                setCmTom((float) 0.5);
                                String ft = String.format(" %.1f", getFtCount());
                                tvft.setText(ft);

                                if (getCmTom() >= 0.5) {
                                    resetcmTom();
                                    setMCount((float) 0.2);
                                    String m = String.format(" %.1f", getMCount());
                                    tvm.setText(m);
                                }

                            }

                        }

                    }
                }

            }



        }
    }


    public void leftordown(){
        rightUp.setVisibility(View.INVISIBLE);
        leftDown.setVisibility(View.VISIBLE);
    }

    public void rightorUp(){
        rightUp.setVisibility(View.VISIBLE);
        leftDown.setVisibility(View.INVISIBLE);
    }


    /**
     * STUFF THAT WE DON'T NEED BUT MUST BE HEAR FOR THE COMPILER TO BE HAPPY.
     */
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}