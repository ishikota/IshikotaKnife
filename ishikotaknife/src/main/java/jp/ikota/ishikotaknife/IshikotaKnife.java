package jp.ikota.ishikotaknife;


import android.app.Activity;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class IshikotaKnife {

    public static void bind(Activity target) {
        Log.d(IshikotaKnife.class.getCanonicalName(),
                "On binding for class : "+target.getClass().getCanonicalName());
        try {
            final Class clazz =
                    Class.forName(target.getClass().getCanonicalName() + "$$IshikotaViewBinder");
            final Method bind = clazz.getMethod("bind", target.getClass());
            bind.invoke(clazz.newInstance(), target);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
