package com.example.theguardian;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * The Fragment that will display the full article with its associated image and full contents. On phones this fragment will
 * be loaded in its own separate activity and on the tablet it will be loaded within the main Guardian page next to the listview.
 * @author Emrah Kinay
 * @version 1.0
 */
public class TheGuardianFragment extends Fragment {

    /**
     * The parent activity that the fragment is attached to.
     */
    private AppCompatActivity parentActivity;
    /**
     * Whether the article is a favourite or not.
     */
    private boolean isFavorite = false;
    /**
     * The database id of the article.
     */
    private long articleId;
    /**
     * The articles image taken from the url.
     */
    private Bitmap bitmap;
    /**
     * The Android node that will hold the article image.
     */
    private ImageView articleImage;
    /**
     * File that holds the saved search text typed by the user.
     */
    private SharedPreferences prefs;

    /**
     * Default no-arg constructor.
     */
    public TheGuardianFragment() {

    }

    /**
     * Creates the fragment by inflating the view which will hold the contents of the article.
     * @param inflater Inflates the view that will be used in the fragment.
     * @param container Parent view used by the fragment.
     * @param savedInstanceState Any previously saved data used by the fragment.
     * @return The view which displays the contents of a Guardian article.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment, container, false);
        Bundle bundle = getArguments();

        TextView articleTitle = fragmentView.findViewById(R.id.fragmentArticleTitle);
        articleTitle.setText(bundle.getString(TheGuardianMain.ARTICLE_TITLE));

        TextView articleBodyText = fragmentView.findViewById(R.id.fragmentArticleBodyText);
        articleBodyText.setText(bundle.getString(TheGuardianMain.ARTICLE_BODY_TEXT));
        articleBodyText.setMovementMethod(new ScrollingMovementMethod());

        TextView articleDate = fragmentView.findViewById(R.id.fragmentArticleDate);
        articleDate.setText(bundle.getString(TheGuardianMain.ARTICLE_DATE));

        TextView articleUrl = fragmentView.findViewById(R.id.fragmentArticleUrl);
        articleUrl.setText(bundle.getString(TheGuardianMain.ARTICLE_URL));
        articleUrl.setMovementMethod(LinkMovementMethod.getInstance());

        TextView articleSection = fragmentView.findViewById(R.id.fragmentArticleSection);
        articleSection.setText(bundle.getString(TheGuardianMain.ARTICLE_SECTION));


        ImageButton favImgBtn = fragmentView.findViewById(R.id.fragmentImageButton);

        TheGuardianDbOpener opener = new TheGuardianDbOpener(parentActivity);
        SQLiteDatabase db = opener.getWritableDatabase();

        isFavorite = isAlreadyFavorite(bundle);
        if(isFavorite){
            favImgBtn.setImageResource(R.drawable.favorite_yellow_medium);
        }
        favImgBtn.setOnClickListener((c) -> {
            if(isFavorite){
                favImgBtn.setImageResource(R.drawable.favorite_blue_medium);
                isFavorite = false;
                db.delete(TheGuardianDbOpener.TABLE_NAME, TheGuardianDbOpener.COL_ID + "= ?", new String[] {String.valueOf(articleId)});
            }
            else{
                favImgBtn.setImageResource(R.drawable.favorite_yellow_medium);
                isFavorite = true;
                articleId = addToDatabase(
                        bundle.getString(TheGuardianMain.ARTICLE_TITLE),
                        bundle.getString(TheGuardianMain.ARTICLE_URL),
                        bundle.getString(TheGuardianMain.ARTICLE_SECTION),
                        bundle.getString(TheGuardianMain.ARTICLE_BODY_TEXT),
                        bundle.getString(TheGuardianMain.ARTICLE_IMAGE_URL),
                        bundle.getString(TheGuardianMain.ARTICLE_DATE)
                );
                LayoutInflater toastInflater = getLayoutInflater();
                View layout = toastInflater.inflate(R.layout.toast, container.findViewById(R.id.toast), false);
                ImageView toastImage = layout.findViewById(R.id.favorite_toast);
                animateStar(toastImage);
                Toast toast = new Toast(parentActivity);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setView(layout);
                toast.show();
            }
        });

        articleImage = fragmentView.findViewById(R.id.articleImage);
        new ImagePull().execute(bundle.getString(TheGuardianMain.ARTICLE_IMAGE_URL), bundle.getString(TheGuardianMain.ARTICLE_TITLE).replace(" ",""));
        return fragmentView;
    }

    public void animateStar(final ImageView view) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        prepareAnimation(scaleAnimation);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        prepareAnimation(alphaAnimation);

        AnimationSet animation = new AnimationSet(true);
        animation.addAnimation(alphaAnimation);
        animation.addAnimation(scaleAnimation);
        animation.setDuration(700);
        animation.setFillAfter(true);

        view.startAnimation(animation);

    }

    private Animation prepareAnimation(Animation animation){
        animation.setRepeatCount(1);
        animation.setRepeatMode(Animation.REVERSE);
        return animation;
    }
    /**
     * Adds an article to the database when it is favourited by the user.
     * @param strings The array of strings holding all the relevant article information that will be stored in the columns of the database.
     * @return
     */
    private long addToDatabase(String... strings){
        TheGuardianDbOpener opener = new TheGuardianDbOpener(parentActivity);
        SQLiteDatabase db = opener.getWritableDatabase();
        ContentValues articleValues = new ContentValues();
        articleValues.put(TheGuardianDbOpener.COL_TITLE, strings[0]);
        articleValues.put(TheGuardianDbOpener.COL_URL, strings[1]);
        articleValues.put(TheGuardianDbOpener.COL_SECTION_NAME, strings[2]);
        articleValues.put(TheGuardianDbOpener.COL_BODY_TEXT, strings[3]);
        articleValues.put(TheGuardianDbOpener.COL_IMAGE_URL, strings[4]);
        articleValues.put(TheGuardianDbOpener.COL_DATE, strings[5]);
        long id = db.insert(TheGuardianDbOpener.TABLE_NAME, null, articleValues);
        return id;
    }

    /**
     * Determines if the article is already among the favourites list by checking its title with those already in the database.
     * @param bundle Holds information about the article that needs to be checked.
     * @return True if the article is already a favourite, and false if it is not.
     */
    private boolean isAlreadyFavorite(Bundle bundle){
        TheGuardianDbOpener opener = new TheGuardianDbOpener(parentActivity);
        SQLiteDatabase db = opener.getWritableDatabase();
        String [] columns = {TheGuardianDbOpener.COL_ID, TheGuardianDbOpener.COL_TITLE};
        Cursor results = db.query(false, TheGuardianDbOpener.TABLE_NAME, columns, null, null, null, null, null, null);

        int articleTitleColIndex = results.getColumnIndex(TheGuardianDbOpener.COL_TITLE);
        int idColIndex = results.getColumnIndex(TheGuardianDbOpener.COL_ID);

        while(results.moveToNext())
        {
            String articleTitle = results.getString(articleTitleColIndex);
            long id = results.getLong(idColIndex);
            if(articleTitle.equals(bundle.getString(TheGuardianMain.ARTICLE_TITLE))){
                articleId = id;
                return true;
            }
        }
        return false;
    }

    /**
     * Attaches the fragment to its context (parent activity).
     * @param context The context (parent activity).
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parentActivity = (AppCompatActivity)context;
    }

    /**
     * AsyncTask class that is responsible for scraping and downloading the article image from the URL given in doInBackground.
     */
    private class ImagePull extends AsyncTask<String, Integer, String> {

        /**
         * Downloads the article image from the given URL.
         * @param strings String array holding the URL containing the article image.
         * @return null String that signalizes that the method has finished its task.
         */
        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... strings) {
            if(strings[0]!= null && !strings[0].isEmpty()){
                try {
                    File file = parentActivity.getBaseContext().getFileStreamPath(strings[1]);
                    if(file!=null && file.exists()){
                        FileInputStream fis = parentActivity.openFileInput(strings[1]);
                        bitmap = BitmapFactory.decodeStream(fis);
                    } else{
                        URL articleImageUrl = new URL(strings[0]);
                        HttpURLConnection connection = (HttpURLConnection) articleImageUrl.openConnection();
                        InputStream inStream = connection.getInputStream();
                        connection.connect();
                        int responseCode = connection.getResponseCode();
                        if (responseCode == 200) {
                            bitmap = BitmapFactory.decodeStream(connection.getInputStream());
                        }
                        FileOutputStream outputStream = parentActivity.openFileOutput( strings[1], Context.MODE_PRIVATE);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                        outputStream.flush();
                        outputStream.close();
                        inStream.close();
                        connection.disconnect();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        /**
         * Sets the ImageView of the fragment to the image downloaded from doInBackground.
         * @param s null String returned by doInBackground when it is done.
         */
        @Override
        protected void onPostExecute(String s) {
            articleImage.setImageBitmap(bitmap);
        }

    }
}
