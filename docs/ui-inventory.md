# PipePipe UI architecture map

This inventory was created before the redesign. The implementation stays view-based so the extractor, player service, Room database, download manager, and navigation contracts remain intact.

| Surface | Existing implementation | Shared redesign treatment | Notes |
| --- | --- | --- | --- |
| App shell | `MainActivity`, `activity_main.xml`, `toolbar_layout.xml`, drawer resources | neutral YouTube-style top bar, edge-aware surfaces, calmer drawer | keeps service switching and deep links |
| Main navigation | `MainFragment`, `TabsManager`, `fragment_main.xml` | bottom navigation by default, selected red indicator, consistent icon tint | the existing “move tabs to bottom” preference remains supported |
| Feed/search results | `BaseListFragment`, `InfoListAdapter`, `list_stream_*.xml` | shared YouTube spacing, 16:9 rounded thumbnails, two-line titles, secondary metadata hierarchy | actual extractor pages and adapters are unchanged |
| Search | `SearchFragment`, `fragment_search.xml`, `SearchFilterDialog.kt` | modern search bar, result hierarchy, filter sheet using the same tokenized Material 3 palette | advanced PipePipe filters remain available |
| Watch page | `VideoDetailFragment`, `fragment_video_detail.xml`, `fragment_description.xml` | black media stage, action row, channel/description hierarchy, rounded surfaces | playback and comments logic unchanged |
| Player | `Player`, `player.xml`, `player_*` drawables | YouTube-like dark overlay, red progress, larger touch targets, consistent control spacing | Media3/ExoPlayer, SponsorBlock, gestures, PiP, fullscreen remain untouched |
| Channels | `ChannelFragment`, `channel_header.xml`, channel list layouts | centered avatar/header, tabs, consistent cards and empty/loading states | service data and tabs preserved |
| Subscriptions/feed/history | local fragments and existing list adapters | same list/card tokens and overflow affordances | database and notification workers preserved |
| Playlists/downloads | playlist fragments, `DownloadActivity`, GigaGet layouts | surfaces, headers, chips, metadata and row affordances use global tokens | download behavior is not changed |
| Settings | `SettingsActivity`, `BasePreferenceFragment`, `res/xml/*_settings.xml` | neutral toolbar, readable sections, red active controls, theme-aware preference rows | every PipePipe-specific setting remains accessible |
| Dialogs/sheets | AppCompat dialogs, Material bottom sheets, Compose filter dialog | shared surface colors, 18dp sheet corners, red primary action, scrim hierarchy | no functional dialog action was removed |
| Splash/launcher | `OpeningTheme`, `splash_background.xml`, `ic_launcher*` | red play-mark launch treatment with adaptive foreground/background | PipePipe attribution and legal metadata remain in About/License |

## Shared design tokens

The single source of truth for presentation tokens is `app/src/main/res/values/yt_design_system.xml`. Existing legacy resource names are aliases or consumed by service-specific themes so older screens do not drift into a second palette.

- Color roles: light/dark surfaces, primary/secondary text, divider, chip, red action, player overlay.
- Dimensions: toolbar, bottom navigation, thumbnail radius, list rhythm, touch target, sheet radius.
- Styles: `YouTubeToolbarTitle`, `YouTubeVideoTitle`, `YouTubeVideoMeta`, and `YouTubeSettingsRow`.

## Validation map

Build validation covers the `debug` application, Android resource linking, Kotlin/Java compilation, and the included extractor composite build. A device/emulator is not available in this environment, so screenshot comparison is performed against the checked-in reference screenshots and static resource inspection; runtime sign-in, extraction, playback, PiP, and network behavior are intentionally left to the existing test/device matrix.
