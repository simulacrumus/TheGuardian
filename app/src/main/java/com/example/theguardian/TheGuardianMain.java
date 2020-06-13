package com.example.theguardian;

        import android.annotation.SuppressLint;
        import android.content.ContentValues;
        import android.content.Intent;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.text.SpannableString;
        import android.text.style.TextAppearanceSpan;
        import android.view.Gravity;
        import android.view.LayoutInflater;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.view.ViewGroup;
        import android.view.animation.AlphaAnimation;
        import android.view.animation.Animation;
        import android.view.animation.AnimationSet;
        import android.view.animation.ScaleAnimation;
        import android.widget.BaseAdapter;
        import android.widget.ImageView;
        import android.widget.ListView;
        import android.widget.SearchView;
        import android.widget.TextView;
        import android.widget.Toast;
        import androidx.annotation.NonNull;
        import androidx.appcompat.app.ActionBarDrawerToggle;
        import androidx.appcompat.app.AlertDialog;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.appcompat.view.ContextThemeWrapper;
        import androidx.appcompat.widget.Toolbar;
        import androidx.core.view.GravityCompat;
        import androidx.drawerlayout.widget.DrawerLayout;
        import com.google.android.material.navigation.NavigationView;
        import com.google.android.material.snackbar.Snackbar;
        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;
        import java.util.ArrayList;

/**
 *
 * @author Emrah Kinay
 * @version 1.0.0
 */
public class TheGuardianMain extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    //URLs
    private static final String BASE_URL_PART_1 = "https://content.guardianapis.com/search?=";
    private static final String BASE_URL_PART_2 = "&show-blocks=all&api-key=test";
    private static final String URL_SECTION_BUSINESS = "https://content.guardianapis.com/search?section=business&show-blocks=all&api-key=test";
    private static final String URL_SECTION_SPORT = "https://content.guardianapis.com/search?section=sport&show-blocks=all&api-key=test";
    private static final String URL_SECTION_POLITICS = "https://content.guardianapis.com/search?section=politics&show-blocks=all&api-key=test";
    private static final String URL_SECTION_OPINION = "https://content.guardianapis.com/search?section=commentisfree&show-blocks=all&api-key=test";
    private static final String URL_SECTION_TECHNOLOGY = "https://content.guardianapis.com/search?section=technology&show-blocks=all&api-key=test";
    private static final String URL_SECTION_WEATHER = "https://content.guardianapis.com/search?section=weather&show-blocks=all&api-key=test";
    private static final String URL_SECTION_UK_NEWS = "https://content.guardianapis.com/search?section=uk-news&show-blocks=all&api-key=test";
    private static final String URL_SECTION_SCIENCE = "https://content.guardianapis.com/search?section=science&show-blocks=all&api-key=test";
    private static final String URL_SECTION_US_NEWS = "https://content.guardianapis.com/search?section=us-news&show-blocks=all&api-key=test";
    private static final String URL_SECTION_SOCIETY = "https://content.guardianapis.com/search?section=society&show-blocks=all&api-key=test";
    private static final String URL_SECTION_TRAVEL = "https://content.guardianapis.com/search?section=travel&show-blocks=all&api-key=test";
    private static final String URL_SECTION_WORLD = "https://content.guardianapis.com/search?section=world&show-blocks=all&api-key=test";

    //
    static final String ARTICLE_TITLE = "articleTitle";
    static final String ARTICLE_BODY_TEXT = "articleBodyText";
    static final String ARTICLE_DATE = "articleDate";
    static final String ARTICLE_SECTION = "articleSection";
    static final String ARTICLE_IMAGE_URL = "articleImageUrl";
    static final String ARTICLE_URL= "articleUrl";
    private static final String JSON_KEY_RESPONSE = "response";
    private static final String JSON_KEY_RESULTS = "results";
    private static final String JSON_KEY_MAIN = "main";
    private static final String JSON_KEY_ASSETS = "assets";
    private static final String JSON_KEY_BODY_TEXT_SUMMARY = "bodyTextSummary";
    private static final String JSON_KEY_BLOCKS = "blocks";
    private static final String JSON_KEY_ELEMENTS = "elements";
    private static final String JSON_KEY_BODY = "body";
    private static final String JSON_KEY_WEB_URL = "webUrl";
    private static final String JSON_KEY_FILE = "file";
    private static final String JSON_KEY_PUBLISH_DATE = "publishedDate";
    private static final String JSON_KEY_WEB_TITLE = "webTitle";
    private static final String JSON_KEY_SECTION_NAME = "sectionName";

    /**
     * Database object
     */
    private SQLiteDatabase db;

    /**
     * ListView that shows articles as a list
     */
    private ListView listView;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TheGuardianDbOpener opener = new TheGuardianDbOpener(this);
        db = opener.getWritableDatabase();

        Toolbar tBar = findViewById(R.id.toolbar);
        setSupportActionBar(tBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, tBar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setItemIconTintList(null);
        Menu menu = navigationView.getMenu();
        MenuItem news = menu.findItem(R.id.news);
        SpannableString sn = new SpannableString(news.getTitle());
        sn.setSpan(new TextAppearanceSpan(this, R.style.MenuItemAppearance), 0, sn.length(), 0);
        news.setTitle(sn);
        MenuItem others = menu.findItem(R.id.others);
        SpannableString so = new SpannableString(others.getTitle());
        so.setSpan(new TextAppearanceSpan(this, R.style.MenuItemAppearance), 0, so.length(), 0);
        others.setTitle(so);
        navigationView.setNavigationItemSelectedListener(menuItem ->{
            switch (menuItem.getItemId()){
                case R.id.politics:
                    new ArticlePull().execute(URL_SECTION_POLITICS);
                    break;
                case R.id.business:
                    new ArticlePull().execute(URL_SECTION_BUSINESS);
                    break;
                case R.id.sport:
                    new ArticlePull().execute(URL_SECTION_SPORT);
                    break;
                case R.id.opinion:
                    new ArticlePull().execute(URL_SECTION_OPINION);
                    break;
                case R.id.science:
                    new ArticlePull().execute(URL_SECTION_SCIENCE);
                    break;
                case R.id.technology:
                    new ArticlePull().execute(URL_SECTION_TECHNOLOGY);
                    break;
                case R.id.weather:
                    new ArticlePull().execute(URL_SECTION_WEATHER);
                    break;
                case R.id.world:
                    new ArticlePull().execute(URL_SECTION_WORLD);
                    break;
                case R.id.travel:
                    new ArticlePull().execute(URL_SECTION_TRAVEL);
                    break;
                case R.id.society:
                    new ArticlePull().execute(URL_SECTION_SOCIETY);
                    break;
                case R.id.us:
                    new ArticlePull().execute(URL_SECTION_US_NEWS);
                    break;
                default:
                    new ArticlePull().execute(URL_SECTION_UK_NEWS);
            }
            drawer.closeDrawer(GravityCompat.START);

            return true;
        });

        new ArticlePull().execute(URL_SECTION_UK_NEWS);
    }

    /**
     *
     */
    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     *
     * @param menuItem
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    /**
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.guardianSearch);
        SearchView sView = (SearchView)searchItem.getActionView();
        sView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                sView.clearFocus();
                searchItem.collapseActionView();
                sView.setIconified(true);
                sView.onActionViewCollapsed();
                new ArticlePull().execute(BASE_URL_PART_1 + query + BASE_URL_PART_2);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //new ArticlePull().execute(BASE_URL_PART_1 + newText + BASE_URL_PART_2);
                return false;
            }
        });
        return true;
    }

    /**
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.theGuardianaFavorites:
                loadFavorites();
                break;
            case R.id.guardianSearch:

                break;
            case R.id.the_guardian_help:
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(TheGuardianMain.this, R.style.TheGuardianAlertDialog));
                builder.setTitle(R.string.help)
                        .setMessage(R.string.theGuardianHelpText)
                        .setCancelable(true)
                        .setPositiveButton(R.string.ok, (dialog, which) -> {});
                AlertDialog dialog  = builder.create();
                dialog.show();
                break;
        }
        return true;
    }

    /**
     *
     */
    public void loadFavorites(){
        TheGuardianDbOpener dbOpener = new TheGuardianDbOpener(this);
        db = dbOpener.getWritableDatabase();

        String [] columns = {
                TheGuardianDbOpener.COL_ID,
                TheGuardianDbOpener.COL_TITLE,
                TheGuardianDbOpener.COL_URL,
                TheGuardianDbOpener.COL_SECTION_NAME,
                TheGuardianDbOpener.COL_BODY_TEXT,
                TheGuardianDbOpener.COL_DATE,
                TheGuardianDbOpener.COL_IMAGE_URL
        };

        Cursor results = db.query(false, TheGuardianDbOpener.TABLE_NAME, columns, null, null, null, null, null, null);

        int titleColIndex = results.getColumnIndex(TheGuardianDbOpener.COL_TITLE);
        int urlColIndex = results.getColumnIndex(TheGuardianDbOpener.COL_URL);
        int sectionColIndex = results.getColumnIndex(TheGuardianDbOpener.COL_SECTION_NAME);
        int bodyTextColIndex = results.getColumnIndex(TheGuardianDbOpener.COL_BODY_TEXT);
        int dateColIndex = results.getColumnIndex(TheGuardianDbOpener.COL_DATE);
        int idColIndex = results.getColumnIndex(TheGuardianDbOpener.COL_ID);
        int imageUrlIndex = results.getColumnIndex(TheGuardianDbOpener.COL_IMAGE_URL);

        TheGuardianAdapter adapter = new TheGuardianAdapter();

        while(results.moveToNext())
        {
            String title = results.getString(titleColIndex);
            String url = results.getString(urlColIndex);
            String section = results.getString(sectionColIndex);
            String bodyText = results.getString(bodyTextColIndex);
            String date = results.getString(dateColIndex);
            String imageUrl =results.getString(imageUrlIndex);
            long id = results.getLong(idColIndex);
            adapter.getArticlesList().add(new TheGuardainArticle(title, url, section, bodyText, date, imageUrl, id));
        }
        results.close();
        listView = findViewById(R.id.the_guardian_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) ->{
            Bundle bundle = new Bundle();
            bundle.putString(ARTICLE_TITLE, adapter.getArticlesList().get(position).getTitle());
            bundle.putString(ARTICLE_BODY_TEXT, adapter.getArticlesList().get(position).getBodyText());
            bundle.putString(ARTICLE_DATE, adapter.getArticlesList().get(position).getDate());
            bundle.putString(ARTICLE_URL, adapter.getArticlesList().get(position).getUrl());
            bundle.putString(ARTICLE_SECTION, adapter.getArticlesList().get(position).getSectionName());
            bundle.putString(ARTICLE_IMAGE_URL, adapter.getArticlesList().get(position).getImageUrl());

            if( findViewById(R.id.the_guardian_article_frame) == null ){
                Intent goToEmptyFragment = new Intent(TheGuardianMain.this, TheGuardianEmpty.class);
                goToEmptyFragment.putExtras(bundle);
                startActivity(goToEmptyFragment);
            } else {
                TheGuardianFragment fragment = new TheGuardianFragment();
                fragment.setArguments(bundle);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.the_guardian_article_frame,fragment)
                        .commit();
            }
        });

        listView.setOnItemLongClickListener((parent,view,position,id) -> {
            String articleTitle = adapter.getArticlesList().get(position).getTitle();
            String articleUrl = adapter.getArticlesList().get(position).getUrl();
            String articleSection = adapter.getArticlesList().get(position).getSectionName();
            String articleBodyText = adapter.getArticlesList().get(position).getBodyText();
            String articleImageUrl = adapter.getArticlesList().get(position).getImageUrl();
            String articleDate = adapter.getArticlesList().get(position).getDate();
            long articleId = adapter.getArticlesList().get(position).getId();
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(TheGuardianMain.this, R.style.TheGuardianAlertDialog));
            builder.setTitle(R.string.remove_favorite)
                    .setMessage(articleTitle)
                    .setCancelable(true)
                    .setNeutralButton(R.string.cancel, (a,b)->{})
                    .setPositiveButton(R.string.yes, (dialog, which) -> {
                        deleteArticleFromDatabase(articleId);
                        adapter.getArticlesList().remove(position);
                        adapter.notifyDataSetChanged();
                        Snackbar snackbar = Snackbar
                                .make(findViewById(android.R.id.content),R.string.removed_favorites, Snackbar.LENGTH_SHORT)
                                .setAction(R.string.undo,e ->  {
                                    addToDatabase(articleTitle, articleUrl, articleSection, articleBodyText, articleImageUrl, articleDate);
                                    loadFavorites();
                                    adapter.notifyDataSetChanged();
                                });
                        snackbar.setActionTextColor(getResources().getColor(R.color.highlight));
                        View snackbarView = snackbar.getView();
                        snackbarView.setBackgroundColor(getResources().getColor(R.color.main));
                        snackbar.show();
                    })
                    .setNegativeButton(R.string.no,(dialog, which) -> {
                        adapter.notifyDataSetChanged();
                    });
            AlertDialog dialog  = builder.create();
            dialog.show();
            return true;
        });
    }

    /**
     *
     * @param title
     * @return
     */
    private boolean isAlreadyFavorite(String title){
        TheGuardianDbOpener opener = new TheGuardianDbOpener(this);
        SQLiteDatabase db = opener.getWritableDatabase();
        String [] columns = {TheGuardianDbOpener.COL_TITLE};
        Cursor results = db.query(false, TheGuardianDbOpener.TABLE_NAME, columns, null, null, null, null, null, null);
        int articleTitleColIndex = results.getColumnIndex(TheGuardianDbOpener.COL_TITLE);
        while(results.moveToNext()){
            String articleTitle = results.getString(articleTitleColIndex);
            if(articleTitle.equals(title)){
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param id
     */
    protected void deleteArticleFromDatabase(long id){
        db.delete(TheGuardianDbOpener.TABLE_NAME, TheGuardianDbOpener.COL_ID + "= ?", new String[] {Long.toString(id)});
    }

    /**
     *
     * @param title
     */
    protected void deleteArticleFromDatabase(String title){
        db.delete(TheGuardianDbOpener.TABLE_NAME, TheGuardianDbOpener.COL_TITLE + "= ?", new String[] {title});
    }

    /**
     *
     * @param strings
     * @return
     */
    private long addToDatabase(String... strings){
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
     *
     */
    public class TheGuardianAdapter extends BaseAdapter {
        /**
         *
         */
        private ArrayList<TheGuardainArticle> list = new ArrayList<>();

        /**
         *
         * @return
         */
        public ArrayList<TheGuardainArticle> getArticlesList() {
            return list;
        }

        /**
         *
         * @return
         */
        @Override
        public int getCount() {
            return list.size();
        }

        /**
         *
         * @param position
         * @return
         */
        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        /**
         *
         * @param position
         * @return
         */
        @Override
        public long getItemId(int position) {
            return ((TheGuardainArticle)getItem(position)).getId();
        }

        /**
         *
         * @param position
         * @param convertView
         * @param parent
         * @return
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();
            TheGuardainArticle article = list.get(position);
            if(isAlreadyFavorite(article.getTitle())){
                View row_view = inflater.inflate(R.layout.article_row_favorite, parent, false);
                TextView title = row_view.findViewById(R.id.guardianArticleTitle);
                TextView date = row_view.findViewById(R.id.guardianArticleDate);
                title.setText(article.getTitle());
                date.setText(article.getDate());
                return row_view;
            } else{
                View row_view = inflater.inflate(R.layout.article_row, parent, false);
                TextView title = row_view.findViewById(R.id.guardianArticleTitle);
                TextView date = row_view.findViewById(R.id.guardianArticleDate);
                title.setText(article.getTitle());
                date.setText(article.getDate());
                return row_view;
            }
        }
    }

    /**
     *
     */
    private class ArticlePull extends AsyncTask<String, Integer, String> {
        /**
         *
         */
        TheGuardianAdapter adapter = new TheGuardianAdapter();

        /**
         * Downloads the values from the given URL and stores them in the adapter object
         * @param strings
         * @return
         */
        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String...strings) {
            publishProgress(0);
            int progresBarStatus = 0;
            try {
                JSONObject jsonBase = Utility.buildJSONObject(strings[0]);
                System.out.println(jsonBase.getJSONObject(JSON_KEY_RESPONSE));
                JSONArray articles = jsonBase.getJSONObject(JSON_KEY_RESPONSE).getJSONArray(JSON_KEY_RESULTS);
                for(int i=0; i<articles.length(); i++){
                    JSONObject a = articles.getJSONObject(i);
                    JSONObject blocks = a.getJSONObject(JSON_KEY_BLOCKS);
                    JSONObject main = blocks.getJSONObject(JSON_KEY_MAIN);
                    JSONArray mainElements = main.getJSONArray(JSON_KEY_ELEMENTS);
                    JSONObject elementObject = mainElements.getJSONObject(0);
                    JSONArray assets = elementObject.getJSONArray(JSON_KEY_ASSETS);
                    JSONArray bodyArray = blocks.getJSONArray(JSON_KEY_BODY);
                    JSONObject bodyObject = bodyArray.getJSONObject(0);
                    String bodyText = bodyObject.getString(JSON_KEY_BODY_TEXT_SUMMARY);

                    String date=null;
                    if(main.getString(JSON_KEY_PUBLISH_DATE)!=null && !main.getString(JSON_KEY_PUBLISH_DATE).isEmpty())
                        date = main.getString(JSON_KEY_PUBLISH_DATE).substring(0,10);

                    String title = a.getString(JSON_KEY_WEB_TITLE);
                    String url = a.getString(JSON_KEY_WEB_URL);
                    String sectionName = a.getString(JSON_KEY_SECTION_NAME);

                    String imageUrl=null;
                    if(assets!=null && !assets.isNull(0))
                        imageUrl = assets.getJSONObject(1).getString(JSON_KEY_FILE);

                    adapter.getArticlesList().add(new TheGuardainArticle(title, url, sectionName, bodyText, date,imageUrl));
                    publishProgress((progresBarStatus += 10) + 10);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            publishProgress(100);
            return null;
        }

        /**
         * Updates the listview with downloaded articles information and sets the visibility for the progress bar to invisible
         * @param fromDoInBackground
         */
        @Override
        public void onPostExecute(String fromDoInBackground) {
            listView = findViewById(R.id.the_guardian_list);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener((parent, view, position, id) ->{
                Bundle bundle = new Bundle();
                bundle.putString(ARTICLE_TITLE, adapter.getArticlesList().get(position).getTitle());
                bundle.putString(ARTICLE_BODY_TEXT, adapter.getArticlesList().get(position).getBodyText());
                bundle.putString(ARTICLE_DATE, adapter.getArticlesList().get(position).getDate());
                bundle.putString(ARTICLE_URL, adapter.getArticlesList().get(position).getUrl());
                bundle.putString(ARTICLE_SECTION, adapter.getArticlesList().get(position).getSectionName());
                bundle.putString(ARTICLE_IMAGE_URL, adapter.getArticlesList().get(position).getImageUrl());

                if( findViewById(R.id.the_guardian_article_frame) == null ){
                    Intent goToEmptyFragment = new Intent(TheGuardianMain.this, TheGuardianEmpty.class);
                    goToEmptyFragment.putExtras(bundle);
                    startActivity(goToEmptyFragment);
                } else {
                    TheGuardianFragment fragment = new TheGuardianFragment();
                    fragment.setArguments(bundle);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.the_guardian_article_frame,fragment)
                            .commit();
                }
            });

            listView.setOnItemLongClickListener((parent,view,position,id) -> {
                TheGuardainArticle article = adapter.getArticlesList().get(position);
                String articleTitle = article.getTitle();
                String articleSection = article.getSectionName();
                String articleUrl = article.getUrl();
                String articleImageUrl = article.getImageUrl();
                String articleBodyText = article.getBodyText();
                String articleDate = article.getDate();
                long articleId = adapter.getArticlesList().get(position).getId();
                boolean isFavorite = isAlreadyFavorite(articleTitle);
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(TheGuardianMain.this, R.style.TheGuardianAlertDialog));
                builder.setTitle(isFavorite ? R.string.remove_favorite : R.string.add_favorite)
                        .setMessage(articleTitle)
                        .setCancelable(true)
                        .setNeutralButton(R.string.cancel, (dialog,which)->{
                        })
                        .setPositiveButton(R.string.yes, (dialog, which) -> {
                            if(isFavorite){
                                deleteArticleFromDatabase(articleTitle);
                                adapter.notifyDataSetChanged();
                                Snackbar snackbar = Snackbar
                                        .make(findViewById(android.R.id.content),R.string.removed_favorites, Snackbar.LENGTH_SHORT)
                                        .setAction(R.string.undo,e ->  {
                                            addToDatabase(articleTitle, articleUrl, articleSection, articleBodyText, articleImageUrl, articleDate);
                                            adapter.notifyDataSetChanged();
                                        });
                                snackbar.setActionTextColor(getResources().getColor(R.color.highlight));
                                View snackbarView = snackbar.getView();
                                snackbarView.setBackgroundColor(getResources().getColor(R.color.main));
                                snackbar.show();
                            } else {
                                addToDatabase(articleTitle, articleUrl, articleSection, articleBodyText, articleImageUrl, articleDate);
                                adapter.notifyDataSetChanged();
                                LayoutInflater inflater = getLayoutInflater();
                                View layout = inflater.inflate(R.layout.toast, parent, false);
                                ImageView toastImage = layout.findViewById(R.id.favorite_toast);
                                animateStar(toastImage);
                                Toast toast = new Toast(getApplicationContext());
                                toast.setDuration(Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                                toast.setView(layout);
                                toast.show();
                            }
                        })
                        .setNegativeButton(R.string.no,(dialog, which) -> {
                            adapter.notifyDataSetChanged();
                        });
                AlertDialog dialog  = builder.create();
                dialog.show();
                return true;
            });
        }
    }
}
