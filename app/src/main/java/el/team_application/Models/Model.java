package el.team_application.Models;

import android.content.Context;

import el.team_application.Listeners.AfterLoginCallback;
import el.team_application.Listeners.AfterRegisterCallback;


/**
 * Created by ariel-mac on 24/05/2015.
 */

public class Model {

    // our db models
    ModelPARSE modelPARSE = new ModelPARSE();
    ModelSQL modelSQL = new ModelSQL();

    // single-tone db instance - exists only once in the memory
    private final static Model instance = new Model();
    final static int VERSION = 0;

    public static Model getInstance() { return instance; }

    private Model() {
    }

    public void init(Context context){
        //for sql migration checks
    }

    //model login method, creating instance of a table if there isn't
    public void login(String email, String password, final AfterLoginCallback loginCallback) {
        //if MODEL PARSE

        modelPARSE.login(email, password, loginCallback);

    }

    public void register(final AfterRegisterCallback registerCallback)
    {

    }

}