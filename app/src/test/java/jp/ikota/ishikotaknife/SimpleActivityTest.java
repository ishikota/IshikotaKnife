package jp.ikota.ishikotaknife;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static com.google.common.truth.Truth.assertThat;


@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk=21)
public class SimpleActivityTest {
    @Test
    public void verifyContentViewBinding() {
        SimpleActivity activity = Robolectric.buildActivity(SimpleActivity.class)
                .create()
                .get();

        assertThat(activity.mTextView).isNotNull();
    }

}
