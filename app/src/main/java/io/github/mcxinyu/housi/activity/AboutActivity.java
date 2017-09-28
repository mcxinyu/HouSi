package io.github.mcxinyu.housi.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;

import com.danielstone.materialaboutlibrary.ConvenienceBuilder;
import com.danielstone.materialaboutlibrary.MaterialAboutActivity;
import com.danielstone.materialaboutlibrary.items.MaterialAboutActionItem;
import com.danielstone.materialaboutlibrary.items.MaterialAboutItemOnClickAction;
import com.danielstone.materialaboutlibrary.items.MaterialAboutTitleItem;
import com.danielstone.materialaboutlibrary.model.MaterialAboutCard;
import com.danielstone.materialaboutlibrary.model.MaterialAboutList;
import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.mikepenz.aboutlibraries.util.Colors;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import io.github.mcxinyu.housi.BuildConfig;
import io.github.mcxinyu.housi.R;

/**
 * Created by huangyuefeng on 2017/9/25.
 * Contact me : mcxinyu@gmail.com
 */
public class AboutActivity extends MaterialAboutActivity {
    private static final int iconColorInt = R.color.colorAccent;

    public static Intent newIntent(Context context) {

        Intent intent = new Intent(context, AboutActivity.class);
        // intent.putExtra();
        return intent;
    }

    @NonNull
    @Override
    protected MaterialAboutList getMaterialAboutList(@NonNull final Context context) {
        return createMaterialAboutList(context);
    }

    @Nullable
    @Override
    protected CharSequence getActivityTitle() {
        return getString(R.string.about_title);
    }

    public MaterialAboutList createMaterialAboutList(final Context context) {
        MaterialAboutCard.Builder appCardBuilder = new MaterialAboutCard.Builder();

        appCardBuilder.addItem(new MaterialAboutTitleItem.Builder()
                .text(R.string.app_name)
                .desc(R.string.author_info)
                .icon(R.mipmap.ic_launcher)
                .build());

        appCardBuilder.addItem(ConvenienceBuilder.createVersionActionItem(context,
                new IconicsDrawable(context)
                        .icon(GoogleMaterial.Icon.gmd_info_outline)
                        .color(ContextCompat.getColor(context, iconColorInt))
                        .sizeDp(18),
                getString(R.string.version),
                false));

        appCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("Changelog")
                .icon(new IconicsDrawable(context)
                        .icon(CommunityMaterial.Icon.cmd_history)
                        .color(ContextCompat.getColor(context, iconColorInt))
                        .sizeDp(18))
                .setOnClickAction(new MaterialAboutItemOnClickAction() {
                    @Override
                    public void onClick() {
                        showCustomTab(context, "https://github.com/mcxinyu/HouSi/releases");
                    }
                })
                .build());

        appCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("Licenses")
                .icon(new IconicsDrawable(context)
                        .icon(GoogleMaterial.Icon.gmd_book)
                        .color(ContextCompat.getColor(context, iconColorInt))
                        .sizeDp(18))
                .setOnClickAction(new MaterialAboutItemOnClickAction() {
                    @Override
                    public void onClick() {
                        new LibsBuilder()
                                .withActivityColor(new Colors(getResources().getColor(R.color.colorAccent),
                                        getResources().getColor(R.color.colorAccent)))
                                .withActivityTitle("Licenses")
                                .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
                                .start(context);
                    }
                })
                .build());

        MaterialAboutCard.Builder aboutAppCardBuilder = new MaterialAboutCard.Builder();
        aboutAppCardBuilder.title("About This App");

        aboutAppCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .icon(new IconicsDrawable(context)
                        .icon(CommunityMaterial.Icon.cmd_alpha)
                        .color(ContextCompat.getColor(context, iconColorInt))
                        .sizeDp(18))
                .subTextHtml(getString(R.string.aboutLibraries_description_text))
                .setIconGravity(MaterialAboutActionItem.GRAVITY_TOP)
                .build()
        );

        MaterialAboutCard.Builder authorCardBuilder = new MaterialAboutCard.Builder();
        authorCardBuilder.title("Author");

        authorCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text(R.string.author_info)
                .subText("没有工作室 No own a house")
                .icon(new IconicsDrawable(context)
                        .icon(GoogleMaterial.Icon.gmd_person)
                        .color(ContextCompat.getColor(context, iconColorInt))
                        .sizeDp(18))
                .setOnClickAction(new MaterialAboutItemOnClickAction() {
                    @Override
                    public void onClick() {
                        showCustomTab(context, "https://github.com/mcxinyu");
                    }
                })
                .build());

        authorCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("Fork on GitHub")
                .subText("本应用开源，因为没钱买私库")
                .icon(new IconicsDrawable(context)
                        .icon(CommunityMaterial.Icon.cmd_github_circle)
                        .color(ContextCompat.getColor(context, iconColorInt))
                        .sizeDp(18))
                .setOnClickAction(new MaterialAboutItemOnClickAction() {
                    @Override
                    public void onClick() {
                        showCustomTab(context, BuildConfig.MY_GITHUB_URL);
                    }
                })
                .build());

        MaterialAboutCard.Builder convenienceCardBuilder = new MaterialAboutCard.Builder();

        convenienceCardBuilder.title("Convenience Info");

        // convenienceCardBuilder.addItem(ConvenienceBuilder.createRateActionItem(context,
        //         new IconicsDrawable(context)
        //                 .icon(CommunityMaterial.Icon.cmd_star)
        //                 .color(ContextCompat.getColor(context, iconColorInt))
        //                 .sizeDp(18),
        //         "Rate this app",
        //         null
        // ));

        convenienceCardBuilder.addItem(ConvenienceBuilder.createEmailItem(context,
                new IconicsDrawable(context)
                        .icon(CommunityMaterial.Icon.cmd_email)
                        .color(ContextCompat.getColor(context, iconColorInt))
                        .sizeDp(18),
                "Send an email",
                true,
                "mcxinyu@gmail.com",
                "Question concerning HouSi"));

        convenienceCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("Telegram Group")
                .subText("Google Hosts女装交♂友群")
                .icon(ContextCompat.getDrawable(context, R.drawable.ic_telegram))
                .setOnClickAction(new MaterialAboutItemOnClickAction() {
                    @Override
                    public void onClick() {
                        showCustomTab(context, "https://t.me/googlehosts");
                    }
                })
                .build());

        return new MaterialAboutList(appCardBuilder.build(),
                aboutAppCardBuilder.build(),
                authorCardBuilder.build(),
                convenienceCardBuilder.build());
    }

    private void showCustomTab(Context context, String url) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(getResources().getColor(R.color.colorAccent))
                .setShowTitle(true)
                .addDefaultShareMenuItem();
        CustomTabsIntent intent = builder.build();
        intent.launchUrl(context, Uri.parse(url));
    }
}
