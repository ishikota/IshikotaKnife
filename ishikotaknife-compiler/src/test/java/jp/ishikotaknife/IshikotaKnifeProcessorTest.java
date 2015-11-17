package jp.ishikotaknife;

import com.google.common.base.Joiner;
import com.google.testing.compile.JavaFileObjects;

import org.junit.Test;

import javax.tools.JavaFileObject;

import static com.google.common.truth.Truth.assert_;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

/**
 * Created by kota on 2015/11/17.
 */
public class IshikotaKnifeProcessorTest {

    @Test
    public void testProcessor() {
        JavaFileObject source = JavaFileObjects.forSourceString("test.MyActivity", Joiner.on('\n').join(
                "package test;",
                "import android.app.Activity;",
                "import android.widget.TextView;",
                "import android.widget.ImageView;",
                "import jp.ishikotaknife.IshikotaBind;",
                "public class MyActivity extends Activity {",
                "  @IshikotaBind(1) TextView mTextView;",
                "  @IshikotaBind(2) TextView mTextView2;",
                "  @IshikotaBind(3) ImageView mImageView;",
                "  ImageView mImageView2;",
                "}")
        );

        JavaFileObject expectedSource = JavaFileObjects.forSourceString("test/MyActivity$$IshikotaViewBinder",
                Joiner.on('\n').join(
                        "package test;",
                        "import android.widget.ImageView;",
                        "import android.widget.TextView;",
                        "public class MyActivity$$IshikotaViewBinder {",
                        "  public void bind(MyActivity target) {",
                        "    ((MyActivity)target).mTextView = (TextView)target.findViewById(1);",
                        "    ((MyActivity)target).mTextView2 = (TextView)target.findViewById(2);",
                        "    ((MyActivity)target).mImageView = (ImageView)target.findViewById(3);",
                        "  }",
                        "}"
                ));

        assert_().about(javaSource())
                .that(source)
                .processedWith(new IshikotaKnifeProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(expectedSource);
    }
}
