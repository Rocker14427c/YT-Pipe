import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Creates the safe, settings-only PipePipe UI preset.
 *
 * The archive deliberately has no newpipe.db entry. The app recognizes the marker entry and
 * merges these SharedPreferences without overwriting history, subscriptions, playlists, cookies,
 * downloads, or other user-owned data.
 */
public final class PipePipeUiPresetGenerator {
    private PipePipeUiPresetGenerator() { }

    private static Set<String> set(final String... values) {
        return new HashSet<>(Arrays.asList(values));
    }

    private static void put(final Map<String, Object> settings,
                            final String key,
                            final Object value) {
        settings.put(key, value);
    }

    private static Map<String, Object> youtubeUiSettings() {
        final Map<String, Object> settings = new HashMap<>();

        // Theme and shell: follow the device, with the same layered night theme as YouTube.
        put(settings, "theme", "auto_device_theme");
        put(settings, "night_theme", "dark_theme");
        put(settings, "main_tabs_position", true);
        put(settings, "saved_tabs_key", "{\"tabs\":[{\"tab_id\":7},{\"tab_id\":1},{\"tab_id\":4}]}");

        // Main surfaces: a phone-friendly feed/list, not the legacy two-column search grid.
        put(settings, "grid_layout_enabled_key", false);
        put(settings, "grid_columns_key", "1");
        put(settings, "grid_columns_landscape_key", "2");
        put(settings, "card_mode_enabled_key", false);
        put(settings, "list_view_mode", "list");
        put(settings, "always_list_mode_for_bookmark_key", true);
        put(settings, "pull_to_refresh_key", true);
        put(settings, "show_thumbnail_key", true);
        put(settings, "download_thumbnail_key", true);

        // Main and watch-page hierarchy.
        put(settings, "show_comments", true);
        put(settings, "show_next_video", true);
        put(settings, "show_description", true);
        put(settings, "show_meta_info", true);
        put(settings, "show_dislike_key", false);
        put(settings, "pin_video_to_top_key", false);
        put(settings, "video_tabs_key", set("comments", "related", "description"));
        put(settings, "channel_tabs", set(
                "show_channel_tabs_shorts",
                "show_channel_tabs_livestreams",
                "show_channel_tabs_playlists",
                "show_channel_tabs_info"));

        // Search: modern filter sheet and both local/network suggestions.
        put(settings, "use_old_search_filter", false);
        put(settings, "use_experimental_new_ui", false);
        put(settings, "show_search_suggestions", set(
                "show_local_search_suggestions",
                "show_remote_search_suggestions"));
        put(settings, "search_suggestions_count", "25");
        put(settings, "enable_search_history", true);
        put(settings, "enable_watch_history", true);

        // Playback behavior that maps cleanly to YouTube's mobile defaults.
        put(settings, "default_resolution", "720p");
        put(settings, "default_popup_resolution", "480p");
        put(settings, "preferred_open_action_key", "show_info");
        put(settings, "minimize_on_exit_key", "minimize_on_exit_background_key");
        put(settings, "autoplay_key", "autoplay_wifi_key");
        put(settings, "auto_queue_key", false);
        put(settings, "dont_auto_queue_long_key", true);
        put(settings, "auto_background_play_key", false);
        put(settings, "random_music_play_mode_key", false);
        put(settings, "sleep_timer_length_key", "15");
        put(settings, "seek_duration", "10000");
        put(settings, "seekbar_preview_thumbnail_key", "seekbar_preview_thumbnail_low_quality");
        put(settings, "start_main_player_fullscreen_key", false);
        put(settings, "always_start_from_beginning_key", false);
        put(settings, "require_audio_focus_key", true);
        put(settings, "resume_on_audio_focus_gain", false);
        put(settings, "use_inexact_seek_key", false);
        put(settings, "show_hold_to_append", true);
        put(settings, "volume_gesture_control", true);
        put(settings, "brightness_gesture_control", true);
        put(settings, "fullscreen_gesture_control", true);
        put(settings, "swipe_seek_gesture_control", true);
        put(settings, "playback_speed_gesture_control", true);
        put(settings, "advanced_formats_key", set("VP9", "HEVC"));
        put(settings, "preferred_audio_language_key", "original");
        put(settings, "show_auto_translated_subtitles", true);
        put(settings, "auto_translated_subtitles_language", "en");

        // Data and regional defaults; account/cookie values are intentionally not included.
        put(settings, "app_language_key", "system");
        put(settings, "content_country", "IN");
        put(settings, "limit_mobile_data_usage", "none");
        put(settings, "downloads_storage_ask", false);
        put(settings, "cross_network_downloads", false);
        put(settings, "downloads_max_retry", "3");

        // PipePipe capabilities stay enabled but present through the shared visual system.
        put(settings, "sponsor_block_enable", true);
        put(settings, "sponsor_block_show_manual_skip", true);
        put(settings, "sponsor_block_graced_rewind", true);
        put(settings, "sponsor_block_notifications", true);
        put(settings, "sponsor_block_category_sponsor", true);
        put(settings, "sponsor_block_category_sponsor_mode", "automatic");
        put(settings, "sponsor_block_category_intro", true);
        put(settings, "sponsor_block_category_intro_mode", "automatic");
        put(settings, "sponsor_block_category_outro", true);
        put(settings, "sponsor_block_category_outro_mode", "automatic");
        put(settings, "sponsor_block_category_interaction", true);
        put(settings, "sponsor_block_category_interaction_mode", "automatic");
        put(settings, "sponsor_block_category_highlight", true);
        put(settings, "sponsor_block_category_self_promo", true);
        put(settings, "sponsor_block_category_promo_mode", "automatic");
        put(settings, "sponsor_block_category_music", true);
        put(settings, "sponsor_block_category_music_mode", "automatic");
        put(settings, "sponsor_block_category_preview", true);
        put(settings, "sponsor_block_category_preview_mode", "automatic");
        put(settings, "sponsor_block_category_filler", true);
        put(settings, "sponsor_block_category_filler_mode", "automatic");

        // Notifications/update defaults.
        put(settings, "notification_colorize_key", true);
        put(settings, "scale_to_square_image_in_notifications", false);
        put(settings, "update_app_key", true);
        put(settings, "show_prerelease_key", false);
        put(settings, "enable_streams_notifications", false);

        return settings;
    }

    private static void addText(final ZipOutputStream zip,
                                final String name,
                                final String text) throws Exception {
        zip.putNextEntry(new ZipEntry(name));
        zip.write(text.getBytes(StandardCharsets.UTF_8));
        zip.closeEntry();
    }

    public static void main(final String[] args) throws Exception {
        if (args.length != 1) {
            throw new IllegalArgumentException("Usage: PipePipeUiPresetGenerator <output.zip>");
        }

        final File outputFile = new File(args[0]);
        final Map<String, Object> settings = youtubeUiSettings();

        final ByteArrayOutputStream serialized = new ByteArrayOutputStream();
        try (ObjectOutputStream objectOutput = new ObjectOutputStream(serialized)) {
            objectOutput.writeObject(settings);
        }

        outputFile.getParentFile().mkdirs();
        try (ZipOutputStream zip = new ZipOutputStream(new FileOutputStream(outputFile))) {
            addText(zip, "pipepipe.ui-preset",
                    "PipePipe UI preset\nversion=1\nprofile=youtube\nmode=settings-only\n");
            addText(zip, "preset-manifest.txt",
                    "PipePipe YouTube-style UI preset\n"
                            + "Applies presentation, navigation, playback, search, history, and SponsorBlock preferences.\n"
                            + "Does not contain or replace newpipe.db.\n"
                            + "Account cookies, download paths, and user data are intentionally excluded.\n");
            zip.putNextEntry(new ZipEntry("newpipe.settings"));
            zip.write(serialized.toByteArray());
            zip.closeEntry();
        }

        System.out.println("Created " + outputFile.getAbsolutePath()
                + " with " + settings.size() + " settings");
    }
}
